package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/wellness-list")
public class WellnessListServlet extends HttpServlet {

    private static final WellnessServiceDAO dao = new WellnessServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int page = 1;
        int pageSize = 5;

        String pageParam = request.getParameter("page");
        String status = request.getParameter("status");

        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        if (status == null || status.isBlank()) {
            status = "all";
        }

        List<WellnessService> list = dao.getAll(page, pageSize, status);
        int totalRecords = dao.countAll(status);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("list", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("statusFilter", status);

        request.getRequestDispatcher("/wellness_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
