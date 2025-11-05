<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Khách sạn - Hotel Manager</title>
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 10px;
            padding: 12px 40px;
            color: white;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-cancel {
            border-radius: 10px;
            padding: 12px 40px;
            font-weight: 600;
        }
        
        .image-preview {
            width: 100%;
            height: 250px;
            border: 2px dashed #ddd;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            background-color: #f8f9fa;
            margin-bottom: 10px;
        }
        
        .image-preview img {
            max-width: 100%;
            max-height: 100%;
            object-fit: cover;
        }
        
        .image-preview .placeholder {
            text-align: center;
            color: #999;
        }
        
        .preview-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
            max-height: 500px;
            overflow-y: auto;
        }
        
        .preview-item {
            position: relative;
            width: 100%;
            height: 150px;
            border: 2px solid #ddd;
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
                <h2><i class="bi bi-plus-circle"></i> Thêm Khách sạn Mới</h2>
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
            
            <form method="post" action="${pageContext.request.contextPath}/hotel/add" 
                  enctype="multipart/form-data" id="addHotelForm">
                  
                <div class="row">
                    <div class="col-md-8">
                        <!-- Tên khách sạn -->
                        <div class="mb-3">
                            <label for="name" class="form-label">
                                Tên khách sạn <span class="required">*</span>
                            </label>
                            <input type="text" class="form-control" id="name" name="name" 
                                   placeholder="Nhập tên khách sạn..." required>
                        </div>
                        
                        <!-- Địa chỉ -->
                        <div class="mb-3">
                            <label for="address" class="form-label">
                                Địa chỉ <span class="required">*</span>
                            </label>
                            <input type="text" class="form-control" id="address" name="address" 
                                   placeholder="Nhập địa chỉ khách sạn..." required>
                        </div>
                        
                        <!-- Mô tả -->
                        <div class="mb-3">
                            <label for="description" class="form-label">Mô tả</label>
                            <textarea class="form-control" id="description" name="description" 
                                      rows="5" placeholder="Nhập mô tả về khách sạn..."></textarea>
                        </div>
                    </div>
                    
                    <div class="col-md-4">
                        <!-- Upload nhiều ảnh -->
                        <div class="mb-3">
                            <label for="images" class="form-label">
                                Hình ảnh khách sạn <span class="text-muted">(có thể chọn nhiều)</span>
                            </label>
                            <input type="file" class="form-control" id="images" name="images" 
                                   accept="image/*" multiple onchange="previewImages(event)">
                            <small class="text-muted">Định dạng: JPG, PNG, GIF (Tối đa 10MB/ảnh)</small>
                        </div>
                        
                        <!-- Preview ảnh -->
                        <div id="imagePreviewContainer">
                            <div class="image-preview" id="defaultPreview">
                                <div class="placeholder">
                                    <i class="bi bi-images" style="font-size: 50px;"></i>
                                    <p>Chọn ảnh để xem trước</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Buttons -->
                <div class="d-flex justify-content-end gap-3 mt-4">
                    <a href="${pageContext.request.contextPath}/hotel/list" 
                       class="btn btn-secondary btn-cancel">
                        <i class="bi bi-x-lg"></i> Hủy
                    </a>
                    <button type="submit" class="btn btn-submit">
                        <i class="bi bi-check-lg"></i> Thêm khách sạn
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
                // Xóa preview cũ
                container.innerHTML = '';
                
                // Tạo grid preview
                const grid = document.createElement('div');
                grid.className = 'preview-grid';
                
                // Preview từng ảnh
                Array.from(files).forEach((file, index) => {
                    if (file.type.startsWith('image/')) {
                        const reader = new FileReader();
                        reader.onload = function(e) {
                            const item = document.createElement('div');
                            item.className = 'preview-item';
                            
                            const img = document.createElement('img');
                            img.src = e.target.result;
                            img.alt = 'Preview ' + (index + 1);
                            
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
                // Reset về placeholder
                container.innerHTML = `
                    <div class="image-preview" id="defaultPreview">
                        <div class="placeholder">
                            <i class="bi bi-images" style="font-size: 50px;"></i>
                            <p>Chọn ảnh để xem trước</p>
                        </div>
                    </div>
                `;
            }
        }
        
        // Validate form trước khi submit
        document.getElementById('addHotelForm').addEventListener('submit', function(e) {
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
