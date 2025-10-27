package controller;

import dao.RoomDAO;
import entity.Room;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet xử lý danh sách phòng
 * Hỗ trợ offline booking (lễ tân tại quầy) - chọn phòng cho khách
 * 
 * @author SamSon Travel Team
 */
@WebServlet(name = "RoomListServlet", urlPatterns = {"/room-list"})
public class RoomListServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(RoomListServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.info("=== RoomListServlet.doGet() START ===");
        
        try {
            // Khởi tạo DAO
            RoomDAO roomDAO = new RoomDAO();
            
            // Lấy parameters để filter (nếu có)
            String filterType = request.getParameter("filter"); // single, double, dormitory
            String searchKeyword = request.getParameter("search");
            String hotelIdParam = request.getParameter("hotelId");
            String checkinDate = request.getParameter("checkin");
            String checkoutDate = request.getParameter("checkout");
            
            LOGGER.log(Level.INFO, "Filter type: {0}, Search: {1}, Hotel ID: {2}, Checkin: {3}, Checkout: {4}", 
                new Object[]{filterType, searchKeyword, hotelIdParam, checkinDate, checkoutDate});
            
            List<Room> rooms = null;
            
            // Xử lý filter theo hotel (cho offline booking tại quầy của từng hotel)
            if (hotelIdParam != null && !hotelIdParam.trim().isEmpty()) {
                int hotelId = Integer.parseInt(hotelIdParam);
                
                if (filterType != null && !filterType.trim().isEmpty() && !"all".equals(filterType)) {
                    // Lọc theo hotel và room type
                    rooms = roomDAO.getRoomsByHotelAndType(hotelId, filterType);
                } else {
                    // Lấy tất cả phòng của hotel
                    rooms = roomDAO.getRoomsByHotel(hotelId);
                }
                
            // Xử lý search keyword
            } else if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                rooms = roomDAO.searchRooms(searchKeyword);
                request.setAttribute("searchKeyword", searchKeyword);
                
            // Xử lý filter theo room type
            } else if (filterType != null && !filterType.trim().isEmpty() && !"all".equals(filterType)) {
                rooms = roomDAO.getRoomsByType(filterType);
                
            // Lấy tất cả phòng (default)
            } else {
                rooms = roomDAO.getAllRooms();
            }
            
            // Log kết quả
            int roomCount = (rooms != null) ? rooms.size() : 0;
            LOGGER.log(Level.INFO, "Loaded {0} rooms", roomCount);
            
            // Set attributes cho JSP
            request.setAttribute("rooms", rooms);
            request.setAttribute("filterType", filterType);
            request.setAttribute("checkinDate", checkinDate);
            request.setAttribute("checkoutDate", checkoutDate);
            
            // Forward to JSP
            request.getRequestDispatcher("/Room-list.jsp").forward(request, response);
            
            LOGGER.info("=== RoomListServlet.doGet() END ===");
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid hotel ID format", e);
            request.setAttribute("errorMessage", "ID hotel không hợp lệ");
            request.getRequestDispatcher("/Room-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in RoomListServlet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Lỗi khi tải danh sách phòng: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Room List Servlet - Hiển thị danh sách phòng cho offline booking (lễ tân tại quầy)";
    }
}

