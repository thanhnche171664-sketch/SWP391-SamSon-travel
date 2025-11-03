<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Dịch Vụ - Hotel Manager</title>
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
        }
        
        .info-row {
            display: flex;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }
        
        .info-row:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            color: #495057;
            min-width: 200px;
        }
        
        .info-value {
            color: #212529;
            flex: 1;
        }
        
        .images-section {
            margin-top: 30px;
        }
        
        .images-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        
        .image-item {
            position: relative;
            height: 200px;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }
        
        .image-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        .image-item.primary {
            border: 3px solid #ffc107;
            box-shadow: 0 0 15px rgba(255, 193, 7, 0.4);
        }
        
        .image-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .primary-badge {
            position: absolute;
            top: 10px;
            left: 10px;
            background: rgba(255, 193, 7, 0.95);
            color: #000;
            padding: 5px 12px;
            border-radius: 5px;
            font-size: 12px;
            font-weight: 600;
        }
        
        .badge-meal-BREAKFAST {
            background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
            color: white;
            padding: 8px 15px;
            border-radius: 20px;
            font-weight: 600;
        }
        
        .badge-meal-LUNCH {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
            color: white;
            padding: 8px 15px;
            border-radius: 20px;
            font-weight: 600;
        }
        
        .badge-meal-DINNER {
            background: linear-gradient(135deg, #30cfd0 0%, #330867 100%);
            color: white;
            padding: 8px 15px;
            border-radius: 20px;
            font-weight: 600;
        }
        
        .btn-action {
            padding: 10px 25px;
            border-radius: 10px;
            transition: all 0.3s;
        }
        
        .btn-action:hover {
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <jsp:include page="components/sidebar.jsp"/>
    
    <div class="main-content">
        <div class="page-header">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h2><i class="bi bi-cup-hot"></i> Chi Tiết Dịch Vụ Ăn Uống</h2>
                    <p class="text-muted mb-0">Thông tin chi tiết dịch vụ</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/hotel/meal/edit?id=${meal.mealId}" class="btn btn-warning btn-action me-2">
                        <i class="bi bi-pencil"></i> Sửa
                    </a>
                    <a href="${pageContext.request.contextPath}/hotel/meal/delete?id=${meal.mealId}" class="btn btn-danger btn-action me-2">
                        <i class="bi bi-trash"></i> Xóa
                    </a>
                    <a href="${pageContext.request.contextPath}/hotel/detail?id=${meal.hotelId}" class="btn btn-secondary btn-action">
                        <i class="bi bi-arrow-left"></i> Quay lại
                    </a>
                </div>
            </div>
        </div>
        
        <div class="detail-card">
            <h4 class="mb-4">Thông Tin Dịch Vụ</h4>
            
            <div class="info-row">
                <div class="info-label">
                    <i class="bi bi-hash"></i> Mã Dịch Vụ
                </div>
                <div class="info-value">#${meal.mealId}</div>
            </div>
            
            <div class="info-row">
                <div class="info-label">
                    <i class="bi bi-bookmark"></i> Danh Mục
                </div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${meal.categoryId == 1}">Dịch vụ ăn uống</c:when>
                        <c:when test="${meal.categoryId == 2}">Buffet</c:when>
                        <c:when test="${meal.categoryId == 3}">Set Menu</c:when>
                        <c:otherwise>Khác</c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <div class="info-row">
                <div class="info-label">
                    <i class="bi bi-alarm"></i> Loại Bữa Ăn
                </div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${meal.mealType == 'BREAKFAST'}">
                            <span class="badge-meal-BREAKFAST"><i class="bi bi-brightness-high"></i> Bữa Sáng</span>
                        </c:when>
                        <c:when test="${meal.mealType == 'LUNCH'}">
                            <span class="badge-meal-LUNCH"><i class="bi bi-sun"></i> Bữa Trưa</span>
                        </c:when>
                        <c:when test="${meal.mealType == 'DINNER'}">
                            <span class="badge-meal-DINNER"><i class="bi bi-moon-stars"></i> Bữa Tối</span>
                        </c:when>
                    </c:choose>
                </div>
            </div>
            
            <div class="info-row">
                <div class="info-label">
                    <i class="bi bi-calendar-event"></i> Ngày Áp Dụng
                </div>
                <div class="info-value">
                    <fmt:formatDate value="${meal.mealDate}" pattern="dd/MM/yyyy"/>
                </div>
            </div>
            
            <div class="info-row">
                <div class="info-label">
                    <i class="bi bi-cash-coin"></i> Giá
                </div>
                <div class="info-value">
                    <strong class="text-primary"><fmt:formatNumber value="${meal.price}" pattern="#,###"/> VNĐ</strong>
                </div>
            </div>
            
            <div class="info-row">
                <div class="info-label">
                    <i class="bi bi-file-text"></i> Mô Tả
                </div>
                <div class="info-value">
                    ${meal.description != null ? meal.description : '<i class="text-muted">Chưa có mô tả</i>'}
                </div>
            </div>
            
            <div class="info-row">
                <div class="info-label">
                    <i class="bi bi-check-circle"></i> Trạng Thái
                </div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${meal.status == 'ACTIVE'}">
                            <span class="badge bg-success">Hoạt động</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">Không hoạt động</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        
        <!-- Images Section -->
        <div class="images-section">
            <div class="detail-card">
                <h4 class="mb-3">Hình Ảnh Dịch Vụ</h4>
                
                <c:if test="${empty mealImages}">
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle"></i> Chưa có hình ảnh nào cho dịch vụ này.
                    </div>
                </c:if>
                
                <div class="images-grid">
                    <c:forEach var="image" items="${mealImages}">
                        <div class="image-item ${image.primary ? 'primary' : ''}">
                            <img src="${pageContext.request.contextPath}/${image.imageUrl}" alt="${image.altText}">
                            <c:if test="${image.primary}">
                                <span class="primary-badge">
                                    <i class="bi bi-star-fill"></i> Ảnh chính
                                </span>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
