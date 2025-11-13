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
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getAll] SQL Error: {0}", e.getMessage());
        }
        return list;
    }

    public TransportService getById(int id) {
        String sql = "SELECT * FROM TransportServices WHERE transport_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getById] SQL Error: {0}", e.getMessage());
        }
        return null;
    }

    public boolean insert(TransportService ts) {
        String sql = """
            INSERT INTO TransportServices
            (hotel_id, category_id, vehicle_type, vehicle_name, description,
             pickup_location, departure_time, price, capacity, image, current_passengers, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, GETDATE(), GETDATE())
            """;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[delete] SQL Error: {0}", e.getMessage());
            return false;
        }
    }

    private TransportService mapResultSet(ResultSet rs) throws SQLException {
        return new TransportService(
                rs.getInt("transport_id"),
                rs.getInt("hotel_id"),
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
                    list.add(mapResultSet(rs));
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
                    list.add(mapResultSet(rs)); 
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getPagedList] SQL Error: {0}", e.getMessage());
        }

        return list;
    }

}
