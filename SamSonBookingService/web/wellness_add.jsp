<%@page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thêm Dịch vụ Wellness</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body style="background-color:#f8fafc;">
        <div class="container mt-5">
            <div class="card p-4 shadow-lg rounded-4">
                <h3 class="text-center text-success mb-4">➕ Thêm Dịch vụ Wellness Mới</h3>

                <form method="post" action="wellness-add">
                    <div class="mb-3">
                        <label class="form-label">Tên dịch vụ</label>
                        <input type="text" name="serviceName" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Mô tả</label>
                        <textarea name="description" class="form-control" rows="3" required></textarea>
                    </div>

                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Giá cơ bản (VND)</label>
                            <input type="number" name="basePrice" class="form-control" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Thời lượng (phút)</label>
                            <input type="number" name="durationMinutes" class="form-control" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Sức chứa</label>
                            <input type="number" name="capacity" class="form-control" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Giờ hoạt động (VD: 08:00–21:00)</label>
                        <input type="text" name="operatingHours" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Trạng thái</label>
                        <select name="status" class="form-select">
                            <option value="ACTIVE">Hoạt động</option>
                            <option value="INACTIVE">Ngừng</option>
                        </select>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <a href="wellness-list?action=list" class="btn btn-outline-secondary">⬅ Quay lại</a>
                        <button type="submit" class="btn btn-success px-4">Thêm mới</button>
                    </div>
                </form>

            </div>
        </div>
    </body>
</html>
