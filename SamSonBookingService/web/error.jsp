<%-- 
    Document   : error.jsp
    Created on : Oct 28, 2025
    Author     : trant
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lỗi Hệ Thống - Booking Travel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .error-box {
            max-width: 600px;
            margin: 100px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
            padding: 40px 30px;
        }
        .error-title {
            font-size: 1.8rem;
            font-weight: 700;
            color: #dc3545;
        }
        .error-message {
            background-color: #fff5f5;
            border-left: 4px solid #dc3545;
            padding: 12px 15px;
            margin: 15px 0;
            border-radius: 6px;
            color: #b02a37;
            font-size: 1rem;
        }
        .back-btn {
            background-color: #198754;
            color: #fff;
            border: none;
            padding: 10px 18px;
            border-radius: 6px;
            transition: 0.2s ease-in-out;
        }
        .back-btn:hover {
            background-color: #157347;
        }
    </style>
</head>
<body>
    <div class="error-box text-center">
        <h1 class="error-title">⚠️ Đã xảy ra lỗi!</h1>
        <p class="mt-3">Rất tiếc, hệ thống gặp sự cố khi xử lý yêu cầu của bạn.</p>

        <% 
            String errorMsg = (String) request.getAttribute("error");
            if (errorMsg != null && !errorMsg.isEmpty()) {
        %>
            <div class="error-message">
                <strong>Chi tiết lỗi:</strong><br>
                <%= errorMsg %>
            </div>
        <% } else { %>
            <div class="error-message">
                Không có thông tin chi tiết. Vui lòng thử lại sau.
            </div>
        <% } %>

        <button class="back-btn mt-4" onclick="history.back()">⬅ Quay lại</button>
    </div>
</body>
</html>
