<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết Dịch vụ Wellness</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body style="background-color:#f8fafc;">
<div class="container mt-5">
    <div class="card shadow-lg p-4 rounded-4">
        <h3 class="text-center text-info mb-4"> Thông tin chi tiết Dịch vụ Wellness</h3>

        <div class="row">
            <div class="col-md-6">
                <h5 class="fw-bold">${ws.serviceName}</h5>
                <p>${ws.description}</p>

                <p><strong>Giá cơ bản:</strong> 
                    <fmt:formatNumber value="${ws.basePrice}" type="currency" currencySymbol="₫"/>
                </p>
                <p><strong>Thời lượng:</strong> ${ws.durationMinutes} phút</p>
                <p><strong>Sức chứa:</strong> ${ws.capacity} người</p>
                <p><strong>Trạng thái:</strong> 
                    <c:choose>
                        <c:when test="${ws.status eq 'active'}"><span class="text-success">Hoạt động</span></c:when>
                        <c:otherwise><span class="text-danger">Ngừng</span></c:otherwise>
                    </c:choose>
                </p>
            </div>
            <div class="col-md-6 text-center">
                <img src="${ws.imageUrl != null ? ws.imageUrl : '../assets/default_wellness.jpg'}" 
                     class="img-fluid rounded-4" style="max-height: 300px;">
            </div>
        </div>

        <div class="text-center mt-4">
            <a href="wellness-list" class="btn btn-outline-primary px-4">⬅ Quay lại danh sách</a>
        </div>
    </div>
</div>
</body>
</html>
