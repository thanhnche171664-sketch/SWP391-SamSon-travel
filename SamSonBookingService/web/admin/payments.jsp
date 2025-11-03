<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*, entity.Booking" %>
<%
    List<Booking> pendings = (List<Booking>) request.getAttribute("pendings");
    String csrf = (String) request.getAttribute("csrfToken");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Pending Payments - Admin</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/booking.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/components.css">
    <style>
        table{width:100%;border-collapse:collapse;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 10px 30px rgba(0,0,0,.08)}
        th,td{padding:12px 14px;border-bottom:1px solid #eef2f7;text-align:left}
        th{background:#f7f9fc;font-weight:700}
        .wrap{max-width:980px;margin:24px auto}
        .btn{padding:8px 12px;background:#0a7;color:#fff;border-radius:8px;text-decoration:none;border:0}
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/includes/header.jsp" />
<div class="wrap">
  <h2>Pending Payments</h2>
  <table aria-label="Pending Bookings">
    <thead>
      <tr>
        <th>ID</th><th>Contact</th><th>Total</th><th>Guest</th><th>Created</th><th>Action</th>
      </tr>
    </thead>
    <tbody>
    <%
      if (pendings != null){
        for (Booking b : pendings){
    %>
      <tr>
        <td><%=b.getId()%></td>
        <td><%=b.getContactName()%> (<%=b.getContactPhone()%>)</td>
        <td><%=String.format("%,.0f", b.getTotalPrice())%> VND</td>
        <td><%=b.getGuestCount()%></td>
        <td><%=b.getCreatedAt()%></td>
        <td>
          <form method="post" action="<%=request.getContextPath()%>/payment/manual-confirm" style="display:inline">
            <input type="hidden" name="_csrf" value="<%=csrf%>">
            <input type="hidden" name="bookingId" value="<%=b.getId()%>">
            <input type="hidden" name="amount" value="<%=b.getTotalPrice()%>">
            <button class="btn" type="submit">Mark as Paid</button>
          </form>
        </td>
      </tr>
    <%
        }
      }
    %>
    </tbody>
  </table>
</div>
<jsp:include page="/WEB-INF/includes/footer.jsp" />
</body>
</html>


