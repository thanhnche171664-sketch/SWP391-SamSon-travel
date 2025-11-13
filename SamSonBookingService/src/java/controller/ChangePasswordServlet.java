/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import entity.User;
import util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ValidationUtil;

/**
 * Servlet for handling password change requests via AJAX
 * 
 * @author SamSon Travel Team
 */
public class ChangePasswordServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ChangePasswordServlet.class.getName());
    
    // DAO instance
    private final UserDAO userDAO = new UserDAO();
    
    // Session attribute names
    private static final String USER_SESSION_ATTR = "user";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("ChangePasswordServlet POST request received");
        
        // Set response content type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        try {
            // Check session
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(USER_SESSION_ATTR) == null) {
                LOGGER.warning("User not logged in");
                sendJsonResponse(out, false, "Bạn cần đăng nhập để thực hiện thao tác này");
                return;
            }
            
            User currentUser = (User) session.getAttribute(USER_SESSION_ATTR);
            
            // Get form parameters
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // Validate current password
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                sendJsonResponse(out, false, "Vui lòng nhập mật khẩu hiện tại");
                return;
            }
            
            // Verify current password
            if (!PasswordUtil.verifyPassword(currentPassword, currentUser.getPassword())) {
                sendJsonResponse(out, false, "Mật khẩu hiện tại không đúng");
                return;
            }
            
            // Validate new password
            if (newPassword == null || newPassword.trim().isEmpty()) {
                sendJsonResponse(out, false, "Vui lòng nhập mật khẩu mới");
                return;
            }
            
            if (!ValidationUtil.isValidPassword(newPassword)) {
                String strengthFeedback = ValidationUtil.getPasswordStrength(newPassword);
                sendJsonResponse(out, false, strengthFeedback);
                return;
            }
            
            // Check password confirmation
            if (!newPassword.equals(confirmPassword)) {
                sendJsonResponse(out, false, "Mật khẩu xác nhận không khớp");
                return;
            }
            
            // Check if new password is same as current password
            if (PasswordUtil.verifyPassword(newPassword, currentUser.getPassword())) {
                sendJsonResponse(out, false, "Mật khẩu mới phải khác mật khẩu hiện tại");
                return;
            }
            
            // Hash new password
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            
            // Update password in database
            boolean success = userDAO.updatePassword(currentUser.getId(), hashedPassword);
            
            if (success) {
                // Refresh user data from database
                User updatedUser = userDAO.getUserById(currentUser.getId());
                if (updatedUser != null) {
                    // Update session
                    session.setAttribute(USER_SESSION_ATTR, updatedUser);
                    LOGGER.info("Password changed successfully for user ID: " + currentUser.getId());
                    sendJsonResponse(out, true, "Đổi mật khẩu thành công");
                } else {
                    LOGGER.warning("Failed to refresh user data after password change");
                    sendJsonResponse(out, false, "Có lỗi xảy ra khi đổi mật khẩu");
                }
            } else {
                LOGGER.warning("Failed to change password for user ID: " + currentUser.getId());
                sendJsonResponse(out, false, "Có lỗi xảy ra khi đổi mật khẩu");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing password change", e);
            sendJsonResponse(out, false, "Có lỗi xảy ra: " + e.getMessage());
        }
    }
    
    /**
     * Send JSON response
     */
    private void sendJsonResponse(PrintWriter out, boolean success, String message) {
        String json = String.format("{\"success\": %s, \"message\": \"%s\"}", 
                                   success ? "true" : "false", message);
        out.print(json);
        out.flush();
    }
}
















