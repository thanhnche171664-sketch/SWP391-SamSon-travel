package controller;

import entity.Booking;
import entity.Payment;
import entity.OfflineCustomer;
import dao.BookingDAO;
import dao.PaymentDAO;
import dao.OfflineCustomerDAO;
import dao.OfflineBookingCustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * ServiceInvoiceServlet - Hiển thị hóa đơn dịch vụ (không cần thông tin khách hàng)
 * 
 * Chức năng:
 * 1. Lấy service cart từ session
 * 2. Tạo hóa đơn dịch vụ (không cần thông tin khách hàng)
 * 3. Hiển thị hóa đơn và có thể in
 */
@WebServlet(name = "ServiceInvoiceServlet", urlPatterns = {"/service-invoice"})
public class ServiceInvoiceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        try {
            // BƯỚC 1: Lấy session
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/service-list");
                return;
            }
            
            // BƯỚC 2: Lấy service cart từ session
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> serviceCart = (List<Map<String, Object>>) session.getAttribute("serviceCart");
            
            // Kiểm tra nếu không có dịch vụ
            if (serviceCart == null || serviceCart.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/service-list");
                return;
            }
            
            // BƯỚC 3: Tạo object Booking (chỉ có dịch vụ, không có phòng)
            Booking booking = createServiceBooking(serviceCart);
            
            // BƯỚC 4: Tạo object Payment
            Payment payment = createPayment(booking);
            
            // BƯỚC 5: Tạo danh sách dịch vụ để hiển thị
            List<ServiceItem> serviceItems = createServiceItems(serviceCart);
            
            // BƯỚC 6: Tính tổng tiền dịch vụ
            double serviceTotalPrice = 0;
            for (Map<String, Object> service : serviceCart) {
                int quantity = getIntValue(service, "quantity");
                double price = getDoubleValue(service, "price", 0);
                serviceTotalPrice += price * quantity;
            }
            
            // BƯỚC 7: Lưu dữ liệu vào database
            saveServiceBookingToDatabase(booking, payment, serviceCart);
            
            // BƯỚC 8: Đặt vào request để hiển thị trong JSP
            request.setAttribute("booking", booking);
            request.setAttribute("payment", payment);
            request.setAttribute("serviceItems", serviceItems);
            request.setAttribute("serviceTotalPrice", serviceTotalPrice);
            request.setAttribute("isServiceOnly", true); // Đánh dấu đây là hóa đơn chỉ dịch vụ
            
            // BƯỚC 9: Chuyển đến trang hóa đơn
            request.getRequestDispatcher("ServiceInvoice.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/service-list");
        }
    }
    
    /**
     * Tạo object Booking chỉ có dịch vụ (không có phòng)
     */
    private Booking createServiceBooking(List<Map<String, Object>> serviceCart) {
        Booking booking = new Booking();
        
        // Tạo ID ngẫu nhiên
        Random random = new Random();
        booking.setId(2000 + random.nextInt(9000));
        
        // Thông tin booking
        booking.setBookingSource("OFFLINE");
        booking.setStatus("confirmed");
        booking.setBookingDate(new Date());
        booking.setCreatedAt(new Date());
        booking.setUpdatedAt(new Date());
        
        // Không có phòng
        booking.setHotelId(1);
        booking.setRoomType(null);
        booking.setNumberOfRooms(0);
        booking.setTransportFee(0);
        
        // Tính tổng tiền dịch vụ
        double totalPrice = 0;
        for (Map<String, Object> service : serviceCart) {
            int quantity = getIntValue(service, "quantity");
            double price = getDoubleValue(service, "price", 0);
            totalPrice += price * quantity;
        }
        
        booking.setTotalPrice(totalPrice);
        
        return booking;
    }
    
    /**
     * Tạo object Payment
     */
    private Payment createPayment(Booking booking) {
        Payment payment = new Payment();
        
        // Tạo transaction ID
        String transactionId = "SVC" + System.currentTimeMillis();
        payment.setTransactionId(transactionId);
        
        // Thông tin thanh toán
        payment.setBookingId(booking.getId());
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod("CASH"); // Mặc định là tiền mặt
        payment.setPaymentDate(new Date());
        payment.setStatus("PAID");
        payment.setCurrency("VND");
        
        return payment;
    }
    
    /**
     * Tạo danh sách dịch vụ để hiển thị trong hóa đơn
     */
    private List<ServiceItem> createServiceItems(List<Map<String, Object>> serviceCart) {
        List<ServiceItem> items = new ArrayList<>();
        
        for (Map<String, Object> service : serviceCart) {
            ServiceItem item = new ServiceItem();
            item.name = getStringValue(service, "name");
            item.quantity = getIntValue(service, "quantity");
            item.pricePerUnit = getDoubleValue(service, "price", 0);
            item.price = item.pricePerUnit * item.quantity;
            item.description = getStringValue(service, "type");
            
            items.add(item);
        }
        
        return items;
    }
    
    // ========== CÁC HÀM HỖ TRỢ ==========
    
    private int getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0;
        if (value instanceof Integer) return (Integer) value;
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private double getDoubleValue(Map<String, Object> map, String key, double defaultValue) {
        Object value = map.get(key);
        if (value == null) return defaultValue;
        if (value instanceof Double) return (Double) value;
        if (value instanceof Integer) return (Integer) value;
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return "";
        return String.valueOf(value);
    }
    
    /**
     * Lưu booking dịch vụ vào database
     */
    private void saveServiceBookingToDatabase(Booking booking, Payment payment, List<Map<String, Object>> serviceCart) {
        try {
            BookingDAO bookingDAO = new BookingDAO();
            PaymentDAO paymentDAO = new PaymentDAO();
            OfflineCustomerDAO customerDAO = new OfflineCustomerDAO();
            OfflineBookingCustomerDAO historyDAO = new OfflineBookingCustomerDAO();
            
            // BƯỚC 1: Tìm hoặc tạo khách hàng mặc định "Khách vãng lai"
            // (Nếu đã có thì dùng lại, không tạo mới mỗi lần)
            int customerId = customerDAO.findOrCreateDefaultCustomer();
            if (customerId <= 0) {
                System.err.println("Lỗi: Không thể tìm hoặc tạo khách hàng mặc định");
                return;
            }
            
            // BƯỚC 2: Tạo mã booking
            String bookingCode = "SVC" + System.currentTimeMillis();
            
            // BƯỚC 3: Lấy ngày hiện tại cho check-in/check-out (vì service-only không có ngày cụ thể)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());
            
            // BƯỚC 4: Lưu booking (chỉ có dịch vụ, không có phòng)
            int bookingId = bookingDAO.saveOfflineBooking(
                booking,
                today, // Check-in date = ngày hiện tại
                today, // Check-out date = ngày hiện tại
                1, // Mặc định 1 người lớn
                0, // Không có trẻ em
                bookingCode,
                "Service-only booking - Khách vãng lai",
                serviceCart
            );
            
            if (bookingId > 0) {
                System.out.println("Đã lưu booking dịch vụ với ID: " + bookingId);
                
                // BƯỚC 5: Lưu payment
                payment.setBookingId(bookingId);
                int paymentId = paymentDAO.savePayment(payment);
                System.out.println("Đã lưu payment với ID: " + paymentId);
                
                // BƯỚC 6: Lưu lịch sử booking vào bảng Offline_Booking_Customers
                int historyId = historyDAO.saveOfflineBookingCustomer(
                    bookingId,
                    customerId,
                    booking.getHotelId() != null ? booking.getHotelId() : 1,
                    today, // Check-in date
                    today, // Check-out date
                    1, // Số người lớn
                    0, // Số trẻ em
                    booking.getTotalPrice(),
                    payment.getStatus(),
                    "Service-only booking - Khách vãng lai"
                );
                System.out.println("Đã lưu lịch sử booking dịch vụ với ID: " + historyId);
                
                // Cập nhật booking ID trong object để hiển thị
                booking.setId(bookingId);
            } else {
                System.err.println("Lỗi: Không thể lưu booking dịch vụ vào database");
            }
            
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu booking dịch vụ: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ========== CLASS ĐỂ CHỨA THÔNG TIN DỊCH VỤ ==========
    
    /**
     * Class để chứa thông tin dịch vụ trong hóa đơn
     */
    public static class ServiceItem {
        public String name;
        public int quantity;
        public double pricePerUnit;
        public double price;
        public String description;
        
        // Getters (JSP cần dùng)
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public double getPricePerUnit() { return pricePerUnit; }
        public double getPrice() { return price; }
        public String getDescription() { return description; }
    }
}









