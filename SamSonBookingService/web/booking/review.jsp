<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="util.PricingService" %>
<%
    Integer tourId = (Integer) request.getAttribute("tourId");
    Integer scheduleId = (Integer) request.getAttribute("scheduleId");
    Integer packageId = (Integer) request.getAttribute("packageId");
    Integer guestCount = (Integer) request.getAttribute("guestCount");
    PricingService.PriceBreakdown price = (PricingService.PriceBreakdown) request.getAttribute("price");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Review Booking - SamSon Travel</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/booking.css">
</head>
<body>
<div class="glass-card" style="max-width:760px;margin:28px auto;padding:22px">
    <div class="stepper" role="progressbar" aria-label="Các bước đặt tour">
        <div class="step"></div>
        <div class="step active"></div>
        <div class="step"></div>
    </div>
    <h2>Xác nhận thông tin</h2>
    <div class="price-row"><span>Khách:</span><strong><%=guestCount%></strong></div>
    <div class="price-row"><span>Subtotal:</span><strong><%=String.format("%,.0f", price.subtotal)%> VNĐ</strong></div>
    <div class="price-row"><span>Thuế:</span><strong><%=String.format("%,.0f", price.tax)%> VNĐ</strong></div>
    <div class="price-row"><span>Giảm:</span><strong><%=String.format("%,.0f", price.discount)%> VNĐ</strong></div>
    <div class="price-row price-total"><span>Tổng:</span><strong><%=String.format("%,.0f", price.total)%> VNĐ</strong></div>

    <form method="post" action="<%=request.getContextPath()%>/booking/confirm">
        <input type="hidden" name="tourId" value="<%=tourId%>">
        <input type="hidden" name="scheduleId" value="<%=scheduleId%>">
        <input type="hidden" name="packageId" value="<%=packageId%>">
        <input type="hidden" name="guestCount" value="<%=guestCount%>">
        <input type="hidden" name="_csrf" value="<%=request.getAttribute("csrfToken")%>">
        <label>Tên liên hệ: <input name="contactName" required aria-label="Tên liên hệ"></label><br>
        <label>Email: <input name="contactEmail" aria-label="Email"></label><br>
        <label>Điện thoại: <input name="contactPhone" aria-label="Điện thoại"></label><br>
        <button class="btn-primary" type="submit" aria-label="Đặt và thanh toán">Đặt và thanh toán</button>
    </form>
</div>
</body>
</html>


