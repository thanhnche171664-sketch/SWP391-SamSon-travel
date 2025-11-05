<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - SamSon Travel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .dashboard-container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
            border: 1px solid rgba(255, 255, 255, 0.18);
            width: 100%;
            max-width: 800px;
            margin: 50px auto;
            animation: slideIn 0.8s ease-out;
        }
        
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .welcome-header {
            text-align: center;
            margin-bottom: 40px;
        }
        
        .welcome-header h1 {
            color: white;
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        
        .welcome-header p {
            color: rgba(255, 255, 255, 0.8);
            font-size: 1.2rem;
        }
        
        .user-info {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 30px;
            border: 1px solid rgba(255, 255, 255, 0.2);
        }
        
        .user-info h3 {
            color: white;
            margin-bottom: 20px;
            font-size: 1.5rem;
        }
        
        .info-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .info-item:last-child {
            border-bottom: none;
        }
        
        .info-label {
            color: rgba(255, 255, 255, 0.8);
            font-weight: 500;
        }
        
        .info-value {
            color: white;
            font-weight: bold;
        }
        
        .role-badge {
            background: linear-gradient(45deg, #28a745, #20c997);
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: bold;
        }
        
        .action-buttons {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        
        .action-btn {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
            border-radius: 15px;
            padding: 20px;
            text-align: center;
            text-decoration: none;
            color: white;
            transition: all 0.3s ease;
            backdrop-filter: blur(5px);
        }
        
        .action-btn:hover {
            background: rgba(255, 255, 255, 0.2);
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            color: white;
        }
        
        .action-btn i {
            font-size: 2rem;
            margin-bottom: 10px;
            display: block;
        }
        
        .action-btn h4 {
            margin-bottom: 10px;
            font-size: 1.2rem;
        }
        
        .action-btn p {
            margin: 0;
            color: rgba(255, 255, 255, 0.8);
            font-size: 0.9rem;
        }
        
        .logout-btn {
            background: linear-gradient(45deg, #ff6b6b, #ee5a24);
            border: none;
            border-radius: 10px;
            padding: 12px 25px;
            color: white;
            font-weight: bold;
            text-decoration: none;
            transition: all 0.3s ease;
            display: inline-block;
            margin-top: 20px;
        }
        
        .logout-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            color: white;
        }
        
        .status-active {
            color: #28a745;
            font-weight: bold;
        }
        
        .status-inactive {
            color: #ffc107;
            font-weight: bold;
        }
        
        .status-banned {
            color: #dc3545;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <div class="welcome-header">
            <h1><i class="fas fa-umbrella-beach"></i> SamSon Travel</h1>
            <p>Chào mừng bạn đến với hệ thống quản lý du lịch!</p>
        </div>
        
        <c:if test="${not empty user}">
            <div class="user-info">
                <h3><i class="fas fa-user-circle me-2"></i>Thông tin tài khoản</h3>
                
                <div class="info-item">
                    <span class="info-label">Họ và tên:</span>
                    <span class="info-value">${user.name}</span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Email:</span>
                    <span class="info-value">${user.email}</span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Số điện thoại:</span>
                    <span class="info-value">${user.phone}</span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Giới tính:</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${user.gender == 'MALE'}">Nam</c:when>
                            <c:when test="${user.gender == 'FEMALE'}">Nữ</c:when>
                            <c:otherwise>Khác</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Địa chỉ:</span>
                    <span class="info-value">${user.address}</span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Vai trò:</span>
                    <span class="role-badge">${user.role.roleName}</span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Trạng thái:</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${user.status == 'ACTIVE'}">
                                <span class="status-active"><i class="fas fa-check-circle me-1"></i>Hoạt động</span>
                            </c:when>
                            <c:when test="${user.status == 'INACTIVE'}">
                                <span class="status-inactive"><i class="fas fa-clock me-1"></i>Chưa kích hoạt</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-banned"><i class="fas fa-ban me-1"></i>Bị khóa</span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
                
                <div class="info-item">
                    <span class="info-label">Ngày tạo:</span>
                    <span class="info-value">${user.createdAt}</span>
                </div>
            </div>
            
            <div class="action-buttons">
                <c:choose>
                    <c:when test="${user.role.roleName == 'Administrator'}">
                        <a href="#" class="action-btn">
                            <i class="fas fa-cogs"></i>
                            <h4>Quản trị hệ thống</h4>
                            <p>Quản lý người dùng và cấu hình</p>
                        </a>
                        <a href="#" class="action-btn">
                            <i class="fas fa-chart-bar"></i>
                            <h4>Báo cáo thống kê</h4>
                            <p>Xem báo cáo và phân tích</p>
                        </a>
                    </c:when>
                    <c:when test="${user.role.roleName == 'Service Manager'}">
                        <a href="#" class="action-btn">
                            <i class="fas fa-concierge-bell"></i>
                            <h4>Quản lý dịch vụ</h4>
                            <p>Quản lý các dịch vụ du lịch</p>
                        </a>
                        <a href="#" class="action-btn">
                            <i class="fas fa-calendar-alt"></i>
                            <h4>Lịch trình</h4>
                            <p>Quản lý lịch trình tour</p>
                        </a>
                    </c:when>
                    <c:when test="${user.role.roleName == 'Hotel Manager'}">
                        <a href="#" class="action-btn">
                            <i class="fas fa-bed"></i>
                            <h4>Quản lý khách sạn</h4>
                            <p>Quản lý phòng và dịch vụ</p>
                        </a>
                        <a href="#" class="action-btn">
                            <i class="fas fa-users"></i>
                            <h4>Khách hàng</h4>
                            <p>Quản lý thông tin khách hàng</p>
                        </a>
                    </c:when>
                    <c:when test="${user.role.roleName == 'Customer'}">
                        <a href="#" class="action-btn">
                            <i class="fas fa-search"></i>
                            <h4>Tìm kiếm tour</h4>
                            <p>Khám phá các tour du lịch</p>
                        </a>
                        <a href="#" class="action-btn">
                            <i class="fas fa-shopping-cart"></i>
                            <h4>Đặt tour</h4>
                            <p>Đặt tour và thanh toán</p>
                        </a>
                        <a href="#" class="action-btn">
                            <i class="fas fa-history"></i>
                            <h4>Lịch sử đặt tour</h4>
                            <p>Xem lịch sử đặt tour</p>
                        </a>
                    </c:when>
                    <c:when test="${user.role.roleName == 'Front Office'}">
                        <a href="#" class="action-btn">
                            <i class="fas fa-clipboard-check"></i>
                            <h4>Check-in/Check-out</h4>
                            <p>Quản lý check-in khách hàng</p>
                        </a>
                        <a href="#" class="action-btn">
                            <i class="fas fa-headset"></i>
                            <h4>Hỗ trợ khách hàng</h4>
                            <p>Hỗ trợ và tư vấn</p>
                        </a>
                    </c:when>
                </c:choose>
                
                <a href="${pageContext.request.contextPath}/profile" class="action-btn">
                    <i class="fas fa-user-edit"></i>
                    <h4>Cập nhật thông tin</h4>
                    <p>Chỉnh sửa thông tin cá nhân</p>
                </a>
            </div>
            
            <div style="text-align: center;">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                    <i class="fas fa-sign-out-alt me-2"></i>Đăng xuất
                </a>
            </div>
        </c:if>
        
        <c:if test="${empty user}">
            <div class="alert alert-warning" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                Bạn cần đăng nhập để truy cập dashboard.
            </div>
            <div style="text-align: center;">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                    <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập
                </a>
            </div>
        </c:if>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Add some interactive effects
        document.querySelectorAll('.action-btn').forEach(function(btn) {
            btn.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-5px) scale(1.02)';
            });
            
            btn.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0) scale(1)';
            });
        });
        
        // Auto-refresh user info every 30 seconds (optional)
        setInterval(function() {
            // This could be used to refresh user data if needed
        }, 30000);
    </script>
</body>
</html>
