package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;


@WebServlet("/wellness-search")
public class WellnessSearchServlet extends HttpServlet {

    private final WellnessServiceDAO dao = new WellnessServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("search");
        String status = request.getParameter("status");
        if (status == null || status.isEmpty()) status = "all";

        int page = 1;
        int pageSize = 5;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException ignored) {
        }

        List<WellnessService> list;
        int totalRecords;

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = dao.searchByName(keyword.trim(), page, pageSize, status);
            totalRecords = dao.countSearch(keyword.trim(), status);
        } else {
            list = dao.getAll(page, pageSize, status);
            totalRecords = dao.countAll(status);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("list", list);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("statusFilter", status);

        request.getRequestDispatcher("wellness_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
