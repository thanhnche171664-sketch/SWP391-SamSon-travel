package controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dao.DBContext;
import util.RateLimiterUtil;

@WebServlet(name = "PaymentCheckServlet", urlPatterns = {"/payment/check"})
public class PaymentCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!RateLimiterUtil.allow(req, 5)) { // burst 5, ~1 rps refill
            resp.setStatus(429);
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) { out.write("{\"status\":\"rate_limited\"}"); }
            return;
        }
        int bookingId = Integer.parseInt(req.getParameter("bookingId"));
        String status = "pending";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT TOP 1 status FROM Payments WHERE booking_id = ? ORDER BY payment_date DESC")) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String s = rs.getString(1);
                    if ("PAID".equalsIgnoreCase(s)) status = "paid";
                    else if ("FAILED".equalsIgnoreCase(s)) status = "failed";
                }
            }
        } catch (Exception ignore) {}
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.write("{\"status\":\"" + status + "\"}");
        }
    }
}


