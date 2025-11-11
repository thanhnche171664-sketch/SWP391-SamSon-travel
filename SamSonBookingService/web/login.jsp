<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - SamSon Travel</title>
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
        
        .login-container {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
            border: 1px solid rgba(255, 255, 255, 0.18);
            width: 100%;
            max-width: 400px;
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
            text-align: center;
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
        
        .form-floating {
            margin-bottom: 20px;
        }
        
        .form-control {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
            color: white;
            border-radius: 10px;
            padding: 15px;
        }
        
        .form-control:focus {
            background: rgba(255, 255, 255, 0.2);
            border-color: rgba(255, 255, 255, 0.5);
            color: white;
            box-shadow: 0 0 0 0.2rem rgba(255, 255, 255, 0.25);
        }
        
        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.7);
        }
        
        .form-floating > label {
            color: rgba(255, 255, 255, 0.8);
        }
        
        .btn-login {
            background: linear-gradient(45deg, #ff6b6b, #ee5a24);
            border: none;
            border-radius: 10px;
            padding: 15px;
            font-size: 1.1rem;
            font-weight: bold;
            color: white;
            width: 100%;
            transition: all 0.3s ease;
            margin-bottom: 20px;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            background: linear-gradient(45deg, #ee5a24, #ff6b6b);
        }
        
        .form-check {
            margin-bottom: 20px;
        }
        
        .form-check-input:checked {
            background-color: #ff6b6b;
            border-color: #ff6b6b;
        }
        
        .form-check-label {
            color: rgba(255, 255, 255, 0.8);
        }
        
        .register-link {
            text-align: center;
            margin-top: 20px;
        }
        
        .register-link a {
            color: white;
            text-decoration: none;
            font-weight: bold;
            transition: all 0.3s ease;
        }
        
        .register-link a:hover {
            color: #ff6b6b;
            text-shadow: 0 0 10px rgba(255, 107, 107, 0.5);
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
        
        .alert-success {
            background: rgba(40, 167, 69, 0.2);
            color: #28a745;
            border-left: 4px solid #28a745;
        }
        
        .password-toggle {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: rgba(255, 255, 255, 0.7);
            cursor: pointer;
            z-index: 10;
        }
        
        .password-toggle:hover {
            color: white;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="logo">
            <h1><i class="fas fa-umbrella-beach"></i> SamSon Travel</h1>
            <p>Đăng nhập để khám phá thế giới</p>
        </div>
        
        <!-- Error/Success Messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                ${errorMessage}
            </div>
        </c:if>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                ${successMessage}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/login" method="POST">
            <div class="form-floating">
                <input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
                <label for="email"><i class="fas fa-envelope me-2"></i>Email</label>
            </div>
            
            <div class="form-floating position-relative">
                <input type="password" class="form-control" id="password" name="password" placeholder="Mật khẩu" required>
                <label for="password"><i class="fas fa-lock me-2"></i>Mật khẩu</label>
                <button type="button" class="password-toggle" onclick="togglePassword()">
                    <i class="fas fa-eye" id="toggleIcon"></i>
                </button>
            </div>
            
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="rememberMe" name="rememberMe">
                <label class="form-check-label" for="rememberMe">
                    Ghi nhớ đăng nhập
                </label>
            </div>
            
            <button type="submit" class="btn btn-login">
                <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập
            </button>
        </form>
        
        <div class="register-link">
            <p><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a></p>
            <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a></p>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function togglePassword() {
            const passwordField = document.getElementById('password');
            const toggleIcon = document.getElementById('toggleIcon');
            
            if (passwordField.type === 'password') {
                passwordField.type = 'text';
                toggleIcon.classList.remove('fa-eye');
                toggleIcon.classList.add('fa-eye-slash');
            } else {
                passwordField.type = 'password';
                toggleIcon.classList.remove('fa-eye-slash');
                toggleIcon.classList.add('fa-eye');
            }
        }
        
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
        
        // Form validation
        document.querySelector('form').addEventListener('submit', function(e) {
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            if (!email || !password) {
                e.preventDefault();
                alert('Vui lòng nhập đầy đủ thông tin');
                return;
            }
            
            // Show loading state
            const submitBtn = document.querySelector('.btn-login');
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Đang đăng nhập...';
            submitBtn.disabled = true;
        });
    </script>
</body>
</html>
