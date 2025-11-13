<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận đặt phòng | SamSon Travel</title>
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
        
        .breadcrumb {
            margin-bottom: 20px;
            animation: fadeInDown 0.6s ease;
        }
        
        .breadcrumb a {
            color: rgba(255,255,255,0.9);
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .breadcrumb a:hover {
            color: white;
            text-shadow: 0 0 10px rgba(255,255,255,0.5);
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
            font-size: 3rem;
            font-weight: 800;
            color: #fff;
            text-shadow: 2px 2px 20px rgba(0,0,0,0.3);
            margin-bottom: 10px;
            background: linear-gradient(45deg, #fff, #f0f0f0);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        
        .layout {
            display: grid;
            grid-template-columns: 1.5fr 1fr;
            gap: 30px;
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
        
        .card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 30px;
            padding: 40px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            animation: slideInLeft 0.6s ease both;
        }
        
        .card:last-child {
            animation: slideInRight 0.6s ease both;
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
        
        @keyframes slideInRight {
            from {
                opacity: 0;
                transform: translateX(50px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }
        
        .card-title {
            font-size: 1.8rem;
            font-weight: 700;
            color: #667eea;
            margin-bottom: 25px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .card-title::before {
            content: '';
            width: 4px;
            height: 30px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            border-radius: 2px;
        }
        
        .info-item {
            padding: 15px 0;
            border-bottom: 1px solid #f0f0f0;
            display: flex;
            justify-content: space-between;
            align-items: center;
            animation: fadeIn 0.5s ease both;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        
        .info-item:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            color: #666;
            font-size: 0.95rem;
        }
        
        .info-value {
            font-weight: 700;
            color: #333;
            font-size: 1.1rem;
        }
        
        .section-divider {
            margin: 25px 0;
            height: 2px;
            background: linear-gradient(90deg, transparent, #667eea, transparent);
        }
        
        .service-list {
            margin-top: 15px;
        }
        
        .service-item {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            padding: 15px 20px;
            border-radius: 12px;
            margin-bottom: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            transition: all 0.3s ease;
            animation: slideInLeft 0.4s ease both;
        }
        
        .service-item:hover {
            transform: translateX(5px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.2);
        }
        
        .service-name {
            font-weight: 600;
            color: #333;
        }
        
        .service-details {
            text-align: right;
        }
        
        .service-qty {
            color: #667eea;
            font-weight: 700;
        }
        
        .service-price {
            color: #666;
            font-size: 0.9rem;
        }
        
        .total-section {
            margin-top: 30px;
            padding: 25px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 20px;
            color: white;
            text-align: center;
            animation: scaleIn 0.5s ease both;
        }
        
        @keyframes scaleIn {
            from {
                opacity: 0;
                transform: scale(0.9);
            }
            to {
                opacity: 1;
                transform: scale(1);
            }
        }
        
        .total-label {
            font-size: 1.2rem;
            font-weight: 600;
            margin-bottom: 10px;
            opacity: 0.9;
        }
        
        .total-amount {
            font-size: 2.5rem;
            font-weight: 800;
            text-shadow: 2px 2px 10px rgba(0,0,0,0.2);
        }
        
        .qr-section {
            text-align: center;
        }
        
        .qr-code {
            background: white;
            padding: 20px;
            border-radius: 20px;
            margin: 20px 0;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            animation: pulse 2s ease infinite;
        }
        
        @keyframes pulse {
            0%, 100% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.02);
            }
        }
        
        .qr-code img {
            max-width: 100%;
            height: auto;
            border-radius: 10px;
        }
        
        .booking-code {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 25px;
            border-radius: 15px;
            font-size: 1.3rem;
            font-weight: 700;
            margin: 20px 0;
            display: inline-block;
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
            animation: glow 2s ease infinite;
        }
        
        @keyframes glow {
            0%, 100% {
                box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
            }
            50% {
                box-shadow: 0 5px 30px rgba(102, 126, 234, 0.6);
            }
        }
        
        .submit-btn {
            width: 100%;
            padding: 18px 40px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 15px;
            font-size: 1.2rem;
            font-weight: 700;
            font-family: 'Poppins', sans-serif;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
            position: relative;
            overflow: hidden;
            margin-top: 20px;
        }
        
        .back-btn {
            width: 100%;
            padding: 18px 40px;
            background: linear-gradient(135deg, #95a5a6 0%, #7f8c8d 100%);
            color: white;
            border: none;
            border-radius: 15px;
            font-size: 1.2rem;
            font-weight: 700;
            font-family: 'Poppins', sans-serif;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 10px 30px rgba(149, 165, 166, 0.4);
            position: relative;
            overflow: hidden;
            margin-bottom: 15px;
        }
        
        .back-btn::before {
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
        
        .back-btn:hover::before {
            width: 300px;
            height: 300px;
        }
        
        .back-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(149, 165, 166, 0.5);
        }
        
        .back-btn:active {
            transform: translateY(-1px);
        }
        
        .button-group {
            display: flex;
            flex-direction: column;
            gap: 15px;
            margin-top: 20px;
        }
        
        .submit-btn::before {
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
        
        .submit-btn:hover::before {
            width: 300px;
            height: 300px;
        }
        
        .submit-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(102, 126, 234, 0.5);
        }
        
        .submit-btn:active {
            transform: translateY(-1px);
        }
        
        .submit-btn.loading {
            opacity: 0.7;
            cursor: not-allowed;
        }
        
        .submit-btn .loading {
            display: none;
        }
        
        .submit-btn.loading .loading {
            display: inline-block;
            animation: spin 1s linear infinite;
        }
        
        .submit-btn.loading .text {
            display: none;
        }
        
        @keyframes spin {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }
        
        @media (max-width: 1024px) {
            .layout {
                grid-template-columns: 1fr;
            }
        }
        
        @media (max-width: 768px) {
            .header h1 {
                font-size: 2rem;
            }
            
            .card {
                padding: 25px;
            }
            
            .total-amount {
                font-size: 2rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/home">🏠 Trang chủ</a> / 
            <a href="${pageContext.request.contextPath}/hotels">Khách sạn</a> / 
            <a href="${pageContext.request.contextPath}/bookings?hotel=${hotel.id}">Đặt phòng</a> / 
            <span style="color: white;">Xác nhận</span>
        </div>
        
        <div class="header">
            <h1>✨ Xác nhận đặt phòng</h1>
            <p>${hotel.name}</p>
        </div>
        
        <c:if test="${not empty errorMessage}">
            <div style="background: rgba(255, 107, 107, 0.9); color: white; padding: 20px; border-radius: 15px; margin-bottom: 20px; animation: fadeInDown 0.6s ease;">
                <strong>⚠️ Lỗi:</strong> ${errorMessage}
            </div>
        </c:if>
        
        <div class="layout">
            <div class="card">
                <h2 class="card-title">📋 Thông tin đặt phòng</h2>
                
                <div class="info-item">
                    <span class="info-label">Loại phòng</span>
                    <span class="info-value">${roomType}</span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Số phòng</span>
                    <span class="info-value">${numberOfRooms}</span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Khách</span>
                    <span class="info-value">${numAdults} người lớn, ${numChildren} trẻ em</span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Nhận phòng</span>
                    <span class="info-value"><fmt:formatDate value="${checkIn}" pattern="dd/MM/yyyy" /></span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Trả phòng</span>
                    <span class="info-value"><fmt:formatDate value="${checkOut}" pattern="dd/MM/yyyy" /></span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Số đêm</span>
                    <span class="info-value">${nights} đêm</span>
                </div>
                
                <c:if test="${fn:length(chosenMeals) > 0}">
                    <div class="section-divider"></div>
                    <h3 style="font-weight: 700; color: #667eea; margin-bottom: 15px;">🍽️ Dịch vụ ăn uống</h3>
                    <div class="service-list">
                        <c:forEach var="i" begin="0" end="${fn:length(chosenMeals)-1}" varStatus="st">
                            <div class="service-item" style="animation-delay: ${st.index * 0.1}s">
                                <span class="service-name">${chosenMeals[i].mealType} (<fmt:formatDate value="${chosenMeals[i].mealDate}" pattern="dd/MM/yyyy" />)</span>
                                <div class="service-details">
                                    <span class="service-qty">x${chosenMealQtys[i]}</span>
                                    <span class="service-price"><fmt:formatNumber value="${chosenMeals[i].price}" pattern="#,###" />₫</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                
                <c:if test="${fn:length(chosenWellness) > 0}">
                    <div class="section-divider"></div>
                    <h3 style="font-weight: 700; color: #667eea; margin-bottom: 15px;">💆 Wellness & Spa</h3>
                    <div class="service-list">
                        <c:forEach var="i" begin="0" end="${fn:length(chosenWellness)-1}" varStatus="st">
                            <div class="service-item" style="animation-delay: ${st.index * 0.1}s">
                                <span class="service-name">${chosenWellness[i].serviceName}</span>
                                <div class="service-details">
                                    <span class="service-qty">x${chosenWellnessQtys[i]}</span>
                                    <span class="service-price"><fmt:formatNumber value="${chosenWellness[i].basePrice}" pattern="#,###" />₫</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                
                <c:if test="${fn:length(chosenTransports) > 0}">
                    <div class="section-divider"></div>
                    <h3 style="font-weight: 700; color: #667eea; margin-bottom: 15px;">🚗 Dịch vụ vận chuyển</h3>
                    <div class="service-list">
                        <c:forEach var="i" begin="0" end="${fn:length(chosenTransports)-1}" varStatus="st">
                            <div class="service-item" style="animation-delay: ${st.index * 0.1}s">
                                <span class="service-name">
                                    <strong>${chosenTransports[i].vehicleName}</strong><br/>
                                    <small style="color: #666;">${chosenTransports[i].vehicleType} - ${chosenTransports[i].pickupLocation}</small><br/>
                                    <small style="color: #666;">
                                        <fmt:formatDate value="${chosenTransports[i].departureTime}" pattern="dd/MM/yyyy HH:mm" /> - 
                                        ${chosenTransports[i].capacity} chỗ
                                    </small>
                                </span>
                                <div class="service-details">
                                    <span class="service-qty">x${chosenTransportQtys[i]}</span>
                                    <span class="service-price"><fmt:formatNumber value="${chosenTransports[i].price}" pattern="#,###" />₫</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                
                <div class="total-section">
                    <div class="total-label">Tổng tiền</div>
                    <div style="font-size: 0.9rem; opacity: 0.9; margin-bottom: 10px;">
                        Phòng: <fmt:formatNumber value="${roomSubtotal}" pattern="#,###" />₫<br/>
                        Dịch vụ: <fmt:formatNumber value="${addonsTotal}" pattern="#,###" />₫<br/>
                        <c:if test="${fn:length(chosenTransports) > 0}">
                            Vận chuyển: <fmt:formatNumber value="${transportFee}" pattern="#,###" />₫<br/>
                        </c:if>
                    </div>
                    <div class="total-amount"><fmt:formatNumber value="${total}" pattern="#,###" />₫</div>
                </div>
                
                <div class="button-group">
                    <form id="backForm" method="post" action="${pageContext.request.contextPath}/bookings/back">
                        <input type="hidden" name="hotel" value="${hotel.id}" />
                        <input type="hidden" name="room_type" value="${roomType}" />
                        <input type="hidden" name="number_of_rooms" value="${numberOfRooms}" />
                        <input type="hidden" name="num_adults" value="${numAdults}" />
                        <input type="hidden" name="num_children" value="${numChildren}" />
                        <input type="hidden" name="check_in_date" value="<fmt:formatDate value="${checkIn}" pattern="yyyy-MM-dd" />" />
                        <input type="hidden" name="check_out_date" value="<fmt:formatDate value="${checkOut}" pattern="yyyy-MM-dd" />" />
                        <c:if test="${not empty chosenMeals and fn:length(chosenMeals) > 0}">
                            <c:forEach var="i" begin="0" end="${fn:length(chosenMeals)-1}">
                                <input type="hidden" name="meal_id" value="${chosenMeals[i].mealId}" />
                                <input type="hidden" name="meal_qty" value="${chosenMealQtys[i]}" />
                            </c:forEach>
                        </c:if>
                        <c:if test="${not empty chosenWellness and fn:length(chosenWellness) > 0}">
                            <c:forEach var="i" begin="0" end="${fn:length(chosenWellness)-1}">
                                <input type="hidden" name="wellness_id" value="${chosenWellness[i].wellnessId}" />
                                <input type="hidden" name="wellness_qty" value="${chosenWellnessQtys[i]}" />
                            </c:forEach>
                        </c:if>
                        <c:if test="${not empty chosenTransports and fn:length(chosenTransports) > 0}">
                            <c:forEach var="i" begin="0" end="${fn:length(chosenTransports)-1}">
                                <input type="hidden" name="transport_id" value="${chosenTransports[i].transportId}" />
                                <input type="hidden" name="transport_qty" value="${chosenTransportQtys[i]}" />
                            </c:forEach>
                        </c:if>
                        <button type="submit" class="back-btn">
                            ← Quay lại để chỉnh sửa
                        </button>
                    </form>
                    
                    <form id="confirmForm" method="post" action="${pageContext.request.contextPath}/bookings/confirm">
                        <input type="hidden" name="hotel" value="${hotel.id}" />
                        <input type="hidden" name="room_type" value="${roomType}" />
                        <input type="hidden" name="number_of_rooms" value="${numberOfRooms}" />
                        <input type="hidden" name="num_adults" value="${numAdults}" />
                        <input type="hidden" name="num_children" value="${numChildren}" />
                        <input type="hidden" name="check_in_date" value="<fmt:formatDate value="${checkIn}" pattern="yyyy-MM-dd" />" />
                        <input type="hidden" name="check_out_date" value="<fmt:formatDate value="${checkOut}" pattern="yyyy-MM-dd" />" />
                        <input type="hidden" name="total" value="${total}" />
                        <input type="hidden" name="booking_code" value="${bookingCode}" />
                        <c:if test="${not empty chosenMeals and fn:length(chosenMeals) > 0}">
                            <c:forEach var="i" begin="0" end="${fn:length(chosenMeals)-1}">
                                <input type="hidden" name="meal_id" value="${chosenMeals[i].mealId}" />
                                <input type="hidden" name="meal_qty" value="${chosenMealQtys[i]}" />
                            </c:forEach>
                        </c:if>
                        <c:if test="${not empty chosenWellness and fn:length(chosenWellness) > 0}">
                            <c:forEach var="i" begin="0" end="${fn:length(chosenWellness)-1}">
                                <input type="hidden" name="wellness_id" value="${chosenWellness[i].wellnessId}" />
                                <input type="hidden" name="wellness_qty" value="${chosenWellnessQtys[i]}" />
                            </c:forEach>
                        </c:if>
                        <c:if test="${not empty chosenTransports and fn:length(chosenTransports) > 0}">
                            <c:forEach var="i" begin="0" end="${fn:length(chosenTransports)-1}">
                                <input type="hidden" name="transport_id" value="${chosenTransports[i].transportId}" />
                                <input type="hidden" name="transport_qty" value="${chosenTransportQtys[i]}" />
                            </c:forEach>
                        </c:if>
                        <button type="submit" class="submit-btn" id="confirmBtn" onclick="this.classList.add('loading')">
                            <span class="text">✅ Xác nhận đặt phòng</span>
                            <span class="loading">⏳</span>
                        </button>
                    </form>
                </div>
            </div>
            
            <div class="card qr-section">
                <h2 class="card-title">💳 Thanh toán</h2>
                <div class="booking-code">${bookingCode}</div>
                <div class="qr-code">
                    <img src="${qrUrl}" alt="VietQR Code" />
                </div>
                <p style="color: #666; font-size: 0.9rem; margin-top: 15px;">
                    Quét mã QR để thanh toán qua ứng dụng ngân hàng của bạn
                </p>
            </div>
        </div>
    </div>
</body>
</html>
