package controller.hotel;

import dao.HotelDAO;
import dao.ImageDAO;
import entity.Hotel;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet xóa khách sạn
 * Dành cho Hotel Manager (role_id = 3)
 */
@WebServlet("/hotel/delete")
public class DeleteHotelServlet extends HttpServlet {

    private static final HotelDAO hotelDAO = new HotelDAO();
    private static final ImageDAO imageDAO = new ImageDAO();

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

        // Lấy ID khách sạn cần xóa
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

            // Kiểm tra quyền sở hữu
            if (hotel.getManagerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
                return;
            }

            // Kiểm tra xem khách sạn có booking nào không
            boolean hasBookings = hotelDAO.hasBookings(hotelId);
            int[] bookingStats = null;
            if (hasBookings) {
                bookingStats = hotelDAO.getHotelBookingsStats(hotelId);
            }

            // Load ảnh của hotel
            entity.Image primaryImage = imageDAO.getPrimaryImage("hotel", hotelId);
            if (primaryImage == null) {
                primaryImage = imageDAO.getFirstImage("hotel", hotelId);
            }

            // Hiển thị trang xác nhận xóa
            request.setAttribute("hotel", hotel);
            request.setAttribute("hotelImage", primaryImage);
            request.setAttribute("hasBookings", hasBookings);
            request.setAttribute("bookingStats", bookingStats);
            request.getRequestDispatcher("/hotel/hotel_delete.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        try {
            int hotelId = Integer.parseInt(request.getParameter("id"));
            Hotel hotel = hotelDAO.getHotelById(hotelId);

            // Kiểm tra quyền sở hữu
            if (hotel == null || hotel.getManagerId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
                return;
            }

            // Kiểm tra xem khách sạn có booking nào không
            if (hotelDAO.hasBookings(hotelId)) {
                // Lấy thống kê chi tiết
                int[] stats = hotelDAO.getHotelBookingsStats(hotelId);
                String errorMessage = "Không thể xóa khách sạn này! ";
                
                if (stats != null) {
                    errorMessage += String.format(
                        "Hiện có %d booking (%d đã xác nhận, %d đang chờ). " +
                        "Vui lòng xử lý hết các booking trước khi xóa khách sạn.",
                        stats[0], stats[1], stats[2]
                    );
                } else {
                    errorMessage += "Khách sạn đang có booking. Vui lòng xử lý hết các booking trước khi xóa.";
                }
                
                session.setAttribute("errorMessage", errorMessage);
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }
            
            // Kiểm tra xem khách sạn có trong tour packages không
            if (hotelDAO.hasTourPackages(hotelId)) {
                int tourCount = hotelDAO.getTourPackagesCount(hotelId);
                String errorMessage = String.format(
                    "Không thể xóa khách sạn này! Khách sạn đang được sử dụng trong %d tour du lịch. " +
                    "Vui lòng xóa hoặc cập nhật các tour package trước khi xóa khách sạn.",
                    tourCount
                );
                
                session.setAttribute("errorMessage", errorMessage);
                response.sendRedirect(request.getContextPath() + "/hotel/list");
                return;
            }

            // Xóa tất cả rooms của hotel (nếu có)
            if (hotelDAO.hasRooms(hotelId)) {
                // Get all rooms
                dao.RoomDAO roomDAO = new dao.RoomDAO();
                java.util.List<entity.Room> rooms = roomDAO.getRoomsByHotelId(hotelId);
                
                // Delete images and rooms
                for (entity.Room room : rooms) {
                    imageDAO.deleteAllImagesByEntity("room", room.getId());
                    roomDAO.deleteRoom(room.getId());
                }
            }
            
            // Xóa tất cả ảnh của khách sạn
            imageDAO.deleteAllImagesByEntity("hotel", hotelId);
            
            // Xóa khách sạn
            boolean success = hotelDAO.deleteHotel(hotelId);

            if (success) {
                session.setAttribute("successMessage", "Xóa khách sạn thành công!");
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi xóa khách sạn!");
            }

            response.sendRedirect(request.getContextPath() + "/hotel/list");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }
}
