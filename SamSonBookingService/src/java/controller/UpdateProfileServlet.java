/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import entity.User;
import util.ValidationUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling profile update requests via AJAX
 * Follows best practices with comprehensive validation and error handling
 * 
 * @author SamSon Travel Team
 */
public class UpdateProfileServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(UpdateProfileServlet.class.getName());
    
    // DAO instance
    private final UserDAO userDAO = new UserDAO();
    
    // Session attribute names
    private static final String USER_SESSION_ATTR = "user";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("UpdateProfileServlet POST request received");
        
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
            LOGGER.info("Processing update profile for user: " + currentUser.getId());
            
            // Set character encoding FIRST before reading parameters
            try {
                request.setCharacterEncoding("UTF-8");
            } catch (Exception e) {
                LOGGER.warning("Failed to set character encoding: " + e.getMessage());
            }
            
            // Get all parameters for debugging
            LOGGER.info("=== PARAMETERS DEBUG ===");
            java.util.Enumeration<String> paramNames = request.getParameterNames();
            int paramCount = 0;
            while (paramNames.hasMoreElements()) {
                paramCount++;
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                LOGGER.info("  " + paramName + " = [" + paramValue + "]");
            }
            LOGGER.info("Total parameters received: " + paramCount);
            LOGGER.info("=== END DEBUG ===");
            
            // Get form parameters
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            
            LOGGER.info("Raw name from request: '" + name + "'");
            
            // Validate name
            if (name == null || name.trim().isEmpty()) {
                LOGGER.warning("Name parameter is null or empty. Total params: " + paramCount);
                if (paramCount == 0) {
                    sendJsonResponse(out, false, "Không nhận được dữ liệu từ form. Có thể do vấn đề về encoding hoặc form submission.");
                } else {
                    sendJsonResponse(out, false, "Tên không được để trống");
                }
                return;
            }
            
            String trimmedName = name.trim();
            LOGGER.info("Name after trim: '" + trimmedName + "'");
            
            if (trimmedName.isEmpty()) {
                LOGGER.warning("Name is empty after trimming");
                sendJsonResponse(out, false, "Tên không được để trống");
                return;
            }
            
            if (trimmedName.length() < 2 || trimmedName.length() > 50) {
                LOGGER.warning("Name length is invalid: " + trimmedName.length());
                sendJsonResponse(out, false, "Tên phải có từ 2 đến 50 ký tự");
                return;
            }
            
            // Validate phone (optional field)
            if (phone != null && !phone.trim().isEmpty()) {
                String trimmedPhone = phone.trim();
                if (!ValidationUtil.isValidPhone(trimmedPhone)) {
                    LOGGER.warning("Invalid phone: " + trimmedPhone);
                    sendJsonResponse(out, false, "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 chữ số, bắt đầu bằng 0)");
                    return;
                }
            }
            
            // Validate gender (optional field)
            if (gender != null && !gender.trim().isEmpty()) {
                String trimmedGender = gender.trim().toLowerCase();
                if (!ValidationUtil.isValidGender(trimmedGender)) {
                    LOGGER.warning("Invalid gender: " + trimmedGender);
                    sendJsonResponse(out, false, "Giới tính không hợp lệ");
                    return;
                }
            }
            
            // Update profile in database
            boolean success = userDAO.updateUserProfile(
                currentUser.getId(),
                trimmedName,
                phone != null ? phone.trim() : "",
                gender != null ? gender.trim().toLowerCase() : "",
                address != null ? address.trim() : ""
            );
            
            if (success) {
                // Refresh user data from database
                User updatedUser = userDAO.getUserById(currentUser.getId());
                if (updatedUser != null) {
                    // Update session
                    session.setAttribute(USER_SESSION_ATTR, updatedUser);
                    LOGGER.info("Profile updated successfully for user ID: " + currentUser.getId());
                    sendJsonResponse(out, true, "Cập nhật thông tin thành công");
                } else {
                    LOGGER.warning("Failed to refresh user data after update");
                    sendJsonResponse(out, false, "Có lỗi xảy ra khi cập nhật thông tin");
                }
            } else {
                LOGGER.warning("Failed to update profile for user ID: " + currentUser.getId());
                sendJsonResponse(out, false, "Có lỗi xảy ra khi cập nhật thông tin");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing profile update", e);
            sendJsonResponse(out, false, "Có lỗi xảy ra: " + e.getMessage());
        }
    }
    
    /**
     * Send JSON response with proper escaping
     */
    private void sendJsonResponse(PrintWriter out, boolean success, String message) {
        // Escape quotes and special characters for JSON
        String escapedMessage = message
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
        
        String json = String.format("{\"success\": %s, \"message\": \"%s\"}", 
                                   success ? "true" : "false", escapedMessage);
        out.print(json);
        out.flush();
    }
}
