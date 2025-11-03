<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Dịch Vụ Ăn Uống - Hotel Manager</title>
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
        
        .form-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        
        .preview-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
            margin-top: 10px;
        }
        
        .preview-item {
            position: relative;
            width: 100%;
            height: 150px;
            border: 2px dashed #667eea;
            border-radius: 10px;
            overflow: hidden;
            background: #f8f9fa;
        }
        
        .preview-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 12px 30px;
            border-radius: 10px;
            transition: all 0.3s;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            padding: 12px 30px;
            border-radius: 10px;
        }
    </style>
</head>
<body>
    <jsp:include page="components/sidebar.jsp"/>
    
    <div class="main-content">
        <div class="page-header">
            <h2><i class="bi bi-cup-hot"></i> Thêm Dịch Vụ Ăn Uống Mới</h2>
            <p class="text-muted mb-0">Thêm dịch vụ ăn uống cho khách sạn</p>
        </div>
        
        <div class="form-card">
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/hotel/meal/add" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
                <input type="hidden" name="hotelId" value="${param.hotelId}">
                
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="categoryId" class="form-label">Danh mục dịch vụ <span class="text-danger">*</span></label>
                        <select class="form-select" id="categoryId" name="categoryId" required>
                            <option value="">Chọn danh mục...</option>
                            <option value="1">Dịch vụ ăn uống</option>
                            <option value="2">Buffet</option>
                            <option value="3">Set Menu</option>
                        </select>
                    </div>
                    
                    <div class="col-md-6 mb-3">
                        <label for="mealType" class="form-label">Loại bữa ăn <span class="text-danger">*</span></label>
                        <select class="form-select" id="mealType" name="mealType" required>
                            <option value="">Chọn loại bữa ăn...</option>
                            <option value="BREAKFAST">🌅 Bữa Sáng</option>
                            <option value="LUNCH">☀️ Bữa Trưa</option>
                            <option value="DINNER">🌙 Bữa Tối</option>
                        </select>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="mealDate" class="form-label">Ngày áp dụng <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" id="mealDate" name="mealDate" required>
                    </div>
                    
                    <div class="col-md-6 mb-3">
                        <label for="price" class="form-label">Giá (VNĐ) <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" id="price" name="price" step="1000" min="0" required>
                    </div>
                </div>
                
                <div class="mb-3">
                    <label for="description" class="form-label">Mô tả</label>
                    <textarea class="form-control" id="description" name="description" rows="4" placeholder="Mô tả chi tiết về dịch vụ ăn uống..."></textarea>
                </div>
                
                <div class="mb-3">
                    <label for="status" class="form-label">Trạng thái <span class="text-danger">*</span></label>
                    <select class="form-select" id="status" name="status" required>
                        <option value="ACTIVE">Hoạt động</option>
                        <option value="INACTIVE">Không hoạt động</option>
                    </select>
                </div>
                
                <div class="mb-3">
                    <label for="images" class="form-label">Hình ảnh dịch vụ <span class="text-danger">*</span></label>
                    <input type="file" class="form-control" id="images" name="images" multiple accept="image/*" required onchange="previewImages(event)">
                    <small class="text-muted">Chọn nhiều hình ảnh (JPG, PNG, GIF). Ảnh đầu tiên sẽ là ảnh chính.</small>
                    <div id="preview-container" class="preview-grid"></div>
                </div>
                
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-check-circle"></i> Thêm Dịch Vụ
                    </button>
                    <a href="${pageContext.request.contextPath}/hotel/detail?id=${param.hotelId}" class="btn btn-secondary">
                        <i class="bi bi-x-circle"></i> Hủy
                    </a>
                </div>
            </form>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function previewImages(event) {
            const container = document.getElementById('preview-container');
            container.innerHTML = '';
            const files = event.target.files;
            
            if (files.length > 0) {
                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    const reader = new FileReader();
                    
                    reader.onload = function(e) {
                        const div = document.createElement('div');
                        div.className = 'preview-item';
                        div.innerHTML = '<img src="' + e.target.result + '" alt="Preview">';
                        if (i === 0) {
                            const badge = document.createElement('span');
                            badge.className = 'badge bg-primary position-absolute top-0 start-0 m-2';
                            badge.textContent = 'Ảnh chính';
                            div.appendChild(badge);
                        }
                        container.appendChild(div);
                    }
                    
                    reader.readAsDataURL(file);
                }
            }
        }
        
        function validateForm() {
            const images = document.getElementById('images').files;
            if (images.length === 0) {
                alert('Vui lòng chọn ít nhất một hình ảnh!');
                return false;
            }
            return true;
        }
        
        // Set min date to today
        document.getElementById('mealDate').min = new Date().toISOString().split('T')[0];
    </script>
</body>
</html>
