<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa Phòng - Hotel Manager</title>
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
        }
        
        .preview-item img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .preview-badge {
            position: absolute;
            top: 5px;
            left: 5px;
            background: rgba(102, 126, 234, 0.9);
            color: white;
            padding: 3px 8px;
            border-radius: 5px;
            font-size: 12px;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <jsp:include page="/hotel/components/sidebar.jsp" />
    
    <div class="main-content">
        <div class="page-header">
            <div class="d-flex justify-content-between align-items-center">
                <h2><i class="bi bi-pencil-square"></i> Sửa Phòng</h2>
                <a href="${pageContext.request.contextPath}/hotel/detail?id=${room.hotelId}" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Quay lại
                </a>
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-7">
                <div class="form-card">
                    <h4><i class="bi bi-info-circle"></i> Thông tin phòng</h4>
                    
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="alert alert-success">${success}</div>
                    </c:if>
                    
                    <form method="post" action="${pageContext.request.contextPath}/hotel/room/edit">
                        <input type="hidden" name="roomId" value="${room.id}">
                        <input type="hidden" name="hotelId" value="${room.hotelId}">
                        
                        <div class="mb-3">
                            <label class="form-label">Loại phòng <span class="text-danger">*</span></label>
                            <select class="form-select" name="roomType" required>
                                <option value="single" ${room.roomType == 'single' ? 'selected' : ''}>Phòng Đơn (Single)</option>
                                <option value="double" ${room.roomType == 'double' ? 'selected' : ''}>Phòng Đôi (Double)</option>
                                <option value="dormitory" ${room.roomType == 'dormitory' ? 'selected' : ''}>Phòng Tập Thể (Dormitory)</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Giá phòng (VNĐ) <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" name="price" 
                                   value="${room.price}" min="0" step="1000" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Tổng số phòng <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" name="totalRooms" 
                                   value="${room.totalRooms}" min="1" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Số phòng còn trống <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" name="availableRooms" 
                                   value="${room.availableRooms}" min="0" required>
                        </div>
                        
                        <div class="d-flex justify-content-end gap-3">
                            <a href="${pageContext.request.contextPath}/hotel/detail?id=${room.hotelId}" 
                               class="btn btn-secondary">
                                <i class="bi bi-x-lg"></i> Hủy
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-check-lg"></i> Cập nhật
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="col-md-5">
                <div class="form-card">
                    <h4><i class="bi bi-images"></i> Quản lý hình ảnh</h4>
                    
                    <!-- Existing Images -->
                    <c:if test="${not empty roomImages}">
                        <div class="images-gallery">
                            <h6>Hình ảnh hiện tại (${roomImages.size()})</h6>
                            <div class="gallery-grid">
                                <c:forEach var="img" items="${roomImages}">
                                    <div class="gallery-item ${img.primary ? 'primary' : ''}" id="image-${img.id}">
                                        <img src="${pageContext.request.contextPath}/${img.imageUrl}" 
                                             alt="${img.altText}">
                                        
                                        <c:if test="${img.primary}">
                                            <span class="image-badge">
                                                <i class="bi bi-star-fill"></i> Ảnh chính
                                            </span>
                                        </c:if>
                                        
                                        <div class="image-overlay">
                                            <c:if test="${!img.primary}">
                                                <button class="btn btn-sm btn-warning" 
                                                        onclick="setPrimary(${img.id}, ${room.id})">
                                                    <i class="bi bi-star"></i>
                                                </button>
                                            </c:if>
                                            <button class="btn btn-sm btn-danger" 
                                                    onclick="deleteImage(${img.id}, ${room.id})">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <hr>
                    </c:if>
                    
                    <!-- Add New Images -->
                    <div class="mt-3">
                        <h6>Thêm hình ảnh mới</h6>
                        <form method="post" action="${pageContext.request.contextPath}/hotel/room/edit" 
                              enctype="multipart/form-data">
                            <input type="hidden" name="roomId" value="${room.id}">
                            <input type="hidden" name="hotelId" value="${room.hotelId}">
                            <input type="hidden" name="uploadOnly" value="true">
                            
                            <div class="mb-3">
                                <input type="file" class="form-control" name="images" 
                                       accept="image/*" multiple onchange="previewImages(event)">
                                <small class="text-muted">JPG, PNG, GIF (Tối đa 10MB/ảnh)</small>
                            </div>
                            
                            <div id="imagePreviewContainer"></div>
                            
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="bi bi-upload"></i> Tải lên
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function previewImages(event) {
            const container = document.getElementById('imagePreviewContainer');
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
                            const badge = document.createElement('div');
                            badge.className = 'preview-badge';
                            badge.textContent = 'Ảnh mới ' + (index + 1);
                            item.appendChild(img);
                            item.appendChild(badge);
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
        
        function deleteImage(imageId, roomId) {
            if (confirm('Bạn có chắc muốn xóa ảnh này?')) {
                fetch('${pageContext.request.contextPath}/room/images?action=delete&imageId=' + imageId + '&roomId=' + roomId, {
                    method: 'POST'
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        document.getElementById('image-' + imageId).remove();
                        location.reload();
                    } else {
                        alert('Lỗi: ' + data.message);
                    }
                });
            }
        }
        
        function setPrimary(imageId, roomId) {
            fetch('${pageContext.request.contextPath}/room/images?action=setPrimary&imageId=' + imageId + '&roomId=' + roomId, {
                method: 'POST'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    location.reload();
                } else {
                    alert('Lỗi: ' + data.message);
                }
            });
        }
    </script>
</body>
</html>
