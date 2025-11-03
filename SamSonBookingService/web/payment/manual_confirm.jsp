<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manual Payment Confirm</title>
    <style>
        .wrap{max-width:520px;margin:40px auto;background:#fff;border-radius:12px;box-shadow:0 10px 30px rgba(0,0,0,.08);padding:20px}
        .row{margin:10px 0}
        .btn{padding:10px 16px;background:#0a7;display:inline-block;color:#fff;border-radius:8px;text-decoration:none;border:0}
    </style>
 </head>
<body>
<div class="wrap">
    <h3>Manual Confirm Payment</h3>
    <form method="post" action="<%=request.getContextPath()%>/payment/manual-confirm">
        <input type="hidden" name="_csrf" value="<%=session.getAttribute("csrfToken")%>">
        <div class="row"><label>Booking ID <input name="bookingId" required></label></div>
        <div class="row"><label>Amount (VND) <input name="amount" required></label></div>
        <div class="row"><label>Transaction ID (optional) <input name="transactionId"></label></div>
        <button class="btn" type="submit">Confirm</button>
    </form>
</div>
</body>
</html>


