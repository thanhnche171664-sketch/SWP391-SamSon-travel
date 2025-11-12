package controller.booking;

import dao.BookingDAO;
import entity.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/bookings/success"})
public class BookingSuccessServlet extends HttpServlet {

    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        Booking booking = null;
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                booking = bookingDAO.getBookingById(id);
            } catch (NumberFormatException ignored) {}
        }

        // Check for payment confirmation message
        String paymentConfirmed = request.getParameter("payment_confirmed");
        if ("true".equals(paymentConfirmed)) {
            request.setAttribute("successMessage", "Bạn đã xác nhận thanh toán. Vui lòng chờ admin duyệt.");
        }
        
        String error = request.getParameter("error");
        if ("payment_confirm_failed".equals(error)) {
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi xác nhận thanh toán. Vui lòng thử lại.");
        }

        request.setAttribute("booking", booking);
        request.getRequestDispatcher("/bookings/booking_success.jsp").forward(request, response);
    }
}



