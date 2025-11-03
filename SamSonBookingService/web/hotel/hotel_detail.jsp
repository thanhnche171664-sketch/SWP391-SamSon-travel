<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Khách sạn - Hotel Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
        }
        
        .main-content {
            margin-left: 250px;
            padding: 30px;
            min-height: 100vh;
        }
        
        .page-header {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            padding: 25px 30px;
            border-radius: 20px;
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
            margin-bottom: 25px;
            border: 1px solid rgba(255, 255, 255, 0.3);
        }
        
        .page-header h2 {
            color: #333;
            margin: 0;
            font-weight: 600;
        }
        
        .hotel-detail-container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            overflow: hidden;
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
        }
        
        .hotel-image-section {
            position: relative;
            background: white;
            padding: 20px;
            border-radius: 15px;
            margin-bottom: 20px;
        }
        
        .hotel-images-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 15px;
            margin-top: 15px;
        }
        
        .hotel-image-item {
            position: relative;
            width: 100%;
            height: 200px;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        
        .hotel-image-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
        }
        
        .hotel-image-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .image-primary-badge {
            position: absolute;
            top: 10px;
            left: 10px;
            background: linear-gradient(135deg, #ffc107 0%, #ff9800 100%);
            color: white;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            box-shadow: 0 2px 8px rgba(0,0,0,0.2);
        }
        
        .image-gallery-modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.95);
            z-index: 9999;
            padding: 20px;
        }
        
        .gallery-content {
            max-width: 1200px;
            margin: 0 auto;
            height: 100%;
            display: flex;
            flex-direction: column;
        }
        
        .gallery-header {
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px;
            margin-bottom: 20px;
        }
        
        .gallery-main {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 20px;
        }
        
        .gallery-main img {
            max-width: 100%;
            max-height: 70vh;
            object-fit: contain;
        }
        
        .gallery-thumbnails {
            display: flex;
            gap: 10px;
            overflow-x: auto;
            padding: 10px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
        }
        
        .thumbnail-item {
            flex-shrink: 0;
            width: 100px;
            height: 70px;
            cursor: pointer;
            border: 3px solid transparent;
            border-radius: 8px;
            overflow: hidden;
            transition: all 0.3s ease;
        }
        
        .thumbnail-item.active {
            border-color: #667eea;
        }
        
        .thumbnail-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .gallery-nav {
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            background: rgba(255, 255, 255, 0.3);
            border: none;
            color: white;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            cursor: pointer;
            font-size: 24px;
            transition: all 0.3s ease;
        }
        
        .gallery-nav:hover {
            background: rgba(255, 255, 255, 0.5);
        }
        
        .gallery-nav.prev {
            left: 20px;
        }
        
        .gallery-nav.next {
            right: 20px;
        }
        
        .close-gallery {
            background: transparent;
            border: none;
            color: white;
            font-size: 40px;
            cursor: pointer;
        }
        
        .hotel-image-overlay {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            background: linear-gradient(to top, rgba(0,0,0,0.8), transparent);
            padding: 40px 30px 30px;
            color: white;
        }
        
        .hotel-name-large {
            font-size: 36px;
            font-weight: 700;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        
        .hotel-address-large {
            font-size: 18px;
            opacity: 0.9;
        }
        
        .hotel-info-content {
            padding: 40px;
        }
        
        .info-section {
            margin-bottom: 40px;
        }
        
        .section-title {
            font-size: 20px;
            font-weight: 600;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 3px solid #667eea;
            display: inline-block;
        }
        
        .info-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 15px;
            margin-bottom: 15px;
        }
        
        .info-row {
            display: flex;
            align-items: center;
            padding: 12px 0;
        }
        
        .info-icon {
            width: 40px;
            height: 40px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            margin-right: 15px;
            font-size: 18px;
        }
        
        .info-label {
            font-weight: 600;
            color: #333;
            width: 150px;
        }
        
        .info-value {
            color: #666;
            flex-grow: 1;
        }
        
        .action-buttons {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        
        .btn-action {
            flex: 1;
            padding: 15px;
            border-radius: 12px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: none;
            font-size: 15px;
        }
        
        .btn-action:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.2);
        }
        
        .btn-edit-hotel {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }
        
        .btn-delete-hotel {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
            color: white;
        }
        
        /* Room Section */
        .rooms-section {
            margin-top: 40px;
        }
        
        .room-card {
            background: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            border: 2px solid transparent;
        }
        
        .room-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
            border-color: #667eea;
        }
        
        .room-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }
        
        .room-type-badge {
            padding: 8px 20px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 14px;
            text-transform: uppercase;
        }
        
        .room-type-single {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
            color: #333;
        }
        
        .room-type-double {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
            color: #333;
        }
        
        .room-type-dormitory {
            background: linear-gradient(135deg, #d299c2 0%, #fef9d7 100%);
            color: #333;
        }
        
        .room-price {
            font-size: 24px;
            font-weight: 700;
            color: #667eea;
        }
        
        .room-details {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 15px;
            margin-top: 15px;
        }
        
        .room-detail-item {
            text-align: center;
            padding: 10px;
            background: #f8f9fa;
            border-radius: 10px;
        }
        
        .room-detail-label {
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
        }
        
        .room-detail-value {
            font-size: 18px;
            font-weight: 600;
            color: #333;
        }
        
        .room-actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }
        
        .btn-room-action {
            flex: 1;
            padding: 10px;
            border-radius: 10px;
            border: none;
            font-weight: 600;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
        }
        
        .btn-room-info {
            background: #17a2b8;
            color: white;
        }
        
        .btn-room-edit {
            background: #ffc107;
            color: white;
        }
        
        .btn-room-delete {
            background: #dc3545;
            color: white;
        }
        
        .btn-add-room {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            border-radius: 12px;
            border: none;
            font-weight: 600;
            transition: all 0.3s ease;
            margin-bottom: 20px;
        }
        
        .btn-add-room:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .badge-meal-BREAKFAST {
            background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
        }
        
        .badge-meal-LUNCH {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
        
        .badge-meal-DINNER {
            background: linear-gradient(135deg, #30cfd0 0%, #330867 100%);
        }
        
        .empty-rooms {
            text-align: center;
            padding: 60px 20px;
            background: #f8f9fa;
            border-radius: 15px;
        }
        
        .empty-rooms i {
            font-size: 60px;
            color: #ddd;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar -->
    <jsp:include page="/hotel/components/sidebar.jsp" />
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Page Header -->
        <div class="page-header">
            <div class="d-flex justify-content-between align-items-center">
                <h2><i class="bi bi-building-fill"></i> Chi tiết Khách sạn</h2>
                <div>
                   
                    <a href="${pageContext.request.contextPath}/hotel/list" class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Quay lại danh sách
                    </a>
                </div>
            </div>
        </div>
        
        <!-- Alerts -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle-fill"></i> ${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="successMessage" scope="session" />
        </c:if>
        
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert" style="white-space: pre-line;">
                <i class="bi bi-exclamation-triangle-fill"></i> 
                <strong>Cảnh báo!</strong><br/>
                ${sessionScope.errorMessage}
            </div>
            <c:remove var="errorMessage" scope="session" />
        </c:if>
        
        <!-- Hotel Detail Container -->
        <div class="hotel-detail-container">
            <!-- Hotel Images Section -->
            <div class="hotel-image-section">
                <h3><i class="bi bi-images"></i> Hình ảnh khách sạn (${hotelImages.size()})</h3>
                <c:choose>
                    <c:when test="${not empty hotelImages}">
                        <div class="hotel-images-grid">
                            <c:forEach var="img" items="${hotelImages}">
                                <div class="hotel-image-item">
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
                        <div class="text-center text-muted py-5">
                            <i class="bi bi-images" style="font-size: 48px;"></i>
                            <p>Chưa có ảnh nào</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <!-- Hotel Info Content -->
            <div class="hotel-info-content">
                <!-- Basic Information -->
                <div class="info-section">
                    <h3 class="section-title">Thông tin cơ bản</h3>
                    <div class="info-card">
                        <div class="info-row">
                            <div class="info-icon">
                                <i class="bi bi-hash"></i>
                            </div>
                            <div class="info-label">Mã khách sạn:</div>
                            <div class="info-value"><strong>#${hotel.id}</strong></div>
                        </div>
                        
                        <div class="info-row">
                            <div class="info-icon">
                                <i class="bi bi-file-text"></i>
                            </div>
                            <div class="info-label">Mô tả:</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${not empty hotel.description}">
                                        ${hotel.description}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Chưa có mô tả</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <div class="info-row">
                            <div class="info-icon">
                                <i class="bi bi-calendar-plus"></i>
                            </div>
                            <div class="info-label">Ngày tạo:</div>
                            <div class="info-value">
                                <fmt:formatDate value="${hotel.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                            </div>
                        </div>
                        
                        <div class="info-row">
                            <div class="info-icon">
                                <i class="bi bi-calendar-check"></i>
                            </div>
                            <div class="info-label">Cập nhật cuối:</div>
                            <div class="info-value">
                                <fmt:formatDate value="${hotel.updatedAt}" pattern="dd/MM/yyyy HH:mm" />
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Action Buttons -->
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/hotel/edit?id=${hotel.id}" 
                       class="btn-action btn-edit-hotel">
                        <i class="bi bi-pencil-square"></i> Chỉnh sửa thông tin
                    </a>
                    <a href="${pageContext.request.contextPath}/hotel/delete?id=${hotel.id}" 
                       class="btn-action btn-delete-hotel"
                       onclick="return confirm('Bạn có chắc chắn muốn xóa khách sạn này?')">
                        <i class="bi bi-trash3"></i> Xóa khách sạn
                    </a>
                </div>
                
                <!-- Rooms Section -->
                <div class="rooms-section">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h3 class="section-title">Danh sách Phòng</h3>
                        <a href="${pageContext.request.contextPath}/hotel/room/add?hotelId=${hotel.id}" class="btn btn-add-room">
                            <i class="bi bi-plus-circle"></i> Thêm phòng mới
                        </a>
                    </div>
                    
                    <c:choose>
                        <c:when test="${empty rooms}">
                            <div class="empty-rooms">
                                <i class="bi bi-door-closed"></i>
                                <h4>Chưa có phòng nào</h4>
                                <p class="text-muted">Hãy thêm phòng đầu tiên cho khách sạn này!</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <c:forEach var="room" items="${rooms}">
                                    <div class="col-md-6 col-lg-4">
                                        <div class="room-card">
                                            <div class="room-header">
                                                <span class="room-type-badge room-type-${room.roomType}">
                                                    <c:choose>
                                                        <c:when test="${room.roomType == 'single'}">Phòng Đơn</c:when>
                                                        <c:when test="${room.roomType == 'double'}">Phòng Đôi</c:when>
                                                        <c:when test="${room.roomType == 'dormitory'}">Phòng Tập Thể</c:when>
                                                    </c:choose>
                                                </span>
                                                <div class="room-price">
                                                    <fmt:formatNumber value="${room.price}" type="currency" 
                                                                      currencySymbol="₫" groupingUsed="true"/>
                                                </div>
                                            </div>
                                            
                                            <div class="room-details">
                                                <div class="room-detail-item">
                                                    <div class="room-detail-label">Tổng phòng</div>
                                                    <div class="room-detail-value">${room.totalRooms}</div>
                                                </div>
                                                <div class="room-detail-item">
                                                    <div class="room-detail-label">Còn trống</div>
                                                    <div class="room-detail-value">${room.availableRooms}</div>
                                                </div>
                                                <div class="room-detail-item">
                                                    <div class="room-detail-label">Đã đặt</div>
                                                    <div class="room-detail-value">${room.totalRooms - room.availableRooms}</div>
                                                </div>
                                            </div>
                                            
                                            <div class="room-actions">
                                                <a href="${pageContext.request.contextPath}/hotel/room/detail?id=${room.id}&hotelId=${hotel.id}" 
                                                   class="btn-room-action btn-room-info" 
                                                   title="Chi tiết">
                                                    <i class="bi bi-eye"></i> Chi tiết
                                                </a>
                                                <a href="${pageContext.request.contextPath}/hotel/room/edit?id=${room.id}&hotelId=${hotel.id}" 
                                                   class="btn-room-action btn-room-edit">
                                                    <i class="bi bi-pencil"></i> Sửa
                                                </a>
                                                <a href="${pageContext.request.contextPath}/hotel/room/delete?id=${room.id}&hotelId=${hotel.id}" 
                                                   class="btn-room-action btn-room-delete"
                                                   onclick="return confirm('Bạn có chắc muốn xóa phòng này?')">
                                                    <i class="bi bi-trash"></i> Xóa
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <!-- Meal Services Section -->
                <div class="rooms-section mt-5">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h3 class="section-title">
                            <i class="bi bi-cup-hot"></i> Dịch Vụ Ăn Uống
                        </h3>
                        <a href="${pageContext.request.contextPath}/hotel/meal/add?hotelId=${hotel.id}" 
                           class="btn btn-add-room">
                            <i class="bi bi-plus-circle"></i> Thêm dịch vụ mới
                        </a>
                    </div>
                    
                    <c:choose>
                        <c:when test="${empty mealServices}">
                            <div class="empty-rooms">
                                <i class="bi bi-cup-straw"></i>
                                <h4>Chưa có dịch vụ ăn uống nào</h4>
                                <p class="text-muted">Hãy thêm dịch vụ đầu tiên cho khách sạn!</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row g-3">
                                <c:forEach var="meal" items="${mealServices}">
                                    <div class="col-md-6 col-lg-4">
                                        <div class="room-card">
                                            <div class="room-type-badge badge-meal-${meal.mealType}">
                                                <c:choose>
                                                    <c:when test="${meal.mealType == 'BREAKFAST'}">
                                                        <i class="bi bi-brightness-high"></i> Bữa Sáng
                                                    </c:when>
                                                    <c:when test="${meal.mealType == 'LUNCH'}">
                                                        <i class="bi bi-sun"></i> Bữa Trưa
                                                    </c:when>
                                                    <c:when test="${meal.mealType == 'DINNER'}">
                                                        <i class="bi bi-moon-stars"></i> Bữa Tối
                                                    </c:when>
                                                </c:choose>
                                            </div>
                                            <div class="room-card-body">
                                                <div class="room-price">
                                                    <fmt:formatNumber value="${meal.price}" pattern="#,###" /> VNĐ
                                                </div>
                                                <div class="room-details">
                                                    <div class="room-detail-item">
                                                        <div class="room-detail-label">Ngày</div>
                                                        <div class="room-detail-value">
                                                            <fmt:formatDate value="${meal.mealDate}" pattern="dd/MM/yyyy"/>
                                                        </div>
                                                    </div>
                                                    <div class="room-detail-item">
                                                        <div class="room-detail-label">Trạng thái</div>
                                                        <div class="room-detail-value">
                                                            <span class="badge ${meal.status == 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">
                                                                ${meal.status}
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                                
                                                <div class="room-actions">
                                                    <a href="${pageContext.request.contextPath}/hotel/meal/detail?id=${meal.mealId}&hotelId=${hotel.id}" 
                                                       class="btn-room-action btn-room-info">
                                                        <i class="bi bi-eye"></i> Chi tiết
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/hotel/meal/edit?id=${meal.mealId}&hotelId=${hotel.id}" 
                                                       class="btn-room-action btn-room-edit">
                                                        <i class="bi bi-pencil"></i> Sửa
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/hotel/meal/delete?id=${meal.mealId}&hotelId=${hotel.id}" 
                                                       class="btn-room-action btn-room-delete"
                                                       onclick="return confirm('Bạn có chắc muốn xóa dịch vụ này?')">
                                                        <i class="bi bi-trash"></i> Xóa
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
