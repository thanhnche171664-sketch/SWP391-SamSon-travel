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
        booking.setUserId(resultSet.getInt("user_id"));
        booking.setHotelId(resultSet.getInt("hotel_id"));
        booking.setRoomType(resultSet.getString("room_type"));
        booking.setNumberOfRooms(resultSet.getInt("number_of_rooms"));
        booking.setTransportId(resultSet.getInt("transport_id"));
        booking.setTransportFee(resultSet.getDouble("transport_fee"));
        booking.setTotalPrice(resultSet.getDouble("total_price"));
        booking.setBookingDate(new Date(resultSet.getTimestamp("booking_date").getTime()));
        booking.setBookingSource(resultSet.getString("booking_source"));
        booking.setCreatedBy(resultSet.getInt("created_by"));
        booking.setStatus(resultSet.getString("status"));
        booking.setCreatedAt(new Date(resultSet.getTimestamp("created_at").getTime()));
        booking.setUpdatedAt(new Date(resultSet.getTimestamp("updated_at").getTime()));
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
