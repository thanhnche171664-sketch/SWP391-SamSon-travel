<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách Dịch vụ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

    <style>
        body {
            background-color: #f8fafc;
            font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
        }

        .layout-wrapper {
            display: flex;
            min-height: 100vh;
        }

        .sidebar {
            width: 260px;
            background: linear-gradient(180deg, #4c6fff, #7c3aed);
            color: #fff;
            padding: 24px 20px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .sidebar-header {
            display: flex;
            flex-direction: column;
            gap: 4px;
            margin-bottom: 28px;
        }

        .sidebar-header-title {
            display: flex;
            align-items: center;
            gap: 10px;
            font-weight: 700;
            font-size: 1.1rem;
        }

        .sidebar-header-title i {
            font-size: 1.5rem;
        }

        .sidebar-header-subtitle {
            font-size: 0.9rem;
            opacity: 0.9;
        }

        .sidebar-menu-title {
            font-size: 0.85rem;
            letter-spacing: 0.04em;
            text-transform: uppercase;
            opacity: 0.8;
            margin-bottom: 10px;
        }

        .sidebar-menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .sidebar-menu li + li {
            margin-top: 4px;
        }

        .sidebar-link {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 8px 12px;
            border-radius: 999px;
            color: #e5e7eb;
            text-decoration: none;
            font-size: 0.94rem;
        }

        .sidebar-link i {
            font-size: 1.05rem;
        }

        .sidebar-link:hover {
            background-color: rgba(255, 255, 255, 0.12);
            color: #fff;
        }

        .sidebar-link.active {
            background-color: rgba(255, 255, 255, 0.25);
            color: #fff;
        }

        .sidebar-bottom {
            margin-top: 32px;
        }

        .sidebar-user-box {
            background: rgba(0, 0, 0, 0.1);
            border-radius: 16px;
            padding: 12px 14px;
            margin-bottom: 12px;
        }

        .sidebar-user-name {
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 2px;
        }

        .sidebar-user-role {
            font-size: 0.85rem;
            opacity: 0.9;
        }

        .logout-btn {
            width: 100%;
            border-radius: 999px;
            border: 1px solid rgba(255, 255, 255, 0.4);
            background: transparent;
            color: #f9fafb;
            padding: 7px 0;
            font-size: 0.9rem;
        }

        .logout-btn i {
            margin-right: 6px;
        }

        .logout-btn:hover {
            background-color: rgba(255, 255, 255, 0.12);
            color: #fff;
        }

        .main-content {
            flex: 1;
            padding: 24px;
        }

        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.05);
        }

        .page-title {
            color: #007b5e;
            font-weight: 700;
        }

        .btn-primary {
            background-color: #0d6efd;
            border: none;
        }

        .btn-primary:hover {
            background-color: #0b5ed7;
        }

        .search-box input {
            border-radius: 8px 0 0 8px;
        }

        .search-box button {
            border-radius: 0 8px 8px 0;
        }

        table th {
            color: #222;
            font-weight: 600;
        }

        table td {
            vertical-align: middle;
        }

        .status-active {
            color: green;
            font-weight: 600;
        }

        .status-inactive {
            color: red;
            font-weight: 600;
        }
    </style>
</head>
<body>

<div class="layout-wrapper">

    <aside class="sidebar">
        <div class="sidebar-top">
            <div class="sidebar-header">
                <div class="sidebar-header-title">
                    <i class="bi bi-building-fill"></i>
                    <span>Service Manager</span>
                </div>
                <div class="sidebar-header-subtitle">
                    <i class="bi bi-spa" style="font-size: 1.2rem; opacity: 0.9;"></i>
                </div>
            </div>

            <div class="sidebar-menu-title">
                Danh mục
            </div>
            <ul class="sidebar-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/wellness-list" class="sidebar-link active">
                        <i class="bi bi-list-ul"></i>
                        <span>Danh sách dịch vụ</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/wellness-add" class="sidebar-link">
                        <i class="bi bi-plus-circle"></i>
                        <span>Thêm dịch vụ</span>
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/transport-list" class="sidebar-link">
                        <i class="bi bi-bus-front-fill"></i>
                        <span>Phương tiện</span>
                    </a>
                </li>

                <li>
                    <a href="${pageContext.request.contextPath}/profile" class="sidebar-link">
                        <i class="bi bi-person-circle"></i>
                        <span>Thông tin cá nhân</span>
                    </a>
                </li>
            </ul>
        </div>

        <div class="sidebar-bottom">
            <div class="sidebar-user-box">
                <div class="sidebar-user-name">
                    <i class="bi bi-person-badge-fill"></i>
                    <span>
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <c:out value="${sessionScope.user.name}" />
                            </c:when>
                            <c:otherwise>
                                Service Manager 01
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="sidebar-user-role">
                    Service Manager
                </div>
            </div>
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button type="submit" class="logout-btn">
                    <i class="bi bi-box-arrow-right"></i> Đăng xuất
                </button>
            </form>
        </div>
    </aside>

    <main class="main-content">
        <div class="card p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="page-title">
                    <i class="bi bi-clipboard2-check-fill"></i>
                    Danh sách Dịch vụ Wellness
                </h3>
                <a href="${pageContext.request.contextPath}/wellness-add" class="btn btn-primary">
                    <i class="bi bi-plus-lg"></i> Thêm mới
                </a>
            </div>

            <form method="get" action="${pageContext.request.contextPath}/wellness-search" class="d-flex mb-3 search-box">
                <input type="text" name="search" class="form-control"
                       placeholder="Tìm theo tên dịch vụ..."
                       value="${keyword}">
                <button class="btn btn-success px-4" type="submit">Tìm kiếm</button>
            </form>

            <form method="get" action="${pageContext.request.contextPath}/wellness-list" class="mb-3 d-flex align-items-center gap-2">
                <label class="form-label mb-0 fw-semibold">Trạng thái:</label>
                <select name="status" class="form-select w-auto" onchange="this.form.submit()">
                    <option value="all" ${statusFilter eq 'all' ? 'selected' : ''}>Tất cả</option>
                    <option value="active" ${statusFilter eq 'active' ? 'selected' : ''}>Hoạt động</option>
                    <option value="inactive" ${statusFilter eq 'inactive' ? 'selected' : ''}>Ngừng</option>
                </select>
            </form>

            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Tên dịch vụ</th>
                        <th>Mô tả</th>
                        <th>Giá cơ bản</th>
                        <th>Thời lượng</th>
                        <th>Sức chứa</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="ws" items="${list}">
                        <tr>
                            <td>${ws.wellnessId}</td>
                            <td>${ws.serviceName}</td>
                            <td>${ws.description}</td>
                            <td>${ws.basePrice}</td>
                            <td>${ws.durationMinutes}</td>
                            <td>${ws.capacity}</td>
                            <td>
                                <span class="${ws.status eq 'ACTIVE' ? 'text-success' : 'text-danger'} fw-bold">
                                    ${ws.status}
                                </span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/wellness-detail?id=${ws.wellnessId}"
                                   class="btn btn-sm btn-outline-info">Chi tiết</a>
                                <a href="${pageContext.request.contextPath}/wellness-edit?id=${ws.wellnessId}"
                                   class="btn btn-sm btn-outline-primary">Sửa</a>
                                <a href="${pageContext.request.contextPath}/wellness-delete?id=${ws.wellnessId}"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('Bạn có chắc muốn xóa dịch vụ này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty list}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">Không có dữ liệu.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>

            <nav class="d-flex justify-content-center mt-4">
                <ul class="pagination">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/wellness-list?page=${i}&status=${statusFilter}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </nav>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
