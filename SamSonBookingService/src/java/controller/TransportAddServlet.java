package controller;

import dao.TransportServiceDAO;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

@WebServlet(name="TransportAddServlet", urlPatterns={"/transport-add"})
public class TransportAddServlet extends HttpServlet {
    private final TransportServiceDAO dao = new TransportServiceDAO();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/transport_add.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String categoryIdStr = req.getParameter("categoryId");
        String vehicleType   = req.getParameter("vehicleType");
        String vehicleName   = req.getParameter("vehicleName");
        String description   = req.getParameter("description");
        String pickup        = req.getParameter("pickupLocation");
        String departureStr  = req.getParameter("departureTime"); // "yyyy-MM-dd HH:mm" hoặc "yyyy-MM-ddTHH:mm"
        String priceStr      = req.getParameter("price");
        String capacityStr   = req.getParameter("capacity");

        boolean hasErr = false;

        // --- NOT EMPTY ---
        if (categoryIdStr == null || categoryIdStr.isBlank()) { req.setAttribute("errCategoryId","Category ID không được để trống"); hasErr = true; }
        if (vehicleType   == null || vehicleType.isBlank())   { req.setAttribute("errVehicleType","Hãy chọn loại phương tiện"); hasErr = true; }
        if (vehicleName   == null || vehicleName.isBlank())   { req.setAttribute("errVehicleName","Tên phương tiện không được để trống"); hasErr = true; }
        if (description   == null || description.isBlank())   { req.setAttribute("errDescription","Mô tả không được để trống"); hasErr = true; }
        if (pickup        == null || pickup.isBlank())        { req.setAttribute("errPickup","Điểm đón không được để trống"); hasErr = true; }
        if (departureStr  == null || departureStr.isBlank())  { req.setAttribute("errDeparture","Khởi hành không được để trống (yyyy-MM-dd HH:mm)"); hasErr = true; }
        if (priceStr      == null || priceStr.isBlank())      { req.setAttribute("errPrice","Giá không được để trống"); hasErr = true; }
        if (capacityStr   == null || capacityStr.isBlank())   { req.setAttribute("errCapacity","Sức chứa không được để trống"); hasErr = true; }

        // --- PARSE & RANGE ---
        int categoryId = 0;
        if (categoryIdStr != null && !categoryIdStr.isBlank()) {
            try {
                categoryId = Integer.parseInt(categoryIdStr.trim());
                if (categoryId <= 0) {
                    req.setAttribute("errCategoryId","Category ID phải > 0 (không âm)");
                    hasErr = true;
                }
            } catch (Exception e) {
                req.setAttribute("errCategoryId","Category ID không hợp lệ");
                hasErr = true;
            }
        }

        double price = 0;
        if (priceStr != null && !priceStr.isBlank()) {
            try {
                price = Double.parseDouble(priceStr.trim());
                if (price < 0) {
                    req.setAttribute("errPrice","Giá không được âm");
                    hasErr = true;
                } else if (price < 80000) {
                    req.setAttribute("errPrice","Giá phải ≥ 80 000");
                    hasErr = true;
                }
            } catch (Exception e) {
                req.setAttribute("errPrice","Giá không hợp lệ");
                hasErr = true;
            }
        }

        int capacity = 0;
        if (capacityStr != null && !capacityStr.isBlank()) {
            try {
                capacity = Integer.parseInt(capacityStr.trim());
                if (capacity < 1 || capacity >= 45) {
                    req.setAttribute("errCapacity","Sức chứa phải từ 1–44 chỗ");
                    hasErr = true;
                }
            } catch (Exception e) {
                req.setAttribute("errCapacity","Sức chứa không hợp lệ");
                hasErr = true;
            }
        }

        // Khởi hành: chấp nhận " " hoặc "T", loại bỏ NBSP/tab, strict date
        java.util.Date departure = null;
        if (departureStr != null && !departureStr.isBlank()) {
            String raw = departureStr.replace('\u00A0',' ').trim(); // NBSP -> space
            raw = raw.replaceAll("\\s+"," ");
            raw = raw.replace('T',' ');
            String dtPattern = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";
            if (!raw.matches(dtPattern)) {
                req.setAttribute("errDeparture","Khởi hành phải đúng định dạng (yyyy-MM-dd HH:mm)");
                hasErr = true;
            } else {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    sdf.setLenient(false);
                    departure = sdf.parse(raw);
                    // lưu lại để show lại form khi lỗi chỗ khác
                    departureStr = raw;
                } catch (Exception e) {
                    req.setAttribute("errDeparture","Khởi hành phải đúng định dạng (yyyy-MM-dd HH:mm)");
                    hasErr = true;
                }
            }
        }

        if (hasErr) {
            // giữ lại form values
            req.setAttribute("categoryId", categoryIdStr);
            req.setAttribute("vehicleType", vehicleType);
            req.setAttribute("vehicleName", vehicleName);
            req.setAttribute("description", description);
            req.setAttribute("pickupLocation", pickup);
            req.setAttribute("departureStr", departureStr);
            req.setAttribute("priceStr", priceStr);
            req.setAttribute("capacityStr", capacityStr);
            req.setAttribute("error","Vui lòng kiểm tra lỗi bên dưới.");
            req.getRequestDispatcher("/transport_add.jsp").forward(req, resp);
            return;
        }

        TransportService t = new TransportService();
        t.setCategoryId(categoryId);
        t.setVehicleType(vehicleType);
        t.setVehicleName(vehicleName);
        t.setDescription(description);
        t.setPickupLocation(pickup);
        t.setDepartureTime(departure);
        t.setPrice(price);
        t.setCapacity(capacity);

        try {
            boolean ok = dao.insert(t);
            if (ok) {
                resp.sendRedirect(req.getContextPath()+"/transport-service?action=list&message=add_success");
                return;
            } else {
                req.setAttribute("error","Không thể thêm dịch vụ vận chuyển (insert trả về false).");
            }
        } catch (RuntimeException ex) {
            req.setAttribute("error","Không thể thêm: " + (ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage()));
        }

        // giữ lại input khi DB lỗi
        req.setAttribute("categoryId", categoryIdStr);
        req.setAttribute("vehicleType", vehicleType);
        req.setAttribute("vehicleName", vehicleName);
        req.setAttribute("description", description);
        req.setAttribute("pickupLocation", pickup);
        req.setAttribute("departureStr", departureStr);
        req.setAttribute("priceStr", priceStr);
        req.setAttribute("capacityStr", capacityStr);
        req.getRequestDispatcher("/transport_add.jsp").forward(req, resp);
    }
}