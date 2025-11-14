<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Booking Detail - Admin</title>
    <style>
        :root {
            --primary: #2563eb;
            --primary-dark: #1d4ed8;
            --danger: #ef4444;
            --bg: #f3f4f6;
            --card-bg: #ffffff;
            --border: #e5e7eb;
            --text-main: #111827;
            --text-sub: #6b7280;
        }

        * { box-sizing: border-box; }

        body {
            font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            margin: 0;
            padding: 24px;
            background: radial-gradient(circle at top, #e0ecff, #f3f4f6);
            color: var(--text-main);
        }

        .page-container {
            max-width: 1100px;
            margin: 0 auto;
        }

        a {
            text-decoration: none;
            color: inherit;
        }

        .breadcrumb {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 13px;
            color: var(--text-sub);
            margin-bottom: 10px;
        }

        .breadcrumb a {
            color: var(--primary-dark);
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            margin-bottom: 16px;
        }

        .page-title {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .page-title h1 {
            margin: 0;
            font-size: 24px;
            font-weight: 600;
        }

        .page-title span {
            font-size: 13px;
            color: var(--text-sub);
        }

        .badge-code {
            font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono","Courier New",monospace;
            font-size: 12px;
            padding: 4px 8px;
            border-radius: 999px;
            background: #eef2ff;
            color: #1d4ed8;
        }

        .layout {
            display: grid;
            grid-template-columns: 2fr 1.5fr;
            gap: 16px;
        }

        .card {
            background: var(--card-bg);
            border-radius: 14px;
            border: 1px solid rgba(148,163,184,0.35);
            box-shadow: 0 18px 40px rgba(15,23,42,0.12);
            padding: 16px 18px 18px;
        }

        .card h2 {
            margin: 0 0 10px 0;
            font-size: 16px;
            font-weight: 600;
        }

        .sub-text {
            font-size: 12px;
            color: var(--text-sub);
        }

        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px 16px;
            margin-top: 10px;
        }

        .info-item {
            display: flex;
            flex-direction: column;
            gap: 3px;
        }

        .info-label {
            font-size: 12px;
            color: var(--text-sub);
        }

        .info-value {
            font-size: 13px;
            font-weight: 500;
        }

        .info-value.muted {
            font-weight: 400;
            color: var(--text-sub);
        }

        .pill {
            display: inline-flex;
            align-items: center;
            padding: 3px 10px;
            border-radius: 999px;
            font-size: 12px;
            border: 1px solid var(--border);
            background: #f9fafb;
            gap: 6px;
        }

        .status-badge {
            display: inline-flex;
            align-items: center;
            padding: 3px 9px;
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

        .divider {
            height: 1px;
            background: linear-gradient(to right, transparent, #e5e7eb, transparent);
            margin: 10px 0;
        }

        .notes-box {
            margin-top: 8px;
            padding: 10px;
            border-radius: 10px;
            background: #f9fafb;
            border: 1px dashed #e5e7eb;
            font-size: 13px;
        }

        .btn {
            border-radius: 999px;
            padding: 7px 16px;
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

        .status-select {
            font-size: 13px;
            padding: 6px 10px;
            border-radius: 999px;
            border: 1px solid var(--border);
            outline: none;
        }

        .status-row {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-top: 8px;
        }

        .section-footer {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin-top: 12px;
        }

        @media (max-width: 900px) {
            .layout {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<div class="page-container">

    <!-- Breadcrumb -->
    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/booking_list">&larr; Quay lại danh sách</a>
        <span>/</span>
        <span>Chi tiết booking</span>
    </div>

    <!-- Header -->
    <div class="page-header">
        <div class="page-title">
            <h1>Chi tiết Booking</h1>
            <span>Xem thông tin đầy đủ và cập nhật trạng thái đơn đặt phòng.</span>
        </div>
        <div>
            <span class="badge-code">
                Mã booking: ${booking.bookingCode}
            </span>
        </div>
    </div>

    <div class="layout">
        <!-- CARD 1: Thông tin booking tổng quan -->
        <div class="card">
            <h2>Thông tin đặt phòng</h2>
            <div class="sub-text">
                Tóm tắt chi tiết thời gian, loại phòng và số lượng khách.
            </div>

            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">Trạng thái hiện tại</span>
                    <span class="info-value">
                        <c:set var="st" value="${booking.status}"/>
                        <span class="status-badge
                            ${st == 'pending'   ? 'status-pending'   : ''}
                            ${st == 'confirmed' ? 'status-confirmed' : ''}
                            ${st == 'canceled'  ? 'status-canceled'  : ''}">
                            <c:choose>
                                <c:when test="${st == 'pending'}">Chờ duyệt</c:when>
                                <c:when test="${st == 'confirmed'}">Đã duyệt</c:when>
                                <c:when test="${st == 'canceled'}">Đã hủy</c:when>
                                <c:otherwise>${st}</c:otherwise>
                            </c:choose>
                        </span>
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Nguồn booking</span>
                    <span class="info-value muted">
                        <c:choose>
                            <c:when test="${booking.bookingSource == 'OFFLINE'}">
                                Tạo bởi lễ tân (OFFLINE)
                            </c:when>
                            <c:otherwise>
                                Đặt qua website (ONLINE)
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Ngày đặt</span>
                    <span class="info-value">
                        <c:if test="${booking.bookingDate != null}">
                            <fmt:formatDate value="${booking.bookingDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </c:if>
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Khoảng thời gian lưu trú</span>
                    <span class="info-value">
                        <fmt:formatDate value="${booking.checkInDate}" pattern="dd/MM/yyyy"/>
                        &nbsp;&rarr;&nbsp;
                        <fmt:formatDate value="${booking.checkOutDate}" pattern="dd/MM/yyyy"/>
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Loại phòng</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${booking.roomType == 'single'}">Single</c:when>
                            <c:when test="${booking.roomType == 'double'}">Double</c:when>
                            <c:when test="${booking.roomType == 'dormitory'}">Dormitory</c:when>
                            <c:otherwise>${booking.roomType}</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Số phòng</span>
                    <span class="info-value">
                        ${booking.numberOfRooms}
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Số khách</span>
                    <span class="info-value">
                        ${booking.numAdults} người lớn
                        <c:if test="${booking.numChildren > 0}">
                            , ${booking.numChildren} trẻ em
                        </c:if>
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Tổng tiền</span>
                    <span class="info-value">
                        <fmt:formatNumber value="${booking.totalPrice}" type="number" groupingUsed="true"/> đ
                    </span>
                </div>

                <div class="info-item">
                    <span class="info-label">Phí vận chuyển</span>
                    <span class="info-value muted">
                        <fmt:formatNumber value="${booking.transportFee}" type="number" groupingUsed="true"/> đ
                    </span>
                </div>
            </div>

            <div class="divider"></div>

            <div class="info-item">
                <span class="info-label">Ghi chú từ khách</span>
                <div class="notes-box">
                    <c:if test="${empty booking.notes}">
                        <span class="sub-text">Không có ghi chú thêm.</span>
                    </c:if>
                    <c:if test="${not empty booking.notes}">
                        ${booking.notes}
                    </c:if>
                </div>
            </div>

            <!-- Form cập nhật trạng thái (chỉ field này sửa được) -->
            <form method="post"
                  action="${pageContext.request.contextPath}/booking_list">
                <input type="hidden" name="action" value="update"/>
                <input type="hidden" name="id" value="${booking.id}"/>
                <input type="hidden" name="redirect" value="detail"/>

                <div class="divider"></div>

                <div class="status-row">
                    <span class="info-label">Cập nhật trạng thái</span>
                    <select name="newStatus" class="status-select">
                        <option value="pending"   ${booking.status == 'pending'   ? 'selected' : ''}>Chờ duyệt</option>
                        <option value="confirmed" ${booking.status == 'confirmed' ? 'selected' : ''}>Đã duyệt</option>
                        <option value="canceled"  ${booking.status == 'canceled'  ? 'selected' : ''}>Đã hủy</option>
                    </select>
                </div>

                <div class="section-footer">
                    <a href="${pageContext.request.contextPath}/booking_list" class="btn btn-ghost">
                        Hủy & quay lại
                    </a>
                    <button type="submit" class="btn btn-primary">
                        ✔ Lưu trạng thái
                    </button>
                </div>
            </form>
        </div>

        <!-- CARD 2: Thông tin khách / khách sạn / vận chuyển -->
        <div class="card">
            <h2>Thông tin chi tiết</h2>

            <!-- Khách hàng -->
            <div class="info-item">
                <span class="info-label">Khách hàng</span>
                <div class="pill">
                    👤 <span>${booking.customerName != null ? booking.customerName : 'Khách lẻ (offline)'}</span>
                </div>
            </div>

            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">Email</span>
                    <span class="info-value muted">
                        <c:out value="${booking.email}" default="-"/>
                    </span>
                </div>
                <div class="info-item">
                    <span class="info-label">Số điện thoại</span>
                    <span class="info-value muted">
                        <c:out value="${booking.phone}" default="-"/>
                    </span>
                </div>
                <div class="info-item">
                    <span class="info-label">Địa chỉ</span>
                    <span class="info-value muted">
                        <c:out value="${booking.customerAddress}" default="-"/>
                    </span>
                </div>
            </div>

            <div class="divider"></div>

            <!-- Khách sạn -->
            <div class="info-item">
                <span class="info-label">Khách sạn</span>
                <div class="pill">
                    🏨 <span><c:out value="${booking.hotelName}" default="(Không rõ khách sạn)"/></span>
                </div>
            </div>

            <div class="info-item" style="margin-top:8px;">
                <span class="info-label">Địa chỉ khách sạn</span>
                <span class="info-value muted">
                    <c:out value="${booking.hotelAddress}" default="-"/>
                </span>
            </div>

            <div class="divider"></div>

            <!-- Vận chuyển -->
            <div class="info-item">
                <span class="info-label">Dịch vụ vận chuyển</span>
                <c:choose>
                    <c:when test="${empty booking.transportName}">
                        <span class="info-value muted">Không sử dụng dịch vụ vận chuyển.</span>
                    </c:when>
                    <c:otherwise>
                        <div class="pill">
                            🚐
                            <span>${booking.transportName}</span>
                        </div>

                        <div class="info-grid" style="margin-top:8px;">
                            <div class="info-item">
                                <span class="info-label">Loại phương tiện</span>
                                <span class="info-value muted">
                                    <c:out value="${booking.transportType}" default="-"/>
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Điểm đón</span>
                                <span class="info-value muted">
                                    <c:out value="${booking.pickupLocation}" default="-"/>
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Giờ khởi hành</span>
                                <span class="info-value muted">
                                    <c:if test="${booking.departureTime != null}">
                                        <fmt:formatDate value="${booking.departureTime}" pattern="dd/MM/yyyy HH:mm"/>
                                    </c:if>
                                </span>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
</body>
</html>
