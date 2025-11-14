<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="${pageDescription}">
    <meta name="keywords" content="${pageKeywords}">
    <meta name="author" content="SamSon Travel Team">
    
    <!-- Open Graph Meta Tags -->
    <meta property="og:title" content="${pageTitle}">
    <meta property="og:description" content="${pageDescription}">
    <meta property="og:type" content="website">
    <meta property="og:url" content="${pageContext.request.requestURL}">
    <meta property="og:image" content="${pageContext.request.contextPath}/assets/images/hero/samson-travel-og.jpg">
    
    <!-- Twitter Card Meta Tags -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${pageTitle}">
    <meta name="twitter:description" content="${pageDescription}">
    <meta name="twitter:image" content="${pageContext.request.contextPath}/assets/images/hero/samson-travel-twitter.jpg">
    
    <title>${pageTitle}</title>
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/icons/favicon.ico">
    <link rel="apple-touch-icon" sizes="180x180" href="${pageContext.request.contextPath}/assets/images/icons/apple-touch-icon.png">
    
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
    
    <!-- Structured Data -->
    <script type="application/ld+json">
    {
        "@context": "https://schema.org",
        "@type": "TravelAgency",
        "name": "SamSon Travel",
        "description": "Dịch vụ du lịch cao cấp tại Sầm Sơn",
        "url": "${pageContext.request.requestURL}",
        "logo": "${pageContext.request.contextPath}/assets/images/icons/logo.png",
        "address": {
            "@type": "PostalAddress",
            "addressLocality": "Sầm Sơn",
            "addressRegion": "Thanh Hóa",
            "addressCountry": "VN"
        },
        "contactPoint": {
            "@type": "ContactPoint",
            "telephone": "+84-123-456-789",
            "contactType": "customer service"
        }
    }
    </script>
