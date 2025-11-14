<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật Banner</title>

    <style>
        body { font-family: system-ui, sans-serif; margin: 24px; background: #f3f4f6; }
        .page-container { max-width: 800px; margin: 0 auto; }
        .page-title { font-size: 22px; font-weight: 600; margin-bottom: 16px; }

        .card {
            background: #ffffff;
            border-radius: 12px;
            border: 1px solid #e5e7eb;
            box-shadow: 0 10px 25px rgba(15,23,42,0.06);
            padding: 18px 20px 22px;
        }

        .form-row { margin-bottom: 12px; }
        .label { display: block; font-size: 13px; font-weight: 600; margin-bottom: 4px; }
        .input, .textarea, .select {
            width: 100%;
            border-radius: 8px;
            border: 1px solid #d1d5db;
            padding: 7px 10px;
            font-size: 13px;
            box-sizing: border-box;
        }
        .textarea { min-height: 80px; resize: vertical; }
        .input:focus, .textarea:focus, .select:focus {
            outline: none;
            border-color: #2563eb;
            box-shadow: 0 0 0 1px rgba(37,99,235,0.22);
        }

        .thumb {
            width: 200px;
            height: 100px;
            object-fit: cover;
            border-radius: 8px;
            border: 1px solid #e5e7eb;
            background: #f3f4f6;
        }

        .btn {
            padding: 7px 14px;
            border-radius: 8px;
            border: none;
            cursor: pointer;
            font-size: 13px;
        }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-primary:hover { background: #1d4ed8; }
        .btn-secondary {
            background: #e5e7eb;
            color: #374151;
            text-decoration: none;
            display: inline-block;
            margin-left: 8px;
        }
        .btn-secondary:hover { background: #d1d5db; }

        .flex { display: flex; gap: 12px; flex-wrap: wrap; align-items: center; }
        .small-text { font-size: 12px; color: #6b7280; }

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
    <div class="page-title">Cập nhật Banner</div>

    <div class="card">
        <c:if test="${banner == null}">
            Không tìm thấy dữ liệu banner.
        </c:if>

        <c:if test="${banner != null}">

            <c:if test="${not empty error}">
                <div class="message-error">${error}</div>
            </c:if>

            <!-- Format sẵn giá trị datetime-local -->
            <fmt:formatDate value="${banner.startAt}" pattern="yyyy-MM-dd'T'HH:mm" var="startAtValue"/>
            <c:choose>
                <c:when test="${banner.endAt != null}">
                    <fmt:formatDate value="${banner.endAt}" pattern="yyyy-MM-dd'T'HH:mm" var="endAtValue"/>
                </c:when>
                <c:otherwise>
                    <c:set var="endAtValue" value=""/>
                </c:otherwise>
            </c:choose>

            <form action="${pageContext.request.contextPath}/banner_edit"
                  method="post"
                  enctype="multipart/form-data">

                <input type="hidden" name="bannerId" value="${banner.bannerId}"/>

                <div class="form-row">
                    <label class="label">Tiêu đề</label>
                    <input type="text" name="title" class="input" value="${banner.title}" required/>
                </div>

                <div class="form-row">
                    <label class="label">Mô tả</label>
                    <textarea name="description" class="textarea">${banner.description}</textarea>
                </div>

                <div class="form-row">
                    <label class="label">Target URL</label>
                    <input type="text" name="targetUrl" class="input" value="${banner.targetUrl}"/>
                    <div class="small-text">Ví dụ: /tours/summer, /wellness, /transport...</div>
                </div>

                <div class="form-row flex">
                    <div style="flex:1; min-width: 200px;">
                        <label class="label">Bắt đầu</label>
                        <input type="datetime-local" name="startAt" class="input"
                               value="${startAtValue}" required/>
                    </div>
                    <div style="flex:1; min-width: 200px;">
                        <label class="label">Kết thúc</label>
                        <input type="datetime-local" name="endAt" class="input"
                               value="${endAtValue}"/>
                        <div class="small-text">Để trống nếu không giới hạn.</div>
                    </div>
                </div>

                <div class="form-row">
                    <label class="label">Trạng thái</label>
                    <select name="status" class="select">
                        <option value="SHOW" ${banner.status == 'SHOW' ? 'selected' : ''}>SHOW (Hiện)</option>
                        <option value="HIDE" ${banner.status == 'HIDE' ? 'selected' : ''}>HIDE (Ẩn)</option>
                    </select>
                </div>

                <div class="form-row">
                    <label class="label">Ảnh hiện tại</label>
                    <div class="flex">
                        <c:if test="${not empty banner.imageUrl}">
                            <img class="thumb" src="${pageContext.request.contextPath}/${banner.imageUrl}" alt="">
                        </c:if>
                        <div class="small-text">
                            Đường dẫn hiện tại: ${banner.imageUrl}
                        </div>
                    </div>
                </div>

                <div class="form-row">
                    <label class="label">Tải ảnh mới (nếu muốn thay)</label>
                    <input type="file" name="imageFile" class="input" accept="image/*"/>
                    <div class="small-text">Nếu không chọn file, hệ thống sẽ giữ nguyên ảnh cũ.</div>
                </div>

                <div class="form-row" style="margin-top: 16px;">
                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    <a href="${pageContext.request.contextPath}/banner_list" class="btn btn-secondary">
                        Quay lại danh sách
                    </a>
                </div>

            </form>
        </c:if>
    </div>
</div>

</body>
</html>
