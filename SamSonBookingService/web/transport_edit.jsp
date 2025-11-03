<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chỉnh sửa Phương Tiện</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
  <div class="card shadow-sm">
    <div class="card-body">
      <h4 class="mb-3">Chỉnh sửa Phương Tiện</h4>

      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>

      <c:url var="editUrl" value="/transport-edit"/>
      <form method="post" action="${editUrl}">
        <!-- ID ẩn -->
        <input type="hidden" name="transportId" value="${item.transportId}"/>

        <!-- Category ID -->
        <div class="mb-3">
          <label class="form-label">Category ID</label>
          <input type="number" name="categoryId" min="1" step="1"
                 class="form-control ${not empty errCategoryId ? 'is-invalid' : ''}"
                 value="${param.categoryId != null ? param.categoryId : item.categoryId}">
          <c:if test="${not empty errCategoryId}">
            <div class="invalid-feedback">${errCategoryId}</div>
          </c:if>
        </div>

        <!-- Loại phương tiện -->
        <div class="mb-3">
          <label class="form-label">Loại phương tiện</label>
          <select name="vehicleType" class="form-select ${not empty errVehicleType ? 'is-invalid':''}">
            <c:set var="vt" value="${not empty param.vehicleType ? param.vehicleType : item.vehicleType}"/>
            <option value="">-- Chọn --</option>
            <option value="CAR"       ${vt=='CAR'?'selected':''}>CAR</option>
            <option value="MINIVAN"   ${vt=='MINIVAN'?'selected':''}>MINIVAN</option>
            <option value="BUS"       ${vt=='BUS'?'selected':''}>BUS</option>
            <option value="LIMOUSINE" ${vt=='LIMOUSINE'?'selected':''}>LIMOUSINE</option>
            <option value="SELF"      ${vt=='SELF'?'selected':''}>SELF</option>
          </select>
          <c:if test="${not empty errVehicleType}">
            <div class="invalid-feedback d-block">${errVehicleType}</div>
          </c:if>
        </div>

        <!-- Tên phương tiện -->
        <div class="mb-3">
          <label class="form-label">Tên phương tiện</label>
          <input type="text" name="vehicleName"
                 class="form-control ${not empty errVehicleName ? 'is-invalid' : ''}"
                 value="${param.vehicleName != null ? param.vehicleName : item.vehicleName}">
          <c:if test="${not empty errVehicleName}">
            <div class="invalid-feedback">${errVehicleName}</div>
          </c:if>
        </div>

        <!-- Mô tả -->
        <div class="mb-3">
          <label class="form-label">Mô tả</label>
          <textarea name="description" rows="3"
                    class="form-control ${not empty errDescription ? 'is-invalid' : ''}">${param.description != null ? param.description : item.description}</textarea>
          <c:if test="${not empty errDescription}">
            <div class="invalid-feedback">${errDescription}</div>
          </c:if>
        </div>

        <div class="row">
          <!-- Điểm đón -->
          <div class="col-md-6 mb-3">
            <label class="form-label">Điểm đón</label>
            <input type="text" name="pickupLocation"
                   class="form-control ${not empty errPickup ? 'is-invalid' : ''}"
                   value="${param.pickupLocation != null ? param.pickupLocation : item.pickupLocation}">
            <c:if test="${not empty errPickup}">
              <div class="invalid-feedback">${errPickup}</div>
            </c:if>
          </div>

          <!-- Khởi hành -->
          <div class="col-md-6 mb-3">
            <label class="form-label">Khởi hành (yyyy-MM-dd HH:mm)</label>

            <%-- 1) Quyết định chuỗi hiển thị cho input --%>
            <c:set var="depVal" value="${not empty departureStr ? departureStr : (not empty param.departureTime ? param.departureTime : '')}"/>
            <c:if test="${empty depVal && item.departureTime != null}">
              <fmt:formatDate value="${item.departureTime}" pattern="yyyy-MM-dd HH:mm" var="depVal"/>
            </c:if>

            <%-- 2) Đổi 'T' -> khoảng trắng nếu người dùng copy từ datetime-local --%>
            <c:set var="depValClean" value="${fn:replace(depVal,'T',' ')}"/>

            <input type="text" name="departureTime"
                   class="form-control ${not empty errDeparture ? 'is-invalid' : ''}"
                   placeholder="yyyy-MM-dd HH:mm"
                   pattern="[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}"
                   title="Định dạng: yyyy-MM-dd HH:mm"
                   value="${depValClean}"
                   oninput="this.setCustomValidity(''); this.value=this.value.replace(/\u00A0/g,' ').replace(/T/,' ').replace(/\s+/g,' ');"
                   oninvalid="this.setCustomValidity('Vui lòng nhập đúng định dạng yyyy-MM-dd HH:mm');">
            <c:if test="${not empty errDeparture}">
              <div class="invalid-feedback">${errDeparture}</div>
            </c:if>
          </div>
        </div>

        <div class="row">
          <!-- Giá -->
          <div class="col-md-6 mb-3">
            <label class="form-label">Giá (đ)</label>
            <input type="number" name="price" min="80000" step="1000"
                   class="form-control ${not empty errPrice ? 'is-invalid' : ''}"
                   value="${not empty priceStr ? priceStr : (param.price != null ? param.price : item.price)}">
            <c:if test="${not empty errPrice}">
              <div class="invalid-feedback">${errPrice}</div>
            </c:if>
          </div>

          <!-- Sức chứa -->
          <div class="col-md-6 mb-3">
            <label class="form-label">Sức chứa</label>
            <input type="number" name="capacity" min="1" max="44" step="1"
                   class="form-control ${not empty errCapacity ? 'is-invalid' : ''}"
                   value="${not empty capacityStr ? capacityStr : (param.capacity != null ? param.capacity : item.capacity)}">
            <c:if test="${not empty errCapacity}">
              <div class="invalid-feedback">${errCapacity}</div>
            </c:if>
          </div>
        </div>

        <div class="d-flex justify-content-between mt-3">
          <c:url var="listUrl" value="/transport-service"><c:param name="action" value="list"/></c:url>
          <a href="${listUrl}" class="btn btn-outline-secondary">⬅ Quay lại</a>
          <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
</html>