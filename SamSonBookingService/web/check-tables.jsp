<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="dao.DBContext" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kiểm tra Database Tables - SamSon Travel</title>
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
            max-width: 1400px;
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
        
        .status.warning {
            background: #fff3cd;
            color: #856404;
            border: 2px solid #ffeeba;
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
        
        .highlight {
            background: #fffacd !important;
            font-weight: bold;
        }
        
        .code-block {
            background: #f8f9fa;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin: 15px 0;
            border-radius: 5px;
            font-family: 'Courier New', monospace;
            overflow-x: auto;
        }
        
        .suggestion {
            background: #e7f3ff;
            border-left: 4px solid #2196F3;
            padding: 15px;
            margin: 15px 0;
            border-radius: 5px;
        }
        
        .suggestion h3 {
            color: #2196F3;
            margin-bottom: 10px;
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
            margin: 5px;
        }
        
        .btn-primary {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        .error-message {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid #dc3545;
            margin: 15px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1><i class="fas fa-search"></i> Kiểm tra Database Tables</h1>
            <p>Tự động phát hiện tên bảng chính xác trong database</p>
        </div>

        <%
            Connection conn = null;
            boolean dbConnected = false;
            List<String> allTables = new ArrayList<>();
            List<String> mealTables = new ArrayList<>();
            List<String> wellnessTables = new ArrayList<>();
            String errorMsg = null;
            
            try {
                conn = DBContext.getConnection();
                dbConnected = true;
        %>

        <!-- Test 1: Database Connection -->
        <div class="test-section">
            <h2><i class="fas fa-database"></i> Kết nối Database</h2>
            <div class="status success">
                <i class="fas fa-check-circle"></i>
                <span>✅ KẾT NỐI THÀNH CÔNG!</span>
            </div>
            <p><strong>Database:</strong> <%= conn.getCatalog() %></p>
            <p><strong>URL:</strong> <%= conn.getMetaData().getURL() %></p>
        </div>

        <!-- Test 2: List All Tables -->
        <div class="test-section">
            <h2><i class="fas fa-table"></i> Tất cả các bảng trong Database</h2>
            <%
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet tables = metaData.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});
                
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    allTables.add(tableName);
                    
                    // Tìm bảng liên quan đến Meal
                    if (tableName.toLowerCase().contains("meal")) {
                        mealTables.add(tableName);
                    }
                    
                    // Tìm bảng liên quan đến Wellness
                    if (tableName.toLowerCase().contains("wellness")) {
                        wellnessTables.add(tableName);
                    }
                }
                tables.close();
            %>
            
            <p><strong>Tổng số bảng:</strong> <%= allTables.size() %></p>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Tên bảng</th>
                        <th>Liên quan</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    int index = 1;
                    for (String tableName : allTables) { 
                        boolean isMeal = tableName.toLowerCase().contains("meal");
                        boolean isWellness = tableName.toLowerCase().contains("wellness");
                        String cssClass = (isMeal || isWellness) ? "highlight" : "";
                    %>
                    <tr class="<%= cssClass %>">
                        <td><%= index++ %></td>
                        <td><strong><%= tableName %></strong></td>
                        <td>
                            <% if (isMeal) { %>
                                <span style="color: #f5576c;">🍽️ MEAL SERVICE</span>
                            <% } else if (isWellness) { %>
                                <span style="color: #4facfe;">💆 WELLNESS SERVICE</span>
                            <% } else { %>
                                -
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <!-- Test 3: Meal Tables Found -->
        <div class="test-section">
            <h2><i class="fas fa-utensils"></i> Bảng liên quan đến MEAL</h2>
            <% if (mealTables.isEmpty()) { %>
                <div class="status error">
                    <i class="fas fa-times-circle"></i>
                    <span>❌ KHÔNG TÌM THẤY bảng Meal Service!</span>
                </div>
                
                <div class="suggestion">
                    <h3><i class="fas fa-lightbulb"></i> Giải pháp:</h3>
                    <p>Cần tạo bảng MealService trong database. Chạy script:</p>
                    <div class="code-block">
                        database/create_tables.sql
                    </div>
                </div>
            <% } else { %>
                <div class="status success">
                    <i class="fas fa-check-circle"></i>
                    <span>✅ Tìm thấy <%= mealTables.size() %> bảng!</span>
                </div>
                
                <% for (String tableName : mealTables) { %>
                    <h3 style="color: #667eea; margin-top: 20px;">📋 Bảng: <%= tableName %></h3>
                    
                    <%
                        // Get column info
                        ResultSet columns = metaData.getColumns(conn.getCatalog(), null, tableName, "%");
                    %>
                    
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Tên cột</th>
                                <th>Kiểu dữ liệu</th>
                                <th>Nullable</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% while (columns.next()) { %>
                            <tr>
                                <td><strong><%= columns.getString("COLUMN_NAME") %></strong></td>
                                <td><%= columns.getString("TYPE_NAME") %>(<%= columns.getInt("COLUMN_SIZE") %>)</td>
                                <td><%= columns.getString("IS_NULLABLE") %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% columns.close(); %>
                    
                    <%
                        // Count records
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM [" + tableName + "]");
                        int count = 0;
                        if (rs.next()) {
                            count = rs.getInt("cnt");
                        }
                        rs.close();
                        stmt.close();
                    %>
                    
                    <p style="margin-top: 15px;"><strong>Số lượng records:</strong> <span style="color: #f5576c; font-size: 1.5rem;"><%= count %></span></p>
                    
                    <% if (!tableName.equals("MealService")) { %>
                        <div class="suggestion">
                            <h3><i class="fas fa-exclamation-triangle"></i> Cảnh báo!</h3>
                            <p>Tên bảng trong database là <strong><%= tableName %></strong>, nhưng code đang dùng <strong>MealService</strong></p>
                            <p><strong>Cần sửa trong file:</strong></p>
                            <div class="code-block">
// src/java/dao/MealServiceDAO.java
// Line 22-26: Thay đổi tên bảng
String sql = "SELECT ... FROM [<%= tableName %>] ...";</div>
                        </div>
                    <% } %>
                <% } %>
            <% } %>
        </div>

        <!-- Test 4: Wellness Tables Found -->
        <div class="test-section">
            <h2><i class="fas fa-spa"></i> Bảng liên quan đến WELLNESS</h2>
            <% if (wellnessTables.isEmpty()) { %>
                <div class="status error">
                    <i class="fas fa-times-circle"></i>
                    <span>❌ KHÔNG TÌM THẤY bảng Wellness Service!</span>
                </div>
                
                <div class="suggestion">
                    <h3><i class="fas fa-lightbulb"></i> Giải pháp:</h3>
                    <p>Cần tạo bảng WellnessService trong database. Chạy script:</p>
                    <div class="code-block">
                        database/create_tables.sql
                    </div>
                </div>
            <% } else { %>
                <div class="status success">
                    <i class="fas fa-check-circle"></i>
                    <span>✅ Tìm thấy <%= wellnessTables.size() %> bảng!</span>
                </div>
                
                <% for (String tableName : wellnessTables) { %>
                    <h3 style="color: #667eea; margin-top: 20px;">📋 Bảng: <%= tableName %></h3>
                    
                    <%
                        // Get column info
                        ResultSet columns = metaData.getColumns(conn.getCatalog(), null, tableName, "%");
                    %>
                    
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Tên cột</th>
                                <th>Kiểu dữ liệu</th>
                                <th>Nullable</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% while (columns.next()) { %>
                            <tr>
                                <td><strong><%= columns.getString("COLUMN_NAME") %></strong></td>
                                <td><%= columns.getString("TYPE_NAME") %>(<%= columns.getInt("COLUMN_SIZE") %>)</td>
                                <td><%= columns.getString("IS_NULLABLE") %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% columns.close(); %>
                    
                    <%
                        // Count records
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM [" + tableName + "]");
                        int count = 0;
                        if (rs.next()) {
                            count = rs.getInt("cnt");
                        }
                        rs.close();
                        stmt.close();
                    %>
                    
                    <p style="margin-top: 15px;"><strong>Số lượng records:</strong> <span style="color: #4facfe; font-size: 1.5rem;"><%= count %></span></p>
                    
                    <% if (!tableName.equals("WellnessService")) { %>
                        <div class="suggestion">
                            <h3><i class="fas fa-exclamation-triangle"></i> Cảnh báo!</h3>
                            <p>Tên bảng trong database là <strong><%= tableName %></strong>, nhưng code đang dùng <strong>WellnessService</strong></p>
                            <p><strong>Cần sửa trong file:</strong></p>
                            <div class="code-block">
// src/java/dao/WellnessServiceDAO.java
// Line 22-27: Thay đổi tên bảng
String sql = "SELECT ... FROM [<%= tableName %>] ...";</div>
                        </div>
                    <% } %>
                <% } %>
            <% } %>
        </div>

        <%
            } catch (Exception e) {
                errorMsg = e.getMessage();
        %>
        <div class="test-section">
            <h2><i class="fas fa-exclamation-triangle"></i> Lỗi kết nối</h2>
            <div class="status error">
                <i class="fas fa-times-circle"></i>
                <span>❌ KHÔNG THỂ KẾT NỐI DATABASE</span>
            </div>
            <div class="error-message">
                <strong>Error:</strong> <%= e.getMessage() %>
                <pre><%= e.toString() %></pre>
            </div>
        </div>
        <%
            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (Exception e) {}
                }
            }
        %>

        <!-- Navigation -->
        <div style="text-align: center; margin-top: 30px;">
            <a href="check-tables.jsp" class="btn btn-primary">
                <i class="fas fa-sync-alt"></i> Refresh
            </a>
            <a href="test-db.jsp" class="btn btn-primary">
                <i class="fas fa-database"></i> Test Database
            </a>
        </div>
    </div>
</body>
</html>




