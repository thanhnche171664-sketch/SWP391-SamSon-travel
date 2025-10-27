/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ResetToken;
import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for ResetToken entity
 * Handles all database operations related to verification/reset tokens
 * 
 * @author SamSon Travel Team
 */
public class ResetTokenDAO {
    
    private static final Logger LOGGER = Logger.getLogger(ResetTokenDAO.class.getName());
    
    // Token expiration time (24 hours)
    private static final int TOKEN_EXPIRATION_HOURS = 24;
    
    // SQL queries
    private static final String CREATE_VERIFICATION_TOKEN = 
        "INSERT INTO reset_tokens (user_id, token, expires_at, created_at, updated_at, used) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String GET_TOKEN_BY_VALUE = 
        "SELECT id, user_id, token, expires_at, created_at, updated_at, used " +
        "FROM reset_tokens WHERE token = ?";
    
    private static final String GET_TOKEN_BY_USER_ID = 
        "SELECT id, user_id, token, expires_at, created_at, updated_at, used " +
        "FROM reset_tokens WHERE user_id = ? ORDER BY created_at DESC";
    
    private static final String MARK_TOKEN_AS_USED = 
        "UPDATE reset_tokens SET used = 1, updated_at = ? WHERE token = ?";
    
    private static final String DELETE_EXPIRED_TOKENS = 
        "DELETE FROM reset_tokens WHERE expires_at < ?";
    
    private static final String DELETE_TOKEN_BY_VALUE = 
        "DELETE FROM reset_tokens WHERE token = ?";
    
    private static final String DELETE_TOKENS_BY_USER_ID = 
        "DELETE FROM reset_tokens WHERE user_id = ?";
    
    private static final String GET_ACTIVE_TOKEN_BY_USER_ID = 
        "SELECT id, user_id, token, expires_at, created_at, updated_at, used " +
        "FROM reset_tokens WHERE user_id = ? AND used = 0 AND expires_at > ? ORDER BY created_at DESC";
    
