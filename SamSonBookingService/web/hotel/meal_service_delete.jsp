<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xóa Dịch Vụ - Hotel Manager</title>
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
        
        .delete-card {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            max-width: 800px;
            margin: 0 auto;
        }
        
        .warning-icon {
            font-size: 80px;
            color: #dc3545;
            margin-bottom: 20px;
        }
        
        .meal-preview {
            background: #f8f9fa;
            padding: 25px;
            border-radius: 10px;
            margin: 30px 0;
        }
        
        .meal-image {
            width: 100%;
            max-width: 300px;
            height: 200px;
            object-fit: cover;
            border-radius: 10px;
            margin: 0 auto;
            display: block;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .info-item {
            display: flex;
            padding: 12px 0;
            border-bottom: 1px solid #dee2e6;
        }
        
        .info-item:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            min-width: 150px;
            color: #495057;
        }
        
        .info-value {
            color: #212529;
        }
        
        .badge-meal-BREAKFAST {
            background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
            color: white;
            padding: 5px 12px;
            border-radius: 15px;
            font-weight: 600;
        }
        
        .badge-meal-LUNCH {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
            color: white;
            padding: 5px 12px;
            border-radius: 15px;
            font-weight: 600;
        }
        
        .badge-meal-DINNER {
            background: linear-gradient(135deg, #30cfd0 0%, #330867 100%);
            color: white;
            padding: 5px 12px;
            border-radius: 15px;
            font-weight: 600;
        }
        
        .btn-delete {
            background: #dc3545;
            color: white;
            border: none;
            padding: 12px 40px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s;
        }
        
        .btn-delete:hover {
            background: #c82333;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(220, 53, 69, 0.4);
        }
        
        .btn-cancel {
            padding: 12px 40px;
            border-radius: 10px;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <jsp:include page="components/sidebar.jsp"/>
    
    <div class="main-content">
        <div class="page-header">
            <h2><i class="bi bi-trash"></i> Xóa Dịch Vụ Ăn Uống</h2>
            <p class="text-muted mb-0">Xác nhận xóa dịch vụ</p>
        </div>
        
        <div class="delete-card text-center">
            <i class="bi bi-exclamation-triangle-fill warning-icon"></i>
            <h3 class="mb-3">Xác Nhận Xóa Dịch Vụ</h3>
            <p class="text-muted mb-4">Bạn có chắc chắn muốn xóa dịch vụ này? Hành động này không thể hoàn tác!</p>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="bi bi-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <div class="meal-preview text-start">
                <c:if test="${not empty primaryImage}">
                    <img src="${pageContext.request.contextPath}/${primaryImage.imageUrl}" 
                         alt="Meal Image" class="meal-image mb-3">
                </c:if>
                
                <div class="info-item">
                    <div class="info-label">
                        <i class="bi bi-hash"></i> Mã Dịch Vụ:
                    </div>
                    <div class="info-value">#${meal.mealId}</div>
                </div>
                
                <div class="info-item">
                    <div class="info-label">
                        <i class="bi bi-alarm"></i> Loại Bữa Ăn:
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
                
                <div class="info-item">
                    <div class="info-label">
                        <i class="bi bi-calendar-event"></i> Ngày Áp Dụng:
                    </div>
                    <div class="info-value">
                        <fmt:formatDate value="${meal.mealDate}" pattern="dd/MM/yyyy"/>
                    </div>
                </div>
                
                <div class="info-item">
                    <div class="info-label">
                        <i class="bi bi-cash-coin"></i> Giá:
                    </div>
                    <div class="info-value">
                        <strong class="text-primary"><fmt:formatNumber value="${meal.price}" pattern="#,###"/> VNĐ</strong>
                    </div>
                </div>
                
                <c:if test="${not empty meal.description}">
                    <div class="info-item">
                        <div class="info-label">
                            <i class="bi bi-file-text"></i> Mô Tả:
                        </div>
                        <div class="info-value">${meal.description}</div>
                    </div>
                </c:if>
            </div>
            
            <div class="alert alert-warning mt-4">
                <i class="bi bi-info-circle"></i>
                <strong>Lưu ý:</strong> Tất cả hình ảnh liên quan đến dịch vụ này cũng sẽ bị xóa vĩnh viễn.
            </div>
            
            <form action="${pageContext.request.contextPath}/hotel/meal/delete" method="post" class="mt-4">
                <input type="hidden" name="mealId" value="${meal.mealId}">
                <input type="hidden" name="hotelId" value="${meal.hotelId}">
                
                <div class="d-flex gap-3 justify-content-center">
                    <button type="submit" class="btn btn-delete">
                        <i class="bi bi-trash"></i> Xác Nhận Xóa
                    </button>
                    <a href="${pageContext.request.contextPath}/hotel/detail?id=${meal.hotelId}" class="btn btn-secondary btn-cancel">
                        <i class="bi bi-x-circle"></i> Hủy Bỏ
                    </a>
                </div>
            </form>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
