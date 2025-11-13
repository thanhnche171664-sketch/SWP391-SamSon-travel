package controller;

import dao.UserManagerDAO;
import entity.Role;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/editUser")
public class EditUserServlet extends HttpServlet {

    private UserManagerDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserManagerDAO();
    }

    private boolean isValidName(String name) {
        return name != null && name.trim().matches("(?U)^[\\p{L}\\s]+$");
    }

    // Kiểm tra định dạng email hợp lệ
    private boolean isValidEmail(String email) {
        return email != null && email.trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    // Kiểm tra số điện thoại gồm đúng 10 chữ số
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\d{10}$");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        try {
            // Lấy danh sách vai trò
            List<Role> roles = userDAO.getAllRoles();
            request.setAttribute("roles", roles);

            // Tìm người dùng theo ID
            User user = null;
            for (User u : userDAO.getAllUsers()) {
                if (u.getId() == userId) {
                    user = u;
                    break;
                }
            }

            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("/editUser.jsp").forward(request, response);
            } else {
                response.sendRedirect("users");
            }

        } catch (SQLException e) {
            throw new ServletException("Lỗi cơ sở dữ liệu", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> errors = new HashMap<>();

        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String roleIdStr = request.getParameter("roleId");
        String status = request.getParameter("status");

        Integer id = null;
        Integer roleId = null;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            errors.put("form", "ID người dùng không hợp lệ.");
        }

        // Kiểm tra dữ liệu bắt buộc
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Vui lòng nhập họ tên.");
        } else if (!isValidName(name)) {
            errors.put("name", "Họ tên chỉ được chứa chữ cái và khoảng trắng.");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Vui lòng nhập mật khẩu.");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Vui lòng nhập email.");
        } else if (!isValidEmail(email)) {
            errors.put("email", "Email không hợp lệ (phải có '@' và '.').");
        }

        if (phone == null || phone.trim().isEmpty()) {
            errors.put("phone", "Vui lòng nhập số điện thoại.");
        } else if (!isValidPhone(phone)) {
            errors.put("phone", "Số điện thoại phải có đúng 10 chữ số.");
        }

        if (gender == null || gender.trim().isEmpty()) {
            errors.put("gender", "Vui lòng chọn giới tính.");
        }

        if (address == null || address.trim().isEmpty()) {
            errors.put("address", "Vui lòng nhập địa chỉ.");
        }

        try {
            roleId = Integer.parseInt(roleIdStr);
        } catch (Exception e) {
            errors.put("roleId", "Vui lòng chọn vai trò.");
        }

        if (status == null || status.trim().isEmpty()) {
            errors.put("status", "Vui lòng chọn trạng thái.");
        }

        // Kiểm tra trùng email (trừ người dùng hiện tại)
        try {
            final String emailValue = email;
            final Integer idValue = id;
            boolean emailTakenByOther = userDAO.getAllUsers().stream()
                    .anyMatch(u -> u.getEmail() != null
                    && u.getEmail().equalsIgnoreCase(emailValue)
                    && !Objects.equals(u.getId(), idValue));
            if (emailTakenByOther) {
                errors.put("email", "Email này đã tồn tại. Vui lòng sử dụng email khác.");
            }
        } catch (SQLException e) {
            throw new ServletException("Không thể kiểm tra email trùng lặp", e);
        }

        // Tạo đối tượng User tạm để lưu dữ liệu nhập
        User draft = new User();
        if (id != null) {
            draft.setId(id);
        }
        draft.setName(name);
        draft.setPassword(password);
        draft.setEmail(email);
        draft.setPhone(phone);
        draft.setGender(gender);
        draft.setAddress(address);
        if (roleId != null) {
            draft.setRoleId(roleId);
        }
        draft.setStatus(status);

        // Nếu có lỗi, quay lại form chỉnh sửa
        if (!errors.isEmpty()) {
            try {
                List<Role> roles = userDAO.getAllRoles();
                request.setAttribute("roles", roles);
            } catch (SQLException e) {
                throw new ServletException("Không thể tải lại danh sách vai trò", e);
            }
            request.setAttribute("errors", errors);
            request.setAttribute("user", draft);
            request.getRequestDispatcher("/editUser.jsp").forward(request, response);
            return;
        }

        // Cập nhật người dùng
        try {
            boolean updated = userDAO.updateUser(draft);
            if (updated) {
                response.sendRedirect("users");
            } else {
                request.setAttribute("formError", "Cập nhật thất bại. Vui lòng thử lại.");
                List<Role> roles = userDAO.getAllRoles();
                request.setAttribute("roles", roles);
                request.setAttribute("user", draft);
                request.getRequestDispatcher("/editUser.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Lỗi khi cập nhật người dùng", e);
        }
    }
}
