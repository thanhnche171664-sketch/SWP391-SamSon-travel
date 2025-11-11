<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - SamSon Travel</title>
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
        @keyframes slideIn { from { opacity: 0; transform: translateY(30px); } to { opacity: 1; transform: translateY(0); } }
        .logo { text-align: center; margin-bottom: 30px; }
        .logo h1 { color: white; font-size: 2.2rem; font-weight: bold; margin-bottom: 10px; text-shadow: 2px 2px 4px rgba(0,0,0,0.3); }
        .logo p { color: rgba(255,255,255,0.85); font-size: 1rem; }
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
        .password-toggle {
            position: absolute; right: 15px; top: 1.15rem;
            background: none; border: none; color: rgba(255,255,255,0.7); cursor: pointer; z-index: 10;
        }
        .password-toggle:hover { color: white; }
        .btn-primary-gradient {
            background: linear-gradient(45deg, #ff6b6b, #ee5a24);
            border: none; border-radius: 10px; padding: 14px; font-size: 1.05rem; font-weight: bold; color: white; width: 100%;
            transition: all 0.3s ease; margin-top: 10px;
        }
        .btn-primary-gradient:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(0,0,0,0.3); background: linear-gradient(45deg, #ee5a24, #ff6b6b); }
        .alert { border-radius: 10px; border: none; margin-bottom: 20px; animation: fadeIn 0.5s ease-in; }
        @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
        .alert-danger { background: rgba(220, 53, 69, 0.2); color: #ff6b6b; border-left: 4px solid #ff6b6b; }
        .policy small { color: rgba(255,255,255,0.85); }
        .meter { height: 6px; background: rgba(255,255,255,0.2); border-radius: 4px; overflow: hidden; margin-top: 6px; }
        .meter > div { height: 100%; width: 0%; background: linear-gradient(90deg,#ff4d4f,#fa8c16,#52c41a); transition: width .3s ease; }
    </style>
</head>
<body>
    <div class="container-glass">
        <div class="logo">
            <h1><i class="fas fa-umbrella-beach"></i> SamSon Travel</h1>
            <p>Tạo mật khẩu mới cho tài khoản của bạn</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert" aria-live="polite">
                <i class="fas fa-exclamation-triangle me-2"></i>${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/reset-password" method="POST" novalidate>
            <div class="form-floating position-relative">
                <input type="password" class="form-control" id="password" name="password" placeholder="Mật khẩu mới" required>
                <label for="password"><i class="fas fa-lock me-2"></i>Mật khẩu mới</label>
                <button type="button" class="password-toggle" onclick="toggle('password','t1')"><i class="fas fa-eye" id="t1"></i></button>
                <div class="meter"><div id="meter"></div></div>
                <div class="policy mt-2">
                    <small>Ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.</small>
                </div>
            </div>
            <div class="form-floating position-relative">
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu" required>
                <label for="confirmPassword"><i class="fas fa-lock me-2"></i>Nhập lại mật khẩu</label>
                <button type="button" class="password-toggle" onclick="toggle('confirmPassword','t2')"><i class="fas fa-eye" id="t2"></i></button>
            </div>
            <input type="hidden" name="token" value="${token}"/>
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <button type="submit" class="btn btn-primary-gradient">
                <i class="fas fa-key me-2"></i>Đặt lại mật khẩu
            </button>
        </form>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function toggle(id, iconId){
            const f = document.getElementById(id);
            const i = document.getElementById(iconId);
            if(f.type === 'password'){ f.type = 'text'; i.classList.remove('fa-eye'); i.classList.add('fa-eye-slash'); }
            else { f.type = 'password'; i.classList.remove('fa-eye-slash'); i.classList.add('fa-eye'); }
        }
        const pwd = document.getElementById('password');
        const meter = document.getElementById('meter');
        function score(p){
            let s=0;
            if(p.length>=8) s+=25;
            if(/[A-Z]/.test(p)) s+=25;
            if(/[a-z]/.test(p)) s+=20;
            if(/[0-9]/.test(p)) s+=15;
            if(/[!@#$%^&*()_+\-=\[\]{}|;:,.<>?]/.test(p)) s+=15;
            return Math.min(100,s);
        }
        pwd.addEventListener('input', function(){
            const v = score(pwd.value);
            meter.style.width = v + '%';
        });
    </script>
</body>
</html>


