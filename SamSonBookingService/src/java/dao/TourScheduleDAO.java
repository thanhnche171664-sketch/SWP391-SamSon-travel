/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.TourSchedule;
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
 * TourScheduleDAO - Data Access Object for TourSchedule operations
 * Manages tour schedules, availability, and booking slots
 * 
 * @author SamSon Travel Team
 */
public class TourScheduleDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TourScheduleDAO.class.getName());
    
    // SQL Queries
    private static final String GET_SCHEDULES_BY_TOUR = 
        "SELECT * FROM Tour_Schedules WHERE tour_id = ? AND status = 'ACTIVE' " +
        "ORDER BY departure_date ASC";
    
    private static final String GET_AVAILABLE_SCHEDULES = 
        "SELECT * FROM Tour_Schedules WHERE tour_id = ? AND status = 'ACTIVE' " +
        "AND available_slots > booked_slots AND departure_date > GETDATE() " +
        "ORDER BY departure_date ASC";
    
    private static final String GET_SCHEDULE_BY_ID = 
        "SELECT * FROM Tour_Schedules WHERE schedule_id = ? AND status = 'ACTIVE'";
    
    private static final String GET_UPCOMING_SCHEDULES = 
        "SELECT * FROM Tour_Schedules WHERE status = 'ACTIVE' " +
        "AND departure_date > GETDATE() ORDER BY departure_date ASC";
    
    private static final String INSERT_SCHEDULE = 
        "INSERT INTO Tour_Schedules (tour_id, departure_date, return_date, " +
        "available_slots, booked_slots, price_adjustment, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_SCHEDULE = 
        "UPDATE Tour_Schedules SET departure_date = ?, return_date = ?, " +
        "available_slots = ?, booked_slots = ?, price_adjustment = ?, " +
        "status = ?, updated_at = GETDATE() WHERE schedule_id = ?";
    
    private static final String UPDATE_BOOKED_SLOTS = 
        "UPDATE Tour_Schedules SET booked_slots = booked_slots + ?, " +
        "updated_at = GETDATE() WHERE schedule_id = ? AND " +
        "(booked_slots + ?) <= available_slots";
    
    private static final String CANCEL_BOOKING_SLOTS = 
        "UPDATE Tour_Schedules SET booked_slots = booked_slots - ?, " +
        "updated_at = GETDATE() WHERE schedule_id = ? AND booked_slots >= ?";
    
    private static final String DELETE_SCHEDULE = 
        "UPDATE Tour_Schedules SET status = 'CANCELLED', updated_at = GETDATE() " +
        "WHERE schedule_id = ?";
    
    private static final String GET_SCHEDULE_COUNT = 
        "SELECT COUNT(*) FROM Tour_Schedules WHERE status = 'ACTIVE'";
    
    /**
     * Get all schedules for a specific tour
     * @param tourId Tour ID
     * @return List of schedules for the tour
     */
    public List<TourSchedule> getSchedulesByTour(int tourId) {
        List<TourSchedule> schedules = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_SCHEDULES_BY_TOUR)) {
            
            statement.setInt(1, tourId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    schedules.add(mapResultSetToSchedule(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting schedules for tour: " + tourId, e);
        }
        return schedules;
    }
    
    /**
     * Get available schedules for a tour (with available slots)
     * @param tourId Tour ID
     * @return List of available schedules
     */
    public List<TourSchedule> getAvailableSchedules(int tourId) {
        List<TourSchedule> schedules = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_AVAILABLE_SCHEDULES)) {
            
            statement.setInt(1, tourId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    schedules.add(mapResultSetToSchedule(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting available schedules for tour: " + tourId, e);
        }
        return schedules;
    }
    
    /**
     * Get schedule by ID
     * @param scheduleId Schedule ID
     * @return TourSchedule object or null if not found
     */
    public TourSchedule getScheduleById(int scheduleId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_SCHEDULE_BY_ID)) {
            
            statement.setInt(1, scheduleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSchedule(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting schedule by ID: " + scheduleId, e);
        }
        return null;
    }
    
    /**
     * Get all upcoming schedules
     * @return List of upcoming schedules
     */
    public List<TourSchedule> getUpcomingSchedules() {
        List<TourSchedule> schedules = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_UPCOMING_SCHEDULES);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                schedules.add(mapResultSetToSchedule(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting upcoming schedules", e);
        }
        return schedules;
    }
    
    /**
     * Insert new schedule
     * @param schedule TourSchedule object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertSchedule(TourSchedule schedule) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SCHEDULE)) {
            
            statement.setInt(1, schedule.getTourId());
            statement.setTimestamp(2, schedule.getDepartureDate());
            statement.setTimestamp(3, schedule.getReturnDate());
            statement.setInt(4, schedule.getAvailableSlots());
            statement.setInt(5, schedule.getBookedSlots());
            statement.setDouble(6, schedule.getPriceAdjustment());
            statement.setString(7, schedule.getStatus());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting schedule for tour: " + schedule.getTourId(), e);
            return false;
        }
    }
    
    /**
     * Update existing schedule
     * @param schedule TourSchedule object to update
     * @return true if successful, false otherwise
     */
    public boolean updateSchedule(TourSchedule schedule) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SCHEDULE)) {
            
            statement.setTimestamp(1, schedule.getDepartureDate());
            statement.setTimestamp(2, schedule.getReturnDate());
            statement.setInt(3, schedule.getAvailableSlots());
            statement.setInt(4, schedule.getBookedSlots());
            statement.setDouble(5, schedule.getPriceAdjustment());
            statement.setString(6, schedule.getStatus());
            statement.setInt(7, schedule.getScheduleId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating schedule: " + schedule.getScheduleId(), e);
            return false;
        }
    }
    
    /**
     * Book slots for a schedule
     * @param scheduleId Schedule ID
     * @param slotsToBook Number of slots to book
     * @return true if successful, false otherwise
     */
    public boolean bookSlots(int scheduleId, int slotsToBook) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BOOKED_SLOTS)) {
            
            statement.setInt(1, slotsToBook);
            statement.setInt(2, scheduleId);
            statement.setInt(3, slotsToBook);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error booking slots for schedule: " + scheduleId, e);
            return false;
        }
    }
    
    /**
     * Cancel booking slots for a schedule
     * @param scheduleId Schedule ID
     * @param slotsToCancel Number of slots to cancel
     * @return true if successful, false otherwise
     */
    public boolean cancelBookingSlots(int scheduleId, int slotsToCancel) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CANCEL_BOOKING_SLOTS)) {
            
            statement.setInt(1, slotsToCancel);
            statement.setInt(2, scheduleId);
            statement.setInt(3, slotsToCancel);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error canceling booking slots for schedule: " + scheduleId, e);
            return false;
        }
    }
    
    /**
     * Delete schedule (soft delete)
     * @param scheduleId Schedule ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteSchedule(int scheduleId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SCHEDULE)) {
            
            statement.setInt(1, scheduleId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting schedule: " + scheduleId, e);
            return false;
        }
    }
    
    /**
     * Get total count of active schedules
     * @return Total count
     */
    public int getScheduleCount() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_SCHEDULE_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting schedule count", e);
        }
        return 0;
    }
    
    /**
     * Check if schedule has available slots
     * @param scheduleId Schedule ID
     * @param requiredSlots Required number of slots
     * @return true if available, false otherwise
     */
    public boolean hasAvailableSlots(int scheduleId, int requiredSlots) {
        TourSchedule schedule = getScheduleById(scheduleId);
        if (schedule == null) return false;
        
        return schedule.getRemainingSlots() >= requiredSlots;
    }
    
    /**
     * Map ResultSet to TourSchedule object
     * @param resultSet ResultSet from database query
     * @return TourSchedule object
     * @throws SQLException if mapping fails
     */
    private TourSchedule mapResultSetToSchedule(ResultSet resultSet) throws SQLException {
        TourSchedule schedule = new TourSchedule();
        schedule.setScheduleId(resultSet.getInt("schedule_id"));
        schedule.setTourId(resultSet.getInt("tour_id"));
        schedule.setDepartureDate(resultSet.getTimestamp("departure_date"));
        schedule.setReturnDate(resultSet.getTimestamp("return_date"));
        schedule.setAvailableSlots(resultSet.getInt("available_slots"));
        schedule.setBookedSlots(resultSet.getInt("booked_slots"));
        schedule.setPriceAdjustment(resultSet.getDouble("price_adjustment"));
        schedule.setStatus(resultSet.getString("status"));
        schedule.setCreatedAt(resultSet.getTimestamp("created_at"));
        schedule.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return schedule;
    }
}
