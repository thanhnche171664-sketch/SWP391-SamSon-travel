/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.TransportService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gamel
 */
public class TransportServiceDAO {

    private static final Logger LOGGER = Logger.getLogger(TransportServiceDAO.class.getName());

    public List<TransportService> getAll() {
        List<TransportService> list = new ArrayList<>();
        String sql = "SELECT * FROM TransportServices ORDER BY transport_id DESC";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetSafe(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getAll] SQL Error: {0}", e.getMessage());
        }
        return list;
    }

    public TransportService getById(int id) {
        String sql = "SELECT * FROM TransportServices WHERE transport_id = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetSafe(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getById] SQL Error: {0}", e.getMessage());
        }
        return null;
    }

    public List<TransportService> getTransportServicesByHotelId(int hotelId) {
        List<TransportService> list = new ArrayList<>();

        // Thử lấy transport services có hotel_id = ? hoặc hotel_id IS NULL
        // Nếu lỗi do cột hotel_id không tồn tại, sẽ fallback về lấy tất cả
        String sql = "SELECT * FROM TransportServices WHERE hotel_id = ? OR hotel_id IS NULL ORDER BY vehicle_type, price";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hotelId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetSafe(rs));
                }
            }
        } catch (SQLException e) {
            // Nếu lỗi do cột hotel_id không tồn tại, lấy tất cả transport services
            if (e.getMessage() != null && e.getMessage().contains("hotel_id")) {
                LOGGER.log(Level.WARNING, "[getTransportServicesByHotelId] Column hotel_id does not exist, fetching all transports: {0}", e.getMessage());
            } else {
                LOGGER.log(Level.SEVERE, "[getTransportServicesByHotelId] SQL Error: {0}", e.getMessage());
            }
        }

        // Nếu không có cột hotel_id hoặc query trên trả về rỗng, lấy tất cả transport services
        if (list.isEmpty()) {
            String fallbackSql = "SELECT * FROM TransportServices ORDER BY vehicle_type, price";
            try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(fallbackSql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetSafe(rs));
                }
                LOGGER.log(Level.INFO, "[getTransportServicesByHotelId] Loaded {0} transport services (fallback mode)", list.size());
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "[getTransportServicesByHotelId] Fallback SQL Error: {0}", ex.getMessage());
            }
        } else {
            LOGGER.log(Level.INFO, "[getTransportServicesByHotelId] Loaded {0} transport services for hotel {1}", new Object[]{list.size(), hotelId});
        }

        return list;
    }

    /**
     * Kiểm tra xem cột có tồn tại trong bảng không
     */
    private boolean checkColumnExists(String tableName, String columnName) {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "[checkColumnExists] Error checking column: {0}", e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Map ResultSet an toàn, xử lý trường hợp cột hotel_id không tồn tại
     */
    private TransportService mapResultSetSafe(ResultSet rs) throws SQLException {
        int hotelId = 0;
        try {
            hotelId = rs.getInt("hotel_id");
            if (rs.wasNull()) {
                hotelId = 0; // Default value if NULL
            }
        } catch (SQLException e) {
            // Cột hotel_id không tồn tại, sử dụng giá trị mặc định
            hotelId = 0;
        }

        return new TransportService(
                rs.getInt("transport_id"),
                hotelId,
                rs.getInt("category_id"),
                rs.getString("vehicle_type"),
                rs.getString("vehicle_name"),
                rs.getString("description"),
                rs.getString("pickup_location"),
                rs.getTimestamp("departure_time"),
                rs.getDouble("price"),
                rs.getInt("capacity"),
                rs.getInt("current_passengers"),
                rs.getString("image"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }

    public boolean insert(TransportService ts) {
        String sql = """
            INSERT INTO TransportServices
            (hotel_id, category_id, vehicle_type, vehicle_name, description,
             pickup_location, departure_time, price, capacity, image, current_passengers, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, GETDATE(), GETDATE())
            """;
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ts.getHotelId());
            ps.setInt(2, ts.getCategoryId());
            ps.setString(3, ts.getVehicleType());
            ps.setString(4, ts.getVehicleName());
            ps.setString(5, ts.getDescription());
            ps.setString(6, ts.getPickupLocation());
            ps.setTimestamp(7, new Timestamp(ts.getDepartureTime().getTime()));
            ps.setDouble(8, ts.getPrice());
            ps.setInt(9, ts.getCapacity());
            ps.setString(10, ts.getImage());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[insert] SQL Error: {0}", e.getMessage());
            return false;
        }
    }

    public boolean update(TransportService ts) {
        String sql = """
            UPDATE TransportServices
               SET hotel_id = ?,
                   category_id = ?,
                   vehicle_type = ?,
                   vehicle_name = ?,
                   description = ?,
                   pickup_location = ?,
                   departure_time = ?,
                   price = ?,
                   capacity = ?,
                   image = ?,
                   updated_at = GETDATE()
             WHERE transport_id = ?
            """;
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ts.getHotelId());
            ps.setInt(2, ts.getCategoryId());
            ps.setString(3, ts.getVehicleType());
            ps.setString(4, ts.getVehicleName());
            ps.setString(5, ts.getDescription());
            ps.setString(6, ts.getPickupLocation());
            ps.setTimestamp(7, new Timestamp(ts.getDepartureTime().getTime()));
            ps.setDouble(8, ts.getPrice());
            ps.setInt(9, ts.getCapacity());
            ps.setString(10, ts.getImage());
            ps.setInt(11, ts.getTransportId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[update] SQL Error: {0}", e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM TransportServices WHERE transport_id = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[delete] SQL Error: {0}", e.getMessage());
            return false;
        }
    }

    private TransportService mapResultSet(ResultSet rs) throws SQLException {
        // Sử dụng mapResultSetSafe để xử lý an toàn
        return mapResultSetSafe(rs);
    }

    public List<TransportService> search(String keyword) {
        List<TransportService> list = new ArrayList<>();
        String sql = "SELECT * FROM TransportServices "
                + "WHERE vehicle_name LIKE ? OR pickup_location LIKE ? "
                + "ORDER BY transport_id ASC";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetSafe(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[search] SQL Error: {0}", e.getMessage());
        }
        return list;
    }

    public List<int[]> getAllHotels() {
        List<int[]> res = new ArrayList<>();
        String sql = "SELECT id, name FROM Hotels";
        try (Connection c = DBContext.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                res.add(new int[]{rs.getInt("id")});
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getAllHotels] SQL Error: {0}", e.getMessage());
        }
        return res;
    }

    public int countAll(String keyword) {
        String sql = "SELECT COUNT(*) FROM TransportServices";
        boolean hasKeyword = keyword != null && !keyword.isEmpty();

        if (hasKeyword) {
            sql += " WHERE vehicle_name LIKE ? OR pickup_location LIKE ?";
        }

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (hasKeyword) {
                String kw = "%" + keyword + "%";
                ps.setString(1, kw);
                ps.setString(2, kw);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[countAll] SQL Error: {0}", e.getMessage());
        }
        return 0;
    }

    public List<TransportService> getPagedList(String keyword, int page, int pageSize) {
        List<TransportService> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM TransportServices");
        boolean hasKeyword = keyword != null && !keyword.isEmpty();

        if (hasKeyword) {
            sql.append(" WHERE vehicle_name LIKE ? OR pickup_location LIKE ?");
        }
        sql.append(" ORDER BY transport_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (hasKeyword) {
                String kw = "%" + keyword + "%";
                ps.setString(idx++, kw);
                ps.setString(idx++, kw);
            }

            ps.setInt(idx++, (page - 1) * pageSize);
            ps.setInt(idx, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetSafe(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getPagedList] SQL Error: {0}", e.getMessage());
        }

        return list;
    }

    public boolean existsTransport(int hotelId,
            String vehicleName,
            String pickupLocation,
            Timestamp departureTime) {
        String sql = """
        SELECT COUNT(*) 
        FROM TransportServices
        WHERE hotel_id = ?
          AND vehicle_name = ?
          AND pickup_location = ?
          AND departure_time = ?
        """;
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            ps.setString(2, vehicleName);
            ps.setString(3, pickupLocation);
            ps.setTimestamp(4, departureTime);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    LOGGER.log(Level.INFO,
                            "[existsTransport] hotelId={0}, vehicleName={1}, pickupLocation={2}, departure={3}, count={4}",
                            new Object[]{hotelId, vehicleName, pickupLocation, departureTime, count});
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[existsTransport] SQL Error: {0}", e.getMessage());
        }
        return false;
    }

}
