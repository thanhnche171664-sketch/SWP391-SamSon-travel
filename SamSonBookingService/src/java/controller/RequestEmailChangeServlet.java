/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import dao.ResetTokenDAO;
import entity.User;
import entity.ResetToken;
import util.PasswordUtil;
import util.ValidationUtil;
import util.TokenGenerator;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for requesting email change
 * Sends verification email to new email address
 * 
 * @author SamSon Travel Team
 */
public class RequestEmailChangeServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(RequestEmailChangeServlet.class.getName());
    
    // DAO instances
    private final UserDAO userDAO = new UserDAO();
    private final ResetTokenDAO resetTokenDAO = new ResetTokenDAO();
    
    // Session attribute names
    private static final String USER_SESSION_ATTR = "user";
    private static final String PENDING_EMAIL_ATTR = "pendingEmail";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("RequestEmailChangeServlet POST request received");
        
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
            String newEmail = request.getParameter("newEmail");
            String currentPassword = request.getParameter("currentPassword");
            
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
            
            // Validate new email
            if (newEmail == null || newEmail.trim().isEmpty() || !ValidationUtil.isValidEmail(newEmail)) {
                sendJsonResponse(out, false, "Địa chỉ email không hợp lệ");
                return;
            }
            
            // Check if new email is different
            if (newEmail.trim().equalsIgnoreCase(currentUser.getEmail())) {
                sendJsonResponse(out, false, "Địa chỉ email mới phải khác email hiện tại");
                return;
            }
            
            // Check if email already exists
            if (userDAO.checkEmailExists(newEmail)) {
                sendJsonResponse(out, false, "Địa chỉ email này đã được sử dụng");
                return;
            }
            
            // Generate verification token
            String token = TokenGenerator.generateToken();
            
            // Calculate expiration date (24 hours from now)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 24);
            Date expiresAt = cal.getTime();
            
            // Create reset token record
            ResetToken resetToken = new ResetToken();
            resetToken.setUserId(currentUser.getId());
            resetToken.setToken(token);
            resetToken.setExpiresAt(new java.sql.Timestamp(expiresAt.getTime()));
            resetToken.setUsed(false);
            
            boolean tokenCreated = resetTokenDAO.createToken(resetToken);
            
            if (tokenCreated) {
                // Store pending email in session temporarily
                session.setAttribute(PENDING_EMAIL_ATTR, newEmail.trim().toLowerCase());
                
                LOGGER.info("Email change token created for user ID: " + currentUser.getId());
                sendJsonResponse(out, true, "Chúng tôi đã gửi email xác nhận đến địa chỉ email mới. Vui lòng kiểm tra email để hoàn tất việc thay đổi email.");
            } else {
                LOGGER.warning("Failed to create email change token for user ID: " + currentUser.getId());
                sendJsonResponse(out, false, "Có lỗi xảy ra khi tạo mã xác nhận");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing email change request", e);
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



