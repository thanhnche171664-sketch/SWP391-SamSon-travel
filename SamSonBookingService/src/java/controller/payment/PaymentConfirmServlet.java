package controller.payment;

import dao.PaymentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/payment/confirm"})
public class PaymentConfirmServlet extends HttpServlet {

    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        entity.User user = (session != null) ? (entity.User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String bookingIdParam = request.getParameter("booking_id");
        if (bookingIdParam == null || bookingIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "Thiếu thông tin booking");
            request.getRequestDispatcher("/bookings/booking_success.jsp").forward(request, response);
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdParam);
            
            // Get payment for this booking
            entity.Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
            if (payment == null) {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin thanh toán");
                request.getRequestDispatcher("/bookings/booking_success.jsp").forward(request, response);
                return;
            }

            // Check if user owns this booking
            if (payment.getBookingId() != bookingId) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập");
                return;
            }

            // Update payment status to PENDING (waiting for admin approval)
            boolean success = paymentDAO.updatePaymentStatus(payment.getPaymentId(), "PENDING");
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/bookings/success?id=" + bookingId + "&payment_confirmed=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/bookings/success?id=" + bookingId + "&error=payment_confirm_failed");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Thông tin booking không hợp lệ");
            request.getRequestDispatcher("/bookings/booking_success.jsp").forward(request, response);
        }
    }
}

