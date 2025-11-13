/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import dao.ResetTokenDAO;
import entity.User;
import entity.FormData;
import util.PasswordUtil;
import util.EmailUtil;
import util.TokenGenerator;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling user registration
 * Supports both GET (display registration form) and POST (process registration)
 * 
 * @author SamSon Travel Team
 */
public class RegisterServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    
    // DAO instances
    private final UserDAO userDAO = new UserDAO();
    private final ResetTokenDAO resetTokenDAO = new ResetTokenDAO();
    
    // Servlet lifecycle state
    private volatile boolean servletDestroyed = false;
    
    // Constants
    private static final int CUSTOMER_ROLE_ID = 4; // Customer role ID
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String SUCCESS_MESSAGE_ATTR = "successMessage";
    private static final String FORM_DATA_ATTR = "formData";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if servlet is being destroyed
        if (servletDestroyed) {
            LOGGER.warning("RegisterServlet GET request received but servlet is being destroyed");
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Service temporarily unavailable");
            return;
        }
        
        LOGGER.info("RegisterServlet GET request received");
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            LOGGER.info("User already logged in, redirecting to dashboard");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        // Forward to registration page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if servlet is being destroyed
        if (servletDestroyed) {
            LOGGER.warning("RegisterServlet POST request received but servlet is being destroyed");
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Service temporarily unavailable");
            return;
        }
        
        LOGGER.info("RegisterServlet POST request received");
        
        // Get form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        
        // Validate input
        String validationError = validateRegistrationInput(name, email, phone, password, confirmPassword, gender);
        
        if (validationError != null) {
            LOGGER.warning("Registration validation failed: " + validationError);
            request.setAttribute(ERROR_MESSAGE_ATTR, validationError);
            setFormData(request, name, email, phone, gender, address);
            forwardToRegister(request, response);
            return;
        }
        
        try {
            // Check if email already exists
            if (userDAO.checkEmailExists(email.trim().toLowerCase())) {
                LOGGER.warning("Registration attempt with existing email: " + email);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Email này đã được sử dụng. Vui lòng chọn email khác.");
                setFormData(request, name, email, phone, gender, address);
                forwardToRegister(request, response);
                return;
            }
            
            // Hash password
            String hashedPassword = PasswordUtil.hashPassword(password);
            
            // Create user object
            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPhone(phone != null ? phone.trim() : null);
            user.setPassword(hashedPassword);
            user.setGender(gender != null ? gender.trim().toLowerCase() : null);
            user.setAddress(address != null ? address.trim() : null);
            user.setRoleId(CUSTOMER_ROLE_ID);
            user.setStatus("pending"); // Pending verification
            
            // Save user to database
            int userId = userDAO.createUser(user);
            
            if (userId == -1) {
                LOGGER.severe("Failed to create user in database");
                request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
                setFormData(request, name, email, phone, gender, address);
                forwardToRegister(request, response);
                return;
            }
            
            // Generate verification token
            String verificationToken = TokenGenerator.generateToken();
            
            // Save token to database
            int tokenId = resetTokenDAO.createVerificationToken(userId, verificationToken);
            
            if (tokenId == -1) {
                LOGGER.severe("Failed to create verification token for user: " + userId);
                // Clean up user if token creation fails
                userDAO.deactivateUser(userId);
                request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
                setFormData(request, name, email, phone, gender, address);
                forwardToRegister(request, response);
                return;
            }
            
            // Send verification email
            boolean emailSent = EmailUtil.sendVerificationEmail(email, verificationToken);
            
            if (!emailSent) {
                LOGGER.warning("Failed to send verification email to: " + email);
                // Don't fail registration, just log the issue
            }
            
            LOGGER.info("User registered successfully: " + email + " with ID: " + userId);
            
            // Redirect to verification pending page
            request.setAttribute(SUCCESS_MESSAGE_ATTR, "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận tài khoản.");
            request.setAttribute("userEmail", email);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/verification-pending.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration process", e);
            request.setAttribute(ERROR_MESSAGE_ATTR, "Lỗi hệ thống. Vui lòng thử lại sau.");
            setFormData(request, name, email, phone, gender, address);
            forwardToRegister(request, response);
        }
    }
    
    /**
     * Validate registration input
     */
    private String validateRegistrationInput(String name, String email, String phone, 
                                          String password, String confirmPassword, String gender) {
        
        // Check required fields
        if (name == null || name.trim().isEmpty()) {
            return "Vui lòng nhập họ tên";
        }
        
        if (email == null || email.trim().isEmpty()) {
            return "Vui lòng nhập email";
        }
        
        if (password == null || password.trim().isEmpty()) {
            return "Vui lòng nhập mật khẩu";
        }
        
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return "Vui lòng xác nhận mật khẩu";
        }
        
        // Validate name length
        if (name.trim().length() < 2 || name.trim().length() > 50) {
            return "Họ tên phải có từ 2 đến 50 ký tự";
        }
        
        // Validate email format
        if (!isValidEmail(email.trim())) {
            return "Email không đúng định dạng";
        }
        
        // Validate password strength
        if (!PasswordUtil.isValidPassword(password)) {
            return "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt";
        }
        
        // Check password confirmation
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp";
        }
        
        // Validate phone if provided
        if (phone != null && !phone.trim().isEmpty()) {
            if (!isValidPhone(phone.trim())) {
                return "Số điện thoại không đúng định dạng";
            }
        }
        
        // Validate gender if provided
        if (gender != null && !gender.trim().isEmpty()) {
            String genderLower = gender.trim().toLowerCase();
            if (!genderLower.equals("male") && !genderLower.equals("female") && !genderLower.equals("other")) {
                return "Giới tính không hợp lệ";
            }
        }
        
        return null; // No validation errors
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
     * Validate phone number format
     */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Phone is optional
        }
        
        // Vietnamese phone number format
        String phoneRegex = "^(\\+84|84|0)[1-9][0-9]{8,9}$";
        return phone.matches(phoneRegex);
    }
    
    /**
     * Set form data for redisplay
     */
    private void setFormData(HttpServletRequest request, String name, String email, 
                           String phone, String gender, String address) {
        
        // Check if servlet is being destroyed to avoid IllegalStateException
        if (servletDestroyed) {
            LOGGER.warning("Attempted to set form data but servlet is being destroyed");
            return;
        }
        
        try {
            request.setAttribute(FORM_DATA_ATTR, new FormData(name, email, phone, gender, address));
        } catch (IllegalStateException e) {
            LOGGER.warning("Failed to set form data due to servlet lifecycle issue: " + e.getMessage());
            // Don't rethrow the exception, just log it and continue
        } catch (Exception e) {
            LOGGER.warning("Unexpected error setting form data: " + e.getMessage());
            // Don't rethrow the exception, just log it and continue
        }
    }
    
    /**
     * Forward to registration page
     */
    private void forwardToRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    public void init() throws ServletException {
        LOGGER.info("RegisterServlet initialized");
    }
    
    @Override
    public void destroy() {
        LOGGER.info("RegisterServlet destroy() called - marking servlet as destroyed");
        servletDestroyed = true;
        LOGGER.info("RegisterServlet destroyed");
    }
}
