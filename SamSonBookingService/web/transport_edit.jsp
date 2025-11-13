<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Cập nhật dịch vụ vận chuyển</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <div class="card p-4 shadow-sm">
                <h3 class="mb-3 text-primary">Cập nhật dịch vụ vận chuyển</h3>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/transport-edit">
                    <input type="hidden" name="transportId" value="${transport.transportId}" />

                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Khách sạn</label>
                            <select name="hotelId" class="form-select" required>
                                <c:forEach var="h" items="${hotels}">
                                    <option value="${h.id}"
                                            <c:if test="${h.id == transport.hotelId}">
                                                selected
                                            </c:if>>
                                        ${h.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-4 mb-3">
                            <label class="form-label">Danh mục</label>
                            <input type="number" name="categoryId" class="form-control" value="2" readonly>
                        </div>

                        <div class="col-md-4 mb-3">
                            <label class="form-label">Loại xe</label>
                            <select name="vehicleType" class="form-select">
                                <option value="CAR"       ${transport.vehicleType == 'CAR' ? 'selected' : ''}>CAR</option>
                                <option value="MINIVAN"   ${transport.vehicleType == 'MINIVAN' ? 'selected' : ''}>MINIVAN</option>
                                <option value="BUS"       ${transport.vehicleType == 'BUS' ? 'selected' : ''}>BUS</option>
                                <option value="LIMOUSINE" ${transport.vehicleType == 'LIMOUSINE' ? 'selected' : ''}>LIMOUSINE</option>
                                <option value="SELF"      ${transport.vehicleType == 'SELF' ? 'selected' : ''}>SELF</option>
                            </select>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Tên xe</label>
                        <input type="text" name="vehicleName" class="form-control"
                               value="${transport.vehicleName}" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Mô tả</label>
                        <textarea name="description" class="form-control" rows="3" required>${transport.description}</textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Điểm đón</label>
                        <input type="text" name="pickupLocation" class="form-control"
                               value="${transport.pickupLocation}" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Thời gian khởi hành (yyyy-MM-dd HH:mm:ss)</label>
                        <input type="text" name="departureTime" class="form-control"
                               value="<fmt:formatDate value='${transport.departureTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"
                               required>
                    </div>

                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Giá</label>
                            <input type="number" name="price" class="form-control"
                                   step="0.01" min="0" value="${transport.price}" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Sức chứa</label>
                            <input type="number" name="capacity" class="form-control"
                                   min="1" value="${transport.capacity}" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Ảnh</label>
                            <input type="text" name="image" class="form-control" value="${transport.image}">
                        </div>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/transport-list" class="btn btn-secondary">⬅ Quay lại</a>
                        <button type="submit" class="btn btn-primary">Cập nhật</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>