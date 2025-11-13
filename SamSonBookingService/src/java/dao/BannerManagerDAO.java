package dao;

import entity.Banner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BannerManagerDAO {

    // Lấy tất cả banner
    public List<Banner> getAllBanners() throws SQLException {
        List<Banner> list = new ArrayList<>();

        String sql = "SELECT banner_id, image_url, title, description, "
                + "       target_url, start_at, end_at, status "
                + "FROM Banners "
                + "ORDER BY start_at DESC, banner_id DESC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Banner b = new Banner();
                b.setBannerId(rs.getInt("banner_id"));
                b.setImageUrl(rs.getString("image_url"));
                b.setTitle(rs.getString("title"));
                b.setDescription(rs.getString("description"));
                b.setTargetUrl(rs.getString("target_url"));
                b.setStartAt(rs.getTimestamp("start_at"));
                b.setEndAt(rs.getTimestamp("end_at"));
                b.setStatus(rs.getString("status"));

                list.add(b);
            }
        }

        return list;
    }

    // Lọc theo trạng thái SHOW / HIDE / ALL
    public List<Banner> filterByStatus(String status) throws SQLException {
        if (status == null || status.equals("ALL")) {
            return getAllBanners();
        }

        List<Banner> list = new ArrayList<>();

        String sql = "SELECT banner_id, image_url, title, description, "
                + "       target_url, start_at, end_at, status "
                + "FROM Banners "
                + "WHERE status = ? "
                + "ORDER BY start_at DESC, banner_id DESC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Banner b = new Banner();
                    b.setBannerId(rs.getInt("banner_id"));
                    b.setImageUrl(rs.getString("image_url"));
                    b.setTitle(rs.getString("title"));
                    b.setDescription(rs.getString("description"));
                    b.setTargetUrl(rs.getString("target_url"));
                    b.setStartAt(rs.getTimestamp("start_at"));
                    b.setEndAt(rs.getTimestamp("end_at"));
                    b.setStatus(rs.getString("status"));
                    list.add(b);
                }
            }
        }

        return list;
    }

    // Tìm kiếm kết hợp với trạng thái (SHOW/HIDE/ALL)
    public List<Banner> searchBanners(String keyword, String status) throws SQLException {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != null && !status.equals("ALL");

        // Không có keyword, có trạng thái → lọc theo trạng thái
        if (!hasKeyword && hasStatus) {
            return filterByStatus(status);
        }

        // Không keyword, không trạng thái → lấy tất cả
        if (!hasKeyword) {
            return getAllBanners();
        }

        // Có keyword → search, có thể kèm trạng thái
        keyword = keyword.trim();
        String like = "%" + keyword + "%";

        StringBuilder sql = new StringBuilder(
                "SELECT banner_id, image_url, title, description, "
                + "       target_url, start_at, end_at, status "
                + "FROM Banners "
                + "WHERE (title LIKE ? OR description LIKE ?)"
        );

        if (hasStatus) {
            sql.append(" AND status = ?");
        }

        sql.append(" ORDER BY start_at DESC, banner_id DESC");

        List<Banner> list = new ArrayList<>();

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, like);
            ps.setString(2, like);

            if (hasStatus) {
                ps.setString(3, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Banner b = new Banner();
                    b.setBannerId(rs.getInt("banner_id"));
                    b.setImageUrl(rs.getString("image_url"));
                    b.setTitle(rs.getString("title"));
                    b.setDescription(rs.getString("description"));
                    b.setTargetUrl(rs.getString("target_url"));
                    b.setStartAt(rs.getTimestamp("start_at"));
                    b.setEndAt(rs.getTimestamp("end_at"));
                    b.setStatus(rs.getString("status"));
                    list.add(b);
                }
            }
        }

        return list;
    }

    // Xóa banner theo ID
    public boolean deleteBanner(int bannerId) throws SQLException {
        String sql = "DELETE FROM Banners WHERE banner_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bannerId);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // Lấy 1 banner theo ID
    public Banner getBannerById(int bannerId) throws SQLException {
        String sql = "SELECT banner_id, image_url, title, description, "
                + "       target_url, start_at, end_at, status "
                + "FROM Banners "
                + "WHERE banner_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bannerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Banner b = new Banner();
                    b.setBannerId(rs.getInt("banner_id"));
                    b.setImageUrl(rs.getString("image_url"));
                    b.setTitle(rs.getString("title"));
                    b.setDescription(rs.getString("description"));
                    b.setTargetUrl(rs.getString("target_url"));
                    b.setStartAt(rs.getTimestamp("start_at"));
                    b.setEndAt(rs.getTimestamp("end_at"));
                    b.setStatus(rs.getString("status"));
                    return b;
                }
            }
        }

        return null;
    }

    // Cập nhật banner
    public boolean updateBanner(Banner b) throws SQLException {
        String sql = "UPDATE Banners "
                + "SET image_url = ?, "
                + "    title = ?, "
                + "    description = ?, "
                + "    target_url = ?, "
                + "    start_at = ?, "
                + "    end_at = ?, "
                + "    status = ? "
                + "WHERE banner_id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getImageUrl());
            ps.setString(2, b.getTitle());
            ps.setString(3, b.getDescription());
            ps.setString(4, b.getTargetUrl());

            if (b.getStartAt() != null) {
                ps.setTimestamp(5, new Timestamp(b.getStartAt().getTime()));
            } else {
                ps.setTimestamp(5, null);
            }

            if (b.getEndAt() != null) {
                ps.setTimestamp(6, new Timestamp(b.getEndAt().getTime()));
            } else {
                ps.setTimestamp(6, null);
            }

            ps.setString(7, b.getStatus());
            ps.setInt(8, b.getBannerId());

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // Thêm mới banner
    public boolean addBanner(Banner b) throws SQLException {
        String sql = "INSERT INTO Banners "
                + "(image_url, title, description, target_url, start_at, end_at, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getImageUrl());
            ps.setString(2, b.getTitle());
            ps.setString(3, b.getDescription());
            ps.setString(4, b.getTargetUrl());

            if (b.getStartAt() != null) {
                ps.setTimestamp(5, new Timestamp(b.getStartAt().getTime()));
            } else {
                ps.setTimestamp(5, null);
            }

            if (b.getEndAt() != null) {
                ps.setTimestamp(6, new Timestamp(b.getEndAt().getTime()));
            } else {
                ps.setTimestamp(6, null);
            }

            ps.setString(7, b.getStatus());

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}
