<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="${pageDescription}">
    <meta name="keywords" content="${pageKeywords}">
    <meta name="author" content="SamSon Travel Team">
    
    <title>${pageTitle}</title>
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/icons/favicon.ico">
    
    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- CSS Files -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/animations.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/responsive.css">
    
    <style>
        .hotel-detail-page {
            margin-top: 80px;
            padding-top: var(--spacing-8);
        }
        
        .hotel-detail-header {
            background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
            color: var(--white);
            padding: var(--spacing-12) 0;
            margin-bottom: var(--spacing-8);
        }
        
        .hotel-detail-header-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 var(--spacing-6);
        }
        
        .hotel-detail-breadcrumb {
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
            margin-bottom: var(--spacing-4);
            font-size: var(--font-size-sm);
            opacity: 0.9;
        }
        
        .hotel-detail-breadcrumb a {
            color: var(--white);
            text-decoration: none;
            transition: opacity var(--transition-base);
        }
        
        .hotel-detail-breadcrumb a:hover {
            opacity: 0.8;
        }
        
        .hotel-detail-title {
            font-size: var(--font-size-4xl);
            font-weight: var(--font-weight-extrabold);
            margin-bottom: var(--spacing-4);
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
        
        .hotel-detail-location {
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
            font-size: var(--font-size-lg);
            opacity: 0.95;
            margin-bottom: var(--spacing-4);
        }
        
        .hotel-detail-rating {
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
            font-size: var(--font-size-lg);
            font-weight: var(--font-weight-semibold);
        }
        
        .hotel-detail-main {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 var(--spacing-6);
        }
        
        .hotel-detail-gallery {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: var(--spacing-4);
            margin-bottom: var(--spacing-8);
            border-radius: var(--radius-2xl);
            overflow: hidden;
        }
        
        .hotel-detail-primary-image {
            position: relative;
            width: 100%;
            height: 500px;
            overflow: hidden;
            background: var(--gray-200);
        }
        
        .hotel-detail-primary-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform var(--transition-base);
        }
        
        .hotel-detail-primary-image:hover img {
            transform: scale(1.05);
        }
        
        .hotel-detail-secondary-images {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: var(--spacing-4);
        }
        
        .hotel-detail-secondary-image {
            position: relative;
            width: 100%;
            height: 240px;
            overflow: hidden;
            background: var(--gray-200);
            border-radius: var(--radius-lg);
            cursor: pointer;
        }
        
        .hotel-detail-secondary-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform var(--transition-base);
        }
        
        .hotel-detail-secondary-image:hover img {
            transform: scale(1.1);
        }
        
        .hotel-detail-content {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: var(--spacing-8);
            margin-bottom: var(--spacing-8);
        }
        
        .hotel-detail-info {
            background: var(--white);
            padding: var(--spacing-8);
            border-radius: var(--radius-2xl);
            box-shadow: var(--shadow-xl);
        }
        
        .hotel-detail-section {
            margin-bottom: var(--spacing-8);
        }
        
        .hotel-detail-section:last-child {
            margin-bottom: 0;
        }
        
        .hotel-detail-section-title {
            font-size: var(--font-size-2xl);
            font-weight: var(--font-weight-bold);
            color: var(--gray-900);
            margin-bottom: var(--spacing-4);
            display: flex;
            align-items: center;
            gap: var(--spacing-3);
        }
        
        .hotel-detail-description {
            color: var(--gray-700);
            line-height: var(--line-height-relaxed);
            font-size: var(--font-size-base);
        }
        
        .hotel-detail-amenities {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: var(--spacing-4);
        }
        
        .hotel-detail-amenity {
            display: flex;
            align-items: center;
            gap: var(--spacing-3);
            padding: var(--spacing-3);
            background: var(--gray-50);
            border-radius: var(--radius-lg);
            transition: all var(--transition-base);
        }
        
        .hotel-detail-amenity:hover {
            background: var(--primary-color);
            color: var(--white);
            transform: translateY(-2px);
        }
        
        .hotel-detail-amenity i {
            font-size: var(--font-size-lg);
        }
        
        .hotel-detail-sidebar {
            display: flex;
            flex-direction: column;
            gap: var(--spacing-6);
        }
        
        .hotel-detail-card {
            background: var(--white);
            padding: var(--spacing-6);
            border-radius: var(--radius-2xl);
            box-shadow: var(--shadow-xl);
        }
        
        .hotel-detail-card-title {
            font-size: var(--font-size-xl);
            font-weight: var(--font-weight-bold);
            color: var(--gray-900);
            margin-bottom: var(--spacing-4);
        }
        
        .hotel-detail-card-item {
            display: flex;
            align-items: center;
            gap: var(--spacing-3);
            padding: var(--spacing-3) 0;
            border-bottom: 1px solid var(--gray-200);
        }
        
        .hotel-detail-card-item:last-child {
            border-bottom: none;
        }
        
        .hotel-detail-card-item i {
            color: var(--primary-color);
            font-size: var(--font-size-lg);
            width: 24px;
        }
        
        .hotel-detail-card-item-content {
            flex: 1;
        }
        
        .hotel-detail-card-item-label {
            font-size: var(--font-size-sm);
            color: var(--gray-600);
            margin-bottom: var(--spacing-1);
        }
        
        .hotel-detail-card-item-value {
            font-size: var(--font-size-base);
            font-weight: var(--font-weight-semibold);
            color: var(--gray-900);
        }
        
        .hotel-detail-booking-button {
            width: 100%;
            padding: var(--spacing-4);
            background: var(--primary-color);
            color: var(--white);
            border: none;
            border-radius: var(--radius-lg);
            font-size: var(--font-size-lg);
            font-weight: var(--font-weight-semibold);
            cursor: pointer;
            transition: all var(--transition-base);
            display: flex;
            align-items: center;
            justify-content: center;
            gap: var(--spacing-2);
        }
        
        .hotel-detail-booking-button:hover {
            background: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: var(--shadow-lg);
        }
        
        .hotel-detail-rooms {
            margin-bottom: var(--spacing-8);
        }
        
        .hotel-detail-rooms-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: var(--spacing-6);
        }
        
        .hotel-detail-room-card {
            background: var(--white);
            border-radius: var(--radius-2xl);
            overflow: hidden;
            box-shadow: var(--shadow-xl);
            transition: all var(--transition-base);
        }
        
        .hotel-detail-room-card:hover {
            transform: translateY(-4px);
            box-shadow: var(--shadow-2xl);
        }
        
        .hotel-detail-room-image {
            width: 100%;
            height: 200px;
            overflow: hidden;
            background: var(--gray-200);
        }
        
        .hotel-detail-room-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .hotel-detail-room-content {
            padding: var(--spacing-6);
        }
        
        .hotel-detail-room-type {
            font-size: var(--font-size-xl);
            font-weight: var(--font-weight-bold);
            color: var(--gray-900);
            margin-bottom: var(--spacing-2);
        }
        
        .hotel-detail-room-price {
            font-size: var(--font-size-2xl);
            font-weight: var(--font-weight-extrabold);
            color: var(--primary-color);
            margin-bottom: var(--spacing-4);
        }
        
        .hotel-detail-room-availability {
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
            color: var(--gray-600);
            font-size: var(--font-size-sm);
            margin-bottom: var(--spacing-4);
        }
        
        .hotel-detail-room-button {
            width: 100%;
            padding: var(--spacing-3);
            background: var(--primary-color);
            color: var(--white);
            border: none;
            border-radius: var(--radius-lg);
            font-size: var(--font-size-base);
            font-weight: var(--font-weight-semibold);
            cursor: pointer;
            transition: all var(--transition-base);
        }
        
        .hotel-detail-room-button:hover {
            background: var(--secondary-color);
        }
        
        .hotel-detail-room-button:disabled {
            background: var(--gray-400);
            cursor: not-allowed;
        }
        
        @media (max-width: 768px) {
            .hotel-detail-gallery {
                grid-template-columns: 1fr;
            }
            
            .hotel-detail-secondary-images {
                grid-template-columns: 1fr;
            }
            
            .hotel-detail-content {
                grid-template-columns: 1fr;
            }
            
            .hotel-detail-rooms-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <!-- Loading Overlay -->
    <div id="loadingOverlay" class="loading-overlay">
        <div class="loading-content">
            <div class="loading-spinner"></div>
            <div class="loading-text">Đang tải trang...</div>
        </div>
    </div>

    <!-- Header -->
    <jsp:include page="/components/header.jsp" />

    <!-- Main Content -->
    <main class="hotel-detail-page" role="main">
        <!-- Hotel Detail Header -->
        <div class="hotel-detail-header">
            <div class="hotel-detail-header-content">
                <div class="hotel-detail-breadcrumb">
                    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                    <span>/</span>
                    <a href="${pageContext.request.contextPath}/hotels">Khách sạn</a>
                    <span>/</span>
                    <span>${hotel.name}</span>
                </div>
                <h1 class="hotel-detail-title">${hotel.name}</h1>
                <div class="hotel-detail-location">
                    <i class="fas fa-map-marker-alt"></i>
                    <span>${hotel.address}</span>
                </div>
                <c:if test="${hotel.rating > 0}">
                    <div class="hotel-detail-rating">
                        <i class="fas fa-star" style="color: #FFD700;"></i>
                        <span><fmt:formatNumber value="${hotel.rating}" maxFractionDigits="1"/>/5.0</span>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Hotel Detail Main -->
        <div class="hotel-detail-main">
            <!-- Gallery -->
            <div class="hotel-detail-gallery">
                <div class="hotel-detail-primary-image">
                    <c:choose>
                        <c:when test="${not empty hotel.imageUrl}">
                            <img src="${pageContext.request.contextPath}/${hotel.imageUrl}" 
                                 alt="${hotel.name}"
                                 onerror="console.error('Failed to load image: ${hotel.imageUrl}'); this.src='${pageContext.request.contextPath}/uploads/hotel_image/hotel_1.jpg';">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/uploads/hotel_image/hotel_1.jpg" 
                                 alt="${hotel.name}"
                                 onerror="this.style.display='none';">
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Content -->
            <div class="hotel-detail-content">
                <!-- Main Info -->
                <div class="hotel-detail-info">
                    <!-- Description -->
                    <div class="hotel-detail-section">
                        <h2 class="hotel-detail-section-title">
                            <i class="fas fa-info-circle"></i>
                            Mô tả
                        </h2>
                        <div class="hotel-detail-description">
                            ${hotel.description != null && !hotel.description.isEmpty() ? hotel.description : 'Không có mô tả.'}
                        </div>
                    </div>

                    <!-- Amenities -->
                    <c:if test="${not empty hotel.amenities}">
                        <div class="hotel-detail-section">
                            <h2 class="hotel-detail-section-title">
                                <i class="fas fa-star"></i>
                                Tiện ích
                            </h2>
                            <div class="hotel-detail-amenities">
                                <c:forTokens var="amenity" items="${hotel.amenities}" delims=",">
                                    <div class="hotel-detail-amenity">
                                        <i class="fas fa-check-circle"></i>
                                        <span>${amenity}</span>
                                    </div>
                                </c:forTokens>
                            </div>
                        </div>
                    </c:if>
                </div>

                <!-- Sidebar -->
                <div class="hotel-detail-sidebar">
                    <!-- Quick Info -->
                    <div class="hotel-detail-card">
                        <h3 class="hotel-detail-card-title">Thông tin nhanh</h3>
                        <c:if test="${hotel.checkInTime != null}">
                            <div class="hotel-detail-card-item">
                                <i class="fas fa-sign-in-alt"></i>
                                <div class="hotel-detail-card-item-content">
                                    <div class="hotel-detail-card-item-label">Check-in</div>
                                    <div class="hotel-detail-card-item-value">
                                        <fmt:formatDate value="${hotel.checkInTime}" type="time" timeStyle="short"/>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${hotel.checkOutTime != null}">
                            <div class="hotel-detail-card-item">
                                <i class="fas fa-sign-out-alt"></i>
                                <div class="hotel-detail-card-item-content">
                                    <div class="hotel-detail-card-item-label">Check-out</div>
                                    <div class="hotel-detail-card-item-value">
                                        <fmt:formatDate value="${hotel.checkOutTime}" type="time" timeStyle="short"/>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${hotel.rating > 0}">
                            <div class="hotel-detail-card-item">
                                <i class="fas fa-star"></i>
                                <div class="hotel-detail-card-item-content">
                                    <div class="hotel-detail-card-item-label">Đánh giá</div>
                                    <div class="hotel-detail-card-item-value">
                                        <fmt:formatNumber value="${hotel.rating}" maxFractionDigits="1"/>/5.0
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${hotel.featured}">
                            <div class="hotel-detail-card-item">
                                <i class="fas fa-certificate"></i>
                                <div class="hotel-detail-card-item-content">
                                    <div class="hotel-detail-card-item-label">Trạng thái</div>
                                    <div class="hotel-detail-card-item-value">Nổi bật</div>
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <!-- Booking Button -->
                    <button class="hotel-detail-booking-button" onclick="bookHotel(${hotel.id})">
                        <i class="fas fa-calendar-check"></i>
                        Đặt phòng ngay
                    </button>
                </div>
            </div>

            <!-- Rooms -->
            <c:if test="${not empty rooms}">
                <div class="hotel-detail-rooms">
                    <h2 class="hotel-detail-section-title" style="margin-bottom: var(--spacing-6);">
                        <i class="fas fa-bed"></i>
                        Phòng & Giá
                    </h2>
                    <div class="hotel-detail-rooms-grid">
                        <c:forEach var="room" items="${rooms}">
                            <div class="hotel-detail-room-card">
                                <c:set var="roomImages" value="${roomImagesMap[room.id]}" />
                                <c:choose>
                                    <c:when test="${not empty roomImages}">
                                        <c:set var="roomImage" value="${roomImages[0]}" />
                                        <div class="hotel-detail-room-image">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(roomImage.imageUrl, 'http://') || fn:startsWith(roomImage.imageUrl, 'https://')}">
                                                    <img data-image-url="${roomImage.imageUrl}" 
                                                         src="" 
                                                         alt="${room.roomType}" 
                                                         class="external-hotel-image"
                                                         onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/hotels/default-hotel.jpg';"
                                                         loading="lazy">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/${roomImage.imageUrl}" 
                                                         alt="${room.roomType}"
                                                         onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/hotels/default-hotel.jpg';"
                                                         loading="lazy">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="hotel-detail-room-image">
                                            <img src="${pageContext.request.contextPath}/assets/images/hotels/default-hotel.jpg" 
                                                 alt="${room.roomType}">
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="hotel-detail-room-content">
                                    <h3 class="hotel-detail-room-type">${room.roomType}</h3>
                                    <div class="hotel-detail-room-price">
                                        <fmt:formatNumber value="${room.price}" type="currency" currencyCode="VND"/>
                                    </div>
                                    <div class="hotel-detail-room-availability">
                                        <i class="fas fa-bed"></i>
                                        <span>Còn ${room.availableRooms}/${room.totalRooms} phòng</span>
                                    </div>
                                    <button class="hotel-detail-room-button" 
                                            onclick="bookRoom(${hotel.id}, ${room.id})"
                                            ${room.availableRooms == 0 ? 'disabled' : ''}>
                                        ${room.availableRooms == 0 ? 'Hết phòng' : 'Đặt phòng'}
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>
    </main>

    <!-- Footer -->
    <jsp:include page="/components/footer.jsp" />

    <!-- JavaScript -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Hide loading overlay
            setTimeout(() => {
                const loadingOverlay = document.getElementById('loadingOverlay');
                if (loadingOverlay) {
                    loadingOverlay.style.opacity = '0';
                    setTimeout(() => {
                        loadingOverlay.style.display = 'none';
                    }, 300);
                }
            }, 500);
            
            // Set up external images with proxy
            function setupExternalImages() {
                const externalImages = document.querySelectorAll('img.external-hotel-image');
                const contextPath = '<%= request.getContextPath() %>';
                
                externalImages.forEach(function(img) {
                    const imageUrl = img.getAttribute('data-image-url');
                    if (imageUrl && imageUrl.trim() !== '') {
                        try {
                            const encodedUrl = encodeURIComponent(imageUrl);
                            const proxyUrl = contextPath + '/image-proxy?url=' + encodedUrl;
                            img.src = proxyUrl;
                        } catch (e) {
                            console.error('Error setting image src:', e);
                        }
                    }
                });
            }
            
            setupExternalImages();
            setTimeout(setupExternalImages, 100);
            
            // Initialize animations
            if (typeof initializeAnimations === 'function') {
                initializeAnimations();
            }
        });
        
        function bookHotel(hotelId) {
            // Chuyển đến trang booking với hotel ID
            window.location.href = '${pageContext.request.contextPath}/bookings?hotel=' + hotelId;
        }
        
        function bookRoom(hotelId, roomId) {
            // Chuyển đến trang booking với hotel ID và room ID
            window.location.href = '${pageContext.request.contextPath}/bookings?hotel=' + hotelId + '&room=' + roomId;
        }
    </script>
</body>
</html>

