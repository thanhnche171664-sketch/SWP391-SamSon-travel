<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết dịch vụ - ${ws.serviceName}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8fafc;
                font-family: 'Segoe UI', sans-serif;
            }
            .container {
                max-width: 1000px;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 3px 10px rgba(0,0,0,0.1);
                background-color: #fff;
                padding: 25px;
            }
            .service-title {
                color: #007b5e;
                font-weight: 700;
            }
            .price-tag {
                font-size: 22px;
                color: #ff6600;
                font-weight: bold;
            }
            .desc {
                font-size: 16px;
                color: #555;
            }
            .info-list li {
                font-size: 15px;
                padding: 4px 0;
            }
            .btn-back {
                background-color: #6c757d;
                color: white;
            }
            .btn-back:hover {
                background-color: #5c636a;
            }
        </style>
    </head>
    <body>

        <div class="container mt-5 mb-5">
            <div class="card">
                <h2 class="service-title mb-3">${ws.serviceName}</h2>
                <p class="price-tag">${ws.basePrice} đ</p>
                <p class="desc">${ws.description}</p>

                <ul class="list-unstyled info-list mt-3">
                    <li><strong>Thời lượng:</strong> ${ws.durationMinutes} phút</li>
                    <li><strong>Giờ hoạt động:</strong> ${ws.operatingHours}</li>
                    <li><strong>Sức chứa tối đa:</strong> ${ws.capacity} người</li>
                    <li><strong>Trạng thái:</strong>
                        <span class="${ws.status eq 'ACTIVE' ? 'text-success' : 'text-danger'}">
                            ${ws.status}
                        </span>
                    </li>
                    <li><strong>Khách sạn ID:</strong> ${ws.hotelId}</li>
                    <li><strong>Loại dịch vụ ID:</strong> ${ws.categoryId}</li>
                    <li><strong>Ngày tạo:</strong> ${ws.createdAt}</li>
                    <li><strong>Cập nhật lần cuối:</strong> ${ws.updatedAt}</li>
                </ul>

                <div class="mt-4">
                    <a href="user-wellness" class="btn btn-back">← Quay lại danh sách</a>
                </div>
            </div>
        </div>

    </body>
</html>
