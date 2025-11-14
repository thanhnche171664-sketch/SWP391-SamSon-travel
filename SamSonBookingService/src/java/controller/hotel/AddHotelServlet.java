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

/**
 * Servlet thêm mới khách sạn
 * Dành cho Hotel Manager (role_id = 3)
 */
@WebServlet("/hotel/add")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class AddHotelServlet extends HttpServlet {

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

        // Hiển thị form thêm mới
        request.getRequestDispatcher("/hotel/hotel_add.jsp").forward(request, response);
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
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String description = request.getParameter("description");

            // Validate
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Tên khách sạn không được để trống!");
                request.getRequestDispatcher("/hotel/hotel_add.jsp").forward(request, response);
                return;
            }

            // Validate address (có thể null)
            if (address == null) {
                address = "";
            }
            if (description == null) {
                description = "";
            }

            // Tạo đối tượng Hotel
            Hotel hotel = new Hotel();
            hotel.setName(name.trim());
            hotel.setAddress(address.trim());
            hotel.setDescription(description.trim());
            // Không set manager_id - để NULL hoặc 0
            hotel.setManagerId(0); // Hoặc có thể để NULL nếu database cho phép
            
            // Debug logging
            System.out.println("=== AddHotelServlet Debug ===");
            System.out.println("Hotel Name: " + hotel.getName());
            System.out.println("Hotel Address: " + hotel.getAddress());
            System.out.println("Hotel Description: " + (hotel.getDescription() != null ? hotel.getDescription().substring(0, Math.min(50, hotel.getDescription().length())) : "NULL"));
            System.out.println("Manager ID: NOT SET (0 or NULL)");

            // Lưu vào database và lấy ID
            int hotelId = hotelDAO.insertHotel(hotel);

            if (hotelId > 0) {
                System.out.println("✅ Hotel inserted successfully with ID: " + hotelId);
                
                // Xử lý upload nhiều ảnh
                handleMultipleFileUpload(request, hotelId);
                
                // Kiểm tra lại hotel vừa tạo
                Hotel createdHotel = hotelDAO.getHotelById(hotelId);
                if (createdHotel != null) {
                    System.out.println("✅ Hotel verified in database: " + createdHotel.getName());
                } else {
                    System.out.println("⚠️ WARNING: Hotel not found after insert! ID: " + hotelId);
                }
                
                session.setAttribute("success", "Thêm khách sạn thành công!");
                session.setAttribute("successMessage", "Khách sạn đã được thêm vào hệ thống và sẽ hiển thị trên trang công khai.");
                response.sendRedirect(request.getContextPath() + "/hotel/list");
            } else {
                System.err.println("❌ ERROR: Failed to insert hotel");
                request.setAttribute("error", "Có lỗi xảy ra khi thêm khách sạn vào database!");
                request.getRequestDispatcher("/hotel/hotel_add.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("/hotel/hotel_add.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý upload nhiều file ảnh
     */
    private void handleMultipleFileUpload(HttpServletRequest request, int hotelId) 
            throws IOException, ServletException {
        
        Collection<Part> fileParts = request.getParts();
        int displayOrder = 1;
        boolean isFirstImage = true;
        
        for (Part filePart : fileParts) {
            // Chỉ xử lý các part là file ảnh (có name="images")
            if (filePart.getName().equals("images") && filePart.getSize() > 0) {
                
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                
                // Validate file type
                if (!isValidImageFile(fileName)) {
                    continue;
                }
                
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                String newFileName = "hotel_" + hotelId + "_" + System.currentTimeMillis() + fileExtension;

                // Lấy đường dẫn thực tế của thư mục upload
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Lưu file
                Path filePath = Paths.get(uploadPath, newFileName);
                Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Lưu thông tin vào database
                Image image = new Image();
                image.setEntityType("hotel");
                image.setEntityId(hotelId);
                image.setImageUrl(UPLOAD_DIR + "/" + newFileName);
                image.setPrimary(isFirstImage); // Ảnh đầu tiên là primary
                image.setDisplayOrder(displayOrder);
                image.setAltText(fileName);
                
                imageDAO.insertImage(image);
                
                isFirstImage = false;
                displayOrder++;
            }
        }
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
