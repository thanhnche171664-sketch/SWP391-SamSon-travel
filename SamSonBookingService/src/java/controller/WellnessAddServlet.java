package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "WellnessAddServlet", urlPatterns = {"/wellness-add"})
public class WellnessAddServlet extends HttpServlet {

    private final WellnessServiceDAO dao = new WellnessServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/wellness_add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            String serviceName = request.getParameter("serviceName");
            String description = request.getParameter("description");
            double basePrice = Double.parseDouble(request.getParameter("basePrice"));
            int durationMinutes = Integer.parseInt(request.getParameter("durationMinutes"));
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            String operatingHours = request.getParameter("operatingHours");
            String status = request.getParameter("status").toUpperCase(); 

            int hotelId = 1;
            int categoryId = 4;

            WellnessService ws = new WellnessService();
            ws.setHotelId(hotelId);
            ws.setCategoryId(categoryId);
            ws.setServiceName(serviceName);
            ws.setDescription(description);
            ws.setBasePrice(basePrice);
            ws.setDurationMinutes(durationMinutes);
            ws.setOperatingHours(operatingHours);
            ws.setCapacity(capacity);
            ws.setStatus(status);
            ws.setCreatedAt(new Date());
            ws.setUpdatedAt(new Date());

            boolean success = dao.addWellnessService(ws);

            if (success) {
                response.sendRedirect("wellness-list?action=list&message=add_success");
            } else {
                request.setAttribute("error", "Không thể thêm dịch vụ. Vui lòng thử lại.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}

