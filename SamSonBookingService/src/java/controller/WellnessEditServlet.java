package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "WellnessEditServlet", urlPatterns = {"/wellness-edit"})
public class WellnessEditServlet extends HttpServlet {

    private WellnessServiceDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new WellnessServiceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            WellnessService ws = dao.getById(id);

            if (ws == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ cần chỉnh sửa!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("wellnessService", ws);
            request.getRequestDispatcher("wellness_edit.jsp").forward(request, response);

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

        request.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("wellnessId"));
            String name = request.getParameter("serviceName");
            String description = request.getParameter("description");
            double basePrice = Double.parseDouble(request.getParameter("basePrice"));
            int duration = Integer.parseInt(request.getParameter("durationMinutes"));
            String operatingHours = request.getParameter("operatingHours");
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            String status = request.getParameter("status");

            WellnessService ws = dao.getById(id);
            if (ws == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ cần cập nhật!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            ws.setServiceName(name);
            ws.setDescription(description);
            ws.setBasePrice(basePrice);
            ws.setDurationMinutes(duration);
            ws.setOperatingHours(operatingHours);
            ws.setCapacity(capacity);
            ws.setStatus(status);

            boolean updated = dao.updateWellnessService(ws);

            if (updated) {
                response.sendRedirect("wellness-service?action=list&message=update_success");
            } else {
                request.setAttribute("error", "Cập nhật thất bại!");
                request.setAttribute("wellnessService", ws);
                request.getRequestDispatcher("wellness_edit.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ! Vui lòng kiểm tra lại.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
