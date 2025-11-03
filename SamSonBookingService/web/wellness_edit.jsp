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
        <h3 class="text-center text-primary mb-4"> Cập nhật Dịch vụ Wellness</h3>

        <c:if test="${not empty error}">
            <div class="alert alert-danger mb-3">
                ${error}
            </div>
        </c:if>

        <form method="post" action="wellness-edit">
            <input type="hidden" name="wellnessId" value="${wellnessService.wellnessId}">
            <input type="hidden" name="hotelId" value="${wellnessService.hotelId}">
            <input type="hidden" name="categoryId" value="${wellnessService.categoryId}">

            <div class="mb-3">
                <label class="form-label">Tên dịch vụ</label>
                <input
                    type="text"
                    name="serviceName"
                    class="form-control"
                    value="${not empty param.serviceName ? param.serviceName : wellnessService.serviceName}"
                    required
                >
                <c:if test="${not empty errorServiceName}">
                    <small class="text-danger">${errorServiceName}</small>
                </c:if>
            </div>

            <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea name="description" class="form-control" rows="3" required>
${not empty param.description ? param.description : wellnessService.description}
                </textarea>
                <c:if test="${not empty errorDescription}">
                    <small class="text-danger">${errorDescription}</small>
                </c:if>
            </div>

            <div class="row">
                <!-- GIÁ CƠ BẢN -->
                <div class="col-md-4 mb-3">
                    <label class="form-label">Giá cơ bản (VND)</label>
                    <input
                        type="number"
                        name="basePrice"
                        class="form-control"
                        value="${not empty param.basePrice ? param.basePrice : wellnessService.basePrice}"
                        required
                    >
                    <c:if test="${not empty errorBasePrice}">
                        <small class="text-danger">${errorBasePrice}</small>
                    </c:if>
                </div>

                <div class="col-md-4 mb-3">
                    <label class="form-label">Thời lượng (phút)</label>
                    <input
                        type="number"
                        name="durationMinutes"
                        class="form-control"
                        value="${not empty param.durationMinutes ? param.durationMinutes : wellnessService.durationMinutes}"
                        required
                    >
                    <c:if test="${not empty errorDuration}">
                        <small class="text-danger">${errorDuration}</small>
                    </c:if>
                </div>

                <div class="col-md-4 mb-3">
                    <label class="form-label">Sức chứa</label>
                    <input
                        type="number"
                        name="capacity"
                        class="form-control"
                        value="${not empty param.capacity ? param.capacity : wellnessService.capacity}"
                        required
                    >
                    <c:if test="${not empty errorCapacity}">
                        <small class="text-danger">${errorCapacity}</small>
                    </c:if>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Giờ hoạt động (08:00–21:00)</label>
                <input
                    type="text"
                    name="operatingHours"
                    class="form-control"
                    value="${not empty param.operatingHours ? param.operatingHours : wellnessService.operatingHours}"
                >
                <c:if test="${not empty errorOperatingHours}">
                    <small class="text-danger">${errorOperatingHours}</small>
                </c:if>
            </div>

            <div class="mb-3">
                <label class="form-label">Trạng thái</label>
                <select name="status" class="form-select">
                    <c:set var="currentStatus" value="${not empty param.status ? param.status : wellnessService.status}" />
                    <option value="ACTIVE"  ${currentStatus == 'ACTIVE' ? 'selected' : ''}>Hoạt động</option>
                    <option value="INACTIVE" ${currentStatus == 'INACTIVE' ? 'selected' : ''}>Ngừng</option>
                </select>
                <c:if test="${not empty errorStatus}">
                    <small class="text-danger">${errorStatus}</small>
                </c:if>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <a href="wellness-edit?cancel=true&status=${status}&page=${page}" class="btn btn-outline-secondary">⬅ Quay lại</a>
                <button type="submit" class="btn btn-primary px-4">Cập nhật</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
