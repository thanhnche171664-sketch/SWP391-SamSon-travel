<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thêm Banner mới</title>

        <style>
            body {
                font-family: system-ui, sans-serif;
                margin: 24px;
                background: #f3f4f6;
            }
            .page-container {
                max-width: 800px;
                margin: 0 auto;
            }
            .page-title {
                font-size: 22px;
                font-weight: 600;
                margin-bottom: 16px;
            }

            .card {
                background: #ffffff;
                border-radius: 12px;
                border: 1px solid #e5e7eb;
                box-shadow: 0 10px 25px rgba(15,23,42,0.06);
                padding: 18px 20px 22px;
            }

            .form-row {
                margin-bottom: 12px;
            }
            .label {
                display: block;
                font-size: 13px;
                font-weight: 600;
                margin-bottom: 4px;
            }
            .input, .textarea, .select {
                width: 100%;
                border-radius: 8px;
                border: 1px solid #d1d5db;
                padding: 7px 10px;
                font-size: 13px;
                box-sizing: border-box;
            }
            .textarea {
                min-height: 80px;
                resize: vertical;
            }
            .input:focus, .textarea:focus, .select:focus {
                outline: none;
                border-color: #2563eb;
                box-shadow: 0 0 0 1px rgba(37,99,235,0.22);
            }

            .btn {
                padding: 7px 14px;
                border-radius: 8px;
                border: none;
                cursor: pointer;
                font-size: 13px;
            }
            .btn-primary {
                background: #2563eb;
                color: #fff;
            }
            .btn-primary:hover {
                background: #1d4ed8;
            }
            .btn-secondary {
                background: #e5e7eb;
                color: #374151;
                text-decoration: none;
                display: inline-block;
                margin-left: 8px;
            }
            .btn-secondary:hover {
                background: #d1d5db;
            }

            .flex {
                display: flex;
                gap: 12px;
                flex-wrap: wrap;
                align-items: center;
            }
            .small-text {
                font-size: 12px;
                color: #6b7280;
            }

            .message-error {
                background: #fef2f2;
                color: #b91c1c;
                border: 1px solid #fecaca;
                padding: 8px 10px;
                border-radius: 8px;
                font-size: 13px;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>

        <div class="page-container">
            <div class="page-title">Thêm Banner mới</div>

            <div class="card">

                <c:if test="${not empty error}">
                    <div class="message-error">${error}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/banner_add"
                      method="post"
                      enctype="multipart/form-data">

                    <!-- TIÊU ĐỀ -->
                    <div class="form-row">
                        <label class="label">Tiêu đề</label>
                        <input type="text" name="title" class="input" required
                               value="${param.title}"/>
                    </div>

                    <!-- MÔ TẢ -->
                    <div class="form-row">
                        <label class="label">Mô tả</label>
                        <textarea name="description" class="textarea">${param.description}</textarea>
                    </div>

                    <!-- TARGET URL -->
                    <div class="form-row">
                        <label class="label">Target URL</label>
                        <input type="text" name="targetUrl" class="input"
                               value="${param.targetUrl}"/>
                        <div class="small-text">Ví dụ: /tours/summer, /wellness, /transport...</div>
                    </div>

                    <!-- THỜI GIAN BẮT ĐẦU & KẾT THÚC -->
                    <div class="form-row flex">
                        <div style="flex:1; min-width: 200px;">
                            <label class="label">Bắt đầu</label>
                            <input type="datetime-local" name="startAt" class="input" required
                                   value="${param.startAt}"/>
                        </div>
                        <div style="flex:1; min-width: 200px;">
                            <label class="label">Kết thúc</label>
                            <input type="datetime-local" name="endAt" class="input"
                                   value="${param.endAt}"/>
                            <div class="small-text">Để trống nếu không giới hạn.</div>
                        </div>
                    </div>

                    <!-- TRẠNG THÁI -->
                    <div class="form-row">
                        <label class="label">Trạng thái</label>
                        <select name="status" class="select">
                            <option value="SHOW" ${param.status == 'SHOW' || empty param.status ? 'selected' : ''}>
                                SHOW (Hiện)
                            </option>
                            <option value="HIDE" ${param.status == 'HIDE' ? 'selected' : ''}>
                                HIDE (Ẩn)
                            </option>
                        </select>
                    </div>

                    <!-- ẢNH -->
                    <div class="form-row">
                        <label class="label">Ảnh banner</label>
                        <input type="file" name="imageFile" class="input" accept="image/*"/>
                        <div class="small-text">Nên chọn ảnh kích thước ngang, ví dụ 1200x400.</div>
                    </div>

                    <!-- NÚT -->
                    <div class="form-row" style="margin-top: 16px;">
                        <button type="submit" class="btn btn-primary">Thêm mới</button>
                        <a href="${pageContext.request.contextPath}/banner_list" class="btn btn-secondary">
                            Quay lại danh sách
                        </a>
                    </div>

                </form>
            </div>
        </div>

    </body>
</html>