</head>
<body>
    <!-- Loading Overlay -->
    <div id="loadingOverlay" class="loading-overlay">
        <div class="loading-content">
            <div class="loading-spinner"></div>
            <div class="loading-text">Đang tải trang...</div>
        </div>
    </div>

    <!-- Skip Links for Accessibility -->
    <a href="#main-content" class="skip-link">Skip to main content</a>
    <a href="#navigation" class="skip-link">Skip to navigation</a>

    <!-- Header -->
    <jsp:include page="/components/header.jsp" />

    <!-- Main Content -->
    <main id="main-content" role="main">
        <!-- Section 1: Hero Section -->
        <section id="home" class="hero-section" aria-label="Hero section">
            <div class="hero-slider" id="heroSlider" role="img" aria-label="Hero images showcasing SamSon Travel destinations">
                <c:forEach var="heroImage" items="${homepageData.heroImages}" varStatus="status">
                    <div class="hero-slide ${status.first ? 'active' : ''}" 
                         style="background-image: url('${pageContext.request.contextPath}/uploads/${heroImage.fileUrl}')"
                         aria-hidden="${status.first ? 'false' : 'true'}">
                    </div>
                </c:forEach>
            </div>
            
            <div class="hero-overlay"></div>
            
            <div class="hero-content animate-fade-in">
                <h1 class="hero-title animate-slide-up">
                    Khám Phá Vẻ Đẹp<br>
                    <span class="text-gradient">Sầm Sơn</span>
                </h1>
                <p class="hero-subtitle animate-slide-up delay-200">
                    Trải nghiệm du lịch tuyệt vời với các dịch vụ đa dạng, khách sạn cao cấp và dịch vụ chuyên nghiệp
                </p>
                <div class="hero-buttons animate-slide-up delay-400">
                    <a href="#booking" class="hero-cta hero-cta-secondary">
                        <i class="fas fa-calendar-check"></i>
                        Đặt ngay
                    </a>
                </div>
            </div>
            
            <div class="scroll-indicator">
                <i class="fas fa-chevron-down scroll-indicator-icon"></i>
            </div>
        </section>
        
        <!-- Section 2: Quick Booking Widget -->
        <section id="booking" class="booking-section">
            <div class="booking-widget" id="bookingWidget">
                <div class="booking-widget-header">
                    <h3 class="booking-widget-title">Đặt Dịch Vụ Ngay</h3>
                    <p class="booking-widget-subtitle">Tìm kiếm và đặt dịch vụ phù hợp với bạn</p>
                </div>
                
                <form class="booking-form" id="bookingForm" role="search" aria-label="Booking search form">
                    <div class="booking-form-group">
                        <label class="booking-form-label" for="destination">Điểm đến</label>
                        <select class="booking-form-select" name="destination" id="destination" aria-required="true">
                            <option value="">Chọn điểm đến</option>
                            <option value="samson">Sầm Sơn</option>
                            <option value="honme">Đảo Hòn Mê</option>
                            <option value="docuoc">Chùa Độc Cước</option>
                        </select>
                    </div>
                    
                    <div class="booking-form-group">
                        <label class="booking-form-label" for="departureDate">Ngày</label>
                        <input type="date" class="booking-form-input" name="departureDate" id="departureDate" required aria-required="true">
                    </div>
                    
                    <div class="booking-form-group">
                        <label class="booking-form-label" for="guests">Số người</label>
                        <select class="booking-form-select" name="guests" id="guests" aria-required="true">
                            <option value="1">1 người</option>
                            <option value="2">2 người</option>
                            <option value="3">3 người</option>
                            <option value="4">4 người</option>
                            <option value="5+">5+ người</option>
                        </select>
                    </div>
                    
                    <button type="submit" class="booking-form-button" aria-describedby="booking-help">
                        <i class="fas fa-search" aria-hidden="true"></i>
                        <span>Tìm kiếm</span>
                    </button>
                    <div id="booking-help" class="sr-only">Tìm kiếm dịch vụ phù hợp với tiêu chí của bạn</div>
                </form>
            </div>
        </section>

        <!-- Section 4: Featured Hotels -->
        <section id="hotels" class="section hotels-section">
            <div class="container">
                <div class="section-header animate-fade-in">
                    <h2 class="section-title">Khách Sạn Cao Cấp</h2>
                    <div class="section-divider"></div>
                    <p class="section-subtitle">
                        Nghỉ dưỡng tại những khách sạn và resort cao cấp nhất với view biển tuyệt đẹp
                    </p>
                </div>
                
                <div class="hotels-grid">
                    <c:forEach var="hotel" items="${homepageData.featuredHotels}" varStatus="status">
                        <div class="hotel-card animate-slide-up" style="animation-delay: ${status.index * 0.1}s">
                            <div class="hotel-card-image">
                                <c:choose>
                                    <c:when test="${not empty hotel.imageUrl}">
                                        <img src="${pageContext.request.contextPath}/${hotel.imageUrl}" 
                                             alt="${hotel.name}" 
                                             loading="lazy"
                                             onerror="console.error('Failed to load image: ${hotel.imageUrl}'); this.src='${pageContext.request.contextPath}/uploads/hotel_image/hotel_1.jpg';">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/uploads/hotel_image/hotel_1.jpg" 
                                             alt="${hotel.name}" 
                                             loading="lazy"
                                             onerror="this.style.display='none';">
                                    </c:otherwise>
                                </c:choose>
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
                                
                                <div class="hotel-card-amenities">
                                    <c:forTokens var="amenity" items="${hotel.amenities}" delims=",">
                                        <span class="hotel-card-amenity">${amenity}</span>
                                    </c:forTokens>
                                </div>
                                
                                <div class="hotel-card-price">
                                    Từ <fmt:formatNumber value="2000000" type="currency" currencyCode="VND"/>/đêm
                                </div>
                                
                                <button class="btn btn-primary" onclick="viewHotelDetails(${hotel.id})">
                                    <i class="fas fa-bed"></i>
                                    Xem chi tiết
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                
                <div class="text-center animate-fade-in delay-800">
                    <a href="${pageContext.request.contextPath}/hotels" class="btn btn-secondary btn-lg">
                        <i class="fas fa-bed"></i>
                        Xem tất cả khách sạn
                    </a>
                </div>
            </div>
        </section>

        <!-- Section 5: Destinations -->
        <section id="destinations" class="section destinations-section">
            <div class="container">
                <div class="section-header animate-fade-in">
                    <h2 class="section-title">Điểm Đến Hấp Dẫn</h2>
                    <div class="section-divider"></div>
                    <p class="section-subtitle">
                        Khám phá những địa điểm du lịch nổi tiếng và đẹp nhất tại Sầm Sơn
                    </p>
                </div>
                
                <div class="destinations-grid">
                    <c:forEach var="destination" items="${homepageData.destinationImages}" varStatus="status">
                        <div class="destination-card animate-scale-in" style="animation-delay: ${status.index * 0.1}s">
                            <div class="destination-image">
                                <img src="${pageContext.request.contextPath}/uploads/${destination.fileUrl}" 
                                     alt="${destination.title}" loading="lazy">
                                <div class="destination-overlay">
                                    <h3 class="destination-title">${destination.title}</h3>
                                    <p class="destination-description">${destination.description}</p>
                                    <button class="btn btn-white" onclick="viewDestination('${destination.title}')">
                                        <i class="fas fa-eye"></i>
                                        Khám phá
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Section 6: Services -->
        <section id="services" class="section services-section">
            <div class="container">
                <div class="section-header animate-fade-in">
                    <h2 class="section-title">Dịch Vụ Của Chúng Tôi</h2>
                    <div class="section-divider"></div>
                    <p class="section-subtitle">
                        Cung cấp đầy đủ các dịch vụ du lịch từ A-Z với chất lượng cao nhất
                    </p>
                </div>
                
                <div class="services-grid">
                    <c:forEach var="service" items="${homepageData.serviceCategories}" varStatus="status">
                        <div class="service-card animate-slide-up" style="animation-delay: ${status.index * 0.1}s">
                            <div class="service-icon">
                                <i class="${service.iconClass}"></i>
                            </div>
                            <h3 class="service-title">${service.categoryName}</h3>
                            <p class="service-description">${service.description}</p>
                            <a href="${pageContext.request.contextPath}/services?category=${service.categoryCode}" 
                               class="service-link">
                                <i class="fas fa-arrow-right"></i>
                                Tìm hiểu thêm
                            </a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- Section 8: Statistics -->
        <section id="statistics" class="section statistics-section">
            <div class="container">
                <div class="statistics-grid">
                    <div class="statistic-item animate-count-up delay-200">
                        <div class="statistic-number" data-target="${homepageData.totalHotels}">0</div>
                        <div class="statistic-label">Khách sạn</div>
                    </div>
                    <div class="statistic-item animate-count-up delay-600">
                        <div class="statistic-number" data-target="98">0</div>
                        <div class="statistic-label">% Hài lòng</div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Section 9: Newsletter -->
        <section id="newsletter" class="newsletter-section">
            <div class="container">
                <div class="newsletter-content animate-fade-in">
                    <h2 class="newsletter-title">Đăng Ký Nhận Tin</h2>
                    <p class="newsletter-subtitle">
                        Nhận thông tin khuyến mãi đặc biệt và tin tức du lịch từ SamSon Travel
                    </p>
                    
                    <form class="newsletter-form" id="newsletterForm" role="form" aria-label="Newsletter subscription form">
                        <label for="newsletter-email" class="sr-only">Email address for newsletter</label>
                        <input type="email" class="newsletter-input" id="newsletter-email" placeholder="Nhập email của bạn" required aria-required="true">
                        <button type="submit" class="newsletter-button" aria-describedby="newsletter-help">
                            <i class="fas fa-paper-plane" aria-hidden="true"></i>
                            <span>Đăng ký</span>
                        </button>
                        <div id="newsletter-help" class="sr-only">Đăng ký nhận tin về khuyến mãi</div>
                    </form>
                </div>
            </div>
        </section>

        <!-- Section 10: About Us -->
        <section id="about" class="section about-section">
            <div class="container">
                <div class="about-content">
                    <div class="about-text animate-slide-left">
                        <h2 class="section-title">Về SamSon Travel</h2>
                        <div class="section-divider"></div>
                        <p class="about-description">
                            Với hơn 10 năm kinh nghiệm trong lĩnh vực du lịch, SamSon Travel tự hào là đơn vị 
                            cung cấp dịch vụ du lịch uy tín và chuyên nghiệp tại Sầm Sơn. Chúng tôi cam kết mang đến 
                            cho khách hàng những trải nghiệm du lịch tuyệt vời nhất với giá cả hợp lý.
                        </p>
                        <p class="about-description">
                            Đội ngũ nhân viên giàu kinh nghiệm, nhiệt tình và chuyên nghiệp sẽ đồng hành cùng bạn 
                            trong suốt hành trình khám phá vẻ đẹp của Sầm Sơn.
                        </p>
                        
                        <div class="about-features">
                            <div class="about-feature">
                                <i class="fas fa-award"></i>
                                <span>Dịch vụ chuyên nghiệp</span>
                            </div>
                            <div class="about-feature">
                                <i class="fas fa-shield-alt"></i>
                                <span>Bảo hiểm đầy đủ</span>
                            </div>
                            <div class="about-feature">
                                <i class="fas fa-headset"></i>
                                <span>Hỗ trợ 24/7</span>
                            </div>
                            <div class="about-feature">
                                <i class="fas fa-dollar-sign"></i>
                                <span>Giá cả hợp lý</span>
                            </div>
                        </div>
                        
                        <a href="${pageContext.request.contextPath}/about" class="btn btn-primary btn-lg">
                            <i class="fas fa-info-circle"></i>
                            Tìm hiểu thêm
                        </a>
                    </div>
                    
                    <div class="about-image animate-slide-right">
                        <img src="${pageContext.request.contextPath}/assets/images/about/about-us.jpg" 
                             alt="Về SamSon Travel" loading="lazy">
                    </div>
                </div>
            </div>
        </section>
    </main>

    <!-- Footer -->
    <jsp:include page="/components/footer.jsp" />

    <!-- Back to Top Button -->
    <button class="back-to-top" id="backToTop" aria-label="Back to top" aria-describedby="back-to-top-help">
        <i class="fas fa-chevron-up" aria-hidden="true"></i>
        <span class="sr-only">Back to top</span>
    </button>
    <div id="back-to-top-help" class="sr-only">Return to the top of the page</div>

    <!-- Toast Container -->
    <div class="toast-container" id="toastContainer"></div>

    <!-- JavaScript Files -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/animations.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
    
    <!-- Initialize Page -->
    <script>
        // Initialize page when DOM is loaded
        document.addEventListener('DOMContentLoaded', function() {
            // Hide loading overlay
            setTimeout(() => {
                document.getElementById('loadingOverlay').style.display = 'none';
            }, 1000);
            
            // Initialize homepage functionality
            initializeHomepage();
        });
        
        // Role-based content visibility
        <c:if test="${userRole == 'ADMINISTRATOR' || userRole == 'SERVICE_MANAGER'}">
            // Show admin-specific content
            document.addEventListener('DOMContentLoaded', function() {
                showAdminFeatures();
            });
        </c:if>
    </script>
</body>
</html>
