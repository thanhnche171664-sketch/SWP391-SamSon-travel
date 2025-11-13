<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ Offline - SamSon Travel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- CSS riêng -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/service-list.css">
    <style>
        /* CSS bổ sung cho trang chủ */
        .home-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 30px;
            margin-bottom: 30px;
        }
        
        .home-card {
            background: white;
            border-radius: 15px;
            padding: 40px;
            text-align: center;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
            cursor: pointer;
            text-decoration: none;
            color: inherit;
            display: block;
        }
        
        .home-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        
        .home-card-icon {
            width: 80px;
            height: 80px;
            margin: 0 auto 20px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2.5rem;
            color: white;
        }
        
        .home-card.room .home-card-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .home-card.service .home-card-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        .home-card h3 {
            font-size: 1.8rem;
            margin-bottom: 15px;
            color: #333;
        }
        
        .home-card p {
            color: #666;
            font-size: 1rem;
            line-height: 1.6;
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        
        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .stat-card-icon {
            font-size: 2.5rem;
            margin-bottom: 15px;
            color: #667eea;
        }
        
        .stat-card h4 {
            font-size: 2rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }
        
        .stat-card p {
            color: #666;
            font-size: 0.9rem;
        }
        
        .welcome-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 15px;
            margin-bottom: 30px;
            text-align: center;
        }
        
        .welcome-section h2 {
            font-size: 2rem;
            margin-bottom: 10px;
        }
        
        .welcome-section p {
            font-size: 1.1rem;
            opacity: 0.9;
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
            <a href="${pageContext.request.contextPath}/offline-home" class="active"><i class="fas fa-home"></i><span>Trang chủ</span></a>
<!--            <a href="AddBooking.jsp"><i class="fas fa-plus-circle"></i><span>Booking mới</span></a>-->
            <a href="room-list"><i class="fas fa-hotel"></i><span>Phòng</span></a>
            <a href="service-list"><i class="fas fa-concierge-bell"></i><span>Dịch vụ</span></a>
            <a href="${pageContext.request.contextPath}/offline-booking-list"><i class="fas fa-history"></i><span>Lịch sử booking</span></a>
            <a href="#"><i class="fas fa-headset"></i><span>Hỗ trợ</span></a>
        </nav>
    </aside>

    <!-- Main -->
    <main class="main-content">
        <header>
            <h1>Trang chủ Offline</h1>
            <div class="user-info">
                <div class="user-avatar">K</div>
                <span><b>Khanh</b> <small>(Lễ tân)</small></span>
            </div>
        </header>

        <section class="content">
            <!-- Welcome Section -->
            <div class="welcome-section">
                <h2><i class="fas fa-hotel"></i> Chào mừng đến hệ thống đặt phòng & dịch vụ</h2>
                <p>Hệ thống đặt phòng và dịch vụ offline - Phục vụ khách hàng tại quầy</p>
                <p style="margin-top: 10px; font-size: 0.95rem;">
                    <i class="fas fa-clock"></i> Thời gian hiện tại: <strong id="currentTime">--:--:--</strong>
                </p>
            </div>

            <!-- Main Cards -->
            <div class="home-cards">
                <!-- Đặt phòng -->
                <a href="${pageContext.request.contextPath}/room-list" class="home-card room">
                    <div class="home-card-icon">
                        <i class="fas fa-bed"></i>
                    </div>
                    <h3>Đặt Phòng</h3>
                    <p>Chọn phòng khách sạn, đặt phòng cho khách hàng và xử lý thanh toán. Hỗ trợ đặt nhiều phòng cùng lúc.</p>
                </a>
                
                <!-- Đặt dịch vụ -->
                <a href="${pageContext.request.contextPath}/service-list" class="home-card service">
                    <div class="home-card-icon">
                        <i class="fas fa-concierge-bell"></i>
                    </div>
                    <h3>Đặt Dịch Vụ</h3>
                    <p>Chọn dịch vụ ăn uống, spa & wellness và tạo hóa đơn dịch vụ. Không cần điền thông tin khách hàng.</p>
                </a>
            </div>

            <!-- Quick Actions -->
            <div class="content-header" style="margin-top: 30px;">
                <h3 style="margin-bottom: 20px; color: #333; font-size: 1.3rem;">
                    <i class="fas fa-bolt"></i> Thao tác nhanh
                </h3>
                <div class="filter-section">
                    <a href="${pageContext.request.contextPath}/room-list" 
                       class="filter-btn active" 
                       style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; text-decoration: none;">
                        <i class="fas fa-bed"></i> Đặt phòng mới
                    </a>
                    <a href="${pageContext.request.contextPath}/service-list" 
                       class="filter-btn active" 
                       style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; text-decoration: none;">
                        <i class="fas fa-concierge-bell"></i> Đặt dịch vụ
                    </a>
                    <a href="${pageContext.request.contextPath}/home" 
                       class="filter-btn" 
                       style="background: #6c757d; color: white; text-decoration: none;">
                        <i class="fas fa-globe"></i> Trang chủ công khai
                    </a>
                </div>
            </div>

            <!-- Stats Section -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-card-icon">
                        <i class="fas fa-calendar-check"></i>
                    </div>
                    <h4>Hôm nay</h4>
                    <p>Sẵn sàng phục vụ</p>
                </div>
                
                <div class="stat-card">
                    <div class="stat-card-icon">
                        <i class="fas fa-clock"></i>
                    </div>
                    <h4 id="currentTimeShort">--:--</h4>
                    <p>Thời gian hiện tại</p>
                </div>
                
                <div class="stat-card">
                    <div class="stat-card-icon">
                        <i class="fas fa-user-tie"></i>
                    </div>
                    <h4>Offline</h4>
                    <p>Chế độ đặt tại quầy</p>
                </div>
                
                <div class="stat-card">
                    <div class="stat-card-icon">
                        <i class="fas fa-check-circle"></i>
                    </div>
                    <h4>Hoạt động</h4>
                    <p>Hệ thống đang chạy</p>
                </div>
            </div>
        </section>
    </main>
</div>

<script>
    // Hiển thị thời gian hiện tại
    function updateTime() {
        const now = new Date();
        const timeString = now.toLocaleTimeString('vi-VN', { 
            hour: '2-digit', 
            minute: '2-digit',
            second: '2-digit'
        });
        const timeShortString = now.toLocaleTimeString('vi-VN', { 
            hour: '2-digit', 
            minute: '2-digit'
        });
        
        const timeElement = document.getElementById('currentTime');
        const timeShortElement = document.getElementById('currentTimeShort');
        
        if (timeElement) {
            timeElement.textContent = timeString;
        }
        if (timeShortElement) {
            timeShortElement.textContent = timeShortString;
        }
    }
    
    // Cập nhật thời gian mỗi giây
    updateTime();
    setInterval(updateTime, 1000);
    
    // Animation khi load trang
    document.addEventListener('DOMContentLoaded', function() {
        const cards = document.querySelectorAll('.home-card, .stat-card');
        cards.forEach((card, index) => {
            card.style.opacity = '0';
            card.style.transform = 'translateY(20px)';
            setTimeout(() => {
                card.style.transition = 'all 0.5s ease';
                card.style.opacity = '1';
                card.style.transform = 'translateY(0)';
            }, index * 100);
        });
    });
    
    // Xóa giỏ hàng khỏi sessionStorage nếu đã booking xong
    // (khi về trang chủ từ hóa đơn)
    if (typeof(Storage) !== "undefined") {
        const bookingCompleted = sessionStorage.getItem('booking_completed');
        if (bookingCompleted === 'true') {
            sessionStorage.removeItem('roomCart');
            sessionStorage.removeItem('serviceCart');
            sessionStorage.removeItem('checkinDate');
            sessionStorage.removeItem('checkoutDate');
            sessionStorage.removeItem('booking_completed');
            console.log('Đã xóa giỏ hàng khi về trang chủ');
        }
    }
</script>
</body>
</html>


