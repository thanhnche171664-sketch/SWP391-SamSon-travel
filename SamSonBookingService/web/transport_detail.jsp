<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết dịch vụ vận chuyển</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8fafc;
            font-family: 'Segoe UI', sans-serif;
        }
        .card {
            border: none;
            border-radius: 16px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        }
        .detail-label {
            font-weight: 600;
            color: #374151;
            width: 180px;
        }
        .detail-value {
            color: #111827;
        }
        .badge-type {
            font-size: 0.85rem;
        }
        .transport-image {
            width: 100%;
            max-width: 320px;
            height: 200px;
            object-fit: cover;
            border-radius: 10px;
        }
        .back-box {
            margin-top: 25px;
        }
    </style>
</head>
<body class="bg-light">
<fmt:setLocale value="vi_VN"/>

<div class="container mt-5 mb-5">
    <div class="card p-4">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="mb-0 text-primary">
                Chi tiết dịch vụ vận chuyển
            </h3>

            <!-- ❌ LOẠI BỎ nút quay lại ở đây -->
            <a href="${pageContext.request.contextPath}/transport-edit?id=${transport.transportId}"
               class="btn btn-sm btn-warning">
               Sửa
            </a>
        </div>


        <c:if test="${empty transport}">
            <div class="alert alert-danger">
                Không có dữ liệu dịch vụ để hiển thị.
            </div>
        </c:if>

        <c:if test="${not empty transport}">
            <div class="row">
                <!-- Hình ảnh -->
                <div class="col-md-4 mb-3">
                    <c:choose>
                        <c:when test="${not empty transport.image}">
                            <img class="transport-image"
                                 src="${pageContext.request.contextPath}/${transport.image}"
                                 alt="${transport.vehicleName}">
                        </c:when>
                        <c:otherwise>
                            <img class="transport-image"
                                 src="${pageContext.request.contextPath}/Imagetransport/default_transport.jpg"
                                 alt="No image">
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Thông tin -->
                <div class="col-md-8">

                    <div class="mb-2 d-flex">
                        <div class="detail-label">ID dịch vụ:</div>
                        <div class="detail-value">${transport.transportId}</div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Khách sạn (ID):</div>
                        <div class="detail-value">${transport.hotelId}</div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Danh mục (ID):</div>
                        <div class="detail-value">${transport.categoryId}</div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Tên xe:</div>
                        <div class="detail-value fw-semibold">${transport.vehicleName}</div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Loại xe:</div>
                        <div class="detail-value">
                            <span class="badge bg-info text-dark badge-type">${transport.vehicleType}</span>
                        </div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Điểm đón:</div>
                        <div class="detail-value">${transport.pickupLocation}</div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Giờ khởi hành:</div>
                        <div class="detail-value">
                            <fmt:formatDate value="${transport.departureTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Giá:</div>
                        <div class="detail-value text-danger fw-semibold">
                            <fmt:formatNumber value="${transport.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Sức chứa:</div>
                        <div class="detail-value">${transport.capacity} khách</div>
                    </div>

                    <div class="mb-2 d-flex">
                        <div class="detail-label">Mô tả:</div>
                        <div class="detail-value">
                            <pre style="white-space:pre-wrap; margin-bottom:0; font-family:inherit;">
${transport.description}
                            </pre>
                        </div>
                    </div>

                </div>
            </div>
        </c:if>

        <div class="back-box">
            <a href="${pageContext.request.contextPath}/transport-list" class="btn btn-secondary">
                ⬅ Quay lại danh sách
            </a>
        </div>

    </div>
</div>

</body>
</html>
