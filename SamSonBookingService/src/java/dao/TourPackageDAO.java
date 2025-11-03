/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.TourPackage;
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
 * TourPackageDAO - Data Access Object for TourPackage operations
 * Manages tour packages with different service tiers and pricing
 * 
 * @author SamSon Travel Team
 */
public class TourPackageDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TourPackageDAO.class.getName());
    
    // SQL Queries
    private static final String GET_PACKAGES_BY_TOUR = 
        "SELECT * FROM Tour_Packages WHERE tour_id = ? AND status = 'ACTIVE' " +
        "ORDER BY package_type ASC, price ASC";
    
    private static final String GET_PACKAGE_BY_ID = 
        "SELECT * FROM Tour_Packages WHERE package_id = ? AND status = 'ACTIVE'";
    
    private static final String GET_PACKAGES_BY_TYPE = 
        "SELECT * FROM Tour_Packages WHERE package_type = ? AND status = 'ACTIVE' " +
        "ORDER BY price ASC";
    
    private static final String GET_PACKAGES_BY_PRICE_RANGE = 
        "SELECT * FROM Tour_Packages WHERE status = 'ACTIVE' AND price BETWEEN ? AND ? " +
        "ORDER BY price ASC";
    
    private static final String GET_PACKAGES_WITH_MEALS = 
        "SELECT * FROM Tour_Packages WHERE status = 'ACTIVE' AND includes_meals = 1 " +
        "ORDER BY price ASC";
    
    private static final String GET_PACKAGES_WITH_WELLNESS = 
        "SELECT * FROM Tour_Packages WHERE status = 'ACTIVE' AND includes_wellness = 1 " +
        "ORDER BY price ASC";
    
    private static final String INSERT_PACKAGE = 
        "INSERT INTO Tour_Packages (tour_id, package_name, description, price, " +
        "hotel_id, transport_id, includes_meals, includes_wellness, package_type, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_PACKAGE = 
        "UPDATE Tour_Packages SET package_name = ?, description = ?, price = ?, " +
        "hotel_id = ?, transport_id = ?, includes_meals = ?, includes_wellness = ?, " +
        "package_type = ?, status = ?, updated_at = GETDATE() WHERE package_id = ?";
    
    private static final String DELETE_PACKAGE = 
        "UPDATE Tour_Packages SET status = 'INACTIVE', updated_at = GETDATE() " +
        "WHERE package_id = ?";
    
    private static final String GET_PACKAGE_COUNT = 
        "SELECT COUNT(*) FROM Tour_Packages WHERE status = 'ACTIVE'";
    
    private static final String GET_PACKAGES_BY_HOTEL = 
        "SELECT * FROM Tour_Packages WHERE hotel_id = ? AND status = 'ACTIVE' " +
        "ORDER BY price ASC";
    
    /**
     * Get all packages for a specific tour
     * @param tourId Tour ID
     * @return List of packages for the tour
     */
    public List<TourPackage> getPackagesByTour(int tourId) {
        List<TourPackage> packages = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PACKAGES_BY_TOUR)) {
            
            statement.setInt(1, tourId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    packages.add(mapResultSetToPackage(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting packages for tour: " + tourId, e);
        }
        return packages;
    }

    // Alias for API layer naming consistency
    public List<TourPackage> getPackagesByTourId(int tourId) {
        return getPackagesByTour(tourId);
    }
    
    /**
     * Get package by ID
     * @param packageId Package ID
     * @return TourPackage object or null if not found
     */
    public TourPackage getPackageById(int packageId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PACKAGE_BY_ID)) {
            
            statement.setInt(1, packageId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPackage(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting package by ID: " + packageId, e);
        }
        return null;
    }
    
    /**
     * Get packages by type
     * @param packageType Package type (BASIC, STANDARD, PREMIUM, LUXURY)
     * @return List of packages with specified type
     */
    public List<TourPackage> getPackagesByType(String packageType) {
        List<TourPackage> packages = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PACKAGES_BY_TYPE)) {
            
            statement.setString(1, packageType);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    packages.add(mapResultSetToPackage(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting packages by type: " + packageType, e);
        }
        return packages;
    }
    
    /**
     * Get packages by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of packages within price range
     */
    public List<TourPackage> getPackagesByPriceRange(double minPrice, double maxPrice) {
        List<TourPackage> packages = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PACKAGES_BY_PRICE_RANGE)) {
            
            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    packages.add(mapResultSetToPackage(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting packages by price range: " + minPrice + "-" + maxPrice, e);
        }
        return packages;
    }
    
    /**
     * Get packages that include meals
     * @return List of packages with meals included
     */
    public List<TourPackage> getPackagesWithMeals() {
        List<TourPackage> packages = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PACKAGES_WITH_MEALS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                packages.add(mapResultSetToPackage(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting packages with meals", e);
        }
        return packages;
    }
    
    /**
     * Get packages that include wellness services
     * @return List of packages with wellness included
     */
    public List<TourPackage> getPackagesWithWellness() {
        List<TourPackage> packages = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PACKAGES_WITH_WELLNESS);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                packages.add(mapResultSetToPackage(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting packages with wellness", e);
        }
        return packages;
    }
    
    /**
     * Get packages by hotel
     * @param hotelId Hotel ID
     * @return List of packages for the hotel
     */
    public List<TourPackage> getPackagesByHotel(int hotelId) {
        List<TourPackage> packages = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PACKAGES_BY_HOTEL)) {
            
            statement.setInt(1, hotelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    packages.add(mapResultSetToPackage(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting packages for hotel: " + hotelId, e);
        }
        return packages;
    }
    
    /**
     * Insert new package
     * @param packageObj TourPackage object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertPackage(TourPackage packageObj) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_PACKAGE)) {
            
            statement.setInt(1, packageObj.getTourId());
            statement.setString(2, packageObj.getPackageName());
            statement.setString(3, packageObj.getDescription());
            statement.setDouble(4, packageObj.getPrice());
            statement.setObject(5, packageObj.getHotelId());
            statement.setObject(6, packageObj.getTransportId());
            statement.setBoolean(7, packageObj.isIncludesMeals());
            statement.setBoolean(8, packageObj.isIncludesWellness());
            statement.setString(9, packageObj.getPackageType());
            statement.setString(10, packageObj.getStatus());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting package: " + packageObj.getPackageName(), e);
            return false;
        }
    }
    
    /**
     * Update existing package
     * @param packageObj TourPackage object to update
     * @return true if successful, false otherwise
     */
    public boolean updatePackage(TourPackage packageObj) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PACKAGE)) {
            
            statement.setString(1, packageObj.getPackageName());
            statement.setString(2, packageObj.getDescription());
            statement.setDouble(3, packageObj.getPrice());
            statement.setObject(4, packageObj.getHotelId());
            statement.setObject(5, packageObj.getTransportId());
            statement.setBoolean(6, packageObj.isIncludesMeals());
            statement.setBoolean(7, packageObj.isIncludesWellness());
            statement.setString(8, packageObj.getPackageType());
            statement.setString(9, packageObj.getStatus());
            statement.setInt(10, packageObj.getPackageId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating package: " + packageObj.getPackageId(), e);
            return false;
        }
    }
    
    /**
     * Delete package (soft delete)
     * @param packageId Package ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deletePackage(int packageId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PACKAGE)) {
            
            statement.setInt(1, packageId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting package: " + packageId, e);
            return false;
        }
    }
    
    /**
     * Get total count of active packages
     * @return Total count
     */
    public int getPackageCount() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PACKAGE_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting package count", e);
        }
        return 0;
    }
    
    /**
     * Get cheapest package for a tour
     * @param tourId Tour ID
     * @return Cheapest package or null if none found
     */
    public TourPackage getCheapestPackage(int tourId) {
        List<TourPackage> packages = getPackagesByTour(tourId);
        if (packages.isEmpty()) return null;
        
        return packages.stream()
                .min((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                .orElse(null);
    }
    
    /**
     * Get most expensive package for a tour
     * @param tourId Tour ID
     * @return Most expensive package or null if none found
     */
    public TourPackage getMostExpensivePackage(int tourId) {
        List<TourPackage> packages = getPackagesByTour(tourId);
        if (packages.isEmpty()) return null;
        
        return packages.stream()
                .max((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                .orElse(null);
    }
    
    /**
     * Map ResultSet to TourPackage object
     * @param resultSet ResultSet from database query
     * @return TourPackage object
     * @throws SQLException if mapping fails
     */
    private TourPackage mapResultSetToPackage(ResultSet resultSet) throws SQLException {
        TourPackage packageObj = new TourPackage();
        packageObj.setPackageId(resultSet.getInt("package_id"));
        packageObj.setTourId(resultSet.getInt("tour_id"));
        packageObj.setPackageName(resultSet.getString("package_name"));
        packageObj.setDescription(resultSet.getString("description"));
        packageObj.setPrice(resultSet.getDouble("price"));
        
        // Handle nullable hotel_id and transport_id
        int hotelId = resultSet.getInt("hotel_id");
        packageObj.setHotelId(resultSet.wasNull() ? null : hotelId);
        
        int transportId = resultSet.getInt("transport_id");
        packageObj.setTransportId(resultSet.wasNull() ? null : transportId);
        
        packageObj.setIncludesMeals(resultSet.getBoolean("includes_meals"));
        packageObj.setIncludesWellness(resultSet.getBoolean("includes_wellness"));
        packageObj.setPackageType(resultSet.getString("package_type"));
        packageObj.setStatus(resultSet.getString("status"));
        packageObj.setCreatedAt(resultSet.getTimestamp("created_at"));
        packageObj.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return packageObj;
    }
}
