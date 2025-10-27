<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="dao.DBContext" %>
<%@ page import="dao.MealServiceDAO" %>
<%@ page import="dao.WellnessServiceDAO" %>
<%@ page import="entity.MealService" %>
<%@ page import="entity.WellnessService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Database Connection Test - SamSon Travel</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 20px;
            min-height: 100vh;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        
        .header {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            margin-bottom: 30px;
            text-align: center;
        }
        
        .header h1 {
            color: #667eea;
            font-size: 2.5rem;
            margin-bottom: 10px;
        }
        
        .header p {
            color: #666;
            font-size: 1.1rem;
        }
        
        .test-section {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            margin-bottom: 20px;
        }
        
        .test-section h2 {
            color: #667eea;
            font-size: 1.8rem;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .status {
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: bold;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 20px;
        }
        
        .status.success {
            background: #d4edda;
            color: #155724;
            border: 2px solid #c3e6cb;
        }
        
        .status.error {
            background: #f8d7da;
            color: #721c24;
            border: 2px solid #f5c6cb;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin: 20px 0;
        }
        
        .info-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
        }
        
        .info-card .number {
            font-size: 3rem;
            font-weight: bold;
            margin: 10px 0;
        }
        
        .info-card .label {
            font-size: 1.1rem;
            opacity: 0.9;
        }
        
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: white;
        }
        
        .data-table th {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px;
            text-align: left;
            font-weight: bold;
        }
        
        .data-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
        }
        
        .data-table tr:hover {
            background: #f8f9fa;
        }
        
        .data-table tr:last-child td {
            border-bottom: none;
        }
        
        .price {
            color: #f5576c;
            font-weight: bold;
            font-size: 1.1rem;
        }
        
        .badge {
            padding: 5px 12px;
            border-radius: 15px;
            font-size: 0.85rem;
            font-weight: bold;
            display: inline-block;
        }
        
        .badge.active {
            background: #d4edda;
            color: #155724;
        }
        
        .badge.inactive {
            background: #f8d7da;
            color: #721c24;
        }
        
        .empty-state {
            text-align: center;
            padding: 50px;
            color: #999;
        }
        
        .empty-state i {
            font-size: 4rem;
            margin-bottom: 20px;
            opacity: 0.5;
        }
        
        .btn-refresh {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            transition: all 0.3s ease;
            text-decoration: none;
        }
        
        .btn-refresh:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .error-message {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid #dc3545;
            margin: 15px 0;
        }
        
        .error-message pre {
            margin-top: 10px;
            background: #fff;
            padding: 10px;
            border-radius: 5px;
            overflow-x: auto;
            font-size: 0.9rem;
        }
        
        .nav-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }
        
        .btn {
            padding: 12px 30px;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            transition: all 0.3s ease;
            text-decoration: none;
            border: none;
        }
        
        .btn-primary {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
        }
        
        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1><i class="fas fa-database"></i> Database Connection Test</h1>
            <p>Testing database connectivity and data retrieval for SamSon Travel Booking Service</p>
        </div>

        <%
            // Format helpers
            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            boolean dbConnected = false;
            Connection conn = null;
            String errorMessage = null;
            int mealCount = 0;
            int wellnessCount = 0;
            int userCount = 0;
            int hotelCount = 0;
        %>

        <!-- Test 1: Database Connection -->
        <div class="test-section">
            <h2><i class="fas fa-plug"></i> Test 1: Database Connection</h2>
            <%
                try {
                    conn = DBContext.getConnection();
                    if (conn != null && !conn.isClosed()) {
                        dbConnected = true;
            %>
                        <div class="status success">
                            <i class="fas fa-check-circle"></i>
                            <span>✅ KẾT NỐI DATABASE THÀNH CÔNG!</span>
                        </div>
                        <p><strong>Database:</strong> <%= conn.getMetaData().getDatabaseProductName() %></p>
                        <p><strong>Version:</strong> <%= conn.getMetaData().getDatabaseProductVersion() %></p>
                        <p><strong>URL:</strong> <%= conn.getMetaData().getURL() %></p>
            <%
                    } else {
            %>
                        <div class="status error">
                            <i class="fas fa-times-circle"></i>
                            <span>❌ KẾT NỐI THẤT BẠI!</span>
                        </div>
            <%
                    }
                } catch (Exception e) {
                    errorMessage = e.getMessage();
            %>
                    <div class="status error">
                        <i class="fas fa-times-circle"></i>
                        <span>❌ LỖI KẾT NỐI DATABASE</span>
                    </div>
                    <div class="error-message">
                        <strong>Error Message:</strong>
                        <pre><%= e.getMessage() %></pre>
                        <p><strong>Kiểm tra:</strong></p>
                        <ul>
                            <li>SQL Server có đang chạy không?</li>
                            <li>Database "booking_travel" có tồn tại không?</li>
                            <li>Username/Password trong DBContext.java có đúng không?</li>
                            <li>JDBC Driver đã được add vào project chưa?</li>
                        </ul>
                    </div>
            <%
                }
            %>
        </div>

        <% if (dbConnected) { %>
        
        <!-- Test 2: Count Records -->
        <div class="test-section">
            <h2><i class="fas fa-chart-bar"></i> Test 2: Database Statistics</h2>
            <%
                try {
                    Statement stmt = conn.createStatement();
                    
                    // Count MealService
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM MealService WHERE status = 'active'");
                    if (rs.next()) {
                        mealCount = rs.getInt("cnt");
                    }
                    rs.close();
                    
                    // Count WellnessService
                    rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM WellnessService WHERE status = 'active'");
                    if (rs.next()) {
                        wellnessCount = rs.getInt("cnt");
                    }
                    rs.close();
                    
                    // Count Users
                    rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM [User]");
                    if (rs.next()) {
                        userCount = rs.getInt("cnt");
                    }
                    rs.close();
                    
                    // Count Hotels (if table exists)
                    try {
                        rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM Hotel");
                        if (rs.next()) {
                            hotelCount = rs.getInt("cnt");
                        }
                        rs.close();
                    } catch (Exception e) {
                        // Table might not exist
                        hotelCount = 0;
                    }
                    
                    stmt.close();
            %>
                    <div class="info-grid">
                        <div class="info-card">
                            <i class="fas fa-utensils" style="font-size: 2rem;"></i>
                            <div class="number"><%= mealCount %></div>
                            <div class="label">Meal Services</div>
                        </div>
                        <div class="info-card">
                            <i class="fas fa-spa" style="font-size: 2rem;"></i>
                            <div class="number"><%= wellnessCount %></div>
                            <div class="label">Wellness Services</div>
                        </div>
                        <div class="info-card">
                            <i class="fas fa-users" style="font-size: 2rem;"></i>
                            <div class="number"><%= userCount %></div>
                            <div class="label">Users</div>
                        </div>
                        <div class="info-card">
                            <i class="fas fa-hotel" style="font-size: 2rem;"></i>
                            <div class="number"><%= hotelCount %></div>
                            <div class="label">Hotels</div>
                        </div>
                    </div>
            <%
                } catch (Exception e) {
            %>
                    <div class="error-message">
                        <strong>Lỗi khi đếm records:</strong>
                        <pre><%= e.getMessage() %></pre>
                    </div>
            <%
                }
            %>
        </div>

        <!-- Test 3: MealService Data -->
        <div class="test-section">
            <h2><i class="fas fa-utensils"></i> Test 3: MealService Data (using DAO)</h2>
            <%
                try {
                    MealServiceDAO mealDAO = new MealServiceDAO();
                    List<MealService> mealServices = mealDAO.getAllActiveServices();
                    
                    if (mealServices != null && !mealServices.isEmpty()) {
            %>
                        <div class="status success">
                            <i class="fas fa-check-circle"></i>
                            <span>Tìm thấy <%= mealServices.size() %> Meal Services</span>
                        </div>
                        
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên món ăn</th>
                                    <th>Mô tả</th>
                                    <th>Giá</th>
                                    <th>Ngày</th>
                                    <th>Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (MealService meal : mealServices) { %>
                                <tr>
                                    <td><%= meal.getMealId() %></td>
                                    <td><strong><%= meal.getMealType() %></strong></td>
                                    <td><%= meal.getDescription() != null ? meal.getDescription() : "-" %></td>
                                    <td class="price"><%= currencyFormat.format(meal.getPrice()) %>₫</td>
                                    <td><%= meal.getMealDate() != null ? dateFormat.format(meal.getMealDate()) : "-" %></td>
                                    <td><span class="badge <%= meal.getStatus() %>"><%= meal.getStatus() %></span></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
            <%
                    } else {
            %>
                        <div class="empty-state">
                            <i class="fas fa-inbox"></i>
                            <h3>Chưa có dữ liệu MealService</h3>
                            <p>Hãy chạy file SQL: <strong>database/insert_sample_services.sql</strong></p>
                        </div>
            <%
                    }
                } catch (Exception e) {
            %>
                    <div class="error-message">
                        <strong>Lỗi khi lấy MealService data:</strong>
                        <pre><%= e.getMessage() %></pre>
                    </div>
            <%
                    e.printStackTrace();
                }
            %>
        </div>

        <!-- Test 4: WellnessService Data -->
        <div class="test-section">
            <h2><i class="fas fa-spa"></i> Test 4: WellnessService Data (using DAO)</h2>
            <%
                try {
                    WellnessServiceDAO wellnessDAO = new WellnessServiceDAO();
                    List<WellnessService> wellnessServices = wellnessDAO.getAllActiveServices();
                    
                    if (wellnessServices != null && !wellnessServices.isEmpty()) {
            %>
                        <div class="status success">
                            <i class="fas fa-check-circle"></i>
                            <span>Tìm thấy <%= wellnessServices.size() %> Wellness Services</span>
                        </div>
                        
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tên dịch vụ</th>
                                    <th>Mô tả</th>
                                    <th>Giá</th>
                                    <th>Thời gian</th>
                                    <th>Giờ hoạt động</th>
                                    <th>Sức chứa</th>
                                    <th>Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (WellnessService wellness : wellnessServices) { %>
                                <tr>
                                    <td><%= wellness.getWellnessId() %></td>
                                    <td><strong><%= wellness.getServiceName() %></strong></td>
                                    <td><%= wellness.getDescription() != null ? wellness.getDescription() : "-" %></td>
                                    <td class="price"><%= currencyFormat.format(wellness.getBasePrice()) %>₫</td>
                                    <td><%= wellness.getDurationMinutes() != null ? wellness.getDurationMinutes() + " phút" : "-" %></td>
                                    <td><%= wellness.getOperatingHours() != null ? wellness.getOperatingHours() : "-" %></td>
                                    <td><%= wellness.getCapacity() != null ? wellness.getCapacity() + " người" : "-" %></td>
                                    <td><span class="badge <%= wellness.getStatus() %>"><%= wellness.getStatus() %></span></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
            <%
                    } else {
            %>
                        <div class="empty-state">
                            <i class="fas fa-inbox"></i>
                            <h3>Chưa có dữ liệu WellnessService</h3>
                            <p>Hãy chạy file SQL: <strong>database/insert_sample_services.sql</strong></p>
                        </div>
            <%
                    }
                } catch (Exception e) {
            %>
                    <div class="error-message">
                        <strong>Lỗi khi lấy WellnessService data:</strong>
                        <pre><%= e.getMessage() %></pre>
                    </div>
            <%
                    e.printStackTrace();
                }
            %>
        </div>

        <% } // end if dbConnected %>

        <!-- Navigation Buttons -->
        <div class="nav-buttons">
            <a href="test-db.jsp" class="btn btn-primary">
                <i class="fas fa-sync-alt"></i> Refresh Test
            </a>
            <a href="service-list" class="btn btn-secondary">
                <i class="fas fa-arrow-right"></i> Go to Service List
            </a>
            <a href="dashboard.jsp" class="btn btn-secondary">
                <i class="fas fa-home"></i> Dashboard
            </a>
        </div>

        <%
            // Close connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        %>
    </div>
</body>
</html>



