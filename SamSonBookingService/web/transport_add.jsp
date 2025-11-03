<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Thêm Phương Tiện</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
  <div class="card shadow-sm">
    <div class="card-body">
      <h4 class="mb-3">Thêm Dịch vụ Vận chuyển</h4>

      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>

      <c:url var="addUrl" value="/transport-add"/>
      <form method="post" action="${addUrl}" novalidate>
        <div class="mb-3">
          <label class="form-label">Category ID</label>
          <input type="number" name="categoryId" min="1" step="1"
                 class="form-control ${not empty errCategoryId ? 'is-invalid' : ''}"
                 value="${not empty categoryId ? categoryId : param.categoryId}">
          <c:if test="${not empty errCategoryId}">
            <div class="invalid-feedback">${errCategoryId}</div>
          </c:if>
        </div>

        <div class="mb-3">
          <label class="form-label">Loại phương tiện</label>
          <select name="vehicleType" class="form-select ${not empty errVehicleType ? 'is-invalid' : ''}">
            <c:set var="vt" value="${not empty vehicleType ? vehicleType : param.vehicleType}"/>
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

        <div class="mb-3">
          <label class="form-label">Tên phương tiện</label>
          <input type="text" name="vehicleName"
                 class="form-control ${not empty errVehicleName ? 'is-invalid' : ''}"
                 value="${not empty vehicleName ? vehicleName : param.vehicleName}">
          <c:if test="${not empty errVehicleName}">
            <div class="invalid-feedback">${errVehicleName}</div>
          </c:if>
        </div>

        <div class="mb-3">
          <label class="form-label">Mô tả</label>
          <textarea name="description" rows="3"
                    class="form-control ${not empty errDescription ? 'is-invalid' : ''}">${not empty description ? description : param.description}</textarea>
          <c:if test="${not empty errDescription}">
            <div class="invalid-feedback">${errDescription}</div>
          </c:if>
        </div>

        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label">Điểm đón</label>
            <input type="text" name="pickupLocation"
                   class="form-control ${not empty errPickup ? 'is-invalid' : ''}"
                   value="${not empty pickupLocation ? pickupLocation : param.pickupLocation}">
            <c:if test="${not empty errPickup}">
              <div class="invalid-feedback">${errPickup}</div>
            </c:if>
          </div>

          <div class="col-md-6 mb-3">
            <label class="form-label">Khởi hành (yyyy-MM-dd HH:mm)</label>
            <input type="text"
                   name="departureTime"
                   class="form-control ${not empty errDeparture ? 'is-invalid' : ''}"
                   placeholder="yyyy-MM-dd HH:mm"
                   pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}[ T][0-9]{2}:[0-9]{2}"
                   title="Định dạng: yyyy-MM-dd HH:mm (vd: 2025-01-01 08:00 hoặc 2025-01-01T08:00)"
                   autocomplete="off"
                   value="${fn:replace((not empty departureStr ? departureStr : param.departureTime), 'T', ' ')}"
                   oninput="this.setCustomValidity(''); this.value=this.value.replace(/\u00A0/g,' ').replace(/T/,' ').replace(/\s+/g,' ');"
                   oninvalid="this.setCustomValidity('Vui lòng nhập đúng định dạng yyyy-MM-dd HH:mm (vd: 2025-01-01 08:00)');">
            <c:if test="${not empty errDeparture}">
              <div class="invalid-feedback">${errDeparture}</div>
            </c:if>
          </div>
        </div>

        <div class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label">Giá (đ)</label>
            <input type="number" name="price" min="80000" step="1000"
                   class="form-control ${not empty errPrice ? 'is-invalid' : ''}"
                   value="${not empty priceStr ? priceStr : param.price}">
            <c:if test="${not empty errPrice}">
              <div class="invalid-feedback">${errPrice}</div>
            </c:if>
          </div>

          <div class="col-md-6 mb-3">
            <label class="form-label">Sức chứa</label>
            <input type="number" name="capacity" min="1" max="44" step="1"
                   class="form-control ${not empty errCapacity ? 'is-invalid' : ''}"
                   value="${not empty capacityStr ? capacityStr : param.capacity}">
            <c:if test="${not empty errCapacity}">
              <div class="invalid-feedback">${errCapacity}</div>
            </c:if>
          </div>
        </div>

        <div class="d-flex justify-content-between mt-3">
          <c:url var="listUrl2" value="/transport-service"><c:param name="action" value="list"/></c:url>
          <a href="${listUrl2}" class="btn btn-outline-secondary">⬅ Quay lại</a>
          <button type="submit" class="btn btn-primary">Thêm mới</button>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
</html>