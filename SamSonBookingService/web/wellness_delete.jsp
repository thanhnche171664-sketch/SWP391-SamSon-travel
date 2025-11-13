<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xóa Dịch vụ Wellness</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body style="background-color:#f8fafc;">
        <div class="container mt-5">
            <div class="card p-4 shadow-lg rounded-4">
                <h3 class="text-center text-danger mb-4">Xóa Dịch vụ Wellness</h3>

                <p class="text-center mb-3">
                    Bạn có chắc chắn muốn xóa dịch vụ
                    <strong>"${wellnessService.serviceName}"</strong> không?
                </p>
                <p class="text-center text-muted">Thao tác này <strong>không thể hoàn tác</strong>.</p>

                <form method="post" action="${pageContext.request.contextPath}/wellness-delete">
                    <input type="hidden" name="wellnessId" value="${wellnessService.wellnessId}" />

                    <div class="d-flex justify-content-center gap-3 mt-4">
                        <a href="${pageContext.request.contextPath}/wellness-list" 
                           class="btn btn-secondary">⬅ Hủy</a>
                        <button type="submit" class="btn btn-danger px-4">Xóa</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
