/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.hotel;
import dao.ImageDAO;
import dao.RoomDAO;
import entity.Image;
import entity.Room;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "AddRoomServlet", urlPatterns = {"/hotel/room/add"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class AddRoomServlet extends HttpServlet {
    private RoomDAO roomDAO = new RoomDAO();
    private ImageDAO imageDAO = new ImageDAO();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hotelIdParam = request.getParameter("hotelId");
        if (hotelIdParam == null || hotelIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/hotel/list");
            return;
        }
        request.getRequestDispatcher("/hotel/room_add.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            String roomType = request.getParameter("roomType");
            double price = Double.parseDouble(request.getParameter("price"));
            int totalRooms = Integer.parseInt(request.getParameter("totalRooms"));
            int availableRooms = Integer.parseInt(request.getParameter("availableRooms"));
            
            if (availableRooms > totalRooms) {
                request.setAttribute("error", "Số phòng còn trống không được lớn hơn tổng số phòng!");
                request.getRequestDispatcher("/hotel/room_add.jsp").forward(request, response);
                return;
            }
            
            Room room = new Room();
            room.setHotelId(hotelId);
            room.setRoomType(roomType);
            room.setPrice(price);
            room.setTotalRooms(totalRooms);
            room.setAvailableRooms(availableRooms);
            
            int roomId = roomDAO.insertRoomAndReturnId(room);
            
            if (roomId > 0) {
                handleMultipleFileUpload(request, roomId);
                response.sendRedirect(request.getContextPath() + "/hotel/detail?id=" + hotelId);
            } else {
                request.setAttribute("error", "Không thể thêm phòng!");
                request.getRequestDispatcher("/hotel/room_add.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("/hotel/room_add.jsp").forward(request, response);
        }
    }
    
    private void handleMultipleFileUpload(HttpServletRequest request, int roomId) throws IOException, ServletException {
        Collection<Part> fileParts = request.getParts();
        
        // Lưu vào thư mục web/uploads (source directory, không bị mất khi clean build)
        String realPath = getServletContext().getRealPath("/");
        // Chuyển từ build/web sang web (source directory)
        String webPath = realPath.replace("build" + java.io.File.separator + "web", "web");
        String uploadPath = webPath + "uploads" + java.io.File.separator + "rooms";
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        System.out.println("DEBUG Room: Saving to " + uploadPath);
        
        boolean isFirstImage = true;
        int displayOrder = 1;
        
        for (Part part : fileParts) {
            if (part.getName().equals("images") && part.getSize() > 0) {
                String fileName = part.getSubmittedFileName();
                String contentType = part.getContentType();
                if (!contentType.startsWith("image/")) continue;
                
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                String uniqueFileName = "room_" + roomId + "_" + System.currentTimeMillis() + fileExtension;
                Path filePath = uploadDir.resolve(uniqueFileName);
                Files.copy(part.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                Image image = new Image();
                image.setEntityType("room");
                image.setEntityId(roomId);
                image.setImageUrl("uploads/rooms/" + uniqueFileName);
                image.setPrimary(isFirstImage);
                image.setDisplayOrder(displayOrder);
                image.setAltText("Room image " + displayOrder);
                imageDAO.insertImage(image);
                
                isFirstImage = false;
                displayOrder++;
            }
        
}
    }
}