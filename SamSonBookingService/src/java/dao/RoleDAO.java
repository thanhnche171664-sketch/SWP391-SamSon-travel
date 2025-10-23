/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Role entity
 * Handles all database operations related to roles
 * 
 * @author SamSon Travel Team
 */
public class RoleDAO {
    
    private static final Logger LOGGER = Logger.getLogger(RoleDAO.class.getName());
    
    // SQL queries
    private static final String GET_ROLE_BY_ID = 
        "SELECT role_id, role_name FROM roles WHERE role_id = ?";
    
    private static final String GET_ROLE_BY_NAME = 
        "SELECT role_id, role_name FROM roles WHERE role_name = ?";
    
    private static final String GET_ALL_ROLES = 
        "SELECT role_id, role_name FROM roles ORDER BY role_id";
    
    private static final String CREATE_ROLE = 
        "INSERT INTO roles (role_name) VALUES (?)";
    
    private static final String UPDATE_ROLE = 
        "UPDATE roles SET role_name = ? WHERE role_id = ?";
    
    private static final String DELETE_ROLE = 
        "DELETE FROM roles WHERE role_id = ?";
    
    private static final String CHECK_ROLE_EXISTS = 
        "SELECT COUNT(*) FROM roles WHERE role_name = ?";
    
    /**
     * Get role by ID
     * 
     * @param roleId Role ID
     * @return Role object if found, null otherwise
     */
    public Role getRoleById(int roleId) {
        if (roleId <= 0) {
            LOGGER.warning("Invalid role ID: " + roleId);
            return null;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ROLE_BY_ID)) {
            
            statement.setInt(1, roleId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Role role = mapResultSetToRole(resultSet);
                    LOGGER.info("Role found by ID: " + roleId);
                    return role;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting role by ID: " + roleId, e);
        }
        
        LOGGER.info("No role found with ID: " + roleId);
        return null;
    }
    
    /**
     * Get role by name
     * 
     * @param roleName Role name
     * @return Role object if found, null otherwise
     */
    public Role getRoleByName(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            LOGGER.warning("Role name parameter is null or empty");
            return null;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ROLE_BY_NAME)) {
            
            statement.setString(1, roleName.trim());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Role role = mapResultSetToRole(resultSet);
                    LOGGER.info("Role found by name: " + roleName);
                    return role;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting role by name: " + roleName, e);
        }
        
        LOGGER.info("No role found with name: " + roleName);
        return null;
    }
    
    /**
     * Get all roles
     * 
     * @return List of all roles
     */
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_ROLES);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Role role = mapResultSetToRole(resultSet);
                roles.add(role);
            }
            
            LOGGER.info("Retrieved " + roles.size() + " roles from database");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all roles", e);
        }
        
        return roles;
    }
    
    /**
     * Create a new role
     * 
     * @param roleName Role name to create
     * @return Generated role ID if successful, -1 otherwise
     */
    public int createRole(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            LOGGER.warning("Role name parameter is null or empty");
            return -1;
        }
        
        // Check if role name already exists
        if (checkRoleExists(roleName)) {
            LOGGER.warning("Role name already exists: " + roleName);
            return -1;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_ROLE, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, roleName.trim());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int roleId = generatedKeys.getInt(1);
                        LOGGER.info("Role created successfully with ID: " + roleId);
                        return roleId;
                    }
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating role: " + roleName, e);
        }
        
        LOGGER.warning("Failed to create role: " + roleName);
        return -1;
    }
    
    /**
     * Update an existing role
     * 
     * @param roleId Role ID to update
     * @param newRoleName New role name
     * @return true if successful, false otherwise
     */
    public boolean updateRole(int roleId, String newRoleName) {
        if (roleId <= 0) {
            LOGGER.warning("Invalid role ID: " + roleId);
            return false;
        }
        
        if (newRoleName == null || newRoleName.trim().isEmpty()) {
            LOGGER.warning("New role name is null or empty");
            return false;
        }
        
        // Check if new role name already exists (excluding current role)
        Role existingRole = getRoleById(roleId);
        if (existingRole == null) {
            LOGGER.warning("Role not found with ID: " + roleId);
            return false;
        }
        
        if (!existingRole.getRoleName().equals(newRoleName.trim()) && checkRoleExists(newRoleName)) {
            LOGGER.warning("Role name already exists: " + newRoleName);
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ROLE)) {
            
            statement.setString(1, newRoleName.trim());
            statement.setInt(2, roleId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Role updated successfully with ID: " + roleId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating role with ID: " + roleId, e);
        }
        
        LOGGER.warning("Failed to update role with ID: " + roleId);
        return false;
    }
    
    /**
     * Delete a role
     * 
     * @param roleId Role ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteRole(int roleId) {
        if (roleId <= 0) {
            LOGGER.warning("Invalid role ID: " + roleId);
            return false;
        }
        
        // Check if role exists
        Role role = getRoleById(roleId);
        if (role == null) {
            LOGGER.warning("Role not found with ID: " + roleId);
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ROLE)) {
            
            statement.setInt(1, roleId);
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.info("Role deleted successfully with ID: " + roleId);
                return true;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting role with ID: " + roleId, e);
        }
        
        LOGGER.warning("Failed to delete role with ID: " + roleId);
        return false;
    }
    
    /**
     * Check if role name already exists
     * 
     * @param roleName Role name to check
     * @return true if role exists, false otherwise
     */
    public boolean checkRoleExists(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            LOGGER.warning("Role name parameter is null or empty");
            return false;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_ROLE_EXISTS)) {
            
            statement.setString(1, roleName.trim());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    boolean exists = count > 0;
                    LOGGER.info("Role exists check for " + roleName + ": " + exists);
                    return exists;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking role existence: " + roleName, e);
        }
        
        return false;
    }
    
    /**
     * Get predefined role IDs
     * 
     * @return Array of predefined role IDs
     */
    public static int[] getPredefinedRoleIds() {
        return new int[]{1, 2, 3, 4, 5}; // Administrator, Service Manager, Hotel Manager, Customer, Front Office
    }
    
    /**
     * Get predefined role names
     * 
     * @return Array of predefined role names
     */
    public static String[] getPredefinedRoleNames() {
        return new String[]{"Administrator", "Service Manager", "Hotel Manager", "Customer", "Front Office"};
    }
    
    /**
     * Initialize predefined roles in database
     * 
     * @return true if all roles initialized successfully, false otherwise
     */
    public boolean initializePredefinedRoles() {
        String[] roleNames = getPredefinedRoleNames();
        boolean allSuccess = true;
        
        for (String roleName : roleNames) {
            if (!checkRoleExists(roleName)) {
                int roleId = createRole(roleName);
                if (roleId == -1) {
                    LOGGER.warning("Failed to create predefined role: " + roleName);
                    allSuccess = false;
                }
            }
        }
        
        LOGGER.info("Predefined roles initialization completed. Success: " + allSuccess);
        return allSuccess;
    }
    
    /**
     * Map ResultSet to Role object
     * 
     * @param resultSet ResultSet from database query
     * @return Role object
     * @throws SQLException if mapping fails
     */
    private Role mapResultSetToRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setRoleId(resultSet.getInt("role_id"));
        role.setRoleName(resultSet.getString("role_name"));
        return role;
    }
}
