/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import dao.ResetTokenDAO;
import entity.User;
import entity.ResetToken;
import util.EmailUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling email verification
 * Processes verification tokens sent via email
 * 
 * @author SamSon Travel Team
 */
public class VerifyEmailServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(VerifyEmailServlet.class.getName());
    
    // DAO instances
    private final UserDAO userDAO = new UserDAO();
    private final ResetTokenDAO resetTokenDAO = new ResetTokenDAO();
    
    // Constants
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String SUCCESS_MESSAGE_ATTR = "successMessage";
    private static final String USER_EMAIL_ATTR = "userEmail";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("VerifyEmailServlet GET request received");
        
        // Get token from request parameter
        String token = request.getParameter("token");
        
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warning("No verification token provided");
            request.setAttribute(ERROR_MESSAGE_ATTR, "Token xác nhận không hợp lệ hoặc đã hết hạn.");
            forwardToVerificationFailed(request, response);
            return;
        }
        
        try {
            // Validate token
            if (!resetTokenDAO.isValidToken(token.trim())) {
                LOGGER.warning("Invalid verification token: " + token);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Token xác nhận không hợp lệ hoặc đã hết hạn.");
                forwardToVerificationFailed(request, response);
                return;
            }
            
            // Get token details
            ResetToken resetToken = resetTokenDAO.getTokenByValue(token.trim());
            if (resetToken == null) {
                LOGGER.warning("Token not found in database: " + token);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Token xác nhận không tồn tại.");
                forwardToVerificationFailed(request, response);
                return;
            }
            
            // Get user details
            User user = userDAO.getUserById(resetToken.getUserId());
            if (user == null) {
                LOGGER.severe("User not found for token: " + token);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Người dùng không tồn tại.");
                forwardToVerificationFailed(request, response);
                return;
            }
            
            // Check if user is already active
            if ("active".equals(user.getStatus())) {
                LOGGER.info("User already active: " + user.getEmail());
                request.setAttribute(SUCCESS_MESSAGE_ATTR, "Tài khoản đã được xác nhận trước đó.");
                request.setAttribute(USER_EMAIL_ATTR, user.getEmail());
                forwardToVerificationSuccess(request, response);
                return;
            }
            
            // Activate user account
            boolean activated = userDAO.activateUser(user.getId());
            if (!activated) {
                LOGGER.severe("Failed to activate user: " + user.getId());
                request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
                forwardToVerificationFailed(request, response);
                return;
            }
            
            // Mark token as used
            boolean tokenMarked = resetTokenDAO.markTokenAsUsed(token.trim());
            if (!tokenMarked) {
                LOGGER.warning("Failed to mark token as used: " + token);
                // Don't fail the process, just log the issue
            }
            
            // Send welcome email
            try {
                EmailUtil.sendWelcomeEmail(user.getEmail(), user.getName());
                LOGGER.info("Welcome email sent to: " + user.getEmail());
            } catch (Exception e) {
                LOGGER.warning("Failed to send welcome email to: " + user.getEmail());
                // Don't fail the process, just log the issue
            }
            
            LOGGER.info("Email verification successful for user: " + user.getEmail());
            
            // Forward to success page
            request.setAttribute(SUCCESS_MESSAGE_ATTR, "Email đã được xác nhận thành công!");
            request.setAttribute(USER_EMAIL_ATTR, user.getEmail());
            forwardToVerificationSuccess(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during email verification process", e);
            request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
            forwardToVerificationFailed(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("VerifyEmailServlet POST request received");
        
        // Handle resend verification email
        String email = request.getParameter("email");
        String action = request.getParameter("action");
        
        if ("resend".equals(action) && email != null && !email.trim().isEmpty()) {
            handleResendVerification(request, response, email.trim());
        } else {
            // Redirect to GET method
            doGet(request, response);
        }
    }
    
    /**
     * Handle resend verification email
     */
    private void handleResendVerification(HttpServletRequest request, HttpServletResponse response, String email)
            throws ServletException, IOException {
        
        LOGGER.info("Resend verification request for email: " + email);
        
        try {
            // Get user by email
            User user = userDAO.getUserByEmail(email.toLowerCase());
            
            if (user == null) {
                LOGGER.warning("Resend verification for non-existent email: " + email);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Email không tồn tại trong hệ thống.");
                forwardToVerificationFailed(request, response);
                return;
            }
            
            // Check if user is already active
            if ("active".equals(user.getStatus())) {
                LOGGER.info("Resend verification for already active user: " + email);
                request.setAttribute(SUCCESS_MESSAGE_ATTR, "Tài khoản đã được xác nhận. Bạn có thể đăng nhập ngay.");
                request.setAttribute(USER_EMAIL_ATTR, email);
                forwardToVerificationSuccess(request, response);
                return;
            }
            
            // Delete old tokens for this user
            resetTokenDAO.deleteTokensByUserId(user.getId());
            
            // Generate new verification token
            String newToken = util.TokenGenerator.generateToken();
            
            // Create new token
            int tokenId = resetTokenDAO.createVerificationToken(user.getId(), newToken);
            
            if (tokenId == -1) {
                LOGGER.severe("Failed to create new verification token for user: " + user.getId());
                request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
                forwardToVerificationFailed(request, response);
                return;
            }
            
            // Send new verification email
            boolean emailSent = EmailUtil.sendVerificationEmail(email, newToken);
            
            if (emailSent) {
                LOGGER.info("Verification email resent successfully to: " + email);
                request.setAttribute(SUCCESS_MESSAGE_ATTR, "Email xác nhận đã được gửi lại. Vui lòng kiểm tra hộp thư.");
                request.setAttribute(USER_EMAIL_ATTR, email);
                forwardToVerificationPending(request, response);
            } else {
                LOGGER.warning("Failed to resend verification email to: " + email);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Không thể gửi email xác nhận. Vui lòng thử lại sau.");
                forwardToVerificationFailed(request, response);
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during resend verification process", e);
            request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
            forwardToVerificationFailed(request, response);
        }
    }
    
    /**
     * Forward to verification success page
     */
    private void forwardToVerificationSuccess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/verification-success.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Forward to verification failed page
     */
    private void forwardToVerificationFailed(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/verification-failed.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Forward to verification pending page
     */
    private void forwardToVerificationPending(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/verification-pending.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Clean up expired tokens (can be called periodically)
     */
    public void cleanupExpiredTokens() {
        try {
            int deletedCount = resetTokenDAO.deleteExpiredTokens();
            LOGGER.info("Cleaned up " + deletedCount + " expired tokens");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up expired tokens", e);
        }
    }
    
    @Override
    public void init() throws ServletException {
        LOGGER.info("VerifyEmailServlet initialized");
        
        // Clean up expired tokens on startup
        cleanupExpiredTokens();
    }
    
    @Override
    public void destroy() {
        LOGGER.info("VerifyEmailServlet destroyed");
    }
}
