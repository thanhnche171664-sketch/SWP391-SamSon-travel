/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.User;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for User entity
 * Handles all database operations related to users
 * 
 * @author SamSon Travel Team
 */
public class UserDAO {
    
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    
    // SQL queries
    private static final String GET_USER_BY_EMAIL = 
        "SELECT id, name, password, email, phone, gender, address, avatar_url, role_id, status, created_at, updated_at " +
        "FROM users WHERE email = ?";
    
    private static final String GET_USER_BY_ID = 
        "SELECT id, name, password, email, phone, gender, address, avatar_url, role_id, status, created_at, updated_at " +
        "FROM users WHERE id = ?";
    
    private static final String CREATE_USER = 
        "INSERT INTO users (name, password, email, phone, gender, address, role_id, status, created_at, updated_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_USER = 
        "UPDATE users SET name = ?, password = ?, email = ?, phone = ?, gender = ?, address = ?, " +
        "role_id = ?, status = ?, updated_at = ? WHERE id = ?";
    
    private static final String CHECK_EMAIL_EXISTS = 
        "SELECT COUNT(*) FROM users WHERE email = ?";
    
    private static final String ACTIVATE_USER = 
        "UPDATE users SET status = 'active', updated_at = ? WHERE id = ?";
    
    private static final String DEACTIVATE_USER = 
        "UPDATE users SET status = 'inactive', updated_at = ? WHERE id = ?";
    
    private static final String UPDATE_PASSWORD = 
        "UPDATE users SET password = ?, updated_at = ? WHERE id = ?";
    
    private static final String GET_USERS_BY_ROLE = 
        "SELECT id, name, password, email, phone, gender, address, avatar_url, role_id, status, created_at, updated_at " +
        "FROM users WHERE role_id = ? ORDER BY created_at DESC";
    
    private static final String UPDATE_USER_PROFILE = 
        "UPDATE users SET name = ?, phone = ?, gender = ?, address = ?, updated_at = ? WHERE id = ?";
    
    private static final String UPDATE_USER_EMAIL = 
        "UPDATE users SET email = ?, updated_at = ? WHERE id = ?";
    
    private static final String CHECK_EMAIL_EXISTS_EXCLUDING_USER = 
        "SELECT COUNT(*) FROM users WHERE email = ? AND id != ?";
    
    private static final String UPDATE_AVATAR = 
        "UPDATE users SET avatar_url = ?, updated_at = ? WHERE id = ?";
    
