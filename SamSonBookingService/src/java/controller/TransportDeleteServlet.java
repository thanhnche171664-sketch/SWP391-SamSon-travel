package controller;

import dao.TransportServiceDAO;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "TransportDeleteServlet", urlPatterns = {"/transport-delete"})
public class TransportDeleteServlet extends HttpServlet {

    private final TransportServiceDAO dao = new TransportServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            TransportService ts = dao.getById(id);
            if (ts == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ vận chuyển để xóa!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            request.setAttribute("transport", ts);
            request.getRequestDispatcher("transport_delete.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("transportId"));
            boolean ok = dao.delete(id);
            if (ok) {
                response.sendRedirect("transport-list?message=deleted");
            } else {
                response.sendRedirect("transport-list?error=delete_failed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("transport-list?error=invalid_id");
        }
    }
}
