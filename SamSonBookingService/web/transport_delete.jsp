<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xóa dịch vụ vận chuyển</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card p-4 shadow-sm">
        <h3 class="text-danger mb-3">Xóa dịch vụ vận chuyển</h3>
        <p>Bạn có chắc chắn muốn xóa dịch vụ
            <strong>${transport.vehicleName}</strong> không?</p>
        <form method="post" action="${pageContext.request.contextPath}/transport-delete">
            <input type="hidden" name="transportId" value="${transport.transportId}" />
            <div class="d-flex gap-3 mt-3">
                <a href="${pageContext.request.contextPath}/transport-list" class="btn btn-secondary">Hủy</a>
                <button type="submit" class="btn btn-danger">Xóa</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
