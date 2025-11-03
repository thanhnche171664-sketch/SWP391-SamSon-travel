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
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "EditRoomServlet", urlPatterns = {"/hotel/room/edit"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class EditRoomServlet extends HttpServlet {
    private RoomDAO roomDAO = new RoomDAO();
    private ImageDAO imageDAO = new ImageDAO();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int roomId = Integer.parseInt(request.getParameter("id"));
            Room room = roomDAO.getRoomById(roomId);
            if (room == null) {
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }
            List<Image> roomImages = imageDAO.getImagesByEntity("room", roomId);
            request.setAttribute("room", room);
            request.setAttribute("roomImages", roomImages);
            request.getRequestDispatcher("/hotel/room_edit.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Note: Image delete and setPrimary operations are now handled by RoomImageServlet (/room/images)
        
        try {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            String uploadOnly = request.getParameter("uploadOnly");
            
            if ("true".equals(uploadOnly)) {
                handleMultipleFileUpload(request, roomId);
                response.sendRedirect(request.getContextPath() + "/hotel/room/edit?id=" + roomId + "&hotelId=" + hotelId);
                return;
            }
            
            String roomType = request.getParameter("roomType");
            double price = Double.parseDouble(request.getParameter("price"));
            int totalRooms = Integer.parseInt(request.getParameter("totalRooms"));
            int availableRooms = Integer.parseInt(request.getParameter("availableRooms"));
            
            if (availableRooms > totalRooms) {
                request.setAttribute("error", "Số phòng còn trống không được lớn hơn tổng số phòng!");
                doGet(request, response);
                return;
            }
            
            Room room = new Room();
            room.setId(roomId);
            room.setHotelId(hotelId);
            room.setRoomType(roomType);
            room.setPrice(price);
            room.setTotalRooms(totalRooms);
            room.setAvailableRooms(availableRooms);
            
            boolean success = roomDAO.updateRoom(room);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/hotel/detail?id=" + hotelId);
            } else {
                request.setAttribute("error", "Không thể cập nhật phòng!");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            doGet(request, response);
        }
    }
    
    private void handleMultipleFileUpload(HttpServletRequest request, int roomId) throws IOException, ServletException {
        Collection<Part> fileParts = request.getParts();
        String uploadPath = getServletContext().getRealPath("") + "uploads" + java.io.File.separator + "rooms";
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        Image primaryImage = imageDAO.getPrimaryImage("room", roomId);
        boolean isFirstImage = (primaryImage == null);
        List<Image> existingImages = imageDAO.getImagesByEntity("room", roomId);
        int displayOrder = existingImages.size() + 1;
        
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
