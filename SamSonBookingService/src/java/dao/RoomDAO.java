package dao;

import entity.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO for Room entity
 * Handles all database operations for Rooms table
 * 
 * @author SamSon Travel Team
 */
public class RoomDAO {
    
    private static final Logger LOGGER = Logger.getLogger(RoomDAO.class.getName());
    
    /**
     * Lấy tất cả phòng
     * @return List of all rooms
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms ORDER BY room_type, price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} rooms", rooms.size());
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAllRooms", e);
        }
        
        return rooms;
    }
    
    /**
     * Lấy phòng theo ID
     * @param roomId ID của phòng
     * @return Room object or null if not found
     */
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM Rooms WHERE id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LOGGER.log(Level.INFO, "Found room with ID: {0}", roomId);
                    return mapResultSetToRoom(rs);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getRoomById: " + roomId, e);
        }
        
        LOGGER.log(Level.WARNING, "Room not found with ID: {0}", roomId);
        return null;
    }
    
    /**
     * Lấy tất cả phòng của một hotel
     * @param hotelId ID của hotel
     * @return List of rooms
     */
    public List<Room> getRoomsByHotel(int hotelId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE hotel_id = ? ORDER BY room_type, price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, hotelId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} rooms for hotel ID: {1}", 
                new Object[]{rooms.size(), hotelId});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getRoomsByHotel: " + hotelId, e);
        }
        
        return rooms;
    }
    
    /**
     * Lấy phòng theo hotel và loại phòng
     * @param hotelId ID của hotel
     * @param roomType Loại phòng (single, double, dormitory)
     * @return List of rooms
     */
    public List<Room> getRoomsByHotelAndType(int hotelId, String roomType) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE hotel_id = ? AND room_type = ? ORDER BY price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, hotelId);
            ps.setString(2, roomType);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} rooms for hotel ID: {1}, type: {2}", 
                new Object[]{rooms.size(), hotelId, roomType});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getRoomsByHotelAndType: " + hotelId + ", " + roomType, e);
        }
        
        return rooms;
    }
    
    /**
     * Lấy phòng theo loại (single, double, dormitory)
     * @param roomType Loại phòng
     * @return List of rooms
     */
    public List<Room> getRoomsByType(String roomType) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE room_type = ? ORDER BY price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, roomType);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} rooms of type: {1}", 
                new Object[]{rooms.size(), roomType});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getRoomsByType: " + roomType, e);
        }
        
        return rooms;
    }
    
    /**
     * Lấy phòng còn trống (available_rooms > 0)
     * @param hotelId ID của hotel
     * @return List of available rooms
     */
    public List<Room> getAvailableRooms(int hotelId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE hotel_id = ? AND available_rooms > 0 ORDER BY room_type, price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, hotelId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Loaded {0} available rooms for hotel ID: {1}", 
                new Object[]{rooms.size(), hotelId});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAvailableRooms: " + hotelId, e);
        }
        
        return rooms;
    }
    
    /**
     * Tìm kiếm phòng theo keyword
     * @param keyword Từ khóa tìm kiếm (room_type)
     * @return List of rooms matching keyword
     */
    public List<Room> searchRooms(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE room_type LIKE ? ORDER BY room_type, price";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
            
            LOGGER.log(Level.INFO, "Found {0} rooms matching keyword: {1}", 
                new Object[]{rooms.size(), keyword});
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in searchRooms: " + keyword, e);
        }
        
        return rooms;
    }
    
    /**
     * Cập nhật số lượng phòng còn trống (khi booking)
     * @param roomId ID của phòng
     * @param quantity Số lượng phòng đặt (số dương)
     * @return true nếu cập nhật thành công
     */
    public boolean decreaseAvailableRooms(int roomId, int quantity) {
        String sql = "UPDATE Rooms SET available_rooms = available_rooms - ?, updated_at = GETDATE() " +
                     "WHERE id = ? AND available_rooms >= ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setInt(2, roomId);
            ps.setInt(3, quantity);
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Decreased {0} rooms for room ID: {1}", 
                    new Object[]{quantity, roomId});
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Cannot decrease rooms - not enough available rooms or room not found");
                return false;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in decreaseAvailableRooms: " + roomId, e);
            return false;
        }
    }
    
    /**
     * Tăng số lượng phòng còn trống (khi hủy booking)
     * @param roomId ID của phòng
     * @param quantity Số lượng phòng trả lại (số dương)
     * @return true nếu cập nhật thành công
     */
    public boolean increaseAvailableRooms(int roomId, int quantity) {
        String sql = "UPDATE Rooms SET available_rooms = available_rooms + ?, updated_at = GETDATE() " +
                     "WHERE id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, quantity);
            ps.setInt(2, roomId);
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Increased {0} rooms for room ID: {1}", 
                    new Object[]{quantity, roomId});
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Room not found with ID: {0}", roomId);
                return false;
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in increaseAvailableRooms: " + roomId, e);
            return false;
        }
    }
    
    /**
     * Map ResultSet to Room object (DRY principle)
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setHotelId(rs.getInt("hotel_id"));
        room.setRoomType(rs.getString("room_type"));
        room.setPrice(rs.getDouble("price"));
        room.setTotalRooms(rs.getInt("total_rooms"));
        room.setAvailableRooms(rs.getInt("available_rooms"));
        room.setCreatedAt(rs.getTimestamp("created_at"));
        room.setUpdatedAt(rs.getTimestamp("updated_at"));
        return room;
    }
}
