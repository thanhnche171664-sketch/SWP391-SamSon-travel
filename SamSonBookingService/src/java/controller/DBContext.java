package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {

    // Singleton instance duy nhất
    private static volatile DBContext instance;

    // Connection luôn mở trong suốt vòng đời ứng dụng
    private static Connection connection;

    // Thông tin cấu hình database
    private static final String USER = "sa";
    private static final String PASSWORD = "123";
    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;"
            + "databaseName=booking_travel1;"
            + "encrypt=true;trustServerCertificate=true;loginTimeout=10;";

    // Private constructor để ngăn tạo đối tượng bên ngoài
    private DBContext() {
        try {
            // Load driver SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Khởi tạo kết nối ban đầu
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DBContext] ✅ Kết nối SQL Server thành công!");

            // Giữ kết nối luôn mở bằng cách test định kỳ ở thread nền
            startConnectionKeeper();

        } catch (ClassNotFoundException e) {
            System.err.println("[DBContext] ❌ Không tìm thấy driver SQL Server!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DBContext] ⚠️ Lỗi SQL khi tạo kết nối: " + e.getMessage());
            reconnect();
        }
    }

    // ✅ Lấy thể hiện duy nhất (thread-safe double-check locking)
    public static DBContext getInstance() {
        if (instance == null) {
            synchronized (DBContext.class) {
                if (instance == null) {
                    instance = new DBContext();
                }
            }
        }
        return instance;
    }

    // ✅ Luôn trả về connection đang hoạt động
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                System.out.println("[DBContext] ⚙️ Connection bị ngắt, đang kết nối lại...");
                reconnect();
            }
        } catch (SQLException e) {
            System.err.println("[DBContext] ⚠️ Lỗi khi kiểm tra kết nối: " + e.getMessage());
            reconnect();
        }
        return connection;
    }

    // 🔄 Tự động kết nối lại khi bị mất
    private synchronized void reconnect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DBContext] 🔁 Đã tái kết nối SQL Server thành công!");
        } catch (SQLException e) {
            System.err.println("[DBContext] ❌ Không thể tái kết nối SQL Server: " + e.getMessage());
        }
    }

    // 🕒 Thread nền: kiểm tra và giữ kết nối định kỳ
    private void startConnectionKeeper() {
        Thread keepAlive = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000); // kiểm tra mỗi 30 giây
                    if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                        System.out.println("[DBContext] 🔄 Kết nối SQL bị timeout — đang tái kết nối...");
                        reconnect();
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.err.println("[DBContext] ⚠️ Luồng kiểm tra kết nối bị dừng!");
                    break;
                } catch (SQLException e) {
                    System.err.println("[DBContext] ⚠️ Lỗi khi kiểm tra connection: " + e.getMessage());
                    reconnect();
                }
            }
        });
        keepAlive.setDaemon(true); // chạy nền
        keepAlive.start();
    }

    // ❌ Không bao giờ đóng kết nối (nếu muốn dọn dẹp, có thể mở lại hàm dưới)
//    public void closeConnection() {
//        try {
//            if (connection != null && !connection.isClosed()) {
//                connection.close();
//                System.out.println("[DBContext] 🔒 Đã đóng kết nối.");
//            }
//        } catch (SQLException e) {
//            System.err.println("[DBContext] ⚠️ Lỗi khi đóng kết nối: " + e.getMessage());
//        }
//    }

    // ✅ Test nhanh
   public static void main(String[] args) {
       DBContext db = DBContext.getInstance();
        Connection conn = db.getConnection();
       System.out.println("Kết nối hiện tại: " + (conn != null ? "OK" : "FAILED"));
    }
}
