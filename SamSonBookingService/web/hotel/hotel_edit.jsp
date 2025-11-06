<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa Khách sạn - Hotel Manager</title>
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
            color: #333;
            margin: 0;
            font-weight: 600;
        }
        
        .form-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        
        .form-label {
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
        }
        
        .form-control, .form-select {
            border-radius: 10px;
            border: 1px solid #ddd;
            padding: 10px 15px;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .btn-submit {
            background: linear-gradient(135deg, #ffc107 0%, #ff9800 100%);
            border: none;
            border-radius: 10px;
            padding: 12px 40px;
            color: white;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 193, 7, 0.4);
        }
        
        .btn-cancel {
            border-radius: 10px;
            padding: 12px 40px;
            font-weight: 600;
        }
        
        .image-gallery {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
            margin-bottom: 15px;
            max-height: 400px;
            overflow-y: auto;
        }
        
        .image-item {
            position: relative;
            width: 100%;
            height: 150px;
            border: 2px solid #ddd;
            border-radius: 10px;
            overflow: hidden;
        }
        
        .image-item.primary {
            border-color: #ffc107;
            border-width: 3px;
        }
        
        .image-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .image-badge {
            position: absolute;
            top: 5px;
            left: 5px;
            background: rgba(255, 193, 7, 0.9);
            color: white;
            padding: 3px 8px;
            border-radius: 5px;
            font-size: 11px;
            font-weight: 600;
        }
        
        .image-actions {
            position: absolute;
            top: 5px;
            right: 5px;
            display: flex;
            gap: 5px;
        }
        
        .btn-icon {
            width: 28px;
            height: 28px;
            padding: 0;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
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
            height: 120px;
            border: 2px dashed #667eea;
            border-radius: 10px;
            overflow: hidden;
        }
        
        .preview-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .required {
            color: red;
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
                <h2><i class="bi bi-pencil-square"></i> Chỉnh sửa Khách sạn</h2>
                <a href="${pageContext.request.contextPath}/hotel/list" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Quay lại
                </a>
            </div>
        </div>
        
        <!-- Form Card -->
        <div class="form-card">
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <form method="post" action="${pageContext.request.contextPath}/hotel/edit" 
                  enctype="multipart/form-data" id="editHotelForm">
                  
                <input type="hidden" name="id" value="${hotel.id}">
                  
                <div class="row">
                    <div class="col-md-7">
                        <!-- Tên khách sạn -->
                        <div class="mb-3">
                            <label for="name" class="form-label">
                                Tên khách sạn <span class="required">*</span>
                            </label>
                            <input type="text" class="form-control" id="name" name="name" 
                                   value="${hotel.name}" placeholder="Nhập tên khách sạn..." required>
                        </div>
                        
                        <!-- Địa chỉ -->
                        <div class="mb-3">
                            <label for="address" class="form-label">
                                Địa chỉ <span class="required">*</span>
                            </label>
                            <input type="text" class="form-control" id="address" name="address" 
                                   value="${hotel.address}" placeholder="Nhập địa chỉ khách sạn..." required>
                        </div>
                        
                        <!-- Mô tả -->
                        <div class="mb-3">
                            <label for="description" class="form-label">Mô tả</label>
                            <textarea class="form-control" id="description" name="description" 
                                      rows="4" placeholder="Nhập mô tả về khách sạn...">${hotel.description}</textarea>
                        </div>
                    </div>
                    
                    <div class="col-md-5">
                        <!-- Ảnh hiện có -->
                        <div class="mb-3">
                            <label class="form-label">
                                Ảnh hiện có (${hotelImages.size()})
                            </label>
                            <div class="image-gallery" id="imageGallery">
                                <c:forEach var="img" items="${hotelImages}">
                                    <div class="image-item ${img.primary ? 'primary' : ''}" id="img-${img.id}">
                                        <img src="${pageContext.request.contextPath}/${img.imageUrl}" alt="${img.altText}">
                                        <c:if test="${img.primary}">
                                            <span class="image-badge">Ảnh chính</span>
                                        </c:if>
                                        <div class="image-actions">
                                            <c:if test="${!img.primary}">
                                                <button type="button" class="btn btn-warning btn-icon" 
                                                        onclick="setPrimary(${img.id})" title="Đặt làm ảnh chính">
                                                    <i class="bi bi-star-fill"></i>
                                                </button>
                                            </c:if>
                                            <button type="button" class="btn btn-danger btn-icon" 
                                                    onclick="deleteImage(${img.id})" title="Xóa ảnh">
                                                <i class="bi bi-trash-fill"></i>
                                            </button>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        
                        <!-- Thêm ảnh mới -->
                        <div class="mb-3">
                            <label for="newImages" class="form-label">
                                Thêm ảnh mới <span class="text-muted">(có thể chọn nhiều)</span>
                            </label>
                            <input type="file" class="form-control" id="newImages" name="newImages" 
                                   accept="image/*" multiple onchange="previewNewImages(event)">
                            <small class="text-muted">JPG, PNG, GIF (Tối đa 10MB/ảnh)</small>
                        </div>
                        
                        <!-- Preview ảnh mới -->
                        <div id="newImagePreview"></div>
                    </div>
                </div>
                
                <!-- Buttons -->
                <div class="d-flex justify-content-end gap-3 mt-4">
                    <a href="${pageContext.request.contextPath}/hotel/list" 
                       class="btn btn-secondary btn-cancel">
                        <i class="bi bi-x-lg"></i> Hủy
                    </a>
                    <button type="submit" class="btn btn-submit">
                        <i class="bi bi-check-lg"></i> Cập nhật
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const hotelId = ${hotel.id};
        const contextPath = '${pageContext.request.contextPath}';
        
        // Preview ảnh mới
        function previewNewImages(event) {
            const container = document.getElementById('newImagePreview');
            const files = event.target.files;
            
            if (files.length > 0) {
                container.innerHTML = '';
                const grid = document.createElement('div');
                grid.className = 'preview-grid';
                
                Array.from(files).forEach((file, index) => {
                    if (file.type.startsWith('image/')) {
                        const reader = new FileReader();
                        reader.onload = function(e) {
                            const item = document.createElement('div');
                            item.className = 'preview-item';
                            const img = document.createElement('img');
                            img.src = e.target.result;
                            img.alt = 'Preview ' + (index + 1);
                            item.appendChild(img);
                            grid.appendChild(item);
                        };
                        reader.readAsDataURL(file);
                    }
                });
                
                container.appendChild(grid);
            } else {
                container.innerHTML = '';
            }
        }
        
        // Đặt ảnh làm primary
        function setPrimary(imageId) {
            if (confirm('Đặt ảnh này làm ảnh chính?')) {
                fetch(contextPath + '/hotel/images?action=setPrimary&imageId=' + imageId + '&hotelId=' + hotelId)
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            location.reload();
                        } else {
                            alert('Có lỗi xảy ra: ' + data.message);
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('Có lỗi xảy ra khi đặt ảnh chính!');
                    });
            }
        }
        
        // Xóa ảnh
        function deleteImage(imageId) {
            if (confirm('Bạn có chắc muốn xóa ảnh này?')) {
                fetch(contextPath + '/hotel/images?action=delete&imageId=' + imageId + '&hotelId=' + hotelId)
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            document.getElementById('img-' + imageId).remove();
                            
                            // Cập nhật số lượng ảnh
                            const remaining = document.querySelectorAll('.image-item').length;
                            document.querySelector('.image-gallery').previousElementSibling.textContent = 
                                'Ảnh hiện có (' + remaining + ')';
                        } else {
                            alert('Có lỗi xảy ra: ' + data.message);
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('Có lỗi xảy ra khi xóa ảnh!');
                    });
            }
        }
        
        // Validate form
        document.getElementById('editHotelForm').addEventListener('submit', function(e) {
            const name = document.getElementById('name').value.trim();
            const address = document.getElementById('address').value.trim();
            
            if (!name || !address) {
                e.preventDefault();
                alert('Vui lòng nhập đầy đủ thông tin bắt buộc!');
                return false;
            }
        });
    </script>
</body>
</html>
