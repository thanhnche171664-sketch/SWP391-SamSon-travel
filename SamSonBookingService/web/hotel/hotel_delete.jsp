<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xóa Khách sạn - Hotel Manager</title>
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
        
        .page-header h2 {
            color: #dc3545;
            margin: 0;
            font-weight: 600;
        }
        
        .delete-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            border-left: 5px solid #dc3545;
        }
        
        .warning-icon {
            font-size: 80px;
            color: #dc3545;
            text-align: center;
            margin-bottom: 20px;
        }
        
        .hotel-info {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
        }
        
        .hotel-info img {
            width: 100%;
            max-width: 300px;
            height: 200px;
            object-fit: cover;
            border-radius: 10px;
            margin-bottom: 15px;
        }
        
        .hotel-info h4 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .hotel-info p {
            color: #666;
            margin: 5px 0;
        }
        
        .warning-text {
            color: #dc3545;
            font-weight: 600;
            text-align: center;
            margin: 20px 0;
            font-size: 16px;
        }
        
        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }
        
        .btn-action {
            padding: 12px 40px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .btn-delete {
            background-color: #dc3545;
            color: white;
            border: none;
        }
        
        .btn-delete:hover {
            background-color: #c82333;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(220, 53, 69, 0.4);
        }
        
        .btn-cancel {
            background-color: #6c757d;
            color: white;
        }
        
        .btn-cancel:hover {
            background-color: #5a6268;
            transform: translateY(-2px);
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
                <h2><i class="bi bi-trash"></i> Xác nhận Xóa Khách sạn</h2>
                <a href="${pageContext.request.contextPath}/hotel/list" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Quay lại
                </a>
            </div>
        </div>
        
        <!-- Delete Card -->
        <div class="delete-card">
            <div class="warning-icon">
                <i class="bi bi-exclamation-triangle-fill"></i>
            </div>
            
            <h3 class="text-center mb-4">Bạn có chắc chắn muốn xóa khách sạn này?</h3>
            
            <div class="hotel-info">
                <div class="row">
                    <div class="col-md-4 text-center">
                        <c:choose>
                            <c:when test="${not empty hotelImage}">
                                <img src="${pageContext.request.contextPath}/${hotelImage.imageUrl}" 
                                     alt="${hotel.name}">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/uploads/hotels/default.jpg" 
                                     alt="${hotel.name}">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="col-md-8">
                        <h4><i class="bi bi-building"></i> ${hotel.name}</h4>
                        <p><i class="bi bi-hash"></i> <strong>ID:</strong> ${hotel.id}</p>
                        <p><i class="bi bi-geo-alt"></i> <strong>Địa chỉ:</strong> ${hotel.address}</p>
                        <p><i class="bi bi-file-text"></i> <strong>Mô tả:</strong> 
                            <c:choose>
                                <c:when test="${not empty hotel.description}">
                                    ${hotel.description}
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">Chưa có mô tả</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </div>
            
            <!-- Cảnh báo nếu có bookings -->
            <c:if test="${hasBookings}">
                <div class="alert alert-danger mb-4" role="alert">
                    <h5 class="alert-heading">
                        <i class="bi bi-exclamation-triangle-fill"></i> 
                        Không thể xóa khách sạn này!
                    </h5>
                    <hr>
                    <p class="mb-0">
                        <strong>Khách sạn đang có booking:</strong><br/>
                        <c:if test="${bookingStats != null}">
                            • Tổng số booking: <strong>${bookingStats[0]}</strong><br/>
                            • Booking đã xác nhận: <strong class="text-success">${bookingStats[1]}</strong><br/>
                            • Booking đang chờ: <strong class="text-warning">${bookingStats[2]}</strong><br/>
                        </c:if>
                        <br/>
                        Vui lòng xử lý hết các booking trước khi xóa khách sạn.
                    </p>
                </div>
            </c:if>
            
            <div class="warning-text">
                <i class="bi bi-exclamation-circle"></i> 
                Hành động này không thể hoàn tác! Tất cả dữ liệu liên quan sẽ bị xóa vĩnh viễn.
            </div>
            
            <form method="post" action="${pageContext.request.contextPath}/hotel/delete">
                <input type="hidden" name="id" value="${hotel.id}">
                
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/hotel/list" 
                       class="btn btn-cancel btn-action">
                        <i class="bi bi-x-lg"></i> Quay lại
                    </a>
                    <c:choose>
                        <c:when test="${hasBookings}">
                            <button type="button" class="btn btn-delete btn-action" disabled 
                                    title="Không thể xóa vì đang có booking">
                                <i class="bi bi-lock"></i> Không thể xóa
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" class="btn btn-delete btn-action">
                                <i class="bi bi-trash"></i> Xác nhận xóa
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </form>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
