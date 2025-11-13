package dao;

import entity.MealService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealServiceDAO extends DBContext {
    
    // Lấy tất cả meal services đang active
    public List<MealService> getAllActiveMealServices() {
        List<MealService> services = new ArrayList<>();
        String sql = "SELECT * FROM Meal_Services WHERE status = 'ACTIVE' ORDER BY meal_date DESC, meal_type";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                services.add(mapResultSetToMealService(rs));
            }
        } catch (Exception e) {
            System.err.println("MealServiceDAO: Lỗi khi lấy danh sách meal services: " + e.getMessage());
            e.printStackTrace();
        }
        return services;
    }
    
    public List<MealService> getMealServicesByHotelId(int hotelId) {
        List<MealService> services = new ArrayList<>();
        String sql = "SELECT * FROM Meal_Services WHERE hotel_id = ? ORDER BY meal_date DESC, meal_type";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hotelId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                services.add(mapResultSetToMealService(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return services;
    }
    
    public MealService getMealServiceById(int mealId) {
        String sql = "SELECT * FROM Meal_Services WHERE meal_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mealId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToMealService(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int insertMealServiceAndReturnId(MealService service) {
        String sql = "INSERT INTO Meal_Services (hotel_id, category_id, meal_type, meal_date, description, price, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, service.getHotelId());
            ps.setInt(2, service.getCategoryId());
            ps.setString(3, service.getMealType());
            ps.setDate(4, new java.sql.Date(service.getMealDate().getTime()));
            ps.setString(5, service.getDescription());
            ps.setDouble(6, service.getPrice());
            ps.setString(7, service.getStatus());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public boolean updateMealService(MealService service) {
        String sql = "UPDATE Meal_Services SET category_id = ?, meal_type = ?, meal_date = ?, " +
                    "description = ?, price = ?, status = ?, updated_at = GETDATE() WHERE meal_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, service.getCategoryId());
            ps.setString(2, service.getMealType());
            ps.setDate(3, new java.sql.Date(service.getMealDate().getTime()));
            ps.setString(4, service.getDescription());
            ps.setDouble(5, service.getPrice());
            ps.setString(6, service.getStatus());
            ps.setInt(7, service.getMealId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteMealService(int mealId) {
        String sql = "DELETE FROM Meal_Services WHERE meal_id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mealId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private MealService mapResultSetToMealService(ResultSet rs) throws SQLException {
        MealService service = new MealService();
        service.setMealId(rs.getInt("meal_id"));
        service.setHotelId(rs.getInt("hotel_id"));
        service.setCategoryId(rs.getInt("category_id"));
        service.setMealType(rs.getString("meal_type"));
        service.setMealDate(rs.getDate("meal_date"));
        service.setDescription(rs.getString("description"));
        service.setPrice(rs.getDouble("price"));
        service.setStatus(rs.getString("status"));
        service.setCreatedAt(rs.getTimestamp("created_at"));
        service.setUpdatedAt(rs.getTimestamp("updated_at"));
        return service;
    }
}
