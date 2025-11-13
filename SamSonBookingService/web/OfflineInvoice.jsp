<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="entity.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hóa đơn - Sam Son Travel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f7fa;
            padding: 20px;
        }
        
        .alert {
            max-width: 800px;
            margin: 0 auto 20px;
            padding: 15px 20px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            gap: 12px;
            font-size: 0.95rem;
        }

        .alert i {
            font-size: 1.2rem;
        }

        .alert-error {
            background: #fdecea;
            color: #b71c1c;
            border: 1px solid #f5c6cb;
        }
        
        .invoice-container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 40px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            border-radius: 8px;
        }
        
        .invoice-header {
            text-align: center;
            border-bottom: 3px solid #667eea;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        
        .invoice-header h1 {
            color: #667eea;
            font-size: 2rem;
            margin-bottom: 10px;
        }
        
        .invoice-header p {
            color: #666;
            font-size: 0.9rem;
        }
        
        .invoice-info {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }
        
        .info-section h3 {
            color: #333;
            font-size: 1rem;
            margin-bottom: 10px;
            border-bottom: 2px solid #eee;
            padding-bottom: 5px;
        }
        
        .info-section p {
            color: #666;
            margin: 5px 0;
            font-size: 0.9rem;
        }
        
        .customer-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #667eea;
        }
        
        .customer-info h4 {
            color: #667eea;
            margin-bottom: 10px;
        }
        
        .invoice-details {
            margin: 30px 0;
        }
        
        .invoice-details h3 {
            color: #333;
            margin-bottom: 15px;
            font-size: 1.2rem;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        
        table th {
            background: #667eea;
            color: white;
            padding: 12px;
            text-align: left;
            font-weight: 600;
        }
        
        table td {
            padding: 12px;
            border-bottom: 1px solid #eee;
        }
        
        table tr:last-child td {
            border-bottom: none;
        }
        
        .text-right {
            text-align: right;
        }
        
        .total-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-top: 20px;
        }
        
        .total-row {
            display: flex;
            justify-content: space-between;
            margin: 10px 0;
            font-size: 1.1rem;
        }
        
        .total-row.grand-total {
            font-size: 1.5rem;
            font-weight: bold;
            color: #667eea;
            border-top: 2px solid #667eea;
            padding-top: 15px;
            margin-top: 15px;
        }
        
        .payment-info {
            background: #e8f5e8;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
            border-left: 4px solid #4CAF50;
        }
        
        .payment-info h4 {
            color: #4CAF50;
            margin-bottom: 10px;
        }
        
        .invoice-footer {
            text-align: center;
            margin-top: 40px;
            padding-top: 20px;
            border-top: 2px solid #eee;
            color: #666;
            font-size: 0.9rem;
        }
        
        .print-section {
            text-align: center;
            margin-top: 30px;
        }
        
        .btn-print {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1rem;
            font-weight: bold;
            transition: all 0.3s ease;
        }
        
        .btn-print:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-back {
            background: #6c757d;
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1rem;
            margin-right: 10px;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .btn-back:hover {
            background: #5a6268;
        }
        
        @media print {
            body {
                background: white;
                padding: 0;
            }
            
            .print-section {
                display: none;
            }
            
            .invoice-container {
                box-shadow: none;
            }
        }
    </style>
</head>
<body>
            <c:if test="${saveSuccess == false}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-triangle"></i>
                    <span>Không thể lưu đơn đặt phòng vào hệ thống. Vui lòng kiểm tra kết nối cơ sở dữ liệu hoặc thử lại.</span>
                    <c:if test="${not empty saveErrorMessage}">
                        <br>
                        <small>Chi tiết: ${saveErrorMessage}</small>
                    </c:if>
                </div>
            </c:if>
            <div class="invoice-container">
        <!-- Header -->
        <div class="invoice-header">
            <h1><i class="fas fa-receipt"></i> HÓA ĐƠN OFFLINE</h1>
            <p>Sam Son Travel - Dịch vụ du lịch & khách sạn</p>
            <p>Mã hóa đơn: #${booking.id != null ? booking.id : 'TMP'}</p>
        </div>
        
        <!-- Customer Info -->
        <c:if test="${not empty customer}">
            <div class="customer-info">
                <h4><i class="fas fa-user"></i> Thông tin khách hàng</h4>
                <p><strong>Họ và tên:</strong> ${customer.fullName}</p>
                <p><strong>Số điện thoại:</strong> ${customer.phone}</p>
                <c:if test="${not empty customer.email}">
                    <p><strong>Email:</strong> ${customer.email}</p>
                </c:if>
                <c:if test="${not empty customer.idCardNumber}">
                    <p><strong>CMND/CCCD:</strong> ${customer.idCardNumber}</p>
                </c:if>
            </div>
        </c:if>
        
        <!-- Invoice Info -->
        <div class="invoice-info">
            <div class="info-section">
                <h3><i class="fas fa-info-circle"></i> Thông tin booking</h3>
                <p><strong>Mã booking:</strong> #${booking.id}</p>
                <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${booking.bookingDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                <p><strong>Nguồn:</strong> Đặt tại quầy</p>
                <p><strong>Trạng thái:</strong> 
                    <span style="color: ${booking.status == 'confirmed' ? '#4CAF50' : '#ff9800'}; font-weight: bold;">
                        ${booking.status == 'confirmed' ? 'Đã xác nhận' : booking.status}
                    </span>
                </p>
            </div>
            
            <div class="info-section">
                <h3><i class="fas fa-money-bill-wave"></i> Thông tin thanh toán</h3>
                <c:if test="${not empty payment}">
                    <p><strong>Mã giao dịch:</strong> ${payment.transactionId}</p>
                    <p><strong>Phương thức:</strong> 
                        ${payment.paymentMethod == 'CASH' ? 'Tiền mặt' : 
                          payment.paymentMethod == 'BANK_TRANSFER' ? 'Chuyển khoản' : 'Thẻ tín dụng'}
                    </p>
                    <p><strong>Ngày thanh toán:</strong> <fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                    <p><strong>Trạng thái:</strong> 
                        <span style="color: #4CAF50; font-weight: bold;">
                            ${payment.status == 'PAID' ? 'Đã thanh toán' : payment.status}
                        </span>
                    </p>
                </c:if>
            </div>
        </div>
        
        <!-- Room Info (nếu có) -->
        <c:if test="${not empty booking.roomType && booking.numberOfRooms > 0}">
            <div class="invoice-details">
                <h3><i class="fas fa-bed"></i> Thông tin phòng</h3>
                <table>
                    <tr>
                        <th>Loại phòng</th>
                        <th>Số lượng</th>
                        <th class="text-right">Giá</th>
                    </tr>
                    <tr>
                        <td>
                            ${booking.roomType == 'single' ? 'Phòng đơn' : 
                              booking.roomType == 'double' ? 'Phòng đôi' : 'Phòng tập thể'}
                        </td>
                        <td>${booking.numberOfRooms} phòng</td>
                        <td class="text-right">
                            <fmt:formatNumber value="${roomTotalPrice != null ? roomTotalPrice : booking.totalPrice}" type="currency" currencyCode="VND"/>
                        </td>
                    </tr>
                </table>
            </div>
        </c:if>
        
        <!-- Service Details -->
        <c:if test="${not empty serviceItems}">
            <div class="invoice-details">
                <h3><i class="fas fa-concierge-bell"></i> Chi tiết dịch vụ</h3>
                <table>
                    <tr>
                        <th>Tên dịch vụ</th>
                        <th>Số lượng</th>
                        <th class="text-right">Đơn giá</th>
                        <th class="text-right">Thành tiền</th>
                    </tr>
                    <c:forEach var="item" items="${serviceItems}">
                        <tr>
                            <td>
                                <strong>${item.name}</strong>
                                <c:if test="${not empty item.description}">
                                    <br><small style="color: #666;">${item.description}</small>
                                </c:if>
                            </td>
                            <td>${item.quantity}</td>
                            <td class="text-right">
                                <c:if test="${not empty item.pricePerUnit}">
                                    <fmt:formatNumber value="${item.pricePerUnit}" type="currency" currencyCode="VND"/>
                                </c:if>
                            </td>
                            <td class="text-right">
                                <fmt:formatNumber value="${item.price}" type="currency" currencyCode="VND"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </c:if>
        
        <!-- Total -->
        <div class="total-section">
            <c:if test="${roomTotalPrice != null && roomTotalPrice > 0}">
                <div class="total-row">
                    <span>Tổng tiền phòng:</span>
                    <span><fmt:formatNumber value="${roomTotalPrice}" type="currency" currencyCode="VND"/></span>
                </div>
            </c:if>
            <c:if test="${not empty serviceItems && serviceItems.size() > 0}">
                <div class="total-row">
                    <span>Tổng tiền dịch vụ:</span>
                    <span>
                        <fmt:formatNumber value="${booking.totalPrice - (roomTotalPrice != null ? roomTotalPrice : 0)}" type="currency" currencyCode="VND"/>
                    </span>
                </div>
            </c:if>
            <div class="total-row grand-total">
                <span>TỔNG CỘNG:</span>
                <span><fmt:formatNumber value="${booking.totalPrice}" type="currency" currencyCode="VND"/></span>
            </div>
        </div>
        
        <!-- Payment Info -->
        <c:if test="${not empty payment}">
            <div class="payment-info">
                <h4><i class="fas fa-check-circle"></i> Đã thanh toán</h4>
                <p>Số tiền: <strong><fmt:formatNumber value="${payment.amount}" type="currency" currencyCode="VND"/></strong></p>
                <p>Phương thức: <strong>${payment.paymentMethod == 'CASH' ? 'Tiền mặt' : payment.paymentMethod}</strong></p>
            </div>
        </c:if>
        
        <!-- Footer -->
        <div class="invoice-footer">
            <p>Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi!</p>
            <p><i class="fas fa-phone"></i> Hotline: 1900-xxxx | <i class="fas fa-envelope"></i> Email: contact@samsontravel.com</p>
        </div>
        
        <!-- Print Section -->
        <div class="print-section">
            <a href="${pageContext.request.contextPath}/clear-cart" class="btn-back">
                <i class="fas fa-home"></i> Về trang chủ
            </a>
            <button class="btn-print" onclick="window.print()">
                <i class="fas fa-print"></i> In hóa đơn
            </button>
        </div>
    </div>
    
    <script>
        // Xóa giỏ hàng khỏi sessionStorage ngay khi vào trang hóa đơn
        // (vì booking đã được lưu vào database)
        if (typeof(Storage) !== "undefined") {
            sessionStorage.removeItem('roomCart');
            sessionStorage.removeItem('serviceCart');
            sessionStorage.removeItem('checkinDate');
            sessionStorage.removeItem('checkoutDate');
            // Đánh dấu booking đã hoàn thành
            sessionStorage.setItem('booking_completed', 'true');
            console.log('Đã xóa giỏ hàng khỏi sessionStorage và đánh dấu booking hoàn thành');
        }
    </script>
</body>
</html>

