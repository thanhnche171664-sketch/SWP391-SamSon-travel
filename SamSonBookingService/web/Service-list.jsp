<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="entity.*" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chọn dịch vụ - Sam Son Travel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- CSS riêng -->
    <link rel="stylesheet" href="assets/css/service-list.css">
</head>
<body>
<div class="container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-header">
            <h3><i class="fas fa-umbrella-beach"></i> <span>SamSon Travel</span></h3>
        </div>
        <nav class="sidebar-menu">
            <a href="HomePage.jsp"><i class="fas fa-home"></i><span>Trang chủ</span></a>
            <a href="AddBooking.jsp"><i class="fas fa-plus-circle"></i><span>Booking mới</span></a>
            <a href="#"><i class="fas fa-users"></i><span>Khách hàng</span></a>
            <a href="ListRoom.jsp"><i class="fas fa-hotel"></i><span>Phòng</span></a>
            <a href="service-list" class="active"><i class="fas fa-concierge-bell"></i><span>Dịch vụ</span></a>
            <a href="#"><i class="fas fa-headset"></i><span>Hỗ trợ</span></a>
        </nav>
    </aside>

    <!-- Main -->
    <main class="main-content">
        <header>
            <h1>Chọn dịch vụ</h1>
            <div class="user-info">
                <button class="btn-view-cart" onclick="showMiniCart()" id="viewCartBtn" style="display: none;">
                    <i class="fas fa-shopping-cart"></i>
                    <span id="headerCartCount">0</span>
                </button>
                <div class="user-avatar">K</div>
                <span><b>Khanh</b></span>
            </div>
        </header>

        <section class="content">
            <!-- Bộ lọc + Tìm kiếm -->
            <div class="content-header">
                <div class="page-title">
                    <i class="fas fa-shopping-cart"></i>
                    <h2>Chọn dịch vụ cho khách</h2>
                </div>
                <div class="filter-section">
                    <div class="search-box">
                        <i class="fas fa-search"></i>
                        <input type="text" id="searchInput" placeholder="Tìm kiếm dịch vụ..." onkeyup="filterServices()">
                    </div>
                    <button class="filter-btn active" data-filter="all" onclick="filterByCategory('all')">
                        <i class="fas fa-list"></i>Tất cả
                    </button>
                    <button class="filter-btn" data-filter="MEAL" onclick="filterByCategory('MEAL')">
                        <i class="fas fa-utensils"></i>Ăn uống
                    </button>
                    <button class="filter-btn" data-filter="WELLNESS" onclick="filterByCategory('WELLNESS')">
                        <i class="fas fa-spa"></i>Spa & Wellness
                    </button>
                </div>
            </div>

            <!-- Giỏ hàng mini -->
            <div class="cart-summary" id="cartSummary" style="display: none;">
                <div class="cart-info">
                    <i class="fas fa-shopping-cart"></i>
                    <span id="cartCount">0</span> dịch vụ đã chọn
                    <span class="cart-total" id="cartTotal">0₫</span>
                </div>
                <button class="btn-checkout" onclick="proceedToCheckout()">
                    <i class="fas fa-arrow-right"></i> Tiếp tục
                </button>
            </div>

            <!-- Danh sách dịch vụ -->
            <div class="services-grid" id="servicesGrid">
                <!-- Meal Services từ Database -->
                <c:if test="${not empty mealServices}">
                    <c:forEach var="meal" items="${mealServices}">
                        <div class="service-card" data-category="MEAL" data-service-type="meal" data-service-id="${meal.mealId}">
                            <div class="service-icon-wrapper food">
                                <i class="fas fa-utensils service-icon"></i>
                                <span class="service-status-badge">Có sẵn</span>
                            </div>
                            <div class="service-info">
                                <div class="service-header">
                                    <div class="service-name">${meal.mealType}</div>
                                    <div class="service-category">Ăn uống</div>
                                </div>
                                <div class="service-description">${meal.description}</div>
                                <div class="service-price"><fmt:formatNumber value="${meal.price}" type="number" groupingUsed="true"/>₫</div>
                                <div class="service-details">
                                    <div class="service-detail-item">
                                        <i class="fas fa-calendar"></i>
                                        <span><fmt:formatDate value="${meal.mealDate}" pattern="dd/MM/yyyy"/></span>
                                    </div>
                                    <div class="service-detail-item">
                                        <i class="fas fa-map-marker-alt"></i>
                                        <span>Nhà hàng tầng 1</span>
                                    </div>
                                </div>
                                <div class="service-actions">
                                    <button class="btn-action btn-add-to-cart" onclick="addToCart(this)">
                                        <i class="fas fa-plus"></i> Thêm vào giỏ
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
                
                <!-- Wellness Services từ Database -->
                <c:if test="${not empty wellnessServices}">
                    <c:forEach var="wellness" items="${wellnessServices}">
                        <div class="service-card" data-category="WELLNESS" data-service-type="wellness" data-service-id="${wellness.wellnessId}">
                            <div class="service-icon-wrapper spa">
                                <i class="fas fa-spa service-icon"></i>
                                <span class="service-status-badge">Có sẵn</span>
                            </div>
                            <div class="service-info">
                                <div class="service-header">
                                    <div class="service-name">${wellness.serviceName}</div>
                                    <div class="service-category">Spa & Wellness</div>
                                </div>
                                <div class="service-description">${wellness.description}</div>
                                <div class="service-price">
                                    <fmt:formatNumber value="${wellness.basePrice}" type="number" groupingUsed="true"/>₫
                                    <c:if test="${not empty wellness.durationMinutes}">
                                        /${wellness.durationMinutes} phút
                                    </c:if>
                                </div>
                                <div class="service-details">
                                    <c:if test="${not empty wellness.operatingHours}">
                                        <div class="service-detail-item">
                                            <i class="fas fa-clock"></i>
                                            <span>${wellness.operatingHours}</span>
                                        </div>
                                    </c:if>
                                    <div class="service-detail-item">
                                        <i class="fas fa-map-marker-alt"></i>
                                        <span>Spa tầng 3</span>
                                    </div>
                                    <c:if test="${not empty wellness.capacity}">
                                        <div class="service-detail-item">
                                            <i class="fas fa-users"></i>
                                            <span>Sức chứa: ${wellness.capacity} người</span>
                                        </div>
                                    </c:if>
                                </div>
                                <div class="service-actions">
                                    <button class="btn-action btn-add-to-cart" onclick="addToCart(this)">
                                        <i class="fas fa-plus"></i> Thêm vào giỏ
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
                
                <!-- Hiển thị khi không có dữ liệu -->
                <c:if test="${empty mealServices && empty wellnessServices}">
                    <div class="no-services">
                        <i class="fas fa-inbox"></i>
                        <h3>Chưa có dịch vụ nào</h3>
                        <p>Hiện tại chưa có dịch vụ nào được cung cấp</p>
                    </div>
                </c:if>
            </div>
        </section>
    </main>

    <!-- Mini Cart Sidebar -->
    <aside class="mini-cart" id="miniCart" style="display: none;">
        <div class="mini-cart-header">
            <h3><i class="fas fa-shopping-cart"></i> Dịch vụ đã chọn</h3>
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
            <button class="btn-payment" onclick="proceedToPayment()">
                <i class="fas fa-credit-card"></i> Thanh toán
            </button>
        </div>
    </aside>
