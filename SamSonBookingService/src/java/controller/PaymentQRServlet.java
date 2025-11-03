package controller;

import dao.BookingDAO;
import entity.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

@WebServlet(name = "PaymentQRServlet", urlPatterns = {"/payment/qr"})
public class PaymentQRServlet extends HttpServlet {

    private static final String QR_BASE = "https://vietqr.co/api/generate/mb/529042003/SAM%20SON%20TRAVEL/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int bookingId = Integer.parseInt(req.getParameter("bookingId"));
        BookingDAO dao = new BookingDAO();
        Booking b = dao.getFullById(bookingId);
        if (b == null) {
            resp.sendRedirect(req.getContextPath() + "/error/404.jsp");
            return;
        }

        // Amount format e.g. 1.250.000
        DecimalFormat df = new DecimalFormat("###,###,###");
        String amountFormatted = df.format(Math.round(b.getTotalPrice())).replace(",", ".");

        // Prefer phone (more recognizable) else userId
        String suffix = (b.getContactPhone() != null && !b.getContactPhone().isEmpty()) ? b.getContactPhone()
                        : (b.getUserId() != null ? String.valueOf(b.getUserId()) : "");
        String content = (suffix.isEmpty() ? ("BOOK-" + bookingId) : ("BOOK-" + bookingId + "-" + suffix));
        String encodedContent = URLEncoder.encode(content, StandardCharsets.UTF_8.toString());

        String qrUrl = QR_BASE + amountFormatted + "/" + encodedContent + "?style=2";

        req.setAttribute("booking", b);
        req.setAttribute("qrUrl", qrUrl);
        req.setAttribute("qrContent", content);
        req.setAttribute("amountFormatted", amountFormatted + " VND");
        req.getRequestDispatcher("/payment/qr.jsp").forward(req, resp);
    }
}


