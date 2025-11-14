<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách Booking Offline - SamSon Travel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/service-list.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .booking-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .booking-table thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .booking-table th,
        .booking-table td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }
        
        .booking-table th {
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }
        
        .booking-table tbody tr:hover {
            background-color: #f8f9ff;
        }
        
        .booking-table tbody tr:last-child td {
            border-bottom: none;
        }
        
        .status-badge {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            text-transform: uppercase;
        }
        
        .status-confirmed {
            background-color: #d4edda;
            color: #155724;
        }
        
        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }
        
        .status-canceled {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .booking-code {
            font-family: 'Courier New', monospace;
            font-weight: 600;
            color: #667eea;
        }
        
        .price {
            font-weight: 600;
            color: #28a745;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        
        .empty-state i {
            font-size: 4rem;
            margin-bottom: 20px;
            color: #ddd;
        }
        
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        
        .page-title {
            font-size: 1.8rem;
            font-weight: 700;
            color: #333;
        }
        
        .btn-refresh {
            padding: 10px 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            transition: transform 0.2s;
        }
        
        .btn-refresh:hover {
            transform: translateY(-2px);
        }
        
        .btn-view-detail {
            padding: 6px 12px;
            background: #667eea;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.85rem;
            transition: all 0.3s;
        }
        
        .btn-view-detail:hover {
            background: #5568d3;
            transform: translateY(-1px);
        }
        
        /* Modal */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
            overflow: auto;
        }
        
        .modal-content {
            background-color: white;
            margin: 5% auto;
            padding: 30px;
            border-radius: 10px;
            width: 80%;
            max-width: 700px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.3);
        }
        
        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 2px solid #eee;
        }
        
        .modal-header h2 {
            margin: 0;
            color: #333;
        }
        
        .close {
            color: #aaa;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
            transition: color 0.3s;
        }
        
        .close:hover {
            color: #000;
        }
        
        .customer-detail {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .detail-item {
            padding: 15px;
            background: #f8f9fa;
            border-radius: 6px;
        }
        
        .detail-item label {
            display: block;
            font-size: 0.85rem;
            color: #666;
            margin-bottom: 5px;
            font-weight: 600;
        }
        
        .detail-item .value {
            font-size: 1rem;
            color: #333;
        }
        
        .detail-item.full-width {
            grid-column: 1 / -1;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-header">
            <h3><i class="fas fa-umbrella-beach"></i> <span>SamSon Travel</span></h3>
        </div>
        <nav class="sidebar-menu">
            <a href="${pageContext.request.contextPath}/offline-home"><i class="fas fa-home"></i><span>Trang chủ</span></a>
            <a href="room-list"><i class="fas fa-hotel"></i><span>Phòng</span></a>
            <a href="service-list"><i class="fas fa-concierge-bell"></i><span>Dịch vụ</span></a>
            <a href="${pageContext.request.contextPath}/offline-booking-list" class="active"><i class="fas fa-list"></i><span>Danh sách Booking</span></a>
            <a href="#"><i class="fas fa-headset"></i><span>Hỗ trợ</span></a>
        </nav>
        <div class="sidebar-footer">
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                <i class="fas fa-sign-out-alt"></i>
                <span>Đăng xuất</span>
            </a>
        </div>
    </aside>

    <!-- Main -->
    <main class="main-content">
        <header>
            <h1>Danh sách Booking Offline</h1>
            <div class="user-info">
                <div class="user-avatar">K</div>
                <span><b>Khanh</b> <small>(Lễ tân)</small></span>
            </div>
        </header>

        <section class="content">
            <div class="page-header">
                <h2 class="page-title"><i class="fas fa-list-alt"></i> Tất cả Booking Offline</h2>
                <button class="btn-refresh" onclick="window.location.reload()">
                    <i class="fas fa-sync-alt"></i> Làm mới
                </button>
            </div>

            <c:choose>
                <c:when test="${hasCustomerInfo == true && not empty bookingHistory}">
                    <!-- Hiển thị với thông tin khách hàng từ VIEW -->
                    <table class="booking-table">
                        <thead>
                   <tr>
                                <th>Mã Booking</th>
                                <th>Khách hàng</th>
                                <th>Khách sạn</th>
                                <th>Check-in</th>
                                <th>Check-out</th>
                       <th>Đặt gì</th>
                                <th>Số người</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Chi tiết</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="history" items="${bookingHistory}">
                                <tr>
                                    <td>
                                        <span class="booking-code">
                                            <c:choose>
                                                <c:when test="${not empty history.booking_code}">${history.booking_code}</c:when>
                                                <c:otherwise>#${history.booking_id}</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td>
                                        <div><strong>${history.customer_name}</strong></div>
                                        <small style="color: #666;">${history.customer_phone}</small>
                                        <c:if test="${not empty history.customer_email}">
                                            <br><small style="color: #999;">${history.customer_email}</small>
                                        </c:if>
                                    </td>
                                    <td>${history.hotel_name}</td>
                                    <td>
                                        <fmt:formatDate value="${history.check_in_date}" pattern="dd/MM/yyyy" />
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${history.check_out_date}" pattern="dd/MM/yyyy" />
                                    </td>
                       <td>
                           <div>
                               <strong>Phòng:</strong>
                               <c:choose>
                                   <c:when test="${history.room_type == 'single'}">Phòng đơn</c:when>
                                   <c:when test="${history.room_type == 'double'}">Phòng đôi</c:when>
                                   <c:when test="${history.room_type == 'dormitory'}">Phòng tập thể</c:when>
                                   <c:otherwise>
                                       <c:out value="${empty history.room_type ? '-' : history.room_type}" />
                                   </c:otherwise>
                               </c:choose>
                               <c:if test="${history.number_of_rooms != null && history.number_of_rooms > 0}">
                                   x ${history.number_of_rooms}
                               </c:if>
                           </div>
                           <c:if test="${not empty history.service_items}">
                               <div style="margin-top: 4px;">
                                   <strong>Dịch vụ:</strong>
                                   <span>${history.service_items}</span>
                               </div>
                           </c:if>
                       </td>
                                    <td>
                                        ${history.num_adults} người lớn
                                        <c:if test="${history.num_children > 0}">
                                            <br><small>${history.num_children} trẻ em</small>
                                        </c:if>
                                    </td>
                                    <td class="price">
                                        <fmt:formatNumber value="${history.total_amount}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${history.payment_status == 'PAID'}">
                                                <span class="status-badge status-confirmed">Đã thanh toán</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge status-pending">${history.payment_status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <button class="btn-view-detail" 
                                            data-booking-id="${history.booking_id}"
                                            data-booking-code="${history.booking_code != null ? history.booking_code : ''}"
                                            data-customer-name="${history.customer_name}"
                                            data-customer-phone="${history.customer_phone != null ? history.customer_phone : ''}"
                                            data-customer-email="${history.customer_email != null ? history.customer_email : ''}"
                                            data-customer-id-card="${history.customer_id_card != null ? history.customer_id_card : ''}"
                                            data-customer-nationality="${history.customer_nationality != null ? history.customer_nationality : ''}"
                                            data-customer-gender="${history.customer_gender != null ? history.customer_gender : ''}"
                                            data-customer-date-of-birth="${history.customer_date_of_birth != null ? history.customer_date_of_birth : ''}"
                                            data-customer-address="${history.customer_address != null ? history.customer_address : ''}"
                                            data-hotel-name="${history.hotel_name}"
                                            data-check-in-date="${history.check_in_date}"
                                            data-check-out-date="${history.check_out_date}"
                                            data-num-adults="${history.num_adults}"
                                            data-num-children="${history.num_children}"
                                            data-total-amount="${history.total_amount}"
                                            data-payment-status="${history.payment_status}"
                                   data-room-type="${history.room_type != null ? history.room_type : ''}"
                                   data-number-of-rooms="${history.number_of_rooms != null ? history.number_of_rooms : 0}"
                                   data-service-items="${history.service_items != null ? history.service_items : ''}"
                                            data-notes="${history.notes != null ? history.notes : ''}"
                                            onclick="showCustomerDetailFromButton(this)">
                                            <i class="fas fa-eye"></i> Xem
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:when test="${hasCustomerInfo == false && not empty bookings}">
                    <!-- Hiển thị từ bảng Bookings (fallback) -->
                    <table class="booking-table">
                        <thead>
                            <tr>
                                <th>Mã Booking</th>
                                <th>Ngày đặt</th>
                                <th>Check-in</th>
                                <th>Check-out</th>
                       <th>Đặt gì</th>
                                <th>Số phòng</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Chi tiết</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="booking" items="${bookings}">
                                <tr>
                                    <td>
                                        <span class="booking-code">
                                            <c:choose>
                                                <c:when test="${not empty booking.bookingCode}">${booking.bookingCode}</c:when>
                                                <c:otherwise>#${booking.id}</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${booking.bookingDate}" pattern="dd/MM/yyyy HH:mm" />
                                    </td>
                                    <td>
                                        <c:if test="${booking.checkInDate != null}">
                                            <fmt:formatDate value="${booking.checkInDate}" pattern="dd/MM/yyyy" />
                                        </c:if>
                                        <c:if test="${booking.checkInDate == null}">
                                            <span style="color: #999;">-</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${booking.checkOutDate != null}">
                                            <fmt:formatDate value="${booking.checkOutDate}" pattern="dd/MM/yyyy" />
                                        </c:if>
                                        <c:if test="${booking.checkOutDate == null}">
                                            <span style="color: #999;">-</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${booking.roomType == 'single'}">Đơn</c:when>
                                            <c:when test="${booking.roomType == 'double'}">Đôi</c:when>
                                            <c:when test="${booking.roomType == 'dormitory'}">Ký túc</c:when>
                                            <c:otherwise>
                                                <span style="color: #999;">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${booking.numberOfRooms}</td>
                                    <td class="price">
                                        <fmt:formatNumber value="${booking.totalPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0" />
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${booking.status == 'confirmed'}">
                                                <span class="status-badge status-confirmed">Đã xác nhận</span>
                                            </c:when>
                                            <c:when test="${booking.status == 'pending'}">
                                                <span class="status-badge status-pending">Chờ xác nhận</span>
                                            </c:when>
                                            <c:when test="${booking.status == 'canceled'}">
                                                <span class="status-badge status-canceled">Đã hủy</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge">${booking.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <button class="btn-view-detail" 
                                            data-booking-id="${booking.id}"
                                            onclick="loadCustomerDetail(${booking.id})">
                                            <i class="fas fa-eye"></i> Xem
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <i class="fas fa-inbox"></i>
                        <h3>Chưa có booking nào</h3>
                        <p>Danh sách booking offline sẽ hiển thị ở đây sau khi bạn tạo booking</p>
                        <a href="${pageContext.request.contextPath}/room-list" style="margin-top: 20px; display: inline-block; padding: 10px 20px; background: #667eea; color: white; text-decoration: none; border-radius: 6px;">
                            <i class="fas fa-plus"></i> Tạo booking mới
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</div>

<!-- Modal hiển thị chi tiết khách hàng -->
<div id="customerDetailModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2><i class="fas fa-user"></i> Chi tiết khách hàng</h2>
            <span class="close" onclick="closeModal()">&times;</span>
        </div>
        <div id="customerDetailContent">
            <!-- Nội dung sẽ được điền bằng JavaScript -->
        </div>
    </div>
</div>

<script>
    // Hàm lấy dữ liệu từ button và hiển thị chi tiết
    function showCustomerDetailFromButton(button) {
        const data = {
            bookingId: parseInt(button.getAttribute('data-booking-id')) || 0,
            bookingCode: button.getAttribute('data-booking-code') || '',
            customerName: button.getAttribute('data-customer-name') || '',
            customerPhone: button.getAttribute('data-customer-phone') || '',
            customerEmail: button.getAttribute('data-customer-email') || '',
            customerIdCard: button.getAttribute('data-customer-id-card') || '',
            customerNationality: button.getAttribute('data-customer-nationality') || '',
            customerGender: button.getAttribute('data-customer-gender') || '',
            customerDateOfBirth: button.getAttribute('data-customer-date-of-birth') || '',
            customerAddress: button.getAttribute('data-customer-address') || '',
            hotelName: button.getAttribute('data-hotel-name') || '',
            checkInDate: button.getAttribute('data-check-in-date') || '',
            checkOutDate: button.getAttribute('data-check-out-date') || '',
            numAdults: parseInt(button.getAttribute('data-num-adults')) || 0,
            numChildren: parseInt(button.getAttribute('data-num-children')) || 0,
            totalAmount: parseFloat(button.getAttribute('data-total-amount')) || 0,
            paymentStatus: button.getAttribute('data-payment-status') || '',
               roomType: button.getAttribute('data-room-type') || '',
               numberOfRooms: parseInt(button.getAttribute('data-number-of-rooms')) || 0,
               serviceItems: button.getAttribute('data-service-items') || '',
            notes: button.getAttribute('data-notes') || ''
        };
        showCustomerDetail(data);
    }
    
    // Hàm escape HTML để tránh XSS
    function escapeHtml(text) {
        if (!text) return '';
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return String(text).replace(/[&<>"']/g, function(m) { return map[m]; });
    }
    
    // Hàm hiển thị chi tiết khách hàng
    function showCustomerDetail(data) {
        const modal = document.getElementById('customerDetailModal');
        const content = document.getElementById('customerDetailContent');
        
        // Format ngày
        function formatDate(dateStr) {
            if (!dateStr) return '-';
            try {
                const date = new Date(dateStr);
                if (isNaN(date.getTime())) return dateStr;
                return date.toLocaleDateString('vi-VN');
            } catch (e) {
                return dateStr;
            }
        }
        
        // Format giới tính
        function formatGender(gender) {
            if (!gender) return '-';
            const genderMap = {
                'male': 'Nam',
                'female': 'Nữ',
                'other': 'Khác'
            };
            return genderMap[gender.toLowerCase()] || gender;
        }
        
        // Format tiền
        function formatCurrency(amount) {
            if (!amount && amount !== 0) return '0 ₫';
            try {
                return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
            } catch (e) {
                return amount + ' ₫';
            }
        }
               
        function formatRoomType(roomType) {
            if (!roomType) return '-';
            const map = {
                'single': 'Phòng đơn',
                'double': 'Phòng đôi',
                'dormitory': 'Phòng tập thể'
            };
            const value = map[roomType.toLowerCase()];
            return value || roomType;
        }
               
        function createServiceList(items) {
            if (!items) return '-';
            return items;
        }
        
        // Tạo HTML cho chi tiết - dùng DOM để an toàn hơn
        const detailDiv = document.createElement('div');
        detailDiv.className = 'customer-detail';
        
        // Helper function để tạo detail item
        function createDetailItem(label, value, icon, fullWidth) {
            const item = document.createElement('div');
            item.className = 'detail-item' + (fullWidth ? ' full-width' : '');
            
            const labelEl = document.createElement('label');
            labelEl.innerHTML = '<i class="fas fa-' + icon + '"></i> ' + label;
            
            const valueEl = document.createElement('div');
            valueEl.className = 'value';
            valueEl.textContent = value || '-';
            
            item.appendChild(labelEl);
            item.appendChild(valueEl);
            return item;
        }
        
        // Thêm các detail items
        detailDiv.appendChild(createDetailItem('Mã Booking', data.bookingCode || '#' + data.bookingId, 'hashtag'));
        detailDiv.appendChild(createDetailItem('Khách sạn', data.hotelName, 'hotel'));
        
        // Họ và tên với <strong>
        const nameItem = document.createElement('div');
        nameItem.className = 'detail-item';
        nameItem.innerHTML = '<label><i class="fas fa-user"></i> Họ và tên</label>' +
            '<div class="value"><strong>' + escapeHtml(data.customerName || '-') + '</strong></div>';
        detailDiv.appendChild(nameItem);
        detailDiv.appendChild(createDetailItem('Số điện thoại', data.customerPhone, 'phone'));
        detailDiv.appendChild(createDetailItem('Email', data.customerEmail, 'envelope'));
        detailDiv.appendChild(createDetailItem('CMND/CCCD', data.customerIdCard, 'id-card'));
        detailDiv.appendChild(createDetailItem('Quốc tịch', data.customerNationality, 'flag'));
        detailDiv.appendChild(createDetailItem('Giới tính', formatGender(data.customerGender), 'venus-mars'));
        detailDiv.appendChild(createDetailItem('Ngày sinh', formatDate(data.customerDateOfBirth), 'birthday-cake'));
        detailDiv.appendChild(createDetailItem('Địa chỉ', data.customerAddress, 'map-marker-alt', true));
        detailDiv.appendChild(createDetailItem('Check-in', formatDate(data.checkInDate), 'calendar-check'));
        detailDiv.appendChild(createDetailItem('Check-out', formatDate(data.checkOutDate), 'calendar-times'));
              
               // Phòng và dịch vụ
               let roomText = formatRoomType(data.roomType);
               if (data.numberOfRooms && data.numberOfRooms > 0) {
                   roomText += ' x ' + data.numberOfRooms;
               }
               detailDiv.appendChild(createDetailItem('Phòng đã đặt', roomText, 'bed'));
               
               if (data.serviceItems) {
                   detailDiv.appendChild(createDetailItem('Dịch vụ đi kèm', createServiceList(data.serviceItems), 'concierge-bell', true));
               }
        
        // Số người
        let peopleText = data.numAdults + ' người lớn';
        if (data.numChildren > 0) {
            peopleText += ', ' + data.numChildren + ' trẻ em';
        }
        detailDiv.appendChild(createDetailItem('Số người', peopleText, 'users'));
        
        // Tổng tiền (cần style đặc biệt)
        const moneyItem = document.createElement('div');
        moneyItem.className = 'detail-item';
        moneyItem.innerHTML = '<label><i class="fas fa-money-bill-wave"></i> Tổng tiền</label>' +
            '<div class="value" style="color: #28a745; font-weight: 600;">' + formatCurrency(data.totalAmount) + '</div>';
        detailDiv.appendChild(moneyItem);
        
        // Trạng thái thanh toán
        const statusItem = document.createElement('div');
        statusItem.className = 'detail-item';
        const statusClass = data.paymentStatus === 'PAID' ? 'status-confirmed' : 'status-pending';
        const statusText = data.paymentStatus === 'PAID' ? 'Đã thanh toán' : data.paymentStatus;
        statusItem.innerHTML = '<label><i class="fas fa-check-circle"></i> Trạng thái thanh toán</label>' +
            '<div class="value">' +
            '<span class="status-badge ' + statusClass + '">' + escapeHtml(statusText) + '</span>' +
            '</div>';
        detailDiv.appendChild(statusItem);
        
        // Ghi chú nếu có
        if (data.notes) {
            detailDiv.appendChild(createDetailItem('Ghi chú', data.notes, 'sticky-note', true));
        }
        
        // Xóa nội dung cũ và thêm mới
        content.innerHTML = '';
        content.appendChild(detailDiv);
        
        modal.style.display = 'block';
    }
    
    // Đóng modal
    function closeModal() {
        document.getElementById('customerDetailModal').style.display = 'none';
    }
    
    // Đóng modal khi click bên ngoài
    window.onclick = function(event) {
        const modal = document.getElementById('customerDetailModal');
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    }
    
    // Hàm load thông tin khách hàng từ server (cho bảng fallback)
    function loadCustomerDetail(bookingId) {
        const modal = document.getElementById('customerDetailModal');
        const content = document.getElementById('customerDetailContent');
        
        // Hiển thị loading
        content.innerHTML = '<div style="text-align: center; padding: 40px;"><i class="fas fa-spinner fa-spin" style="font-size: 2rem; color: #667eea;"></i><p>Đang tải thông tin...</p></div>';
        modal.style.display = 'block';
        
        // Gọi API để lấy thông tin
        fetch('${pageContext.request.contextPath}/api/customer-detail?bookingId=' + bookingId)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Không thể tải thông tin');
                }
                return response.json();
            })
            .then(data => {
                // Chuyển đổi dữ liệu từ server sang format của hàm showCustomerDetail
                const customerData = {
                    bookingId: data.booking_id || 0,
                    bookingCode: data.booking_code || '',
                    customerName: data.customer_name || '',
                    customerPhone: data.customer_phone || '',
                    customerEmail: data.customer_email || '',
                    customerIdCard: data.customer_id_card || '',
                    customerNationality: data.customer_nationality || '',
                    customerGender: data.customer_gender || '',
                    customerDateOfBirth: data.customer_date_of_birth || '',
                    customerAddress: data.customer_address || '',
                    hotelName: data.hotel_name || '',
                    checkInDate: data.check_in_date || '',
                    checkOutDate: data.check_out_date || '',
                    numAdults: data.num_adults || 0,
                    numChildren: data.num_children || 0,
                    totalAmount: data.total_amount || 0,
                    paymentStatus: data.payment_status || '',
                           roomType: data.room_type || '',
                           numberOfRooms: data.number_of_rooms || 0,
                           serviceItems: data.service_items || '',
                    notes: data.notes || ''
                };
                
                // Hiển thị thông tin
                showCustomerDetail(customerData);
            })
            .catch(error => {
                console.error('Lỗi:', error);
                content.innerHTML = '<div style="text-align: center; padding: 40px; color: #dc3545;">' +
                    '<i class="fas fa-exclamation-triangle" style="font-size: 2rem;"></i>' +
                    '<p style="margin-top: 20px;">Không thể tải thông tin khách hàng</p>' +
                    '<p style="color: #666; font-size: 0.9rem;">' + error.message + '</p>' +
                    '<button onclick="closeModal()" style="margin-top: 20px; padding: 10px 20px; background: #667eea; color: white; border: none; border-radius: 6px; cursor: pointer;">Đóng</button>' +
                    '</div>';
            });
    }
</script>
</body>
</html>






