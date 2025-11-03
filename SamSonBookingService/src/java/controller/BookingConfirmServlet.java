package controller;

import dao.BookingDAO;
import util.PricingService;
import util.CsrfTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BookingConfirmServlet", urlPatterns = {"/booking/confirm"})
public class BookingConfirmServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!CsrfTokenUtil.isValid(req)) { resp.sendError(403); return; }
        // Will compute pricing properly in next TODO and call SP
        String userIdStr = String.valueOf(req.getSession().getAttribute("userId"));
        int userId = userIdStr != null ? Integer.parseInt(userIdStr) : 0;
        int tourId, scheduleId, guestCount; Integer packageId = null;
        try {
            tourId = Integer.parseInt(req.getParameter("tourId"));
            scheduleId = Integer.parseInt(req.getParameter("scheduleId"));
            String packageIdStr = req.getParameter("packageId");
            if (packageIdStr != null && !packageIdStr.isEmpty()) packageId = Integer.parseInt(packageIdStr);
            guestCount = Integer.parseInt(req.getParameter("guestCount"));
            if (guestCount <= 0) throw new NumberFormatException("guestCount <= 0");
        } catch (NumberFormatException ex) {
            resp.sendError(400); return;
        }

        String contactName = req.getParameter("contactName");
        String contactEmail = req.getParameter("contactEmail");
        String contactPhone = req.getParameter("contactPhone");

        PricingService pricing = new PricingService();
        PricingService.PriceBreakdown p = pricing.compute(tourId, scheduleId, packageId, guestCount);
        double subtotal = p.subtotal;
        double discount = p.discount;
        double tax = p.tax;
        double total = p.total;

        BookingDAO dao = new BookingDAO();
        Integer bookingId = dao.createBookingUsingSP(userId, tourId, scheduleId, packageId, guestCount,
                contactName, contactEmail, contactPhone, subtotal, discount, tax, total, "VND");

        if (bookingId == null) {
            resp.sendRedirect(req.getContextPath() + "/error/500.jsp");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/payment/qr?bookingId=" + bookingId);
    }
}


