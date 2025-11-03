/*
 * DAO for BookingItem
 */
package dao;

import entity.BookingItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingItemDAO {

    private static final String SELECT_BY_BOOKING =
        "SELECT id, booking_id, item_type, item_id, title_snapshot, meta_json, start_time, end_time, " +
        "quantity, unit_price, total_price, created_at FROM Booking_Items WHERE booking_id = ? ORDER BY id";

    private static final String INSERT_ITEM =
        "INSERT INTO Booking_Items (booking_id, item_type, item_id, title_snapshot, meta_json, start_time, end_time, " +
        "quantity, unit_price, total_price) VALUES (?,?,?,?,?,?,?,?,?,?)";

    public List<BookingItem> findByBookingId(int bookingId) {
        List<BookingItem> items = new ArrayList<>();
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_BOOKING)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(map(rs));
                }
            }
        } catch (SQLException ex) {
            // log as needed
        }
        return items;
    }

    public void insert(Connection con, BookingItem item) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_ITEM)) {
            ps.setInt(1, item.getBookingId());
            ps.setString(2, item.getItemType());
            if (item.getItemId() == null) ps.setNull(3, java.sql.Types.INTEGER); else ps.setInt(3, item.getItemId());
            ps.setString(4, item.getTitleSnapshot());
            ps.setString(5, item.getMetaJson());
            if (item.getStartTime() == null) ps.setNull(6, java.sql.Types.TIMESTAMP); else ps.setTimestamp(6, new java.sql.Timestamp(item.getStartTime().getTime()));
            if (item.getEndTime() == null) ps.setNull(7, java.sql.Types.TIMESTAMP); else ps.setTimestamp(7, new java.sql.Timestamp(item.getEndTime().getTime()));
            ps.setInt(8, item.getQuantity());
            ps.setDouble(9, item.getUnitPrice());
            ps.setDouble(10, item.getTotalPrice());
            ps.executeUpdate();
        }
    }

    private BookingItem map(ResultSet rs) throws SQLException {
        BookingItem bi = new BookingItem();
        bi.setId(rs.getInt("id"));
        bi.setBookingId(rs.getInt("booking_id"));
        bi.setItemType(rs.getString("item_type"));
        int itemId = rs.getInt("item_id");
        bi.setItemId(rs.wasNull() ? null : itemId);
        bi.setTitleSnapshot(rs.getString("title_snapshot"));
        bi.setMetaJson(rs.getString("meta_json"));
        java.sql.Timestamp st = rs.getTimestamp("start_time");
        if (st != null) bi.setStartTime(new Date(st.getTime()));
        java.sql.Timestamp et = rs.getTimestamp("end_time");
        if (et != null) bi.setEndTime(new Date(et.getTime()));
        bi.setQuantity(rs.getInt("quantity"));
        bi.setUnitPrice(rs.getDouble("unit_price"));
        bi.setTotalPrice(rs.getDouble("total_price"));
        java.sql.Timestamp ct = rs.getTimestamp("created_at");
        if (ct != null) bi.setCreatedAt(new Date(ct.getTime()));
        return bi;
    }
}


