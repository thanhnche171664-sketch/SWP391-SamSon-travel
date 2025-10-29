/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ServiceCategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServiceCategoryDAO - Data Access Object for ServiceCategory operations
 * Manages service categories with icons and display order
 * 
 * @author SamSon Travel Team
 */
public class ServiceCategoryDAO {
    
    private static final Logger LOGGER = Logger.getLogger(ServiceCategoryDAO.class.getName());
    
    // SQL Queries
    private static final String GET_ALL_CATEGORIES = 
        "SELECT * FROM ServiceCategories ORDER BY display_order ASC, category_name ASC";
    
    private static final String GET_CATEGORY_BY_ID = 
        "SELECT * FROM ServiceCategories WHERE category_id = ?";
    
    private static final String GET_CATEGORY_BY_CODE = 
        "SELECT * FROM ServiceCategories WHERE category_code = ?";
    
    private static final String INSERT_CATEGORY = 
        "INSERT INTO ServiceCategories (category_code, category_name, icon_class, display_order, description) " +
        "VALUES (?, ?, ?, ?, ?)";
    
    private static final String UPDATE_CATEGORY = 
        "UPDATE ServiceCategories SET category_code = ?, category_name = ?, icon_class = ?, " +
        "display_order = ?, description = ? WHERE category_id = ?";
    
    private static final String DELETE_CATEGORY = 
        "DELETE FROM ServiceCategories WHERE category_id = ?";
    
    private static final String GET_CATEGORY_COUNT = 
        "SELECT COUNT(*) FROM ServiceCategories";
    
    /**
     * Get all service categories ordered by display order
     * @return List of all service categories
     */
    public List<ServiceCategory> getAllCategories() {
        return getAllServiceCategories();
    }
    
    /**
     * Get all service categories ordered by display order
     * @return List of all service categories
     */
    public List<ServiceCategory> getAllServiceCategories() {
        List<ServiceCategory> categories = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_CATEGORIES);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                categories.add(mapResultSetToCategory(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all categories", e);
        }
        return categories;
    }
    
    /**
     * Get category by ID
     * @param categoryId Category ID
     * @return ServiceCategory object or null if not found
     */
    public ServiceCategory getCategoryById(int categoryId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CATEGORY_BY_ID)) {
            
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCategory(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting category by ID: " + categoryId, e);
        }
        return null;
    }
    
    /**
     * Get category by code
     * @param categoryCode Category code (HOTEL, TRANSPORT, MEAL, WELLNESS)
     * @return ServiceCategory object or null if not found
     */
    public ServiceCategory getCategoryByCode(String categoryCode) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CATEGORY_BY_CODE)) {
            
            statement.setString(1, categoryCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCategory(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting category by code: " + categoryCode, e);
        }
        return null;
    }
    
    /**
     * Insert new category
     * @param category ServiceCategory object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertCategory(ServiceCategory category) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_CATEGORY)) {
            
            statement.setString(1, category.getCategoryCode());
            statement.setString(2, category.getCategoryName());
            statement.setString(3, category.getIconClass());
            statement.setInt(4, category.getDisplayOrder());
            statement.setString(5, category.getDescription());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting category: " + category.getCategoryName(), e);
            return false;
        }
    }
    
    /**
     * Update existing category
     * @param category ServiceCategory object to update
     * @return true if successful, false otherwise
     */
    public boolean updateCategory(ServiceCategory category) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CATEGORY)) {
            
            statement.setString(1, category.getCategoryCode());
            statement.setString(2, category.getCategoryName());
            statement.setString(3, category.getIconClass());
            statement.setInt(4, category.getDisplayOrder());
            statement.setString(5, category.getDescription());
            statement.setInt(6, category.getCategoryId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating category: " + category.getCategoryId(), e);
            return false;
        }
    }
    
    /**
     * Delete category
     * @param categoryId Category ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY)) {
            
            statement.setInt(1, categoryId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting category: " + categoryId, e);
            return false;
        }
    }
    
    /**
     * Get total count of categories
     * @return Total count
     */
    public int getCategoryCount() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CATEGORY_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting category count", e);
        }
        return 0;
    }
    
    /**
     * Map ResultSet to ServiceCategory object
     * @param resultSet ResultSet from database query
     * @return ServiceCategory object
     * @throws SQLException if mapping fails
     */
    private ServiceCategory mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        ServiceCategory category = new ServiceCategory();
        category.setCategoryId(resultSet.getInt("category_id"));
        category.setCategoryCode(resultSet.getString("category_code"));
        category.setCategoryName(resultSet.getString("category_name"));
        category.setIconClass(resultSet.getString("icon_class"));
        category.setDisplayOrder(resultSet.getInt("display_order"));
        category.setDescription(resultSet.getString("description"));
        return category;
    }
}