    /**
     * Create a verification token for user
     * 
     * @param userId User ID
     * @param token Token value
     * @return Generated token ID if successful, -1 otherwise
     */
    public int createVerificationToken(int userId, String token) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return -1;
        }
        
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warning("Token parameter is null or empty");
            return -1;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_VERIFICATION_TOKEN, Statement.RETURN_GENERATED_KEYS)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp expiresAt = calculateExpirationTime();
            
            statement.setInt(1, userId);
            statement.setString(2, token.trim());
            statement.setTimestamp(3, expiresAt);
            statement.setTimestamp(4, now);
            statement.setTimestamp(5, now);
            statement.setBoolean(6, false); // Not used initially
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int tokenId = generatedKeys.getInt(1);
                        LOGGER.info("Verification token created successfully with ID: " + tokenId + " for user: " + userId);
                        return tokenId;
                    }
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating verification token for user: " + userId, e);
        }
        
        LOGGER.warning("Failed to create verification token for user: " + userId);
        return -1;
    }
    
    /**
     * Create token with ResetToken object
     * 
     * @param resetToken ResetToken object to create
     * @return true if successful, false otherwise
     */
    public boolean createToken(ResetToken resetToken) {
        if (resetToken == null || resetToken.getUserId() <= 0 || 
            resetToken.getToken() == null || resetToken.getToken().trim().isEmpty()) {
            LOGGER.warning("Invalid reset token data");
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_VERIFICATION_TOKEN)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            
            statement.setInt(1, resetToken.getUserId());
            statement.setString(2, resetToken.getToken().trim());
            statement.setTimestamp(3, resetToken.getExpiresAt());
            statement.setTimestamp(4, now);
            statement.setTimestamp(5, now);
            statement.setBoolean(6, resetToken.isUsed());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Reset token created successfully for user: " + resetToken.getUserId());
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating reset token for user: " + resetToken.getUserId(), e);
        }
        
        LOGGER.warning("Failed to create reset token for user: " + resetToken.getUserId());
        return false;
    }
    
    /**
     * Get token by token value
     * 
     * @param token Token value
     * @return ResetToken object if found, null otherwise
     */
    public ResetToken getTokenByValue(String token) {
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warning("Token parameter is null or empty");
            return null;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TOKEN_BY_VALUE)) {
            
            statement.setString(1, token.trim());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ResetToken resetToken = mapResultSetToResetToken(resultSet);
                    LOGGER.info("Token found by value: " + token);
                    return resetToken;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting token by value: " + token, e);
        }
        
        LOGGER.info("No token found with value: " + token);
        return null;
    }
    
    /**
     * Get active token for user
     * 
     * @param userId User ID
     * @return Active ResetToken object if found, null otherwise
     */
    public ResetToken getActiveTokenByUserId(int userId) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return null;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ACTIVE_TOKEN_BY_USER_ID)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            statement.setInt(1, userId);
            statement.setTimestamp(2, now);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ResetToken resetToken = mapResultSetToResetToken(resultSet);
                    LOGGER.info("Active token found for user: " + userId);
                    return resetToken;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting active token for user: " + userId, e);
        }
        
        LOGGER.info("No active token found for user: " + userId);
        return null;
    }
    
    /**
     * Mark token as used
     * 
     * @param token Token value
     * @return true if successful, false otherwise
     */
    public boolean markTokenAsUsed(String token) {
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warning("Token parameter is null or empty");
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(MARK_TOKEN_AS_USED)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(1, now);
            statement.setString(2, token.trim());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Token marked as used: " + token);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking token as used: " + token, e);
        }
        
        LOGGER.warning("Failed to mark token as used: " + token);
        return false;
    }
    
    /**
     * Delete expired tokens
     * 
     * @return Number of tokens deleted
     */
    public int deleteExpiredTokens() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_EXPIRED_TOKENS)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(1, now);
            
            int rowsAffected = statement.executeUpdate();
            
            LOGGER.info("Deleted " + rowsAffected + " expired tokens");
            return rowsAffected;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting expired tokens", e);
            return 0;
        }
    }
    
    /**
     * Delete token by value
     * 
     * @param token Token value
     * @return true if successful, false otherwise
     */
    public boolean deleteTokenByValue(String token) {
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warning("Token parameter is null or empty");
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TOKEN_BY_VALUE)) {
            
            statement.setString(1, token.trim());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Token deleted successfully: " + token);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting token: " + token, e);
        }
        
        LOGGER.warning("Failed to delete token: " + token);
        return false;
    }
    
    /**
     * Delete all tokens for a user
     * 
     * @param userId User ID
     * @return Number of tokens deleted
     */
    public int deleteTokensByUserId(int userId) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return 0;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TOKENS_BY_USER_ID)) {
            
            statement.setInt(1, userId);
            
            int rowsAffected = statement.executeUpdate();
            
            LOGGER.info("Deleted " + rowsAffected + " tokens for user: " + userId);
            return rowsAffected;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting tokens for user: " + userId, e);
            return 0;
        }
    }
    
    /**
     * Validate token (check if exists, not used, and not expired)
     * 
     * @param token Token value
     * @return true if token is valid, false otherwise
     */
    public boolean isValidToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            LOGGER.warning("Token parameter is null or empty");
            return false;
        }
        
        ResetToken resetToken = getTokenByValue(token);
        if (resetToken == null) {
            LOGGER.info("Token not found: " + token);
            return false;
        }
        
        if (resetToken.isUsed()) {
            LOGGER.info("Token already used: " + token);
            return false;
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (resetToken.getExpiresAt().before(now)) {
            LOGGER.info("Token expired: " + token);
            return false;
        }
        
        LOGGER.info("Token is valid: " + token);
        return true;
    }
    
    /**
     * Get token expiration time
     * 
     * @return Timestamp representing expiration time
     */
    private Timestamp calculateExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, TOKEN_EXPIRATION_HOURS);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    /**
     * Clean up old tokens (delete expired and used tokens older than 7 days)
     * 
     * @return Number of tokens cleaned up
     */
    public int cleanupOldTokens() {
        String cleanupQuery = 
            "DELETE FROM reset_tokens WHERE (expires_at < ?) OR (used = 1 AND created_at < ?)";
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(cleanupQuery)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -7); // 7 days ago
            Timestamp sevenDaysAgo = new Timestamp(calendar.getTimeInMillis());
            
            statement.setTimestamp(1, now);
            statement.setTimestamp(2, sevenDaysAgo);
            
            int rowsAffected = statement.executeUpdate();
            
            LOGGER.info("Cleaned up " + rowsAffected + " old tokens");
            return rowsAffected;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up old tokens", e);
            return 0;
        }
    }
    
    /**
     * Map ResultSet to ResetToken object
     * 
     * @param resultSet ResultSet from database query
     * @return ResetToken object
     * @throws SQLException if mapping fails
     */
    private ResetToken mapResultSetToResetToken(ResultSet resultSet) throws SQLException {
        ResetToken resetToken = new ResetToken();
        resetToken.setId(resultSet.getInt("id"));
        resetToken.setUserId(resultSet.getInt("user_id"));
        resetToken.setToken(resultSet.getString("token"));
        resetToken.setExpiresAt(resultSet.getTimestamp("expires_at"));
        resetToken.setCreatedAt(resultSet.getTimestamp("created_at"));
        resetToken.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        resetToken.setUsed(resultSet.getBoolean("used"));
        return resetToken;
    }
}
