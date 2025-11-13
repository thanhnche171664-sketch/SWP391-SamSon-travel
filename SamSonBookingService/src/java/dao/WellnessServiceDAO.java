package dao;

import entity.WellnessService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

public class WellnessServiceDAO {
    private static final Logger LOGGER = Logger.getLogger(WellnessServiceDAO.class.getName());

    // Lấy tất cả wellness services đang active (không phân trang)
    public List<WellnessService> getAllActiveWellnessServices() {
        List<WellnessService> list = new ArrayList<>();
        String sql = "SELECT * FROM Wellness_Services WHERE status = 'ACTIVE' ORDER BY wellness_id DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getAllActiveWellnessServices] SQL Error: {0}", e.getMessage());
            System.err.println("WellnessServiceDAO: Lỗi khi lấy danh sách wellness services: " + e.getMessage());
        }

        return list;
    }
    
    public List<WellnessService> getAll(int page, int pageSize, String statusFilter) {
        List<WellnessService> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Wellness_Services WHERE 1=1");

        if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
            sql.append(" AND status = ?");
        }

        sql.append(" ORDER BY wellness_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
                ps.setString(index++, statusFilter.toUpperCase());
            }

            ps.setInt(index++, (page - 1) * pageSize);
            ps.setInt(index, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getAll] SQL Error", e);
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
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[countAll] SQL Error", e);
        }
        return 0;
    }

    public WellnessService getById(int id) {
        String sql = "SELECT * FROM Wellness_Services WHERE wellness_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getById] SQL Error", e);
        }
        return null;
    }

    public boolean existsByHotelAndName(int hotelId, String serviceName) {
        String sql = "SELECT 1 FROM Wellness_Services WHERE hotel_id = ? AND service_name = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            ps.setString(2, serviceName.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[existsByHotelAndName] SQL Error", e);
        }
        return false;
    }

    public boolean addWellnessService(WellnessService ws) {
        String sql = """
            INSERT INTO Wellness_Services 
            (hotel_id, category_id, service_name, description, base_price, 
             duration_minutes, operating_hours, capacity, image_url, status, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())
            """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ws.getHotelId());
            ps.setInt(2, ws.getCategoryId());
            ps.setString(3, ws.getServiceName());
            ps.setString(4, ws.getDescription());
            ps.setDouble(5, ws.getBasePrice());

            if (ws.getDurationMinutes() > 0) {
                ps.setInt(6, ws.getDurationMinutes());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            String hours = (ws.getOperatingHours() != null && !ws.getOperatingHours().isEmpty())
                    ? ws.getOperatingHours()
                    : "08:00–22:00";
            ps.setString(7, hours);

            if (ws.getCapacity() > 0) {
                ps.setInt(8, ws.getCapacity());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setString(9, ws.getImageUrl()); // có thể null

            String status = (ws.getStatus() != null) ? ws.getStatus().toUpperCase() : "ACTIVE";
            ps.setString(10, status);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace(); 
            LOGGER.log(Level.SEVERE, "[addWellnessService] SQL Error", e);
            return false;
        }
    }

    public boolean updateWellnessService(WellnessService ws) {
        String sql = """
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

            if (ws.getDurationMinutes() > 0) {
                ps.setInt(6, ws.getDurationMinutes());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            String hours = (ws.getOperatingHours() != null && !ws.getOperatingHours().isEmpty())
                    ? ws.getOperatingHours()
                    : "08:00–22:00";
            ps.setString(7, hours);

            if (ws.getCapacity() > 0) {
                ps.setInt(8, ws.getCapacity());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setString(9, ws.getImageUrl());

            String status = (ws.getStatus() != null) ? ws.getStatus().toUpperCase() : "ACTIVE";
            ps.setString(10, status);

            ps.setInt(11, ws.getWellnessId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[updateWellnessService] SQL Error", e);
            return false;
        }
    }

    // Xóa dịch vụ
    public boolean deleteWellnessService(int id) {
        String sql = "DELETE FROM Wellness_Services WHERE wellness_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[deleteWellnessService] SQL Error", e);
            return false;
        }
    }

    // Search theo tên + phân trang + filter status
    public List<WellnessService> searchByName(String keyword, int page, int pageSize, String statusFilter) {
        List<WellnessService> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Wellness_Services WHERE service_name LIKE ?");

        if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
            sql.append(" AND status = ?");
        }

        sql.append(" ORDER BY wellness_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setString(index++, "%" + keyword + "%");

            if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
                ps.setString(index++, statusFilter.toUpperCase());
            }

            ps.setInt(index++, (page - 1) * pageSize);
            ps.setInt(index, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[searchByName] SQL Error", e);
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
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[countSearch] SQL Error", e);
        }

        return 0;
    }

    // Một vài hàm filter khác nếu cần
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
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getFilteredList] SQL Error", e);
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
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[getWellnessServicesByPriceRange] SQL Error", e);
        }

        return list;
    }

    // Map resultset → entity
    private WellnessService mapResultSet(ResultSet rs) throws SQLException {
        int duration = rs.getInt("duration_minutes");
        if (rs.wasNull()) {
            duration = 0;
        }

        int capacity = rs.getInt("capacity");
        if (rs.wasNull()) {
            capacity = 0;
        }

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
