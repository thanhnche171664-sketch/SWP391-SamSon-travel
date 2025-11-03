<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entity.Booking" %>
<%
    Booking booking = (Booking) request.getAttribute("booking");
    String qrUrl = (String) request.getAttribute("qrUrl");
    String qrContent = (String) request.getAttribute("qrContent");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán VietQR - SamSon Travel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/booking.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/components.css">
    <script type="module">
      import { startPaymentPolling, confettiOnce, enableCopyButtons, enableImageDownload } from '<%=request.getContextPath()%>/assets/js/booking.js';
      window.addEventListener('load', ()=>{
        startPaymentPolling('<%=request.getContextPath()%>/payment/check?bookingId=<%=booking.getId()%>', ()=>{
          const st = document.querySelector('#status');
          if(st) st.textContent = 'Đã thanh toán';
          document.body.classList.add('paid');
          confettiOnce();
        });
        enableCopyButtons();
        enableImageDownload('#qrImage');
      });
    </script>
    <!-- Ref: VietQR pattern https://vietqr.co/api/generate/mb/529042003/SAM%20SON%20TRAVEL/100.000/DONATE?style=2 -->
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />
<section class="parallax-hero" aria-label="Thanh toán đơn đặt chỗ">
  <div class="blob" aria-hidden="true"></div>
  <div class="container" style="max-width:1100px;margin:0 auto;padding:0 16px;">
    <div class="stepper" aria-hidden="true">
      <div class="step active"></div>
      <div class="step active"></div>
      <div class="step active"></div>
    </div>
    <h1 style="margin:0 0 8px;font-size:clamp(24px,4vw,36px);font-weight:800">Hoàn tất thanh toán</h1>
    <p style="margin:0;color:#333;opacity:.9">Đơn hàng #<%=booking.getId()%> — <span id="status" aria-live="polite">Đang chờ thanh toán…</span></p>
  </div>
</section>

<main style="max-width:1100px;margin:-40px auto 40px;padding:0 16px;">
  <div class="glass-card" style="padding:24px;">
    <div style="display:grid;grid-template-columns:1.2fr .8fr;gap:24px;align-items:start;">
      <section aria-labelledby="booking-summary">
        <h2 id="booking-summary" class="tour-title" style="font-size:20px;margin:0 0 12px;display:flex;align-items:center;gap:8px;">
          Tóm tắt đặt chỗ
        </h2>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:12px;">
          <div class="glass-card" style="padding:14px;">
            <div style="font-size:12px;color:#555">Khách</div>
            <div style="font-weight:700"><%=booking.getGuestCount() == null ? 1 : booking.getGuestCount()%> người</div>
          </div>
          <div class="glass-card" style="padding:14px;">
            <div style="font-size:12px;color:#555">Liên hệ</div>
            <div style="font-weight:700"><%=booking.getContactName()%> · <%=booking.getContactPhone()%></div>
          </div>
          <div class="glass-card" style="padding:14px;">
            <div style="font-size:12px;color:#555">Ngày nhận phòng</div>
            <div style="font-weight:700"><%=booking.getCheckInDate() == null ? "—" : booking.getCheckInDate()%></div>
          </div>
          <div class="glass-card" style="padding:14px;">
            <div style="font-size:12px;color:#555">Ngày trả phòng</div>
            <div style="font-weight:700"><%=booking.getCheckOutDate() == null ? "—" : booking.getCheckOutDate()%></div>
          </div>
        </div>

        <div class="glass-card" style="padding:16px;">
          <div style="font-weight:700;margin-bottom:8px">Chi tiết dịch vụ</div>
          <ul style="list-style:none;padding:0;margin:0;display:grid;gap:10px">
            <%
              java.util.List<entity.BookingItem> items = booking.getBookingItems();
              if (items != null && !items.isEmpty()) {
                for (entity.BookingItem it : items) {
            %>
              <li style="display:flex;justify-content:space-between;gap:12px;align-items:center;background:#fff7;backdrop-filter: blur(6px);padding:12px;border-radius:12px">
                <div>
                  <div style="font-weight:700"><%=it.getTitleSnapshot()%></div>
                  <div style="font-size:12px;color:#555"><%=it.getItemType()%> · x<%=it.getQuantity()%></div>
                </div>
                <div style="font-weight:700"><%=new java.text.DecimalFormat("###,###,###").format(Math.round(it.getTotalPrice())).replace(",",".")%> VND</div>
              </li>
            <%
                }
              } else {
            %>
              <li style="color:#555">Không có mục chi tiết. Vui lòng kiểm tra lại đơn đặt chỗ.</li>
            <%
              }
            %>
          </ul>

          <div style="margin-top:12px">
            <div class="price-row"><span>Tạm tính</span><span><%=booking.getSubtotal()==null?"—": new java.text.DecimalFormat("###,###,###").format(Math.round(booking.getSubtotal())).replace(",",".") + " VND"%></span></div>
            <div class="price-row"><span>Giảm giá</span><span><%=booking.getDiscountTotal()==null?"—": new java.text.DecimalFormat("###,###,###").format(Math.round(booking.getDiscountTotal())).replace(",",".") + " VND"%></span></div>
            <div class="price-row"><span>Thuế phí</span><span><%=booking.getTaxTotal()==null?"—": new java.text.DecimalFormat("###,###,###").format(Math.round(booking.getTaxTotal())).replace(",",".") + " VND"%></span></div>
            <div class="price-row price-total"><span>Tổng thanh toán</span><span><%=request.getAttribute("amountFormatted")%></span></div>
          </div>
        </div>
      </section>

      <aside aria-labelledby="qr-section">
        <h2 id="qr-section" class="tour-title" style="font-size:20px;margin:0 0 12px">Thanh toán qua VietQR</h2>
        <div class="qr-card" style="margin:0">
          <img id="qrImage" class="qr-img" src="<%=qrUrl%>" alt="Mã VietQR cho đơn #<%=booking.getId()%>">
          <div class="qr-meta" style="text-align:left">
            <div style="margin:8px 0">Số tiền: <span class="chip"><%=request.getAttribute("amountFormatted")%></span></div>
            <div style="margin:8px 0">Nội dung chuyển khoản: <span class="chip" id="transferContent"><%=qrContent%></span></div>
            <div style="display:flex;gap:8px;flex-wrap:wrap;margin:12px 0">
              <button class="btn-primary" data-copy="#transferContent">Copy nội dung</button>
              <button class="btn-primary" data-download="#qrImage">Tải QR PNG</button>
            </div>
            <div style="font-size:12px;color:#555;line-height:1.5">
              Ngân hàng: MB Bank — Chủ TK: SAM SON TRAVEL — Số TK: 5290 42003<br>
              Lưu ý: Vui lòng chuyển khoản đúng số tiền và nội dung để hệ thống xác nhận nhanh.
            </div>
            <div id="status" style="margin-top:10px">Đang chờ thanh toán…</div>
          </div>
          <a class="btn-primary" href="<%=request.getContextPath()%>/home" style="margin-top:12px;">Quay lại Trang chủ</a>
        </div>
      </aside>
    </div>
  </div>
</main>
<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>


