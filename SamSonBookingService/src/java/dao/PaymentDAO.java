package dao;

import entity.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentDAO {
    
    private static final Logger LOGGER = Logger.getLogger(PaymentDAO.class.getName());
    
    private static final String GET_PAYMENT_BY_BOOKING_ID = 
        "SELECT payment_id, booking_id, transaction_id, currency, payment_method, payment_date, amount, status, description " +
        "FROM Payments WHERE booking_id = ?";
    
    private static final String GET_PENDING_PAYMENTS = 
        "SELECT p.payment_id, p.booking_id, p.transaction_id, p.currency, p.payment_method, p.payment_date, " +
        "p.amount, p.status, p.description, b.booking_code, b.user_id, b.total_price, b.check_in_date, b.check_out_date, " +
        "u.name as full_name, u.email, h.name as hotel_name " +
        "FROM Payments p " +
        "INNER JOIN Bookings b ON p.booking_id = b.id " +
        "INNER JOIN Users u ON b.user_id = u.id " +
        "INNER JOIN Hotels h ON b.hotel_id = h.id " +
        "WHERE p.status = 'PENDING' " +
        "ORDER BY p.payment_date DESC";
    
    private static final String UPDATE_PAYMENT_STATUS = 
        "UPDATE Payments SET status = ?, payment_date = GETDATE() WHERE payment_id = ?";
    
    private static final String UPDATE_BOOKING_STATUS = 
        "UPDATE Bookings SET status = ?, updated_at = GETDATE() WHERE id = ?";
    
    public Payment getPaymentByBookingId(int bookingId) {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_PAYMENT_BY_BOOKING_ID)) {
            
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting payment by booking ID: " + bookingId, e);
        }
        return null;
    }
    
    public List<PaymentWithBooking> getPendingPayments() {
        List<PaymentWithBooking> payments = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_PENDING_PAYMENTS)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentWithBooking pwb = new PaymentWithBooking();
                    pwb.payment = mapResultSetToPayment(rs);
                    pwb.bookingCode = rs.getString("booking_code");
                    pwb.userId = rs.getInt("user_id");
                    pwb.totalPrice = rs.getDouble("total_price");
                    pwb.checkInDate = rs.getDate("check_in_date");
                    pwb.checkOutDate = rs.getDate("check_out_date");
                    pwb.userName = rs.getString("full_name");
                    pwb.userEmail = rs.getString("email");
                    pwb.hotelName = rs.getString("hotel_name");
                    payments.add(pwb);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting pending payments", e);
        }
        return payments;
    }
    
    public boolean updatePaymentStatus(int paymentId, String status) {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_PAYMENT_STATUS)) {
            
            ps.setString(1, status);
            ps.setInt(2, paymentId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating payment status", e);
            return false;
        }
    }
    
    public boolean updateBookingStatus(int bookingId, String status) {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_BOOKING_STATUS)) {
            
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating booking status", e);
            return false;
        }
    }
    
    public boolean confirmPayment(int paymentId, int bookingId) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);
            
            // Update payment status to PAID
            try (PreparedStatement psPayment = conn.prepareStatement(UPDATE_PAYMENT_STATUS)) {
                psPayment.setString(1, "PAID");
                psPayment.setInt(2, paymentId);
                psPayment.executeUpdate();
            }
            
            // Update booking status to confirmed
            try (PreparedStatement psBooking = conn.prepareStatement(UPDATE_BOOKING_STATUS)) {
                psBooking.setString(1, "confirmed");
                psBooking.setInt(2, bookingId);
                psBooking.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            LOGGER.log(Level.SEVERE, "Error confirming payment", e);
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
    
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setBookingId(rs.getInt("booking_id"));
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setCurrency(rs.getString("currency"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentDate(rs.getTimestamp("payment_date"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setStatus(rs.getString("status"));
        return payment;
    }
    
    public static class PaymentWithBooking {
        public Payment payment;
        public String bookingCode;
        public int userId;
        public double totalPrice;
        public java.sql.Date checkInDate;
        public java.sql.Date checkOutDate;
        public String userName;
        public String userEmail;
        public String hotelName;
        
        // Getter methods for JSP EL
        public Payment getPayment() {
            return payment;
        }
        
        public String getBookingCode() {
            return bookingCode;
        }
        
        public int getUserId() {
            return userId;
        }
        
        public double getTotalPrice() {
            return totalPrice;
        }
        
        public java.sql.Date getCheckInDate() {
            return checkInDate;
        }
        
        public java.sql.Date getCheckOutDate() {
            return checkOutDate;
        }
        
        public String getUserName() {
            return userName;
        }
        
        public String getUserEmail() {
            return userEmail;
        }
        
        public String getHotelName() {
            return hotelName;
        }
    }
}
