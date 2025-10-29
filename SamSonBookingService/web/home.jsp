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
    <header class="header" id="header" role="banner">
        <div class="header-container">
            <!-- Logo -->
            <a href="${pageContext.request.contextPath}/home" class="logo" aria-label="SamSon Travel - Go to homepage">
                <img src="${pageContext.request.contextPath}/assets/images/icons/logo.svg" alt="SamSon Travel Logo" class="logo-icon">
                <span>SamSon Travel</span>
            </a>
            
            <!-- Navigation -->
            <nav class="nav" id="nav" role="navigation" aria-label="Main navigation">
                <ul class="nav-list" id="navigation">
                    <li class="nav-item">
                        <a href="#home" class="nav-link active" aria-current="page">Trang chủ</a>
                    </li>
                    <li class="nav-item">
                        <a href="#tours" class="nav-link">Tours</a>
                    </li>
                    <li class="nav-item">
                        <a href="#hotels" class="nav-link">Khách sạn</a>
                    </li>
                    <li class="nav-item">
                        <a href="#destinations" class="nav-link">Điểm đến</a>
                    </li>
                    <li class="nav-item">
                        <a href="#about" class="nav-link">Về chúng tôi</a>
                    </li>
                    <li class="nav-item">
                        <a href="#contact" class="nav-link">Liên hệ</a>
                    </li>
                </ul>
                
                <!-- User Menu -->
                <div class="user-menu">
                    <c:choose>
                        <c:when test="${currentUser != null}">
                            <div class="user-dropdown">
                                <img src="${pageContext.request.contextPath}/uploads/avatars/${currentUser.avatarUrl != null ? currentUser.avatarUrl : 'default-avatar.jpg'}" 
                                     alt="Avatar" class="user-avatar" id="userAvatar">
                                <div class="user-dropdown-menu">
                                    <a href="${pageContext.request.contextPath}/profile" class="dropdown-item">
                                        <i class="fas fa-user"></i> Hồ sơ
                                    </a>
                                    <c:if test="${userRole == 'ADMINISTRATOR' || userRole == 'SERVICE_MANAGER'}">
                                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="dropdown-item">
                                            <i class="fas fa-cog"></i> Quản trị
                                        </a>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/logout" class="dropdown-item">
                                        <i class="fas fa-sign-out-alt"></i> Đăng xuất
                                    </a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="auth-buttons">
                                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline btn-sm">Đăng nhập</a>
                                <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary btn-sm">Đăng ký</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </nav>
            
            <!-- Mobile Menu Toggle -->
            <button class="mobile-menu-toggle" id="mobileMenuToggle" aria-label="Toggle mobile menu" aria-expanded="false" aria-controls="nav">
                <span class="sr-only">Menu</span>
                <span></span>
                <span></span>
                <span></span>
            </button>
        </div>
    </header>

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
                    Trải nghiệm du lịch tuyệt vời với các tour đa dạng, khách sạn cao cấp và dịch vụ chuyên nghiệp
                </p>
                <div class="hero-buttons animate-slide-up delay-400">
                    <a href="#tours" class="hero-cta hero-cta-primary">
                        <i class="fas fa-compass"></i>
                        Khám phá Tours
                    </a>
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
                    <h3 class="booking-widget-title">Đặt Tour Ngay</h3>
                    <p class="booking-widget-subtitle">Tìm kiếm và đặt tour phù hợp với bạn</p>
                </div>
                
                <form class="booking-form" id="bookingForm" role="search" aria-label="Tour booking search form">
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
                        <label class="booking-form-label" for="departureDate">Ngày khởi hành</label>
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
                    <div id="booking-help" class="sr-only">Tìm kiếm tours phù hợp với tiêu chí của bạn</div>
                </form>
            </div>
        </section>

        <!-- Section 3: Featured Tours -->
        <section id="tours" class="section tours-section" aria-labelledby="tours-heading">
            <div class="container">
                <div class="section-header animate-fade-in">
                    <h2 class="section-title" id="tours-heading">Tours Nổi Bật</h2>
                    <div class="section-divider"></div>
                    <p class="section-subtitle">
                        Khám phá những tour du lịch hấp dẫn nhất tại Sầm Sơn với giá cả hợp lý và dịch vụ chuyên nghiệp
                    </p>
                </div>
                
                <div class="tours-grid" role="list" aria-label="Featured tours">
                    <c:forEach var="tour" items="${homepageData.featuredTours}" varStatus="status">
                        <article class="tour-card animate-slide-up" style="animation-delay: ${status.index * 0.1}s" role="listitem">
                            <div class="tour-card-image">
                                <img src="${pageContext.request.contextPath}/uploads/${tour.featuredImage}" 
                                     alt="${tour.tourName}" loading="lazy">
                                <div class="tour-card-badge" aria-label="Featured tour">
                                    <i class="fas fa-star" aria-hidden="true"></i>
                                    Nổi bật
                                </div>
                            </div>
                            
                            <div class="tour-card-content">
                                <h3 class="tour-card-title">${tour.tourName}</h3>
                                <p class="tour-card-description">${tour.description}</p>
                                
                                <div class="tour-card-meta">
                                    <div class="tour-card-duration">
                                        <i class="fas fa-clock" aria-hidden="true"></i>
                                        <span>${tour.durationDays} ngày ${tour.durationNights} đêm</span>
                                    </div>
                                    <div class="tour-card-rating" aria-label="Rating: 4.8 out of 5 stars">
                                        <div class="tour-card-stars" role="img" aria-label="4.8 stars">
                                            <i class="fas fa-star" aria-hidden="true"></i>
                                            <i class="fas fa-star" aria-hidden="true"></i>
                                            <i class="fas fa-star" aria-hidden="true"></i>
                                            <i class="fas fa-star" aria-hidden="true"></i>
                                            <i class="fas fa-star" aria-hidden="true"></i>
                                        </div>
                                        <span class="rating-text">4.8</span>
                                    </div>
                                </div>
                                
                                <div class="tour-card-price">
                                    <span class="tour-card-price-current">
                                        <fmt:formatNumber value="${tour.basePrice}" type="currency" currencyCode="VND"/>
                                    </span>
                                    <span class="tour-card-price-original">
                                        <fmt:formatNumber value="${tour.basePrice * 1.2}" type="currency" currencyCode="VND"/>
                                    </span>
                                </div>
                                
                                <button class="tour-card-button" onclick="viewTourDetails(${tour.tourId})" aria-label="View details for ${tour.tourName}">
                                    <i class="fas fa-eye" aria-hidden="true"></i>
                                    <span>Xem chi tiết</span>
                                </button>
                            </div>
                        </article>
                    </c:forEach>
                </div>
                
                <div class="text-center animate-fade-in delay-800">
                    <a href="${pageContext.request.contextPath}/tours" class="btn btn-primary btn-lg" aria-label="View all available tours">
                        <i class="fas fa-compass" aria-hidden="true"></i>
                        <span>Xem tất cả tours</span>
                    </a>
                </div>
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
                                <img src="${pageContext.request.contextPath}/assets/images/hotels/hotel-${hotel.id}.jpg" 
                                     alt="${hotel.name}" loading="lazy">
                                <div class="hotel-card-rating">
                                    <i class="fas fa-star"></i>
                                    <span><fmt:formatNumber value="${hotel.rating}" maxFractionDigits="1"/></span>
                                </div>
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

        <!-- Section 7: Testimonials -->
        <section id="testimonials" class="section testimonials-section">
            <div class="container">
                <div class="section-header animate-fade-in">
                    <h2 class="section-title">Khách Hàng Nói Gì</h2>
                    <div class="section-divider"></div>
                    <p class="section-subtitle">
                        Những chia sẻ chân thực từ khách hàng đã trải nghiệm dịch vụ của chúng tôi
                    </p>
                </div>
                
                <div class="testimonials-slider" id="testimonialsSlider">
                    <div class="testimonials-container" id="testimonialsContainer">
                        <c:forEach var="testimonial" items="${homepageData.featuredTestimonials}">
                            <div class="testimonial-slide">
                                <div class="testimonial-card">
                                    <div class="testimonial-quote">
                                        "${testimonial.reviewText}"
                                    </div>
                                    
                                    <div class="testimonial-author">
                                        <img src="${pageContext.request.contextPath}/uploads/${testimonial.customerAvatar != null ? testimonial.customerAvatar : 'default-avatar.jpg'}" 
                                             alt="${testimonial.customerName}" class="testimonial-avatar">
                                        <div class="testimonial-info">
                                            <div class="testimonial-name">${testimonial.customerName}</div>
                                            <div class="testimonial-rating">
                                                <c:forEach begin="1" end="${testimonial.rating}">
                                                    <i class="fas fa-star"></i>
                                                </c:forEach>
                                            </div>
                                            <div class="testimonial-tour">Tour đã tham gia</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                
                <div class="testimonials-controls">
                    <button class="testimonial-prev" id="testimonialPrev">
                        <i class="fas fa-chevron-left"></i>
                    </button>
                    <button class="testimonial-next" id="testimonialNext">
                        <i class="fas fa-chevron-right"></i>
                    </button>
                </div>
            </div>
        </section>

        <!-- Section 8: Statistics -->
        <section id="statistics" class="section statistics-section">
            <div class="container">
                <div class="statistics-grid">
                    <div class="statistic-item animate-count-up">
                        <div class="statistic-number" data-target="${homepageData.totalTours}">0</div>
                        <div class="statistic-label">Tours</div>
                    </div>
                    <div class="statistic-item animate-count-up delay-200">
                        <div class="statistic-number" data-target="${homepageData.totalHotels}">0</div>
                        <div class="statistic-label">Khách sạn</div>
                    </div>
                    <div class="statistic-item animate-count-up delay-400">
                        <div class="statistic-number" data-target="${homepageData.totalTestimonials}">0</div>
                        <div class="statistic-label">Khách hàng hài lòng</div>
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
                        Nhận thông tin về tour mới, khuyến mãi đặc biệt và tin tức du lịch từ SamSon Travel
                    </p>
                    
                    <form class="newsletter-form" id="newsletterForm" role="form" aria-label="Newsletter subscription form">
                        <label for="newsletter-email" class="sr-only">Email address for newsletter</label>
                        <input type="email" class="newsletter-input" id="newsletter-email" placeholder="Nhập email của bạn" required aria-required="true">
                        <button type="submit" class="newsletter-button" aria-describedby="newsletter-help">
                            <i class="fas fa-paper-plane" aria-hidden="true"></i>
                            <span>Đăng ký</span>
                        </button>
                        <div id="newsletter-help" class="sr-only">Đăng ký nhận tin về tour mới và khuyến mãi</div>
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
    <footer id="contact" class="footer" role="contentinfo">
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>SamSon Travel</h3>
                    <p>Dịch vụ du lịch cao cấp tại Sầm Sơn với hơn 10 năm kinh nghiệm.</p>
                    <div class="footer-social" role="list" aria-label="Social media links">
                        <a href="#" class="footer-social-link" aria-label="Follow us on Facebook" role="listitem">
                            <i class="fab fa-facebook-f" aria-hidden="true"></i>
                        </a>
                        <a href="#" class="footer-social-link" aria-label="Follow us on Instagram" role="listitem">
                            <i class="fab fa-instagram" aria-hidden="true"></i>
                        </a>
                        <a href="#" class="footer-social-link" aria-label="Follow us on Twitter" role="listitem">
                            <i class="fab fa-twitter" aria-hidden="true"></i>
                        </a>
                        <a href="#" class="footer-social-link" aria-label="Subscribe to our YouTube channel" role="listitem">
                            <i class="fab fa-youtube" aria-hidden="true"></i>
                        </a>
                    </div>
                </div>
                
                <div class="footer-section">
                    <h3>Dịch vụ</h3>
                    <ul class="footer-links">
                        <li><a href="#tours">Tours du lịch</a></li>
                        <li><a href="#hotels">Khách sạn</a></li>
                        <li><a href="#destinations">Điểm đến</a></li>
                        <li><a href="#services">Dịch vụ</a></li>
                    </ul>
                </div>
                
                <div class="footer-section">
                    <h3>Hỗ trợ</h3>
                    <ul class="footer-links">
                        <li><a href="${pageContext.request.contextPath}/help">Trung tâm trợ giúp</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact">Liên hệ</a></li>
                        <li><a href="${pageContext.request.contextPath}/faq">Câu hỏi thường gặp</a></li>
                        <li><a href="${pageContext.request.contextPath}/terms">Điều khoản</a></li>
                    </ul>
                </div>
                
                <div class="footer-section">
                    <h3>Liên hệ</h3>
                    <div class="contact-info">
                        <div class="contact-item">
                            <i class="fas fa-map-marker-alt"></i>
                            <span>123 Đường Trần Phú, Sầm Sơn, Thanh Hóa</span>
                        </div>
                        <div class="contact-item">
                            <i class="fas fa-phone"></i>
                            <span>+84 123 456 789</span>
                        </div>
                        <div class="contact-item">
                            <i class="fas fa-envelope"></i>
                            <span>info@samsontravel.com</span>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="footer-bottom">
                <p>&copy; 2024 SamSon Travel. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>
    </footer>

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
