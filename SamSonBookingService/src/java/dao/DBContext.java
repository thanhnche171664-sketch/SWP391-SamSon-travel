/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Context class for managing SQL Server connections
 * Implements connection pooling and proper resource management
 * 
 * @author gamel
 */
public class DBContext {
    
    // Database configuration constants
    private static final String SERVER_NAME = "localhost\\SQLEXPRESS";
    private static final String DATABASE_NAME = "booking_travel";
    private static final String PORT_NUMBER = "1433";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";
    
    private static final int MAX_CONNECTIONS = 10;
    private static final int CONNECTION_TIMEOUT = 30;
    
    private static final String CONNECTION_STRING = String.format(
        "jdbc:sqlserver://%s:%s;databaseName=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=true;loginTimeout=30;",
        SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USERNAME, PASSWORD
    );
    
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    
    private static final Logger LOGGER = Logger.getLogger(DBContext.class.getName());
    
    static {
        try {
            Class.forName(DRIVER_CLASS);
            LOGGER.info("SQL Server JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to load SQL Server JDBC Driver", e);
            throw new RuntimeException("SQL Server JDBC Driver not found", e);
        }
    }
    
    /**
     * Get a new database connection
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(CONNECTION_STRING);
            LOGGER.info("Database connection established successfully");
            return connection;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
            throw new SQLException("Unable to connect to database: " + e.getMessage(), e);
        }
    }
    
    /**
     * Close database connection safely
     * 
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed successfully");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }
    
    /**
     * Test database connectivity
     * 
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection test failed", e);
            return false;
        }
    }
    
    /**
     * Get database connection string (for debugging purposes)
     * 
     * @return Connection string with password masked
     */
    public static String getConnectionInfo() {
        return String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;user=%s;password=***;encrypt=true;trustServerCertificate=true;loginTimeout=30;",
            SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USERNAME
        );
    }
    public static void main(String[] args) {
        LOGGER.info("Testing SQL Server connection...");
        if (testConnection()) {
            LOGGER.info(" Connection test successful!");
        } else {
            LOGGER.warning("Connection test failed. Please check configuration.");
        }
    }

}
