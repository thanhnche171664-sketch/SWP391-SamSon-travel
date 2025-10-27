package dao;

import entity.MealService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MealServiceDAO {
    
    private static final Logger LOGGER = Logger.getLogger(MealServiceDAO.class.getName());
    
    // Lấy tất cả meal services đang active
    public List<MealService> getAllActiveMeals() {
        List<MealService> meals = new ArrayList<>();
        String sql = "SELECT * FROM Meal_Services WHERE status = 'ACTIVE' ORDER BY meal_type, created_at DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                meals.add(mapResultSetToMealService(rs));
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} active meal services", meals.size());
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAllActiveMeals", e);
        }
        
        return meals;
    }
    
    // Lấy meal service theo ID
    public MealService getMealById(int mealId) {
        String sql = "SELECT * FROM Meal_Services WHERE meal_id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, mealId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.log(Level.INFO, "Found meal service with ID: {0}", mealId);
                    return mapResultSetToMealService(rs);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getMealById: " + mealId, e);
        }
        
        LOGGER.log(Level.WARNING, "Meal service not found with ID: {0}", mealId);
        return null;
    }
    
    // Lấy meals theo hotel
    public List<MealService> getMealsByHotel(int hotelId) {
        List<MealService> meals = new ArrayList<>();
        String sql = "SELECT * FROM Meal_Services WHERE hotel_id = ? AND status = 'ACTIVE' ORDER BY meal_type";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, hotelId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    meals.add(mapResultSetToMealService(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} meals for hotel ID: {1}", new Object[]{meals.size(), hotelId});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getMealsByHotel: " + hotelId, e);
        }
        
        return meals;
    }
    
    // Lấy meals theo loại (BREAKFAST, LUNCH, DINNER)
    public List<MealService> getMealsByType(String mealType) {
        List<MealService> meals = new ArrayList<>();
        String sql = "SELECT * FROM Meal_Services WHERE meal_type = ? AND status = 'ACTIVE' ORDER BY price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, mealType);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    meals.add(mapResultSetToMealService(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} meals of type: {1}", new Object[]{meals.size(), mealType});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getMealsByType: " + mealType, e);
        }
        
        return meals;
    }
    
    // Tìm kiếm meals theo keyword (tên hoặc mô tả)
    public List<MealService> searchMeals(String keyword) {
        List<MealService> meals = new ArrayList<>();
        String sql = "SELECT * FROM Meal_Services WHERE (meal_type LIKE ? OR description LIKE ?) AND status = 'ACTIVE' ORDER BY meal_type";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    meals.add(mapResultSetToMealService(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Found {0} meals matching keyword: {1}", new Object[]{meals.size(), keyword});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in searchMeals: " + keyword, e);
        }
        
        return meals;
    }
    
    /**
     * Map ResultSet to MealService object (DRY principle)
     */
    private MealService mapResultSetToMealService(ResultSet rs) throws SQLException {
        MealService meal = new MealService();
        meal.setMealId(rs.getInt("meal_id"));
        meal.setHotelId(rs.getInt("hotel_id"));
        meal.setCategoryId(rs.getInt("category_id"));
        meal.setMealType(rs.getString("meal_type"));
        meal.setMealDate(rs.getDate("meal_date"));
        meal.setDescription(rs.getString("description"));
        meal.setPrice(rs.getDouble("price"));
        meal.setStatus(rs.getString("status"));
        meal.setCreatedAt(rs.getDate("created_at"));
        meal.setUpdatedAt(rs.getDate("updated_at"));
        return meal;
    }
}
