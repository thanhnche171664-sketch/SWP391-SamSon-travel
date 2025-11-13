<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu thành công - SamSon Travel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .box {
            background: rgba(255,255,255,0.1); backdrop-filter: blur(10px); border-radius: 20px; padding: 40px;
            box-shadow: 0 8px 32px 0 rgba(31,38,135,0.37); border: 1px solid rgba(255,255,255,0.18); width: 100%; max-width: 520px;
            text-align: center;
        }
        h1 { color: white; font-weight: 700; margin-bottom: 10px; }
        p { color: rgba(255,255,255,0.9); }
        a.btn {
            background: linear-gradient(45deg,#00b894,#00a085); border: none; color: white; font-weight: 600; padding: 12px 20px; border-radius: 10px;
        }
        a.btn:hover { opacity: .95; }
    </style>
</head>
<body>
    <div class="box">
        <h1><i class="fas fa-check-circle me-2"></i>Đặt lại mật khẩu thành công</h1>
        <p>Bạn có thể đăng nhập với mật khẩu mới ngay bây giờ.</p>
        <a class="btn" href="${pageContext.request.contextPath}/login"><i class="fas fa-sign-in-alt me-2"></i>Đăng nhập</a>
    </div>
</body>
</html>



