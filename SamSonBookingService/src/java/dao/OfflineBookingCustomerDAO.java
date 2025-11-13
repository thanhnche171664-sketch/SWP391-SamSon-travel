/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object cho bảng Offline_Booking_Customers
 * Lưu lịch sử đặt phòng offline theo khách hàng và khách sạn
 * 
 * @author SamSon Travel Team
 */
public class OfflineBookingCustomerDAO {
    
    private static final Logger LOGGER = Logger.getLogger(OfflineBookingCustomerDAO.class.getName());
    
    // SQL query để lưu lịch sử booking offline
    private static final String INSERT_OFFLINE_BOOKING_CUSTOMER = 
        "INSERT INTO Offline_Booking_Customers (booking_id, offline_customer_id, hotel_id, " +
        "check_in_date, check_out_date, num_adults, num_children, total_amount, payment_status, notes, created_at, updated_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
    
    // SQL query để lấy lịch sử booking theo khách hàng
    private static final String GET_BOOKING_HISTORY_BY_CUSTOMER = 
        "SELECT * FROM vw_Offline_Customer_Booking_History " +
        "WHERE offline_customer_id = ? " +
        "ORDER BY created_at DESC";
    
    // SQL query để lấy lịch sử booking theo khách sạn
    private static final String GET_BOOKING_HISTORY_BY_HOTEL = 
        "SELECT * FROM vw_Offline_Hotel_Booking_History " +
        "WHERE hotel_id = ? " +
        "ORDER BY created_at DESC";
    
    // SQL query để lấy tất cả lịch sử booking offline
    private static final String GET_ALL_BOOKING_HISTORY = 
        "SELECT * FROM vw_Offline_Customer_Booking_History " +
        "ORDER BY created_at DESC";
    
    // SQL query để lấy lịch sử booking theo booking_id
    private static final String GET_BOOKING_HISTORY_BY_BOOKING_ID = 
        "SELECT * FROM vw_Offline_Customer_Booking_History " +
        "WHERE booking_id = ?";
    
    private static final String GET_BOOKING_BASIC_INFO =
        "SELECT room_type, number_of_rooms FROM Bookings WHERE id = ?";
    
    private static final String GET_BOOKING_SERVICE_SUMMARY =
        "SELECT STRING_AGG(COALESCE(sc.category_name, N'Dịch vụ') + N' (x' + CAST(bd.quantity AS NVARCHAR(10)) + N')', N', ') AS service_items " +
        "FROM Booking_Details bd " +
        "LEFT JOIN ServiceCategories sc ON bd.category_id = sc.category_id " +
        "WHERE bd.booking_id = ?";
    
    /**
     * Lấy thông tin booking và khách hàng theo booking_id
     * 
     * @param bookingId ID của booking
     * @return Map chứa thông tin booking và khách hàng, null nếu không tìm thấy
     */
    public Map<String, Object> getBookingHistoryByBookingId(int bookingId) {
        if (bookingId <= 0) {
            return null;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BOOKING_HISTORY_BY_BOOKING_ID)) {
            
            statement.setInt(1, bookingId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Map<String, Object> history = mapResultSetToMap(resultSet);
                    enrichHistoryMap(history);
                    System.out.println("Đã lấy thông tin booking ID: " + bookingId);
                    return history;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi lấy thông tin booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Lưu lịch sử đặt phòng offline
     * 
     * Cách dùng:
     *   int id = dao.saveOfflineBookingCustomer(bookingId, customerId, hotelId, 
     *                                            "2024-01-15", "2024-01-17", 
     *                                            2, 0, 1000000, "PAID", "Ghi chú");
     */
    public int saveOfflineBookingCustomer(int bookingId, int offlineCustomerId, int hotelId,
                                          String checkInDate, String checkOutDate,
                                          int numAdults, int numChildren, double totalAmount,
                                          String paymentStatus, String notes) {
        
        // Kiểm tra dữ liệu đầu vào
        if (bookingId <= 0 || offlineCustomerId <= 0 || hotelId <= 0) {
            System.err.println("Lỗi: Dữ liệu không hợp lệ");
            return -1;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 INSERT_OFFLINE_BOOKING_CUSTOMER, Statement.RETURN_GENERATED_KEYS)) {
            
            // Chuyển đổi ngày từ String sang Date
            java.sql.Date checkin = java.sql.Date.valueOf(checkInDate);
            java.sql.Date checkout = java.sql.Date.valueOf(checkOutDate);
            
            // Điền dữ liệu vào câu lệnh SQL
            statement.setInt(1, bookingId);
            statement.setInt(2, offlineCustomerId);
            statement.setInt(3, hotelId);
            statement.setDate(4, checkin);
            statement.setDate(5, checkout);
            statement.setInt(6, numAdults);
            statement.setInt(7, numChildren);
            statement.setDouble(8, totalAmount);
            statement.setString(9, paymentStatus != null ? paymentStatus : "PAID");
            statement.setString(10, notes);
            
            // Thực thi và lấy kết quả
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        System.out.println("Đã lưu lịch sử booking với ID: " + id);
                        return id;
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Lỗi lưu lịch sử booking: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Lấy lịch sử đặt phòng theo khách hàng
     * 
     * Cách dùng:
     *   List<Map<String, Object>> list = dao.getBookingHistoryByCustomer(customerId);
     *   for (Map<String, Object> item : list) {
     *       String name = (String) item.get("customer_name");
     *       double amount = (Double) item.get("total_amount");
     *   }
     */
    public List<Map<String, Object>> getBookingHistoryByCustomer(int offlineCustomerId) {
        List<Map<String, Object>> historyList = new ArrayList<>();
        
        if (offlineCustomerId <= 0) {
            return historyList;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BOOKING_HISTORY_BY_CUSTOMER)) {
            
            statement.setInt(1, offlineCustomerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> history = mapResultSetToMap(resultSet);
                    historyList.add(history);
                }
            }
            
            System.out.println("Đã lấy " + historyList.size() + " booking của khách hàng ID: " + offlineCustomerId);
            enrichHistoryList(historyList);
            
        } catch (SQLException e) {
            System.err.println("Lỗi lấy lịch sử booking: " + e.getMessage());
        }
        
        return historyList;
    }
    
    /**
     * Lấy lịch sử đặt phòng theo khách sạn
     * 
     * Cách dùng:
     *   List<Map<String, Object>> list = dao.getBookingHistoryByHotel(hotelId);
     */
    public List<Map<String, Object>> getBookingHistoryByHotel(int hotelId) {
        List<Map<String, Object>> historyList = new ArrayList<>();
        
        if (hotelId <= 0) {
            return historyList;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BOOKING_HISTORY_BY_HOTEL)) {
            
            statement.setInt(1, hotelId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> history = mapResultSetToMap(resultSet);
                    historyList.add(history);
                }
            }
            
            System.out.println("Đã lấy " + historyList.size() + " booking của khách sạn ID: " + hotelId);
            enrichHistoryList(historyList);
            
        } catch (SQLException e) {
            System.err.println("Lỗi lấy lịch sử booking: " + e.getMessage());
        }
        
