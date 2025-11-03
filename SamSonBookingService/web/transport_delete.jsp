<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Xoá Dịch vụ Vận chuyển</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
  <div class="card shadow-sm">
    <div class="card-body">
      <h4 class="mb-3 text-danger">Xác nhận xoá</h4>

      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>

      <div class="mb-3">
        <div><strong>ID:</strong> ${item.transportId}</div>
        <div><strong>Tên xe:</strong> ${item.vehicleName}</div>
        <div><strong>Loại:</strong> ${item.vehicleType}</div>
        <div><strong>Điểm đón:</strong> ${item.pickupLocation}</div>
        <div><strong>Khởi hành:</strong> <fmt:formatDate value="${item.departureTime}" pattern="yyyy-MM-dd HH:mm"/></div>
        <div><strong>Giá:</strong> <fmt:formatNumber value="${item.price}" type="number" groupingUsed="true"/></div>
        <div><strong>Sức chứa:</strong> ${item.capacity}</div>
      </div>

      <div class="alert alert-warning">
        Thao tác này không thể hoàn tác. Bạn có chắc chắn muốn xoá dịch vụ này?
      </div>

      <div class="d-flex gap-2">
        <form method="post" action="<c:url value='/transport-delete'/>">
          <input type="hidden" name="id" value="${item.transportId}">
          <button class="btn btn-danger" type="submit">💥 Xoá</button>
        </form>

        <a class="btn btn-outline-secondary"
           href="<c:url value='/transport-service'><c:param name='action' value='list'/></c:url>">
          ← Huỷ
        </a>
      </div>
    </div>
  </div>
</div>
</body>
</html>