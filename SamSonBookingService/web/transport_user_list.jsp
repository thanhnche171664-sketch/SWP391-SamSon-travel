<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Dịch vụ vận chuyển</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8fafc;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            border: none;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 3px 8px rgba(0,0,0,0.08);
            transition: transform .15s ease;
            height: 100%;
        }
        .card:hover {
            transform: translateY(-3px);
        }
        .card img {
            width: 100%;
            height: 180px;
            object-fit: cover;
        }
        .price {
            color: #ff6600;
            font-weight: 600;
            font-size: 1rem;
        }
        .vehicle-type {
            font-size: .8rem;
            background: #e2f3ff;
            color: #0d6efd;
            display: inline-block;
            padding: 2px 10px;
            border-radius: 999px;
            margin-bottom: .5rem;
        }
        .search-box {
            background-color: #fff;
            padding: 15px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        }
        .btn-cart {
            background-color: #ff8c00;
            color: white;
        }
        .btn-cart:hover {
            background-color: #e67600;
        }
    </style>
</head>
<body>

<div class="container mt-5">

    <!-- Nút quay lại trang Home -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/home"
           class="btn btn-outline-primary">
            ⬅ Quay lại trang Home
        </a>
    </div>

    <h2 class="text-center mb-4 text-primary">🚐 Dịch vụ vận chuyển</h2>

    <form method="get" action="${pageContext.request.contextPath}/user-transport"
          class="row g-3 align-items-end mb-4 search-box">
        <div class="col-md-5">
            <label class="form-label fw-bold">Tìm theo tên xe</label>
            <input type="text" name="search" value="${param.search}" class="form-control"
                   placeholder="Ví dụ: Limousine, Mazda, Innova...">
        </div>
        <div class="col-md-3">
            <label class="form-label fw-bold">Điểm đón</label>
            <input type="text" name="pickup" value="${param.pickup}" class="form-control"
                   placeholder="Hà Nội - Sầm Sơn">
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-primary w-100">Lọc</button>
        </div>
    </form>

    <div class="row g-4">
        <c:forEach var="t" items="${services}">
            <c:if test="${empty param.search 
                         or fn:contains(fn:toLowerCase(t.vehicleName), fn:toLowerCase(param.search))
                         or fn:contains(fn:toLowerCase(t.pickupLocation), fn:toLowerCase(param.search))}">
                <div class="col-md-4 col-sm-6">
                    <div class="card">
                        <!-- Ảnh -->
                        <c:choose>
                            <c:when test="${not empty t.image}">
                                <img src="${pageContext.request.contextPath}/${t.image}" alt="${t.vehicleName}">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/Imagetransport/default_transport.jpg"
                                     alt="No image">
                            </c:otherwise>
                        </c:choose>

                        <div class="card-body d-flex flex-column">
                            <span class="vehicle-type">${t.vehicleType}</span>
                            <h5 class="card-title fw-bold mb-1">${t.vehicleName}</h5>
                            <p class="mb-1 text-muted" style="font-size: .9rem;">
                                Điểm đón: ${t.pickupLocation}
                            </p>
                            <p class="mb-1" style="font-size: .8rem;">
                                Khởi hành:
                                <fmt:formatDate value="${t.departureTime}" pattern="dd/MM/yyyy HH:mm" />
                            </p>
                            <p class="price mb-2">${t.price} đ</p>
                            <p class="mb-2" style="font-size: .8rem;">Sức chứa: ${t.capacity}</p>

                            <form action="${pageContext.request.contextPath}/cart-add" method="post" class="mt-auto">
                                <input type="hidden" name="serviceType" value="TRANSPORT">
                                <input type="hidden" name="serviceId" value="${t.transportId}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn btn-cart btn-sm w-100">
                                    🛒 Thêm giỏ hàng
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>

        <c:if test="${empty services}">
            <div class="text-center text-muted py-5">Không tìm thấy dịch vụ phù hợp.</div>
        </c:if>
    </div>
</div>

</body>
</html>
