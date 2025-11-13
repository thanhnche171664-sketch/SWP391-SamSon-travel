<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hóa đơn dịch vụ - Sam Son Travel</title>
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
    <div class="invoice-container">
        <!-- Header -->
        <div class="invoice-header">
            <h1><i class="fas fa-receipt"></i> HÓA ĐƠN DỊCH VỤ</h1>
            <p>Sam Son Travel - Dịch vụ du lịch & khách sạn</p>
            <p>Mã hóa đơn: #${booking.id}</p>
        </div>
        
        <!-- Invoice Info -->
        <div class="invoice-info">
            <div class="info-section">
                <h3><i class="fas fa-info-circle"></i> Thông tin booking</h3>
                <p><strong>Mã booking:</strong> #${booking.id}</p>
                <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${booking.bookingDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                <p><strong>Nguồn:</strong> Đặt tại quầy</p>
                <p><strong>Trạng thái:</strong> 
                    <span style="color: #4CAF50; font-weight: bold;">Đã xác nhận</span>
                </p>
            </div>
            
            <div class="info-section">
                <h3><i class="fas fa-money-bill-wave"></i> Thông tin thanh toán</h3>
                <c:if test="${not empty payment}">
                    <p><strong>Mã giao dịch:</strong> ${payment.transactionId}</p>
                    <p><strong>Phương thức:</strong> Tiền mặt</p>
                    <p><strong>Ngày thanh toán:</strong> <fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                    <p><strong>Trạng thái:</strong> 
                        <span style="color: #4CAF50; font-weight: bold;">Đã thanh toán</span>
                    </p>
                </c:if>
            </div>
        </div>
        
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
                                    <br><small style="color: #666;">
                                        <c:if test="${item.description == 'meal'}">
                                            <i class="fas fa-utensils"></i> Ăn uống
                                        </c:if>
                                        <c:if test="${item.description == 'wellness'}">
                                            <i class="fas fa-spa"></i> Spa & Wellness
                                        </c:if>
                                    </small>
                                </c:if>
                            </td>
                            <td>${item.quantity}</td>
                            <td class="text-right">
                                <fmt:formatNumber value="${item.pricePerUnit}" type="currency" currencyCode="VND"/>
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
                <p>Phương thức: <strong>Tiền mặt</strong></p>
            </div>
        </c:if>
        
        <!-- Footer -->
        <div class="invoice-footer">
            <p>Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi!</p>
            <p><i class="fas fa-phone"></i> Hotline: 1900-xxxx | <i class="fas fa-envelope"></i> Email: contact@samsontravel.com</p>
        </div>
        
        <!-- Print Section -->
        <div class="print-section">
            <a href="${pageContext.request.contextPath}/offline-home" class="btn-back">
                <i class="fas fa-home"></i> Về trang chủ
            </a>
            <a href="${pageContext.request.contextPath}/service-list" class="btn-back">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
            <button class="btn-print" onclick="window.print()">
                <i class="fas fa-print"></i> In hóa đơn
            </button>
        </div>
    </div>
</body>
</html>







