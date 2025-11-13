<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách Banner</title>

        <style>
            body {
                font-family: system-ui, sans-serif;
                margin: 24px;
                background: #f3f4f6;
            }
            .page-container {
                max-width: 1200px;
                margin: 0 auto;
            }
            .page-title {
                font-size: 24px;
                font-weight: 600;
                margin-bottom: 16px;
            }

            .card {
                background: #ffffff;
                border-radius: 12px;
                border: 1px solid #e5e7eb;
                box-shadow: 0 10px 25px rgba(15,23,42,0.06);
                padding: 16px 18px 18px;
            }

            .table-wrapper {
                margin-top: 8px;
                border-radius: 10px;
                border: 1px solid #e5e7eb;
                overflow-x: auto;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                font-size: 13px;
            }
            thead {
                background: #f9fafb;
            }
            th, td {
                padding: 8px 10px;
                border-bottom: 1px solid #e5e7eb;
                vertical-align: top;
            }
            th {
                font-weight: 600;
                color: #6b7280;
                white-space: nowrap;
            }
            tbody tr:hover {
                background: #f3f4ff;
            }

            .thumb {
                width: 120px;
                height: 60px;
                object-fit: cover;
                border-radius: 6px;
                border: 1px solid #e5e7eb;
                background: #f3f4f6;
            }

            .small-text {
                font-size: 12px;
                color: #6b7280;
            }
            .link {
                color: #2563eb;
                font-size: 12px;
                text-decoration: none;
            }
            .link:hover {
                text-decoration: underline;
            }

            .badge {
                padding: 3px 8px;
                border-radius: 999px;
                font-size: 11px;
                font-weight: 500;
            }
            .badge-show {
                background: #dcfce7;
                color: #166534;
            }
            .badge-hide {
                background: #fee2e2;
                color: #b91c1c;
            }

            .no-data {
                padding: 20px;
                text-align: center;
                color: #6b7280;
            }

            .top-bar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                flex-wrap: wrap;
                margin-bottom: 10px;
                gap: 12px;
            }
            .search-form {
                display: flex;
                gap: 10px;
                align-items: center;
            }
            /* Thêm class mới để nhóm các nút bên phải */
            .action-buttons {
                display: flex;
                gap: 10px; /* Giữ khoảng cách giữa các nút */
            }

            .search-input {
                padding: 6px 10px;
                border-radius: 999px;
                border: 1px solid #d1d5db;
                background: #f9fafb;
                font-size: 13px;
                min-width: 200px;
            }
            .search-input:focus {
                outline: none;
                border-color: #2563eb;
                background: #fff;
            }

            .btn {
                padding: 6px 12px;
                border-radius: 6px;
                font-size: 12px;
                cursor: pointer;
                border: none;
                text-decoration: none;
                display: inline-block;
            }
            .btn-outline {
                border: 1px solid #d1d5db;
                background: #fff;
                color: #374151;
            }
            .btn-outline:hover {
                background: #f3f4f6;
            }
            .btn-danger {
                background: #dc2626;
                color: #fff;
            }
            .btn-danger:hover {
                background: #b91c1c;
            }
            .btn-primary {
                background: #2563eb;
                color: #ffffff;
            }
            .btn-primary:hover {
                background: #1d4ed8;
            }

            .actions {
                display: flex;
                gap: 6px;
                flex-wrap: wrap;
            }

            .message {
                padding: 10px;
                border-radius: 6px;
                font-size: 13px;
                margin-bottom: 8px;
            }
            .message-success {
                background: #ecfdf5;
                color: #166534;
                border: 1px solid #bbf7d0;
            }
            .message-error    {
                background: #fef2f2;
                color: #b91c1c;
                border: 1px solid #fecaca;
            }
        </style>

        <script>
            function confirmDelete(id) {
                return confirm("Bạn có chắc chắn muốn xóa banner ID " + id + "?");
            }
        </script>
    </head>
    <body>

        <div class="page-container">

            <div class="page-title">Danh sách Banner</div>

            <div class="card">

                <c:if test="${not empty message}">
                    <div class="message message-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="message message-error">${error}</div>
                </c:if>

                <div class="top-bar">
                    <form class="search-form" method="get" action="${pageContext.request.contextPath}/banner_list">

                        <div style="display: flex; gap: 10px;">
                            <input type="text" name="q" class="search-input"
                                   placeholder="Tìm theo tiêu đề hoặc mô tả..."
                                   value="${keyword}"/>
                            <button type="submit" class="btn btn-outline">Tìm</button>
                        </div>
                        
                        <select name="status" class="search-input" onchange="this.form.submit()">
                            <option value="ALL"  ${status == 'ALL' ? 'selected' : ''}>Tất cả</option>
                            <option value="SHOW" ${status == 'SHOW' ? 'selected' : ''}>Hiện</option>
                            <option value="HIDE" ${status == 'HIDE' ? 'selected' : ''}>Ẩn</option>
                        </select>

                    </form>

                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/adminDashboard.jsp" class="btn btn-primary">
                            🏠 Trang chủ
                        </a>
                        
                        <a href="${pageContext.request.contextPath}/banner_add" class="btn btn-primary">
                            + Thêm Banner
                        </a>
                    </div>
                    
                </div>

                <div class="table-wrapper">

                    <c:if test="${empty bannerList}">
                        <div class="no-data">Không có banner nào.</div>
                    </c:if>

                    <c:if test="${not empty bannerList}">
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Hình</th>
                                    <th>Tiêu đề</th>
                                    <th>Mô tả</th>
                                    <th>Target</th>
                                    <th>Thời gian</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="b" items="${bannerList}">
                                    <tr>
                                        <td>${b.bannerId}</td>

                                        <td>
                                            <img class="thumb" src="${pageContext.request.contextPath}/${b.imageUrl}" alt="">
                                            <div class="small-text">${b.imageUrl}</div>
                                        </td>

                                        <td>${b.title}</td>

                                        <td><div class="small-text">${b.description}</div></td>

                                        <td>
                                            <c:if test="${not empty b.targetUrl}">
                                                <a class="link" href="${pageContext.request.contextPath}${b.targetUrl}" target="_blank">
                                                    ${b.targetUrl}
                                                </a>
                                            </c:if>
                                            <c:if test="${empty b.targetUrl}">-</c:if>
                                        </td>

                                        <td>
                                            <div class="small-text">
                                                Bắt đầu:
                                                <fmt:formatDate value="${b.startAt}" pattern="dd/MM/yyyy HH:mm"/>
                                            </div>
                                            <div class="small-text">
                                                Kết thúc:
                                                <c:choose>
                                                    <c:when test="${b.endAt != null}">
                                                        <fmt:formatDate value="${b.endAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </c:when>
                                                    <c:otherwise>Không giới hạn</c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${b.status == 'SHOW'}">
                                                    <span class="badge badge-show">SHOW</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-hide">HIDE</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <div class="actions">
                                                <a href="${pageContext.request.contextPath}/banner_edit?id=${b.bannerId}"
                                                   class="btn btn-outline">Sửa</a>
                                                <a href="${pageContext.request.contextPath}/banner_list?action=delete&id=${b.bannerId}"
                                                   class="btn btn-danger"
                                                   onclick="return confirmDelete(${b.bannerId});">
                                                     Xóa
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:if>

                </div>
            </div>
        </div>

    </body>
</html>