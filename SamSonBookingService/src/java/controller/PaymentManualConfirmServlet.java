package controller;
import dao.BookingDAO;
import dao.PaymentDAO;
import entity.Booking;
import entity.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import util.CsrfTokenUtil;

@WebServlet(name = "PaymentManualConfirmServlet", urlPatterns = {"/payment/manual-confirm"})
public class PaymentManualConfirmServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!CsrfTokenUtil.isValid(req)) { resp.sendError(403); return; }
        HttpSession session = req.getSession(false);
        if (session == null) { resp.sendError(401); return; }
        Role role = (Role) session.getAttribute("role");
        if (role == null) { resp.sendError(403); return; }
        String rn = role.getRoleName().toLowerCase();
        if (!("administrator".equals(rn) || "front office".equals(rn) || "service manager".equals(rn))) {
            resp.sendError(403); return;
        }

        int bookingId = Integer.parseInt(req.getParameter("bookingId"));
        double amount = Double.parseDouble(req.getParameter("amount"));
        String txId = req.getParameter("transactionId");
        if (txId == null || txId.isEmpty()) txId = "MANUAL-" + bookingId + "-" + System.currentTimeMillis();

        try {
            // Anti-tampering: verify amount equals booking total
            BookingDAO bdao = new BookingDAO();
            Booking b = bdao.getBookingById(bookingId);
            if (b == null) { resp.sendError(404); return; }
            double expected = b.getTotalPrice();
            if (Math.abs(expected - amount) > 1.0) { // allow small rounding
                resp.sendError(400); return;
            }
            PaymentDAO pdao = new PaymentDAO();
            pdao.createPaid(bookingId, txId, "VND", "BANK_TRANSFER", amount);
            bdao.updateStatus(bookingId, "confirmed");
            resp.setStatus(204);
        } catch (Exception ex) {
            resp.sendError(500);
        }
    }
}


