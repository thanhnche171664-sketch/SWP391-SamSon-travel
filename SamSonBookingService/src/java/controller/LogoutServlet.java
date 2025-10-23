/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import entity.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Servlet for handling user logout
 * Invalidates session and redirects to login page
 * 
 * @author SamSon Travel Team
 */
public class LogoutServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());
    
    // Constants
    private static final String USER_SESSION_ATTR = "user";
    private static final String ROLE_SESSION_ATTR = "role";
    private static final String SUCCESS_MESSAGE_ATTR = "successMessage";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("LogoutServlet GET request received");
        processLogout(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("LogoutServlet POST request received");
        processLogout(request, response);
    }
    
    /**
     * Process logout request
     */
    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Get user information for logging
            User user = (User) session.getAttribute(USER_SESSION_ATTR);
            String userEmail = (user != null) ? user.getEmail() : "Unknown";
            
            // Log logout activity
            LOGGER.info("User logout: " + userEmail + " from IP: " + getClientIPAddress(request));
            
            // Invalidate session
            session.invalidate();
            
            // Set success message
            request.setAttribute(SUCCESS_MESSAGE_ATTR, "Đăng xuất thành công!");
            
            LOGGER.info("Session invalidated for user: " + userEmail);
        } else {
            LOGGER.info("Logout request with no active session");
            request.setAttribute(SUCCESS_MESSAGE_ATTR, "Bạn đã đăng xuất.");
        }
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
    }
    
    /**
     * Get client IP address
     */
    private String getClientIPAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * Handle logout with confirmation
     */
    private void handleLogoutWithConfirmation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String confirm = request.getParameter("confirm");
        
        if ("yes".equals(confirm)) {
            processLogout(request, response);
        } else {
            // Redirect back to previous page or dashboard
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                response.sendRedirect(referer);
            } else {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard.jsp");
            }
        }
    }
    
    /**
     * Force logout all sessions for a user (admin function)
     */
    private void forceLogoutAllSessions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // This would require session management implementation
        // For now, just invalidate current session
        processLogout(request, response);
    }
    
    @Override
    public void init() throws ServletException {
        LOGGER.info("LogoutServlet initialized");
    }
    
    @Override
    public void destroy() {
        LOGGER.info("LogoutServlet destroyed");
    }
}
