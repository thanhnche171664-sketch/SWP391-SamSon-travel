<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.ImageDAO" %>
<%@ page import="dao.HotelDAO" %>
<%@ page import="entity.Image" %>
<%@ page import="entity.Hotel" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test ImageDAO</title>
    <style>
        body { font-family: Arial; padding: 20px; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        .error { color: red; font-weight: bold; }
        .success { color: green; font-weight: bold; }
        h1 { color: #333; }
        h2 { color: #666; margin-top: 30px; }
        pre { background: #f4f4f4; padding: 15px; border-radius: 4px; overflow-x: auto; }
        ul { line-height: 1.8; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🔍 Test ImageDAO - Debug Images</h1>
        <p><strong>Mục đích:</strong> Kiểm tra xem code có đọc được ảnh từ database không</p>
        
        <%
            ImageDAO imageDAO = new ImageDAO();
            HotelDAO hotelDAO = new HotelDAO();
            
            try {
                List<Hotel> hotels = hotelDAO.getAllHotels();
                out.println("<h2>📊 Tổng số khách sạn: " + hotels.size() + "</h2>");
                
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>Hotel ID</th>");
                out.println("<th>Hotel Name</th>");
                out.println("<th>Primary Image URL</th>");
                out.println("<th>First Image URL</th>");
                out.println("<th>Số lượng ảnh</th>");
                out.println("</tr>");
                
                int hotelsWithImages = 0;
                int hotelsWithoutImages = 0;
                
                for (Hotel hotel : hotels) {
                    Image primaryImage = imageDAO.getPrimaryImage("hotel", hotel.getId());
                    Image firstImage = imageDAO.getFirstImage("hotel", hotel.getId());
                    int imageCount = imageDAO.countImages("hotel", hotel.getId());
                    
                    if (primaryImage != null || firstImage != null) {
                        hotelsWithImages++;
                    } else {
                        hotelsWithoutImages++;
                    }
                    
                    out.println("<tr>");
                    out.println("<td><strong>" + hotel.getId() + "</strong></td>");
                    out.println("<td>" + hotel.getName() + "</td>");
                    out.println("<td>" + (primaryImage != null ? "<span class='success'>" + primaryImage.getImageUrl() + "</span>" : "<span class='error'>NULL</span>") + "</td>");
                    out.println("<td>" + (firstImage != null ? "<span class='success'>" + firstImage.getImageUrl() + "</span>" : "<span class='error'>NULL</span>") + "</td>");
                    out.println("<td>" + imageCount + "</td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
                
                out.println("<h2>📈 Thống kê:</h2>");
                out.println("<ul>");
                out.println("<li><span class='success'>Khách sạn có ảnh: " + hotelsWithImages + "</span></li>");
                out.println("<li><span class='error'>Khách sạn không có ảnh: " + hotelsWithoutImages + "</span></li>");
                out.println("</ul>");
                
                // Test specific hotel
                out.println("<h2>🔬 Chi tiết test cho Hotel ID 1:</h2>");
                Image img1 = imageDAO.getPrimaryImage("hotel", 1);
                if (img1 != null) {
                    out.println("<p class='success'>✓ Tìm thấy Primary Image cho Hotel 1:</p>");
                    out.println("<ul>");
                    out.println("<li><strong>ID:</strong> " + img1.getId() + "</li>");
                    out.println("<li><strong>URL:</strong> " + img1.getImageUrl() + "</li>");
                    out.println("<li><strong>Is Primary:</strong> " + img1.isPrimary() + "</li>");
                    out.println("<li><strong>Entity Type:</strong> " + img1.getEntityType() + "</li>");
                    out.println("<li><strong>Entity ID:</strong> " + img1.getEntityId() + "</li>");
                    out.println("<li><strong>Alt Text:</strong> " + (img1.getAltText() != null ? img1.getAltText() : "NULL") + "</li>");
                    out.println("</ul>");
                    
                    // Test image proxy URL
                    String encodedUrl = java.net.URLEncoder.encode(img1.getImageUrl(), "UTF-8");
                    String proxyUrl = request.getContextPath() + "/image-proxy?url=" + encodedUrl;
                    out.println("<h3>🔗 Test Image Proxy URL:</h3>");
                    out.println("<p>Original URL: <code>" + img1.getImageUrl() + "</code></p>");
                    out.println("<p>Encoded URL: <code>" + encodedUrl + "</code></p>");
                    out.println("<p>Proxy URL: <code>" + proxyUrl + "</code></p>");
                    out.println("<p><a href='" + proxyUrl + "' target='_blank'>Mở ảnh qua proxy (click để test)</a></p>");
                } else {
                    out.println("<p class='error'>✗ KHÔNG tìm thấy Primary Image cho Hotel 1</p>");
                    out.println("<p><strong>Nguyên nhân có thể:</strong></p>");
                    out.println("<ul>");
                    out.println("<li>Chưa chạy script SQL insert ảnh</li>");
                    out.println("<li>Database không có data</li>");
                    out.println("<li>Lỗi kết nối database</li>");
                    out.println("</ul>");
                }
                
            } catch (Exception e) {
                out.println("<h2 class='error'>❌ Lỗi:</h2>");
                out.println("<p class='error'>" + e.getMessage() + "</p>");
                out.println("<h3>Stack Trace:</h3>");
                out.println("<pre>");
                e.printStackTrace(new java.io.PrintWriter(out));
                out.println("</pre>");
            }
        %>
        
        <h2>📝 SQL Query để kiểm tra trong database:</h2>
        <p>Chạy query này trong SQL Server Management Studio để xem data:</p>
        <pre>
SELECT 
    h.id AS hotel_id,
    h.name AS hotel_name,
    i.image_url,
    i.is_primary,
    i.alt_text
FROM Hotels h
LEFT JOIN Images i ON i.entity_type = 'hotel' AND i.entity_id = h.id AND i.is_primary = 1
ORDER BY h.id;
        </pre>
        
        <h2>📋 Hướng dẫn:</h2>
        <ol>
            <li>Nếu thấy <span class='error'>NULL</span> ở tất cả hotels → Chưa chạy script SQL insert ảnh</li>
            <li>Nếu thấy URL nhưng ảnh không hiển thị → Vấn đề ở ImageProxyServlet hoặc URL encoding</li>
            <li>Click vào link "Mở ảnh qua proxy" để test xem ImageProxyServlet có hoạt động không</li>
            <li>Kiểm tra server logs để xem có lỗi gì không</li>
        </ol>
        
        <p><a href="<%= request.getContextPath() %>/hotels">← Quay lại trang danh sách khách sạn</a></p>
    </div>
</body>
</html>

