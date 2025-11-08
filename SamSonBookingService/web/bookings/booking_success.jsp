<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt phòng thành công | SamSon Travel</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap');
        
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
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        @keyframes gradientShift {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }
        
        .success-container {
            max-width: 800px;
            width: 100%;
            position: relative;
            z-index: 1;
        }
        
        .success-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 30px;
            padding: 60px 40px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            text-align: center;
            animation: scaleIn 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
        }
        
        @keyframes scaleIn {
            from {
                opacity: 0;
                transform: scale(0.5) rotate(-10deg);
            }
            to {
                opacity: 1;
                transform: scale(1) rotate(0deg);
            }
        }
        
        .success-icon {
            width: 120px;
            height: 120px;
            margin: 0 auto 30px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 60px;
            color: white;
            animation: bounceIn 1s ease 0.3s both, pulse 2s ease 1.5s infinite;
            box-shadow: 0 10px 40px rgba(102, 126, 234, 0.4);
        }
        
        @keyframes bounceIn {
            0% {
                opacity: 0;
                transform: scale(0.3);
            }
            50% {
                opacity: 1;
                transform: scale(1.1);
            }
            70% {
                transform: scale(0.9);
            }
            100% {
                transform: scale(1);
            }
        }
        
        @keyframes pulse {
            0%, 100% {
                transform: scale(1);
                box-shadow: 0 10px 40px rgba(102, 126, 234, 0.4);
            }
            50% {
                transform: scale(1.05);
                box-shadow: 0 15px 50px rgba(102, 126, 234, 0.6);
            }
        }
        
        .success-title {
            font-size: 2.5rem;
            font-weight: 800;
            color: #667eea;
            margin-bottom: 15px;
            animation: fadeInUp 0.8s ease 0.5s both;
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
        
        .success-message {
            font-size: 1.2rem;
            color: #666;
            margin-bottom: 40px;
            animation: fadeInUp 0.8s ease 0.7s both;
        }
        
        .booking-details {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border-radius: 20px;
            padding: 30px;
            margin: 30px 0;
            animation: fadeInUp 0.8s ease 0.9s both;
        }
        
        .detail-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 0;
            border-bottom: 1px solid rgba(102, 126, 234, 0.1);
        }
        
        .detail-item:last-child {
            border-bottom: none;
        }
        
        .detail-label {
            font-weight: 600;
            color: #666;
            font-size: 0.95rem;
        }
        
        .detail-value {
            font-weight: 700;
            color: #333;
            font-size: 1.1rem;
        }
        
        .confetti {
            position: fixed;
            width: 10px;
            height: 10px;
            background: #667eea;
            position: absolute;
            animation: confettiFall 3s linear infinite;
        }
        
        .confetti:nth-child(1) { left: 10%; animation-delay: 0s; background: #667eea; }
        .confetti:nth-child(2) { left: 20%; animation-delay: 0.5s; background: #764ba2; }
        .confetti:nth-child(3) { left: 30%; animation-delay: 1s; background: #f093fb; }
        .confetti:nth-child(4) { left: 40%; animation-delay: 1.5s; background: #4facfe; }
        .confetti:nth-child(5) { left: 50%; animation-delay: 2s; background: #00f2fe; }
        .confetti:nth-child(6) { left: 60%; animation-delay: 0.3s; background: #667eea; }
        .confetti:nth-child(7) { left: 70%; animation-delay: 0.8s; background: #764ba2; }
        .confetti:nth-child(8) { left: 80%; animation-delay: 1.3s; background: #f093fb; }
        .confetti:nth-child(9) { left: 90%; animation-delay: 1.8s; background: #4facfe; }
        
        @keyframes confettiFall {
            0% {
                transform: translateY(-100vh) rotate(0deg);
                opacity: 1;
            }
            100% {
                transform: translateY(100vh) rotate(720deg);
                opacity: 0;
            }
        }
        
        .action-buttons {
            display: flex;
            gap: 20px;
            justify-content: center;
            margin-top: 40px;
            animation: fadeInUp 0.8s ease 1.1s both;
        }
        
        .btn {
            padding: 15px 40px;
            border: none;
            border-radius: 15px;
            font-size: 1.1rem;
            font-weight: 700;
            font-family: 'Poppins', sans-serif;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
        }
        
        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(102, 126, 234, 0.5);
        }
        
        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }
        
        .btn-secondary:hover {
            background: #667eea;
            color: white;
            transform: translateY(-3px);
        }
        
        @media (max-width: 768px) {
            .success-card {
                padding: 40px 25px;
            }
            
            .success-title {
                font-size: 2rem;
            }
            
            .success-icon {
                width: 100px;
                height: 100px;
                font-size: 50px;
            }
            
            .action-buttons {
                flex-direction: column;
            }
            
            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="confetti"></div>
    <div class="confetti"></div>
    <div class="confetti"></div>
    <div class="confetti"></div>
    <div class="confetti"></div>
    <div class="confetti"></div>
    <div class="confetti"></div>
    <div class="confetti"></div>
    <div class="confetti"></div>
    
    <div class="success-container">
        <div class="success-card">
            <div class="success-icon">✓</div>
            <h1 class="success-title">🎉 Đặt phòng thành công!</h1>
            <p class="success-message">Cảm ơn bạn đã đặt phòng tại SamSon Travel. Chúng tôi đã gửi email xác nhận đến địa chỉ email của bạn.</p>
            
            <c:choose>
                <c:when test="${booking != null}">
                    <div class="booking-details">
                        <div class="detail-item">
                            <span class="detail-label">Mã đặt phòng</span>
                            <span class="detail-value">#${booking.id}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Khách sạn</span>
                            <span class="detail-value">${booking.hotelId}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Loại phòng</span>
                            <span class="detail-value">${booking.roomType}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Số phòng</span>
                            <span class="detail-value">${booking.numberOfRooms}</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Tổng tiền</span>
                            <span class="detail-value"><fmt:formatNumber value="${booking.totalPrice}" pattern="#,###" />₫</span>
                        </div>
                        <div class="detail-item">
                            <span class="detail-label">Trạng thái</span>
                            <span class="detail-value" style="color: #667eea;">${booking.status}</span>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="booking-details">
                        <p style="color: #666; font-size: 1rem;">
                            Đơn đặt phòng của bạn đã được ghi nhận thành công. Vui lòng kiểm tra lịch sử đặt phòng trong trang cá nhân để xem chi tiết.
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
            
            <c:if test="${not empty successMessage}">
                <div style="background: rgba(76, 175, 80, 0.9); color: white; padding: 20px; border-radius: 15px; margin-bottom: 20px; animation: fadeInDown 0.6s ease;">
                    <strong>✅ Thành công:</strong> ${successMessage}
                </div>
            </c:if>
            
            <c:if test="${not empty errorMessage}">
                <div style="background: rgba(255, 107, 107, 0.9); color: white; padding: 20px; border-radius: 15px; margin-bottom: 20px; animation: fadeInDown 0.6s ease;">
                    <strong>⚠️ Lỗi:</strong> ${errorMessage}
                </div>
            </c:if>
            
            <c:if test="${booking != null && booking.status == 'pending'}">
                <form method="post" action="${pageContext.request.contextPath}/payment/confirm" style="margin: 30px 0; animation: fadeInUp 0.8s ease 1.1s both;">
                    <input type="hidden" name="booking_id" value="${booking.id}" />
                    <button type="submit" class="btn btn-primary" style="width: 100%; padding: 20px; font-size: 1.3rem; background: linear-gradient(135deg, #4caf50 0%, #45a049 100%); box-shadow: 0 10px 30px rgba(76, 175, 80, 0.4);">
                        💳 Tôi đã thanh toán
                    </button>
                </form>
            </c:if>
            
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">🏠 Về trang chủ</a>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">📋 Xem đơn đặt</a>
            </div>
        </div>
    </div>
</body>
</html>
