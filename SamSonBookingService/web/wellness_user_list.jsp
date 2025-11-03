<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Dịch vụ Wellness cho khách hàng</title>
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
            box-shadow: 0 3px 8px rgba(0,0,0,0.1);
            transition: transform 0.2s ease;
        }
        .card:hover {
            transform: scale(1.02);
        }
        .card img {
            width: 100%;
            height: 180px;
            object-fit: cover;
        }
        .price {
            color: #ff6600;
            font-weight: bold;
            font-size: 18px;
        }
        .btn-view {
            background-color: #198754;
            color: white;
        }
        .btn-view:hover {
            background-color: #146c43;
        }
        .btn-cart {
            background-color: #ff8c00;
            color: white;
        }
        .btn-cart:hover {
            background-color: #e67600;
        }
        .search-box {
            background-color: #fff;
            padding: 15px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <h2 class="text-center mb-4 text-success">🌿 Dịch vụ Wellness đang hoạt động</h2>

    <!-- 🔍 Thanh tìm kiếm -->
    <form method="get" action="user-wellness" class="row g-3 align-items-end mb-4 search-box">
        <div class="col-md-4">
            <label class="form-label fw-bold">Tìm theo tên dịch vụ</label>
            <input type="text" name="search" value="${keyword}" class="form-control" placeholder="Nhập tên dịch vụ...">
        </div>
        <div class="col-md-3">
            <label class="form-label fw-bold">Giá tối thiểu (VND)</label>
            <input type="number" name="minPrice" value="${minPrice}" class="form-control" placeholder="0">
        </div>
        <div class="col-md-3">
            <label class="form-label fw-bold">Giá tối đa (VND)</label>
            <input type="number" name="maxPrice" value="${maxPrice}" class="form-control" placeholder="1000000">
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-success w-100">Lọc</button>
        </div>
    </form>

    <div class="row g-4">
        <c:forEach var="ws" items="${services}">
            <div class="col-md-4 col-sm-6">
                <div class="card h-100">

                    <c:choose>
                        <c:when test="${not empty ws.imageUrl}">
                            <img src="${pageContext.request.contextPath}/${ws.imageUrl}"
                                 alt="${ws.serviceName}"
                                 class="card-img-top">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/Imagewellness/default_wellness.jpg"
                                 alt="No Image"
                                 class="card-img-top">
                        </c:otherwise>
                    </c:choose>

                    <div class="card-body">
                        <h5 class="card-title text-uppercase fw-bold">${ws.serviceName}</h5>
                        <p class="card-text text-muted">${ws.description}</p>
                        <p class="price">${ws.basePrice} đ</p>

                        <div class="d-flex justify-content-between mt-3 gap-2">
                            <a href="${pageContext.request.contextPath}/wellness-detail?id=${ws.wellnessId}"
                               class="btn btn-view btn-sm w-50 text-center">
                                Xem chi tiết
                            </a>

                            <form action="${pageContext.request.contextPath}/cart-add" method="post" class="w-50">
                                <input type="hidden" name="serviceId" value="${ws.wellnessId}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn btn-cart btn-sm w-100">
                                    Thêm giỏ hàng
                                </button>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </c:forEach>

        <c:if test="${empty services}">
            <div class="text-center text-muted py-5">Không tìm thấy dịch vụ phù hợp.</div>
        </c:if>
    </div>

    
    <c:if test="${totalPages > 1}">
        <nav aria-label="Page navigation" class="mt-5">
            <ul class="pagination justify-content-center">
                <c:forEach begin="1" end="${totalPages}" var="p">
                    <li class="page-item ${p == currentPage ? 'active' : ''}">
                        <a class="page-link"
                           href="user-wellness?page=${p}&search=${keyword}&minPrice=${minPrice}&maxPrice=${maxPrice}">
                            ${p}
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>
</div>

</body>
</html>