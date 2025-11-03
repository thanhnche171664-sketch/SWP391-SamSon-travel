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

        request.setAttribute("serviceName", serviceName);
        request.setAttribute("description", description);
        request.setAttribute("basePrice", basePriceStr);
        request.setAttribute("durationMinutes", durationStr);
        request.setAttribute("capacity", capacityStr);
        request.setAttribute("operatingHours", operatingHours);
        request.setAttribute("status", status);

        boolean hasError = false;

        if (serviceName == null || serviceName.trim().isEmpty()) {
            request.setAttribute("errorServiceName", "Tên dịch vụ không được để trống.");
            hasError = true;
        }

        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("errorDescription", "Mô tả không được để trống.");
            hasError = true;
        }

        double basePrice = 0;
if (basePriceStr == null || basePriceStr.trim().isEmpty()) {
    request.setAttribute("errorBasePrice", "Giá cơ bản không được để trống.");
    hasError = true;
} else {
    try {
        basePrice = Double.parseDouble(basePriceStr);

        if (basePrice < 0) {
            request.setAttribute("errorBasePrice", "Giá cơ bản phải >= 0.");
            hasError = true;
        }
        else if (basePrice < 80000) {
            request.setAttribute("errorBasePrice", "Giá cơ bản phải từ 80.000 VND trở lên.");
            hasError = true;
        }

    } catch (NumberFormatException e) {
        request.setAttribute("errorBasePrice", "Giá cơ bản phải là số.");
        hasError = true;
    }
}

        int durationMinutes = 0;
        if (durationStr == null || durationStr.trim().isEmpty()) {
            request.setAttribute("errorDuration", "Thời lượng không được để trống.");
            hasError = true;
        } else {
            try {
                durationMinutes = Integer.parseInt(durationStr);
                if (durationMinutes <= 0) {
                    request.setAttribute("errorDuration", "Thời lượng phải > 0 phút.");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorDuration", "Thời lượng phải là số nguyên.");
                hasError = true;
            }
        }

        int capacity = 0;
        if (capacityStr == null || capacityStr.trim().isEmpty()) {
            request.setAttribute("errorCapacity", "Sức chứa không được để trống.");
            hasError = true;
        } else {
            try {
                capacity = Integer.parseInt(capacityStr);
                if (capacity <= 0) {
                    request.setAttribute("errorCapacity", "Sức chứa phải > 0.");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorCapacity", "Sức chứa phải là số nguyên.");
                hasError = true;
            }
        }

        if (operatingHours == null || operatingHours.trim().isEmpty()) {
            request.setAttribute("errorOperatingHours", "Giờ hoạt động không được để trống.");
            hasError = true;
        } else {
            String pattern = "^(\\d{2}):(\\d{2})\\s*[-–]\\s*(\\d{2}):(\\d{2})$";
            java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher matcher = regex.matcher(operatingHours.trim());

            if (!matcher.matches()) {
                request.setAttribute("errorOperatingHours",
                        "Giờ hoạt động phải đúng định dạng");
                hasError = true;
            } else {
                try {
                    int startHour = Integer.parseInt(matcher.group(1));
                    int startMin = Integer.parseInt(matcher.group(2));
                    int endHour = Integer.parseInt(matcher.group(3));
                    int endMin = Integer.parseInt(matcher.group(4));
                   
                    boolean outOfRange = (startHour < 8 || endHour > 21
                            || (startHour == 21 && startMin > 0)
                            || (endHour == 21 && endMin > 0));

                    if (outOfRange) {
                        request.setAttribute("errorOperatingHours",
                                "Giờ hoạt động phải nằm trong khoảng 08:00–21:00.");
                        hasError = true;
                    } else if ((endHour < startHour) || (endHour == startHour && endMin <= startMin)) {
                        request.setAttribute("errorOperatingHours",
                                "Giờ kết thúc phải lớn hơn giờ bắt đầu.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorOperatingHours",
                            "Giờ hoạt động không hợp lệ.");
                    hasError = true;
                }
            }
        }
        if (status == null || status.trim().isEmpty()) {
            request.setAttribute("errorStatus", "Phải chọn trạng thái.");
            hasError = true;
        } else {
            status = status.toUpperCase();
            if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
                request.setAttribute("errorStatus", "Trạng thái không hợp lệ.");
                hasError = true;
            }
        }

        if (hasError) {
            request.setAttribute("error", "Vui lòng kiểm tra lại các trường bên dưới.");
            request.getRequestDispatcher("/wellness_add.jsp").forward(request, response);
            return;
        }

        int hotelId = 1;
        int categoryId = 4;

        WellnessService ws = new WellnessService();
        ws.setHotelId(hotelId);
        ws.setCategoryId(categoryId);
        ws.setServiceName(serviceName.trim());
        ws.setDescription(description.trim());
        ws.setBasePrice(basePrice);
        ws.setDurationMinutes(durationMinutes);
        ws.setOperatingHours(operatingHours.trim());
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
}
