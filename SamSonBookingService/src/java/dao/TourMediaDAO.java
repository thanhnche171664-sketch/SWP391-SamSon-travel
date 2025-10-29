/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.TourMedia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TourMediaDAO - Data Access Object for TourMedia operations
 * Manages tour media files including images and videos for different sections
 * 
 * @author SamSon Travel Team
 */
public class TourMediaDAO {
    
    private static final Logger LOGGER = Logger.getLogger(TourMediaDAO.class.getName());
    
    // SQL Queries
    private static final String GET_ALL_MEDIA = 
        "SELECT * FROM Tour_Media WHERE status = 'ACTIVE' ORDER BY uploaded_at DESC";
    
    private static final String GET_MEDIA_BY_SECTION = 
        "SELECT * FROM Tour_Media WHERE section = ? AND status = 'ACTIVE' ORDER BY uploaded_at DESC";
    
    private static final String GET_FEATURED_MEDIA = 
        "SELECT TOP 10 * FROM Tour_Media WHERE status = 'ACTIVE' ORDER BY uploaded_at DESC";
    
    private static final String GET_MEDIA_BY_ID = 
        "SELECT * FROM Tour_Media WHERE media_id = ? AND status = 'ACTIVE'";
    
    private static final String GET_MEDIA_BY_TYPE = 
        "SELECT * FROM Tour_Media WHERE media_type = ? AND status = 'ACTIVE' ORDER BY uploaded_at DESC";
    
    private static final String SEARCH_MEDIA = 
        "SELECT * FROM Tour_Media WHERE status = 'ACTIVE' AND " +
        "(title LIKE ? OR description LIKE ? OR section LIKE ?) ORDER BY uploaded_at DESC";
    
    private static final String INSERT_MEDIA = 
        "INSERT INTO Tour_Media (section, title, description, media_type, file_url, uploaded_by, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_MEDIA = 
        "UPDATE Tour_Media SET section = ?, title = ?, description = ?, media_type = ?, " +
        "file_url = ?, status = ? WHERE media_id = ?";
    
    private static final String DELETE_MEDIA = 
        "UPDATE Tour_Media SET status = 'INACTIVE' WHERE media_id = ?";
    
    private static final String GET_MEDIA_COUNT = 
        "SELECT COUNT(*) FROM Tour_Media WHERE status = 'ACTIVE'";
    
    private static final String GET_MEDIA_COUNT_BY_SECTION = 
        "SELECT COUNT(*) FROM Tour_Media WHERE section = ? AND status = 'ACTIVE'";
    
    /**
     * Get all active media
     * @return List of all active media
     */
    public List<TourMedia> getAllMedia() {
        List<TourMedia> mediaList = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_MEDIA);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                mediaList.add(mapResultSetToMedia(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all media", e);
        }
        return mediaList;
    }
    
