<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách dịch vụ vận chuyển</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8fafc;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.08);
        }
        .input-group .form-control {
            border-radius: 12px 0 0 12px;
        }
        .input-group .btn {
            border-radius: 0 12px 12px 0;
        }
        table img {
            width: 80px;
            height: 55px;
            object-fit: cover;
            border-radius: 6px;
        }
        .btn {
            border-radius: 6px;
        }
    </style>
</head>
<body class="bg-light">
<fmt:setLocale value="vi_VN"/>
<div class="container mt-5">
    <div class="card p-4 shadow-sm">

        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="fw-bold mb-0 text-primary">Danh sách dịch vụ vận chuyển</h3>
            <a href="${pageContext.request.contextPath}/transport-add" class="btn btn-success fw-semibold">+ Thêm mới</a>
        </div>

        <form method="get" action="${pageContext.request.contextPath}/transport-list" class="mb-4">
            <div class="input-group input-group-lg">
                <input type="text"
                       name="search"
                       value="${search}"
                       class="form-control"
                       placeholder="🔍 Tìm theo tên xe hoặc điểm đón...">
                <button class="btn btn-primary px-4 fw-semibold" type="submit">Tìm kiếm</button>
            </div>
        </form>

        <c:if test="${param.message eq 'add_success'}">
            <div class="alert alert-success">✅ Thêm dịch vụ thành công!</div>
        </c:if>
        <c:if test="${param.message eq 'update_success'}">
            <div class="alert alert-success">✅ Cập nhật dịch vụ thành công!</div>
        </c:if>
        <c:if test="${param.message eq 'deleted'}">
            <div class="alert alert-success">🗑️ Xóa dịch vụ thành công!</div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Ảnh</th>
                    <th>Tên xe</th>
                    <th>Loại</th>
                    <th>Điểm đón</th>
                    <th>Giờ đi</th>
                    <th>Giá</th>
                    <th>Sức chứa</th>
                    <th class="text-center">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="t" items="${list}">
                    <tr>
                        <td>${t.transportId}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty t.image}">
                                    <img src="${pageContext.request.contextPath}/${t.image}" alt="${t.vehicleName}">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/Imagetransport/default_transport.jpg" alt="No image">
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${t.vehicleName}</td>
                        <td>${t.vehicleType}</td>
                        <td>${t.pickupLocation}</td>
                        <td>
                            <fmt:formatDate value="${t.departureTime}" pattern="yyyy-MM-dd HH:mm"/>
                        </td>
                        <td>
                            <fmt:formatNumber value="${t.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </td>
                        <td>${t.capacity}</td>
                        <td class="text-center">
                            <a href="${pageContext.request.contextPath}/transport-edit?id=${t.transportId}" class="btn btn-sm btn-outline-primary me-1">Sửa</a>
                            <a href="${pageContext.request.contextPath}/transport-delete?id=${t.transportId}"
                               class="btn btn-sm btn-outline-danger"
                               onclick="return confirm('Bạn có chắc muốn xóa dịch vụ này?');">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty list}">
                    <tr><td colspan="9" class="text-center text-muted py-4">Không có dữ liệu phù hợp.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages gt 1}">
            <nav class="mt-3">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link"
                           href="transport-list?page=${currentPage - 1}&search=${fn:escapeXml(search)}">Trước</a>
                    </li>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link"
                               href="transport-list?page=${i}&search=${fn:escapeXml(search)}">${i}</a>
                        </li>
                    </c:forEach>

                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link"
                           href="transport-list?page=${currentPage + 1}&search=${fn:escapeXml(search)}">Sau</a>
                    </li>
                </ul>
            </nav>
        </c:if>

    </div>
</div>
</body>
</html>
