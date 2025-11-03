<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Danh sách Dịch vụ </title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet"/>

  <style>
    :root{ --sb-bg1:#4f46e5; --sb-bg2:#7c3aed; --sb-text:#e5e7eb; --sb-text-dim:#cbd5e1; --active-bg:rgba(255,255,255,.18); }
    body{ background:#f8fafc; }
    .layout{ display:flex; min-height:100vh; }
    .sidebar{ width:250px; flex:0 0 250px; color:var(--sb-text); background:linear-gradient(180deg,var(--sb-bg1),var(--sb-bg2)); padding:18px 14px; }
    .brand{ font-weight:700; letter-spacing:.2px; }
    .sb-item{ display:flex; align-items:center; gap:10px; color:var(--sb-text-dim); text-decoration:none; padding:10px 12px; border-radius:12px; margin-bottom:6px; transition:background .15s, color .15s, transform .05s; }
    .sb-item:hover{ background:rgba(255,255,255,.12); color:#fff; transform:translateY(-1px); }
    .sb-item.active{ background:var(--active-bg); color:#fff; font-weight:600; }
    .content{ flex:1; padding:28px; }

    .card{ border:none; border-radius:16px; box-shadow:0 6px 18px rgba(0,0,0,.06); }
    .page-title{ color:#0f766e; font-weight:800; }
    .btn-primary{ background:#0d6efd; border:none; }
    .btn-primary:hover{ background:#0b5ed7; }
    .search-box input{ border-radius:10px 0 0 10px; }
    .search-box button{ border-radius:0 10px 10px 0; }
    .status-active{ color:#16a34a; font-weight:700; }
    .status-inactive{ color:#dc2626; font-weight:700; }

    @media (max-width: 992px){
      .sidebar{ position:fixed; z-index:1040; height:100vh; left:0; top:0; transform:translateX(-100%); transition:.2s; }
      .sidebar.show{ transform:translateX(0); }
      .content{ padding-top:64px; }
      .mobile-topbar{ position:fixed; top:0; left:0; right:0; z-index:1030; background:#fff; border-bottom:1px solid #e2e8f0; }
    }
  </style>
</head>
<body>

<div class="mobile-topbar d-lg-none d-flex align-items-center justify-content-between px-3 py-2">
  <button class="btn btn-outline-secondary btn-sm" id="toggleSidebar"><i class="bi bi-list"></i></button>
  <div class="fw-semibold">SamSon Travel</div>
  <div style="width:36px;"></div>
</div>

<div class="layout">
  <!-- SIDEBAR -->
  <aside class="sidebar" id="sidebar">
    <div class="d-flex align-items-center gap-2 mb-3 ps-2 brand">
      <i class="bi bi-tsunami fs-4"></i><span>SamSon Travel</span>
    </div>

    <nav class="mt-2">
        <a class="sb-item" href="<%=ctx%>/" data-match="/"><i class="bi bi-house"></i><span>Trang chủ</span></a>





        <a class="sb-item" href="<%=ctx%>/" data-match="/">
            <i class="bi bi-plus-circle"></i><span>Booking mới</span>
        </a>

        <a class="sb-item" href="<%=ctx%>/user-wellness" data-match="/user-wellness">
            <i class="bi bi-people"></i><span>Khách Hàng</span>
        </a>
        <a class="sb-item" href="<%=ctx%>/wellness-list" data-match="/wellness-list">
          <i class="bi bi-clipboard2-check"></i><span>Dịch vụ</span>
      </a>
      <a class="sb-item" href="<%=ctx%>/transport-service" data-match="/transport-service">
          <i class="bi bi-car-front"></i><span>Phương Tiện</span>
      </a>
      <a class="sb-item" href="<%=ctx%>/support" data-match="/support">
          <i class="bi bi-headset"></i><span>Hỗ trợ</span>
      </a>
    </nav>
  </aside>

  
  <main class="content">
    <div class="card p-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="page-title mb-0">
          <i class="bi bi-clipboard2-check-fill me-1"></i>Danh sách Dịch vụ Wellness
        </h3>
        <a href="<%=ctx%>/wellness-add" class="btn btn-primary">
          <i class="bi bi-plus-lg"></i> Thêm mới
        </a>
      </div>

      
      <form method="get" action="<%=ctx%>/wellness-list" class="d-flex mb-3 search-box">
        <input type="hidden" name="action" value="search"/>
        <input type="text" name="keyword" class="form-control" placeholder="Tìm theo tên dịch vụ..." value="${keyword}">
        <button class="btn btn-success px-4" type="submit">Tìm kiếm</button>
      </form>

      
      <form method="get" action="<%=ctx%>/wellness-list" class="mb-3 d-flex align-items-center gap-2">
        <input type="hidden" name="action" value="list"/>
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
            <th>ID</th><th>Tên dịch vụ</th><th>Mô tả</th><th>Giá cơ bản</th>
            <th>Thời lượng</th><th>Sức chứa</th><th>Trạng thái</th><th>Thao tác</th>
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
                <span class="${ws.status eq 'ACTIVE' ? 'status-active' : 'status-inactive'}">${ws.status}</span>
              </td>
              <td class="d-flex gap-2">
                <a href="<%=ctx%>/wellness-detail?id=${ws.wellnessId}" class="btn btn-sm btn-outline-info">Chi tiết</a>
                <a href="<%=ctx%>/wellness-edit?id=${ws.wellnessId}" class="btn btn-sm btn-outline-primary">Sửa</a>
                <a href="<%=ctx%>/wellness-delete?id=${ws.wellnessId}" class="btn btn-sm btn-outline-danger"
                   onclick="return confirm('Bạn có chắc muốn xóa dịch vụ này?');">Xóa</a>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty list}">
            <tr><td colspan="8" class="text-center text-muted py-4">Không có dữ liệu.</td></tr>
          </c:if>
          </tbody>
        </table>
      </div>
      
      <nav class="d-flex justify-content-center mt-4">
        <ul class="pagination">
          <c:forEach begin="1" end="${totalPages}" var="i">
            <li class="page-item ${i == currentPage ? 'active' : ''}">
              <a class="page-link" href="<%=ctx%>/wellness-list?action=list&page=${i}&status=${statusFilter}">${i}</a>
            </li>
          </c:forEach>
        </ul>
      </nav>
    </div>
  </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
  document.getElementById('toggleSidebar')?.addEventListener('click', () =>
    document.getElementById('sidebar')?.classList.toggle('show')
  );
  (function markActive(){
    const path = location.pathname;
    document.querySelectorAll('.sb-item').forEach(a=>{
      const m = a.getAttribute('data-match');
      if (m && path.indexOf(m) >= 0) a.classList.add('active');
    });
  })();
</script>
</body>
</html>
