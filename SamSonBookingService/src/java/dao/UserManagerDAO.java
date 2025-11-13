package dao;

import entity.Role;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManagerDAO {

    public UserManagerDAO() {

    }

    // Lấy toàn bộ user
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name "
                + "FROM Users u JOIN Roles r ON u.role_id = r.role_id";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        }
        return users;
    }

    // Tìm kiếm theo name hoặc email
    public List<User> searchUsers(String keyword) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name "
                + "FROM Users u JOIN Roles r ON u.role_id = r.role_id "
                + "WHERE u.name LIKE ? OR u.email LIKE ?";
        String kw = (keyword == null) ? "" : keyword.trim();
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + kw + "%");
            ps.setString(2, "%" + kw + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapToUser(rs));
                }
            }
        }
        return users;
    }

    // Lọc theo role
    public List<User> filterUsersByRole(int roleId) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.role_name "
                + "FROM Users u JOIN Roles r ON u.role_id = r.role_id "
                + "WHERE u.role_id = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapToUser(rs));
                }
            }
        }
        return users;
    }

    // Lấy toàn bộ role
    public List<Role> getAllRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT role_id, role_name FROM Roles";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roles.add(new Role(rs.getInt("role_id"), rs.getString("role_name")));
            }
        }
        return roles;
    }

    // Lấy user theo id (hữu ích cho màn Edit)
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT u.*, r.role_name "
                + "FROM Users u JOIN Roles r ON u.role_id = r.role_id "
                + "WHERE u.id = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToUser(rs);
                }
            }
        }
        return null;
    }

    // Xóa user theo id
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật user
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE Users "
                + "SET name = ?, password = ?, email = ?, phone = ?, gender = ?, address = ?, "
                + "    role_id = ?, status = ?, updated_at = GETDATE() "
                + "WHERE id = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getAddress());
            ps.setInt(7, user.getRoleId());
            ps.setString(8, user.getStatus());
            ps.setInt(9, user.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Thêm user mới
    public boolean addUser(User user) throws SQLException {
        String sql = "INSERT INTO Users "
                + "(name, password, email, phone, gender, address, role_id, status, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getAddress());
            ps.setInt(7, user.getRoleId());
            ps.setString(8, user.getStatus());

            return ps.executeUpdate() > 0;
        }
    }

    // Map ResultSet -> User
    private User mapToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("gender"),
                rs.getString("address"),
                rs.getString("avatar_url"), // ⭐ bổ sung đúng thiếu sót
                rs.getInt("role_id"),
                rs.getString("status"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }

}
