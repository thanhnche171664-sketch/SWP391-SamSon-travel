package controller;

import dao.TransportServiceDAO;
import entity.TransportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "TransportAddServlet", urlPatterns = {"/transport-add"})
public class TransportAddServlet extends HttpServlet {

    private final TransportServiceDAO dao = new TransportServiceDAO();
    private static final String DATE_FMT = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("transport_add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String hotelIdStr      = request.getParameter("hotelId");
        String vehicleType     = request.getParameter("vehicleType");
        String vehicleName     = request.getParameter("vehicleName");
        String description     = request.getParameter("description");
        String pickupLocation  = request.getParameter("pickupLocation");
        String departureTimeStr= request.getParameter("departureTime");
        String priceStr        = request.getParameter("price");
        String capacityStr     = request.getParameter("capacity");
        String image           = request.getParameter("image");

        int categoryId = 2;

        boolean hasError = false;
        StringBuilder sbError = new StringBuilder();

        int hotelId = 0;
        int capacity = 0;
        double price = 0;
        Date departureTime = null;

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

        if (hasError) {
            request.setAttribute("error", sbError.toString());
            request.getRequestDispatcher("transport_add.jsp").forward(request, response);
            return;
        }

        TransportService ts = new TransportService();
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

        boolean ok = dao.insert(ts);
        if (ok) {
            response.sendRedirect("transport-list?message=add_success");
        } else {
            request.setAttribute("error", "Không thể thêm dịch vụ vận chuyển.");
            request.getRequestDispatcher("transport_add.jsp").forward(request, response);
        }
    }
}
