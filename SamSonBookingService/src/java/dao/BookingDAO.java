/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Booking;
import entity.BookingDetail;
import entity.BookingItem;
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
        "transport_fee, subtotal, discount_total, tax_total, total_price, currency, " +
        "booking_date, booking_source, created_by, status, created_at, updated_at, " +
        "tour_id, schedule_id, package_id, guest_count, contact_name, contact_email, contact_phone, check_in_date, check_out_date " +
        "FROM bookings WHERE id = ?";
    
    private static final String GET_BOOKING_DETAILS = 
        "SELECT bd.id, bd.booking_id, bd.category_id, bd.quantity, bd.price, " +
        "sc.category_name " +
        "FROM booking_details bd " +
        "INNER JOIN service_categories sc ON bd.category_id = sc.category_id " +
        "WHERE bd.booking_id = ?";
    
    private static final String GET_BOOKING_COUNT_BY_USER_ID = 
        "SELECT COUNT(*) FROM bookings WHERE user_id = ?";
    
    private static final String UPDATE_STATUS =
        "UPDATE Bookings SET status = ?, updated_at = GETDATE() WHERE id = ?";

    private static final String GET_PENDING_BOOKINGS =
        "SELECT id, user_id, hotel_id, room_type, number_of_rooms, transport_id, transport_fee, " +
        "subtotal, discount_total, tax_total, total_price, currency, booking_date, booking_source, created_by, status, created_at, updated_at, " +
        "tour_id, schedule_id, package_id, guest_count, contact_name, contact_email, contact_phone, check_in_date, check_out_date " +
        "FROM Bookings WHERE status = 'pending' ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    
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
        int tourId = resultSet.getInt("tour_id"); booking.setTourId(resultSet.wasNull()? null : tourId);
        int scheduleId = resultSet.getInt("schedule_id"); booking.setScheduleId(resultSet.wasNull()? null : scheduleId);
        int packageId = resultSet.getInt("package_id"); booking.setPackageId(resultSet.wasNull()? null : packageId);
        int guestCount = resultSet.getInt("guest_count"); booking.setGuestCount(resultSet.wasNull()? null : guestCount);
        booking.setHotelId(resultSet.getInt("hotel_id"));
        booking.setRoomType(resultSet.getString("room_type"));
        booking.setNumberOfRooms(resultSet.getInt("number_of_rooms"));
        booking.setTransportId(resultSet.getInt("transport_id"));
        booking.setTransportFee(resultSet.getDouble("transport_fee"));
        double sb = resultSet.getDouble("subtotal"); booking.setSubtotal(resultSet.wasNull()? null : sb);
        double dd = resultSet.getDouble("discount_total"); booking.setDiscountTotal(resultSet.wasNull()? null : dd);
        double tt = resultSet.getDouble("tax_total"); booking.setTaxTotal(resultSet.wasNull()? null : tt);
        booking.setTotalPrice(resultSet.getDouble("total_price"));
        booking.setCurrency(resultSet.getString("currency"));
        booking.setBookingDate(new Date(resultSet.getTimestamp("booking_date").getTime()));
        booking.setBookingSource(resultSet.getString("booking_source"));
        booking.setCreatedBy(resultSet.getInt("created_by"));
        booking.setStatus(resultSet.getString("status"));
        booking.setCreatedAt(new Date(resultSet.getTimestamp("created_at").getTime()));
        booking.setUpdatedAt(new Date(resultSet.getTimestamp("updated_at").getTime()));
        booking.setContactName(resultSet.getString("contact_name"));
        booking.setContactEmail(resultSet.getString("contact_email"));
        booking.setContactPhone(resultSet.getString("contact_phone"));
        java.sql.Date ci = resultSet.getDate("check_in_date"); if (ci != null) booking.setCheckInDate(new Date(ci.getTime()));
        java.sql.Date co = resultSet.getDate("check_out_date"); if (co != null) booking.setCheckOutDate(new Date(co.getTime()));
        return booking;
    }

    // Create booking via stored procedure (preferred for atomic slot handling)
    public Integer createBookingUsingSP(int userId, int tourId, int scheduleId, Integer packageId,
                                        int guestCount,
                                        String contactName, String contactEmail, String contactPhone,
                                        double subtotal, double discountTotal, double taxTotal, double totalPrice,
                                        String currency) {
        String sql = "{ call dbo.sp_CreateTourBooking(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
        try (Connection connection = DBContext.getConnection();
             java.sql.CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, userId);
            cs.setInt(2, tourId);
            cs.setInt(3, scheduleId);
            if (packageId == null) cs.setNull(4, java.sql.Types.INTEGER); else cs.setInt(4, packageId);
            cs.setInt(5, guestCount);
            cs.setString(6, contactName);
            cs.setString(7, contactEmail);
            cs.setString(8, contactPhone);
            cs.setDouble(9, subtotal);
            cs.setDouble(10, discountTotal);
            cs.setDouble(11, taxTotal);
            cs.setDouble(12, totalPrice);
            cs.setString(13, currency);
            cs.registerOutParameter(14, java.sql.Types.INTEGER);
            cs.execute();
            return cs.getInt(14);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating booking via SP", e);
            return null;
        }
    }

    // Load booking with items
    public Booking getFullById(int bookingId) {
        Booking b = getBookingById(bookingId);
        if (b == null) return null;
        BookingItemDAO itemDAO = new BookingItemDAO();
        List<BookingItem> items = itemDAO.findByBookingId(bookingId);
        b.setBookingItems(items);
        List<BookingDetail> details = getBookingDetails(bookingId);
        b.setBookingDetails(details);
        return b;
    }

    public boolean updateStatus(int bookingId, String status) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating booking status", e);
            return false;
        }
    }

    public List<Booking> getPendingBookings(int offset, int limit) {
        List<Booking> list = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_PENDING_BOOKINGS)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading pending bookings", e);
        }
        return list;
    }
}
