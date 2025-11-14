package controller.booking;

import dao.HotelDAO;
import dao.RoomDAO;
import dao.MealServiceDAO;
import dao.WellnessServiceDAO;
import dao.TransportServiceDAO;
import entity.Hotel;
import entity.Room;
import entity.MealService;
import entity.WellnessService;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/bookings"})
public class BookingStartServlet extends HttpServlet {

    private final HotelDAO hotelDAO = new HotelDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final MealServiceDAO mealServiceDAO = new MealServiceDAO();
    private final WellnessServiceDAO wellnessServiceDAO = new WellnessServiceDAO();
    private final TransportServiceDAO transportServiceDAO = new TransportServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String hotelParam = request.getParameter("hotel");
        if (hotelParam == null || hotelParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing hotel parameter");
            return;
        }

        try {
            int hotelId = Integer.parseInt(hotelParam);
            Hotel hotel = hotelDAO.getHotelById(hotelId);
            if (hotel == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hotel not found");
                return;
            }

            List<Room> rooms = roomDAO.getRoomsByHotelId(hotelId);
            List<MealService> mealServices = mealServiceDAO.getMealServicesByHotelId(hotelId);
            List<WellnessService> wellnessServices = wellnessServiceDAO.getWellnessServicesByHotelId(hotelId);
            List<TransportService> transportServices = transportServiceDAO.getTransportServicesByHotelId(hotelId);

            // Xử lý parameter room (nếu có) để tự động chọn phòng
            String roomParam = request.getParameter("room");
            String selectedRoomType = null;
            if (roomParam != null && !roomParam.trim().isEmpty()) {
                try {
                    int roomId = Integer.parseInt(roomParam);
                    // Tìm room theo ID và lấy roomType
                    for (Room room : rooms) {
                        if (room.getId() == roomId) {
                            selectedRoomType = room.getRoomType();
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    // Invalid room parameter, ignore
                }
            }

            request.setAttribute("hotel", hotel);
            request.setAttribute("rooms", rooms);
            request.setAttribute("mealServices", mealServices);
            request.setAttribute("wellnessServices", wellnessServices);
            request.setAttribute("transportServices", transportServices);
            request.setAttribute("selectedRoomType", selectedRoomType); // Room type được chọn từ parameter
            request.getRequestDispatcher("/bookings/booking_form.jsp").forward(request, response);

        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid hotel parameter");
        }
    }
}



