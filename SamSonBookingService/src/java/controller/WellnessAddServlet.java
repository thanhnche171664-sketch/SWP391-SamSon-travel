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
        request.setCharacterEncoding("UTF-8");

        String serviceName = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String basePriceStr = request.getParameter("basePrice");
        String durationStr = request.getParameter("durationMinutes");
        String capacityStr = request.getParameter("capacity");
        String operatingHours = request.getParameter("operatingHours");
        String status = request.getParameter("status");

        boolean hasError = false;

        if (serviceName == null || serviceName.trim().isEmpty()) {
            request.setAttribute("errorServiceName", "Tên dịch vụ không được để trống.");
            hasError = true;
        } else {
            int wordCount = countWords(serviceName);
            if (wordCount > 40) {
                request.setAttribute("errorServiceName", "Tên dịch vụ phải dưới 40 từ (hiện tại: " + wordCount + ").");
                hasError = true;
            }
        }

        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("errorDescription", "Mô tả không được để trống.");
            hasError = true;
        } else {
            int wordCount = countWords(description);
            if (wordCount > 250) {
                request.setAttribute("errorDescription", "Mô tả phải dưới 250 từ (hiện tại: " + wordCount + ").");
                hasError = true;
            }
        }

        double basePrice = 0;
        try {
            basePrice = Double.parseDouble(basePriceStr);
            if (basePrice < 80000) {
                request.setAttribute("errorBasePrice", "Giá cơ bản phải từ 80.000 VND trở lên.");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorBasePrice", "Giá cơ bản không hợp lệ.");
            hasError = true;
        }

        int durationMinutes = 0;
        try {
            durationMinutes = Integer.parseInt(durationStr);
            if (durationMinutes <= 0) {
                request.setAttribute("errorDuration", "Thời lượng phải lớn hơn 0 phút.");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorDuration", "Thời lượng không hợp lệ.");
            hasError = true;
        }

        int capacity = 0;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                request.setAttribute("errorCapacity", "Sức chứa phải lớn hơn 0.");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorCapacity", "Sức chứa không hợp lệ.");
            hasError = true;
        }
        
        if (operatingHours == null || operatingHours.trim().isEmpty()) {
            request.setAttribute("errorOperatingHours", "Giờ hoạt động không được để trống.");
            hasError = true;
        } else if (!isValidOperatingHours(operatingHours)) {
            request.setAttribute("errorOperatingHours", "Giờ hoạt động phải trong khoảng 08:00–21:00 và đúng định dạng HH:mm–HH:mm.");
            hasError = true;
        }

        if (status == null || status.isEmpty()) {
            status = "ACTIVE";
        } else {
            status = status.toUpperCase();
        }

        if (hasError) {
            request.setAttribute("serviceName", serviceName);
            request.setAttribute("description", description);
            request.setAttribute("basePrice", basePriceStr);
            request.setAttribute("durationMinutes", durationStr);
            request.setAttribute("capacity", capacityStr);
            request.setAttribute("operatingHours", operatingHours);
            request.setAttribute("status", status);
            request.setAttribute("error", "Vui lòng kiểm tra lại các trường bên dưới.");
            request.getRequestDispatcher("/wellness_add.jsp").forward(request, response);
            return;
        }

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
    }

    private int countWords(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return 0;
        return trimmed.split("\\s+").length;
    }

    private boolean isValidOperatingHours(String hours) {
        String normalized = hours.replace("-", "–");
        if (!normalized.matches("\\d{2}:\\d{2}–\\d{2}:\\d{2}")) {
            return false;
        }
        String[] parts = normalized.split("–");
        String start = parts[0];
        String end = parts[1];

        int startMinutes = toMinutes(start);
        int endMinutes = toMinutes(end);

        int minAllowed = toMinutes("08:00");
        int maxAllowed = toMinutes("21:00");

        return startMinutes >= minAllowed && endMinutes <= maxAllowed && startMinutes < endMinutes;
    }

    private int toMinutes(String hhmm) {
        String[] p = hhmm.split(":");
        int h = Integer.parseInt(p[0]);
        int m = Integer.parseInt(p[1]);
        return h * 60 + m;
    }
}
