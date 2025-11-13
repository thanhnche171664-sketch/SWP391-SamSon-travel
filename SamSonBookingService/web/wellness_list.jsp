<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách Dịch vụ Wellness</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8fafc;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.05);
            }
            .page-title {
                color: #007b5e;
                font-weight: 700;
            }
            .btn-primary {
                background-color: #0d6efd;
                border: none;
            }
            .btn-primary:hover {
                background-color: #0b5ed7;
            }
            .search-box input {
                border-radius: 8px 0 0 8px;
            }
            .search-box button {
                border-radius: 0 8px 8px 0;
            }
            table th {
                color: #222;
                font-weight: 600;
            }
            table td {
                vertical-align: middle;
            }
            .status-active {
                color: green;
                font-weight: 600;
            }
            .status-inactive {
                color: red;
                font-weight: 600;
            }
        </style>
    </head>
    <body>

        <div class="container mt-5">
            <div class="card p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h3 class="page-title">
                        <i class="bi bi-clipboard2-check-fill"></i>
                        Danh sách Dịch vụ Wellness
                    </h3>
                    <a href="wellness_add.jsp" class="btn btn-primary">
                        <i class="bi bi-plus-lg"></i> Thêm mới
                    </a>
                </div>

                <form method="get" action="wellness-search" class="d-flex mb-3 search-box">
                    <input type="text" name="search" class="form-control" placeholder="Tìm theo tên dịch vụ..."
                           value=${keyword}>
                    <button class="btn btn-success px-4" type="submit">Tìm kiếm</button>
                </form>

                <form method="get" action="wellness-list" class="mb-3 d-flex align-items-center gap-2">
                    <label class="form-label mb-0 fw-semibold">Trạng thái:</label>
                    <select name="status" class="form-select w-auto" onchange="this.form.submit()">
                        <option value="all" ${statusFilter eq 'all' ? 'selected' : ''}>Tất cả</option>
                        <option value="active" ${statusFilter eq 'active' ? 'selected' : ''}>Hoạt động</option>
                        <option value="inactive" ${statusFilter eq 'inactive' ? 'selected' : ''}>Ngừng</option>
                    </select>
                </form>

                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Tên dịch vụ</th>
                                <th>Mô tả</th>
                                <th>Giá cơ bản</th>
                                <th>Thời lượng</th>
                                <th>Sức chứa</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ws" items="${list}">
                                <tr>
                                    <td>${ws.wellnessId}</td>
                                    <td>${ws.serviceName}</td>
                                    <td>${ws.description}</td>
                                    <td>${ws.basePrice}</td>
                                    <td>${ws.durationMinutes}</td>
                                    <td>${ws.capacity}</td>
                                    <td>
                                        <span class="${ws.status eq 'ACTIVE' ? 'text-success' : 'text-danger'} fw-bold">
                                            ${ws.status}
                                        </span>
                                    </td>
                                    <td>
                                        <a href="wellness-detail?id=${ws.wellnessId}" class="btn btn-sm btn-outline-info">Chi tiết</a>
                                        <a href="wellness-edit?id=${ws.wellnessId}" class="btn btn-sm btn-outline-primary">Sửa</a>
                                        <a href="${pageContext.request.contextPath}/wellness-delete?id=${ws.wellnessId}"
                                           class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('Bạn có chắc muốn xóa dịch vụ này?');">Xóa</a>
                                    </td>

                                </tr>
                            </c:forEach>

                            <c:if test="${empty list}">
                                <tr>
                                    <td colspan="8" class="text-center text-muted py-4">Không có dữ liệu.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <nav class="d-flex justify-content-center mt-4">
                    <ul class="pagination">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="wellness-list?page=${i}&status=${statusFilter}">${i}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </nav>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
