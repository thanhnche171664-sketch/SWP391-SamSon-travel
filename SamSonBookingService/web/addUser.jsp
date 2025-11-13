<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thêm người dùng mới</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f4f7f6;
                margin: 20px;
            }
            h2 {
                color: #007bff;
                border-bottom: 2px solid #007bff;
                padding-bottom: 10px;
            }
            form {
                background: #fff;
                padding: 25px;
                border-radius: 8px;
                max-width: 700px;
                margin: auto;
                box-shadow: 0 4px 8px rgba(0, 0, 0, .1);
            }
            .field-group {
                margin-top: 15px;
            }
            label {
                display: block;
                font-weight: 600;
            }
            input[type="text"], input[type="email"], input[type="password"], select, textarea {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                margin-top: 5px;
                font-size: 14px;
            }
            .button-group {
                margin-top: 25px;
                display: flex;
                justify-content: space-between;
            }
            button {
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
            }
            .save-btn {
                background: #007bff;
                color: #fff;
            }
            .save-btn:hover {
                background: #0069d9;
            }
            .back-btn {
                background: #6c757d;
                color: #fff;
            }
            .back-btn:hover {
                background: #5a6268;
            }
            .error-text {
                color: #d93025;
                font-size: 13px;
                margin-top: 6px;
            }
            .invalid {
                border-color: #d93025;
                background: #fff7f7;
            }
            .form-error {
                color: #d93025;
                text-align: center;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>
        <h2>➕ Thêm người dùng mới</h2>

        <c:if test="${not empty formError}">
            <p class="form-error">${formError}</p>
        </c:if>

        <form id="addForm" action="addUser" method="post" novalidate>

            <div class="field-group">
                <label for="name">Họ và tên *</label>
                <input id="name" name="name" type="text" required pattern="[A-Za-z\\s]+"
                       title="Chỉ được nhập chữ và khoảng trắng" value="${user.name}">
                <div class="error-text" id="err-name">
                    <c:out value="${errors.name}"/>
                </div>
            </div>

            <div class="field-group">
                <label for="password">Mật khẩu *</label>
                <input id="password" name="password" type="password" required value="${user.password}">
                <div class="error-text" id="err-password">
                    <c:out value="${errors.password}"/>
                </div>
            </div>

            <div class="field-group">
                <label for="email">Email *</label>
                <input id="email" name="email" type="email" required
                       pattern="^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"
                       title="Phải chứa ký tự '@' và '.'" value="${user.email}">
                <div class="error-text" id="err-email">
                    <c:out value="${errors.email}"/>
                </div>
            </div>

            <div class="field-group">
                <label for="phone">Số điện thoại *</label>
                <input id="phone" name="phone" type="text" required value="${user.phone}">
                <div class="error-text" id="err-phone">
                    <c:out value="${errors.phone}"/>
                </div>
            </div>

            <div class="field-group">
                <label for="gender">Giới tính *</label>
                <select id="gender" name="gender" required>
                    <option value="">-- Chọn giới tính --</option>
                    <option value="MALE" ${user.gender == 'MALE' ? 'selected' : ''}>Nam</option>
                    <option value="FEMALE" ${user.gender == 'FEMALE' ? 'selected' : ''}>Nữ</option>
                    <option value="OTHER" ${user.gender == 'OTHER' ? 'selected' : ''}>Khác</option>
                </select>
                <div class="error-text" id="err-gender">
                    <c:out value="${errors.gender}"/>
                </div>
            </div>

            <div class="field-group">
                <label for="address">Địa chỉ *</label>
                <textarea id="address" name="address" required>${user.address}</textarea>
                <div class="error-text" id="err-address">
                    <c:out value="${errors.address}"/>
                </div>
            </div>

            <div class="field-group">
                <label for="roleId">Vai trò *</label>
                <select id="roleId" name="roleId" required>
                    <option value="">-- Chọn vai trò --</option>
                    <c:forEach var="role" items="${roles}">
                        <option value="${role.roleId}" ${role.roleId == user.roleId ? 'selected' : ''}>
                            ${role.roleName}
                        </option>
                    </c:forEach>
                </select>
                <div class="error-text" id="err-roleId">
                    <c:out value="${errors.roleId}"/>
                </div>
            </div>

            <div class="field-group">
                <label for="status">Trạng thái *</label>
                <select id="status" name="status" required>
                    <option value="">-- Chọn trạng thái --</option>
                    <option value="ACTIVE" ${user.status == 'ACTIVE' ? 'selected' : ''}>Hoạt động</option>
                    <option value="INACTIVE" ${user.status == 'INACTIVE' ? 'selected' : ''}>Tạm ngưng</option>
                </select>
                <div class="error-text" id="err-status">
                    <c:out value="${errors.status}"/>
                </div>
            </div>

            <div class="button-group">
                <button type="submit" class="save-btn">💾 Lưu</button>
                <button type="button" class="back-btn" onclick="window.location.href = 'users'">⬅️ Quay lại</button>
            </div>
        </form>

        <script>
            (function () {
                const form = document.getElementById('addForm');

                function setError(fieldId, message) {
                    const input = document.getElementById(fieldId);
                    const err = document.getElementById('err-' + fieldId);
                    if (message) {
                        input.classList.add('invalid');
                        err.textContent = message;
                    } else {
                        input.classList.remove('invalid');
                        err.textContent = '';
                    }
                }

                function validate() {
                    let ok = true;

                    const name = document.getElementById('name').value.trim();
                    if (!name) {
                        setError('name', 'Vui lòng nhập họ và tên.');
                        ok = false;
                    } else if (!/^[A-Za-z\s]+$/.test(name)) {
                        setError('name', 'Họ tên chỉ được chứa chữ cái và khoảng trắng.');
                        ok = false;
                    } else
                        setError('name', '');

                    const pwd = document.getElementById('password').value.trim();
                    if (!pwd) {
                        setError('password', 'Vui lòng nhập mật khẩu.');
                        ok = false;
                    } else
                        setError('password', '');

                    const email = document.getElementById('email').value.trim();
                    if (!email) {
                        setError('email', 'Vui lòng nhập email.');
                        ok = false;
                    } else if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email)) {
                        setError('email', "Email phải đúng định dạng (phải có '@' và '.').");
                        ok = false;
                    } else
                        setError('email', '');

                    const phone = document.getElementById('phone').value.trim();
                    if (!phone) {
                        setError('phone', 'Vui lòng nhập số điện thoại.');
                        ok = false;
                    } else
                        setError('phone', '');

                    const gender = document.getElementById('gender').value;
                    if (!gender) {
                        setError('gender', 'Vui lòng chọn giới tính.');
                        ok = false;
                    } else
                        setError('gender', '');

                    const address = document.getElementById('address').value.trim();
                    if (!address) {
                        setError('address', 'Vui lòng nhập địa chỉ.');
                        ok = false;
                    } else
                        setError('address', '');

                    const roleId = document.getElementById('roleId').value;
                    if (!roleId) {
                        setError('roleId', 'Vui lòng chọn vai trò.');
                        ok = false;
                    } else
                        setError('roleId', '');

                    const status = document.getElementById('status').value;
                    if (!status) {
                        setError('status', 'Vui lòng chọn trạng thái.');
                        ok = false;
                    } else
                        setError('status', '');

                    return ok;
                }

                // Xóa lỗi khi người dùng nhập lại
                form.querySelectorAll('input,select,textarea').forEach(el => {
                    el.addEventListener('input', () => {
                        const id = el.id;
                        if (id)
                            setError(id, '');
                    });
                    el.addEventListener('change', () => {
                        const id = el.id;
                        if (id)
                            setError(id, '');
                    });
                });

                form.addEventListener('submit', function (e) {
                    if (!validate())
                        e.preventDefault();
                });
            })();
        </script>
    </body>
</html>
