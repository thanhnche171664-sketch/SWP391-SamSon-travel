package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "WellnessDeleteServlet", urlPatterns = {"/wellness-delete"})
public class WellnessDeleteServlet extends HttpServlet {

    private final WellnessServiceDAO dao = new WellnessServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            WellnessService ws = dao.getById(id);

            if (ws == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ cần xóa!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("wellnessService", ws);
            request.getRequestDispatcher("wellness_delete.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("wellnessId"));
            boolean deleted = dao.deleteWellnessService(id);

            if (deleted) {
                response.sendRedirect("wellness-service?action=list&message=deleted");
            } else {
                response.sendRedirect("wellness-service?action=list&error=deletefail");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("wellness-service?action=list&error=invalidid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("wellness-service?action=list&error=exception");
        }
    }
}
