package controller;

import dao.TransportServiceDAO;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "TransportListServlet", urlPatterns = {"/transport-list"})
public class TransportListServlet extends HttpServlet {

    private final TransportServiceDAO dao = new TransportServiceDAO();
    private static final int PAGE_SIZE = 5;   

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("search");
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalRecords = dao.countAll(keyword);
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;       

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        List<TransportService> list = dao.getPagedList(keyword, page, PAGE_SIZE);

        request.setAttribute("list", list);
        request.setAttribute("search", keyword);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("transport_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
