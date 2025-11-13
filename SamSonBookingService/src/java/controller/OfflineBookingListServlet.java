package controller;

import dao.BookingDAO;
import dao.OfflineBookingCustomerDAO;
import entity.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * OfflineBookingListServlet - Hiển thị danh sách tất cả booking offline
 * 
 * Chức năng:
 * 1. Lấy tất cả booking offline từ database (có thông tin khách hàng)
 * 2. Hiển thị trong OfflineBookingList.jsp
 */
@WebServlet(name = "OfflineBookingListServlet", urlPatterns = {"/offline-booking-list"})
public class OfflineBookingListServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        try {
            // Lấy tất cả booking offline từ VIEW (có thông tin khách hàng)
            OfflineBookingCustomerDAO historyDAO = new OfflineBookingCustomerDAO();
            List<Map<String, Object>> bookingHistory = historyDAO.getAllBookingHistory();
            
            // Nếu không có dữ liệu từ VIEW, lấy từ bảng Bookings (fallback)
            if (bookingHistory == null || bookingHistory.isEmpty()) {
                BookingDAO bookingDAO = new BookingDAO();
                List<Booking> bookings = bookingDAO.getAllOfflineBookings();
                request.setAttribute("bookings", bookings);
                request.setAttribute("hasCustomerInfo", false);
            } else {
                request.setAttribute("bookingHistory", bookingHistory);
                request.setAttribute("hasCustomerInfo", true);
            }
            
            // Chuyển đến trang danh sách booking
            request.getRequestDispatcher("OfflineBookingList.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách booking offline: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/offline-home");
        }
    }
}

