package controller;

import dao.OfflineBookingCustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * CustomerDetailServlet - Lấy thông tin chi tiết khách hàng theo booking_id
 * Trả về JSON để JavaScript hiển thị trong modal
 */
@WebServlet(name = "CustomerDetailServlet", urlPatterns = {"/api/customer-detail"})
public class CustomerDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        
        try {
            // Lấy booking_id từ parameter
            String bookingIdStr = request.getParameter("bookingId");
            if (bookingIdStr == null || bookingIdStr.isEmpty()) {
                sendError(response, "Thiếu booking_id");
                return;
            }
            
            int bookingId;
            try {
                bookingId = Integer.parseInt(bookingIdStr);
            } catch (NumberFormatException e) {
                sendError(response, "booking_id không hợp lệ");
                return;
            }
            
            // Lấy thông tin từ database
            OfflineBookingCustomerDAO historyDAO = new OfflineBookingCustomerDAO();
            Map<String, Object> history = historyDAO.getBookingHistoryByBookingId(bookingId);
            
            if (history == null) {
                sendError(response, "Không tìm thấy thông tin booking");
                return;
            }
            
            // Trả về JSON
            PrintWriter out = response.getWriter();
            out.print(convertToJson(history));
            out.flush();
            
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy thông tin khách hàng: " + e.getMessage());
            e.printStackTrace();
            sendError(response, "Lỗi server: " + e.getMessage());
        }
    }
    
    /**
     * Chuyển Map thành JSON (đơn giản, không dùng thư viện)
     */
    private String convertToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            String key = entry.getKey();
            Object value = entry.getValue();
            
            json.append("\"").append(escapeJson(key)).append("\":");
            
            if (value == null) {
                json.append("null");
            } else if (value instanceof String) {
                json.append("\"").append(escapeJson(value.toString())).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                json.append(value);
            } else {
                // Date hoặc các object khác - chuyển thành string
                json.append("\"").append(escapeJson(value.toString())).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
    
    /**
     * Escape JSON string
     */
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Gửi lỗi dưới dạng JSON
     */
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = response.getWriter();
        out.print("{\"error\":\"" + escapeJson(message) + "\"}");
        out.flush();
    }
}



