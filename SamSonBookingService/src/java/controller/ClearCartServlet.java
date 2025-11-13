package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * ClearCartServlet - Xóa tất cả dữ liệu trong giỏ hàng khi về trang chủ từ hóa đơn
 * 
 * Chức năng:
 * 1. Xóa roomCart, serviceCart, checkinDate, checkoutDate, customerInfo khỏi session
 * 2. Redirect về trang chủ offline
 */
@WebServlet(name = "ClearCartServlet", urlPatterns = {"/clear-cart"})
public class ClearCartServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Lấy session
            HttpSession session = request.getSession(false);
            
            if (session != null) {
                // Xóa tất cả dữ liệu giỏ hàng và thông tin khách hàng
                session.removeAttribute("roomCart");
                session.removeAttribute("serviceCart");
                session.removeAttribute("checkinDate");
                session.removeAttribute("checkoutDate");
                session.removeAttribute("customerInfo");
                
                System.out.println("Đã xóa tất cả dữ liệu giỏ hàng và thông tin khách hàng khỏi session");
            }
            
            // Redirect về trang chủ offline
            response.sendRedirect(request.getContextPath() + "/offline-home");
            
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa giỏ hàng: " + e.getMessage());
            e.printStackTrace();
            // Vẫn redirect về trang chủ dù có lỗi
            response.sendRedirect(request.getContextPath() + "/offline-home");
        }
    }
}

