package controller;

import dao.RoomDAO;
import entity.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RoomListServlet - Hiển thị danh sách phòng từ database
 * Xử lý URL: /room-list
 * 
 * @author SamSon Travel Team
 */
@WebServlet(name = "RoomListServlet", urlPatterns = {"/room-list"})
public class RoomListServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(RoomListServlet.class.getName());
    
    private final RoomDAO roomDAO = new RoomDAO();
    
    /**
     * Xử lý GET request - Hiển thị danh sách phòng
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        try {
            LOGGER.info("RoomListServlet: Bắt đầu lấy danh sách phòng từ database...");
            
            // Lấy danh sách phòng từ database
            List<Room> rooms = roomDAO.getAllRooms();
            
            // Kiểm tra null
            if (rooms == null) {
                LOGGER.warning("RoomListServlet: roomDAO.getAllRooms() trả về null");
                rooms = new ArrayList<>();
            }
            
            // Log để debug
            LOGGER.info("RoomListServlet: Số lượng phòng lấy được từ database: " + rooms.size());
            
            if (rooms.isEmpty()) {
                LOGGER.warning("RoomListServlet: Không có phòng nào trong database");
            } else {
                for (Room room : rooms) {
                    LOGGER.info("RoomListServlet: Phòng ID: " + room.getId() + 
                               ", Loại: " + room.getRoomType() + 
                               ", Giá: " + room.getPrice() + 
                               ", Còn: " + room.getAvailableRooms() + "/" + room.getTotalRooms());
                }
            }
            
            // Đặt danh sách phòng vào request attribute
            request.setAttribute("rooms", rooms);
            
            // Forward đến JSP (thử cả hai cách path)
            LOGGER.info("RoomListServlet: Forward đến Room-list.jsp");
            String jspPath = "Room-list.jsp"; // Thử không có dấu / trước
            request.getRequestDispatcher(jspPath).forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "RoomListServlet: Lỗi khi lấy danh sách phòng", e);
            e.printStackTrace();
            
            // Nếu có lỗi, vẫn forward nhưng với danh sách rỗng
            request.setAttribute("rooms", new ArrayList<Room>());
            request.setAttribute("error", "Không thể tải danh sách phòng: " + e.getMessage());
            
            try {
                request.getRequestDispatcher("Room-list.jsp").forward(request, response);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "RoomListServlet: Lỗi khi forward đến JSP", ex);
                // In ra chi tiết lỗi
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Lỗi hệ thống</h1>");
                response.getWriter().println("<p>Không thể tải trang Room-list.jsp</p>");
                response.getWriter().println("<p>Lỗi: " + ex.getMessage() + "</p>");
                response.getWriter().println("<pre>");
                ex.printStackTrace(new java.io.PrintWriter(response.getWriter()));
                response.getWriter().println("</pre>");
                response.getWriter().println("</body></html>");
            }
        }
    }
}








