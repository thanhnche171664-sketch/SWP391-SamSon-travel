<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="jakarta.servlet.jsp.PageContext" %>
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
        .hotels-page-header {
            background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
            color: var(--white);
            padding: var(--spacing-20) 0 var(--spacing-16);
            text-align: center;
            margin-top: 80px;
        }
        
        .hotels-page-title {
            font-size: var(--font-size-5xl);
            font-weight: var(--font-weight-extrabold);
            margin-bottom: var(--spacing-4);
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
        
        .hotels-page-subtitle {
            font-size: var(--font-size-xl);
            opacity: 0.9;
            margin-bottom: var(--spacing-8);
        }
        
        .search-section {
            background: var(--white);
            padding: var(--spacing-8);
            border-radius: var(--radius-2xl);
            box-shadow: var(--shadow-xl);
            max-width: 800px;
            margin: -60px auto 0;
            position: relative;
            z-index: 10;
        }
        
        .search-form {
            display: flex;
            gap: var(--spacing-4);
            align-items: end;
        }
        
        .search-input-group {
            flex: 1;
        }
        
        .search-input {
            width: 100%;
            padding: var(--spacing-4);
            border: 2px solid var(--gray-200);
            border-radius: var(--radius-lg);
            font-size: var(--font-size-base);
            transition: all var(--transition-base);
        }
        
        .search-input:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }
        
        .search-button {
            padding: var(--spacing-4) var(--spacing-8);
            background: var(--primary-color);
            color: var(--white);
            border: none;
            border-radius: var(--radius-lg);
            font-size: var(--font-size-base);
            font-weight: var(--font-weight-semibold);
            cursor: pointer;
            transition: all var(--transition-base);
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
        }
        
        .search-button:hover {
            background: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: var(--shadow-lg);
        }
        
        .hotels-section {
            padding: var(--spacing-20) 0;
            background: var(--gray-50);
        }
        
        .results-info {
            text-align: center;
            margin-bottom: var(--spacing-8);
            color: var(--gray-600);
        }
        
        .hotels-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: var(--spacing-8);
            margin-bottom: var(--spacing-16);
        }
        
        .hotel-card {
            background: var(--white);
            border-radius: var(--radius-xl);
            box-shadow: var(--shadow-md);
            overflow: hidden;
            transition: all var(--transition-base);
            position: relative;
        }
        
        .hotel-card:hover {
            transform: translateY(-8px);
            box-shadow: var(--shadow-2xl);
        }
        
        .hotel-card-image {
            position: relative;
            height: 250px;
            overflow: hidden;
            background: var(--gray-200);
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .hotel-card-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform var(--transition-slow);
            display: block;
            background: var(--gray-200);
        }
        
        .hotel-card-image::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: var(--gray-200);
            z-index: 0;
            display: none; /* Hide placeholder when image loads */
        }
        
        .hotel-card-image img {
            position: relative;
            z-index: 1;
            min-height: 100%;
            width: 100%;
            height: 100%;
            object-fit: cover;
            background: var(--gray-200);
        }
        
        .hotel-card-image img[src=""] {
            display: none;
        }
        
        .hotel-card-image img[src*="default-hotel"] {
            object-fit: contain;
            background: var(--gray-200);
        }
        
        .hotel-card-image img.external-hotel-image[src=""] {
            display: block;
            background: var(--gray-200);
        }
        
        .hotel-card:hover .hotel-card-image img {
            transform: scale(1.1);
        }
        
        .hotel-card-rating {
            position: absolute;
            top: var(--spacing-4);
            right: var(--spacing-4);
            background: rgba(255, 255, 255, 0.95);
            padding: var(--spacing-2) var(--spacing-3);
            border-radius: var(--radius-lg);
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
            font-weight: var(--font-weight-semibold);
            color: var(--secondary-color);
        }
        
        .hotel-card-badge {
            position: absolute;
            top: var(--spacing-4);
            left: var(--spacing-4);
            background: var(--accent-color);
            color: var(--white);
            padding: var(--spacing-2) var(--spacing-4);
            border-radius: var(--radius-lg);
            font-size: var(--font-size-sm);
            font-weight: var(--font-weight-semibold);
        }
        
        .hotel-card-content {
            padding: var(--spacing-6);
        }
        
        .hotel-card-title {
            font-size: var(--font-size-xl);
            font-weight: var(--font-weight-bold);
            color: var(--gray-900);
            margin-bottom: var(--spacing-3);
        }
        
        .hotel-card-location {
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
            color: var(--gray-600);
            font-size: var(--font-size-sm);
            margin-bottom: var(--spacing-4);
        }
        
        .hotel-card-description {
            color: var(--gray-600);
            line-height: var(--line-height-relaxed);
            margin-bottom: var(--spacing-4);
            display: -webkit-box;
            -webkit-line-clamp: 3;
            line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        
        .hotel-card-amenities {
            display: flex;
            flex-wrap: wrap;
            gap: var(--spacing-2);
            margin-bottom: var(--spacing-4);
        }
        
        .hotel-card-amenity {
            background: var(--gray-100);
            color: var(--gray-700);
            padding: var(--spacing-1) var(--spacing-3);
            border-radius: var(--radius-full);
            font-size: var(--font-size-xs);
            font-weight: var(--font-weight-medium);
        }
        
        .hotel-card-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: var(--spacing-4);
            padding-top: var(--spacing-4);
            border-top: 1px solid var(--gray-200);
        }
        
        .hotel-card-button {
            display: flex;
            align-items: center;
            gap: var(--spacing-2);
            padding: var(--spacing-3) var(--spacing-6);
            background: var(--primary-color);
            color: var(--white);
            border: none;
            border-radius: var(--radius-lg);
            font-size: var(--font-size-base);
            font-weight: var(--font-weight-semibold);
            cursor: pointer;
            transition: all var(--transition-base);
            text-decoration: none;
        }
        
        .hotel-card-button:hover {
            background: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: var(--shadow-lg);
        }
        
        .no-results {
            text-align: center;
            padding: var(--spacing-16);
            color: var(--gray-600);
        }
        
        .no-results-icon {
            font-size: var(--font-size-6xl);
            color: var(--gray-400);
            margin-bottom: var(--spacing-4);
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
    <main id="main-content" role="main">
        <!-- Page Header -->
        <section class="hotels-page-header">
            <div class="container">
                <h1 class="hotels-page-title animate-fade-in">Khách Sạn & Resort</h1>
                <p class="hotels-page-subtitle animate-fade-in delay-200">
                    Khám phá các khách sạn và resort cao cấp tại Sầm Sơn
                </p>
            </div>
        </section>

        <!-- Search Section -->
        <section class="search-section">
            <form method="get" action="${pageContext.request.contextPath}/hotels" class="search-form" id="searchForm">
                <div class="search-input-group">
                    <input type="text" 
                           name="search" 
                           class="search-input" 
                           placeholder="Tìm kiếm khách sạn theo tên hoặc địa chỉ..." 
                           value="${searchKeyword}"
                           aria-label="Search hotels">
                </div>
                <button type="submit" class="search-button">
                    <i class="fas fa-search"></i>
                    <span>Tìm kiếm</span>
                </button>
            </form>
        </section>

        <!-- Hotels Section -->
        <section class="hotels-section">
            <div class="container">
                <div class="results-info animate-fade-in">
                    <c:choose>
                        <c:when test="${not empty searchKeyword}">
                            <p>Tìm thấy <strong>${totalHotels}</strong> khách sạn cho từ khóa "<strong>${searchKeyword}</strong>"</p>
                        </c:when>
                        <c:otherwise>
                            <p>Tổng cộng <strong>${totalHotels}</strong> khách sạn</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <c:choose>
                    <c:when test="${not empty hotels}">
                        <div class="hotels-grid">
                            <c:forEach var="hotel" items="${hotels}" varStatus="status">
                                <div class="hotel-card animate-slide-up" style="animation-delay: ${status.index * 0.1}s">
                                    <div class="hotel-card-image">
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
                                        <c:if test="${hotel.featured}">
                                            <div class="hotel-card-badge">
                                                <i class="fas fa-star"></i> Nổi bật
                                            </div>
                                        </c:if>
                                        <c:if test="${hotel.rating > 0}">
                                            <div class="hotel-card-rating">
                                                <i class="fas fa-star"></i>
                                                <span><fmt:formatNumber value="${hotel.rating}" maxFractionDigits="1"/></span>
                                            </div>
                                        </c:if>
                                    </div>
                                    
                                    <div class="hotel-card-content">
                                        <h3 class="hotel-card-title">${hotel.name}</h3>
                                        <div class="hotel-card-location">
                                            <i class="fas fa-map-marker-alt"></i>
                                            ${hotel.address}
                                        </div>
                                        
                                        <c:if test="${not empty hotel.description}">
                                            <p class="hotel-card-description">${hotel.description}</p>
                                        </c:if>
                                        
                                        <c:if test="${not empty hotel.amenities}">
                                            <div class="hotel-card-amenities">
                                                <c:forTokens var="amenity" items="${hotel.amenities}" delims=",">
                                                    <span class="hotel-card-amenity">${amenity}</span>
                                                </c:forTokens>
                                            </div>
                                        </c:if>
                                        
                                        <div class="hotel-card-footer">
                                            <a href="${pageContext.request.contextPath}/hotel-details?id=${hotel.id}" 
                                               class="hotel-card-button">
                                                <i class="fas fa-bed"></i>
                                                Xem chi tiết
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="no-results animate-fade-in">
                            <div class="no-results-icon">
                                <i class="fas fa-hotel"></i>
                            </div>
                            <h3>Không tìm thấy khách sạn</h3>
                            <c:if test="${not empty searchKeyword}">
                                <p>Không có khách sạn nào phù hợp với từ khóa "<strong>${searchKeyword}</strong>"</p>
                                <a href="${pageContext.request.contextPath}/hotels" class="btn btn-primary mt-4">
                                    <i class="fas fa-arrow-left"></i>
                                    Xem tất cả khách sạn
                                </a>
                            </c:if>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <jsp:include page="/components/footer.jsp" />

    <!-- Back to Top Button -->
    <button class="back-to-top" id="backToTop" aria-label="Back to top">
        <i class="fas fa-chevron-up"></i>
    </button>

    <!-- JavaScript Files -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/animations.js"></script>
    
    <!-- Initialize Page -->
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
            
            // Set up external images with proxy - MUST RUN FIRST
            function setupExternalImages() {
                const externalImages = document.querySelectorAll('img.external-hotel-image');
                console.log('Found external images:', externalImages.length);
                const contextPath = '<%= request.getContextPath() %>';
                
                if (externalImages.length === 0) {
                    console.warn('No external images found!');
                    return;
                }
                
                externalImages.forEach(function(img, index) {
                    const imageUrl = img.getAttribute('data-image-url');
                    if (imageUrl && imageUrl.trim() !== '') {
                        try {
                            const encodedUrl = encodeURIComponent(imageUrl);
                            const proxyUrl = contextPath + '/image-proxy?url=' + encodedUrl;
                            img.src = proxyUrl;
                            console.log('[' + index + '] Setting image src:', imageUrl.substring(0, 60) + '...');
                            
                            // Force image load
                            img.onload = function() {
                                console.log('[' + index + '] Image loaded successfully');
                            };
                            img.onerror = function() {
                                console.error('[' + index + '] Failed to load image:', imageUrl);
                                this.src = contextPath + '/assets/images/hotels/default-hotel.jpg';
                            };
                        } catch (e) {
                            console.error('[' + index + '] Error setting image src:', e);
                        }
                    } else {
                        console.warn('[' + index + '] Image missing or empty data-image-url attribute');
                    }
                });
            }
            
            // Run immediately
            setupExternalImages();
            
            // Also run after a short delay to catch any late-loading images
            setTimeout(setupExternalImages, 100);
            
            // Debug: Log all images
            setTimeout(function() {
                const allImages = document.querySelectorAll('.hotel-card-image img');
                console.log('Total images found:', allImages.length);
                allImages.forEach(function(img, index) {
                    console.log('Image ' + index + ':', {
                        src: img.src,
                        alt: img.alt,
                        complete: img.complete,
                        naturalWidth: img.naturalWidth,
                        naturalHeight: img.naturalHeight
                    });
                });
            }, 100);
            
            // Initialize animations
            if (typeof initializeAnimations === 'function') {
                initializeAnimations();
            }
        });
    </script>
</body>
</html>

