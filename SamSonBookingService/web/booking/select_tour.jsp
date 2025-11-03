<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*, entity.Tour" %>
<%
    List<Tour> tours = (List<Tour>) request.getAttribute("tours");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Chọn Tour - SamSon Travel</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/main.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/booking.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/components.css">
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />
<div class="parallax-hero" role="banner" aria-label="Chọn Tour">
  <div class="blob" aria-hidden="true"></div>
  <h2 style="text-align:center">Chọn Tour</h2>
</div>
<div class="tour-grid" aria-live="polite">
    <%
        if (tours != null) {
            for (Tour t : tours) {
    %>
    <div class="glass-card tour-card">
        <h3 class="tour-title"><%=t.getTourName()%></h3>
        <p class="tour-desc"><%=t.getDescription()%></p>
        <form method="post" action="<%=request.getContextPath()%>/booking/review">
            <input type="hidden" name="tourId" value="<%=t.getTourId()%>">
            <input type="hidden" name="_csrf" value="<%=request.getAttribute("csrfToken")%>">
            <label>Lịch khởi hành: 
              <select name="scheduleId" required aria-label="Schedule">
                <option value="">Đang tải…</option>
              </select>
            </label>
            <label>Gói: 
              <select name="packageId" aria-label="Package">
                <option value="">(Không chọn)</option>
              </select>
            </label>
            <label>Số khách: <input type="number" name="guestCount" value="2" min="1" required aria-label="Guest Count"></label>
            <button class="btn-primary" type="submit" aria-label="Tiếp tục chọn tour">Tiếp tục</button>
        </form>
    </div>
    <%
            }
        }
    %>
</div>
<script type="module">
  import { initParallax } from '<%=request.getContextPath()%>/assets/js/booking.js';
  initParallax();
  const forms = document.querySelectorAll('form[action$="/booking/review"]');
  for (const f of forms) {
    const tourId = f.querySelector('input[name="tourId"]').value;
    const scheduleSel = f.querySelector('select[name="scheduleId"]');
    const packageSel = f.querySelector('select[name="packageId"]');
    fetch(`<%=request.getContextPath()%>/booking/api/schedules?tourId=${tourId}`)
      .then(r=>r.json()).then(list=>{
        scheduleSel.innerHTML = '<option value="">Chọn lịch</option>' +
          list.map(s=>`<option value="${s.scheduleId}">${new Date(s.departureDate).toLocaleDateString()} → ${new Date(s.returnDate).toLocaleDateString()} (${s.availableSlots - s.bookedSlots} chỗ)</option>`).join('');
      }).catch(()=>{ scheduleSel.innerHTML = '<option value="">Không tải được lịch</option>'; });
    fetch(`<%=request.getContextPath()%>/booking/api/packages?tourId=${tourId}`)
      .then(r=>r.json()).then(list=>{
        packageSel.innerHTML = '<option value="">(Không chọn)</option>' +
          list.map(p=>`<option value="${p.packageId}">${p.packageName} — ${p.formattedPrice || ''}</option>`).join('');
      }).catch(()=>{});
  }
</script>
<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>


