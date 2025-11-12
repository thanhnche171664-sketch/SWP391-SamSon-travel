<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Duyệt thanh toán | Admin - SamSon Travel</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800;900&display=swap');
        
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 25%, #f093fb 50%, #4facfe 75%, #00f2fe 100%);
            background-size: 400% 400%;
            animation: gradientShift 15s ease infinite;
            min-height: 100vh;
            padding: 20px;
            position: relative;
            overflow-x: hidden;
        }
        
        @keyframes gradientShift {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }
        
        .container {
            max-width: 1400px;
            margin: 0 auto;
            position: relative;
            z-index: 1;
        }
        
        .header {
            text-align: center;
            margin-bottom: 40px;
            animation: fadeInDown 0.8s ease;
        }
        
        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .header h1 {
            font-size: 3.5rem;
            font-weight: 900;
            color: #fff;
            text-shadow: 2px 2px 20px rgba(0,0,0,0.3);
            margin-bottom: 10px;
            background: linear-gradient(45deg, #fff, #f0f0f0);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            letter-spacing: -1px;
        }
        
        .header p {
            font-size: 1.3rem;
            color: rgba(255,255,255,0.9);
            font-weight: 300;
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
            animation: fadeInUp 0.8s ease 0.2s both;
        }
        
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .stat-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 20px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            text-align: center;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }
        
        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
            transition: left 0.5s;
        }
        
        .stat-card:hover::before {
            left: 100%;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 50px rgba(0,0,0,0.3);
        }
        
        .stat-number {
            font-size: 3rem;
            font-weight: 900;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 10px;
        }
        
        .stat-label {
            font-size: 1.1rem;
            color: #666;
            font-weight: 600;
        }
        
        .payments-container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 30px;
            padding: 40px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            animation: fadeInUp 0.8s ease 0.4s both;
        }
        
        .payments-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 3px solid #667eea;
        }
        
        .payments-header h2 {
            font-size: 2rem;
            font-weight: 800;
            color: #667eea;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .payment-card {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border-radius: 20px;
            padding: 30px;
            margin-bottom: 25px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative;
            overflow: hidden;
            animation: slideInLeft 0.6s ease both;
        }
        
        @keyframes slideInLeft {
            from {
                opacity: 0;
                transform: translateX(-50px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }
        
        .payment-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 5px;
            height: 100%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .payment-card:hover {
            transform: translateY(-5px) scale(1.02);
            box-shadow: 0 15px 40px rgba(102, 126, 234, 0.3);
        }
        
        .payment-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 20px;
        }
        
        .payment-info {
            flex: 1;
        }
        
        .payment-code {
            font-size: 1.5rem;
            font-weight: 800;
            color: #667eea;
            margin-bottom: 5px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .payment-status {
            display: inline-block;
            padding: 8px 20px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 700;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #333;
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
            animation: pulse 2s ease infinite;
        }
        
        @keyframes pulse {
            0%, 100% {
                transform: scale(1);
                box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
            }
            50% {
                transform: scale(1.05);
                box-shadow: 0 8px 25px rgba(255, 215, 0, 0.6);
            }
        }
        
        .payment-amount {
            font-size: 2.5rem;
            font-weight: 900;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            text-align: right;
        }
        
        .payment-details {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin: 25px 0;
            padding: 25px;
            background: rgba(255, 255, 255, 0.7);
            border-radius: 15px;
        }
        
        .detail-item {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }
        
        .detail-label {
            font-size: 0.85rem;
            color: #666;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        .detail-value {
            font-size: 1.1rem;
            color: #333;
            font-weight: 700;
        }
        
        .payment-actions {
            display: flex;
            gap: 15px;
            margin-top: 25px;
        }
        
        .btn {
            flex: 1;
            padding: 15px 30px;
            border: none;
            border-radius: 15px;
            font-size: 1.1rem;
            font-weight: 700;
            font-family: 'Poppins', sans-serif;
            cursor: pointer;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        .btn::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.3);
            transform: translate(-50%, -50%);
            transition: width 0.6s, height 0.6s;
        }
        
        .btn:hover::before {
            width: 300px;
            height: 300px;
        }
        
        .btn-approve {
            background: linear-gradient(135deg, #4caf50 0%, #45a049 100%);
            color: white;
            box-shadow: 0 10px 30px rgba(76, 175, 80, 0.4);
        }
        
        .btn-approve:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(76, 175, 80, 0.5);
        }
        
        .btn-reject {
            background: linear-gradient(135deg, #f44336 0%, #d32f2f 100%);
            color: white;
            box-shadow: 0 10px 30px rgba(244, 67, 54, 0.4);
        }
        
        .btn-reject:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(244, 67, 54, 0.5);
        }
        
        .empty-state {
            text-align: center;
            padding: 80px 20px;
            animation: fadeInUp 0.8s ease;
        }
        
        .empty-icon {
            font-size: 6rem;
            margin-bottom: 20px;
            opacity: 0.5;
        }
        
        .empty-text {
            font-size: 1.5rem;
            color: #666;
            font-weight: 600;
        }
        
        .alert {
            padding: 20px 30px;
            border-radius: 15px;
            margin-bottom: 30px;
            animation: fadeInDown 0.6s ease;
            font-weight: 600;
        }
        
        .alert-success {
            background: rgba(76, 175, 80, 0.9);
            color: white;
            box-shadow: 0 10px 30px rgba(76, 175, 80, 0.3);
        }
        
        .alert-error {
            background: rgba(244, 67, 54, 0.9);
            color: white;
            box-shadow: 0 10px 30px rgba(244, 67, 54, 0.3);
        }
        
        @media (max-width: 768px) {
            .header h1 {
                font-size: 2.5rem;
            }
            
            .payment-header {
                flex-direction: column;
                gap: 15px;
            }
            
            .payment-amount {
                text-align: left;
            }
            
            .payment-actions {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>💳 Duyệt Thanh Toán</h1>
            <p>Quản lý và duyệt các khoản thanh toán đang chờ</p>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-number">${fn:length(pendingPayments)}</div>
                <div class="stat-label">Chờ duyệt</div>
            </div>
        </div>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ✅ ${successMessage}
            </div>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                ⚠️ ${errorMessage}
            </div>
        </c:if>
        
        <div class="payments-container">
            <div class="payments-header">
                <h2>📋 Danh sách thanh toán chờ duyệt</h2>
            </div>
            
            <c:choose>
                <c:when test="${empty pendingPayments || fn:length(pendingPayments) == 0}">
                    <div class="empty-state">
                        <div class="empty-icon">✨</div>
                        <div class="empty-text">Không có thanh toán nào đang chờ duyệt</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pwb" items="${pendingPayments}" varStatus="loop">
                        <div class="payment-card" style="animation-delay: ${loop.index * 0.1}s">
                            <div class="payment-header">
                                <div class="payment-info">
                                    <div class="payment-code">
                                        🎫 ${pwb.bookingCode}
                                    </div>
                                    <span class="payment-status">⏳ Đang chờ duyệt</span>
                                </div>
                                <div class="payment-amount">
                                    <fmt:formatNumber value="${pwb.totalPrice}" pattern="#,###" />₫
                                </div>
                            </div>
                            
                            <div class="payment-details">
                                <div class="detail-item">
                                    <span class="detail-label">Khách hàng</span>
                                    <span class="detail-value">${pwb.userName}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Email</span>
                                    <span class="detail-value">${pwb.userEmail}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Khách sạn</span>
                                    <span class="detail-value">${pwb.hotelName}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Nhận phòng</span>
                                    <span class="detail-value">
                                        <fmt:formatDate value="${pwb.checkInDate}" pattern="dd/MM/yyyy" />
                                    </span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Trả phòng</span>
                                    <span class="detail-value">
                                        <fmt:formatDate value="${pwb.checkOutDate}" pattern="dd/MM/yyyy" />
                                    </span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">Ngày thanh toán</span>
                                    <span class="detail-value">
                                        <fmt:formatDate value="${pwb.payment.paymentDate}" pattern="dd/MM/yyyy HH:mm" />
                                    </span>
                                </div>
                            </div>
                            
                            <form method="post" action="${pageContext.request.contextPath}/admin/payments/approve" style="margin-top: 20px;">
                                <input type="hidden" name="payment_id" value="${pwb.payment.paymentId}" />
                                <input type="hidden" name="booking_id" value="${pwb.payment.bookingId}" />
                                <div class="payment-actions">
                                    <button type="submit" name="action" value="approve" class="btn btn-approve">
                                        ✅ Duyệt thanh toán
                                    </button>
                                    <button type="submit" name="action" value="reject" class="btn btn-reject">
                                        ❌ Từ chối
                                    </button>
                                </div>
                            </form>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>

