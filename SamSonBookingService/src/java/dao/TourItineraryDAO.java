/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.TourItinerary;
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
 * TourItineraryDAO - Data Access Object for TourItinerary operations
 * Manages detailed day-by-day tour itineraries
 * 
 * @author SamSon Travel Team
 */
public class TourItineraryDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TourItineraryDAO.class.getName());
    
    // SQL Queries
    private static final String GET_ITINERARIES_BY_TOUR = 
        "SELECT * FROM Tour_Itineraries WHERE tour_id = ? ORDER BY day_number ASC";
    
    private static final String GET_ITINERARY_BY_ID = 
        "SELECT * FROM Tour_Itineraries WHERE itinerary_id = ?";
    
    private static final String GET_ITINERARY_BY_TOUR_AND_DAY = 
        "SELECT * FROM Tour_Itineraries WHERE tour_id = ? AND day_number = ?";
    
    private static final String INSERT_ITINERARY = 
        "INSERT INTO Tour_Itineraries (tour_id, day_number, title, description, " +
        "activities, accommodation, meals_included, transport_info) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_ITINERARY = 
        "UPDATE Tour_Itineraries SET day_number = ?, title = ?, description = ?, " +
        "activities = ?, accommodation = ?, meals_included = ?, transport_info = ? " +
        "WHERE itinerary_id = ?";
    
    private static final String DELETE_ITINERARY = 
        "DELETE FROM Tour_Itineraries WHERE itinerary_id = ?";
    
    private static final String DELETE_ITINERARIES_BY_TOUR = 
        "DELETE FROM Tour_Itineraries WHERE tour_id = ?";
    
    private static final String GET_ITINERARY_COUNT_BY_TOUR = 
        "SELECT COUNT(*) FROM Tour_Itineraries WHERE tour_id = ?";
    
    /**
     * Get all itineraries for a specific tour
     * @param tourId Tour ID
     * @return List of itineraries for the tour ordered by day number
     */
    public List<TourItinerary> getItinerariesByTour(int tourId) {
        List<TourItinerary> itineraries = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ITINERARIES_BY_TOUR)) {
            
            statement.setInt(1, tourId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    itineraries.add(mapResultSetToItinerary(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting itineraries for tour: " + tourId, e);
        }
        return itineraries;
    }
    
    /**
     * Get itinerary by ID
     * @param itineraryId Itinerary ID
     * @return TourItinerary object or null if not found
     */
    public TourItinerary getItineraryById(int itineraryId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ITINERARY_BY_ID)) {
            
            statement.setInt(1, itineraryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToItinerary(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting itinerary by ID: " + itineraryId, e);
        }
        return null;
    }
    
    /**
     * Get itinerary by tour ID and day number
     * @param tourId Tour ID
     * @param dayNumber Day number
     * @return TourItinerary object or null if not found
     */
    public TourItinerary getItineraryByTourAndDay(int tourId, int dayNumber) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ITINERARY_BY_TOUR_AND_DAY)) {
            
            statement.setInt(1, tourId);
            statement.setInt(2, dayNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToItinerary(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting itinerary for tour: " + tourId + ", day: " + dayNumber, e);
        }
        return null;
    }
    
    /**
     * Insert new itinerary
     * @param itinerary TourItinerary object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertItinerary(TourItinerary itinerary) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ITINERARY)) {
            
            statement.setInt(1, itinerary.getTourId());
            statement.setInt(2, itinerary.getDayNumber());
            statement.setString(3, itinerary.getTitle());
            statement.setString(4, itinerary.getDescription());
            statement.setString(5, itinerary.getActivities());
            statement.setString(6, itinerary.getAccommodation());
            statement.setString(7, itinerary.getMealsIncluded());
            statement.setString(8, itinerary.getTransportInfo());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting itinerary for tour: " + itinerary.getTourId(), e);
            return false;
        }
    }
    
    /**
     * Update existing itinerary
     * @param itinerary TourItinerary object to update
     * @return true if successful, false otherwise
     */
    public boolean updateItinerary(TourItinerary itinerary) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ITINERARY)) {
            
            statement.setInt(1, itinerary.getDayNumber());
            statement.setString(2, itinerary.getTitle());
            statement.setString(3, itinerary.getDescription());
            statement.setString(4, itinerary.getActivities());
            statement.setString(5, itinerary.getAccommodation());
            statement.setString(6, itinerary.getMealsIncluded());
            statement.setString(7, itinerary.getTransportInfo());
            statement.setInt(8, itinerary.getItineraryId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating itinerary: " + itinerary.getItineraryId(), e);
            return false;
        }
    }
    
    /**
     * Delete itinerary
     * @param itineraryId Itinerary ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteItinerary(int itineraryId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ITINERARY)) {
            
            statement.setInt(1, itineraryId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting itinerary: " + itineraryId, e);
            return false;
        }
    }
    
    /**
     * Delete all itineraries for a tour
     * @param tourId Tour ID
     * @return true if successful, false otherwise
     */
    public boolean deleteItinerariesByTour(int tourId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ITINERARIES_BY_TOUR)) {
            
            statement.setInt(1, tourId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected >= 0; // Allow 0 rows affected
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting itineraries for tour: " + tourId, e);
            return false;
        }
    }
    
    /**
     * Get count of itineraries for a tour
     * @param tourId Tour ID
     * @return Number of itineraries
     */
    public int getItineraryCountByTour(int tourId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ITINERARY_COUNT_BY_TOUR)) {
            
            statement.setInt(1, tourId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting itinerary count for tour: " + tourId, e);
        }
        return 0;
    }
    
    /**
     * Batch insert multiple itineraries for a tour
     * @param itineraries List of itineraries to insert
     * @return true if all successful, false otherwise
     */
    public boolean batchInsertItineraries(List<TourItinerary> itineraries) {
        if (itineraries == null || itineraries.isEmpty()) {
            return true;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ITINERARY)) {
            
            connection.setAutoCommit(false);
            
            for (TourItinerary itinerary : itineraries) {
                statement.setInt(1, itinerary.getTourId());
                statement.setInt(2, itinerary.getDayNumber());
                statement.setString(3, itinerary.getTitle());
                statement.setString(4, itinerary.getDescription());
                statement.setString(5, itinerary.getActivities());
                statement.setString(6, itinerary.getAccommodation());
                statement.setString(7, itinerary.getMealsIncluded());
                statement.setString(8, itinerary.getTransportInfo());
                
                statement.addBatch();
            }
            
            int[] results = statement.executeBatch();
            connection.commit();
            
            // Check if all inserts were successful
            for (int result : results) {
                if (result != 1) {
                    return false;
                }
            }
            
            return true;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error batch inserting itineraries", e);
            return false;
        }
    }
    
    /**
     * Map ResultSet to TourItinerary object
     * @param resultSet ResultSet from database query
     * @return TourItinerary object
     * @throws SQLException if mapping fails
     */
    private TourItinerary mapResultSetToItinerary(ResultSet resultSet) throws SQLException {
        TourItinerary itinerary = new TourItinerary();
        itinerary.setItineraryId(resultSet.getInt("itinerary_id"));
        itinerary.setTourId(resultSet.getInt("tour_id"));
        itinerary.setDayNumber(resultSet.getInt("day_number"));
        itinerary.setTitle(resultSet.getString("title"));
        itinerary.setDescription(resultSet.getString("description"));
        itinerary.setActivities(resultSet.getString("activities"));
        itinerary.setAccommodation(resultSet.getString("accommodation"));
        itinerary.setMealsIncluded(resultSet.getString("meals_included"));
        itinerary.setTransportInfo(resultSet.getString("transport_info"));
        itinerary.setCreatedAt(resultSet.getTimestamp("created_at"));
        return itinerary;
    }
}
