/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for Image entity
 * @author DrDYNew
 */
public class ImageDAO extends DBContext {
    
    /**
     * Lấy tất cả ảnh của một entity (hotel hoặc room)
     * @param entityType 'hotel' hoặc 'room'
     * @param entityId ID của hotel hoặc room
     * @return Danh sách images
     */
    public List<Image> getImagesByEntity(String entityType, int entityId) {
        List<Image> images = new ArrayList<>();
        String sql = "SELECT * FROM Images WHERE entity_type = ? AND entity_id = ? ORDER BY display_order, created_at";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entityType);
            ps.setInt(2, entityId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                images.add(mapResultSetToImage(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }
    
    /**
     * Lấy ảnh chính (primary) của entity
     * @param entityType 'hotel' hoặc 'room'
     * @param entityId ID của hotel hoặc room
     * @return Primary image hoặc null
     */
    public Image getPrimaryImage(String entityType, int entityId) {
        String sql = "SELECT TOP 1 * FROM Images WHERE entity_type = ? AND entity_id = ? AND is_primary = 1";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entityType);
            ps.setInt(2, entityId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToImage(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy ảnh đầu tiên (nếu không có primary)
     * @param entityType 'hotel' hoặc 'room'
     * @param entityId ID của hotel hoặc room
     * @return First image hoặc null
     */
    public Image getFirstImage(String entityType, int entityId) {
        String sql = "SELECT TOP 1 * FROM Images WHERE entity_type = ? AND entity_id = ? ORDER BY display_order, id";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entityType);
            ps.setInt(2, entityId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToImage(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy ảnh theo ID
     * @param id ID của ảnh
     * @return Image hoặc null
     */
    public Image getImageById(int id) {
        String sql = "SELECT * FROM Images WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToImage(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Thêm ảnh mới
     * @param image Image object
     * @return true nếu thành công
     */
    public boolean insertImage(Image image) {
        String sql = "INSERT INTO Images (entity_type, entity_id, image_url, is_primary, display_order, alt_text) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, image.getEntityType());
            ps.setInt(2, image.getEntityId());
            ps.setString(3, image.getImageUrl());
            ps.setBoolean(4, image.isPrimary());
            ps.setInt(5, image.getDisplayOrder());
            ps.setString(6, image.getAltText());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật ảnh
     * @param image Image object
     * @return true nếu thành công
     */
    public boolean updateImage(Image image) {
        String sql = "UPDATE Images SET image_url = ?, is_primary = ?, display_order = ?, " +
                    "alt_text = ?, updated_at = GETDATE() WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, image.getImageUrl());
            ps.setBoolean(2, image.isPrimary());
            ps.setInt(3, image.getDisplayOrder());
            ps.setString(4, image.getAltText());
            ps.setInt(5, image.getId());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa ảnh
     * @param imageId ID của ảnh
     * @return true nếu thành công
     */
    public boolean deleteImage(int imageId) {
        String sql = "DELETE FROM Images WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, imageId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả ảnh của một entity
     * @param entityType 'hotel' hoặc 'room'
     * @param entityId ID của entity
     * @return true nếu thành công
     */
    public boolean deleteAllImagesByEntity(String entityType, int entityId) {
        String sql = "DELETE FROM Images WHERE entity_type = ? AND entity_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entityType);
            ps.setInt(2, entityId);
            return ps.executeUpdate() >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Set ảnh làm primary (và bỏ primary của các ảnh khác)
     * @param imageId ID của ảnh
     * @param entityType 'hotel' hoặc 'room'
     * @param entityId ID của entity
     * @return true nếu thành công
     */
    public boolean setPrimaryImage(int imageId, String entityType, int entityId) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Bỏ primary của tất cả ảnh
            String sql1 = "UPDATE Images SET is_primary = 0 WHERE entity_type = ? AND entity_id = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, entityType);
            ps1.setInt(2, entityId);
            ps1.executeUpdate();
            
            // Set primary cho ảnh được chọn
            String sql2 = "UPDATE Images SET is_primary = 1, updated_at = GETDATE() WHERE id = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, imageId);
            ps2.executeUpdate();
            
            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Đếm số lượng ảnh của entity
     * @param entityType 'hotel' hoặc 'room'
     * @param entityId ID của entity
     * @return Số lượng ảnh
     */
    public int countImages(String entityType, int entityId) {
        String sql = "SELECT COUNT(*) FROM Images WHERE entity_type = ? AND entity_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, entityType);
            ps.setInt(2, entityId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Map ResultSet to Image object
     */
    private Image mapResultSetToImage(ResultSet rs) throws SQLException {
        Image image = new Image();
        image.setId(rs.getInt("id"));
        image.setEntityType(rs.getString("entity_type"));
        image.setEntityId(rs.getInt("entity_id"));
        image.setImageUrl(rs.getString("image_url"));
        image.setPrimary(rs.getBoolean(
                "is_primary"));
        image.setDisplayOrder(rs.getInt("display_order"));
        image.setAltText(rs.getString("alt_text"));
        image.setCreatedAt(rs.getTimestamp("created_at"));
        image.setUpdatedAt(rs.getTimestamp("updated_at"));
        return image;
    }
}

