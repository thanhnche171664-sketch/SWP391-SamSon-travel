<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dao.RoomDAO, entity.Room, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test Rooms Data</title>
    <style>
        body { font-family: Arial; padding: 20px; background: #f5f5f5; }
        .box { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { padding: 12px; text-align: left; border: 1px solid #ddd; }
        th { background: #4CAF50; color: white; }
        tr:nth-child(even) { background: #f9f9f9; }
        .success { color: green; font-weight: bold; }
        .error { color: red; font-weight: bold; }
    </style>
</head>
<body>
    <h1>🔍 TEST DATABASE - ROOMS</h1>
    
    <div class="box">
        <h2>📊 Kết quả kiểm tra:</h2>
        <%
        try {
            RoomDAO roomDAO = new RoomDAO();
            List<Room> rooms = roomDAO.getAllRooms();
            
            if (rooms != null && !rooms.isEmpty()) {
                out.println("<p class='success'>✅ KẾT NỐI DATABASE THÀNH CÔNG!</p>");
                out.println("<p class='success'>✅ Tìm thấy " + rooms.size() + " phòng trong database</p>");
                
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Hotel ID</th><th>Room Type</th><th>Price</th><th>Total Rooms</th><th>Available Rooms</th></tr>");
                
                for (Room room : rooms) {
                    out.println("<tr>");
                    out.println("<td>" + room.getId() + "</td>");
                    out.println("<td>" + room.getHotelId() + "</td>");
                    out.println("<td>" + room.getRoomType() + "</td>");
                    out.println("<td>" + String.format("%,.0f", room.getPrice()) + " VND</td>");
                    out.println("<td>" + room.getTotalRooms() + "</td>");
                    out.println("<td>" + room.getAvailableRooms() + "</td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
            } else {
                out.println("<p class='error'>❌ DATABASE KHÔNG CÓ DỮ LIỆU!</p>");
                out.println("<p>Vui lòng chạy file SQL: <code>database/setup_database.sql</code></p>");
            }
            
        } catch (Exception e) {
            out.println("<p class='error'>❌ LỖI KẾT NỐI DATABASE!</p>");
            out.println("<p class='error'>Chi tiết: " + e.getMessage() + "</p>");
            out.println("<p>Stack trace:</p><pre>");
            e.printStackTrace(new java.io.PrintWriter(out));
            out.println("</pre>");
        }
        %>
    </div>
    
    <div class="box">
        <h3>📋 Hướng dẫn nếu không có dữ liệu:</h3>
        <ol>
            <li>Mở <strong>SQL Server Management Studio</strong></li>
            <li>Mở file: <code>database/setup_database.sql</code></li>
            <li>Nhấn <strong>Execute</strong> (F5)</li>
            <li>Refresh trang này</li>
        </ol>
    </div>
    
    <p><a href="room-list" style="display: inline-block; padding: 10px 20px; background: #4CAF50; color: white; text-decoration: none; border-radius: 5px;">« Quay lại Room List</a></p>
</body>
</html>

