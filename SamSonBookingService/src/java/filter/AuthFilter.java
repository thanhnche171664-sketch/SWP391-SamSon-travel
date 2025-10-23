/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filter;

import entity.User;
import entity.Role;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Authentication filter to protect secured resources
 * Checks if user is logged in and has appropriate permissions
 * 
 * @author SamSon Travel Team
 */
public class AuthFilter implements Filter {
    
    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());
    
    // Session attribute names
    private static final String USER_SESSION_ATTR = "user";
    private static final String ROLE_SESSION_ATTR = "role";
    
    // Public URLs that don't require authentication
    private static final String[] PUBLIC_URLS = {
        "/login",
        "/register", 
        "/verify-email",
        "/verification-pending.jsp",
        "/verification-success.jsp",
        "/verification-failed.jsp",
        "/login.jsp",
        "/register.jsp",
        "/index.html",
        "/assets/",
        "/css/",
        "/js/",
        "/images/"
    };
    
    // Role-based access control
    private static final String ADMIN_ROLE = "Administrator";
    private static final String SERVICE_MANAGER_ROLE = "Service Manager";
    private static final String HOTEL_MANAGER_ROLE = "Hotel Manager";
    private static final String CUSTOMER_ROLE = "Customer";
    private static final String FRONT_OFFICE_ROLE = "Front Office";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AuthFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        LOGGER.info("AuthFilter processing request: " + path);
        
        // Check if URL is public
        if (isPublicURL(path)) {
            LOGGER.info("Public URL, allowing access: " + path);
            chain.doFilter(request, response);
            return;
        }
        
        // Get session
        HttpSession session = httpRequest.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute(USER_SESSION_ATTR) == null) {
            LOGGER.info("No active session, redirecting to login: " + path);
            redirectToLogin(httpRequest, httpResponse, path);
            return;
        }
        
        // Get user and role from session
        User user = (User) session.getAttribute(USER_SESSION_ATTR);
        Role role = (Role) session.getAttribute(ROLE_SESSION_ATTR);
        
        if (user == null || role == null) {
            LOGGER.warning("Invalid session data, redirecting to login: " + path);
            session.invalidate();
            redirectToLogin(httpRequest, httpResponse, path);
            return;
        }
        
        // Check user status
        if (!"active".equals(user.getStatus())) {
            LOGGER.warning("Inactive user attempting to access protected resource: " + user.getEmail());
            session.invalidate();
            redirectToLogin(httpRequest, httpResponse, path);
            return;
        }
        
        // Check role-based access
        if (!hasAccess(role.getRoleName(), path)) {
            LOGGER.warning("Access denied for role " + role.getRoleName() + " to " + path);
            redirectToAccessDenied(httpRequest, httpResponse);
            return;
        }
        
        // Update last activity timestamp
        updateLastActivity(session);
        
        LOGGER.info("Access granted for user " + user.getEmail() + " with role " + role.getRoleName() + " to " + path);
        
        // Continue with the request
        chain.doFilter(request, response);
    }
    
    /**
     * Check if URL is public (doesn't require authentication)
     */
    private boolean isPublicURL(String path) {
        for (String publicUrl : PUBLIC_URLS) {
            if (path.startsWith(publicUrl)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if user has access to the requested resource based on role
     */
    private boolean hasAccess(String roleName, String path) {
        switch (roleName) {
            case ADMIN_ROLE:
                return true; // Admin has access to everything
                
            case SERVICE_MANAGER_ROLE:
                return path.startsWith("/service-manager/") || 
                       path.startsWith("/customer/") ||
                       path.equals("/dashboard.jsp") ||
                       path.equals("/profile.jsp");
                       
            case HOTEL_MANAGER_ROLE:
                return path.startsWith("/hotel-manager/") || 
                       path.startsWith("/customer/") ||
                       path.equals("/dashboard.jsp") ||
                       path.equals("/profile.jsp");
                       
            case FRONT_OFFICE_ROLE:
                return path.startsWith("/front-office/") || 
                       path.startsWith("/customer/") ||
                       path.equals("/dashboard.jsp") ||
                       path.equals("/profile.jsp");
                       
            case CUSTOMER_ROLE:
                return path.startsWith("/customer/") ||
                       path.equals("/dashboard.jsp") ||
                       path.equals("/profile.jsp") ||
                       path.startsWith("/booking/") ||
                       path.startsWith("/payment/");
                       
            default:
                return false;
        }
    }
    
    /**
     * Redirect to login page with return URL
     */
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response, String originalPath)
            throws IOException {
        
        String loginURL = request.getContextPath() + "/login";
        if (originalPath != null && !originalPath.isEmpty()) {
            loginURL += "?returnUrl=" + java.net.URLEncoder.encode(originalPath, "UTF-8");
        }
        
        response.sendRedirect(loginURL);
    }
    
    /**
     * Redirect to access denied page
     */
    private void redirectToAccessDenied(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
    }
    
    /**
     * Update last activity timestamp in session
     */
    private void updateLastActivity(HttpSession session) {
        session.setAttribute("lastActivity", System.currentTimeMillis());
    }
    
    /**
     * Check if session has expired based on last activity
     */
    private boolean isSessionExpired(HttpSession session) {
        Long lastActivity = (Long) session.getAttribute("lastActivity");
        if (lastActivity == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        long sessionTimeout = session.getMaxInactiveInterval() * 1000L; // Convert to milliseconds
        
        return (currentTime - lastActivity) > sessionTimeout;
    }
    
    /**
     * Log security events
     */
    private void logSecurityEvent(String event, String userEmail, String path, String ipAddress) {
        String logMessage = String.format("Security Event - %s: User=%s, Path=%s, IP=%s", 
                                        event, userEmail, path, ipAddress);
        LOGGER.info(logMessage);
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
    
    @Override
    public void destroy() {
        LOGGER.info("AuthFilter destroyed");
    }
}
