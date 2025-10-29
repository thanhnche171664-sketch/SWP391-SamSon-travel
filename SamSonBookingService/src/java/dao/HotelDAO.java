/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Hotel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HotelDAO - Data Access Object for Hotel operations
 * Provides comprehensive CRUD operations and specialized queries for hotels
 * 
 * @author SamSon Travel Team
 */
public class HotelDAO {
    
    private static final Logger LOGGER = Logger.getLogger(HotelDAO.class.getName());
    
    // SQL Queries
    private static final String GET_ALL_HOTELS = 
        "SELECT * FROM Hotels ORDER BY created_at DESC";
    
    private static final String GET_FEATURED_HOTELS = 
        "SELECT TOP 3 * FROM Hotels WHERE featured = 1 ORDER BY rating DESC";
    
    private static final String GET_HOTEL_BY_ID = 
        "SELECT * FROM Hotels WHERE id = ?";
    
    private static final String GET_HOTELS_BY_RATING = 
        "SELECT * FROM Hotels WHERE rating >= ? ORDER BY rating DESC";
    
    private static final String SEARCH_HOTELS = 
        "SELECT * FROM Hotels WHERE name LIKE ? OR address LIKE ? OR description LIKE ? " +
        "ORDER BY rating DESC";
    
    private static final String INSERT_HOTEL = 
        "INSERT INTO Hotels (name, address, description, manager_id, rating, featured, " +
        "amenities, check_in_time, check_out_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_HOTEL = 
        "UPDATE Hotels SET name = ?, address = ?, description = ?, manager_id = ?, " +
        "rating = ?, featured = ?, amenities = ?, check_in_time = ?, check_out_time = ?, " +
        "updated_at = GETDATE() WHERE id = ?";
    
    private static final String DELETE_HOTEL = 
        "DELETE FROM Hotels WHERE id = ?";
    
    private static final String GET_HOTEL_COUNT = 
        "SELECT COUNT(*) FROM Hotels";
    
    /**
     * Get all hotels
     * @return List of all hotels
     */
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_HOTELS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                hotels.add(mapResultSetToHotel(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all hotels", e);
        }
        return hotels;
    }
    
    /**
     * Get featured hotels (top 3)
     * @return List of featured hotels
     */
    public List<Hotel> getFeaturedHotels() {
        List<Hotel> hotels = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_FEATURED_HOTELS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                hotels.add(mapResultSetToHotel(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting featured hotels", e);
        }
        return hotels;
    }
    
    /**
     * Get hotel by ID
     * @param hotelId Hotel ID
     * @return Hotel object or null if not found
     */
    public Hotel getHotelById(int hotelId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_HOTEL_BY_ID)) {
            
            statement.setInt(1, hotelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToHotel(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting hotel by ID: " + hotelId, e);
        }
        return null;
    }
    
    /**
     * Get hotels by minimum rating
     * @param minRating Minimum rating
     * @return List of hotels with specified rating or higher
     */
    public List<Hotel> getHotelsByRating(double minRating) {
        List<Hotel> hotels = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_HOTELS_BY_RATING)) {
            
            statement.setDouble(1, minRating);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    hotels.add(mapResultSetToHotel(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting hotels by rating: " + minRating, e);
        }
        return hotels;
    }
    
    /**
     * Search hotels by keyword
     * @param keyword Search keyword
     * @return List of matching hotels
     */
    public List<Hotel> searchHotels(String keyword) {
        List<Hotel> hotels = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_HOTELS)) {
            
            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    hotels.add(mapResultSetToHotel(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching hotels with keyword: " + keyword, e);
        }
        return hotels;
    }
    
    /**
     * Insert new hotel
     * @param hotel Hotel object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertHotel(Hotel hotel) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_HOTEL)) {
            
            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getAddress());
            statement.setString(3, hotel.getDescription());
            statement.setInt(4, hotel.getManagerId());
            statement.setDouble(5, hotel.getRating());
            statement.setBoolean(6, hotel.isFeatured());
            statement.setString(7, hotel.getAmenities());
            statement.setTime(8, hotel.getCheckInTime());
            statement.setTime(9, hotel.getCheckOutTime());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting hotel: " + hotel.getName(), e);
            return false;
        }
    }
    
    /**
     * Update existing hotel
     * @param hotel Hotel object to update
     * @return true if successful, false otherwise
     */
    public boolean updateHotel(Hotel hotel) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_HOTEL)) {
            
            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getAddress());
            statement.setString(3, hotel.getDescription());
            statement.setInt(4, hotel.getManagerId());
            statement.setDouble(5, hotel.getRating());
            statement.setBoolean(6, hotel.isFeatured());
            statement.setString(7, hotel.getAmenities());
            statement.setTime(8, hotel.getCheckInTime());
            statement.setTime(9, hotel.getCheckOutTime());
            statement.setInt(10, hotel.getId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating hotel: " + hotel.getId(), e);
            return false;
        }
    }
    
    /**
     * Delete hotel
     * @param hotelId Hotel ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteHotel(int hotelId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_HOTEL)) {
            
            statement.setInt(1, hotelId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting hotel: " + hotelId, e);
            return false;
        }
    }
    
    /**
     * Get total count of hotels
     * @return Total count
     */
    public int getHotelCount() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_HOTEL_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting hotel count", e);
        }
        return 0;
    }
    
    /**
     * Map ResultSet to Hotel object
     * @param resultSet ResultSet from database query
     * @return Hotel object
     * @throws SQLException if mapping fails
     */
    private Hotel mapResultSetToHotel(ResultSet resultSet) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(resultSet.getInt("id"));
        hotel.setName(resultSet.getString("name"));
        hotel.setAddress(resultSet.getString("address"));
        hotel.setDescription(resultSet.getString("description"));
        hotel.setManagerId(resultSet.getInt("manager_id"));
        hotel.setRating(resultSet.getDouble("rating"));
        hotel.setFeatured(resultSet.getBoolean("featured"));
        hotel.setAmenities(resultSet.getString("amenities"));
        hotel.setCheckInTime(resultSet.getTime("check_in_time"));
        hotel.setCheckOutTime(resultSet.getTime("check_out_time"));
        hotel.setCreatedAt(resultSet.getDate("created_at"));
        hotel.setUpdatedAt(resultSet.getDate("updated_at"));
        return hotel;
    }
}
