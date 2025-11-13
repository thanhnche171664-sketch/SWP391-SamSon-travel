/*
 * Servlet này xử lý việc liệt kê, tìm kiếm, lọc và xóa người dùng.
 */
package controller;

import dao.UserManagerDAO;
import entity.Role;
import entity.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private UserManagerDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserManagerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String action = request.getParameter("action");
            String keyword = request.getParameter("keyword");
            String roleIdStr = request.getParameter("roleId");

            // Lấy toàn bộ danh sách vai trò để hiển thị ở bộ lọc (dropdown)
            List<Role> roles = userDAO.getAllRoles();
            request.setAttribute("roles", roles);

            // Lấy danh sách người dùng
            List<User> users;
            if ("search".equals(action) && keyword != null && !keyword.trim().isEmpty()) {
                // Tìm kiếm người dùng theo từ khóa
                users = userDAO.searchUsers(keyword.trim());
            } else if (roleIdStr != null && !roleIdStr.isEmpty()) {
                // Lọc theo ID vai trò
                int roleId = Integer.parseInt(roleIdStr);
                users = userDAO.filterUsersByRole(roleId);
            } else {
                // Lấy tất cả người dùng
                users = userDAO.getAllUsers();
            }

            request.setAttribute("users", users);
            request.getRequestDispatcher("/listUsers.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Lỗi cơ sở dữ liệu", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        // Giữ lại trạng thái tìm kiếm/lọc khi redirect
        String keepKeyword = safe(request.getParameter("keepKeyword"));
        String keepRoleId = safe(request.getParameter("keepRoleId"));

        String redirectUrl = buildRedirectUrl(keepKeyword, keepRoleId);

        if ("delete".equals(action)) {
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                userDAO.deleteUser(userId);
                response.sendRedirect(redirectUrl + "&msg=" + urlEncode("Xóa người dùng thành công"));
                return;
            } catch (SQLException | NumberFormatException ex) {
                response.sendRedirect(redirectUrl + "&err=" + urlEncode("Có lỗi khi xóa người dùng"));
                return;
            }
        }

        response.sendRedirect(redirectUrl);
    }

    // Xây dựng URL redirect, giữ lại tham số tìm kiếm/lọc
    private String buildRedirectUrl(String keyword, String roleId) {
        StringBuilder sb = new StringBuilder("users");
        boolean first = true;
        if (keyword != null && !keyword.isEmpty()) {
            sb.append(first ? "?" : "&").append("action=search&keyword=").append(urlEncode(keyword));
            first = false;
        }
        if (roleId != null && !roleId.isEmpty()) {
            sb.append(first ? "?" : "&").append("roleId=").append(urlEncode(roleId));
        }
        if (first) {
            sb.append("?action=");
        }
        return sb.toString();
    }

    // Mã hóa URL UTF-8 an toàn
    private String urlEncode(String s) {
        try {
            return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8.name());
        } catch (Exception e) { return ""; }
    }

    // Trả về chuỗi rỗng nếu null 
    private String safe(String s) {
        return s == null ? "" : s;
    }
}
