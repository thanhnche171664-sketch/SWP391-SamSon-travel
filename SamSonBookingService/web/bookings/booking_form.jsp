<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt phòng - ${hotel.name} | SamSon Travel</title>
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
            max-width: 1200px;
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
        
        .header p {
            font-size: 1.2rem;
            color: rgba(255,255,255,0.9);
            font-weight: 300;
        }
        
        .booking-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 30px;
            padding: 40px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            animation: fadeInUp 0.8s ease 0.2s both;
            margin-bottom: 30px;
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
        
        .price-summary {
            position: sticky;
            top: 20px;
            z-index: 1000;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
            border-radius: 20px;
            margin-bottom: 30px;
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
            animation: slideInRight 0.6s ease 0.4s both;
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
        
        .price-summary h3 {
            font-size: 1.3rem;
            margin-bottom: 15px;
            font-weight: 700;
        }
        
        .price-item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid rgba(255,255,255,0.2);
        }
        
        .price-item:last-child {
            border-bottom: none;
            margin-top: 10px;
            padding-top: 15px;
            border-top: 2px solid rgba(255,255,255,0.3);
            font-size: 1.3rem;
            font-weight: 800;
        }
        
        .form-section {
            margin-bottom: 35px;
            animation: slideInLeft 0.6s ease both;
        }
        
        @keyframes slideInLeft {
            from {
                opacity: 0;
                transform: translateX(-30px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }
        
        .section-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: #667eea;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .section-title::before {
            content: '';
            width: 4px;
            height: 25px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            border-radius: 2px;
        }
        
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 25px;
        }
        
        .form-group {
            position: relative;
        }
        
        .form-group label {
            display: block;
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            font-size: 0.95rem;
        }
        
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 14px 18px;
            border: 2px solid #e0e0e0;
            border-radius: 12px;
            font-size: 1rem;
            font-family: 'Poppins', sans-serif;
            transition: all 0.3s ease;
            background: #fff;
        }
        
        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
            transform: translateY(-2px);
        }
        
        .form-group input:invalid {
            border-color: #ff6b6b;
        }
        
        .error-message {
            color: #ff6b6b;
            font-size: 0.85rem;
            margin-top: 5px;
            display: none;
        }
        
        .form-group input:invalid + .error-message {
            display: block;
        }
        
        .services-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 15px;
            margin-top: 20px;
        }
        
        .service-item {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border: 2px solid transparent;
            border-radius: 15px;
            padding: 20px;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            cursor: pointer;
            position: relative;
            overflow: hidden;
        }
        
        .service-item::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
            transition: left 0.5s;
        }
        
        .service-item:hover::before {
            left: 100%;
        }
        
        .service-item:hover {
            transform: translateY(-5px) scale(1.02);
            border-color: #667eea;
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.3);
        }
        
        .service-item input[type="checkbox"] {
            width: 22px;
            height: 22px;
            cursor: pointer;
            margin-right: 12px;
            accent-color: #667eea;
        }
        
        .service-item input[type="checkbox"]:checked ~ .service-content .service-label {
            color: #667eea;
            font-weight: 600;
        }
        
        .service-item input[type="checkbox"]:checked ~ .service-content .service-qty {
            opacity: 1;
            pointer-events: auto;
        }
        
        .service-content {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .service-label {
            flex: 1;
            color: #333;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .service-price {
            color: #667eea;
            font-weight: 700;
            font-size: 1.1rem;
            white-space: nowrap;
        }
        
        .service-qty {
            width: 70px;
            padding: 8px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            text-align: center;
            font-weight: 600;
            transition: all 0.3s ease;
            opacity: 0.5;
            pointer-events: none;
        }
        
        .service-item input[type="checkbox"]:checked ~ .service-content .service-qty {
            opacity: 1;
            pointer-events: auto;
            border-color: #667eea;
        }
        
        .service-qty:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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
            margin-top: 30px;
        }
        
        .submit-btn:disabled {
            opacity: 0.6;
            cursor: not-allowed;
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
        
        .submit-btn:hover:not(:disabled)::before {
            width: 300px;
            height: 300px;
        }
        
        .submit-btn:hover:not(:disabled) {
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(102, 126, 234, 0.5);
        }
        
        .submit-btn:active:not(:disabled) {
            transform: translateY(-1px);
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
        
        @media (max-width: 768px) {
            .header h1 {
                font-size: 2rem;
            }
            
            .booking-card {
                padding: 25px;
            }
            
            .form-grid {
                grid-template-columns: 1fr;
            }
            
            .services-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/home">🏠 Trang chủ</a> / 
            <a href="${pageContext.request.contextPath}/hotels">Khách sạn</a> / 
            <span style="color: white;">Đặt phòng</span>
        </div>
        
        <div class="header">
            <h1>✨ Đặt Phòng</h1>
            <p>${hotel.name}</p>
        </div>
        
        <form method="post" action="${pageContext.request.contextPath}/bookings/review" class="booking-card" id="bookingForm" onsubmit="return validateForm()">
            <input type="hidden" name="hotel" value="${hotel.id}" />
            
            <div class="price-summary" id="priceSummary">
                <h3>💰 Tổng tiền</h3>
                <div class="price-item">
                    <span>Phòng:</span>
                    <span id="roomPrice">0₫</span>
                </div>
                <div class="price-item">
                    <span>Dịch vụ:</span>
                    <span id="servicesPrice">0₫</span>
                </div>
                <div class="price-item">
                    <span>Tổng cộng:</span>
                    <span id="totalPrice">0₫</span>
                </div>
            </div>
            
            <div class="form-section">
                <div class="section-title">📅 Ngày đặt phòng</div>
                <div class="form-grid">
                    <div class="form-group">
                        <label>Ngày nhận phòng</label>
                        <input type="date" name="check_in_date" id="checkInDate" required 
                               min="" value="${sessionScope.booking_check_in_date}" onchange="updateCheckOutMin()" />
                        <div class="error-message">Vui lòng chọn ngày nhận phòng</div>
                    </div>
                    <div class="form-group">
                        <label>Ngày trả phòng</label>
                        <input type="date" name="check_out_date" id="checkOutDate" required 
                               min="" value="${sessionScope.booking_check_out_date}" onchange="calculatePrice()" />
                        <div class="error-message">Vui lòng chọn ngày trả phòng</div>
                    </div>
                </div>
            </div>
            
            <div class="form-section">
                <div class="section-title">🏨 Thông tin phòng</div>
                <div class="form-grid">
                    <div class="form-group">
                        <label>Loại phòng</label>
                        <select name="room_type" id="roomType" required onchange="calculatePrice()">
                            <c:forEach var="r" items="${rooms}">
                                <option value="${r.roomType}" data-price="${r.price}" 
                                        ${sessionScope.booking_room_type == r.roomType ? 'selected' : ''}>
                                    ${r.roomType} - <fmt:formatNumber value="${r.price}" pattern="#,###" />₫/đêm
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Số phòng</label>
                        <input type="number" min="1" name="number_of_rooms" id="numberOfRooms" 
                               value="${sessionScope.booking_number_of_rooms != null ? sessionScope.booking_number_of_rooms : '1'}" 
                               required onchange="calculatePrice()" />
                    </div>
                </div>
            </div>
            
            <div class="form-section">
                <div class="section-title">👥 Số lượng khách</div>
                <div class="form-grid">
                    <div class="form-group">
                        <label>Người lớn</label>
                        <input type="number" min="1" name="num_adults" id="numAdults"
                               value="${sessionScope.booking_num_adults != null ? sessionScope.booking_num_adults : '1'}" required />
                    </div>
                    <div class="form-group">
                        <label>Trẻ em</label>
                        <input type="number" min="0" name="num_children" id="numChildren"
                               value="${sessionScope.booking_num_children != null ? sessionScope.booking_num_children : '0'}" required />
                    </div>
                </div>
            </div>
            
            <div class="form-section">
                <div class="section-title">🍽️ Dịch vụ ăn uống</div>
                <div class="services-grid">
                    <c:forEach var="m" items="${mealServices}" varStatus="loop">
                        <div class="service-item">
                            <input type="checkbox" name="meal_id" value="${m.mealId}" id="meal_${m.mealId}" 
                                   data-price="${m.price}" onchange="toggleServiceQty(this); calculatePrice()" />
                            <div class="service-content">
                                <label for="meal_${m.mealId}" class="service-label">
                                    ${m.mealType} (<fmt:formatDate value="${m.mealDate}" pattern="dd/MM/yyyy" />)
                                </label>
                                <span class="service-price"><fmt:formatNumber value="${m.price}" pattern="#,###" />₫</span>
                                <input type="number" name="meal_qty" value="1" min="1" class="service-qty" 
                                       data-index="${loop.index}" onchange="calculatePrice()" />
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            
            <div class="form-section">
                <div class="section-title">💆 Wellness & Spa</div>
                <div class="services-grid">
                    <c:forEach var="w" items="${wellnessServices}" varStatus="loop">
                        <div class="service-item">
                            <input type="checkbox" name="wellness_id" value="${w.wellnessId}" id="wellness_${w.wellnessId}" 
                                   data-price="${w.basePrice}" onchange="toggleServiceQty(this); calculatePrice()" />
                            <div class="service-content">
                                <label for="wellness_${w.wellnessId}" class="service-label">
                                    ${w.serviceName}
                                </label>
                                <span class="service-price"><fmt:formatNumber value="${w.basePrice}" pattern="#,###" />₫</span>
                                <input type="number" name="wellness_qty" value="1" min="1" class="service-qty" 
                                       data-index="${loop.index}" onchange="calculatePrice()" />
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            
            <button type="submit" class="submit-btn" id="submitBtn">
                <span class="text">🚀 Xem lại & Thanh toán</span>
                <span class="loading">⏳</span>
            </button>
        </form>
    </div>
    
    <script>
        // Set min date to today
        document.addEventListener('DOMContentLoaded', function() {
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('checkInDate').min = today;
            document.getElementById('checkOutDate').min = today;
            
            // Restore booking data from session
            restoreBookingData();
            
            calculatePrice();
        });
        
        function restoreBookingData() {
            // Restore meal services
            <c:if test="${not empty sessionScope.booking_meal_ids and not empty sessionScope.booking_meal_qtys}">
                <c:forEach var="mealId" items="${sessionScope.booking_meal_ids}" varStatus="mealLoop">
                    <c:set var="mealQty" value="${sessionScope.booking_meal_qtys[mealLoop.index]}" />
                    const mealCheckbox_${mealLoop.index} = document.getElementById('meal_${mealId}');
                    if (mealCheckbox_${mealLoop.index}) {
                        mealCheckbox_${mealLoop.index}.checked = true;
                        const mealQtyInput_${mealLoop.index} = mealCheckbox_${mealLoop.index}.closest('.service-item').querySelector('.service-qty');
                        if (mealQtyInput_${mealLoop.index}) {
                            mealQtyInput_${mealLoop.index}.disabled = false;
                            mealQtyInput_${mealLoop.index}.value = '${mealQty}';
                        }
                    }
                </c:forEach>
            </c:if>
            
            // Restore wellness services
            <c:if test="${not empty sessionScope.booking_wellness_ids and not empty sessionScope.booking_wellness_qtys}">
                <c:forEach var="wellnessId" items="${sessionScope.booking_wellness_ids}" varStatus="wellnessLoop">
                    <c:set var="wellnessQty" value="${sessionScope.booking_wellness_qtys[wellnessLoop.index]}" />
                    const wellnessCheckbox_${wellnessLoop.index} = document.getElementById('wellness_${wellnessId}');
                    if (wellnessCheckbox_${wellnessLoop.index}) {
                        wellnessCheckbox_${wellnessLoop.index}.checked = true;
                        const wellnessQtyInput_${wellnessLoop.index} = wellnessCheckbox_${wellnessLoop.index}.closest('.service-item').querySelector('.service-qty');
                        if (wellnessQtyInput_${wellnessLoop.index}) {
                            wellnessQtyInput_${wellnessLoop.index}.disabled = false;
                            wellnessQtyInput_${wellnessLoop.index}.value = '${wellnessQty}';
                        }
                    }
                </c:forEach>
            </c:if>
            
            // Update check out min date if check in date is set
            const checkInDate = document.getElementById('checkInDate').value;
            if (checkInDate) {
                updateCheckOutMin();
            }
        }
        
        function updateCheckOutMin() {
            const checkIn = document.getElementById('checkInDate').value;
            if (checkIn) {
                const checkInDate = new Date(checkIn);
                checkInDate.setDate(checkInDate.getDate() + 1);
                document.getElementById('checkOutDate').min = checkInDate.toISOString().split('T')[0];
                calculatePrice();
            }
        }
        
        function toggleServiceQty(checkbox) {
            const qtyInput = checkbox.closest('.service-item').querySelector('.service-qty');
            if (checkbox.checked) {
                qtyInput.disabled = false;
            } else {
                qtyInput.disabled = true;
                qtyInput.value = 1;
            }
        }
        
        function formatCurrency(amount) {
            return new Intl.NumberFormat('vi-VN').format(amount) + '₫';
        }
        
        function calculatePrice() {
            const checkIn = document.getElementById('checkInDate').value;
            const checkOut = document.getElementById('checkOutDate').value;
            const roomType = document.getElementById('roomType');
            const roomPrice = parseFloat(roomType.options[roomType.selectedIndex].dataset.price) || 0;
            const numberOfRooms = parseInt(document.getElementById('numberOfRooms').value) || 1;
            
            let nights = 0;
            if (checkIn && checkOut) {
                const checkInDate = new Date(checkIn);
                const checkOutDate = new Date(checkOut);
                nights = Math.max(0, Math.ceil((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24)));
            }
            
            const roomTotal = roomPrice * nights * numberOfRooms;
            
            let servicesTotal = 0;
            document.querySelectorAll('input[type="checkbox"]:checked').forEach(checkbox => {
                const price = parseFloat(checkbox.dataset.price) || 0;
                const qtyInput = checkbox.closest('.service-item').querySelector('.service-qty');
                const qty = parseInt(qtyInput.value) || 1;
                servicesTotal += price * qty;
            });
            
            const total = roomTotal + servicesTotal;
            
            document.getElementById('roomPrice').textContent = formatCurrency(roomTotal);
            document.getElementById('servicesPrice').textContent = formatCurrency(servicesTotal);
            document.getElementById('totalPrice').textContent = formatCurrency(total);
        }
        
        function validateForm() {
            const checkIn = document.getElementById('checkInDate').value;
            const checkOut = document.getElementById('checkOutDate').value;
            
            if (!checkIn || !checkOut) {
                alert('Vui lòng chọn đầy đủ ngày nhận phòng và ngày trả phòng');
                return false;
            }
            
            const checkInDate = new Date(checkIn);
            const checkOutDate = new Date(checkOut);
            const today = new Date();
            today.setHours(0, 0, 0, 0);
            
            if (checkInDate < today) {
                alert('Ngày nhận phòng không được trong quá khứ');
                return false;
            }
            
            if (checkOutDate <= checkInDate) {
                alert('Ngày trả phòng phải sau ngày nhận phòng');
                return false;
            }
            
            const nights = Math.ceil((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24));
            if (nights < 1 || nights > 30) {
                alert('Số đêm phải từ 1 đến 30 đêm');
                return false;
            }
            
            const submitBtn = document.getElementById('submitBtn');
            submitBtn.disabled = true;
            submitBtn.classList.add('loading');
            
            return true;
        }
        
        // Calculate price on any change
        document.addEventListener('change', function(e) {
            if (e.target.matches('input[type="number"], select, input[type="checkbox"]')) {
                calculatePrice();
            }
        });
    </script>
</body>
</html>
