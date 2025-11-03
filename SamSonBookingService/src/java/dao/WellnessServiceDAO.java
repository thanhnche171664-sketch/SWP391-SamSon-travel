package dao;
import entity.WellnessService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.WellnessService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WellnessServiceDAO {
    private static final Logger LOGGER = Logger.getLogger(WellnessServiceDAO.class.getName());


    public List<WellnessService> getAll(int page, int pageSize, String statusFilter) {
        List<WellnessService> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Wellness_Services WHERE 1=1");
        if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
            sql.append(" AND status = ?");
        }
        sql.append(" ORDER BY wellness_id ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
                ps.setString(idx++, statusFilter.toUpperCase());
            }
            ps.setInt(idx++, (page - 1) * pageSize);
            ps.setInt(idx, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getAll] SQL Error: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }

    public int countAll(String statusFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Wellness_Services WHERE 1=1");
        if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
            sql.append(" AND status = ?");
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
                ps.setString(1, statusFilter.toUpperCase());
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[countAll] SQL Error: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public WellnessService getById(int id) {
        String sql = "SELECT * FROM Wellness_Services WHERE wellness_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getById] SQL Error: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean addWellnessService(WellnessService ws) {
        final String sql = """
            INSERT INTO Wellness_Services 
            (hotel_id, category_id, service_name, description, base_price, 
             duration_minutes, operating_hours, capacity, image_url, status, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())
            """;

        LOGGER.info(() -> "[INSERT Wellness_Services] hotel_id=" + ws.getHotelId()
                + ", category_id=" + ws.getCategoryId()
                + ", service_name=" + ws.getServiceName()
                + ", base_price=" + ws.getBasePrice()
                + ", duration_minutes=" + ws.getDurationMinutes()
                + ", operating_hours=" + ws.getOperatingHours()
                + ", capacity=" + ws.getCapacity()
                + ", status=" + (ws.getStatus() != null ? ws.getStatus().toUpperCase() : "ACTIVE"));

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ws.getHotelId());
            ps.setInt(2, ws.getCategoryId());
            ps.setString(3, ws.getServiceName());
            ps.setString(4, ws.getDescription());
            ps.setDouble(5, ws.getBasePrice());

            
            if (ws.getDurationMinutes() > 0) ps.setInt(6, ws.getDurationMinutes());
            else ps.setNull(6, Types.INTEGER);

            
            String hours = (ws.getOperatingHours() != null && !ws.getOperatingHours().isEmpty())
                    ? ws.getOperatingHours() : "08:00–22:00";
            ps.setString(7, hours);

            
            if (ws.getCapacity() > 0) ps.setInt(8, ws.getCapacity());
            else ps.setNull(8, Types.INTEGER);

            ps.setString(9, ws.getImageUrl());

            String status = (ws.getStatus() != null) ? ws.getStatus().toUpperCase() : "ACTIVE";
            ps.setString(10, status);

            int n = ps.executeUpdate();
            if (n <= 0) {
                throw new SQLException("No rows inserted (executeUpdate returned 0)");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int genId = rs.getInt(1);
                    LOGGER.info("[INSERT Wellness_Services] generated id = " + genId);
                } else {
                    LOGGER.info("[INSERT Wellness_Services] no generated key returned");
                }
            }
            return true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    () -> "[addWellnessService] SQL Error: state=" + e.getSQLState()
                            + ", code=" + e.getErrorCode()
                            + ", msg=" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    
    public boolean updateWellnessService(WellnessService ws) {
        final String sql = """
            UPDATE Wellness_Services
               SET hotel_id = ?, 
                   category_id = ?, 
                   service_name = ?, 
                   description = ?, 
                   base_price = ?, 
                   duration_minutes = ?, 
                   operating_hours = ?, 
                   capacity = ?, 
                   image_url = ?, 
                   status = ?, 
                   updated_at = GETDATE()
             WHERE wellness_id = ?
            """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ws.getHotelId());
            ps.setInt(2, ws.getCategoryId());
            ps.setString(3, ws.getServiceName());
            ps.setString(4, ws.getDescription());
            ps.setDouble(5, ws.getBasePrice());

            if (ws.getDurationMinutes() > 0) ps.setInt(6, ws.getDurationMinutes());
            else ps.setNull(6, Types.INTEGER);

            String hours = (ws.getOperatingHours() != null && !ws.getOperatingHours().isEmpty())
                    ? ws.getOperatingHours() : "08:00–22:00";
            ps.setString(7, hours);

            if (ws.getCapacity() > 0) ps.setInt(8, ws.getCapacity());
            else ps.setNull(8, Types.INTEGER);

            ps.setString(9, ws.getImageUrl());

            String status = (ws.getStatus() != null) ? ws.getStatus().toUpperCase() : "ACTIVE";
            ps.setString(10, status);

            ps.setInt(11, ws.getWellnessId());

            int n = ps.executeUpdate();
            return n > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    () -> "[updateWellnessService] SQL Error: state=" + e.getSQLState()
                            + ", code=" + e.getErrorCode()
                            + ", msg=" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean deleteWellnessService(int id) {
        String sql = "DELETE FROM Wellness_Services WHERE wellness_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int n = ps.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[deleteWellnessService] SQL Error: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public List<WellnessService> searchByName(String keyword, int page, int pageSize, String statusFilter) {
        List<WellnessService> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Wellness_Services WHERE service_name LIKE ?");
        if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
            sql.append(" AND status = ?");
        }
        sql.append(" ORDER BY wellness_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setString(idx++, "%" + keyword + "%");
            if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
                ps.setString(idx++, statusFilter.toUpperCase());
            }
            ps.setInt(idx++, (page - 1) * pageSize);
            ps.setInt(idx, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[searchByName] SQL Error: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }

    public int countSearch(String keyword, String statusFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Wellness_Services WHERE service_name LIKE ?");
        if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
            sql.append(" AND status = ?");
        }

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, "%" + keyword + "%");
            if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
                ps.setString(2, statusFilter.toUpperCase());
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[countSearch] SQL Error: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<WellnessService> getWellnessServicesByHotelId(int hotelId) {
        return getFilteredList("SELECT * FROM Wellness_Services WHERE hotel_id = ?", hotelId);
    }

    public List<WellnessService> getWellnessServicesByCategoryId(int categoryId) {
        return getFilteredList("SELECT * FROM Wellness_Services WHERE category_id = ?", categoryId);
    }

    private List<WellnessService> getFilteredList(String sql, int value) {
        List<WellnessService> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getFilteredList] SQL Error: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<WellnessService> getWellnessServicesByPriceRange(double minPrice, double maxPrice) {
        List<WellnessService> list = new ArrayList<>();
        String sql = "SELECT * FROM Wellness_Services WHERE base_price BETWEEN ? AND ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getWellnessServicesByPriceRange] SQL Error: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return list;
    }


    private WellnessService mapResultSet(ResultSet rs) throws SQLException {
        int duration = rs.getInt("duration_minutes");
        if (rs.wasNull()) duration = 0;

        int capacity = rs.getInt("capacity");
        if (rs.wasNull()) capacity = 0;

        return new WellnessService(
                rs.getInt("wellness_id"),
                rs.getInt("hotel_id"),
                rs.getInt("category_id"),
                rs.getString("service_name"),
                rs.getString("description"),
                rs.getDouble("base_price"),
                duration,
                rs.getString("operating_hours"),
                capacity,
                rs.getString("image_url"),
                rs.getString("status"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}
