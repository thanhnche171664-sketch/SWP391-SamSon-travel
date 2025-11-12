/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import dao.RoleDAO;
import entity.User;
import entity.Role;
import util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling user login
 * Supports both GET (display login page) and POST (process login)
 * 
 * @author SamSon Travel Team
 */
public class LoginServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    
    // DAO instances
    private final UserDAO userDAO = new UserDAO();
    private final RoleDAO roleDAO = new RoleDAO();
    
    // Session attribute names
    private static final String USER_SESSION_ATTR = "user";
    private static final String ROLE_SESSION_ATTR = "role";
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String SUCCESS_MESSAGE_ATTR = "successMessage";
    
    // Redirect URLs based on roles
    private static final String ADMIN_DASHBOARD = "/admin/dashboard.jsp";
    private static final String SERVICE_MANAGER_DASHBOARD = "/wellness-list";
    private static final String HOTEL_MANAGER_DASHBOARD = "/hotel/list";
    private static final String CUSTOMER_DASHBOARD = "/home";
    private static final String FRONT_OFFICE_DASHBOARD = "/front-office/dashboard.jsp";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("LoginServlet GET request received");
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(USER_SESSION_ATTR) != null) {
            LOGGER.info("User already logged in, redirecting to dashboard");
            redirectToDashboard(request, response, session);
            return;
        }
        
        // Forward to login page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("LoginServlet POST request received");
        
        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        // Validate input
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            
            LOGGER.warning("Empty email or password provided");
            request.setAttribute(ERROR_MESSAGE_ATTR, "Vui lòng nhập đầy đủ email và mật khẩu");
            forwardToLogin(request, response);
            return;
        }
        
        try {
            // Get user by email
            User user = userDAO.getUserByEmail(email.trim().toLowerCase());
            
            if (user == null) {
                LOGGER.warning("Login attempt with non-existent email: " + email);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Email hoặc mật khẩu không đúng");
                forwardToLogin(request, response);
                return;
            }
            
            // Check user status
            if (!"active".equals(user.getStatus())) {
                LOGGER.warning("Login attempt with inactive account: " + email);
                String statusMessage = getStatusMessage(user.getStatus());
                request.setAttribute(ERROR_MESSAGE_ATTR, statusMessage);
                forwardToLogin(request, response);
                return;
            }
            
            // Verify password
            if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
                LOGGER.warning("Login attempt with wrong password for email: " + email);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Email hoặc mật khẩu không đúng");
                forwardToLogin(request, response);
                return;
            }
            
            // Get user role
            Role role = roleDAO.getRoleById(user.getRoleId());
            if (role == null) {
                LOGGER.severe("Role not found for user: " + user.getId());
                request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
                forwardToLogin(request, response);
                return;
            }
            
            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute(USER_SESSION_ATTR, user);
            session.setAttribute(ROLE_SESSION_ATTR, role);
            
            // Set session timeout
            if ("on".equals(rememberMe)) {
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
                LOGGER.info("Remember me enabled for user: " + user.getEmail());
            } else {
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
            }
            
            LOGGER.info("User logged in successfully: " + user.getEmail() + " with role: " + role.getRoleName());
            
            // Redirect to appropriate dashboard
            redirectToDashboard(request, response, session);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login process", e);
            request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
            forwardToLogin(request, response);
        }
    }
    
    /**
     * Forward to login page with error message
     */
    private void forwardToLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Redirect to appropriate dashboard based on user role
     */
    private void redirectToDashboard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        
        Role role = (Role) session.getAttribute(ROLE_SESSION_ATTR);
        if (role == null) {
            LOGGER.warning("No role found in session, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String dashboardUrl = getDashboardUrl(role.getRoleName());
        LOGGER.info("Redirecting to dashboard: " + dashboardUrl);
        response.sendRedirect(request.getContextPath() + dashboardUrl);
    }
    
    /**
     * Get dashboard URL based on role name
     */
    private String getDashboardUrl(String roleName) {
        switch (roleName.toLowerCase()) {
            case "administrator":
                return ADMIN_DASHBOARD;
            case "service manager":
                return SERVICE_MANAGER_DASHBOARD;
            case "hotel manager":
                return HOTEL_MANAGER_DASHBOARD;
            case "customer":
                return CUSTOMER_DASHBOARD;
            case "front office":
                return FRONT_OFFICE_DASHBOARD;
            default:
                LOGGER.warning("Unknown role: " + roleName + ", redirecting to customer dashboard");
                return CUSTOMER_DASHBOARD;
        }
    }
    
    /**
     * Get appropriate message based on user status
     */
    private String getStatusMessage(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return "Tài khoản chưa được xác nhận. Vui lòng kiểm tra email để xác nhận tài khoản.";
            case "inactive":
                return "Tài khoản đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.";
            case "suspended":
                return "Tài khoản đã bị tạm khóa. Vui lòng liên hệ quản trị viên.";
            default:
                return "Tài khoản không thể đăng nhập. Vui lòng liên hệ quản trị viên.";
        }
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Log login attempt for security monitoring
     */
    private void logLoginAttempt(String email, String ipAddress, boolean success) {
        String logMessage = String.format("Login attempt - Email: %s, IP: %s, Success: %s", 
                                        email, ipAddress, success);
        
        if (success) {
            LOGGER.info(logMessage);
        } else {
            LOGGER.warning(logMessage);
        }
    }
    
    @Override
    public void init() throws ServletException {
        LOGGER.info("LoginServlet initialized");
    }
    
    @Override
    public void destroy() {
        LOGGER.info("LoginServlet destroyed");
    }
}