    /**
     * Get media by section
     * @param section Section name (hero, tours, destinations, etc.)
     * @return List of media for the section
     */
    public List<TourMedia> getMediaBySection(String section) {
        List<TourMedia> mediaList = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MEDIA_BY_SECTION)) {
            
            statement.setString(1, section);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mediaList.add(mapResultSetToMedia(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting media by section: " + section, e);
        }
        return mediaList;
    }
    
    /**
     * Get featured media (top 10)
     * @return List of featured media
     */
    public List<TourMedia> getFeaturedMedia() {
        List<TourMedia> mediaList = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_FEATURED_MEDIA);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                mediaList.add(mapResultSetToMedia(resultSet));
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting featured media", e);
        }
        return mediaList;
    }
    
    /**
     * Get media by ID
     * @param mediaId Media ID
     * @return TourMedia object or null if not found
     */
    public TourMedia getMediaById(int mediaId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MEDIA_BY_ID)) {
            
            statement.setInt(1, mediaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToMedia(resultSet);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting media by ID: " + mediaId, e);
        }
        return null;
    }
    
    /**
     * Get media by type
     * @param mediaType Media type (IMAGE, VIDEO)
     * @return List of media with specified type
     */
    public List<TourMedia> getMediaByType(String mediaType) {
        List<TourMedia> mediaList = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MEDIA_BY_TYPE)) {
            
            statement.setString(1, mediaType);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mediaList.add(mapResultSetToMedia(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting media by type: " + mediaType, e);
        }
        return mediaList;
    }
    
    /**
     * Search media by keyword
     * @param keyword Search keyword
     * @return List of matching media
     */
    public List<TourMedia> searchMedia(String keyword) {
        List<TourMedia> mediaList = new ArrayList<>();
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_MEDIA)) {
            
            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mediaList.add(mapResultSetToMedia(resultSet));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching media with keyword: " + keyword, e);
        }
        return mediaList;
    }
    
    /**
     * Insert new media
     * @param media TourMedia object to insert
     * @return true if successful, false otherwise
     */
    public boolean insertMedia(TourMedia media) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_MEDIA)) {
            
            statement.setString(1, media.getSection());
            statement.setString(2, media.getTitle());
            statement.setString(3, media.getDescription());
            statement.setString(4, media.getMediaType());
            statement.setString(5, media.getFileUrl());
            statement.setObject(6, media.getUploadedBy());
            statement.setString(7, media.getStatus());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting media: " + media.getTitle(), e);
            return false;
        }
    }
    
    /**
     * Update existing media
     * @param media TourMedia object to update
     * @return true if successful, false otherwise
     */
    public boolean updateMedia(TourMedia media) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_MEDIA)) {
            
            statement.setString(1, media.getSection());
            statement.setString(2, media.getTitle());
            statement.setString(3, media.getDescription());
            statement.setString(4, media.getMediaType());
            statement.setString(5, media.getFileUrl());
            statement.setString(6, media.getStatus());
            statement.setInt(7, media.getMediaId());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating media: " + media.getMediaId(), e);
            return false;
        }
    }
    
    /**
     * Delete media (soft delete)
     * @param mediaId Media ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteMedia(int mediaId) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MEDIA)) {
            
            statement.setInt(1, mediaId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting media: " + mediaId, e);
            return false;
        }
    }
    
    /**
     * Get total count of active media
     * @return Total count
     */
    public int getMediaCount() {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MEDIA_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting media count", e);
        }
        return 0;
    }
    
    /**
     * Get count of media by section
     * @param section Section name
     * @return Count of media in the section
     */
    public int getMediaCountBySection(String section) {
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MEDIA_COUNT_BY_SECTION)) {
            
            statement.setString(1, section);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting media count for section: " + section, e);
        }
        return 0;
    }
    
    /**
     * Get hero section images for homepage slider
     * @return List of hero images
     */
    public List<TourMedia> getHeroImages() {
        return getMediaBySection("hero");
    }
    
    /**
     * Get tour gallery images
     * @return List of tour images
     */
    public List<TourMedia> getTourImages() {
        return getMediaBySection("tours");
    }
    
    /**
     * Get destination images
     * @return List of destination images
     */
    public List<TourMedia> getDestinationImages() {
        return getMediaBySection("destinations");
    }
    
    /**
     * Map ResultSet to TourMedia object
     * @param resultSet ResultSet from database query
     * @return TourMedia object
     * @throws SQLException if mapping fails
     */
    private TourMedia mapResultSetToMedia(ResultSet resultSet) throws SQLException {
        TourMedia media = new TourMedia();
        media.setMediaId(resultSet.getInt("media_id"));
        media.setSection(resultSet.getString("section"));
        media.setTitle(resultSet.getString("title"));
        media.setDescription(resultSet.getString("description"));
        media.setMediaType(resultSet.getString("media_type"));
        media.setFileUrl(resultSet.getString("file_url"));
        
        // Handle nullable uploaded_by
        int uploadedBy = resultSet.getInt("uploaded_by");
        media.setUploadedBy(resultSet.wasNull() ? null : uploadedBy);
        
        media.setUploadedAt(resultSet.getDate("uploaded_at"));
        media.setStatus(resultSet.getString("status"));
        return media;
    }
}
