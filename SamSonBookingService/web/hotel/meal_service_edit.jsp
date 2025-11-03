<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa Dịch Vụ Ăn Uống - Hotel Manager</title>
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
        
        .images-gallery {
            margin-top: 20px;
        }
        
        .gallery-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 15px;
        }
        
        .gallery-item {
            position: relative;
            height: 150px;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .gallery-item.primary {
            border: 3px solid #ffc107;
            box-shadow: 0 0 15px rgba(255, 193, 7, 0.4);
        }
        
        .gallery-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .image-overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0,0,0,0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            opacity: 0;
            transition: opacity 0.3s;
        }
        
        .gallery-item:hover .image-overlay {
            opacity: 1;
        }
        
        .image-badge {
            position: absolute;
            top: 10px;
            left: 10px;
            background: rgba(255, 193, 7, 0.95);
            color: #000;
            padding: 4px 10px;
            border-radius: 5px;
            font-size: 12px;
            font-weight: 600;
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
    </style>
</head>
<body>
    <jsp:include page="components/sidebar.jsp"/>
    
    <div class="main-content">
        <div class="page-header">
            <h2><i class="bi bi-cup-hot"></i> Sửa Dịch Vụ Ăn Uống</h2>
            <p class="text-muted mb-0">Cập nhật thông tin dịch vụ ăn uống</p>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill"></i> ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <div class="row">
            <!-- Form Edit Info - Left Column -->
            <div class="col-lg-7">
                <div class="form-card">
                    <h4 class="mb-4">Thông Tin Dịch Vụ</h4>
                    <form action="${pageContext.request.contextPath}/hotel/meal/edit" method="post">
                        <input type="hidden" name="mealId" value="${meal.mealId}">
                        <input type="hidden" name="hotelId" value="${meal.hotelId}">
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="categoryId" class="form-label">Danh mục <span class="text-danger">*</span></label>
                                <select class="form-select" id="categoryId" name="categoryId" required>
                                    <option value="1" ${meal.categoryId == 1 ? 'selected' : ''}>Dịch vụ ăn uống</option>
                                    <option value="2" ${meal.categoryId == 2 ? 'selected' : ''}>Buffet</option>
                                    <option value="3" ${meal.categoryId == 3 ? 'selected' : ''}>Set Menu</option>
                                </select>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="mealType" class="form-label">Loại bữa ăn <span class="text-danger">*</span></label>
                                <select class="form-select" id="mealType" name="mealType" required>
                                    <option value="BREAKFAST" ${meal.mealType == 'BREAKFAST' ? 'selected' : ''}>🌅 Bữa Sáng</option>
                                    <option value="LUNCH" ${meal.mealType == 'LUNCH' ? 'selected' : ''}>☀️ Bữa Trưa</option>
                                    <option value="DINNER" ${meal.mealType == 'DINNER' ? 'selected' : ''}>🌙 Bữa Tối</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="mealDate" class="form-label">Ngày áp dụng <span class="text-danger">*</span></label>
                                <fmt:formatDate value="${meal.mealDate}" pattern="yyyy-MM-dd" var="formattedDate"/>
                                <input type="date" class="form-control" id="mealDate" name="mealDate" value="${formattedDate}" required>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="price" class="form-label">Giá (VNĐ) <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" id="price" name="price" value="${meal.price}" step="1000" min="0" required>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Mô tả</label>
                            <textarea class="form-control" id="description" name="description" rows="4">${meal.description}</textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label for="status" class="form-label">Trạng thái <span class="text-danger">*</span></label>
                            <select class="form-select" id="status" name="status" required>
                                <option value="ACTIVE" ${meal.status == 'ACTIVE' ? 'selected' : ''}>Hoạt động</option>
                                <option value="INACTIVE" ${meal.status == 'INACTIVE' ? 'selected' : ''}>Không hoạt động</option>
                            </select>
                        </div>
                        
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-check-circle"></i> Cập Nhật
                            </button>
                            <a href="${pageContext.request.contextPath}/hotel/detail?id=${meal.hotelId}" class="btn btn-secondary">
                                <i class="bi bi-x-circle"></i> Hủy
                            </a>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Images Gallery - Right Column -->
            <div class="col-lg-5">
                <div class="form-card">
                    <h4 class="mb-4">Quản Lý Hình Ảnh</h4>
                    
                    <!-- Existing Images -->
                    <div class="images-gallery">
                        <h6 class="mb-3">Hình ảnh hiện tại (${mealImages.size()})</h6>
                        <c:if test="${empty mealImages}">
                            <p class="text-muted">Chưa có hình ảnh nào.</p>
                        </c:if>
                        
                        <div class="gallery-grid">
                            <c:forEach var="image" items="${mealImages}">
                                <div class="gallery-item ${image.primary ? 'primary' : ''}" id="image-${image.id}">
                                    <img src="${pageContext.request.contextPath}/${image.imageUrl}" alt="${image.altText}">
                                    <c:if test="${image.primary}">
                                        <span class="image-badge">
                                            <i class="bi bi-star-fill"></i> Ảnh chính
                                        </span>
                                    </c:if>
                                    <div class="image-overlay">
                                        <c:if test="${!image.primary}">
                                            <button class="btn btn-sm btn-warning" onclick="setPrimaryImage(${image.id}, ${meal.mealId})">
                                                <i class="bi bi-star"></i>
                                            </button>
                                        </c:if>
                                        <button class="btn btn-sm btn-danger" onclick="deleteImage(${image.id})">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <!-- Add New Images Form -->
                    <div class="mt-4 pt-4 border-top">
                        <h6 class="mb-3">Thêm hình ảnh mới</h6>
                        <form id="uploadForm" action="${pageContext.request.contextPath}/hotel/meal/edit" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="mealId" value="${meal.mealId}">
                            <input type="hidden" name="hotelId" value="${meal.hotelId}">
                            <input type="hidden" name="uploadOnly" value="true">
                            
                            <div class="mb-3">
                                <input type="file" class="form-control" id="newImages" name="images" multiple accept="image/*" onchange="previewNewImages(event)">
                                <small class="text-muted">Chọn nhiều hình ảnh (JPG, PNG, GIF)</small>
                                <div id="preview-container" class="preview-grid"></div>
                            </div>
                            
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="bi bi-cloud-upload"></i> Tải Lên Ảnh
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function deleteImage(imageId) {
            if (!confirm('Bạn có chắc muốn xóa ảnh này?')) return;
            
            fetch('${pageContext.request.contextPath}/meal/images?action=delete&imageId=' + imageId, {
                method: 'POST'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.getElementById('image-' + imageId).remove();
                    location.reload();
                } else {
                    alert('Không thể xóa ảnh: ' + (data.message || 'Unknown error'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi xóa ảnh!');
            });
        }
        
        function setPrimaryImage(imageId, mealId) {
            fetch('${pageContext.request.contextPath}/meal/images?action=setPrimary&imageId=' + imageId + '&mealId=' + mealId, {
                method: 'POST'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    location.reload();
                } else {
                    alert('Không thể đặt ảnh chính: ' + (data.message || 'Unknown error'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi đặt ảnh chính!');
            });
        }
        
        function previewNewImages(event) {
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
                        container.appendChild(div);
                    }
                    
                    reader.readAsDataURL(file);
                }
            }
        }
    </script>
</body>
</html>
