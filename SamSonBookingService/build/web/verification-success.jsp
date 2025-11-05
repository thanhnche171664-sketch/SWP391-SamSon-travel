<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực thành công - SamSon Travel</title>
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
        
        .success-container {
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
        
        .success-icon {
            font-size: 5rem;
            color: #28a745;
            margin-bottom: 20px;
            animation: checkmark 1s ease-in-out;
        }
        
        @keyframes checkmark {
            0% {
                transform: scale(0);
                opacity: 0;
            }
            50% {
                transform: scale(1.2);
                opacity: 1;
            }
            100% {
                transform: scale(1);
                opacity: 1;
            }
        }
        
        .success-title {
            color: white;
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 20px;
        }
        
        .success-message {
            color: rgba(255, 255, 255, 0.9);
            font-size: 1.2rem;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        
        .btn-login {
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
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            color: white;
        }
        
        .countdown {
            color: #ffd700;
            font-size: 1.1rem;
            margin-top: 20px;
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
        
        .alert-success {
            background: rgba(40, 167, 69, 0.2);
            color: #28a745;
            border-left: 4px solid #28a745;
        }
        
        .alert-danger {
            background: rgba(220, 53, 69, 0.2);
            color: #ff6b6b;
            border-left: 4px solid #ff6b6b;
        }
        
        .confetti {
            position: absolute;
            width: 10px;
            height: 10px;
            background: #ffd700;
            animation: confetti-fall 3s linear infinite;
        }
        
        @keyframes confetti-fall {
            0% {
                transform: translateY(-100vh) rotate(0deg);
                opacity: 1;
            }
            100% {
                transform: translateY(100vh) rotate(720deg);
                opacity: 0;
            }
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="logo">
            <h1><i class="fas fa-umbrella-beach"></i> SamSon Travel</h1>
            <p>Chào mừng bạn đến với chúng tôi!</p>
        </div>
        
        <!-- Success Message -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                ${successMessage}
            </div>
        </c:if>
        
        <!-- Error Message -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                ${errorMessage}
            </div>
        </c:if>
        
        <div class="success-icon">
            <i class="fas fa-check-circle"></i>
        </div>
        
        <h2 class="success-title">Xác thực thành công!</h2>
        
        <div class="success-message">
            <p>Tài khoản của bạn đã được kích hoạt thành công.</p>
            <p>Bây giờ bạn có thể đăng nhập và bắt đầu khám phá những chuyến du lịch tuyệt vời!</p>
        </div>
        
        <div class="countdown" id="countdown">
            Tự động chuyển đến trang đăng nhập sau: <span id="timer">5</span> giây
        </div>
        
        <div style="margin-top: 30px;">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-login" id="loginBtn">
                <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập ngay
            </a>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Create confetti effect
        function createConfetti() {
            const colors = ['#ffd700', '#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57'];
            
            for (let i = 0; i < 50; i++) {
                setTimeout(() => {
                    const confetti = document.createElement('div');
                    confetti.className = 'confetti';
                    confetti.style.left = Math.random() * 100 + '%';
                    confetti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
                    confetti.style.animationDelay = Math.random() * 3 + 's';
                    confetti.style.animationDuration = (Math.random() * 3 + 2) + 's';
                    document.body.appendChild(confetti);
                    
                    setTimeout(() => {
                        confetti.remove();
                    }, 5000);
                }, i * 100);
            }
        }
        
        // Start confetti effect
        createConfetti();
        
        // Countdown timer
        let timeLeft = 5;
        const timerElement = document.getElementById('timer');
        const countdownElement = document.getElementById('countdown');
        const loginBtn = document.getElementById('loginBtn');
        
        const countdown = setInterval(function() {
            timeLeft--;
            timerElement.textContent = timeLeft;
            
            if (timeLeft <= 0) {
                clearInterval(countdown);
                countdownElement.style.display = 'none';
                window.location.href = loginBtn.href;
            }
        }, 1000);
        
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
    </script>
</body>
</html>
