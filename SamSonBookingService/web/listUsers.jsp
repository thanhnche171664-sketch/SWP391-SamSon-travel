<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Người Dùng - SamSon Travel</title>
        <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>

        <style>
            /* Biến màu giữ nguyên */
            :root {
                --bg-image-url: url('${pageContext.request.contextPath}/images/beach-bg.jpg');
                --main-text-color: #333;
                --primary-color: #007bff;
                --primary-hover-color: #0056b3;
                --table-header-bg: #007bff;
                --container-bg-color: rgba(255, 255, 255, 0.9);
            }

            /* Các style chung (body, body::before, main-wrapper) giữ nguyên */
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 0;
                padding: 0;
                color: var(--main-text-color);
                background: var(--bg-image-url) no-repeat center center fixed;
                background-size: cover;
                position: relative;
            }
            body::before {
                content: '';
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background-color: rgba(0, 0, 0, 0.2);
                z-index: -1;
            }
            .main-wrapper {
                max-width: 1400px;
                margin: 30px auto;
                padding: 30px;
                background-color: var(--container-bg-color);
                border-radius: 12px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
                backdrop-filter: blur(4px);
                -webkit-backdrop-filter: blur(4px);
            }
            /* Header, nút Home, controls-container giữ nguyên */
            .header-container {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                border-bottom: 2px solid var(--primary-color);
                padding-bottom: 10px;
            }
            h2 {
                color: var(--primary-color);
                font-weight: 600;
                margin: 0;
            }
            .btn-home {
                background-color: #28a745;
                color: white;
                padding: 10px 15px;
                border-radius: 5px;
                text-decoration: none;
                font-weight: bold;
                transition: background-color 0.3s;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .btn-home:hover {
                background-color: #1e7e34;
            }
            .controls-container {
                display: flex;
                gap: 20px;
                margin-bottom: 20px;
                padding: 15px;
                background-color: rgba(255, 255, 255, 0.8);
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.05);
                flex-wrap: wrap;
            }
            .control-form {
                display: flex;
                align-items: center;
                gap: 10px;
            }
            input[type="text"], select {
                padding: 10px 15px;
                border: 1px solid #ccc;
                border-radius: 4px;
                font-size: 14px;
                transition: border-color 0.3s;
            }
            input[type="text"]:focus, select:focus {
                border-color: var(--primary-color);
                outline: none;
            }
            button[type="submit"] {
                padding: 10px 15px;
                border: none;
                border-radius: 4px;
                background-color: var(--primary-color);
                color: white;
                cursor: pointer;
                font-size: 14px;
                font-weight: bold;
                transition: background-color 0.3s, transform 0.1s;
            }
            button[type="submit"]:hover {
                background-color: var(--primary-hover-color);
                transform: translateY(-1px);
            }
            .btn-add {
                background-color: #28a745;
                color: white;
                padding: 10px 15px;
                border-radius: 5px;
                text-decoration: none;
                font-weight: bold;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                transition: background-color 0.3s;
            }
            .btn-add:hover {
                background-color: #1e7e34;
            }
            .alert {
                margin: 10px 0;
                padding: 12px 16px;
                border-radius: 6px;
                font-weight: 500;
            }
            .alert-success {
                background: #e7f7ee;
                border:1px solid #b6e2c7;
                color:#1e7e34;
            }
            .alert-error {
                background: #fdeaea;
                border:1px solid #f5c6cb;
                color:#7f1d1d;
            }

            /* --- BẢNG VÀ CỘT (ĐÃ TỐI ƯU CHIỀU RỘNG) --- */

            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
                background-color: #fff;
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                border-radius: 8px;
                overflow: hidden;
                table-layout: fixed; /* Rất quan trọng: Bắt buộc các cột tuân theo width đã định */
            }

            th, td {
                padding: 12px 8px; /* Giảm padding ngang để tiết kiệm không gian */
                text-align: left;
                border-bottom: 1px solid #eee;
                word-wrap: break-word; /* Cho phép từ dài tự động xuống dòng */
            }

            th {
                background-color: var(--table-header-bg);
                color: white;
                text-transform: uppercase;
                font-size: 13px;
                letter-spacing: 0.05em;
                white-space: nowrap; /* Giữ tiêu đề cột không bị xuống dòng */
            }
            tr:nth-child(even) {
                background-color: #f8f9fa;
            }
            tr:hover {
                background-color: #e9ecef;
            }


            /* --- ĐỊNH DẠNG CHIỀU RỘNG TỪNG CỘT --- */

            /* Cột 1: ID */
            table tbody tr td:nth-child(1) {
                width: 40px;
            }

            /* Cột 2: Tên (Có thể xuống dòng) */
            table tbody tr td:nth-child(2) {
                width: 120px;
            }

            /* Cột 3: Email (Rộng hơn) */
            table tbody tr td:nth-child(3) {
                width: 180px;
                font-size: 13px;
            }

            /* Cột 4: SĐT */
            table tbody tr td:nth-child(4) {
                width: 80px;
                white-space: nowrap;
            }

            /* Cột 5: Giới Tính */
            table tbody tr td:nth-child(5) {
                width: 60px;
            }

            /* Cột 6: Địa Chỉ (Có thể xuống dòng) */
            table tbody tr td:nth-child(6) {
                width: 100px;
            }

            /* Cột 7: Quyền (Role) */
            table tbody tr td:nth-child(7) {
                width: 100px;
            }

            /* Cột 8: Trạng Thái (Đã fix lỗi xuống dòng) */
            table tbody tr td:nth-child(8) {
                width: 80px;
            }

            /* Cột 9 & 10: Ngày Tạo & Cập Nhật (Rất quan trọng phải thu gọn) */
            table tbody tr td:nth-child(9),
            table tbody tr td:nth-child(10) {
                width: 90px;
                font-size: 12px;
                white-space: nowrap; /* Bắt buộc giữ ngày giờ trên một dòng */
            }

            /* Cột 11: Hành Động (Actions) */
            table tbody tr td:nth-child(11) {
                width: 120px;
            }


            /* --- CSS CHO BADGE (STATUS) --- */

            .status-active, .status-inactive {
                color: white;
                padding: 4px 8px;
                border-radius: 4px;
                font-weight: bold;
                font-size: 12px;
                display: inline-block;
                white-space: nowrap;
                min-width: 65px;
                text-align: center;
            }
            .status-active {
                background-color: #28a745;
            }
            .status-inactive {
                background-color: #dc3545;
            }


            /* --- CSS CHO ACTIONS (Sửa, Xóa) --- */
            .actions {
                display: flex;
                gap: 4px;
                align-items: center;
            } /* Giảm gap giữa các nút */
            .actions a, .actions button {
                padding: 6px 8px; /* Giảm padding nút */
                border-radius: 4px;
                font-size: 13px; /* Giảm font chữ nút */
                text-decoration: none;
                font-weight: 500;
                transition: opacity 0.3s;
                white-space: nowrap;
            }
            .actions a {
                color: var(--primary-color);
                border: 1px solid var(--primary-color);
                background-color: #f0f8ff;
            }
            .actions a:hover {
                background-color: #e7f3ff;
                opacity: 0.9;
            }
            .actions button {
                background-color: #dc3545;
                color: white;
                border: none;
                cursor: pointer;
            }
            .actions button:hover {
                background-color: #c82333;
                opacity: 0.9;
            }
        </style>
    </head>
    <body>

        <div class="main-wrapper">
            <div class="header-container">
                <h2><i class="fas fa-users"></i> Quản Lý Người Dùng</h2>
                <a class="btn-home" href="${pageContext.request.contextPath}/adminDashboard.jsp" title="Quay về Trang Quản Trị">
                    <i class="fas fa-home"></i> Dashboard
                </a>
            </div>

            <c:if test="${not empty param.msg}">
                <div class="alert alert-success"><i class="fas fa-check-circle"></i> ${param.msg}</div>
            </c:if>
            <c:if test="${not empty param.err}">
                <div class="alert alert-error"><i class="fas fa-times-circle"></i> ${param.err}</div>
            </c:if>

            <div style="margin-bottom: 20px;">
                <a class="btn-add" href="<c:url value='/addUser'/>">➕ Thêm Người Dùng Mới</a>
            </div>

            <div class="controls-container">
                <form action="<c:url value='/users'/>" method="get" class="control-form">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="keyword" placeholder="Tìm theo tên hoặc email..." value="${fn:escapeXml(param.keyword)}">
                    <button type="submit"><i class="fas fa-search"></i> Tìm Kiếm</button>
                </form>

                <form action="<c:url value='/users'/>" method="get" class="control-form">
                    <label for="roleId">Lọc theo Quyền:</label>
                    <select name="roleId" id="roleId" onchange="this.form.submit()">
                        <option value="">-- Tất cả Quyền --</option>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.roleId}"
                                    <c:if test="${param.roleId ne null and param.roleId != '' and param.roleId == role.roleId}">selected="selected"</c:if>>
                                ${role.roleName}
                            </option>
                        </c:forEach>
                    </select>
                </form>
            </div>

            <c:choose>
                <c:when test="${empty users}">
                    <p style="text-align: center; padding: 20px; background-color: #fff3cd; border: 1px solid #ffeeba; color: #856404; border-radius: 4px;">
                        <i class="fas fa-exclamation-triangle"></i> Không tìm thấy người dùng nào.
                    </p>
                </c:when>
                <c:otherwise>
                    <div style="overflow-x: auto;">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên</th>
                                    <th>Email</th>
                                    <th>SĐT</th>
                                    <th>Giới tính</th>
                                    <th>Địa chỉ</th>
                                    <th>Quyền</th>
                                    <th>Trạng thái</th>
                                    <th>Ngày tạo</th>
                                    <th>Cập nhật</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="user" items="${users}">
                                    <tr>
                                        <td>${user.id}</td>
                                        <td>${user.name}</td>
                                        <td>${user.email}</td>
                                        <td>${user.phone}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.gender == 'MALE'}">Nam</c:when>
                                                <c:when test="${user.gender == 'FEMALE'}">Nữ</c:when>
                                                <c:otherwise>Khác</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${user.address}</td>
                                        <td>
                                            <c:forEach var="role" items="${roles}">
                                                <c:if test="${role.roleId == user.roleId}">
                                                    <strong>${role.roleName}</strong>
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                        <td>
                                            <span class="status-${user.status == 'ACTIVE' ? 'active' : 'inactive'}">
                                                ${user.status == 'ACTIVE' ? 'Hoạt động' : 'Bị khóa'}
                                            </span>
                                        </td>
                                        <td><fmt:formatDate value="${user.createdAt}" pattern="dd-MM-yyyy HH:mm"/></td>
                                        <td><fmt:formatDate value="${user.updatedAt}" pattern="dd-MM-yyyy HH:mm"/></td>
                                        <td class="actions">
                                            <a href="<c:url value='/editUser'><c:param name='userId' value='${user.id}'/></c:url>" title="Chỉnh sửa người dùng này"><i class="fas fa-edit"></i> Sửa</a>

                                                <form action="<c:url value='/users'/>" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn XÓA người dùng: ${fn:escapeXml(user.name)} không?')">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="userId" value="${user.id}">
                                                <button type="submit" title="Xóa người dùng này"><i class="fas fa-trash-alt"></i> Xóa</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>