/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gamel
 */
public class RoomDAO {
    
    // Lấy tất cả phòng từ tất cả khách sạn
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms ORDER BY hotel_id, room_type";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            System.out.println("RoomDAO: Đang thực thi query: " + sql);
            ResultSet rs = ps.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                try {
                    Room room = mapResultSetToRoom(rs);
                    rooms.add(room);
                    count++;
                    System.out.println("RoomDAO: Đã load phòng ID: " + room.getId() + ", Loại: " + room.getRoomType());
                } catch (SQLException e) {
                    System.err.println("RoomDAO: Lỗi khi map ResultSet sang Room object: " + e.getMessage());
                    e.printStackTrace();
                    // Tiếp tục với record tiếp theo
                }
            }
            
            System.out.println("RoomDAO: Tổng số phòng lấy được: " + count);
            
        } catch (SQLException e) {
            System.err.println("RoomDAO: Lỗi SQL khi lấy danh sách phòng: " + e.getMessage());
            System.err.println("RoomDAO: SQL State: " + e.getSQLState());
            System.err.println("RoomDAO: Error Code: " + e.getErrorCode());
            e.printStackTrace();
            // Trả về empty list thay vì throw exception
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("RoomDAO: Lỗi không xác định khi lấy danh sách phòng: " + e.getMessage());
            e.printStackTrace();
            // Trả về empty list thay vì throw exception
            return new ArrayList<>();
        }
        return rooms;
    }
    
    // Lấy tất cả phòng của một khách sạn
    public List<Room> getRoomsByHotelId(int hotelId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Rooms WHERE hotel_id = ? ORDER BY room_type";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, hotelId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }
    
    // Lấy phòng theo ID
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM Rooms WHERE id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm phòng mới
    public boolean insertRoom(Room room) {
        String sql = "INSERT INTO Rooms (hotel_id, room_type, price, total_rooms, available_rooms) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, room.getHotelId());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setInt(4, room.getTotalRooms());
            ps.setInt(5, room.getAvailableRooms());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Thêm phòng mới và trả về ID
    public int insertRoomAndReturnId(Room room) {
        String sql = "INSERT INTO Rooms (hotel_id, room_type, price, total_rooms, available_rooms) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, room.getHotelId());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setInt(4, room.getTotalRooms());
            ps.setInt(5, room.getAvailableRooms());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    // Cập nhật thông tin phòng
    public boolean updateRoom(Room room) {
        String sql = "UPDATE Rooms SET room_type = ?, price = ?, total_rooms = ?, available_rooms = ?, updated_at = GETDATE() WHERE id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, room.getRoomType());
            ps.setDouble(2, room.getPrice());
            ps.setInt(3, room.getTotalRooms());
            ps.setInt(4, room.getAvailableRooms());
            ps.setInt(5, room.getId());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa phòng
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM Rooms WHERE id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Ánh xạ ResultSet sang Room object
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
    
    /**
     * Kiểm tra xem room có booking nào không
     * @param roomId ID của room
     * @return true nếu có booking, false nếu không
     */
    public boolean hasBookings(int roomId) {
        String sql = "SELECT COUNT(*) FROM Bookings b " +
                    "INNER JOIN Rooms r ON b.hotel_id = r.hotel_id AND b.room_type = r.room_type " +
                    "WHERE r.id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy thống kê booking của room
     * @param roomId ID của room
     * @return Mảng [total, confirmed, pending] hoặc null nếu lỗi
     */
    public int[] getRoomBookingsStats(int roomId) {
        String sql = "SELECT COUNT(*) as total_bookings, " +
                    "SUM(CASE WHEN b.status = 'confirmed' THEN 1 ELSE 0 END) as confirmed_bookings, " +
                    "SUM(CASE WHEN b.status = 'pending' THEN 1 ELSE 0 END) as pending_bookings " +
                    "FROM Bookings b " +
                    "INNER JOIN Rooms r ON b.hotel_id = r.hotel_id AND b.room_type = r.room_type " +
                    "WHERE r.id = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int[] stats = new int[3];
                stats[0] = rs.getInt("total_bookings");
                stats[1] = rs.getInt("confirmed_bookings");
                stats[2] = rs.getInt("pending_bookings");
                return stats;
            }
            return null;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
