/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import entity.User;
import util.FileUploadUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling avatar upload requests via AJAX
 * 
 * @author SamSon Travel Team
 */
@MultipartConfig(
    maxFileSize = 5242880, // 5MB
    maxRequestSize = 5242880,
    fileSizeThreshold = 0
)
public class UploadAvatarServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(UploadAvatarServlet.class.getName());
    
    // DAO instance
    private final UserDAO userDAO = new UserDAO();
    
    // Session attribute names
    private static final String USER_SESSION_ATTR = "user";
    
    // Upload directory
    private static final String UPLOAD_DIR = "uploads/avatars";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("UploadAvatarServlet POST request received");
        
        // Set response content type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        try {
            // Check session
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(USER_SESSION_ATTR) == null) {
                LOGGER.warning("User not logged in");
                sendJsonResponse(out, false, "Bạn cần đăng nhập để thực hiện thao tác này", null);
                return;
            }
            
            User currentUser = (User) session.getAttribute(USER_SESSION_ATTR);
            
            // Get uploaded file
            Part filePart = request.getPart("avatar");
            
            if (filePart == null || filePart.getSize() == 0) {
                sendJsonResponse(out, false, "Vui lòng chọn file ảnh", null);
                return;
            }
            
            // Validate file
            String validationError = FileUploadUtil.validateImageFile(filePart);
            if (validationError != null) {
                sendJsonResponse(out, false, validationError, null);
                return;
            }
            
            // Get upload path
            String contextPath = request.getServletContext().getRealPath("");
            String uploadPath = contextPath + UPLOAD_DIR;
            
            // Ensure upload directory exists
            if (!FileUploadUtil.ensureDirectoryExists(uploadPath)) {
                sendJsonResponse(out, false, "Không thể tạo thư mục upload", null);
                return;
            }
            
            // Generate unique file name
            String originalFileName = FileUploadUtil.getFileName(filePart);
            String fileName = FileUploadUtil.generateFileName(currentUser.getId(), originalFileName);
            String filePath = uploadPath + "/" + fileName;
            
            // Delete old avatar if exists
            if (currentUser.getAvatarUrl() != null && !currentUser.getAvatarUrl().isEmpty()) {
                String oldAvatarPath = contextPath + currentUser.getAvatarUrl();
                FileUploadUtil.deleteFile(oldAvatarPath);
            }
            
            // Save file
            boolean saved = FileUploadUtil.saveFile(filePart, uploadPath, fileName);
            
            if (!saved) {
                sendJsonResponse(out, false, "Không thể lưu file", null);
                return;
            }
            
            // Update avatar URL in database
            String avatarUrl = "/" + UPLOAD_DIR + "/" + fileName;
            boolean updated = userDAO.updateAvatar(currentUser.getId(), avatarUrl);
            
            if (updated) {
                // Refresh user data from database
                User updatedUser = userDAO.getUserById(currentUser.getId());
                if (updatedUser != null) {
                    // Update session
                    session.setAttribute(USER_SESSION_ATTR, updatedUser);
                    LOGGER.info("Avatar uploaded successfully for user ID: " + currentUser.getId());
                    sendJsonResponse(out, true, "Cập nhật ảnh đại diện thành công", avatarUrl);
                } else {
                    LOGGER.warning("Failed to refresh user data after avatar upload");
                    sendJsonResponse(out, false, "Có lỗi xảy ra khi cập nhật ảnh đại diện", null);
                }
            } else {
                // If database update fails, delete the uploaded file
                FileUploadUtil.deleteFile(filePath);
                LOGGER.warning("Failed to update avatar for user ID: " + currentUser.getId());
                sendJsonResponse(out, false, "Có lỗi xảy ra khi cập nhật ảnh đại diện", null);
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing avatar upload", e);
            sendJsonResponse(out, false, "Có lỗi xảy ra: " + e.getMessage(), null);
        }
    }
    
    /**
     * Send JSON response
     */
    private void sendJsonResponse(PrintWriter out, boolean success, String message, String avatarUrl) {
        String json;
        if (avatarUrl != null) {
            json = String.format("{\"success\": %s, \"message\": \"%s\", \"avatarUrl\": \"%s\"}", 
                               success ? "true" : "false", message, avatarUrl);
        } else {
            json = String.format("{\"success\": %s, \"message\": \"%s\"}", 
                               success ? "true" : "false", message);
        }
        out.print(json);
        out.flush();
    }
}
















