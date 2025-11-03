<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Phòng - Hotel Manager</title>
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
                <h2><i class="bi bi-plus-circle"></i> Thêm Phòng Mới</h2>
                <a href="${pageContext.request.contextPath}/hotel/detail?id=${param.hotelId}" class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Quay lại
                </a>
            </div>
        </div>
        
        <div class="form-card">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            
            <form method="post" action="${pageContext.request.contextPath}/hotel/room/add" enctype="multipart/form-data">
                <input type="hidden" name="hotelId" value="${param.hotelId}">
                
                <div class="row">
                    <div class="col-md-7">
                        <div class="mb-3">
                            <label class="form-label">Loại phòng <span class="text-danger">*</span></label>
                            <select class="form-select" name="roomType" required>
                                <option value="">-- Chọn loại phòng --</option>
                                <option value="single">Phòng Đơn (Single)</option>
                                <option value="double">Phòng Đôi (Double)</option>
                                <option value="dormitory">Phòng Tập Thể (Dormitory)</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Giá phòng (VNĐ) <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" name="price" min="0" step="1000" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Tổng số phòng <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" name="totalRooms" min="1" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Số phòng còn trống <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" name="availableRooms" min="0" required>
                        </div>
                    </div>
                    
                    <div class="col-md-5">
                        <div class="mb-3">
                            <label class="form-label">
                                Hình ảnh phòng <span class="text-muted">(có thể chọn nhiều)</span>
                            </label>
                            <input type="file" class="form-control" name="images" 
                                   accept="image/*" multiple onchange="previewImages(event)">
                            <small class="text-muted">JPG, PNG, GIF (Tối đa 10MB/ảnh)</small>
                        </div>
                        
                        <div id="imagePreviewContainer"></div>
                    </div>
                </div>
                
                <div class="d-flex justify-content-end gap-3 mt-4">
                    <a href="${pageContext.request.contextPath}/hotel/detail?id=${param.hotelId}" 
                       class="btn btn-secondary">
                        <i class="bi bi-x-lg"></i> Hủy
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-check-lg"></i> Thêm phòng
                    </button>
                </div>
            </form>
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
                            badge.textContent = index === 0 ? 'Ảnh chính' : 'Ảnh ' + (index + 1);
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
    </script>
</body>
</html>
