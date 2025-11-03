<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Danh sách Phương Tiện</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">Danh sách Dịch vụ Vận chuyển</h3>
    <c:url var="addUrl" value="/transport-add"/>
    <a href="${addUrl}" class="btn btn-primary">
      <i class="bi bi-plus-lg"></i> Thêm mới
    </a>
  </div>

  <c:if test="${not empty param.message}">
    <div class="alert alert-success">Thành công: ${param.message}</div>
  </c:if>
  <c:if test="${not empty param.error}">
    <div class="alert alert-danger">Lỗi: ${param.error}</div>
  </c:if>

  <c:url var="listUrl" value="/transport-service"/>
  <form method="get" action="${listUrl}" class="row g-2 mb-3">
    <input type="hidden" name="action" value="list"/>
    <div class="col-md-3">
      <label class="form-label">Loại xe</label>
      <select name="vehicleType" class="form-select" onchange="this.form.submit()">
        <c:set var="vt" value="${vehicleType != null ? vehicleType : 'all'}"/>
        <option value="all"      ${vt=='all'?'selected':''}>Tất cả</option>
        <option value="CAR"      ${vt=='CAR'?'selected':''}>Ô tô (CAR)</option>
        <option value="MINIVAN"  ${vt=='MINIVAN'?'selected':''}>Minivan</option>
        <option value="BUS"      ${vt=='BUS'?'selected':''}>Bus</option>
        <option value="LIMOUSINE"${vt=='LIMOUSINE'?'selected':''}>Limousine</option>
        <option value="SELF"     ${vt=='SELF'?'selected':''}>Tự lái (SELF)</option>
      </select>
    </div>
    <div class="col-md-7">
      <label class="form-label">Tìm theo tên</label>
      <input class="form-control" type="text" name="keyword" value="${keyword}" placeholder="VD: Limousine, Xe 7 chỗ...">
    </div>
    <div class="col-md-2 d-grid align-items-end">
      <button class="btn btn-success" type="submit">Tìm kiếm</button>
    </div>
  </form>

  <div class="card">
    <div class="table-responsive">
      <table class="table table-hover align-middle mb-0">
        <thead class="table-light">
        <tr>
          <th>ID</th>
          <th>Tên xe</th>
          <th>Loại</th>
          <th>Điểm đón</th>
          <th>Khởi hành</th>
          <th>Giá (đ)</th>
          <th>Sức chứa</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="t" items="${list}">
          <tr>
            <td>${t.transportId}</td>
            <td class="fw-semibold">${t.vehicleName}</td>
            <td><span class="badge text-bg-secondary">${t.vehicleType}</span></td>
            <td>${t.pickupLocation}</td>
            <td><fmt:formatDate value="${t.departureTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><fmt:formatNumber value="${t.price}" type="number" groupingUsed="true"/></td>
            <td>${t.capacity}</td>
            <td class="text-nowrap">
              <c:url var="editUrlRow" value="/transport-edit"><c:param name="id" value="${t.transportId}"/></c:url>
              <a class="btn btn-sm btn-outline-primary" href="${editUrlRow}">Sửa</a>
              <c:url var="delUrlRow" value="/transport-delete"><c:param name="id" value="${t.transportId}"/></c:url>
              <a class="btn btn-sm btn-outline-danger" href="${delUrlRow}" onclick="return confirm('Xóa dịch vụ này?');">Xóa</a>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty list}">
          <tr><td colspan="8" class="text-center text-muted py-4">Không có dữ liệu.</td></tr>
        </c:if>
        </tbody>
      </table>
    </div>
  </div>

  <nav class="d-flex justify-content-center mt-3">
    <ul class="pagination">
      <c:forEach begin="1" end="${totalPages}" var="i">
        <li class="page-item ${i == currentPage ? 'active' : ''}">
          <c:url var="pageUrl" value="/transport-service">
            <c:param name="action" value="list"/>
            <c:param name="page" value="${i}"/>
            <c:param name="vehicleType" value="${vehicleType}"/>
            <c:param name="keyword" value="${keyword}"/>
          </c:url>
          <a class="page-link" href="${pageUrl}">${i}</a>
        </li>
      </c:forEach>
    </ul>
  </nav>
</div>
</body>
</html>