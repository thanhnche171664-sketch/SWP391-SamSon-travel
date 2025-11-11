<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - SamSon Travel</title>
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
        .container-glass {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
            border: 1px solid rgba(255, 255, 255, 0.18);
            width: 100%;
            max-width: 420px;
            animation: slideIn 0.8s ease-out;
        }
        @keyframes slideIn {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .logo {
            text-align: center;
            margin-bottom: 30px;
        }
        .logo h1 {
            color: white;
            font-size: 2.2rem;
            font-weight: bold;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        .logo p {
            color: rgba(255, 255, 255, 0.85);
            font-size: 1rem;
        }
        .form-floating { margin-bottom: 20px; }
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
        .form-floating > label { color: rgba(255, 255, 255, 0.85); }
        .btn-primary-gradient {
            background: linear-gradient(45deg, #ff6b6b, #ee5a24);
            border: none;
            border-radius: 10px;
            padding: 14px;
            font-size: 1.05rem;
            font-weight: bold;
            color: white;
            width: 100%;
            transition: all 0.3s ease;
            margin-top: 10px;
        }
        .btn-primary-gradient:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            background: linear-gradient(45deg, #ee5a24, #ff6b6b);
        }
        .alert {
            border-radius: 10px;
            border: none;
            margin-bottom: 20px;
            animation: fadeIn 0.5s ease-in;
        }
        @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
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
        .back-link {
            text-align: center;
            margin-top: 16px;
        }
        .back-link a {
            color: white;
            text-decoration: none;
            font-weight: bold;
        }
        .back-link a:hover { color: #ff6b6b; text-shadow: 0 0 10px rgba(255,107,107,0.5); }
    </style>
</head>
<body>
    <div class="container-glass">
        <div class="logo">
            <h1><i class="fas fa-umbrella-beach"></i> SamSon Travel</h1>
            <p>Nhập email để nhận hướng dẫn đặt lại mật khẩu</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert" aria-live="polite">
                <i class="fas fa-exclamation-triangle me-2"></i>${errorMessage}
            </div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert" aria-live="polite">
                <i class="fas fa-check-circle me-2"></i>${successMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/forgot-password/request" method="POST" novalidate>
            <div class="form-floating">
                <input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
                <label for="email"><i class="fas fa-envelope me-2"></i>Email</label>
            </div>
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <button type="submit" class="btn btn-primary-gradient">
                <i class="fas fa-paper-plane me-2"></i>Gửi hướng dẫn
            </button>
        </form>

        <div class="back-link">
            <p><a href="${pageContext.request.contextPath}/login"><i class="fas fa-arrow-left me-2"></i>Quay lại đăng nhập</a></p>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                alert.style.transition = 'opacity 0.5s ease-out';
                alert.style.opacity = '0';
                setTimeout(function() { alert.remove(); }, 500);
            });
        }, 5000);
    </script>
</body>
</html>



