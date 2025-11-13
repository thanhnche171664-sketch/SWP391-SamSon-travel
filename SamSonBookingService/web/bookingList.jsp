<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Booking List - Admin</title>
    <style>
        :root {
            --primary: #2563eb;
            --primary-dark: #1d4ed8;
            --danger: #ef4444;
            --danger-dark: #b91c1c;
            --bg: #f3f4f6;
            --card-bg: #ffffff;
            --border: #e5e7eb;
            --text-main: #111827;
            --text-sub: #6b7280;
        }

        * {
            box-sizing: border-box;
        }

        body {
            font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            margin: 0;
            padding: 24px;
            background: var(--bg);
            color: var(--text-main);
        }

        .page-container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .page-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 16px;
        }
        
        /* CSS cho nhóm nút ở header */
        .header-actions {
            display: flex;
            gap: 10px;
        }

        .page-title {
            font-size: 24px;
            font-weight: 600;
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .page-title span {
            font-size: 13px;
            font-weight: 400;
            color: var(--text-sub);
        }

        .card {
            background: var(--card-bg);
            border-radius: 12px;
            border: 1px solid var(--border);
            box-shadow: 0 10px 25px rgba(15,23,42,0.06);
            padding: 16px 18px 18px;
        }

        /* Filter bar */
        .filter-bar {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            align-items: center;
            margin-bottom: 14px;
        }

        .filter-group {
            display: flex;
            align-items: center;
            gap: 6px;
        }

        .filter-label {
            font-size: 13px;
            color: var(--text-sub);
        }

        .filter-bar input[type="text"],
        .filter-bar select {
            padding: 7px 9px;
            border-radius: 8px;
            border: 1px solid var(--border);
            font-size: 13px;
            min-width: 180px;
            outline: none;
        }

        .filter-bar input[type="text"]:focus,
        .filter-bar select:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 1px rgba(37,99,235,0.2);
        }

        .btn {
            border-radius: 999px;
            padding: 7px 14px;
            font-size: 13px;
            border: none;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            transition: all 0.15s ease-in-out;
        }

        .btn-primary {
            background: var(--primary);
            color: #fff;
        }

        .btn-primary:hover {
            background: var(--primary-dark);
            transform: translateY(-0.5px);
        }

        .btn-ghost {
            background: transparent;
            color: var(--text-sub);
            border: 1px solid var(--border);
        }

        .btn-ghost:hover {
            background: #f9fafb;
        }

        .btn-update {
            background: var(--primary);
            color: #fff;
        }

        .btn-update:hover {
            background: var(--primary-dark);
            transform: translateY(-0.5px);
        }

        .btn-delete {
            background: var(--danger);
            color: #fff;
        }

        .btn-delete:hover {
            background: var(--danger-dark);
        }

        /* Table */
        .table-wrapper {
            overflow-x: auto;
            border-radius: 10px;
            border: 1px solid var(--border);
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
            text-align: left;
            border-bottom: 1px solid var(--border);
            vertical-align: middle;
        }

        th {
            font-weight: 600;
            color: var(--text-sub);
            white-space: nowrap;
        }

        tbody tr:hover {
            background: #f3f4ff;
        }

        .col-code {
            font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
            font-size: 12px;
        }

        .col-money {
            font-weight: 600;
            color: var(--primary-dark);
            white-space: nowrap;
        }

        .status-badge {
            display: inline-flex;
            align-items: center;
            padding: 3px 8px;
            border-radius: 999px;
            font-size: 11px;
            font-weight: 500;
            white-space: nowrap;
        }

        .status-pending {
            background: #fef3c7;
            color: #92400e;
        }

        .status-confirmed {
            background: #dcfce7;
            color: #166534;
        }

        .status-canceled {
            background: #fee2e2;
            color: #b91c1c;
        }

        .small-text {
            font-size: 12px;
            color: var(--text-sub);
        }

        .actions-cell {
            white-space: nowrap;
        }

        .actions-cell form {
            display: inline-block;
            margin: 0 2px;
        }

        .no-data {
            padding: 20px 10px;
            text-align: center;
            color: var(--text-sub);
            font-size: 13px;
        }

        a.btn {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="page-container">
    <div class="page-header">
        <div class="page-title">
            Booking List - Admin
            <span>Quản lý các đơn đặt phòng & dịch vụ của khách</span>
        </div>
        
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/adminDashboard.jsp" 
               class="btn btn-primary">
                🏠 Trang chủ
            </a>
        </div>
        </div>

    <div class="card">
        <form class="filter-bar" method="get"
              action="${pageContext.request.contextPath}/booking_list">
            <div class="filter-group">
                <span class="filter-label">Tìm kiếm:</span>
                <input type="text"
                       name="keyword"
                       placeholder="Mã booking / tên khách / email / khách sạn..."
                       value="${param.keyword}"
                       onkeypress="if(event.key === 'Enter') this.form.submit();"/>
            </div>

            <div class="filter-group">
                <span class="filter-label">Trạng thái:</span>
                <select name="status" onchange="this.form.submit()">
                    <option value="all"
                        ${param.status == null || param.status == 'all' ? 'selected' : ''}>
                        Tất cả trạng thái
                    </option>
                    <option value="pending"
                        ${param.status == 'pending' ? 'selected' : ''}>
                        Chờ duyệt
                    </option>
                    <option value="confirmed"
                        ${param.status == 'confirmed' ? 'selected' : ''}>
                        Đã duyệt
                    </option>
                    <option value="canceled"
                        ${param.status == 'canceled' ? 'selected' : ''}>
                        Đã hủy
                    </option>
                </select>
            </div>
        </form>

        <div class="table-wrapper">
            <c:if test="${empty bookings}">
                <div class="no-data">
                    Hiện chưa có booking nào phù hợp.
                </div>
            </c:if>

            <c:if test="${not empty bookings}">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Mã booking</th>
                        <th>Khách hàng</th>
                        <th>Email</th>
                        <th>Khách sạn</th>
                        <th>Phòng</th>
                        <th>Vận chuyển</th>
                        <th>Tổng tiền</th>
                        <th>Ngày đặt</th>
                        <th>Trạng thái</th>
                        <th style="text-align: center;">Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="b" items="${bookings}">
                        <tr>
                            <td>${b.id}</td>
                            <td class="col-code">${b.bookingCode}</td>
                            <td>
                                <div>${b.customerName}</div>
                                <div class="small-text">
                                    <c:if test="${b.customerName == null}">
                                        (Không rõ)
                                    </c:if>
                                </div>
                            </td>
                            <td>
                                <c:out value="${b.email}" default="-"/>
                            </td>
                            <td>
                                <c:out value="${b.hotelName}" default="(Không rõ)"/>
                            </td>
                            <td>
                                ${b.roomType}
                                <div class="small-text">
                                    Số phòng: ${b.numberOfRooms}
                                </div>
                            </td>
                            <td>
                                <c:out value="${b.transportName}" default="Không sử dụng"/>
                            </td>
                            <td class="col-money">
                                <fmt:formatNumber value="${b.totalPrice}" type="number" groupingUsed="true"/> đ
                            </td>
                            <td>
                                <c:if test="${b.bookingDate != null}">
                                    <fmt:formatDate value="${b.bookingDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </c:if>
                            </td>
                            <td>
                                <c:set var="st" value="${b.status}"/>
                                <span class="status-badge
                                    ${st == 'pending'   ? 'status-pending'  : ''}
                                    ${st == 'confirmed' ? 'status-confirmed' : ''}
                                    ${st == 'canceled'  ? 'status-canceled' : ''}">
                                    <c:choose>
                                        <c:when test="${st == 'pending'}">Chờ duyệt</c:when>
                                        <c:when test="${st == 'confirmed'}">Đã duyệt</c:when>
                                        <c:when test="${st == 'canceled'}">Đã hủy</c:when>
                                        <c:otherwise>${st}</c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td class="actions-cell">
                                <a href="${pageContext.request.contextPath}/booking_list?action=view&id=${b.id}"
                                   class="btn btn-update"
                                   style="margin-bottom: 4px;">
                                    ✔ Cập nhật
                                </a>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/booking_list"
                                      onsubmit="return confirm('Bạn có chắc chắn muốn xóa booking này?');">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="id" value="${b.id}"/>

                                    <button type="submit" class="btn btn-delete">
                                        🗑 Xóa
                                    </button>
                                </form>
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