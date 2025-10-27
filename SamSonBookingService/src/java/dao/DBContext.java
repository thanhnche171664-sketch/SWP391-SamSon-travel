package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {

    // Cấu hình database
    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "booking_travel";
    private static final String PORT_NUMBER = "1433";
    private static final String USERNAME = "sa";   // để đúng user SQL Server
    private static final String PASSWORD = "123";  // đúng mật khẩu tài khoản sa

    // Connection string CHUẨN
    private static final String CONNECTION_URL = 
        "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT_NUMBER
        + ";databaseName=" + DATABASE_NAME
        + ";encrypt=false";  // tắt SSL nếu server không bật SSL

    // Hàm lấy connection
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Chưa có JDBC Driver SQL Server trong project!", e);
        }
    }

    // Test connection
    public static void main(String[] args) {
        try (Connection con = DBContext.getConnection()) {
            if (con != null && !con.isClosed()) {
                System.out.println("✅ KẾT NỐI DATABASE THÀNH CÔNG!");
            } else {
                System.out.println("❌ KẾT NỐI THẤT BẠI!");
            }
        } catch (Exception e) {
            System.out.println("❌ LỖI KHI KẾT NỐI DATABASE:");
            e.printStackTrace();
        }
    }
}
