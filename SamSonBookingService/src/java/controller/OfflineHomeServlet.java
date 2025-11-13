package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OfflineHomeServlet - Trang chủ cho hệ thống đặt phòng/dịch vụ offline
 * 
 * Chức năng:
 * Hiển thị trang chủ với các chức năng:
 * 1. Đặt phòng
 * 2. Đặt dịch vụ
 * 3. Thống kê đơn giản
 */
@WebServlet(name = "OfflineHomeServlet", urlPatterns = {"/offline-home", "/offline"})
public class OfflineHomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        try {
            // Chuyển đến trang JSP
            request.getRequestDispatcher("OfflineHome.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}





