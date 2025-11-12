package controller;

import dao.WellnessServiceDAO;
import entity.WellnessService;
import jakarta.servlet.ServletException;
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

        String idStr = request.getParameter("wellnessId");
        String name = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String basePriceStr = request.getParameter("basePrice");
        String durationStr = request.getParameter("durationMinutes");
        String capacityStr = request.getParameter("capacity");
        String operatingHours = request.getParameter("operatingHours");
        String status = request.getParameter("status");

        boolean hasError = false;
        int id;

        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        WellnessService ws = dao.getById(id);
        if (ws == null) {
            request.setAttribute("error", "Không tìm thấy dịch vụ cần cập nhật!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorServiceName", "Tên dịch vụ không được để trống.");
            hasError = true;
        } else {
            int wordCount = countWords(name);
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

        int duration = 0;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration <= 0) {
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
            ws.setServiceName(name);
            ws.setDescription(description);
            ws.setBasePrice(basePrice);
            ws.setDurationMinutes(duration);
            ws.setCapacity(capacity);
            ws.setOperatingHours(operatingHours);
            ws.setStatus(status);

            request.setAttribute("error", "Vui lòng kiểm tra lại các trường bên dưới.");
            request.setAttribute("wellnessService", ws);
            request.getRequestDispatcher("wellness_edit.jsp").forward(request, response);
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
            response.sendRedirect(request.getContextPath() + "/wellness-list?message=update_success");
        } else {
            request.setAttribute("error", "Cập nhật thất bại!");
            request.setAttribute("wellnessService", ws);
            request.getRequestDispatcher("wellness_edit.jsp").forward(request, response);
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
