package controller.hotel;

import dao.HotelDAO;
import entity.Hotel;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet xem chi tiết khách sạn
 * Dành cho Hotel Manager (role_id = 3)
 */
@WebServlet("/hotel/detail")
public class HotelDetailServlet extends HttpServlet {

    private static final HotelDAO hotelDAO = new HotelDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Kiểm tra phân quyền
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user.getRoleId() != 3) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        // Lấy ID khách sạn
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/hotel/list");
            return;
        }

        try {
            int hotelId = Integer.parseInt(idParam);
            Hotel hotel = hotelDAO.getHotelById(hotelId);

            if (hotel == null) {
                session.setAttribute("error", "Không tìm thấy khách sạn!");
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }

            // Kiểm tra quyền xem
            if (hotel.getManagerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
                return;
            }

            // Lấy danh sách phòng của khách sạn
            dao.RoomDAO roomDAO = new dao.RoomDAO();
            java.util.List<entity.Room> rooms = roomDAO.getRoomsByHotelId(hotelId);
            
            // Lấy danh sách meal services của khách sạn
            dao.MealServiceDAO mealServiceDAO = new dao.MealServiceDAO();
            java.util.List<entity.MealService> mealServices = mealServiceDAO.getMealServicesByHotelId(hotelId);
            
            // Lấy images của hotel
            dao.ImageDAO imageDAO = new dao.ImageDAO();
            java.util.List<entity.Image> hotelImages = imageDAO.getImagesByEntity("hotel", hotelId);
            
            // Lấy images cho từng room
            java.util.Map<Integer, java.util.List<entity.Image>> roomImagesMap = new java.util.HashMap<>();
            for (entity.Room room : rooms) {
                java.util.List<entity.Image> roomImages = imageDAO.getImagesByEntity("room", room.getId());
                roomImagesMap.put(room.getId(), roomImages);
            }
            
            request.setAttribute("hotel", hotel);
            request.setAttribute("rooms", rooms);
            request.setAttribute("mealServices", mealServices);
            request.setAttribute("hotelImages", hotelImages);
            request.setAttribute("roomImagesMap", roomImagesMap);
            request.getRequestDispatcher("/hotel/hotel_detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }
}
