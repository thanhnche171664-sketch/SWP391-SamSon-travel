package dao;

import entity.BookingView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingManagerDAO {

    // Lấy toàn bộ booking (nếu bạn vẫn cần)
    public List<BookingView> getAllBookingViews() throws SQLException {
        List<BookingView> list = new ArrayList<>();

        String sql =
            "SELECT b.id, b.booking_code, " +
            "       u.name  AS customer_name, " +
            "       u.email AS customer_email, " +
            "       h.name  AS hotel_name, " +
            "       b.room_type, b.number_of_rooms, " +
            "       ts.vehicle_name AS transport_name, " +
            "       b.total_price, b.status, b.booking_date " +
            "FROM Bookings b " +
            "LEFT JOIN Users u ON b.user_id = u.id " +
            "LEFT JOIN Hotels h ON b.hotel_id = h.id " +
            "LEFT JOIN TransportServices ts ON b.transport_id = ts.transport_id " +
            "ORDER BY b.booking_date DESC, b.id DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BookingView v = new BookingView();
                v.setId(rs.getInt("id"));
                v.setBookingCode(rs.getString("booking_code"));
                v.setCustomerName(rs.getString("customer_name"));
                v.setEmail(rs.getString("customer_email"));
                v.setHotelName(rs.getString("hotel_name"));
                v.setRoomType(rs.getString("room_type"));
                v.setNumberOfRooms(rs.getInt("number_of_rooms"));
                v.setTransportName(rs.getString("transport_name"));
                v.setTotalPrice(rs.getDouble("total_price"));
                v.setStatus(rs.getString("status"));
                v.setBookingDate(rs.getTimestamp("booking_date"));

                list.add(v);
            }
        }

        return list;
    }

    // 🔍 Lọc & tìm kiếm theo keyword + status
    public List<BookingView> searchBookingViews(String keyword, String status) throws SQLException {
        List<BookingView> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT b.id, b.booking_code, " +
            "       u.name  AS customer_name, " +
            "       u.email AS customer_email, " +
            "       h.name  AS hotel_name, " +
            "       b.room_type, b.number_of_rooms, " +
            "       ts.vehicle_name AS transport_name, " +
            "       b.total_price, b.status, b.booking_date " +
            "FROM Bookings b " +
            "LEFT JOIN Users u ON b.user_id = u.id " +
            "LEFT JOIN Hotels h ON b.hotel_id = h.id " +
            "LEFT JOIN TransportServices ts ON b.transport_id = ts.transport_id " +
            "WHERE 1 = 1 "
        );

        List<Object> params = new ArrayList<>();

        // keyword: booking_code / tên khách / email / tên khách sạn
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND ( b.booking_code LIKE ? " +
                       "    OR u.name LIKE ? " +
                       "    OR u.email LIKE ? " +
                       "    OR h.name LIKE ? ) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        // status: pending / confirmed / canceled (bỏ qua nếu 'all' hoặc null)
        if (status != null && !"all".equalsIgnoreCase(status)) {
            sql.append(" AND b.status = ? ");
            params.add(status);
        }

        sql.append(" ORDER BY b.booking_date DESC, b.id DESC ");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // set parameter
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookingView v = new BookingView();
                    v.setId(rs.getInt("id"));
                    v.setBookingCode(rs.getString("booking_code"));
                    v.setCustomerName(rs.getString("customer_name"));
                    v.setEmail(rs.getString("customer_email"));
                    v.setHotelName(rs.getString("hotel_name"));
                    v.setRoomType(rs.getString("room_type"));
                    v.setNumberOfRooms(rs.getInt("number_of_rooms"));
                    v.setTransportName(rs.getString("transport_name"));
                    v.setTotalPrice(rs.getDouble("total_price"));
                    v.setStatus(rs.getString("status"));
                    v.setBookingDate(rs.getTimestamp("booking_date"));

                    list.add(v);
                }
            }
        }

        return list;
    }

    // ✔ Cập nhật trạng thái booking
    public boolean updateBookingStatus(int id, String newStatus) throws SQLException {
        String sql = "UPDATE Bookings SET status = ?, updated_at = GETDATE() WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // 🗑 Xóa booking
    public boolean deleteBooking(int id) throws SQLException {
        String sql = "DELETE FROM Bookings WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // 🔍 Lấy chi tiết 1 booking theo id
    public BookingView getBookingViewById(int id) throws SQLException {
        String sql =
            "SELECT b.id, b.booking_code, b.status, b.booking_date, b.booking_source, " +
            "       b.room_type, b.number_of_rooms, b.total_price, b.transport_fee, " +
            "       b.check_in_date, b.check_out_date, b.num_adults, b.num_children, b.notes, " +
            "       u.name  AS customer_name, " +
            "       u.email AS customer_email, " +
            "       u.phone AS customer_phone, " +
            "       u.address AS customer_address, " +
            "       h.name  AS hotel_name, " +
            "       h.address AS hotel_address, " +
            "       ts.vehicle_name AS transport_name, " +
            "       ts.vehicle_type, ts.pickup_location, ts.departure_time " +
            "FROM Bookings b " +
            "LEFT JOIN Users u ON b.user_id = u.id " +
            "LEFT JOIN Hotels h ON b.hotel_id = h.id " +
            "LEFT JOIN TransportServices ts ON b.transport_id = ts.transport_id " +
            "WHERE b.id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BookingView v = new BookingView();
                    v.setId(rs.getInt("id"));
                    v.setBookingCode(rs.getString("booking_code"));
                    v.setStatus(rs.getString("status"));
                    v.setBookingDate(rs.getTimestamp("booking_date"));
                    v.setBookingSource(rs.getString("booking_source"));

                    v.setRoomType(rs.getString("room_type"));
                    v.setNumberOfRooms(rs.getInt("number_of_rooms"));
                    v.setTotalPrice(rs.getDouble("total_price"));
                    v.setTransportFee(rs.getDouble("transport_fee"));

                    v.setCheckInDate(rs.getDate("check_in_date"));
                    v.setCheckOutDate(rs.getDate("check_out_date"));
                    v.setNumAdults(rs.getInt("num_adults"));
                    v.setNumChildren(rs.getInt("num_children"));
                    v.setNotes(rs.getString("notes"));

                    v.setCustomerName(rs.getString("customer_name"));
                    v.setEmail(rs.getString("customer_email"));
                    v.setPhone(rs.getString("customer_phone"));
                    v.setCustomerAddress(rs.getString("customer_address"));

                    v.setHotelName(rs.getString("hotel_name"));
                    v.setHotelAddress(rs.getString("hotel_address"));

                    v.setTransportName(rs.getString("transport_name"));
                    v.setTransportType(rs.getString("vehicle_type"));
                    v.setPickupLocation(rs.getString("pickup_location"));
                    v.setDepartureTime(rs.getTimestamp("departure_time"));

                    return v;
                }
            }
        }

        return null;
    }
}
