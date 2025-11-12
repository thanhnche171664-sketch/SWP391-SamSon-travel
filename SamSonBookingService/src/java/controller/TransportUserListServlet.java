package controller;

import dao.TransportServiceDAO;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "TransportUserListServlet", urlPatterns = {"/user-transport"})
public class TransportUserListServlet extends HttpServlet {

    private final TransportServiceDAO dao = new TransportServiceDAO();
    private static final int PAGE_SIZE = 6; // mỗi trang 6 xe

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
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            
            List<TransportService> all = dao.getAll();

            request.setAttribute("services", all);
            request.getRequestDispatcher("transport_user_list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách dịch vụ vận chuyển: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
