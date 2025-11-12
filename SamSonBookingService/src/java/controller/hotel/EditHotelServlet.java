package controller.hotel;

import dao.HotelDAO;
import dao.ImageDAO;
import entity.Hotel;
import entity.Image;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;

/**
 * Servlet chỉnh sửa khách sạn
 * Dành cho Hotel Manager (role_id = 3)
 */
@WebServlet("/hotel/edit")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class EditHotelServlet extends HttpServlet {

    private static final HotelDAO hotelDAO = new HotelDAO();
    private static final ImageDAO imageDAO = new ImageDAO();
    private static final String UPLOAD_DIR = "uploads/hotels";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Kiểm tra phân quyền
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user.getRoleId() != 3) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        // Lấy ID khách sạn cần sửa
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/hotel/list");
            return;
        }

        try {
            int hotelId = Integer.parseInt(idParam);
            Hotel hotel = hotelDAO.getHotelById(hotelId);

            if (hotel == null) {
                session.setAttribute("error", "Không tìm thấy khách sạn!");
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }

            // Kiểm tra quyền sở hữu
            if (hotel.getManagerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
                return;
            }

            // Load ảnh hiện có
            List<Image> hotelImages = imageDAO.getImagesByEntity("hotel", hotelId);
            
            request.setAttribute("hotel", hotel);
            request.setAttribute("hotelImages", hotelImages);
            request.getRequestDispatcher("/hotel/hotel_edit.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Kiểm tra phân quyền
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user.getRoleId() != 3) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        try {
            // Lấy thông tin từ form
            int hotelId = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String description = request.getParameter("description");

            // Kiểm tra quyền sở hữu
            Hotel existingHotel = hotelDAO.getHotelById(hotelId);
            if (existingHotel == null || existingHotel.getManagerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
                return;
            }

            // Validate
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Tên khách sạn không được để trống!");
                request.setAttribute("hotel", existingHotel);
                request.getRequestDispatcher("/hotel/hotel_edit.jsp").forward(request, response);
                return;
            }

            // Cập nhật thông tin
            Hotel hotel = new Hotel();
            hotel.setId(hotelId);
            hotel.setName(name);
            hotel.setAddress(address);
            hotel.setDescription(description);
            hotel.setManagerId(user.getId());

            // Cập nhật database
            boolean success = hotelDAO.updateHotel(hotel);

            if (success) {
                // Xử lý ảnh mới (nếu có)
                handleMultipleFileUpload(request, hotelId);
                
                session.setAttribute("success", "Cập nhật khách sạn thành công!");
                response.sendRedirect(request.getContextPath() + "/hotel/list");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật!");
                request.setAttribute("hotel", hotel);
                request.getRequestDispatcher("/hotel/hotel_edit.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("/hotel/hotel_edit.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý upload nhiều file ảnh mới
     */
    private void handleMultipleFileUpload(HttpServletRequest request, int hotelId) 
            throws IOException, ServletException {
        
        Collection<Part> fileParts = request.getParts();
        
        System.out.println("=== DEBUG: handleMultipleFileUpload for hotelId=" + hotelId + " ===");
        System.out.println("Total parts received: " + fileParts.size());
        
        // Tính display order tiếp theo
        int existingCount = imageDAO.countImages("hotel", hotelId);
        int displayOrder = existingCount + 1;
        
        int uploadedCount = 0;
        
        for (Part filePart : fileParts) {
            System.out.println("Processing part: name=" + filePart.getName() + ", size=" + filePart.getSize());
            
            // Chỉ xử lý các part là file ảnh (có name="newImages")
            if (filePart.getName().equals("newImages") && filePart.getSize() > 0) {
                
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                
                // Validate file type
                if (!isValidImageFile(fileName)) {
                    continue;
                }
                
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                String newFileName = "hotel_" + hotelId + "_" + System.currentTimeMillis() + fileExtension;

                // Lưu vào thư mục web/uploads (source directory, không bị mất khi clean build)
                String realPath = getServletContext().getRealPath("/");
                // Chuyển từ build/web sang web (source directory)
                String webPath = realPath.replace("build" + File.separator + "web", "web");
                String uploadPath = webPath + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                System.out.println("DEBUG: Saving file to SOURCE directory: " + uploadPath + "/" + newFileName);
                Path filePath = Paths.get(uploadPath, newFileName);
                Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Lưu thông tin vào database
                Image image = new Image();
                image.setEntityType("hotel");
                image.setEntityId(hotelId);
                image.setImageUrl(UPLOAD_DIR + "/" + newFileName);
                image.setPrimary(false); // Ảnh mới không phải primary
                image.setDisplayOrder(displayOrder);
                image.setAltText(fileName);
                
                boolean inserted = imageDAO.insertImage(image);
                System.out.println("Database insert: " + inserted);
                displayOrder++;
                uploadedCount++;
            }
        }
        
        System.out.println("=== Total images uploaded: " + uploadedCount + " ===");
    }
    
    /**
     * Kiểm tra file có phải là ảnh hợp lệ không
     */
    private boolean isValidImageFile(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".jpg") || 
               lowerCaseFileName.endsWith(".jpeg") || 
               lowerCaseFileName.endsWith(".png") || 
               lowerCaseFileName.endsWith(".gif");
    }
}