        return historyList;
    }
    
    /**
     * Lấy tất cả lịch sử đặt phòng offline
     */
    public List<Map<String, Object>> getAllBookingHistory() {
        List<Map<String, Object>> historyList = new ArrayList<>();
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_BOOKING_HISTORY)) {
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> history = mapResultSetToMap(resultSet);
                    historyList.add(history);
                }
            }
            
            System.out.println("Đã lấy " + historyList.size() + " booking");
            enrichHistoryList(historyList);
            
        } catch (SQLException e) {
            System.err.println("Lỗi lấy lịch sử booking: " + e.getMessage());
        }
        
        return historyList;
    }
    
    /**
     * Chuyển đổi ResultSet thành Map (để dễ dùng trong JSP)
     */
    private Map<String, Object> mapResultSetToMap(ResultSet resultSet) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        
        // Lấy tất cả cột từ ResultSet
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = resultSet.getObject(i);
            map.put(columnName, value);
        }
        
        return map;
    }
    
    /**
     * Bổ sung thông tin phòng và dịch vụ nếu thiếu trong record
     */
    private void enrichHistoryList(List<Map<String, Object>> historyList) {
        if (historyList == null || historyList.isEmpty()) {
            return;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement roomStmt = connection.prepareStatement(GET_BOOKING_BASIC_INFO);
             PreparedStatement serviceStmt = connection.prepareStatement(GET_BOOKING_SERVICE_SUMMARY)) {
            
            for (Map<String, Object> history : historyList) {
                enrichHistoryRecord(history, roomStmt, serviceStmt);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Không thể bổ sung thông tin phòng/dịch vụ cho lịch sử booking", e);
        }
    }
    
    private void enrichHistoryMap(Map<String, Object> history) {
        if (history == null) {
            return;
        }
        List<Map<String, Object>> single = new ArrayList<>();
        single.add(history);
        enrichHistoryList(single);
    }
    
    private void enrichHistoryRecord(Map<String, Object> history,
                                     PreparedStatement roomStmt,
                                     PreparedStatement serviceStmt) throws SQLException {
        int bookingId = extractInt(history.get("booking_id"));
        if (bookingId <= 0) {
            return;
        }
        
        // Bổ sung room_type và number_of_rooms nếu đang thiếu
        if (isNullOrEmpty(history.get("room_type")) || history.get("number_of_rooms") == null) {
            roomStmt.clearParameters();
            roomStmt.setInt(1, bookingId);
            try (ResultSet rs = roomStmt.executeQuery()) {
                if (rs.next()) {
                    if (isNullOrEmpty(history.get("room_type"))) {
                        history.put("room_type", rs.getString("room_type"));
                    }
                    if (history.get("number_of_rooms") == null) {
                        history.put("number_of_rooms", rs.getInt("number_of_rooms"));
                    }
                }
            }
        }
        
        // Bổ sung service_items nếu đang thiếu
        if (isNullOrEmpty(history.get("service_items"))) {
            serviceStmt.clearParameters();
            serviceStmt.setInt(1, bookingId);
            try (ResultSet rs = serviceStmt.executeQuery()) {
                if (rs.next()) {
                    history.put("service_items", rs.getString("service_items"));
                }
            }
        }
    }
    
    private boolean isNullOrEmpty(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).trim().isEmpty();
        }
        return false;
    }
    
    private int extractInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}






