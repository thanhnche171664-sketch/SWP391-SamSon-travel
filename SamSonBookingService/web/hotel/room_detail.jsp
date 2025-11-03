<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Phòng - Hotel Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .main-content {
            margin-left: 250px;
            padding: 30px;
            min-height: 100vh;
        }
        
        .page-header {
            background: white;
            padding: 25px 30px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            margin-bottom: 25px;
        }
        
        .detail-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            margin-bottom: 25px;
        }
        
        .info-item {
            padding: 15px;
            border-bottom: 1px solid #e9ecef;
        }
        
        .info-item:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            color: #6c757d;
            margin-bottom: 5px;
        }
        
        .info-value {
            font-size: 18px;
            color: #212529;
        }
        
        .room-images-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 15px;
            margin-top: 15px;
        }
        
        .room-image-item {
            position: relative;
            height: 200px;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        
        .room-image-item:hover {
            transform: scale(1.02);
        }
        
        .room-image-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .image-primary-badge {
            position: absolute;
            top: 10px;
            left: 10px;
            background: rgba(255, 193, 7, 0.95);
            color: #000;
            padding: 5px 12px;
            border-radius: 5px;
            font-size: 13px;
            font-weight: 600;
        }
        
        .badge-status {
            padding: 6px 12px;
            border-radius: 6px;
            font-weight: 600;
        }
        
        .badge-available {
            background-color: #d4edda;
            color: #155724;
        }
        
        .badge-full {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <jsp:include page="/hotel/components/sidebar.jsp" />
    
    <div class="main-content">
        <div class="page-header">
            <div class="d-flex justify-content-between align-items-center">
                <h2><i class="bi bi-door-open"></i> Chi Tiết Phòng</h2>
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/hotel/room/edit?id=${room.id}&hotelId=${room.hotelId}" 
                       class="btn btn-warning">
                        <i class="bi bi-pencil"></i> Sửa
                    </a>
                    <a href="${pageContext.request.contextPath}/hotel/detail?id=${room.hotelId}" 
                       class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Quay lại
                    </a>
                </div>
            </div>
        </div>
        
        <!-- Room Information -->
        <div class="detail-card">
            <h4 class="mb-4"><i class="bi bi-info-circle"></i> Thông tin phòng</h4>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="info-item">
                        <div class="info-label">Loại phòng</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${room.roomType == 'single'}">
                                    <i class="bi bi-person"></i> Phòng Đơn (Single)
                                </c:when>
                                <c:when test="${room.roomType == 'double'}">
                                    <i class="bi bi-people"></i> Phòng Đôi (Double)
                                </c:when>
                                <c:when test="${room.roomType == 'dormitory'}">
                                    <i class="bi bi-people-fill"></i> Phòng Tập Thể (Dormitory)
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div class="info-item">
                        <div class="info-label">Giá phòng</div>
                        <div class="info-value text-primary">
                            <fmt:formatNumber value="${room.price}" pattern="#,###" /> VNĐ
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div class="info-item">
                        <div class="info-label">Tổng số phòng</div>
                        <div class="info-value">
                            <i class="bi bi-door-closed"></i> ${room.totalRooms} phòng
                        </div>
                    </div>
                </div>
                
                <div class="col-md-6">
                    <div class="info-item">
                        <div class="info-label">Phòng còn trống</div>
                        <div class="info-value">
                            <span class="badge-status ${room.availableRooms > 0 ? 'badge-available' : 'badge-full'}">
                                <c:choose>
                                    <c:when test="${room.availableRooms > 0}">
                                        <i class="bi bi-check-circle"></i> ${room.availableRooms} phòng
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-x-circle"></i> Hết phòng
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Room Images -->
        <div class="detail-card">
            <h4 class="mb-3">
                <i class="bi bi-images"></i> Hình ảnh phòng 
                <span class="text-muted" style="font-size: 16px;">
                    (${not empty roomImages ? roomImages.size() : 0} ảnh)
                </span>
            </h4>
            
            <c:choose>
                <c:when test="${not empty roomImages}">
                    <div class="room-images-grid">
                        <c:forEach var="img" items="${roomImages}">
                            <div class="room-image-item">
                                <img src="${pageContext.request.contextPath}/${img.imageUrl}" 
                                     alt="${img.altText}">
                                <c:if test="${img.primary}">
                                    <span class="image-primary-badge">
                                        <i class="bi bi-star-fill"></i> Ảnh chính
                                    </span>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle"></i> Chưa có hình ảnh nào cho phòng này.
                        <a href="${pageContext.request.contextPath}/hotel/room/edit?id=${room.id}&hotelId=${room.hotelId}">
                            Thêm hình ảnh
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
