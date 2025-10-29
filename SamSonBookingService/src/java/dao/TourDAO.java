/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Tour;
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
 * TourDAO - Data Access Object for Tour operations
 * Provides comprehensive CRUD operations and specialized queries for tours
 * 
 * @author SamSon Travel Team
 */
public class TourDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TourDAO.class.getName());
    
    // SQL Queries
    private static final String GET_ALL_TOURS = 
        "SELECT * FROM Tours WHERE status = 'ACTIVE' ORDER BY created_at DESC";
    
    private static final String GET_FEATURED_TOURS = 
        "SELECT TOP 6 * FROM Tours WHERE status = 'ACTIVE' ORDER BY created_at DESC";
    
    private static final String GET_TOUR_BY_ID = 
        "SELECT * FROM Tours WHERE tour_id = ? AND status = 'ACTIVE'";
    
    private static final String SEARCH_TOURS = 
        "SELECT * FROM Tours WHERE status = 'ACTIVE' AND " +
        "(tour_name LIKE ? OR description LIKE ? OR highlights LIKE ?) " +
        "ORDER BY created_at DESC";
    
    private static final String GET_TOURS_BY_DIFFICULTY = 
        "SELECT * FROM Tours WHERE status = 'ACTIVE' AND difficulty_level = ? " +
        "ORDER BY created_at DESC";
    
    private static final String GET_TOURS_BY_PRICE_RANGE = 
        "SELECT * FROM Tours WHERE status = 'ACTIVE' AND base_price BETWEEN ? AND ? " +
        "ORDER BY base_price ASC";
    
    private static final String GET_TOURS_BY_DURATION = 
        "SELECT * FROM Tours WHERE status = 'ACTIVE' AND duration_days = ? " +
        "ORDER BY created_at DESC";
    
    private static final String INSERT_TOUR = 
        "INSERT INTO Tours (tour_name, description, duration_days, duration_nights, " +
        "base_price, featured_image, highlights, inclusions, exclusions, difficulty_level, " +
        "min_age, max_group_size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_TOUR = 
        "UPDATE Tours SET tour_name = ?, description = ?, duration_days = ?, duration_nights = ?, " +
        "base_price = ?, featured_image = ?, highlights = ?, inclusions = ?, exclusions = ?, " +
        "difficulty_level = ?, min_age = ?, max_group_size = ?, updated_at = GETDATE() " +
        "WHERE tour_id = ?";
    
    private static final String DELETE_TOUR = 
        "UPDATE Tours SET status = 'INACTIVE', updated_at = GETDATE() WHERE tour_id = ?";
    
    private static final String GET_TOUR_COUNT = 
        "SELECT COUNT(*) FROM Tours WHERE status = 'ACTIVE'";
    
    /**
     * Get all active tours
     * @return List of active tours
     */
    public List<Tour> getAllTours() {
        List<Tour> tours = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_TOURS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                tours.add(mapResultSetToTour(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all tours", e);
        }
        return tours;
    }
    
    /**
     * Get featured tours (top 6)
     * @return List of featured tours
     */
    public List<Tour> getFeaturedTours() {
        List<Tour> tours = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_FEATURED_TOURS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                tours.add(mapResultSetToTour(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting featured tours", e);
        }
        return tours;
    }
    
    /**
     * Get tour by ID
     * @param tourId Tour ID
     * @return Tour object or null if not found
     */
    public Tour getTourById(int tourId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TOUR_BY_ID)) {
            
            statement.setInt(1, tourId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToTour(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting tour by ID: " + tourId, e);
        }
        return null;
    }
    
    /**
     * Search tours by keyword
     * @param keyword Search keyword
     * @return List of matching tours
     */
    public List<Tour> searchTours(String keyword) {
        List<Tour> tours = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_TOURS)) {
            
            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tours.add(mapResultSetToTour(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching tours with keyword: " + keyword, e);
        }
        return tours;
    }
    
    /**
     * Get tours by difficulty level
     * @param difficultyLevel Difficulty level (EASY, MODERATE, HARD, EXPERT)
     * @return List of tours with specified difficulty
     */
    public List<Tour> getToursByDifficulty(String difficultyLevel) {
        List<Tour> tours = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TOURS_BY_DIFFICULTY)) {
            
            statement.setString(1, difficultyLevel);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tours.add(mapResultSetToTour(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting tours by difficulty: " + difficultyLevel, e);
        }
        return tours;
    }
    
    /**
     * Get tours by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of tours within price range
     */
    public List<Tour> getToursByPriceRange(double minPrice, double maxPrice) {
        List<Tour> tours = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TOURS_BY_PRICE_RANGE)) {
            
            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tours.add(mapResultSetToTour(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting tours by price range: " + minPrice + "-" + maxPrice, e);
        }
        return tours;
    }
    
    /**
     * Get tours by duration
     * @param durationDays Number of days
     * @return List of tours with specified duration
     */
    public List<Tour> getToursByDuration(int durationDays) {
        List<Tour> tours = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TOURS_BY_DURATION)) {
            
            statement.setInt(1, durationDays);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tours.add(mapResultSetToTour(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting tours by duration: " + durationDays, e);
        }
        return tours;
    }
    
    /**
     * Insert new tour
     * @param tour Tour object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertTour(Tour tour) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_TOUR)) {
            
            statement.setString(1, tour.getTourName());
            statement.setString(2, tour.getDescription());
            statement.setInt(3, tour.getDurationDays());
            statement.setInt(4, tour.getDurationNights());
            statement.setDouble(5, tour.getBasePrice());
            statement.setString(6, tour.getFeaturedImage());
            statement.setString(7, tour.getHighlights());
            statement.setString(8, tour.getInclusions());
            statement.setString(9, tour.getExclusions());
            statement.setString(10, tour.getDifficultyLevel());
            statement.setInt(11, tour.getMinAge());
            statement.setInt(12, tour.getMaxGroupSize());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting tour: " + tour.getTourName(), e);
            return false;
        }
    }
    
    /**
     * Update existing tour
     * @param tour Tour object to update
     * @return true if successful, false otherwise
     */
    public boolean updateTour(Tour tour) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TOUR)) {
            
            statement.setString(1, tour.getTourName());
            statement.setString(2, tour.getDescription());
            statement.setInt(3, tour.getDurationDays());
            statement.setInt(4, tour.getDurationNights());
            statement.setDouble(5, tour.getBasePrice());
            statement.setString(6, tour.getFeaturedImage());
            statement.setString(7, tour.getHighlights());
            statement.setString(8, tour.getInclusions());
            statement.setString(9, tour.getExclusions());
            statement.setString(10, tour.getDifficultyLevel());
            statement.setInt(11, tour.getMinAge());
            statement.setInt(12, tour.getMaxGroupSize());
            statement.setInt(13, tour.getTourId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating tour: " + tour.getTourId(), e);
            return false;
        }
    }
    
    /**
     * Delete tour (soft delete)
     * @param tourId Tour ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteTour(int tourId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TOUR)) {
            
            statement.setInt(1, tourId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting tour: " + tourId, e);
            return false;
        }
    }
    
    /**
     * Get total count of active tours
     * @return Total count
     */
    public int getTourCount() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TOUR_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting tour count", e);
        }
        return 0;
    }
    
    /**
     * Map ResultSet to Tour object
     * @param resultSet ResultSet from database query
     * @return Tour object
     * @throws SQLException if mapping fails
     */
    private Tour mapResultSetToTour(ResultSet resultSet) throws SQLException {
        Tour tour = new Tour();
        tour.setTourId(resultSet.getInt("tour_id"));
        tour.setTourName(resultSet.getString("tour_name"));
        tour.setDescription(resultSet.getString("description"));
        tour.setDurationDays(resultSet.getInt("duration_days"));
        tour.setDurationNights(resultSet.getInt("duration_nights"));
        tour.setBasePrice(resultSet.getDouble("base_price"));
        tour.setFeaturedImage(resultSet.getString("featured_image"));
        tour.setStatus(resultSet.getString("status"));
        tour.setHighlights(resultSet.getString("highlights"));
        tour.setInclusions(resultSet.getString("inclusions"));
        tour.setExclusions(resultSet.getString("exclusions"));
        tour.setDifficultyLevel(resultSet.getString("difficulty_level"));
        tour.setMinAge(resultSet.getInt("min_age"));
        tour.setMaxGroupSize(resultSet.getInt("max_group_size"));
        tour.setCreatedAt(resultSet.getTimestamp("created_at"));
        tour.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return tour;
    }
}
