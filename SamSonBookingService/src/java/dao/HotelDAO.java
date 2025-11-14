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
        "INSERT INTO Hotels (name, address, description) " +
        "VALUES (?, ?, ?)";
    
    private static final String GET_LAST_INSERTED_HOTEL = 
        "SELECT TOP 1 id FROM Hotels WHERE name = ? ORDER BY created_at DESC";
    
    private static final String UPDATE_HOTEL = 
        "UPDATE Hotels SET name = ?, address = ?, description = ?, manager_id = ?, " +
        "updated_at = GETDATE() WHERE id = ?";
    
    private static final String DELETE_HOTEL = 
        "DELETE FROM Hotels WHERE id = ?";
    
    private static final String GET_HOTEL_COUNT = 
        "SELECT COUNT(*) FROM Hotels";
    
    private static final String GET_HOTELS_PAGINATED = 
        "SELECT * FROM Hotels " +
        "WHERE (@search IS NULL OR name LIKE @search OR address LIKE @search) " +
        "AND (@managerId IS NULL OR manager_id = @managerId) " +
        "ORDER BY created_at DESC " +
        "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    
    private static final String COUNT_HOTELS_FILTERED = 
        "SELECT COUNT(*) FROM Hotels " +
        "WHERE (@search IS NULL OR name LIKE @search OR address LIKE @search) " +
        "AND (@managerId IS NULL OR manager_id = @managerId)";
    
    private static final String CHECK_HOTEL_HAS_BOOKINGS = 
        "SELECT COUNT(*) FROM Bookings WHERE hotel_id = ?";
    
    private static final String GET_HOTEL_BOOKINGS_COUNT = 
        "SELECT COUNT(*) as total_bookings, " +
        "SUM(CASE WHEN status = 'confirmed' THEN 1 ELSE 0 END) as confirmed_bookings, " +
        "SUM(CASE WHEN status = 'pending' THEN 1 ELSE 0 END) as pending_bookings " +
        "FROM Bookings WHERE hotel_id = ?";
    
    private static final String CHECK_HOTEL_HAS_TOUR_PACKAGES = 
        "SELECT COUNT(*) FROM Tour_Packages WHERE hotel_id = ?";
    
    private static final String CHECK_HOTEL_HAS_ROOMS = 
        "SELECT COUNT(*) FROM Rooms WHERE hotel_id = ?";
    
    /**
     * Get all hotels
     * @return List of all hotels
     */
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_HOTELS);
             ResultSet resultSet = statement.executeQuery()) {
            
            System.out.println("=== HotelDAO.getAllHotels Debug ===");
            System.out.println("SQL: " + GET_ALL_HOTELS);
            
            int count = 0;
            while (resultSet.next()) {
                hotels.add(mapResultSetToHotel(resultSet));
                count++;
            }
            
            System.out.println("Rows returned: " + count);
            LOGGER.info("Retrieved " + count + " hotels from database");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all hotels", e);
            System.err.println("SQL Error in getAllHotels: " + e.getMessage());
            e.printStackTrace();
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
     * Insert new hotel and return the generated ID
     * @param hotel Hotel object to insert
     * @return Hotel ID if successful, -1 otherwise
     */
    public int insertHotel(Hotel hotel) {
        try (Connection connection = DBContext.getConnection()) {
            // Insert hotel (không set manager_id - để NULL)
            try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_HOTEL)) {
                insertStmt.setString(1, hotel.getName());
                insertStmt.setString(2, hotel.getAddress());
                insertStmt.setString(3, hotel.getDescription());
                // manager_id được set NULL trong SQL, không cần set parameter
                
                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Query lại để lấy ID vừa tạo (dùng name + created_at DESC, không dùng manager_id)
                    try (PreparedStatement selectStmt = connection.prepareStatement(GET_LAST_INSERTED_HOTEL)) {
                        selectStmt.setString(1, hotel.getName());
                        
                        try (ResultSet rs = selectStmt.executeQuery()) {
                            if (rs.next()) {
                                int hotelId = rs.getInt("id");
                                LOGGER.info("✅ Hotel inserted successfully with ID: " + hotelId);
                                System.out.println("✅ HotelDAO.insertHotel - Hotel ID: " + hotelId);
                                System.out.println("   Hotel Name: " + hotel.getName());
                                System.out.println("   Manager ID: NULL (not set)");
                                return hotelId;
                            }
                        }
                    }
                } else {
                    LOGGER.warning("⚠️ No rows affected when inserting hotel: " + hotel.getName());
                    System.err.println("⚠️ WARNING: No rows affected when inserting hotel");
                    return -1;
                }
            }
            
            LOGGER.warning("⚠️ Hotel inserted but could not retrieve ID");
            System.err.println("⚠️ WARNING: Could not get hotel ID after insert");
            return -1;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error inserting hotel: " + hotel.getName(), e);
            System.err.println("❌ SQL Error inserting hotel: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Insert new hotel (backward compatibility - returns boolean)
     * @param hotel Hotel object to insert
     * @return true if successful, false otherwise
     * @deprecated Use insertHotel(Hotel) which returns int instead
     */
    @Deprecated
    public boolean insertHotelBoolean(Hotel hotel) {
        return insertHotel(hotel) > 0;
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
            statement.setInt(5, hotel.getId());
            
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
     * Get hotels with pagination and search
     * @param page Current page (1-based)
     * @param pageSize Number of items per page
     * @param searchKeyword Search keyword (can be null)
     * @param managerId Manager ID filter (can be null for all)
     * @return List of hotels
     */
    public List<Hotel> getHotelsPaginated(int page, int pageSize, String searchKeyword, Integer managerId) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM Hotels WHERE 1=1 ";
        
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql += "AND (name LIKE ? OR address LIKE ?) ";
        }
        if (managerId != null) {
            sql += "AND manager_id = ? ";
        }
        
        sql += "ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            int paramIndex = 1;
            
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String searchPattern = "%" + searchKeyword + "%";
                statement.setString(paramIndex++, searchPattern);
                statement.setString(paramIndex++, searchPattern);
            }
            if (managerId != null) {
                statement.setInt(paramIndex++, managerId);
            }
            
            statement.setInt(paramIndex++, (page - 1) * pageSize);
            statement.setInt(paramIndex, pageSize);
            
            // Debug logging
            System.out.println("=== HotelDAO.getHotelsPaginated Debug ===");
            System.out.println("SQL: " + sql);
            System.out.println("Page: " + page + ", PageSize: " + pageSize);
            System.out.println("SearchKeyword: " + searchKeyword);
            System.out.println("ManagerId: " + managerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                int count = 0;
                while (resultSet.next()) {
                    hotels.add(mapResultSetToHotel(resultSet));
                    count++;
                }
                System.out.println("Rows returned: " + count);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting paginated hotels", e);
            System.err.println("SQL Error in getHotelsPaginated: " + e.getMessage());
            e.printStackTrace();
        }
        return hotels;
    }
    
    /**
     * Count hotels with filters
     * @param searchKeyword Search keyword (can be null)
     * @param managerId Manager ID filter (can be null)
     * @return Total count
     */
    public int countHotelsFiltered(String searchKeyword, Integer managerId) {
        String sql = "SELECT COUNT(*) FROM Hotels WHERE 1=1 ";
        
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql += "AND (name LIKE ? OR address LIKE ?) ";
        }
        if (managerId != null) {
            sql += "AND manager_id = ? ";
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            int paramIndex = 1;
            
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String searchPattern = "%" + searchKeyword + "%";
                statement.setString(paramIndex++, searchPattern);
                statement.setString(paramIndex++, searchPattern);
            }
            if (managerId != null) {
                statement.setInt(paramIndex++, managerId);
            }
            
            // Debug logging
            System.out.println("=== HotelDAO.countHotelsFiltered Debug ===");
            System.out.println("SQL: " + sql);
            System.out.println("SearchKeyword: " + searchKeyword);
            System.out.println("ManagerId: " + managerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    System.out.println("Total count: " + count);
                    return count;
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting filtered hotels", e);
            System.err.println("SQL Error in countHotelsFiltered: " + e.getMessage());
            e.printStackTrace();
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
        
        // Handle nullable manager_id
        int managerId = resultSet.getInt("manager_id");
        if (!resultSet.wasNull()) {
            hotel.setManagerId(managerId);
        }
        
        // Map additional fields if they exist
        try {
            hotel.setRating(resultSet.getDouble("rating"));
        } catch (SQLException e) {
            // Field may not exist in some queries
        }
        
        try {
            hotel.setFeatured(resultSet.getBoolean("featured"));
        } catch (SQLException e) {
            // Field may not exist in some queries
        }
        
        try {
            hotel.setAmenities(resultSet.getString("amenities"));
        } catch (SQLException e) {
            // Field may not exist in some queries
        }
        
        try {
            hotel.setImageUrl(resultSet.getString("image_url"));
        } catch (SQLException e) {
            // Field may not exist in some queries
        }
        
        try {
            hotel.setCheckInTime(resultSet.getTime("check_in_time"));
        } catch (SQLException e) {
            // Field may not exist in some queries
        }
        
        try {
            hotel.setCheckOutTime(resultSet.getTime("check_out_time"));
        } catch (SQLException e) {
            // Field may not exist in some queries
        }
        
        try {
            java.sql.Date createdAt = resultSet.getDate("created_at");
            hotel.setCreatedAt(createdAt);
        } catch (SQLException e) {
            LOGGER.warning("created_at field not found or error reading: " + e.getMessage());
        }
        
        try {
            java.sql.Date updatedAt = resultSet.getDate("updated_at");
            hotel.setUpdatedAt(updatedAt);
        } catch (SQLException e) {
            LOGGER.warning("updated_at field not found or error reading: " + e.getMessage());
        }
        
        return hotel;
    }
    
    /**
     * Check if hotel has any bookings
     * @param hotelId Hotel ID to check
     * @return true if hotel has bookings, false otherwise
     */
    public boolean hasBookings(int hotelId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_HOTEL_HAS_BOOKINGS)) {
            
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking hotel bookings: " + hotelId, e);
            return false;
        }
    }
    
    /**
     * Get detailed booking statistics for a hotel
     * @param hotelId Hotel ID
     * @return Array [total, confirmed, pending] or null if error
     */
    public int[] getHotelBookingsStats(int hotelId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_HOTEL_BOOKINGS_COUNT)) {
            
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                int[] stats = new int[3];
                stats[0] = resultSet.getInt("total_bookings");
                stats[1] = resultSet.getInt("confirmed_bookings");
                stats[2] = resultSet.getInt("pending_bookings");
                return stats;
            }
            return null;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting hotel booking stats: " + hotelId, e);
            return null;
        }
    }
    
    /**
     * Get hotel by name and manager ID (for newly created hotel)
     * @param name Hotel name
     * @param managerId Manager ID
     * @return Hotel object or null if not found
     */
    public Hotel getHotelByNameAndManagerId(String name, int managerId) {
        String sql = "SELECT TOP 1 * FROM Hotels WHERE name = ? AND manager_id = ? ORDER BY created_at DESC";
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, name);
            statement.setInt(2, managerId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToHotel(resultSet);
            }
            return null;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting hotel by name and manager ID", e);
            return null;
        }
    }
    
    /**
     * Check if hotel has tour packages
     * @param hotelId Hotel ID
     * @return true if has tour packages, false otherwise
     */
    public boolean hasTourPackages(int hotelId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_HOTEL_HAS_TOUR_PACKAGES)) {
            
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking hotel tour packages: " + hotelId, e);
            return false;
        }
    }
    
    /**
     * Get count of tour packages for a hotel
     * @param hotelId Hotel ID
     * @return Number of tour packages
     */
    public int getTourPackagesCount(int hotelId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_HOTEL_HAS_TOUR_PACKAGES)) {
            
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting tour packages count: " + hotelId, e);
            return 0;
        }
    }
    
    /**
     * Check if hotel has rooms
     * @param hotelId Hotel ID
     * @return true if has rooms, false otherwise
     */
    public boolean hasRooms(int hotelId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_HOTEL_HAS_ROOMS)) {
            
            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking hotel rooms: " + hotelId, e);
            return false;
        }
    }
}
