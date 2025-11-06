package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "WellnessUserListServlet", urlPatterns = {"/user-wellness"})
public class WellnessUserListServlet extends HttpServlet {

    private final WellnessServiceDAO dao = new WellnessServiceDAO();
    private static final int PAGE_SIZE = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {           
            String keyword = request.getParameter("search");
            String pageStr = request.getParameter("page");

            int page = 1;
            if (pageStr != null && !pageStr.isEmpty()) {
                try {
                    page = Integer.parseInt(pageStr);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            String statusFilter = "ACTIVE";

            List<WellnessService> services;
            int totalRecords;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = keyword.trim();
                services = dao.searchByName(kw, page, PAGE_SIZE, statusFilter);
                totalRecords = dao.countSearch(kw, statusFilter);
                request.setAttribute("keyword", kw);
            } else {
                
                services = dao.getAll(page, PAGE_SIZE, statusFilter);
                totalRecords = dao.countAll(statusFilter);
            }

            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

            request.setAttribute("services", services);

            request.getRequestDispatcher("wellness_user_list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách dịch vụ: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}