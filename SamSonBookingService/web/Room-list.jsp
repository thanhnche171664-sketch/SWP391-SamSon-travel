<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="entity.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chọn phòng - Sam Son Travel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/service-list.css">
    <style>
        /* Bổ sung CSS cho Room List */
        .service-icon-wrapper.room-single {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .service-icon-wrapper.room-double {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        .service-icon-wrapper.room-dormitory {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        .room-quantity {
            margin-bottom: 15px;
        }
        
        .room-quantity label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
            color: #666;
        }
        
        .quantity-select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .quantity-select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .date-filter {
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
            padding: 15px;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border-radius: 8px;
            width: 100%;
            margin-bottom: 20px;
        }
        
        .date-filter label {
            font-weight: 500;
            color: #333;
        }
        
        .date-filter input[type="date"] {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 0.95rem;
            min-width: 150px;
        }
        
        .date-filter input[type="date"]:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .btn-check-availability {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn-check-availability:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .item-dates {
            color: #667eea;
            font-size: 0.85rem;
            font-weight: 500;
            margin: 3px 0;
        }
        
        .nights-info {
            color: #f5576c;
            font-weight: bold;
            font-size: 0.9rem;
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
<!--            <a href="AddBooking.jsp"><i class="fas fa-plus-circle"></i><span>Booking mới</span></a>-->
            <a href="room-list" class="active"><i class="fas fa-hotel"></i><span>Phòng</span></a>
            <a href="service-list"><i class="fas fa-concierge-bell"></i><span>Dịch vụ</span></a>
            <a href="${pageContext.request.contextPath}/offline-booking-list"><i class="fas fa-history"></i><span>Lịch sử booking</span></a>
            <a href="#"><i class="fas fa-headset"></i><span>Hỗ trợ</span></a>
        </nav>
    </aside>

    <!-- Main -->
    <main class="main-content">
        <header>
            <h1>Chọn phòng</h1>
            <div class="user-info">
                <button class="btn-view-cart" onclick="showMiniCart()" id="viewCartBtn" style="display: none;">
                    <i class="fas fa-shopping-cart"></i>
                    <span id="headerCartCount">0</span>
                </button>
                <div class="user-avatar">K</div>
                <span><b>Khanh</b> <small>(Lễ tân)</small></span>
            </div>
        </header>

        <section class="content">
            <!-- Bộ lọc + Tìm kiếm -->
            <div class="content-header">
                <div class="page-title">
                    <i class="fas fa-hotel"></i>
                    <h2>Chọn phòng cho khách</h2>
                </div>
                
                <!-- Date Filter -->
                <div class="date-filter">
                    <label><i class="fas fa-calendar-check"></i> Nhận phòng:</label>
                    <input type="date" id="checkinDate" required>
                    
                    <label><i class="fas fa-calendar-times"></i> Trả phòng:</label>
                    <input type="date" id="checkoutDate" required>
                    
                    <button class="btn-check-availability" onclick="calculateNightsDisplay()">
                        <i class="fas fa-calculator"></i> Tính số đêm
                    </button>
                    
                    <span id="nightsDisplay" class="nights-info" style="display: none;"></span>
                </div>
                
                <div class="filter-section">
                    <div class="search-box">
                        <i class="fas fa-search"></i>
                        <input type="text" id="searchInput" placeholder="Tìm kiếm loại phòng..." onkeyup="filterRooms()">
                    </div>
                    <button class="filter-btn active" data-filter="all" onclick="filterByCategory('all')">
                        <i class="fas fa-list"></i>Tất cả
                    </button>
                    <button class="filter-btn" data-filter="single" onclick="filterByCategory('single')">
                        <i class="fas fa-bed"></i>Single
                    </button>
                    <button class="filter-btn" data-filter="double" onclick="filterByCategory('double')">
                        <i class="fas fa-bed"></i><i class="fas fa-bed"></i>Double
                    </button>
                    <button class="filter-btn" data-filter="dormitory" onclick="filterByCategory('dormitory')">
                        <i class="fas fa-users"></i>Dormitory
                    </button>
                </div>
            </div>

            <!-- Giỏ hàng mini -->
            <div class="cart-summary" id="cartSummary" style="display: none;">
                <div class="cart-info">
                    <i class="fas fa-hotel"></i>
                    <span id="cartCount">0</span> phòng đã chọn
                    <span class="cart-total" id="cartTotal">0₫</span>
                </div>
                <button class="btn-checkout" onclick="proceedToBooking()">
                    <i class="fas fa-arrow-right"></i> Tiếp tục
                </button>
            </div>

            <!-- Danh sách phòng -->
            <div class="services-grid" id="roomsGrid">
                <!-- Room Cards từ Database -->
                <c:if test="${not empty rooms}">
                    <c:forEach var="room" items="${rooms}">
                        <div class="service-card" data-category="${room.roomType}" data-room-type="${room.roomType}" data-room-id="${room.id}">
                            <div class="service-icon-wrapper room-${room.roomType}">
                                <i class="fas fa-bed service-icon"></i>
                                <span class="service-status-badge">Còn ${room.availableRooms}/${room.totalRooms}</span>
                            </div>
                            <div class="service-info">
                                <div class="service-header">
                                    <div class="service-name">
                                        Phòng 
                                        <c:choose>
                                            <c:when test="${room.roomType == 'single'}">Single</c:when>
                                            <c:when test="${room.roomType == 'double'}">Double</c:when>
                                            <c:when test="${room.roomType == 'dormitory'}">Dormitory</c:when>
                                        </c:choose>
                                    </div>
                                    <div class="service-category">
                                        <c:choose>
                                            <c:when test="${room.roomType == 'single'}">Phòng đơn</c:when>
                                            <c:when test="${room.roomType == 'double'}">Phòng đôi</c:when>
                                            <c:when test="${room.roomType == 'dormitory'}">Phòng tập thể</c:when>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="service-description">
                                    <c:choose>
                                        <c:when test="${room.roomType == 'single'}">
                                            Phòng đơn tiện nghi với 1 giường đơn, view biển đẹp, đầy đủ tiện ích
                                        </c:when>
                                        <c:when test="${room.roomType == 'double'}">
                                            Phòng đôi cao cấp với 1 giường đôi hoặc 2 giường đơn, phòng rộng rãi
                                        </c:when>
                                        <c:when test="${room.roomType == 'dormitory'}">
                                            Phòng tập thể với nhiều giường, phù hợp cho nhóm du lịch, giá cả phải chăng
                                        </c:when>
                                    </c:choose>
                                </div>
                                <div class="service-price">
                                    <fmt:formatNumber value="${room.price}" type="number" groupingUsed="true"/>₫
                                    <span style="font-size: 0.8rem; font-weight: normal;">/đêm</span>
                                </div>
                                <div class="service-details">
                                    <div class="service-detail-item">
                                        <i class="fas fa-ruler-combined"></i>
                                        <span>
                                            <c:choose>
                                                <c:when test="${room.roomType == 'single'}">25m²</c:when>
                                                <c:when test="${room.roomType == 'double'}">35m²</c:when>
                                                <c:when test="${room.roomType == 'dormitory'}">50m²</c:when>
                                            </c:choose>
                                        </span>
                                    </div>
                                    <div class="service-detail-item">
                                        <i class="fas fa-bed"></i>
                                        <span>
                                            <c:choose>
                                                <c:when test="${room.roomType == 'single'}">1 giường đơn</c:when>
                                                <c:when test="${room.roomType == 'double'}">1 giường đôi</c:when>
                                                <c:when test="${room.roomType == 'dormitory'}">4-6 giường</c:when>
                                            </c:choose>
                                        </span>
                                    </div>
                                    <div class="service-detail-item">
                                        <i class="fas fa-users"></i>
                                        <span>
                                            <c:choose>
                                                <c:when test="${room.roomType == 'single'}">1-2 người</c:when>
                                                <c:when test="${room.roomType == 'double'}">2-3 người</c:when>
                                                <c:when test="${room.roomType == 'dormitory'}">4-6 người</c:when>
                                            </c:choose>
                                        </span>
                                    </div>
                                    <div class="service-detail-item">
                                        <i class="fas fa-door-open"></i>
                                        <span>Còn ${room.availableRooms} phòng</span>
                                    </div>
                                </div>
                                
                                <div class="room-quantity">
                                    <label>Số lượng phòng:</label>
                                    <select class="quantity-select">
                                        <c:forEach var="i" begin="1" end="${room.availableRooms > 10 ? 10 : room.availableRooms}">
                                            <option value="${i}">${i} phòng</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="service-actions">
                                    <button class="btn-action btn-add-to-cart" onclick="addRoomToCart(this)">
                                        <i class="fas fa-plus"></i> Thêm vào giỏ
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
                
                <!-- Hiển thị khi không có dữ liệu -->
                <c:if test="${empty rooms}">
                    <div class="no-services">
                        <i class="fas fa-bed"></i>
                        <h3>Chưa có phòng nào</h3>
                        <p>Hiện tại chưa có phòng nào được cung cấp</p>
                    </div>
                </c:if>
            </div>
        </section>
    </main>

    <!-- Mini Cart Sidebar -->
    <aside class="mini-cart" id="miniCart" style="display: none;">
        <div class="mini-cart-header">
            <h3><i class="fas fa-hotel"></i> Phòng đã chọn</h3>
            <button class="btn-close-cart" onclick="closeMiniCart()">
                <i class="fas fa-times"></i>
            </button>
        </div>
        
        <div class="mini-cart-content" id="miniCartContent">
            <!-- Cart items will be populated here -->
        </div>
        
        <div class="mini-cart-footer">
            <div class="cart-total-section">
                <div class="total-label">Tổng cộng:</div>
                <div class="total-amount" id="miniCartTotal">0₫</div>
            </div>
            <button class="btn-payment" onclick="proceedToBooking()">
                <i class="fas fa-arrow-right"></i> Tiếp tục đặt phòng
            </button>
        </div>
    </aside>
</div>

<script>
// Room cart functionality
console.log('🏨 ROOM-LIST.JSP LOADED - VERSION 1.0');
let roomCart = [];
let cartTotal = 0;

// Set default dates (today + 1 day)
window.addEventListener('DOMContentLoaded', function() {
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    const dayAfter = new Date(today);
    dayAfter.setDate(dayAfter.getDate() + 2);
    
    document.getElementById('checkinDate').valueAsDate = tomorrow;
    document.getElementById('checkoutDate').valueAsDate = dayAfter;
    
    // Load cart from sessionStorage
    // NHƯNG: Kiểm tra xem có flag "booking_completed" không (được set khi booking thành công)
    const bookingCompleted = sessionStorage.getItem('booking_completed');
    if (bookingCompleted === 'true') {
        // Nếu đã booking xong, xóa tất cả giỏ hàng
        sessionStorage.removeItem('roomCart');
        sessionStorage.removeItem('serviceCart');
        sessionStorage.removeItem('checkinDate');
        sessionStorage.removeItem('checkoutDate');
        sessionStorage.removeItem('booking_completed');
        console.log('Đã xóa giỏ hàng vì booking đã hoàn thành');
        roomCart = [];
        updateCartDisplay();
        updateMiniCart();
        updateAllButtonStates();
    } else {
        // Nếu chưa booking, load giỏ hàng như bình thường
        const savedCart = sessionStorage.getItem('roomCart');
        if (savedCart) {
            try {
                roomCart = JSON.parse(savedCart);
                console.log('Room cart loaded from sessionStorage:', roomCart);
                updateCartDisplay();
                updateMiniCart();
                updateAllButtonStates();
            } catch (e) {
                console.warn('Could not parse saved cart:', e);
            }
        }
    }
    
    calculateNightsDisplay();
});

// Save cart to sessionStorage
function saveCart() {
    sessionStorage.setItem('roomCart', JSON.stringify(roomCart));
    console.log('Room cart saved to sessionStorage');
}

// Calculate nights between dates
function calculateNights(checkin, checkout) {
    if (!checkin || !checkout) return 0;
    const date1 = new Date(checkin);
    const date2 = new Date(checkout);
    const diffTime = date2 - date1;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays > 0 ? diffDays : 0;
}

// Display nights calculation
function calculateNightsDisplay() {
    const checkin = document.getElementById('checkinDate').value;
    const checkout = document.getElementById('checkoutDate').value;
    const nights = calculateNights(checkin, checkout);
    const display = document.getElementById('nightsDisplay');
    
    if (nights > 0) {
        display.textContent = nights + ' đêm';
        display.style.display = 'inline-block';
    } else {
        display.textContent = 'Vui lòng chọn ngày hợp lệ';
        display.style.display = 'inline-block';
    }
}

// Add room to cart
function addRoomToCart(button) {
    const checkin = document.getElementById('checkinDate').value;
    const checkout = document.getElementById('checkoutDate').value;
    
    if (!checkin || !checkout) {
        alert('Vui lòng chọn ngày nhận phòng và trả phòng!');
        return;
    }
    
    const nights = calculateNights(checkin, checkout);
    if (nights <= 0) {
        alert('Ngày trả phòng phải sau ngày nhận phòng!');
        return;
    }
    
    const roomCard = button.closest('.service-card');
    const roomId = roomCard.dataset.roomId;
    const roomType = roomCard.dataset.roomType;
    const roomName = roomCard.querySelector('.service-name').textContent.trim();
    const priceText = roomCard.querySelector('.service-price').textContent;
    const quantity = parseInt(roomCard.querySelector('.quantity-select').value);
    
    console.log('Adding room to cart:', roomId, roomName, 'qty:', quantity);
    
    // Extract price
    const priceMatch = priceText.match(/[\d,]+/);
    const pricePerNight = priceMatch ? parseInt(priceMatch[0].replace(/,/g, '')) : 0;
    
    // Check if same room already in cart with same dates
    const existingItem = roomCart.find(item => 
        item.id === roomId && item.checkin === checkin && item.checkout === checkout
    );
    
    if (existingItem) {
        existingItem.quantity += quantity;
        console.log('Room already in cart, increased quantity to:', existingItem.quantity);
    } else {
        roomCart.push({
            id: roomId,
            type: roomType,
            name: roomName,
            pricePerNight: pricePerNight,
            quantity: quantity,
            checkin: checkin,
            checkout: checkout,
            nights: nights
        });
        console.log('New room added to cart');
    }
    
    updateCartDisplay();
    updateMiniCart();
    updateButtonState(button, true);
    showMiniCart();
    saveCart();
}

function updateCartDisplay() {
    const totalRooms = roomCart.reduce((sum, item) => sum + item.quantity, 0);
    cartTotal = roomCart.reduce((sum, item) => sum + (item.pricePerNight * item.quantity * item.nights), 0);
    
    document.getElementById('cartCount').textContent = totalRooms;
    document.getElementById('cartTotal').textContent = cartTotal.toLocaleString() + '₫';
    document.getElementById('headerCartCount').textContent = totalRooms;
    
    const cartSummary = document.getElementById('cartSummary');
    const viewCartBtn = document.getElementById('viewCartBtn');
    
    if (totalRooms > 0) {
        cartSummary.style.display = 'flex';
        viewCartBtn.style.display = 'flex';
    } else {
        cartSummary.style.display = 'none';
        viewCartBtn.style.display = 'none';
    }
}

function updateButtonState(button, added) {
    if (added) {
        button.innerHTML = '<i class="fas fa-check"></i> Đã thêm';
        button.classList.add('added');
    } else {
        button.innerHTML = '<i class="fas fa-plus"></i> Thêm vào giỏ';
        button.classList.remove('added');
    }
}

function filterByCategory(category) {
    const cards = document.querySelectorAll('.service-card');
    const buttons = document.querySelectorAll('.filter-btn');
    
    buttons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    cards.forEach(card => {
        if (category === 'all' || card.dataset.category === category) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

function filterRooms() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const cards = document.querySelectorAll('.service-card');
    
    cards.forEach(card => {
        const name = card.querySelector('.service-name').textContent.toLowerCase();
        const description = card.querySelector('.service-description').textContent.toLowerCase();
        
        if (name.includes(searchTerm) || description.includes(searchTerm)) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

function updateMiniCart() {
    const miniCartContent = document.getElementById('miniCartContent');
    const miniCartTotal = document.getElementById('miniCartTotal');
    
    console.log('Updating mini cart, cart length:', roomCart.length);
    
    if (roomCart.length === 0) {
        miniCartContent.innerHTML = '<div class="empty-cart">Chưa có phòng nào được chọn</div>';
        miniCartTotal.textContent = '0₫';
        return;
    }
    
    let html = '';
    roomCart.forEach(function(item, index) {
        const totalPrice = item.pricePerNight * item.quantity * item.nights;
        const checkinFormatted = new Date(item.checkin).toLocaleDateString('vi-VN');
        const checkoutFormatted = new Date(item.checkout).toLocaleDateString('vi-VN');
        
        html += '<div class="mini-cart-item" data-item-index="' + index + '">';
        html += '  <div class="item-info">';
        html += '    <div class="item-name">' + item.quantity + 'x ' + item.name + '</div>';
        html += '    <div class="item-dates">' + checkinFormatted + ' → ' + checkoutFormatted + ' (' + item.nights + ' đêm)</div>';
        html += '    <div class="item-price">' + item.pricePerNight.toLocaleString() + '₫ x ' + item.quantity + ' x ' + item.nights + 'đêm = ' + totalPrice.toLocaleString() + '₫</div>';
        html += '  </div>';
        html += '  <div class="item-controls">';
        html += '    <button class="btn-quantity" onclick="updateQuantity(' + index + ', -1)" title="Giảm số lượng">';
        html += '      <i class="fas fa-minus"></i>';
        html += '    </button>';
        html += '    <span class="quantity">' + item.quantity + '</span>';
        html += '    <button class="btn-quantity" onclick="updateQuantity(' + index + ', 1)" title="Tăng số lượng">';
        html += '      <i class="fas fa-plus"></i>';
        html += '    </button>';
        html += '    <button class="btn-remove" onclick="removeFromCart(' + index + ')" title="Xóa phòng">';
        html += '      <i class="fas fa-trash"></i>';
        html += '    </button>';
        html += '  </div>';
        html += '</div>';
    });
    
    miniCartContent.innerHTML = html;
    miniCartTotal.textContent = cartTotal.toLocaleString() + '₫';
}

function updateQuantity(index, change) {
    console.log('updateQuantity called - index:', index, 'change:', change);
    
    if (roomCart[index]) {
        roomCart[index].quantity += change;
        
        if (roomCart[index].quantity <= 0) {
            roomCart.splice(index, 1);
            console.log('Room removed from cart (quantity = 0)');
        } else {
            console.log('Room quantity updated to:', roomCart[index].quantity);
        }
        
        updateCartDisplay();
        updateMiniCart();
        updateAllButtonStates();
        saveCart();
    }
}

function removeFromCart(index) {
    console.log('Removing room from cart at index:', index);
    roomCart.splice(index, 1);
    updateCartDisplay();
    updateMiniCart();
    updateAllButtonStates();
    saveCart();
}

function updateAllButtonStates() {
    const buttons = document.querySelectorAll('.btn-add-to-cart');
    buttons.forEach(button => {
        const roomCard = button.closest('.service-card');
        const roomId = roomCard.dataset.roomId;
        const item = roomCart.find(item => item.id === roomId);
        updateButtonState(button, !!item);
    });
}

function showMiniCart() {
    const miniCart = document.getElementById('miniCart');
    miniCart.style.display = 'block';
    miniCart.style.animation = 'slideInRight 0.3s ease-out';
}

function closeMiniCart() {
    const miniCart = document.getElementById('miniCart');
    miniCart.style.animation = 'slideOutRight 0.3s ease-out';
    setTimeout(() => {
        miniCart.style.display = 'none';
    }, 300);
}

// Global function for booking - MUST BE ACCESSIBLE FROM HTML onclick
function proceedToBooking() {
    console.log('🚀 proceedToBooking called');
    console.log('📦 Room cart:', roomCart);
    
    if (!roomCart || roomCart.length === 0) {
        alert('Vui lòng chọn ít nhất một phòng!');
        return;
    }
    
    const checkinDate = document.getElementById('checkinDate').value;
    const checkoutDate = document.getElementById('checkoutDate').value;
    
    console.log('📅 Check-in:', checkinDate, 'Check-out:', checkoutDate);
    
    if (!checkinDate || !checkoutDate) {
        alert('Vui lòng chọn ngày check-in và check-out!');
        return;
    }
    
    // Lấy service cart từ sessionStorage (nếu có)
    let serviceCart = [];
    const savedServiceCart = sessionStorage.getItem('serviceCart');
    if (savedServiceCart) {
        try {
            serviceCart = JSON.parse(savedServiceCart);
            console.log('📦 Service cart found:', serviceCart.length, 'items');
        } catch (e) {
            console.warn('Could not parse service cart:', e);
        }
    }
    
    // Tính tổng service cart amount
    const serviceTotal = serviceCart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    
    // BƯỚC 1: Lưu tất cả cart (room + service) vào server session
    const cartData = {
        roomCart: roomCart,
        serviceCart: serviceCart,
        totalAmount: cartTotal + serviceTotal,
        checkinDate: checkinDate,
        checkoutDate: checkoutDate
    };
    
    console.log('💾 Đang lưu cart vào server session...', cartData);
    console.log('📦 Số phòng đã chọn:', roomCart.length);
    roomCart.forEach((room, index) => {
        console.log(`  Phòng ${index + 1}: ${room.name} - Số lượng: ${room.quantity}`);
    });
    
    fetch('${pageContext.request.contextPath}/api/cart/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify(cartData)
    })
    .then(response => response.json())
    .then(data => {
        console.log('✅ Cart đã được lưu vào session:', data);
        
        if (data.success) {
            // BƯỚC 2: Sau khi lưu thành công, redirect đến customer info
            // CHỈ truyền checkin/checkout qua URL, room cart đã có trong session
            const url = '${pageContext.request.contextPath}/customer-info?checkin=' + checkinDate + 
                        '&checkout=' + checkoutDate;
            console.log('🌐 Redirecting to:', url);
            window.location.href = url;
        } else {
            alert('Lỗi khi lưu giỏ hàng: ' + data.message);
        }
    })
    .catch(error => {
        console.error('❌ Lỗi khi lưu cart:', error);
        alert('Lỗi khi lưu giỏ hàng. Vui lòng thử lại!');
    });
}

// Debug
document.addEventListener('DOMContentLoaded', function() {
    console.log('Room list page loaded');
    console.log('Available functions: addRoomToCart, updateQuantity, removeFromCart, showMiniCart, closeMiniCart');
});
</script>
</body>
</html>


