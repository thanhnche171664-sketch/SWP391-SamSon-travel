package controller.payment;

import dao.PaymentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/payments/approval", "/admin/payments/approve"})
public class PaymentApprovalServlet extends HttpServlet {

    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        entity.User user = (session != null) ? (entity.User) session.getAttribute("user") : null;
        
        // Check if user is admin (role_id = 1)
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get pending payments
        java.util.List<PaymentDAO.PaymentWithBooking> pendingPayments = paymentDAO.getPendingPayments();
        request.setAttribute("pendingPayments", pendingPayments);
        
        request.getRequestDispatcher("/admin/payment_approval.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        entity.User user = (session != null) ? (entity.User) session.getAttribute("user") : null;
        
        // Check if user is admin (role_id = 1)
        if (user == null || user.getRoleId() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập");
            return;
        }

        String action = request.getParameter("action");
        String paymentIdParam = request.getParameter("payment_id");
        String bookingIdParam = request.getParameter("booking_id");

        if (paymentIdParam == null || bookingIdParam == null) {
            request.setAttribute("errorMessage", "Thiếu thông tin");
            doGet(request, response);
            return;
        }

        try {
            int paymentId = Integer.parseInt(paymentIdParam);
            int bookingId = Integer.parseInt(bookingIdParam);

            if ("approve".equals(action)) {
                // Approve payment: update payment to PAID and booking to confirmed
                boolean success = paymentDAO.confirmPayment(paymentId, bookingId);
                if (success) {
                    request.setAttribute("successMessage", "Đã duyệt thanh toán thành công!");
                } else {
                    request.setAttribute("errorMessage", "Có lỗi xảy ra khi duyệt thanh toán");
                }
            } else if ("reject".equals(action)) {
                // Reject payment: update payment to FAILED
                boolean success = paymentDAO.updatePaymentStatus(paymentId, "FAILED");
                if (success) {
                    request.setAttribute("successMessage", "Đã từ chối thanh toán");
                } else {
                    request.setAttribute("errorMessage", "Có lỗi xảy ra khi từ chối thanh toán");
                }
            }

            doGet(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Thông tin không hợp lệ");
            doGet(request, response);
        }
    }
}

