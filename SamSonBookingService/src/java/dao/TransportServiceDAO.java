package dao;

import entity.TransportService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransportServiceDAO {
    private static final Logger LOG = Logger.getLogger(TransportServiceDAO.class.getName());

    private TransportService map(ResultSet rs) throws SQLException {
        TransportService t = new TransportService();
        t.setTransportId(rs.getInt("transport_id"));
        t.setCategoryId(rs.getInt("category_id"));
        t.setVehicleType(rs.getString("vehicle_type"));
        t.setVehicleName(rs.getString("vehicle_name"));
        t.setDescription(rs.getString("description"));
        t.setPickupLocation(rs.getString("pickup_location"));
        Timestamp dep = rs.getTimestamp("departure_time");
        t.setDepartureTime(dep != null ? new java.util.Date(dep.getTime()) : null);
        t.setPrice(rs.getDouble("price"));
        t.setCapacity(rs.getInt("capacity"));
        t.setCurrentPassengers(rs.getInt("current_passengers"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setUpdatedAt(rs.getTimestamp("updated_at"));
        return t;
    }

    public List<TransportService> getAll(int page, int pageSize, String vehicleType, String keyword) {
        List<TransportService> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM TransportServices WHERE 1=1");

        if (vehicleType != null && !vehicleType.equalsIgnoreCase("all")) {
            sql.append(" AND vehicle_type = ?");
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND vehicle_name LIKE ?");
        }
        sql.append(" ORDER BY departure_time ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection c = DBContext.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int i = 1;
            if (vehicleType != null && !vehicleType.equalsIgnoreCase("all")) {
                ps.setString(i++, vehicleType.toUpperCase());
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(i++, "%" + keyword.trim() + "%");
            }
            ps.setInt(i++, (page - 1) * pageSize);
            ps.setInt(i, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            LOG.severe("[getAll] " + e.getMessage());
        }
        return list;
    }

    public int countAll(String vehicleType, String keyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM TransportServices WHERE 1=1");
        if (vehicleType != null && !vehicleType.equalsIgnoreCase("all")) {
            sql.append(" AND vehicle_type = ?");
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND vehicle_name LIKE ?");
        }

        try (Connection c = DBContext.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int i = 1;
            if (vehicleType != null && !vehicleType.equalsIgnoreCase("all")) {
                ps.setString(i++, vehicleType.toUpperCase());
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(i++, "%" + keyword.trim() + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.severe("[countAll] " + e.getMessage());
        }
        return 0;
    }

    public TransportService getById(int id) {
        String sql = "SELECT * FROM TransportServices WHERE transport_id = ?";
        try (Connection c = DBContext.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            LOG.severe("[getById] " + e.getMessage());
        }
        return null;
    }

    public boolean insert(TransportService t) {
        String sql = """
            INSERT INTO TransportServices
              (category_id, vehicle_type, vehicle_name, description, pickup_location, departure_time,
               price, capacity, current_passengers, created_at, updated_at)
            VALUES (?,?,?,?,?,?,?,?,0,GETDATE(),GETDATE())
        """;
        try (Connection c = DBContext.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getCategoryId());
            ps.setString(2, t.getVehicleType().toUpperCase());
            ps.setString(3, t.getVehicleName());
            ps.setString(4, t.getDescription());
            ps.setString(5, t.getPickupLocation());
            ps.setTimestamp(6, new Timestamp(t.getDepartureTime().getTime()));
            ps.setDouble(7, t.getPrice());
            ps.setInt(8, t.getCapacity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.severe("[insert] " + e.getMessage());
            return false;
        }
    }

    public boolean update(TransportService t) {
        String sql = """
            UPDATE TransportServices
               SET category_id=?, vehicle_type=?, vehicle_name=?, description=?, pickup_location=?,
                   departure_time=?, price=?, capacity=?, updated_at=GETDATE()
             WHERE transport_id=?
        """;
        try (Connection c = DBContext.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getCategoryId());
            ps.setString(2, t.getVehicleType().toUpperCase());
            ps.setString(3, t.getVehicleName());
            ps.setString(4, t.getDescription());
            ps.setString(5, t.getPickupLocation());
            ps.setTimestamp(6, new Timestamp(t.getDepartureTime().getTime()));
            ps.setDouble(7, t.getPrice());
            ps.setInt(8, t.getCapacity());
            ps.setInt(9, t.getTransportId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.severe("[update] " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM TransportServices WHERE transport_id = ?";
        try (Connection c = DBContext.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            int n = ps.executeUpdate();
            return n > 0;

        } catch (SQLException e) {
            System.err.println("[delete] SQL Error: state=" + e.getSQLState()
                   + ", code=" + e.getErrorCode() + ", msg=" + e.getMessage());
           throw new RuntimeException(e);
        }
   } 

}
