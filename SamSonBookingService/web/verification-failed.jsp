<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực thất bại - SamSon Travel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .error-container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
            border: 1px solid rgba(255, 255, 255, 0.18);
            width: 100%;
            max-width: 500px;
            text-align: center;
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
        
        .logo {
            margin-bottom: 30px;
        }
        
        .logo h1 {
            color: white;
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        
        .logo p {
            color: rgba(255, 255, 255, 0.8);
            font-size: 1.1rem;
        }
        
        .error-icon {
            font-size: 5rem;
            color: #ff6b6b;
            margin-bottom: 20px;
            animation: shake 0.5s ease-in-out;
        }
        
        @keyframes shake {
            0%, 100% {
                transform: translateX(0);
            }
            25% {
                transform: translateX(-5px);
            }
            75% {
                transform: translateX(5px);
            }
        }
        
        .error-title {
            color: white;
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 20px;
        }
        
        .error-message {
            color: rgba(255, 255, 255, 0.9);
            font-size: 1.2rem;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        
        .btn-register {
            background: linear-gradient(45deg, #28a745, #20c997);
            border: none;
            border-radius: 10px;
            padding: 15px 30px;
            font-size: 1.1rem;
            font-weight: bold;
            color: white;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            margin-right: 15px;
        }
        
        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            color: white;
        }
        
        .btn-login {
            background: linear-gradient(45deg, #667eea, #764ba2);
            border: none;
            border-radius: 10px;
            padding: 15px 30px;
            font-size: 1.1rem;
            font-weight: bold;
            color: white;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            color: white;
        }
        
        .alert {
            border-radius: 10px;
            border: none;
            margin-bottom: 20px;
            animation: fadeIn 0.5s ease-in;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        
        .alert-danger {
            background: rgba(220, 53, 69, 0.2);
            color: #ff6b6b;
            border-left: 4px solid #ff6b6b;
        }
        
        .possible-reasons {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
            text-align: left;
        }
        
        .possible-reasons h4 {
            color: white;
            margin-bottom: 15px;
            font-size: 1.2rem;
        }
        
        .possible-reasons ul {
            color: rgba(255, 255, 255, 0.8);
            margin: 0;
            padding-left: 20px;
        }
        
        .possible-reasons li {
            margin-bottom: 8px;
        }
        
        .highlight {
            color: #ffd700;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="logo">
            <h1><i class="fas fa-umbrella-beach"></i> SamSon Travel</h1>
            <p>Xác thực không thành công</p>
        </div>
        
        <!-- Error Message -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                ${errorMessage}
            </div>
        </c:if>
        
        <div class="error-icon">
            <i class="fas fa-times-circle"></i>
        </div>
        
        <h2 class="error-title">Xác thực thất bại!</h2>
        
        <div class="error-message">
            <p>Rất tiếc, chúng tôi không thể xác thực tài khoản của bạn.</p>
            <p>Liên kết xác thực có thể đã hết hạn hoặc không hợp lệ.</p>
        </div>
        
        <div class="possible-reasons">
            <h4><i class="fas fa-info-circle me-2"></i>Nguyên nhân có thể:</h4>
            <ul>
                <li>Liên kết xác thực đã <span class="highlight">hết hạn</span> (24 giờ)</li>
                <li>Liên kết đã được <span class="highlight">sử dụng</span> trước đó</li>
                <li>Email xác thực bị <span class="highlight">hỏng</span> hoặc không đúng</li>
                <li>Tài khoản đã được <span class="highlight">kích hoạt</span> rồi</li>
            </ul>
        </div>
        
        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/register" class="btn btn-register">
                <i class="fas fa-user-plus me-2"></i>Đăng ký lại
            </a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-login">
                <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập
            </a>
        </div>
        
        <div style="margin-top: 20px; color: rgba(255, 255, 255, 0.7); font-size: 0.9rem;">
            <p>Nếu bạn gặp vấn đề liên tục, vui lòng liên hệ với chúng tôi qua email: <span class="highlight">support@samsontravel.com</span></p>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Auto-hide alerts after 5 seconds
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                alert.style.transition = 'opacity 0.5s ease-out';
                alert.style.opacity = '0';
                setTimeout(function() {
                    alert.remove();
                }, 500);
            });
        }, 5000);
        
        // Add some interactive effects
        document.querySelectorAll('.btn-register, .btn-login').forEach(function(btn) {
            btn.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-2px) scale(1.05)';
            });
            
            btn.addEventListener('mouseleave', function() {
                this.style.transform = 'translateY(0) scale(1)';
            });
        });
    </script>
</body>
</html>
