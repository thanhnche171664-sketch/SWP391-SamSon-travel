package controller;

import dao.TransportServiceDAO;
import entity.TransportService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name="TransportListServlet", urlPatterns={"/transport-service"})
public class TransportListServlet extends HttpServlet {
    private final TransportServiceDAO dao = new TransportServiceDAO();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";

        if ("list".equalsIgnoreCase(action) || "search".equalsIgnoreCase(action)) {
            String vehicleType = req.getParameter("vehicleType");
            if (vehicleType == null || vehicleType.isEmpty()) vehicleType = "all";
            String keyword = req.getParameter("keyword");

            int page = 1;
            try {
                page = Integer.parseInt(req.getParameter("page"));
                if (page < 1) page = 1;
            } catch (Exception ignored) {}

            int total = dao.countAll(vehicleType, keyword);
            int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
            if (totalPages > 0 && page > totalPages) page = totalPages;

            List<TransportService> list = dao.getAll(page, PAGE_SIZE, vehicleType, keyword);

            req.setAttribute("list", list);
            req.setAttribute("vehicleType", vehicleType);
            req.setAttribute("keyword", keyword);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);

            req.getRequestDispatcher("/transport_list.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/transport-service?action=list");
    }
}
