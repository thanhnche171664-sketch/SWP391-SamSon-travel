package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CartSaveServlet - Lưu giỏ hàng (phòng + dịch vụ) vào session
 * 
 * Cách hoạt động:
 * 1. Nhận JSON từ JavaScript (Room-list.jsp hoặc Service-list.jsp)
 * 2. Lưu vào session để sử dụng ở các trang khác
 * 3. Trả về kết quả thành công/thất bại
 */
@WebServlet(name = "CartSaveServlet", urlPatterns = {"/api/cart/save"})
public class CartSaveServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập response là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // BƯỚC 1: Đọc dữ liệu JSON từ request
            StringBuilder json = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            
            String jsonData = json.toString();
            System.out.println("Nhận được dữ liệu: " + jsonData);
            
            // BƯỚC 2: Lấy session (tạo mới nếu chưa có)
            HttpSession session = request.getSession(true);
            
            // BƯỚC 3: Parse JSON đơn giản - lấy roomCart và serviceCart
            List<Map<String, Object>> roomCart = getRoomCart(jsonData);
            List<Map<String, Object>> serviceCart = getServiceCart(jsonData);
            String checkinDate = getValue(jsonData, "checkinDate");
            String checkoutDate = getValue(jsonData, "checkoutDate");
            
            // BƯỚC 4: Nếu chỉ có serviceCart, giữ nguyên roomCart cũ trong session
            if (roomCart.isEmpty() && !serviceCart.isEmpty()) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> oldRoomCart = (List<Map<String, Object>>) session.getAttribute("roomCart");
                if (oldRoomCart != null) {
                    roomCart = oldRoomCart;
                }
            }
            
            // BƯỚC 5: Nếu chỉ có roomCart, giữ nguyên serviceCart cũ trong session
            if (!roomCart.isEmpty() && serviceCart.isEmpty()) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> oldServiceCart = (List<Map<String, Object>>) session.getAttribute("serviceCart");
                if (oldServiceCart != null) {
                    serviceCart = oldServiceCart;
                }
            }
            
            // BƯỚC 6: Lưu vào session
            session.setAttribute("roomCart", roomCart);
            session.setAttribute("serviceCart", serviceCart);
            if (checkinDate != null && !checkinDate.isEmpty()) {
                session.setAttribute("checkinDate", checkinDate);
            }
            if (checkoutDate != null && !checkoutDate.isEmpty()) {
                session.setAttribute("checkoutDate", checkoutDate);
            }
            
            System.out.println("Đã lưu: " + roomCart.size() + " phòng, " + serviceCart.size() + " dịch vụ");
            
            // BƯỚC 7: Trả về kết quả thành công
            out.print("{\"success\": true, \"message\": \"Đã lưu giỏ hàng thành công\"}");
            
        } catch (Exception e) {
            // Nếu có lỗi, trả về thông báo lỗi
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Lỗi: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
    
    /**
     * Lấy danh sách phòng từ JSON
     * Ví dụ: {"roomCart": [{"id": 1, "quantity": 2, ...}, ...]}
     */
    private List<Map<String, Object>> getRoomCart(String json) {
        List<Map<String, Object>> list = new ArrayList<>();
        
        // Tìm "roomCart":[...]
        int start = json.indexOf("\"roomCart\":[");
        if (start < 0) return list; // Không tìm thấy
        
        // Tìm vị trí bắt đầu mảng [
        start = json.indexOf("[", start);
        if (start < 0) return list;
        
        // Tìm vị trí kết thúc mảng ]
        int end = findClosingBracket(json, start);
        if (end < 0) return list;
        
        // Lấy phần JSON của mảng
        String arrayJson = json.substring(start + 1, end);
        
        // Parse từng item trong mảng
        int pos = 0;
        while (pos < arrayJson.length()) {
            int itemStart = arrayJson.indexOf("{", pos);
            if (itemStart < 0) break;
            
            int itemEnd = findClosingBrace(arrayJson, itemStart);
            if (itemEnd < 0) break;
            
            String itemJson = arrayJson.substring(itemStart, itemEnd + 1);
            Map<String, Object> item = parseItem(itemJson);
            if (!item.isEmpty()) {
                list.add(item);
            }
            
            pos = itemEnd + 1;
        }
        
        return list;
    }
    
    /**
     * Lấy danh sách dịch vụ từ JSON (tương tự như getRoomCart)
     */
    private List<Map<String, Object>> getServiceCart(String json) {
        List<Map<String, Object>> list = new ArrayList<>();
        
        int start = json.indexOf("\"serviceCart\":[");
        if (start < 0) return list;
        
        start = json.indexOf("[", start);
        if (start < 0) return list;
        
        int end = findClosingBracket(json, start);
        if (end < 0) return list;
        
        String arrayJson = json.substring(start + 1, end);
        
        int pos = 0;
        while (pos < arrayJson.length()) {
            int itemStart = arrayJson.indexOf("{", pos);
            if (itemStart < 0) break;
            
            int itemEnd = findClosingBrace(arrayJson, itemStart);
            if (itemEnd < 0) break;
            
            String itemJson = arrayJson.substring(itemStart, itemEnd + 1);
            Map<String, Object> item = parseItem(itemJson);
            if (!item.isEmpty()) {
                list.add(item);
            }
            
            pos = itemEnd + 1;
        }
        
        return list;
    }
    
    /**
     * Parse một item JSON thành Map
     * Ví dụ: {"id": 1, "quantity": 2, "price": 100000}
     */
    private Map<String, Object> parseItem(String json) {
        Map<String, Object> map = new HashMap<>();
        
        // Loại bỏ dấu ngoặc nhọn { }
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);
        
        // Tách các cặp key:value bằng dấu phẩy
        String[] parts = json.split(",");
        for (String part : parts) {
            // Tách key và value bằng dấu :
            String[] keyValue = part.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim();
                
                // Loại bỏ dấu ngoặc kép nếu có
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                    map.put(key, value);
                } 
                // Nếu là số
                else if (value.matches("\\d+")) {
                    map.put(key, Integer.parseInt(value));
                } 
                // Nếu là số thập phân
                else if (value.matches("\\d+\\.\\d+")) {
                    map.put(key, Double.parseDouble(value));
                } 
                // Ngược lại, lưu như string
                else {
                    map.put(key, value);
                }
            }
        }
        
        return map;
    }
    
    /**
     * Lấy giá trị của một key từ JSON
     * Ví dụ: getValue(json, "checkinDate") -> "2024-01-01"
     */
    private String getValue(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start < 0) return null;
        
        start += search.length();
        int end = json.indexOf("\"", start);
        if (end < 0) return null;
        
        return json.substring(start, end);
    }
    
    /**
     * Tìm dấu đóng ngoặc vuông ] tương ứng
     */
    private int findClosingBracket(String str, int start) {
        int count = 0;
        for (int i = start; i < str.length(); i++) {
            if (str.charAt(i) == '[') count++;
            if (str.charAt(i) == ']') {
                count--;
                if (count == 0) return i;
            }
        }
        return -1;
    }
    
    /**
     * Tìm dấu đóng ngoặc nhọn } tương ứng
     */
    private int findClosingBrace(String str, int start) {
        int count = 0;
        for (int i = start; i < str.length(); i++) {
            if (str.charAt(i) == '{') count++;
            if (str.charAt(i) == '}') {
                count--;
                if (count == 0) return i;
            }
        }
        return -1;
    }
}






