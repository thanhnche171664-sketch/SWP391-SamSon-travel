<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa Dịch vụ Wellness</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body style="background-color:#f8fafc;">
<div class="container mt-5">
    <div class="card p-4 shadow-lg rounded-4">
        <h3 class="text-center text-primary mb-4">️ Cập nhật Dịch vụ Wellness</h3>

        <form method="post" action="wellness-edit">
            <input type="hidden" name="wellnessId" value="${wellnessService.wellnessId}">
            <input type="hidden" name="hotelId" value="${wellnessService.hotelId}">
            <input type="hidden" name="categoryId" value="${wellnessService.categoryId}">

            <div class="mb-3">
                <label class="form-label">Tên dịch vụ</label>
                <input type="text" name="serviceName" class="form-control" value="${wellnessService.serviceName}" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea name="description" class="form-control" rows="3" required>${wellnessService.description}</textarea>
            </div>

            <div class="row">
                <div class="col-md-4 mb-3">
                    <label class="form-label">Giá cơ bản (VND)</label>
                    <input type="number" name="basePrice" class="form-control" value="${wellnessService.basePrice}" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Thời lượng (phút)</label>
                    <input type="number" name="durationMinutes" class="form-control" value="${wellnessService.durationMinutes}" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Sức chứa</label>
                    <input type="number" name="capacity" class="form-control" value="${wellnessService.capacity}" required>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Giờ hoạt động</label>
                <input type="text" name="operatingHours" class="form-control" value="${wellnessService.operatingHours}">
            </div>

            <div class="mb-3">
                <label class="form-label">Trạng thái</label>
                <select name="status" class="form-select">
                    <option value="ACTIVE" ${wellnessService.status eq 'ACTIVE' ? 'selected' : ''}>Hoạt động</option>
                    <option value="INACTIVE" ${wellnessService.status eq 'INACTIVE' ? 'selected' : ''}>Ngừng</option>
                </select>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <a href="wellness-service?action=list" class="btn btn-outline-secondary">⬅ Quay lại</a>
                <button type="submit" class="btn btn-primary px-4">Cập nhật</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