</div>

<script>
// Shopping cart functionality - UPDATED: 2025-10-22 15:30
console.log('🔥 SERVICE-LIST.JSP LOADED - VERSION 2.0 - UPDATED CODE! 🔥');
let cart = [];
let cartTotal = 0;

// Load cart from sessionStorage on page load
window.addEventListener('DOMContentLoaded', function() {
    const savedCart = sessionStorage.getItem('serviceCart');
    if (savedCart) {
        cart = JSON.parse(savedCart);
        console.log('Cart loaded from sessionStorage:', cart);
        updateCartDisplay();
        updateMiniCart();
        updateAllButtonStates();
    }
});

// Save cart to sessionStorage
function saveCart() {
    sessionStorage.setItem('serviceCart', JSON.stringify(cart));
    console.log('Cart saved to sessionStorage');
}

function addToCart(button) {
    const serviceCard = button.closest('.service-card');
    const serviceId = serviceCard.dataset.serviceId; // Giữ nguyên string
    const serviceType = serviceCard.dataset.serviceType;
    const serviceName = serviceCard.querySelector('.service-name').textContent;
    const servicePrice = serviceCard.querySelector('.service-price').textContent;
    
    console.log('Adding to cart:', serviceId, serviceName);
    
    // Extract price number
    const priceMatch = servicePrice.match(/[\d,]+/);
    const price = priceMatch ? parseInt(priceMatch[0].replace(/,/g, '')) : 0;
    
    // Check if already in cart (so sánh string với string)
    const existingItem = cart.find(item => item.id === serviceId);
    if (existingItem) {
        existingItem.quantity += 1;
        console.log('Item already in cart, increased quantity to:', existingItem.quantity);
    } else {
        cart.push({
            id: serviceId, // Lưu dưới dạng string
            type: serviceType,
            name: serviceName,
            price: price,
            quantity: 1
        });
        console.log('New item added to cart');
    }
    
    updateCartDisplay();
    updateMiniCart();
    updateButtonState(button, true);
    showMiniCart();
    saveCart(); // Lưu vào sessionStorage
}

