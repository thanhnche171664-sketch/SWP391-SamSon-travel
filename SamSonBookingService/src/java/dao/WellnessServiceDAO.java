package dao;

import entity.WellnessService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WellnessServiceDAO {
    
    private static final Logger LOGGER = Logger.getLogger(WellnessServiceDAO.class.getName());
    
    // Lấy tất cả wellness services đang active
    public List<WellnessService> getAllActiveServices() {
        List<WellnessService> services = new ArrayList<>();
        String sql = "SELECT * FROM Wellness_Services WHERE status = 'ACTIVE' ORDER BY base_price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                services.add(mapResultSetToWellnessService(rs));
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} active wellness services", services.size());
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAllActiveServices", e);
        }
        
        return services;
    }
    
    // Lấy wellness service theo ID
    public WellnessService getServiceById(int wellnessId) {
        String sql = "SELECT * FROM Wellness_Services WHERE wellness_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, wellnessId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.log(Level.INFO, "Found wellness service with ID: {0}", wellnessId);
                    return mapResultSetToWellnessService(rs);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getServiceById: " + wellnessId, e);
        }
        
        LOGGER.log(Level.WARNING, "Wellness service not found with ID: {0}", wellnessId);
        return null;
    }
    
    // Lấy services theo hotel
    public List<WellnessService> getServicesByHotel(int hotelId) {
        List<WellnessService> services = new ArrayList<>();
        String sql = "SELECT * FROM Wellness_Services WHERE hotel_id = ? AND status = 'ACTIVE' ORDER BY service_name";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, hotelId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToWellnessService(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} wellness services for hotel ID: {1}", new Object[]{services.size(), hotelId});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getServicesByHotel: " + hotelId, e);
        }
        
        return services;
    }
    
    // Tìm kiếm services theo tên
    public List<WellnessService> searchServicesByName(String keyword) {
        List<WellnessService> services = new ArrayList<>();
        String sql = "SELECT * FROM Wellness_Services WHERE (service_name LIKE ? OR description LIKE ?) AND status = 'ACTIVE' ORDER BY service_name";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToWellnessService(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Found {0} wellness services matching keyword: {1}", new Object[]{services.size(), keyword});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in searchServicesByName: " + keyword, e);
        }
        
        return services;
    }
    
    // Lấy services theo price range
    public List<WellnessService> getServicesByPriceRange(double minPrice, double maxPrice) {
        List<WellnessService> services = new ArrayList<>();
        String sql = "SELECT * FROM Wellness_Services WHERE base_price BETWEEN ? AND ? AND status = 'ACTIVE' ORDER BY base_price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToWellnessService(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Found {0} wellness services in price range {1}-{2}", 
                new Object[]{services.size(), minPrice, maxPrice});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getServicesByPriceRange", e);
        }
        
        return services;
    }
    
    /**
     * Map ResultSet to WellnessService object (DRY principle)
     */
    private WellnessService mapResultSetToWellnessService(ResultSet rs) throws SQLException {
        WellnessService service = new WellnessService();
        service.setWellnessId(rs.getInt("wellness_id"));
        service.setHotelId(rs.getInt("hotel_id"));
        service.setCategoryId(rs.getInt("category_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setDescription(rs.getString("description"));
        service.setBasePrice(rs.getDouble("base_price"));
        
        // Handle nullable fields
        Object durationObj = rs.getObject("duration_minutes");
        service.setDurationMinutes(durationObj != null ? rs.getInt("duration_minutes") : null);
        
        service.setOperatingHours(rs.getString("operating_hours"));
        
        Object capacityObj = rs.getObject("capacity");
        service.setCapacity(capacityObj != null ? rs.getInt("capacity") : null);
        
        service.setStatus(rs.getString("status"));
        service.setCreatedAt(rs.getDate("created_at"));
        service.setUpdatedAt(rs.getDate("updated_at"));
        
        return service;
    }
}
