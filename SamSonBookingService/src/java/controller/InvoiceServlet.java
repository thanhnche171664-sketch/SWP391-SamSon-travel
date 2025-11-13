package controller;

import entity.Booking;
import entity.Payment;
import entity.OfflineCustomer;
import dao.BookingDAO;
import dao.OfflineCustomerDAO;
import dao.PaymentDAO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * InvoiceServlet - Hiển thị hóa đơn offline
 * 
 * Chức năng:
 * 1. Lấy dữ liệu từ session (phòng, dịch vụ, thông tin khách hàng)
 * 2. Tạo các object Booking, Payment, Customer để hiển thị
 * 3. Hiển thị trong OfflineInvoice.jsp
 */
@WebServlet(name = "InvoiceServlet", urlPatterns = {"/invoice"})
public class InvoiceServlet extends HttpServlet {
    
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
                response.sendRedirect(request.getContextPath() + "/room-list");
                return;
            }
            
            // BƯỚC 2: Lấy dữ liệu từ session
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> roomCart = (List<Map<String, Object>>) session.getAttribute("roomCart");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> serviceCart = (List<Map<String, Object>>) session.getAttribute("serviceCart");
            @SuppressWarnings("unchecked")
            Map<String, String> customerInfo = (Map<String, String>) session.getAttribute("customerInfo");
            String checkinDate = (String) session.getAttribute("checkinDate");
            String checkoutDate = (String) session.getAttribute("checkoutDate");
            
            // Kiểm tra dữ liệu
            if (roomCart == null || roomCart.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/room-list");
                return;
            }
            
            if (customerInfo == null || customerInfo.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/customer-info");
                return;
            }
            
            // BƯỚC 3: Tạo object Booking (giả lập)
            Booking booking = createBooking(roomCart, serviceCart, checkinDate, checkoutDate);
            
            // BƯỚC 4: Tạo object Payment (giả lập)
            Payment payment = createPayment(booking);
            
            // BƯỚC 5: Tạo object Customer
            OfflineCustomer customer = createCustomer(customerInfo);
            
            // BƯỚC 6: Tạo danh sách dịch vụ để hiển thị
            List<ServiceItem> serviceItems = createServiceItems(serviceCart);
            
            // BƯỚC 7: Tính lại tổng tiền phòng (để hiển thị riêng)
            double roomTotalPrice = 0;
            if (!roomCart.isEmpty()) {
                for (Map<String, Object> room : roomCart) {
                    int quantity = getIntValue(room, "quantity");
                    int nights = getIntValue(room, "nights");
                    double pricePerNight = getDoubleValue(room, "pricePerNight", 0);
                    roomTotalPrice += pricePerNight * quantity * nights;
                }
            }
            
            // BƯỚC 8: Lưu dữ liệu vào database
            String[] errorHolder = new String[1];
            boolean saveSuccess = saveOfflineBookingToDatabase(booking, customer, payment, roomCart, serviceCart, checkinDate, checkoutDate, errorHolder);
            request.setAttribute("saveSuccess", saveSuccess);
            if (!saveSuccess && errorHolder[0] != null) {
                request.setAttribute("saveErrorMessage", errorHolder[0]);
            }
            
            // BƯỚC 9: Nếu lưu thành công, xóa giỏ hàng khỏi session
            if (saveSuccess) {
                session.removeAttribute("roomCart");
                session.removeAttribute("serviceCart");
                session.removeAttribute("checkinDate");
                session.removeAttribute("checkoutDate");
                // Giữ lại customerInfo để hiển thị trong hóa đơn, sẽ xóa khi về trang chủ
                System.out.println("Đã xóa giỏ hàng khỏi session sau khi lưu booking thành công");
            }
            
            // BƯỚC 10: Đặt vào request để hiển thị trong JSP
            request.setAttribute("booking", booking);
            request.setAttribute("payment", payment);
            request.setAttribute("customer", customer);
            request.setAttribute("serviceItems", serviceItems);
            request.setAttribute("roomTotalPrice", roomTotalPrice); // Tiền phòng riêng
            
            // BƯỚC 11: Chuyển đến trang hóa đơn
            request.getRequestDispatcher("OfflineInvoice.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/customer-info");
        }
    }
    
    /**
     * Tạo object Booking từ dữ liệu trong session
     */
    private Booking createBooking(List<Map<String, Object>> roomCart, List<Map<String, Object>> serviceCart, String checkin, String checkout) {
        Booking booking = new Booking();
        
        // Tạo ID ngẫu nhiên (trong thực tế sẽ lấy từ database)
        Random random = new Random();
        booking.setId(1000 + random.nextInt(9000));
        
        // Thông tin booking
        booking.setBookingSource("OFFLINE");
        booking.setStatus("confirmed");
        booking.setBookingDate(new Date());
        booking.setCreatedAt(new Date());
        booking.setUpdatedAt(new Date());
        
        // Tính tổng số phòng và loại phòng (lấy từ phòng đầu tiên)
        double roomTotalPrice = 0;
        if (roomCart != null && !roomCart.isEmpty()) {
            Map<String, Object> firstRoom = roomCart.get(0);
            String roomType = getStringValue(firstRoom, "type");
            int totalQuantity = 0;
            int hotelId = 1; // Mặc định
            
            // Tính tổng tiền phòng
            for (Map<String, Object> room : roomCart) {
                int quantity = getIntValue(room, "quantity");
                int nights = getIntValue(room, "nights");
                double pricePerNight = getDoubleValue(room, "pricePerNight", 0);
                totalQuantity += quantity;
                roomTotalPrice += pricePerNight * quantity * nights;
                
                // Lấy hotelId từ phòng đầu tiên
                if (hotelId == 1) {
                    hotelId = getIntValue(room, "hotelId");
                    if (hotelId == 0) hotelId = 1;
                }
            }
            
            booking.setHotelId(hotelId);
            booking.setRoomType(roomType);
            booking.setNumberOfRooms(totalQuantity);
            booking.setTransportFee(0);
        }
        
        // Tính tổng tiền dịch vụ
        double serviceTotalPrice = 0;
        if (serviceCart != null && !serviceCart.isEmpty()) {
            for (Map<String, Object> service : serviceCart) {
                int quantity = getIntValue(service, "quantity");
                double price = getDoubleValue(service, "price", 0);
                serviceTotalPrice += price * quantity;
            }
        }
        
        // Tổng tiền = tiền phòng + tiền dịch vụ
        booking.setTotalPrice(roomTotalPrice + serviceTotalPrice);
        
        return booking;
    }
    
    /**
     * Tạo object Payment
     */
    private Payment createPayment(Booking booking) {
        Payment payment = new Payment();
        
        // Tạo transaction ID
        String transactionId = "TXN" + System.currentTimeMillis();
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
     * Tạo object Customer từ thông tin trong session
     */
    private OfflineCustomer createCustomer(Map<String, String> customerInfo) {
        OfflineCustomer customer = new OfflineCustomer();
        
        customer.setFullName(customerInfo.get("fullName"));
        customer.setPhone(customerInfo.get("phone"));
        customer.setEmail(customerInfo.get("email"));
        customer.setIdCardNumber(customerInfo.get("idCardNumber"));
        customer.setNationality(customerInfo.get("nationality"));
        customer.setGender(customerInfo.get("gender"));
        
        // Parse ngày sinh
        String dateOfBirthStr = customerInfo.get("dateOfBirth");
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                customer.setDateOfBirth(sdf.parse(dateOfBirthStr));
            } catch (Exception e) {
                // Nếu không parse được, để null
            }
        }
        
        customer.setAddress(customerInfo.get("address"));
        customer.setCreatedAt(new Date());
        
        return customer;
    }
    
    /**
     * Tạo danh sách dịch vụ để hiển thị trong hóa đơn
     */
    private List<ServiceItem> createServiceItems(List<Map<String, Object>> serviceCart) {
        List<ServiceItem> items = new ArrayList<>();
        
        if (serviceCart != null && !serviceCart.isEmpty()) {
            for (Map<String, Object> service : serviceCart) {
                ServiceItem item = new ServiceItem();
                item.name = getStringValue(service, "name");
                item.quantity = getIntValue(service, "quantity");
                item.pricePerUnit = getDoubleValue(service, "price", 0);
                item.price = item.pricePerUnit * item.quantity;
                item.description = getStringValue(service, "type");
                
                items.add(item);
            }
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
     * Lưu booking offline vào database
     * @return true nếu lưu thành công, false nếu lỗi
     */
    private boolean saveOfflineBookingToDatabase(Booking booking, OfflineCustomer customer, Payment payment,
                                             List<Map<String, Object>> roomCart, List<Map<String, Object>> serviceCart,
                                             String checkinDate, String checkoutDate, String[] errorHolder) {
        try {
            BookingDAO bookingDAO = new BookingDAO();
            OfflineCustomerDAO customerDAO = new OfflineCustomerDAO();
            PaymentDAO paymentDAO = new PaymentDAO();
            
            // BƯỚC 1: Lưu thông tin khách hàng
            int customerId = customerDAO.saveOfflineCustomer(customer);
            if (customerId <= 0) {
                String message = "Không thể lưu thông tin khách hàng offline";
                System.err.println(message);
                if (errorHolder != null && errorHolder.length > 0) {
                    errorHolder[0] = message;
                }
                return false;
            }
            System.out.println("Đã lưu khách hàng với ID: " + customerId);
            
            // BƯỚC 2: Tạo mã booking
            String bookingCode = "OFF" + System.currentTimeMillis();
            booking.setBookingCode(bookingCode);

            // Đồng bộ check-in/out vào đối tượng booking để sử dụng khi cần
            setBookingDatesIfPossible(booking, checkinDate, checkoutDate);

            String safeCheckinDate = normalizeDateString(checkinDate, booking.getCheckInDate());
            String safeCheckoutDate = normalizeDateString(checkoutDate, booking.getCheckOutDate());
            
            // BƯỚC 3: Lấy số người lớn và trẻ em từ customerInfo (nếu có)
            int numAdults = 1; // Mặc định
            int numChildren = 0; // Mặc định
            
            // BƯỚC 4: Lưu booking
            System.out.println("=== SAVE OFFLINE BOOKING START ===");
            System.out.println("Customer ID temp: " + customerId);
            System.out.println("Hotel ID: " + (booking.getHotelId() != null ? booking.getHotelId() : "null"));
            System.out.println("Room type: " + booking.getRoomType());
            System.out.println("Number of rooms: " + booking.getNumberOfRooms());
            System.out.println("Total price: " + booking.getTotalPrice());
            System.out.println("Check-in date: " + safeCheckinDate);
            System.out.println("Check-out date: " + safeCheckoutDate);
            System.out.println("Booking code: " + bookingCode);
            
            if (roomCart != null) {
                System.out.println("Room cart items: " + roomCart.size());
                for (Map<String, Object> item : roomCart) {
                    System.out.println("  - Item: " + item);
                }
            }
            if (serviceCart != null) {
                System.out.println("Service cart items: " + serviceCart.size());
                for (Map<String, Object> item : serviceCart) {
                    System.out.println("  - Service: " + item);
                }
            }
            
            int bookingId = bookingDAO.saveOfflineBooking(
                booking, 
                safeCheckinDate, 
                safeCheckoutDate, 
                numAdults, 
                numChildren, 
                bookingCode,
                "Booking offline - " + customer.getFullName(),
                serviceCart
            );
            
            if (bookingId <= 0) {
                String message = "Lỗi: Không thể lưu booking vào database";
                System.err.println(message);
                if (errorHolder != null && errorHolder.length > 0) {
                    errorHolder[0] = message;
                }
                System.err.println("=== SAVE OFFLINE BOOKING FAILED - bookingId <= 0 ===");
                return false;
            }

            System.out.println("Đã lưu booking với ID: " + bookingId);
            System.out.println("=== SAVE OFFLINE BOOKING BOOKING_ID: " + bookingId + " ===");
            
            // BƯỚC 5: Cập nhật booking ID và lưu payment
            payment.setBookingId(bookingId);
            System.out.println("=== SAVE PAYMENT START ===");
            System.out.println("Payment amount: " + payment.getAmount());
            System.out.println("Payment method: " + payment.getPaymentMethod());
            int paymentId = paymentDAO.savePayment(payment);
            if (paymentId <= 0) {
                String message = "Lỗi: Không thể lưu thông tin thanh toán";
                System.err.println(message);
                if (errorHolder != null && errorHolder.length > 0) {
                    errorHolder[0] = message;
                }
                System.err.println("=== SAVE PAYMENT FAILED ===");
                return false;
            }
            System.out.println("Đã lưu payment với ID: " + paymentId);
            System.out.println("=== SAVE PAYMENT SUCCESS ===");
            
            // BƯỚC 6: Lưu lịch sử đặt phòng vào bảng Offline_Booking_Customers
            OfflineBookingCustomerDAO historyDAO = new OfflineBookingCustomerDAO();
            System.out.println("=== SAVE HISTORY START ===");
            int historyId = historyDAO.saveOfflineBookingCustomer(
                bookingId,
                customerId,
                booking.getHotelId() != null ? booking.getHotelId() : 1,
                safeCheckinDate,
                safeCheckoutDate,
                numAdults,
                numChildren,
                booking.getTotalPrice(),
                payment.getStatus(),
                "Booking offline - " + customer.getFullName()
            );
            if (historyId <= 0) {
                String message = "Lỗi: Không thể lưu lịch sử booking";
                System.err.println(message);
                if (errorHolder != null && errorHolder.length > 0) {
                    errorHolder[0] = message;
                }
                System.err.println("=== SAVE HISTORY FAILED ===");
                return false;
            }
            System.out.println("Đã lưu lịch sử booking với ID: " + historyId);
            System.out.println("=== SAVE HISTORY SUCCESS ===");
            
            // Cập nhật booking ID trong object để hiển thị
            booking.setId(bookingId);
            return true; // Lưu thành công
            
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu booking offline: " + e.getMessage());
            e.printStackTrace();
            if (errorHolder != null && errorHolder.length > 0) {
                errorHolder[0] = e.getMessage();
            }
            return false;
        }
    }

    private void setBookingDatesIfPossible(Booking booking, String checkinDate, String checkoutDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (checkinDate != null && !checkinDate.trim().isEmpty()) {
            try {
                booking.setCheckInDate(sdf.parse(checkinDate));
            } catch (Exception ignored) {}
        }
        if (checkoutDate != null && !checkoutDate.trim().isEmpty()) {
            try {
                booking.setCheckOutDate(sdf.parse(checkoutDate));
            } catch (Exception ignored) {}
        }
    }

    private String normalizeDateString(String dateStr, Date fallback) {
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            return dateStr.trim();
        }
        if (fallback != null) {
            return new SimpleDateFormat("yyyy-MM-dd").format(fallback);
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
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








