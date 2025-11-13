package controller;

import dao.RoomDAO;
import dao.MealServiceDAO;
import dao.WellnessServiceDAO;
import entity.Room;
import entity.MealService;
import entity.WellnessService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomerInfoServlet - Hiển thị form thông tin khách hàng
 * 
 * Chức năng:
 * 1. GET: Hiển thị trang Customer-info.jsp với danh sách phòng và dịch vụ đã chọn
 * 2. POST: Lưu thông tin khách hàng và chuyển sang bước tiếp theo
 */
@WebServlet(name = "CustomerInfoServlet", urlPatterns = {"/customer-info"})
public class CustomerInfoServlet extends HttpServlet {
    
    // Các DAO để lấy dữ liệu từ database
    private final RoomDAO roomDAO = new RoomDAO();
    private final MealServiceDAO mealServiceDAO = new MealServiceDAO();
    private final WellnessServiceDAO wellnessServiceDAO = new WellnessServiceDAO();
    
    /**
     * Xử lý GET request - Hiển thị form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập encoding tiếng Việt
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
            
            // BƯỚC 2: Lấy ngày check-in và check-out
            String checkinDate = request.getParameter("checkin");
            String checkoutDate = request.getParameter("checkout");
            
            // Nếu không có trong URL, lấy từ session
            if (checkinDate == null || checkinDate.isEmpty()) {
                checkinDate = (String) session.getAttribute("checkinDate");
            }
            if (checkoutDate == null || checkoutDate.isEmpty()) {
                checkoutDate = (String) session.getAttribute("checkoutDate");
            }
            
            // BƯỚC 3: Lấy giỏ hàng từ session
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> roomCart = (List<Map<String, Object>>) session.getAttribute("roomCart");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> serviceCart = (List<Map<String, Object>>) session.getAttribute("serviceCart");
            
            // Kiểm tra nếu chưa chọn phòng
            if (roomCart == null || roomCart.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/room-list");
                return;
            }
            
            // BƯỚC 4: Tạo danh sách phòng đã chọn (lấy thông tin từ database)
            List<RoomCartItem> roomCartItems = new ArrayList<>();
            double totalRoomPrice = 0;
            int totalQuantity = 0;
            int nights = 0;
            
            for (Map<String, Object> item : roomCart) {
                // Lấy ID phòng
                int roomId = getIntValue(item, "id");
                
                // Lấy thông tin phòng từ database
                Room room = roomDAO.getRoomById(roomId);
                if (room == null) continue; // Bỏ qua nếu không tìm thấy
                
                // Lấy số lượng và số đêm
                int quantity = getIntValue(item, "quantity");
                int itemNights = getIntValue(item, "nights");
                double pricePerNight = getDoubleValue(item, "pricePerNight", room.getPrice());
                
                // Tính tiền
                double subtotal = pricePerNight * quantity * itemNights;
                totalRoomPrice += subtotal;
                totalQuantity += quantity;
                nights = itemNights;
                
                // Tạo item để hiển thị
                RoomCartItem cartItem = new RoomCartItem();
                cartItem.room = room;
                cartItem.quantity = quantity;
                cartItem.nights = itemNights;
                cartItem.pricePerNight = pricePerNight;
                cartItem.subtotal = subtotal;
                
                roomCartItems.add(cartItem);
            }
            
            // BƯỚC 5: Tạo danh sách dịch vụ đã chọn (lấy thông tin từ database)
            List<ServiceCartItem> serviceCartItems = new ArrayList<>();
            double totalServicePrice = 0;
            
            if (serviceCart != null && !serviceCart.isEmpty()) {
                for (Map<String, Object> item : serviceCart) {
                    String serviceType = getStringValue(item, "type");
                    int quantity = getIntValue(item, "quantity");
                    
                    ServiceCartItem cartItem = new ServiceCartItem();
                    cartItem.type = serviceType;
                    cartItem.quantity = quantity;
                    
                    // Lấy thông tin dịch vụ từ database
                    if ("meal".equals(serviceType)) {
                        int mealId = getIntValue(item, "id");
                        MealService meal = mealServiceDAO.getMealServiceById(mealId);
                        if (meal != null) {
                            cartItem.name = meal.getMealType();
                            cartItem.price = meal.getPrice();
                            cartItem.subtotal = meal.getPrice() * quantity;
                            totalServicePrice += cartItem.subtotal;
                        }
                    } else if ("wellness".equals(serviceType)) {
                        int wellnessId = getIntValue(item, "id");
                        WellnessService wellness = wellnessServiceDAO.getById(wellnessId);
                        if (wellness != null) {
                            cartItem.name = wellness.getServiceName();
                            cartItem.price = wellness.getBasePrice();
                            cartItem.subtotal = wellness.getBasePrice() * quantity;
                            totalServicePrice += cartItem.subtotal;
                        }
                    }
                    
                    serviceCartItems.add(cartItem);
                }
            }
            
            // BƯỚC 6: Tính tổng tiền
            double grandTotal = totalRoomPrice + totalServicePrice;
            
            // BƯỚC 7: Đặt dữ liệu vào request để hiển thị trong JSP
            request.setAttribute("roomCartItems", roomCartItems);
            request.setAttribute("serviceCartItems", serviceCartItems);
            request.setAttribute("checkinDate", checkinDate);
            request.setAttribute("checkoutDate", checkoutDate);
            request.setAttribute("nights", nights);
            request.setAttribute("totalQuantity", totalQuantity);
            request.setAttribute("totalRoomPrice", totalRoomPrice);
            request.setAttribute("totalServicePrice", totalServicePrice);
            request.setAttribute("grandTotal", grandTotal);
            
            // BƯỚC 8: Chuyển đến trang JSP
            request.getRequestDispatcher("Customer-info.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("Customer-info.jsp").forward(request, response);
        }
    }
    
    /**
     * Xử lý POST request - Lưu thông tin khách hàng
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Lấy thông tin từ form
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            
            // Kiểm tra dữ liệu bắt buộc
            if (fullName == null || fullName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng nhập họ và tên!");
                doGet(request, response);
                return;
            }
            
            if (phone == null || phone.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng nhập số điện thoại!");
                doGet(request, response);
                return;
            }
            
            // Lưu thông tin khách hàng vào session
            HttpSession session = request.getSession(false);
            if (session != null) {
                Map<String, String> customerInfo = new HashMap<>();
                customerInfo.put("fullName", fullName);
                customerInfo.put("phone", phone);
                customerInfo.put("email", email != null ? email : "");
                customerInfo.put("idCardNumber", request.getParameter("idCardNumber") != null ? request.getParameter("idCardNumber") : "");
                customerInfo.put("nationality", request.getParameter("nationality") != null ? request.getParameter("nationality") : "");
                customerInfo.put("gender", request.getParameter("gender") != null ? request.getParameter("gender") : "");
                customerInfo.put("dateOfBirth", request.getParameter("dateOfBirth") != null ? request.getParameter("dateOfBirth") : "");
                customerInfo.put("address", request.getParameter("address") != null ? request.getParameter("address") : "");
                
                session.setAttribute("customerInfo", customerInfo);
            }
            
            // Chuyển đến trang hóa đơn
            response.sendRedirect(request.getContextPath() + "/invoice");
            
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            doGet(request, response);
        }
    }
    
    // ========== CÁC HÀM HỖ TRỢ ĐƠN GIẢN ==========
    
    /**
     * Lấy giá trị số nguyên từ Map
     */
    private int getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 1;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 1;
            }
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
    
    /**
     * Lấy giá trị số thập phân từ Map
     */
    private double getDoubleValue(Map<String, Object> map, String key, double defaultValue) {
        Object value = map.get(key);
        if (value == null) return defaultValue;
        if (value instanceof Double) return (Double) value;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Lấy giá trị chuỗi từ Map
     */
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return "";
        return String.valueOf(value);
    }
    
    // ========== CÁC CLASS ĐỂ CHỨA DỮ LIỆU ==========
    
    /**
     * Class để chứa thông tin phòng trong giỏ hàng
     */
    public static class RoomCartItem {
        public Room room;
        public int quantity;
        public int nights;
        public double pricePerNight;
        public double subtotal;
        
        // Getters (JSP cần dùng)
        public Room getRoom() { return room; }
        public int getQuantity() { return quantity; }
        public int getNights() { return nights; }
        public double getPricePerNight() { return pricePerNight; }
        public double getSubtotal() { return subtotal; }
    }
    
    /**
     * Class để chứa thông tin dịch vụ trong giỏ hàng
     */
    public static class ServiceCartItem {
        public String name;
        public String type;
        public int quantity;
        public double price;
        public double subtotal;
        
        // Getters (JSP cần dùng)
        public String getName() { return name; }
        public String getType() { return type; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getSubtotal() { return subtotal; }
    }
}






