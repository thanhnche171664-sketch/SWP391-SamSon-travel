package controller;

import dao.TransportServiceDAO;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "TransportDetailServlet", urlPatterns = {"/transport-detail"})
public class TransportDetailServlet extends HttpServlet {

    private final TransportServiceDAO dao = new TransportServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            request.setAttribute("error", "Thiếu tham số ID dịch vụ vận chuyển.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            TransportService ts = dao.getById(id);
            if (ts == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ vận chuyển với ID = " + id);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("transport", ts);
            request.getRequestDispatcher("transport_detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID dịch vụ không hợp lệ.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
