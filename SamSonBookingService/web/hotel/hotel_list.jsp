<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Danh sách Khách sạn - Hotel Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        /* TẮT HOÀN TOÀN MỌI ANIMATION VÀ TRANSITION - ĐẶT ĐẦU TIÊN */
        html, body, *, *::before, *::after {
            animation: none !important;
            transition: none !important;
            -webkit-animation: none !important;
            -webkit-transition: none !important;
            -moz-animation: none !important;
            -moz-transition: none !important;
            -o-animation: none !important;
            -o-transition: none !important;
            transform: none !important;
            -webkit-transform: none !important;
            -moz-transform: none !important;
            -o-transform: none !important;
            will-change: auto !important;
            backface-visibility: visible !important;
            perspective: none !important;
            transform-style: flat !important;
        }
        
        /* Override Bootstrap table styles ngay từ đầu */
        .table, .table-hover, table, .table-striped, .table-bordered {
            --bs-table-hover-bg: transparent !important;
            --bs-table-striped-bg: transparent !important;
            --bs-table-active-bg: transparent !important;
        }
        
        /* ĐẶC BIỆT TẮT CHO TABLE ELEMENTS - CSS MẠNH NHẤT */
        table, table *, table tr, table td, table th, table tbody, table thead,
        .table, .table *, .table tr, .table td, .table th, .table tbody, .table thead,
        .table-hover, .table-hover *, .table-hover tr, .table-hover td, .table-hover th,
        .hotel-table, .hotel-table *, .hotel-table tr, .hotel-table td, .hotel-table th {
            animation: none !important;
            transition: none !important;
            transform: none !important;
            -webkit-animation: none !important;
            -webkit-transition: none !important;
            -webkit-transform: none !important;
            -moz-animation: none !important;
            -moz-transition: none !important;
            -moz-transform: none !important;
            -o-animation: none !important;
            -o-transition: none !important;
            -o-transform: none !important;
            will-change: auto !important;
            backface-visibility: visible !important;
        }
        
        /* TẮT HOÀN TOÀN HOVER EFFECT TRÊN TABLE */
        .table-hover tbody tr:hover,
        .table-hover tbody tr:focus,
        .table-hover tbody tr:active,
        table tbody tr:hover,
        table tbody tr:focus,
        table tbody tr:active,
        .hotel-table tbody tr:hover,
        .hotel-table tbody tr:focus,
        .hotel-table tbody tr:active {
            background-color: white !important;
            background: white !important;
            animation: none !important;
            transition: none !important;
            transform: none !important;
            box-shadow: none !important;
        }
        
        /* TẮT TẤT CẢ PSEUDO-ELEMENTS */
        table tbody tr::before,
        table tbody tr::after,
        table td::before,
        table td::after {
            display: none !important;
            animation: none !important;
            transition: none !important;
        }
        
        html {
            scroll-behavior: auto !important;
        }
        
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .main-content {
            margin-left: 250px;
            padding: 30px;
            min-height: 100vh;
        }
        
        .page-header {
            background: white;
            padding: 25px 30px;
            border-radius: 8px;
            border: 1px solid #e0e0e0;
            margin-bottom: 25px;
        }
        
        .page-header h2 {
            color: #333;
            margin: 0;
            font-weight: 600;
            font-size: 24px;
        }
        
        .stats-row {
            display: flex;
            gap: 20px;
            margin-bottom: 25px;
        }
        
        .stat-card {
            flex: 1;
            background: white;
            padding: 20px;
            border-radius: 8px;
            border: 1px solid #e0e0e0;
            border-left: 4px solid #667eea;
        }
        
        .stat-card h5 {
            color: #666;
            font-size: 14px;
            margin-bottom: 10px;
        }
        
        .stat-card h3 {
            color: #333;
            font-size: 28px;
            font-weight: 700;
            margin: 0;
        }
        
        .card {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            background: white;
        }
        
        .search-box {
            margin-bottom: 20px;
        }
        
        .search-box input {
            border-radius: 4px;
            border: 1px solid #ddd;
            padding: 10px 15px;
        }
        
        .search-box button {
            border-radius: 4px;
            padding: 10px 25px;
        }
        
        .table-container {
            overflow-x: auto;
        }
        
        .hotel-table {
            width: 100%;
            border-collapse: collapse;
            animation: none !important;
            transition: none !important;
        }
        
        .hotel-table thead th {
            background: #667eea !important;
            color: white !important;
            font-weight: 600;
            padding: 12px;
            border: 1px solid #5568d3 !important;
            font-size: 14px;
            text-align: center;
        }
        
        .hotel-table tbody tr {
            background: white !important;
            background-color: white !important;
            border-bottom: 1px solid #e0e0e0;
            animation: none !important;
            transition: none !important;
            transform: none !important;
            will-change: auto !important;
        }
        
        /* TẮT HOÀN TOÀN HOVER EFFECT - KHÔNG BAO GIỜ ĐỔI MÀU */
        .hotel-table tbody tr:hover,
        .hotel-table tbody tr:focus,
        .hotel-table tbody tr:active,
        .hotel-table tbody tr:visited {
            background: white !important;
            background-color: white !important;
            animation: none !important;
            transition: none !important;
            transform: none !important;
            box-shadow: none !important;
            opacity: 1 !important;
        }
        
        .hotel-table tbody td {
            padding: 12px;
            vertical-align: middle;
            border: 1px solid #e0e0e0;
            text-align: center;
            animation: none !important;
            transition: none !important;
            transform: none !important;
            background: white !important;
            background-color: white !important;
        }
        
        .hotel-table tbody td:hover {
            background: white !important;
            background-color: white !important;
        }
        
        /* Override Bootstrap table styles - CSS MẠNH NHẤT */
        .table-container,
        .table-container table,
        .table-container table *,
        .table-container table tr,
        .table-container table td,
        .table-container table th,
        .table-container table tbody,
        .table-container table thead {
            animation: none !important;
            transition: none !important;
            transform: none !important;
            will-change: auto !important;
        }
        
        .hotel-image {
            width: 100px;
            height: 80px;
            object-fit: cover;
            border-radius: 4px;
            border: 1px solid #ddd;
        }
        
        .hotel-name {
            font-weight: 600;
            color: #333;
            text-align: left;
            margin-bottom: 5px;
        }
        
        .hotel-address {
            font-size: 13px;
            color: #666;
            text-align: left;
        }
        
        .btn-action {
            padding: 6px 10px;
            border-radius: 4px;
            font-size: 14px;
            border: none;
            margin: 0 2px;
        }
        
        .btn-detail {
            background-color: #17a2b8;
            color: white;
        }
        
        .btn-detail:hover {
            background-color: #138496;
            color: white;
        }
        
        .btn-edit {
            background-color: #ffc107;
            color: white;
        }
        
        .btn-edit:hover {
            background-color: #e0a800;
            color: white;
        }
        
        .btn-delete {
            background-color: #dc3545;
            color: white;
        }
        
        .btn-delete:hover {
            background-color: #c82333;
            color: white;
        }
        
        .pagination {
            justify-content: center;
            margin-top: 30px;
        }
        
        .pagination .page-link {
            border-radius: 4px;
            margin: 0 2px;
            border: 1px solid #ddd;
            color: #667eea;
        }
        
        .pagination .page-link:hover {
            background-color: #667eea;
            color: white;
        }
        
        .pagination .active .page-link {
            background-color: #667eea;
            border-color: #667eea;
        }
        
        .alert {
            border-radius: 4px;
            border: 1px solid;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
        }
        
        .empty-state i {
            font-size: 80px;
            color: #ddd;
            margin-bottom: 20px;
        }
        
        .empty-state h4 {
            color: #666;
            margin-bottom: 10px;
        }
        
        .empty-state p {
            color: #999;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar -->
    <jsp:include page="/hotel/components/sidebar.jsp" />
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Page Header -->
        <div class="page-header">
            <div class="d-flex justify-content-between align-items-center">
                <h2><i class="bi bi-building"></i> Danh sách Khách sạn</h2>
                <a href="${pageContext.request.contextPath}/hotel/add" class="btn btn-primary">
                    <i class="bi bi-plus-lg"></i> Thêm khách sạn mới
                </a>
            </div>
        </div>
        
        <!-- Stats Row -->
        <div class="stats-row">
            <div class="stat-card">
                <h5><i class="bi bi-building"></i> Tổng số khách sạn</h5>
                <h3>${totalRecords != null ? totalRecords : 0}</h3>
            </div>
            <div class="stat-card" style="border-left-color: #28a745;">
                <h5><i class="bi bi-check-circle"></i> Đang hoạt động</h5>
                <h3>${totalRecords != null ? totalRecords : 0}</h3>
            </div>
        </div>
        
        <!-- Alerts -->
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible show" role="alert">
                <i class="bi bi-check-circle"></i> ${sessionScope.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="success" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible show" role="alert">
                <i class="bi bi-check-circle"></i> ${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="successMessage" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible show" role="alert">
                <i class="bi bi-exclamation-triangle"></i> ${sessionScope.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="error" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger alert-dismissible show" role="alert" style="white-space: pre-line;">
                <i class="bi bi-exclamation-triangle-fill"></i> 
                <strong>Cảnh báo!</strong><br/>
                ${sessionScope.errorMessage}
            </div>
            <c:remove var="errorMessage" scope="session" />
        </c:if>
        
        <!-- Card -->
        <div class="card">
            <div class="card-body p-4">
                <!-- Search Box -->
                <form method="get" action="${pageContext.request.contextPath}/hotel/list" class="search-box">
                    <div class="row g-3">
                        <div class="col-md-9">
                            <input type="text" name="search" class="form-control" 
                                   placeholder="🔍 Tìm kiếm theo tên hoặc địa chỉ khách sạn..." 
                                   value="${searchKeyword}">
                        </div>
                        <div class="col-md-3">
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="bi bi-search"></i> Tìm kiếm
                            </button>
                        </div>
                    </div>
                </form>
                
                <!-- Table -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${empty hotelList}">
                            <div class="empty-state">
                                <i class="bi bi-inbox"></i>
                                <h4>Chưa có khách sạn nào</h4>
                                <p>Hãy thêm khách sạn đầu tiên của bạn!</p>
                                <a href="${pageContext.request.contextPath}/hotel/add" class="btn btn-primary mt-3">
                                    <i class="bi bi-plus-lg"></i> Thêm khách sạn mới
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="hotel-table" style="animation: none !important; transition: none !important;">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Hình ảnh</th>
                                        <th>Thông tin khách sạn</th>
                                        <th>Mô tả</th>
                                        <th>Ngày tạo</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="hotel" items="${hotelList}">
                                        <tr style="background: white !important; animation: none !important; transition: none !important;">
                                            <td style="text-align: center;">
                                                <strong style="color: #667eea; font-size: 16px;">#${hotel.id}</strong>
                                            </td>
                                            <td>
                                                <img src="${pageContext.request.contextPath}/${hotelImages[hotel.id]}" 
                                                     alt="${hotel.name}" 
                                                     class="hotel-image"
                                                     onerror="this.src='${pageContext.request.contextPath}/uploads/hotels/default.jpg'">
                                            </td>
                                            <td style="text-align: left;">
                                                <div class="hotel-name">${hotel.name}</div>
                                                <div class="hotel-address">
                                                    <i class="bi bi-geo-alt"></i> ${hotel.address}
                                                </div>
                                            </td>
                                            <td style="text-align: left;">
                                                <div style="max-width: 350px; line-height: 1.5;">
                                                    <c:choose>
                                                        <c:when test="${not empty hotel.description}">
                                                            ${hotel.description.length() > 75 ? hotel.description.substring(0, 75).concat('...') : hotel.description}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">Chưa có mô tả</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td>
                                                <span class="text-muted" style="white-space: nowrap;">
                                                    ${hotel.createdAt}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="d-flex gap-2 justify-content-center">
                                                    <a href="${pageContext.request.contextPath}/hotel/detail?id=${hotel.id}" 
                                                       class="btn btn-action btn-detail" 
                                                       title="Chi tiết"
                                                       style="width: 36px; height: 36px; display: flex; align-items: center; justify-content: center;">
                                                        <i class="bi bi-eye"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/hotel/edit?id=${hotel.id}" 
                                                       class="btn btn-action btn-edit" 
                                                       title="Sửa"
                                                       style="width: 36px; height: 36px; display: flex; align-items: center; justify-content: center;">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/hotel/delete?id=${hotel.id}" 
                                                       class="btn btn-action btn-delete" 
                                                       title="Xóa"
                                                       style="width: 36px; height: 36px; display: flex; align-items: center; justify-content: center;"
                                                       onclick="return confirm('Bạn có chắc chắn muốn xóa khách sạn này?')">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            
                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}">
                                <nav>
                                    <ul class="pagination">
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="?page=${currentPage - 1}&search=${searchKeyword}">
                                                <i class="bi bi-chevron-left"></i>
                                            </a>
                                        </li>
                                        
                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="?page=${i}&search=${searchKeyword}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link" href="?page=${currentPage + 1}&search=${searchKeyword}">
                                                <i class="bi bi-chevron-right"></i>
                                            </a>
                                        </li>
                                    </ul>
                                </nav>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    
    <!-- CHỈ LOAD BOOTSTRAP JS CHO ALERT DISMISS, KHÔNG ẢNH HƯỞNG TABLE -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // TẮT HOÀN TOÀN MỌI ANIMATION VÀ TRANSITION BẰNG JAVASCRIPT
        (function() {
            'use strict';
            
            // Tắt CSS transitions và animations ngay lập tức
            var style = document.createElement('style');
            style.id = 'no-animation-style';
            style.textContent = `
                *, *::before, *::after {
                    animation: none !important;
                    transition: none !important;
                    -webkit-animation: none !important;
                    -webkit-transition: none !important;
                    -moz-animation: none !important;
                    -moz-transition: none !important;
                    -o-animation: none !important;
                    -o-transition: none !important;
                    transform: none !important;
                    -webkit-transform: none !important;
                    -moz-transform: none !important;
                    -o-transform: none !important;
                }
                table, table *, table tr, table td, table th, table tbody, table thead {
                    animation: none !important;
                    transition: none !important;
                    transform: none !important;
                }
                .hotel-table tbody tr:hover,
                .hotel-table tbody tr:focus,
                .hotel-table tbody tr:active {
                    background: white !important;
                    background-color: white !important;
                }
            `;
            document.head.insertBefore(style, document.head.firstChild);
            
            // Force table rows luôn giữ màu trắng
            function forceTableWhite() {
                var tableRows = document.querySelectorAll('.hotel-table tbody tr');
                tableRows.forEach(function(row) {
                    // Set inline style để override mọi thứ
                    row.style.setProperty('background', 'white', 'important');
                    row.style.setProperty('background-color', 'white', 'important');
                    row.style.setProperty('animation', 'none', 'important');
                    row.style.setProperty('transition', 'none', 'important');
                    row.style.setProperty('transform', 'none', 'important');
                    
                    // Event listeners để force màu trắng khi hover
                    row.addEventListener('mouseenter', function(e) {
                        e.currentTarget.style.setProperty('background', 'white', 'important');
                        e.currentTarget.style.setProperty('background-color', 'white', 'important');
                    }, true);
                    row.addEventListener('mouseleave', function(e) {
                        e.currentTarget.style.setProperty('background', 'white', 'important');
                        e.currentTarget.style.setProperty('background-color', 'white', 'important');
                    }, true);
                });
            }
            
            // Chạy ngay khi DOM ready
            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', forceTableWhite);
            } else {
                forceTableWhite();
            }
            
            // Chạy lại sau khi page load xong
            window.addEventListener('load', forceTableWhite);
            
            // Chạy lại sau 100ms để đảm bảo
            setTimeout(forceTableWhite, 100);
        })();
    </script>
</body>
</html>