function updateCartDisplay() {
    const cartCount = cart.reduce((sum, item) => sum + item.quantity, 0);
    cartTotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    
    document.getElementById('cartCount').textContent = cartCount;
    document.getElementById('cartTotal').textContent = cartTotal.toLocaleString() + '₫';
    document.getElementById('headerCartCount').textContent = cartCount;
    
    const cartSummary = document.getElementById('cartSummary');
    const viewCartBtn = document.getElementById('viewCartBtn');
    
    if (cartCount > 0) {
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
    
    // Update active button
    buttons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    // Filter cards
    cards.forEach(card => {
        if (category === 'all' || card.dataset.category === category) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

function filterServices() {
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
    
    console.log('Updating mini cart, cart length:', cart.length);
    
    if (cart.length === 0) {
        miniCartContent.innerHTML = '<div class="empty-cart">Chưa có dịch vụ nào được chọn</div>';
        miniCartTotal.textContent = '0₫';
        return;
    }
    
    let html = '';
    cart.forEach(function(item) {
        const totalPrice = item.price * item.quantity;
        html += '<div class="mini-cart-item" data-item-id="' + item.id + '">';
        html += '  <div class="item-info">';
        html += '    <div class="item-name">' + item.name + '</div>';
        html += '    <div class="item-price">' + item.price.toLocaleString() + '₫ x ' + item.quantity + ' = ' + totalPrice.toLocaleString() + '₫</div>';
        html += '  </div>';
        html += '  <div class="item-controls">';
        html += '    <button class="btn-quantity" onclick="updateQuantity(\'' + item.id + '\', -1)" title="Giảm số lượng">';
        html += '      <i class="fas fa-minus"></i>';
        html += '    </button>';
        html += '    <span class="quantity">' + item.quantity + '</span>';
        html += '    <button class="btn-quantity" onclick="updateQuantity(\'' + item.id + '\', 1)" title="Tăng số lượng">';
        html += '      <i class="fas fa-plus"></i>';
        html += '    </button>';
        html += '    <button class="btn-remove" onclick="removeFromCart(\'' + item.id + '\')" title="Xóa dịch vụ">';
        html += '      <i class="fas fa-trash"></i>';
        html += '    </button>';
        html += '  </div>';
        html += '</div>';
    });
    
    miniCartContent.innerHTML = html;
    miniCartTotal.textContent = cartTotal.toLocaleString() + '₫';
}

function updateQuantity(serviceId, change) {
    console.log('updateQuantity called - serviceId:', serviceId, 'type:', typeof serviceId, 'change:', change);
    console.log('Current cart:', cart);
    
    // Tìm item trong cart (so sánh string với string)
    const item = cart.find(item => {
        console.log('Comparing', item.id, 'with', serviceId, '→', item.id === serviceId);
        return item.id === serviceId;
    });
    
    if (item) {
        console.log('Item found! Current quantity:', item.quantity);
        item.quantity += change;
        
        if (item.quantity <= 0) {
            cart = cart.filter(item => item.id !== serviceId);
            console.log('Item removed from cart (quantity = 0)');
        } else {
            console.log('Item quantity updated to:', item.quantity);
        }
        
        updateCartDisplay();
        updateMiniCart();
        updateAllButtonStates();
        saveCart(); // Lưu vào sessionStorage
    } else {
        console.error('❌ Item NOT found in cart!');
        console.error('Looking for serviceId:', serviceId, 'type:', typeof serviceId);
        console.error('Available IDs in cart:', cart.map(item => ({id: item.id, type: typeof item.id})));
    }
}

function removeFromCart(serviceId) {
    console.log('Removing item from cart:', serviceId);
    const initialLength = cart.length;
    cart = cart.filter(item => item.id !== serviceId);
    console.log('Cart length before:', initialLength, 'after:', cart.length);
    updateCartDisplay();
    updateMiniCart();
    updateAllButtonStates();
    saveCart(); // Lưu vào sessionStorage
}

function updateAllButtonStates() {
    const buttons = document.querySelectorAll('.btn-add-to-cart');
    buttons.forEach(button => {
        const serviceCard = button.closest('.service-card');
        const serviceId = serviceCard.dataset.serviceId;
        const item = cart.find(item => item.id === serviceId);
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

function proceedToCheckout() {
    // Store cart data in sessionStorage for next page
    sessionStorage.setItem('selectedServices', JSON.stringify(cart));
    
    // Redirect to booking page or service selection confirmation
    alert('Đã lưu ' + cart.length + ' dịch vụ vào giỏ hàng. Chuyển đến trang booking...');
    // window.location.href = 'AddBooking.jsp';
}

function proceedToPayment() {
    if (cart.length === 0) {
        alert('Vui lòng chọn ít nhất một dịch vụ!');
        return;
    }
    
    // Store cart data for payment
    sessionStorage.setItem('selectedServices', JSON.stringify(cart));
    sessionStorage.setItem('totalAmount', cartTotal.toString());
    
    // Redirect to payment page
    alert('Chuyển đến trang thanh toán với tổng tiền: ' + cartTotal.toLocaleString() + '₫');
    // window.location.href = 'Payment.jsp';
}

// Debug function - có thể xóa sau khi test xong
function debugCart() {
    console.log('=== CART DEBUG ===');
    console.log('Cart length:', cart.length);
    console.log('Cart total:', cartTotal);
    console.log('Cart items:', cart);
    console.log('==================');
}

// Thêm event listener để debug
document.addEventListener('DOMContentLoaded', function() {
    console.log('Service list page loaded');
    console.log('Available functions: addToCart, updateQuantity, removeFromCart, showMiniCart, closeMiniCart');
});
</script>
</body>
</html>