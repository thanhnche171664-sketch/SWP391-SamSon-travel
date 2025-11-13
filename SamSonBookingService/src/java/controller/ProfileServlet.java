/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserDAO;
import dao.BookingDAO;
import entity.User;
import entity.Booking;
import entity.BookingDetail;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling user profile page
 * Supports GET requests to display profile with booking history
 * 
 * @author SamSon Travel Team
 */
public class ProfileServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());
    
    // DAO instances
    private final UserDAO userDAO = new UserDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    
    // Session attribute names
    private static final String USER_SESSION_ATTR = "user";
    private static final String BOOKINGS_ATTR = "bookings";
    private static final String BOOKING_COUNT_ATTR = "bookingCount";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("ProfileServlet GET request received");
        
        // Get user from session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(USER_SESSION_ATTR) == null) {
            LOGGER.warning("User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute(USER_SESSION_ATTR);
        
        try {
            // Refresh user data from database
            User user = userDAO.getUserById(currentUser.getId());
            if (user == null) {
                LOGGER.warning("User not found with ID: " + currentUser.getId());
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            // Update user in session
            session.setAttribute(USER_SESSION_ATTR, user);
            
            // Get booking history (first page)
            int userId = user.getId();
            int page = 1;
            int pageSize = 10;
            int offset = (page - 1) * pageSize;
            
            List<Booking> bookings = bookingDAO.getBookingsByUserId(userId, offset, pageSize);
            int totalBookings = bookingDAO.getBookingCount(userId);
            
            // Add booking details to each booking
            for (Booking booking : bookings) {
                List<BookingDetail> details = bookingDAO.getBookingDetails(booking.getId());
                // Store details in booking if needed
                booking.setBookingDetails(details);
            }
            
            // Set attributes for JSP
            request.setAttribute("user", user);
            request.setAttribute(BOOKINGS_ATTR, bookings);
            request.setAttribute(BOOKING_COUNT_ATTR, totalBookings);
            
            // Forward to profile page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/profile.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing profile request", e);
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải trang cá nhân");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error/500.jsp");
            dispatcher.forward(request, response);
        }
    }
}
















