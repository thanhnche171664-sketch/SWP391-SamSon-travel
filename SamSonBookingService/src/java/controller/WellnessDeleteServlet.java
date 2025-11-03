package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "WellnessDeleteServlet", urlPatterns = {"/wellness-delete"})
public class WellnessDeleteServlet extends HttpServlet {

  private final WellnessServiceDAO dao = new WellnessServiceDAO();
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String cancel = request.getParameter("cancel");
            if ("true".equalsIgnoreCase(cancel)) {
                int page = 1;
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    try {
                        page = Integer.parseInt(pageParam);
                    } catch (NumberFormatException ignored) {}
                }

                String statusFilter = request.getParameter("status");
                if (statusFilter == null || statusFilter.trim().isEmpty()) {
                    statusFilter = "all";
                }

                int totalRecords = dao.countAll(statusFilter);
                int totalPages = (int) Math.ceil((double) totalRecords / DEFAULT_PAGE_SIZE);
                if (page > totalPages && totalPages > 0) page = totalPages;
                if (page <= 0) page = 1;

                List<WellnessService> list = dao.getAll(page, DEFAULT_PAGE_SIZE, statusFilter);

                request.setAttribute("list", list);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("statusFilter", statusFilter);
                request.getRequestDispatcher("wellness_list.jsp").forward(request, response);
                return;
            }

            int id = Integer.parseInt(request.getParameter("id"));
            WellnessService ws = dao.getById(id);

            if (ws == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ cần xóa!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            String page = request.getParameter("page");
            String status = request.getParameter("status");
            if (page == null || page.isEmpty()) page = "1";
            if (status == null || status.isEmpty()) status = "all";

            request.setAttribute("wellnessService", ws);
            request.setAttribute("page", page);
            request.setAttribute("status", status);
            request.getRequestDispatcher("wellness_delete.jsp").forward(request, response);

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

            int page = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException ignored) {}
            }

            String statusFilter = request.getParameter("status");
            if (statusFilter == null || statusFilter.trim().isEmpty()) {
                statusFilter = "all";
            }

            boolean deleted = dao.deleteWellnessService(id);

            int totalRecords = dao.countAll(statusFilter);
            int totalPages = (int) Math.ceil((double) totalRecords / DEFAULT_PAGE_SIZE);
            if (page > totalPages && totalPages > 0) page = totalPages;
            if (page <= 0) page = 1;

            List<WellnessService> list = dao.getAll(page, DEFAULT_PAGE_SIZE, statusFilter);

            if (deleted) {
                request.setAttribute("message", "Xóa dịch vụ thành công!");
            } else {
                request.setAttribute("error", "Xóa thất bại!");
            }

            request.setAttribute("list", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("statusFilter", statusFilter);
            request.getRequestDispatcher("wellness_list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            int page = 1;
            String statusFilter = "all";
            List<WellnessService> list = dao.getAll(page, DEFAULT_PAGE_SIZE, statusFilter);

            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("list", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("statusFilter", statusFilter);
            request.getRequestDispatcher("wellness_list.jsp").forward(request, response);
        }
    }    
}
