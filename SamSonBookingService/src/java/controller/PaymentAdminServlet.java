package controller;

import dao.BookingDAO;
import entity.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import util.CsrfTokenUtil;

@WebServlet(name = "PaymentAdminServlet", urlPatterns = {"/admin/payments"})
public class PaymentAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        String csrf = CsrfTokenUtil.ensureToken(session);
        BookingDAO dao = new BookingDAO();
        List<Booking> pendings = dao.getPendingBookings(0, 100);
        req.setAttribute("csrfToken", csrf);
        req.setAttribute("pendings", pendings);
        req.getRequestDispatcher("/web/admin/payments.jsp").forward(req, resp);
    }
}


