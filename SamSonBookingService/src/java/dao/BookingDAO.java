/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Booking;
import entity.BookingDetail;
import entity.Hotel;
import entity.TransportService;
import entity.ServiceCategory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Booking entity
 * Handles all database operations related to bookings
 * 
 * @author SamSon Travel Team
 */
public class BookingDAO {
    
    private static final Logger LOGGER = Logger.getLogger(BookingDAO.class.getName());
    
    // SQL queries
    private static final String GET_BOOKINGS_BY_USER_ID = 
        "SELECT id, user_id, hotel_id, room_type, number_of_rooms, transport_id, " +
        "transport_fee, total_price, booking_date, booking_source, created_by, " +
        "status, created_at, updated_at " +
        "FROM bookings WHERE user_id = ? ORDER BY booking_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    
    private static final String GET_BOOKING_BY_ID = 
        "SELECT id, user_id, hotel_id, room_type, number_of_rooms, transport_id, " +
        "transport_fee, total_price, booking_date, booking_source, created_by, " +
        "status, created_at, updated_at " +
        "FROM bookings WHERE id = ?";
    
    private static final String GET_BOOKING_DETAILS = 
        "SELECT bd.id, bd.booking_id, bd.category_id, bd.quantity, bd.price, " +
        "sc.category_name " +
        "FROM Booking_Details bd " +
        "INNER JOIN ServiceCategories sc ON bd.category_id = sc.category_id " +
        "WHERE bd.booking_id = ?";
    
    private static final String GET_BOOKING_COUNT_BY_USER_ID = 
        "SELECT COUNT(*) FROM bookings WHERE user_id = ?";

    private static final String SUM_OVERLAP_BOOKED_ROOMS =
        "SELECT ISNULL(SUM(number_of_rooms),0) AS sum_rooms " +
        "FROM Bookings " +
        "WHERE hotel_id = ? AND room_type = ? " +
        "AND status IN ('pending','confirmed') " +
        "AND NOT (check_out_date <= ? OR check_in_date >= ?)";

    private static final String INSERT_BOOKING =
        "INSERT INTO Bookings (user_id, hotel_id, room_type, number_of_rooms, transport_id, transport_fee, total_price, booking_date, booking_source, created_by, status, check_in_date, check_out_date, num_adults, num_children, booking_code, created_at, updated_at) " +
        "VALUES (?, ?, ?, ?, NULL, 0, ?, GETDATE(), 'ONLINE', ?, 'pending', ?, ?, ?, ?, ?, GETDATE(), GETDATE())";

    private static final String INSERT_ADDON =
        "INSERT INTO Booking_Addons (booking_id, addon_type, reference_id, name, unit_price, quantity) VALUES (?,?,?,?,?,?)";
    
    // SQL query để lưu booking offline
    private static final String INSERT_OFFLINE_BOOKING =
        "INSERT INTO Bookings (user_id, hotel_id, room_type, number_of_rooms, transport_id, transport_fee, total_price, booking_date, booking_source, created_by, status, check_in_date, check_out_date, num_adults, num_children, booking_code, notes, created_at, updated_at) " +
        "VALUES (NULL, ?, ?, ?, NULL, 0, ?, GETDATE(), 'OFFLINE', ?, 'confirmed', ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
    
    // SQL query để lưu booking detail (dịch vụ)
    private static final String INSERT_BOOKING_DETAIL =
        "INSERT INTO Booking_Details (booking_id, category_id, quantity, price) VALUES (?, ?, ?, ?)";
    
    // SQL query để lấy tất cả booking offline
    private static final String GET_ALL_OFFLINE_BOOKINGS =
        "SELECT b.id, b.user_id, b.hotel_id, b.room_type, b.number_of_rooms, b.transport_id, " +
        "b.transport_fee, b.total_price, b.booking_date, b.booking_source, b.created_by, " +
        "b.status, b.created_at, b.updated_at, b.check_in_date, b.check_out_date, b.booking_code " +
        "FROM Bookings b " +
        "LEFT JOIN Hotels h ON b.hotel_id = h.id " +
        "WHERE b.booking_source = 'OFFLINE' " +
        "ORDER BY b.booking_date DESC";
    
