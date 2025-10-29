/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Discount;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DiscountDAO - Data Access Object for Discount operations
 * Manages discounts and promotional offers
 * 
 * @author SamSon Travel Team
 */
public class DiscountDAO {
    
    private static final Logger LOGGER = Logger.getLogger(DiscountDAO.class.getName());
    
    // SQL Queries
    private static final String GET_ALL_DISCOUNTS = 
        "SELECT * FROM Discounts WHERE status = 'ACTIVE' ORDER BY created_at DESC";
    
    private static final String GET_ACTIVE_DISCOUNTS = 
        "SELECT * FROM Discounts WHERE status = 'ACTIVE' AND start_date <= GETDATE() " +
        "AND end_date >= GETDATE() ORDER BY created_at DESC";
    
    private static final String GET_DISCOUNT_BY_ID = 
        "SELECT * FROM Discounts WHERE discount_id = ?";
    
    private static final String GET_DISCOUNTS_BY_CATEGORY = 
        "SELECT * FROM Discounts WHERE category_id = ? AND status = 'ACTIVE' " +
        "ORDER BY created_at DESC";
    
    private static final String INSERT_DISCOUNT = 
        "INSERT INTO Discounts (category_id, discount_type, value, start_date, end_date, status) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_DISCOUNT = 
        "UPDATE Discounts SET category_id = ?, discount_type = ?, value = ?, " +
        "start_date = ?, end_date = ?, status = ?, updated_at = GETDATE() WHERE discount_id = ?";
    
    private static final String DELETE_DISCOUNT = 
        "UPDATE Discounts SET status = 'INACTIVE', updated_at = GETDATE() WHERE discount_id = ?";
    
    private static final String GET_DISCOUNT_COUNT = 
        "SELECT COUNT(*) FROM Discounts WHERE status = 'ACTIVE'";
    
    /**
     * Get all active discounts
     * @return List of all active discounts
     */
    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_DISCOUNTS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                discounts.add(mapResultSetToDiscount(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all discounts", e);
        }
        return discounts;
    }
    
    /**
     * Get currently active discounts (within date range)
     * @return List of currently active discounts
     */
    public List<Discount> getActiveDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ACTIVE_DISCOUNTS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                discounts.add(mapResultSetToDiscount(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting active discounts", e);
        }
        return discounts;
    }
    
    /**
     * Get discount by ID
     * @param discountId Discount ID
     * @return Discount object or null if not found
     */
    public Discount getDiscountById(int discountId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_DISCOUNT_BY_ID)) {
            
            statement.setInt(1, discountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDiscount(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting discount by ID: " + discountId, e);
        }
        return null;
    }
    
    /**
     * Get discounts by category
     * @param categoryId Category ID
     * @return List of discounts for the category
     */
    public List<Discount> getDiscountsByCategory(int categoryId) {
        List<Discount> discounts = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_DISCOUNTS_BY_CATEGORY)) {
            
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    discounts.add(mapResultSetToDiscount(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting discounts for category: " + categoryId, e);
        }
        return discounts;
    }
    
    /**
     * Insert new discount
     * @param discount Discount object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertDiscount(Discount discount) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_DISCOUNT)) {
            
            statement.setInt(1, discount.getCategoryId());
            statement.setString(2, discount.getDiscountType());
            statement.setDouble(3, discount.getValue());
            statement.setTimestamp(4, discount.getStartDate());
            statement.setTimestamp(5, discount.getEndDate());
            statement.setString(6, discount.getStatus());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting discount", e);
            return false;
        }
    }
    
    /**
     * Update existing discount
     * @param discount Discount object to update
     * @return true if successful, false otherwise
     */
    public boolean updateDiscount(Discount discount) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DISCOUNT)) {
            
            statement.setInt(1, discount.getCategoryId());
            statement.setString(2, discount.getDiscountType());
            statement.setDouble(3, discount.getValue());
            statement.setTimestamp(4, discount.getStartDate());
            statement.setTimestamp(5, discount.getEndDate());
            statement.setString(6, discount.getStatus());
            statement.setInt(7, discount.getDiscountId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating discount: " + discount.getDiscountId(), e);
            return false;
        }
    }
    
    /**
     * Delete discount (soft delete)
     * @param discountId Discount ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteDiscount(int discountId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_DISCOUNT)) {
            
            statement.setInt(1, discountId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting discount: " + discountId, e);
            return false;
        }
    }
    
    /**
     * Get total count of active discounts
     * @return Total count
     */
    public int getDiscountCount() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_DISCOUNT_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting discount count", e);
        }
        return 0;
    }
    
    /**
     * Map ResultSet to Discount object
     * @param resultSet ResultSet from database query
     * @return Discount object
     * @throws SQLException if mapping fails
     */
    private Discount mapResultSetToDiscount(ResultSet resultSet) throws SQLException {
        Discount discount = new Discount();
        discount.setDiscountId(resultSet.getInt("discount_id"));
        discount.setCategoryId(resultSet.getInt("category_id"));
        discount.setDiscountType(resultSet.getString("discount_type"));
        discount.setValue(resultSet.getDouble("value"));
        discount.setStartDate(resultSet.getTimestamp("start_date"));
        discount.setEndDate(resultSet.getTimestamp("end_date"));
        discount.setStatus(resultSet.getString("status"));
        discount.setCreatedAt(resultSet.getTimestamp("created_at"));
        discount.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return discount;
    }
}
