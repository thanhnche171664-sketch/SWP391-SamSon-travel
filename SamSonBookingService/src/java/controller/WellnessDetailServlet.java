package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "WellnessDetailServlet", urlPatterns = {"/wellness-detail"})
public class WellnessDetailServlet extends HttpServlet {

    private WellnessServiceDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new WellnessServiceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.isEmpty()) {
                request.setAttribute("error", "Thiếu ID dịch vụ cần xem chi tiết.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int id = Integer.parseInt(idParam);
            WellnessService ws = dao.getById(id);

            if (ws == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ Wellness với ID = " + id);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("ws", ws);
            request.getRequestDispatcher("wellnessdetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
