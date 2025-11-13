<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách dịch vụ vận chuyển</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
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
            box-shadow: 0 4px 10px rgba(0,0,0,0.08);
        }

        .input-group .form-control {
            border-radius: 12px 0 0 12px;
        }

        .input-group .btn {
            border-radius: 0 12px 12px 0;
        }

        table img {
            width: 80px;
            height: 55px;
            object-fit: cover;
            border-radius: 6px;
        }

        .btn {
            border-radius: 6px;
        }
    </style>
</head>
<body>
<fmt:setLocale value="vi_VN"/>

<div class="layout-wrapper">

    <aside class="sidebar">
        <div class="sidebar-top">
            <div class="sidebar-header">
                <div class="sidebar-header-title">
                    <i class="bi bi-building-fill"></i>
                    <span>Service Manager</span>
                </div>
                <div class="sidebar-header-subtitle">
                    <i class="bi bi-bus-front-fill" style="font-size: 1.2rem; opacity: 0.9;"></i>
                </div>
            </div>

            <div class="sidebar-menu-title">
                Danh mục
            </div>
            <ul class="sidebar-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/wellness-list" class="sidebar-link">
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
                    <a href="${pageContext.request.contextPath}/transport-list" class="sidebar-link active">
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
        <div class="card p-4 shadow-sm">

            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3 class="fw-bold mb-0 text-primary">Danh sách dịch vụ vận chuyển</h3>
                <a href="${pageContext.request.contextPath}/transport-add" class="btn btn-success fw-semibold">+ Thêm mới</a>
            </div>

            <form method="get" action="${pageContext.request.contextPath}/transport-list" class="mb-4">
                <div class="input-group input-group-lg">
                    <input type="text"
                           name="search"
                           value="${search}"
                           class="form-control"
                           placeholder="🔍 Tìm theo tên xe hoặc điểm đón...">
                    <button class="btn btn-primary px-4 fw-semibold" type="submit">Tìm kiếm</button>
                </div>
            </form>

            <c:if test="${param.message eq 'add_success'}">
                <div class="alert alert-success">Thêm dịch vụ thành công!</div>
            </c:if>
            <c:if test="${param.message eq 'update_success'}">
                <div class="alert alert-success">Cập nhật dịch vụ thành công!</div>
            </c:if>
            <c:if test="${param.message eq 'deleted'}">
                <div class="alert alert-success">Xóa dịch vụ thành công!</div>
            </c:if>

            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Ảnh</th>
                        <th>Tên xe</th>
                        <th>Loại</th>
                        <th>Điểm đón</th>
                        <th>Giờ đi</th>
                        <th>Giá</th>
                        <th>Sức chứa</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="t" items="${list}">
                        <tr>
                            <td>${t.transportId}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty t.image}">
                                        <img src="${pageContext.request.contextPath}/${t.image}" alt="${t.vehicleName}">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/Imagetransport/default_transport.jpg" alt="No image">
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${t.vehicleName}</td>
                            <td>${t.vehicleType}</td>
                            <td>${t.pickupLocation}</td>
                            <td>
                                <fmt:formatDate value="${t.departureTime}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
                            <td>
                                <fmt:formatNumber value="${t.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </td>
                            <td>${t.capacity}</td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/transport-detail?id=${t.transportId}"
                                   class="btn btn-sm btn-outline-info me-1">
                                    Chi tiết
                                </a>
                                <a href="${pageContext.request.contextPath}/transport-edit?id=${t.transportId}"
                                   class="btn btn-sm btn-outline-primary me-1">
                                    Sửa
                                </a>
                                <a href="${pageContext.request.contextPath}/transport-delete?id=${t.transportId}"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('Bạn có chắc muốn xóa dịch vụ này?');">
                                    Xóa
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty list}">
                        <tr><td colspan="9" class="text-center text-muted py-4">Không có dữ liệu phù hợp.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>

            <c:if test="${totalPages gt 1}">
                <nav class="mt-3">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="transport-list?page=${currentPage - 1}&search=${fn:escapeXml(search)}">Trước</a>
                        </li>

                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link"
                                   href="transport-list?page=${i}&search=${fn:escapeXml(search)}">${i}</a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="transport-list?page=${currentPage + 1}&search=${fn:escapeXml(search)}">Sau</a>
                        </li>
                    </ul>
                </nav>
            </c:if>

        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
