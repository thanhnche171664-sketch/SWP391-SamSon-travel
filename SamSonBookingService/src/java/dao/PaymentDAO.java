/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PaymentDAO {

    private static final String INSERT_PAYMENT =
        "INSERT INTO Payments (booking_id, transaction_id, currency, payment_method, payment_date, amount, status) " +
        "VALUES (?,?,?,?,?,?,'PAID')";

    private static final String UPDATE_STATUS =
        "UPDATE Payments SET status = ?, payment_date = GETDATE() WHERE payment_id = ?";

    // Placeholder for future use if needed

    public int createPaid(int bookingId, String transactionId, String currency, String method, double amount) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_PAYMENT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bookingId);
            ps.setString(2, transactionId);
            ps.setString(3, currency);
            ps.setString(4, method);
            ps.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
            ps.setDouble(6, amount);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void updateStatus(int paymentId, String status) throws SQLException {
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_STATUS)) {
            ps.setString(1, status);
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        }
    }
}
