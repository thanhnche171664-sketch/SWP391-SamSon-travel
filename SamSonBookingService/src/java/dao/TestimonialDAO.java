/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Testimonial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TestimonialDAO - Data Access Object for Testimonial operations
 * Manages customer reviews and testimonials for tours
 * 
 * @author SamSon Travel Team
 */
public class TestimonialDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TestimonialDAO.class.getName());
    
    // SQL Queries
    private static final String GET_ALL_TESTIMONIALS = 
        "SELECT * FROM Testimonials WHERE status = 'APPROVED' ORDER BY review_date DESC";
    
    private static final String GET_FEATURED_TESTIMONIALS = 
        "SELECT TOP 6 * FROM Testimonials WHERE status = 'APPROVED' AND rating >= 4 " +
        "ORDER BY review_date DESC";
    
    private static final String GET_TESTIMONIAL_BY_ID = 
        "SELECT * FROM Testimonials WHERE testimonial_id = ?";
    
    private static final String GET_TESTIMONIALS_BY_TOUR = 
        "SELECT * FROM Testimonials WHERE tour_id = ? AND status = 'APPROVED' " +
        "ORDER BY review_date DESC";
    
    private static final String GET_TESTIMONIALS_BY_RATING = 
        "SELECT * FROM Testimonials WHERE rating = ? AND status = 'APPROVED' " +
        "ORDER BY review_date DESC";
    
    private static final String GET_PENDING_TESTIMONIALS = 
        "SELECT * FROM Testimonials WHERE status = 'PENDING' ORDER BY review_date ASC";
    
    private static final String INSERT_TESTIMONIAL = 
        "INSERT INTO Testimonials (customer_name, customer_email, customer_avatar, " +
        "tour_id, rating, review_text, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_TESTIMONIAL = 
        "UPDATE Testimonials SET customer_name = ?, customer_email = ?, customer_avatar = ?, " +
        "tour_id = ?, rating = ?, review_text = ?, status = ? WHERE testimonial_id = ?";
    
    private static final String APPROVE_TESTIMONIAL = 
        "UPDATE Testimonials SET status = 'APPROVED' WHERE testimonial_id = ?";
    
    private static final String REJECT_TESTIMONIAL = 
        "UPDATE Testimonials SET status = 'REJECTED' WHERE testimonial_id = ?";
    
    private static final String DELETE_TESTIMONIAL = 
        "DELETE FROM Testimonials WHERE testimonial_id = ?";
    
    private static final String GET_TESTIMONIAL_COUNT = 
        "SELECT COUNT(*) FROM Testimonials WHERE status = 'APPROVED'";
    
    private static final String GET_AVERAGE_RATING_BY_TOUR = 
        "SELECT AVG(CAST(rating AS FLOAT)) FROM Testimonials WHERE tour_id = ? AND status = 'APPROVED'";
    
    /**
     * Get all approved testimonials
     * @return List of approved testimonials
     */
    public List<Testimonial> getAllTestimonials() {
        List<Testimonial> testimonials = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_TESTIMONIALS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                testimonials.add(mapResultSetToTestimonial(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all testimonials", e);
        }
        return testimonials;
    }
    
    /**
     * Get featured testimonials (top 6 with rating >= 4)
     * @return List of featured testimonials
     */
    public List<Testimonial> getFeaturedTestimonials() {
        List<Testimonial> testimonials = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_FEATURED_TESTIMONIALS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                testimonials.add(mapResultSetToTestimonial(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting featured testimonials", e);
        }
        return testimonials;
    }
    
    /**
     * Get testimonial by ID
     * @param testimonialId Testimonial ID
     * @return Testimonial object or null if not found
     */
    public Testimonial getTestimonialById(int testimonialId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TESTIMONIAL_BY_ID)) {
            
            statement.setInt(1, testimonialId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToTestimonial(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting testimonial by ID: " + testimonialId, e);
        }
        return null;
    }
    
    /**
     * Get testimonials by tour
     * @param tourId Tour ID
     * @return List of testimonials for the tour
     */
    public List<Testimonial> getTestimonialsByTour(int tourId) {
        List<Testimonial> testimonials = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TESTIMONIALS_BY_TOUR)) {
            
            statement.setInt(1, tourId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    testimonials.add(mapResultSetToTestimonial(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting testimonials for tour: " + tourId, e);
        }
        return testimonials;
    }
    
    /**
     * Get testimonials by rating
     * @param rating Rating value (1-5)
     * @return List of testimonials with specified rating
     */
    public List<Testimonial> getTestimonialsByRating(int rating) {
        List<Testimonial> testimonials = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TESTIMONIALS_BY_RATING)) {
            
            statement.setInt(1, rating);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    testimonials.add(mapResultSetToTestimonial(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting testimonials by rating: " + rating, e);
        }
        return testimonials;
    }
    
    /**
     * Get pending testimonials for admin approval
     * @return List of pending testimonials
     */
    public List<Testimonial> getPendingTestimonials() {
        List<Testimonial> testimonials = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PENDING_TESTIMONIALS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                testimonials.add(mapResultSetToTestimonial(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting pending testimonials", e);
        }
        return testimonials;
    }
    
    /**
     * Insert new testimonial
     * @param testimonial Testimonial object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertTestimonial(Testimonial testimonial) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_TESTIMONIAL)) {
            
            statement.setString(1, testimonial.getCustomerName());
            statement.setString(2, testimonial.getCustomerEmail());
            statement.setString(3, testimonial.getCustomerAvatar());
            statement.setObject(4, testimonial.getTourId());
            statement.setInt(5, testimonial.getRating());
            statement.setString(6, testimonial.getReviewText());
            statement.setString(7, testimonial.getStatus());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting testimonial from: " + testimonial.getCustomerName(), e);
            return false;
        }
    }
    
    /**
     * Update existing testimonial
     * @param testimonial Testimonial object to update
     * @return true if successful, false otherwise
     */
    public boolean updateTestimonial(Testimonial testimonial) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TESTIMONIAL)) {
            
            statement.setString(1, testimonial.getCustomerName());
            statement.setString(2, testimonial.getCustomerEmail());
            statement.setString(3, testimonial.getCustomerAvatar());
            statement.setObject(4, testimonial.getTourId());
            statement.setInt(5, testimonial.getRating());
            statement.setString(6, testimonial.getReviewText());
            statement.setString(7, testimonial.getStatus());
            statement.setInt(8, testimonial.getTestimonialId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating testimonial: " + testimonial.getTestimonialId(), e);
            return false;
        }
    }
    
    /**
     * Approve testimonial
     * @param testimonialId Testimonial ID to approve
     * @return true if successful, false otherwise
     */
    public boolean approveTestimonial(int testimonialId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(APPROVE_TESTIMONIAL)) {
            
            statement.setInt(1, testimonialId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error approving testimonial: " + testimonialId, e);
            return false;
        }
    }
    
    /**
     * Reject testimonial
     * @param testimonialId Testimonial ID to reject
     * @return true if successful, false otherwise
     */
    public boolean rejectTestimonial(int testimonialId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(REJECT_TESTIMONIAL)) {
            
            statement.setInt(1, testimonialId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error rejecting testimonial: " + testimonialId, e);
            return false;
        }
    }
    
    /**
     * Delete testimonial
     * @param testimonialId Testimonial ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteTestimonial(int testimonialId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TESTIMONIAL)) {
            
            statement.setInt(1, testimonialId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting testimonial: " + testimonialId, e);
            return false;
        }
    }
    
    /**
     * Get total count of approved testimonials
     * @return Total count
     */
    public int getTestimonialCount() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TESTIMONIAL_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting testimonial count", e);
        }
        return 0;
    }
    
    /**
     * Get average rating for a tour
     * @param tourId Tour ID
     * @return Average rating or 0 if no ratings
     */
    public double getAverageRatingByTour(int tourId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_AVERAGE_RATING_BY_TOUR)) {
            
            statement.setInt(1, tourId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double avgRating = resultSet.getDouble(1);
                    return resultSet.wasNull() ? 0.0 : avgRating;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting average rating for tour: " + tourId, e);
        }
        return 0.0;
    }
    
    /**
     * Get rating distribution for a tour
     * @param tourId Tour ID
     * @return Array with count for each rating (1-5)
     */
    public int[] getRatingDistribution(int tourId) {
        int[] distribution = new int[5];
        for (int i = 1; i <= 5; i++) {
            List<Testimonial> testimonials = getTestimonialsByRating(i);
            distribution[i-1] = testimonials.size();
        }
        return distribution;
    }
    
    /**
     * Map ResultSet to Testimonial object
     * @param resultSet ResultSet from database query
     * @return Testimonial object
     * @throws SQLException if mapping fails
     */
    private Testimonial mapResultSetToTestimonial(ResultSet resultSet) throws SQLException {
        Testimonial testimonial = new Testimonial();
        testimonial.setTestimonialId(resultSet.getInt("testimonial_id"));
        testimonial.setCustomerName(resultSet.getString("customer_name"));
        testimonial.setCustomerEmail(resultSet.getString("customer_email"));
        testimonial.setCustomerAvatar(resultSet.getString("customer_avatar"));
        
        // Handle nullable tour_id
        int tourId = resultSet.getInt("tour_id");
        testimonial.setTourId(resultSet.wasNull() ? null : tourId);
        
        testimonial.setRating(resultSet.getInt("rating"));
        testimonial.setReviewText(resultSet.getString("review_text"));
        testimonial.setReviewDate(resultSet.getTimestamp("review_date"));
        testimonial.setStatus(resultSet.getString("status"));
        return testimonial;
    }
}
