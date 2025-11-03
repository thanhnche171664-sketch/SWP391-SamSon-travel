package controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import util.PricingService;
import util.CsrfTokenUtil;

@WebServlet(name = "BookingReviewServlet", urlPatterns = {"/booking/review"})
public class BookingReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!CsrfTokenUtil.isValid(req)) { resp.sendError(403); return; }
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

        PricingService pricing = new PricingService();
        PricingService.PriceBreakdown p = pricing.compute(tourId, scheduleId, packageId, guestCount);

        req.setAttribute("tourId", tourId);
        req.setAttribute("scheduleId", scheduleId);
        req.setAttribute("packageId", packageId);
        req.setAttribute("guestCount", guestCount);
        req.setAttribute("price", p);
        req.setAttribute("csrfToken", req.getSession().getAttribute("csrfToken"));
        req.getRequestDispatcher("/booking/review.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/web/booking/review.jsp").forward(req, resp);
    }
}


