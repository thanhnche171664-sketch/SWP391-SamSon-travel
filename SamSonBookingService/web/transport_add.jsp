<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm dịch vụ vận chuyển</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card p-4 shadow-sm">
        <h3 class="mb-3 text-success">Thêm dịch vụ vận chuyển</h3>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/transport-add">
            <div class="row">
                <div class="col-md-4 mb-3">
                    <label class="form-label">Khách sạn</label>
                    <input type="number" name="hotelId" class="form-control"
                           value="${param.hotelId != null ? param.hotelId : 1}" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Danh mục</label>
                    <!-- cố định luôn là 2 -->
                    <input type="number" name="categoryId" class="form-control" value="2" readonly>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Loại xe</label>
                    <select name="vehicleType" class="form-select">
                        <option value="CAR" ${param.vehicleType == 'CAR' ? 'selected' : ''}>CAR</option>
                        <option value="MINIVAN" ${param.vehicleType == 'MINIVAN' ? 'selected' : ''}>MINIVAN</option>
                        <option value="BUS" ${param.vehicleType == 'BUS' ? 'selected' : ''}>BUS</option>
                        <option value="LIMOUSINE" ${param.vehicleType == 'LIMOUSINE' ? 'selected' : ''}>LIMOUSINE</option>
                        <option value="SELF" ${param.vehicleType == 'SELF' ? 'selected' : ''}>SELF</option>
                    </select>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Tên xe</label>
                <input type="text" name="vehicleName" class="form-control"
                       value="${param.vehicleName}" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea name="description" class="form-control" rows="3" required>${param.description}</textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">Điểm đón</label>
                <input type="text" name="pickupLocation" class="form-control"
                       value="${param.pickupLocation}" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Thời gian khởi hành (yyyy-MM-dd HH:mm:ss)</label>
                <input type="text" name="departureTime" class="form-control"
                       value="${param.departureTime != null ? param.departureTime : '2025-01-01 08:00:00'}" required>
            </div>

            <div class="row">
                <div class="col-md-4 mb-3">
                    <label class="form-label">Giá</label>
                    <input type="number" name="price" step="0.01" min="0"
                           value="${param.price}" class="form-control" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Sức chứa</label>
                    <input type="number" name="capacity" min="1"
                           value="${param.capacity}" class="form-control" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label">Ảnh (đường dẫn)</label>
                    <input type="text" name="image" class="form-control"
                           value="${param.image}" placeholder="Imagetransport/tenanh.jpg">
                </div>
            </div>

            <div class="d-flex justify-content-between">
                <a href="${pageContext.request.contextPath}/transport-list" class="btn btn-secondary">⬅ Quay lại</a>
                <button type="submit" class="btn btn-success">Lưu</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
