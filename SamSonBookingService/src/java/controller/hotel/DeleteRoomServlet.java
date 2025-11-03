package controller.hotel;

import dao.ImageDAO;
import dao.RoomDAO;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet xử lý xóa phòng
 * URL: /hotel/room/delete
 */
@WebServlet(name = "DeleteRoomServlet", urlPatterns = {"/hotel/room/delete"})
public class DeleteRoomServlet extends HttpServlet {

    private RoomDAO roomDAO = new RoomDAO();
    private ImageDAO imageDAO = new ImageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập và role
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user.getRoleId() != 3) { // Chỉ Hotel Manager mới có quyền
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        try {
            int roomId = Integer.parseInt(request.getParameter("id"));
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));

            // Kiểm tra xem phòng có booking nào không
            if (roomDAO.hasBookings(roomId)) {
                // Lấy thống kê chi tiết
                int[] stats = roomDAO.getRoomBookingsStats(roomId);
                String errorMessage = "Không thể xóa phòng này! ";
                
                if (stats != null) {
                    errorMessage += String.format(
                        "Hiện có %d booking (%d đã xác nhận, %d đang chờ). " +
                        "Vui lòng xử lý hết các booking trước khi xóa phòng.",
                        stats[0], stats[1], stats[2]
                    );
                } else {
                    errorMessage += "Phòng đang có booking. Vui lòng xử lý hết các booking trước khi xóa.";
                }
                
                session.setAttribute("errorMessage", errorMessage);
                response.sendRedirect(request.getContextPath() + "/hotel/detail?id=" + hotelId);
                return;
            }

            // Xóa tất cả ảnh của phòng trước
            imageDAO.deleteAllImagesByEntity("room", roomId);
            
            // Xóa phòng
            boolean success = roomDAO.deleteRoom(roomId);

            if (success) {
                session.setAttribute("successMessage", "Xóa phòng thành công!");
            } else {
                session.setAttribute("errorMessage", "Xóa phòng thất bại!");
            }

            // Redirect về trang chi tiết khách sạn
            response.sendRedirect(request.getContextPath() + "/hotel/detail?id=" + hotelId);

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/hotel/list");
        }
    }
}
