package controller.booking;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/bookings/back"})
public class BookingBackServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lưu tất cả thông tin booking vào session để giữ lại khi quay lại form
        HttpSession session = request.getSession(true);
        
        // Lưu các thông tin cơ bản
        session.setAttribute("booking_hotel", request.getParameter("hotel"));
        session.setAttribute("booking_room_type", request.getParameter("room_type"));
        session.setAttribute("booking_number_of_rooms", request.getParameter("number_of_rooms"));
        session.setAttribute("booking_num_adults", request.getParameter("num_adults"));
        session.setAttribute("booking_num_children", request.getParameter("num_children"));
        session.setAttribute("booking_check_in_date", request.getParameter("check_in_date"));
        session.setAttribute("booking_check_out_date", request.getParameter("check_out_date"));
        
        // Lưu meal services
        String[] mealIds = request.getParameterValues("meal_id");
        String[] mealQtys = request.getParameterValues("meal_qty");
        if (mealIds != null && mealQtys != null) {
            session.setAttribute("booking_meal_ids", mealIds);
            session.setAttribute("booking_meal_qtys", mealQtys);
        } else {
            session.removeAttribute("booking_meal_ids");
            session.removeAttribute("booking_meal_qtys");
        }
        
        // Lưu wellness services
        String[] wellnessIds = request.getParameterValues("wellness_id");
        String[] wellnessQtys = request.getParameterValues("wellness_qty");
        if (wellnessIds != null && wellnessQtys != null) {
            session.setAttribute("booking_wellness_ids", wellnessIds);
            session.setAttribute("booking_wellness_qtys", wellnessQtys);
        } else {
            session.removeAttribute("booking_wellness_ids");
            session.removeAttribute("booking_wellness_qtys");
        }
        
        // Redirect về trang booking form
        String hotelId = request.getParameter("hotel");
        response.sendRedirect(request.getContextPath() + "/bookings?hotel=" + hotelId);
    }
}