    /**
     * Lưu booking offline vào database
     * 
     * @param booking Đối tượng Booking cần lưu
     * @param checkinDate Ngày check-in (String format: yyyy-MM-dd)
     * @param checkoutDate Ngày check-out (String format: yyyy-MM-dd)
     * @param numAdults Số người lớn
     * @param numChildren Số trẻ em
     * @param bookingCode Mã booking
     * @param notes Ghi chú
     * @param serviceCart Danh sách dịch vụ (List<Map<String, Object>>)
     * @return ID của booking vừa được tạo, -1 nếu lỗi
     */
    public int saveOfflineBooking(Booking booking, String checkinDate, String checkoutDate, 
                                   int numAdults, int numChildren, String bookingCode, 
                                   String notes, java.util.List<java.util.Map<String, Object>> serviceCart) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);
            
            // Parse ngày tháng
            java.sql.Date checkin = null;
            java.sql.Date checkout = null;
            try {
                if (checkinDate != null && !checkinDate.isEmpty()) {
                    checkin = java.sql.Date.valueOf(checkinDate);
                }
                if (checkoutDate != null && !checkoutDate.isEmpty()) {
                    checkout = java.sql.Date.valueOf(checkoutDate);
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Invalid date format: " + e.getMessage());
            }
            
            // Lưu booking
            try (PreparedStatement ps = conn.prepareStatement(INSERT_OFFLINE_BOOKING, Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, booking.getHotelId());
                ps.setString(2, booking.getRoomType());
                ps.setInt(3, booking.getNumberOfRooms());
                ps.setDouble(4, booking.getTotalPrice());
                ps.setObject(5, booking.getCreatedBy()); // ID của nhân viên tạo booking
                ps.setDate(6, checkin);
                ps.setDate(7, checkout);
                ps.setInt(8, numAdults);
                ps.setInt(9, numChildren);
                ps.setString(10, bookingCode);
                ps.setString(11, notes);
                
                LOGGER.info(String.format(
                    "Executing INSERT_OFFLINE_BOOKING with hotelId=%s, roomType=%s, numberOfRooms=%d, totalPrice=%.2f, createdBy=%s, checkin=%s, checkout=%s, adults=%d, children=%d, bookingCode=%s",
                    booking.getHotelId(),
                    booking.getRoomType(),
                    booking.getNumberOfRooms(),
                    booking.getTotalPrice(),
                    booking.getCreatedBy(),
                    checkin,
                    checkout,
                    numAdults,
                    numChildren,
                    bookingCode
                ));
                
                int affected = ps.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Insert booking failed");
                }
                
                // Lấy ID vừa được tạo
                int bookingId;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        bookingId = keys.getInt(1);
                        LOGGER.info("Generated booking ID: " + bookingId);
                    } else {
                        throw new SQLException("No booking id generated");
                    }
                }
                
                // Lưu booking details (dịch vụ) nếu có
                if (serviceCart != null && !serviceCart.isEmpty()) {
                    try (PreparedStatement psDetail = conn.prepareStatement(INSERT_BOOKING_DETAIL)) {
                        for (java.util.Map<String, Object> service : serviceCart) {
                            int categoryId = getIntValue(service, "categoryId");
                            int quantity = getIntValue(service, "quantity");
                            double price = getDoubleValue(service, "price", 0);
                            
                            // Nếu không có categoryId, thử lấy từ id hoặc serviceId
                            if (categoryId == 0) {
                                categoryId = getIntValue(service, "id");
                                if (categoryId == 0) {
                                    categoryId = getIntValue(service, "serviceId");
                                }
                            }
                            
                            if (categoryId > 0 && quantity > 0) {
                                psDetail.setInt(1, bookingId);
                                psDetail.setInt(2, categoryId);
                                psDetail.setInt(3, quantity);
                                psDetail.setDouble(4, price);
                                psDetail.addBatch();
                            }
                        }
                        psDetail.executeBatch();
                    }
                }
                
                conn.commit();
                LOGGER.info("Saved offline booking with ID: " + bookingId);
                return bookingId;
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {}
            }
            LOGGER.log(Level.SEVERE, "Error saving offline booking", e);
            System.err.println("Lỗi lưu booking offline: " + e.getMessage());
            throw new RuntimeException("Lỗi lưu booking offline: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }
    
    /**
     * Lấy tất cả booking offline từ database
     * 
     * @return Danh sách booking offline
     */
    public List<Booking> getAllOfflineBookings() {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_OFFLINE_BOOKINGS)) {
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = mapResultSetToBooking(resultSet);
                    bookings.add(booking);
                }
            }
            
            LOGGER.info("Retrieved " + bookings.size() + " offline bookings");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving offline bookings", e);
        }
        
        return bookings;
    }
    
    // Hàm hỗ trợ để lấy giá trị int từ Map
    private int getIntValue(java.util.Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0;
        if (value instanceof Integer) return (Integer) value;
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    // Hàm hỗ trợ để lấy giá trị double từ Map
    private double getDoubleValue(java.util.Map<String, Object> map, String key, double defaultValue) {
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
    
    /**
     * Get bookings by user ID with pagination
     * 
     * @param userId User ID
     * @param offset Offset for pagination
     * @param limit Limit for pagination
     * @return List of bookings
     */
    public List<Booking> getBookingsByUserId(int userId, int offset, int limit) {
        List<Booking> bookings = new ArrayList<>();
        
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return bookings;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BOOKINGS_BY_USER_ID)) {
            
            statement.setInt(1, userId);
            statement.setInt(2, offset);
            statement.setInt(3, limit);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = mapResultSetToBooking(resultSet);
                    bookings.add(booking);
                }
            }
            
            LOGGER.info("Retrieved " + bookings.size() + " bookings for user ID: " + userId);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving bookings for user ID: " + userId, e);
        }
        
        return bookings;
    }
    
    /**
     * Get booking by ID
     * 
     * @param bookingId Booking ID
     * @return Booking object or null
     */
    public Booking getBookingById(int bookingId) {
        if (bookingId <= 0) {
            LOGGER.warning("Invalid booking ID: " + bookingId);
            return null;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BOOKING_BY_ID)) {
            
            statement.setInt(1, bookingId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Booking booking = mapResultSetToBooking(resultSet);
                    LOGGER.info("Booking found with ID: " + bookingId);
                    return booking;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving booking with ID: " + bookingId, e);
        }
        
        LOGGER.info("No booking found with ID: " + bookingId);
        return null;
    }
    
    /**
     * Get booking details with service categories
     * 
     * @param bookingId Booking ID
     * @return List of booking details
     */
    public List<BookingDetail> getBookingDetails(int bookingId) {
        List<BookingDetail> details = new ArrayList<>();
        
        if (bookingId <= 0) {
            LOGGER.warning("Invalid booking ID: " + bookingId);
            return details;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BOOKING_DETAILS)) {
            
            statement.setInt(1, bookingId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    BookingDetail detail = new BookingDetail();
                    detail.setId(resultSet.getInt("id"));
                    detail.setBookingId(resultSet.getInt("booking_id"));
                    detail.setCategoryId(resultSet.getInt("category_id"));
                    detail.setQuantity(resultSet.getInt("quantity"));
                    detail.setPrice(resultSet.getDouble("price"));
                    detail.setCategoryName(resultSet.getString("category_name"));
                    
                    details.add(detail);
                }
            }
            
            LOGGER.info("Retrieved " + details.size() + " details for booking ID: " + bookingId);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving booking details for booking ID: " + bookingId, e);
        }
        
        return details;
    }
    
    /**
     * Get booking count by user ID
     * 
     * @param userId User ID
     * @return Total booking count
     */
    public int getBookingCount(int userId) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return 0;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BOOKING_COUNT_BY_USER_ID)) {
            
            statement.setInt(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    LOGGER.info("Booking count for user ID " + userId + ": " + count);
                    return count;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting bookings for user ID: " + userId, e);
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Booking object
     * 
     * @param resultSet ResultSet from database query
     * @return Booking object
     * @throws SQLException if mapping fails
     */
    private Booking mapResultSetToBooking(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setId(resultSet.getInt("id"));
        
        // user_id có thể null
        int userId = resultSet.getInt("user_id");
        if (!resultSet.wasNull()) {
            booking.setUserId(userId);
        }
        
        booking.setHotelId(resultSet.getInt("hotel_id"));
        booking.setRoomType(resultSet.getString("room_type"));
        booking.setNumberOfRooms(resultSet.getInt("number_of_rooms"));
        
        // transport_id có thể null
        int transportId = resultSet.getInt("transport_id");
        if (!resultSet.wasNull()) {
            booking.setTransportId(transportId);
        }
        
        booking.setTransportFee(resultSet.getDouble("transport_fee"));
        booking.setTotalPrice(resultSet.getDouble("total_price"));
        
        Timestamp bookingDate = resultSet.getTimestamp("booking_date");
        if (bookingDate != null) {
            booking.setBookingDate(new Date(bookingDate.getTime()));
        }
        
        booking.setBookingSource(resultSet.getString("booking_source"));
        
        // created_by có thể null
        int createdBy = resultSet.getInt("created_by");
        if (!resultSet.wasNull()) {
            booking.setCreatedBy(createdBy);
        }
        
        booking.setStatus(resultSet.getString("status"));
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(new Date(createdAt.getTime()));
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        if (updatedAt != null) {
            booking.setUpdatedAt(new Date(updatedAt.getTime()));
        }
        
        // check_in_date và check_out_date
        try {
            java.sql.Date checkInDate = resultSet.getDate("check_in_date");
            if (checkInDate != null) {
                booking.setCheckInDate(new Date(checkInDate.getTime()));
            }
        } catch (SQLException e) {
            // Trường có thể không tồn tại trong một số query
        }
        
        try {
            java.sql.Date checkOutDate = resultSet.getDate("check_out_date");
            if (checkOutDate != null) {
                booking.setCheckOutDate(new Date(checkOutDate.getTime()));
            }
        } catch (SQLException e) {
            // Trường có thể không tồn tại trong một số query
        }
        
        // booking_code
        try {
            String bookingCode = resultSet.getString("booking_code");
            if (bookingCode != null) {
                booking.setBookingCode(bookingCode);
            }
        } catch (SQLException e) {
            // Trường có thể không tồn tại trong một số query
        }
        
        return booking;
    }

    public int sumBookedRooms(int hotelId, String roomType, java.time.LocalDate checkIn, java.time.LocalDate checkOut) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(SUM_OVERLAP_BOOKED_ROOMS)) {
            statement.setInt(1, hotelId);
            statement.setString(2, roomType);
            statement.setDate(3, java.sql.Date.valueOf(checkIn));
            statement.setDate(4, java.sql.Date.valueOf(checkOut));
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error summing booked rooms", e);
        }
        return 0;
    }

    public int createBookingTransactional(Booking booking,
                                          String bookingCode,
                                          java.time.LocalDate checkIn,
                                          java.time.LocalDate checkOut,
                                          int numAdults,
                                          int numChildren,
                                          List<entity.BookingDetail> addons,
                                          java.util.Map<Integer, String> addonNames) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(INSERT_BOOKING, Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, booking.getUserId());
                ps.setObject(2, booking.getHotelId());
                ps.setString(3, booking.getRoomType());
                ps.setInt(4, booking.getNumberOfRooms());
                ps.setDouble(5, booking.getTotalPrice());
                ps.setObject(6, booking.getCreatedBy());
                ps.setDate(7, java.sql.Date.valueOf(checkIn));
                ps.setDate(8, java.sql.Date.valueOf(checkOut));
                ps.setInt(9, numAdults);
                ps.setInt(10, numChildren);
                ps.setString(11, bookingCode);
                int affected = ps.executeUpdate();
                if (affected == 0) throw new SQLException("Insert booking failed");

                int bookingId;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) bookingId = keys.getInt(1); else throw new SQLException("No booking id");
                }

                if (addons != null && !addons.isEmpty()) {
                    try (PreparedStatement psAddon = conn.prepareStatement(INSERT_ADDON)) {
                        for (entity.BookingDetail ad : addons) {
                            psAddon.setInt(1, bookingId);
                            // categoryName carries addon_type (MEAL or WELLNESS)
                            psAddon.setString(2, ad.getCategoryName());
                            // categoryId carries reference_id (meal_id or wellness_id)
                            psAddon.setInt(3, ad.getCategoryId());
                            // Get name from map if provided, otherwise use default
                            String addonName = "";
                            if (addonNames != null && addonNames.containsKey(ad.getCategoryId())) {
                                addonName = addonNames.get(ad.getCategoryId());
                            } else {
                                // Fallback to default name
                                addonName = ad.getCategoryName().equals("MEAL") ? 
                                    "Meal Service #" + ad.getCategoryId() : 
                                    "Wellness Service #" + ad.getCategoryId();
                            }
                            psAddon.setString(4, addonName);
                            psAddon.setDouble(5, ad.getPrice());
                            psAddon.setInt(6, ad.getQuantity());
                            psAddon.addBatch();
                        }
                        psAddon.executeBatch();
                    }
                }

                // Insert payment pending with description
                String insertPayment = "INSERT INTO Payments (booking_id, transaction_id, currency, payment_method, payment_date, amount, status, description) VALUES (?,?,?,?,GETDATE(),?,?,?)";
                try (PreparedStatement psPay = conn.prepareStatement(insertPayment)) {
                    psPay.setInt(1, bookingId);
                    psPay.setString(2, null);
                    psPay.setString(3, "VND");
                    psPay.setString(4, "CASH");
                    psPay.setDouble(5, booking.getTotalPrice());
                    psPay.setString(6, "PENDING");
                    psPay.setString(7, "BOOK-" + bookingCode);
                    psPay.executeUpdate();
                }

                conn.commit();
                return bookingId;
            }

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            LOGGER.log(Level.SEVERE, "Error creating booking transactionally", e);
            return -1;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
