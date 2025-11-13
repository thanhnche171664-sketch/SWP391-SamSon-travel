package controller;

import dao.TransportServiceDAO;
import dao.HotelDAO;             // 🔹 THÊM
import entity.TransportService;
import entity.Hotel;            // 🔹 THÊM
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;          // 🔹 THÊM

@WebServlet(name = "TransportEditServlet", urlPatterns = {"/transport-edit"})
public class TransportEditServlet extends HttpServlet {

    private final TransportServiceDAO dao = new TransportServiceDAO();
    private final HotelDAO hotelDAO = new HotelDAO();   // 🔹 THÊM
    private static final String DATE_FMT = "yyyy-MM-dd HH:mm:ss";

    // 🔹 Hàm load danh sách khách sạn cho dropdown
    private void loadHotels(HttpServletRequest request) {
        List<Hotel> hotels = hotelDAO.getAllHotels();   // đổi tên method nếu DAO bạn khác
        request.setAttribute("hotels", hotels);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            TransportService ts = dao.getById(id);
            if (ts == null) {
                request.setAttribute("error", "Không tìm thấy dịch vụ vận chuyển!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            request.setAttribute("transport", ts);
            loadHotels(request); // 🔹 đưa danh sách hotel cho JSP
            request.getRequestDispatcher("transport_edit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String idStr            = request.getParameter("transportId");
        String hotelIdStr       = request.getParameter("hotelId");
        String vehicleType      = request.getParameter("vehicleType");
        String vehicleName      = request.getParameter("vehicleName");
        String description      = request.getParameter("description");
        String pickupLocation   = request.getParameter("pickupLocation");
        String departureTimeStr = request.getParameter("departureTime");
        String priceStr         = request.getParameter("price");
        String capacityStr      = request.getParameter("capacity");
        String image            = request.getParameter("image");

        boolean hasError = false;
        StringBuilder sbError = new StringBuilder();

        int id = 0;
        int hotelId = 0;
        int categoryId = 2;
        double price = 0;
        int capacity = 0;
        Date departureTime = null;

        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            hasError = true;
            sbError.append("ID dịch vụ không hợp lệ. ");
        }

        try {
            hotelId = Integer.parseInt(hotelIdStr);
            if (hotelId < 1) {
                hasError = true;
                sbError.append("Khách sạn phải từ 1 trở lên. ");
            }
        } catch (NumberFormatException e) {
            hasError = true;
            sbError.append("Khách sạn không hợp lệ. ");
        }

        if (vehicleName == null || vehicleName.trim().isEmpty()) {
            hasError = true;
            sbError.append("Tên xe không được để trống. ");
        }

        if (description == null || description.trim().isEmpty()) {
            hasError = true;
            sbError.append("Mô tả không được để trống. ");
        }

        if (pickupLocation == null || pickupLocation.trim().isEmpty()) {
            hasError = true;
            sbError.append("Điểm đón không được để trống. ");
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT);
            sdf.setLenient(false);
            departureTime = sdf.parse(departureTimeStr);
        } catch (ParseException e) {
            hasError = true;
            sbError.append("Thời gian khởi hành phải đúng định dạng yyyy-MM-dd HH:mm:ss. ");
        }

        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                hasError = true;
                sbError.append("Giá phải lớn hơn 0. ");
            }
        } catch (NumberFormatException e) {
            hasError = true;
            sbError.append("Giá không hợp lệ. ");
        }

        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                hasError = true;
                sbError.append("Sức chứa phải lớn hơn 0. ");
            }
        } catch (NumberFormatException e) {
            hasError = true;
            sbError.append("Sức chứa không hợp lệ. ");
        }

        TransportService ts = dao.getById(id);

        if (hasError) {
            request.setAttribute("error", sbError.toString());
            request.setAttribute("transport", ts);
            loadHotels(request); // 🔹 load lại hotels để dropdown không bị rỗng khi báo lỗi
            request.getRequestDispatcher("transport_edit.jsp").forward(request, response);
            return;
        }

        ts.setHotelId(hotelId);
        ts.setCategoryId(categoryId);
        ts.setVehicleType(vehicleType);
        ts.setVehicleName(vehicleName);
        ts.setDescription(description);
        ts.setPickupLocation(pickupLocation);
        ts.setDepartureTime(departureTime);
        ts.setPrice(price);
        ts.setCapacity(capacity);
        ts.setImage(image);

        boolean ok = dao.update(ts);
        if (ok) {
            response.sendRedirect("transport-list?message=update_success");
        } else {
            request.setAttribute("error", "Cập nhật thất bại!");
            request.setAttribute("transport", ts);
            loadHotels(request); // 🔹 lại load hotels
            request.getRequestDispatcher("transport_edit.jsp").forward(request, response);
        }
    }
}