    /**
     * Get user by email address
     * 
     * @param email User's email address
     * @return User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            LOGGER.warning("Email parameter is null or empty");
            return null;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
            
            statement.setString(1, email.trim().toLowerCase());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    LOGGER.info("User found by email: " + email);
                    return user;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email: " + email, e);
        }
        
        LOGGER.info("No user found with email: " + email);
        return null;
    }
    
    /**
     * Get user by ID
     * 
     * @param id User's ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int id) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    LOGGER.info("User found by ID: " + id);
                    return user;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID: " + id, e);
        }
        
        LOGGER.info("No user found with ID: " + id);
        return null;
    }
    
    /**
     * Create a new user
     * 
     * @param user User object to create
     * @return Generated user ID if successful, -1 otherwise
     */
    public int createUser(User user) {
        if (user == null) {
            LOGGER.warning("User parameter is null");
            return -1;
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            LOGGER.warning("User email is null or empty");
            return -1;
        }
        
        // Check if email already exists
        if (checkEmailExists(user.getEmail())) {
            LOGGER.warning("Email already exists: " + user.getEmail());
            return -1;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail().trim().toLowerCase());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getGender());
            statement.setString(6, user.getAddress());
            statement.setInt(7, user.getRoleId());
            statement.setString(8, user.getStatus() != null ? user.getStatus() : "pending");
            statement.setTimestamp(9, now);
            statement.setTimestamp(10, now);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        LOGGER.info("User created successfully with ID: " + userId);
                        return userId;
                    }
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
        }
        
        LOGGER.warning("Failed to create user");
        return -1;
    }
    
    /**
     * Update an existing user
     * 
     * @param user User object to update
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User user) {
        if (user == null || user.getId() <= 0) {
            LOGGER.warning("User parameter is null or invalid ID");
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail().trim().toLowerCase());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getGender());
            statement.setString(6, user.getAddress());
            statement.setInt(7, user.getRoleId());
            statement.setString(8, user.getStatus());
            statement.setTimestamp(9, now);
            statement.setInt(10, user.getId());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("User updated successfully with ID: " + user.getId());
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user with ID: " + user.getId(), e);
        }
        
        LOGGER.warning("Failed to update user with ID: " + user.getId());
        return false;
    }
    
    /**
     * Check if email already exists
     * 
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean checkEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            LOGGER.warning("Email parameter is null or empty");
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_EMAIL_EXISTS)) {
            
            statement.setString(1, email.trim().toLowerCase());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    boolean exists = count > 0;
                    LOGGER.info("Email exists check for " + email + ": " + exists);
                    return exists;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking email existence: " + email, e);
        }
        
        return false;
    }
    
    /**
     * Activate user account
     * 
     * @param userId User ID to activate
     * @return true if successful, false otherwise
     */
    public boolean activateUser(int userId) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(ACTIVATE_USER)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(1, now);
            statement.setInt(2, userId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("User activated successfully with ID: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error activating user with ID: " + userId, e);
        }
        
        LOGGER.warning("Failed to activate user with ID: " + userId);
        return false;
    }
    
    /**
     * Deactivate user account
     * 
     * @param userId User ID to deactivate
     * @return true if successful, false otherwise
     */
    public boolean deactivateUser(int userId) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DEACTIVATE_USER)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(1, now);
            statement.setInt(2, userId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("User deactivated successfully with ID: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deactivating user with ID: " + userId, e);
        }
        
        LOGGER.warning("Failed to deactivate user with ID: " + userId);
        return false;
    }
    
    /**
     * Update user password
     * 
     * @param userId User ID
     * @param newPassword New hashed password
     * @return true if successful, false otherwise
     */
    public boolean updatePassword(int userId, String newPassword) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return false;
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            LOGGER.warning("New password is null or empty");
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PASSWORD)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            statement.setString(1, newPassword);
            statement.setTimestamp(2, now);
            statement.setInt(3, userId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Password updated successfully for user ID: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password for user ID: " + userId, e);
        }
        
        LOGGER.warning("Failed to update password for user ID: " + userId);
        return false;
    }
    
    /**
     * Map ResultSet to User object
     * 
     * @param resultSet ResultSet from database query
     * @return User object
     * @throws SQLException if mapping fails
     */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setGender(resultSet.getString("gender"));
        user.setAddress(resultSet.getString("address"));
        user.setAvatarUrl(resultSet.getString("avatar_url"));
        user.setRoleId(resultSet.getInt("role_id"));
        user.setStatus(resultSet.getString("status"));
        user.setCreatedAt(resultSet.getTimestamp("created_at"));
        user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return user;
    }
    
    /**
     * Update user profile (name, phone, gender, address) without password
     * 
     * @param userId User ID to update
     * @param name New name
     * @param phone New phone
     * @param gender New gender
     * @param address New address
     * @return true if successful, false otherwise
     */
    public boolean updateUserProfile(int userId, String name, String phone, String gender, String address) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_PROFILE)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            
            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, gender);
            statement.setString(4, address);
            statement.setTimestamp(5, now);
            statement.setInt(6, userId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("User profile updated successfully for user ID: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user profile for user ID: " + userId, e);
        }
        
        LOGGER.warning("Failed to update user profile for user ID: " + userId);
        return false;
    }
    
    /**
     * Update user email
     * 
     * @param userId User ID to update
     * @param newEmail New email address
     * @return true if successful, false otherwise
     */
    public boolean updateUserEmail(int userId, String newEmail) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return false;
        }
        
        if (newEmail == null || newEmail.trim().isEmpty()) {
            LOGGER.warning("Email parameter is null or empty");
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_EMAIL)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            
            statement.setString(1, newEmail.trim().toLowerCase());
            statement.setTimestamp(2, now);
            statement.setInt(3, userId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("User email updated successfully for user ID: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user email for user ID: " + userId, e);
        }
        
        LOGGER.warning("Failed to update user email for user ID: " + userId);
        return false;
    }
    
    /**
     * Check if email exists excluding current user
     * 
     * @param email Email to check
     * @param userId Current user ID to exclude
     * @return true if email exists, false otherwise
     */
    public boolean checkEmailExistsExcludingUser(String email, int userId) {
        if (email == null || email.trim().isEmpty()) {
            LOGGER.warning("Email parameter is null or empty");
            return false;
        }
        
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_EMAIL_EXISTS_EXCLUDING_USER)) {
            
            statement.setString(1, email.trim().toLowerCase());
            statement.setInt(2, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    boolean exists = count > 0;
                    LOGGER.info("Email exists check for " + email + " excluding user " + userId + ": " + exists);
                    return exists;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking email existence: " + email, e);
        }
        
        return false;
    }
    
    /**
     * Update user avatar URL
     * 
     * @param userId User ID to update
     * @param avatarUrl Avatar URL path
     * @return true if successful, false otherwise
     */
    public boolean updateAvatar(int userId, String avatarUrl) {
        if (userId <= 0) {
            LOGGER.warning("Invalid user ID: " + userId);
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_AVATAR)) {
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            
            statement.setString(1, avatarUrl);
            statement.setTimestamp(2, now);
            statement.setInt(3, userId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Avatar updated successfully for user ID: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating avatar for user ID: " + userId, e);
        }
        
        LOGGER.warning("Failed to update avatar for user ID: " + userId);
        return false;
    }
}
