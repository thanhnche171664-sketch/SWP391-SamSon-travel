/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.OfflineCustomer;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for OfflineCustomer entity
 * Handles all database operations related to offline customers
 * 
 * @author SamSon Travel Team
 */
public class OfflineCustomerDAO {
    
    private static final Logger LOGGER = Logger.getLogger(OfflineCustomerDAO.class.getName());
    
    // SQL query để lưu khách hàng offline
    private static final String INSERT_OFFLINE_CUSTOMER = 
        "INSERT INTO Offline_Customers (full_name, phone, email, id_card_number, nationality, gender, date_of_birth, address, created_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
    
    // SQL query để tìm khách hàng theo phone và email
    private static final String FIND_CUSTOMER_BY_PHONE_EMAIL = 
        "SELECT TOP 1 offline_customer_id FROM Offline_Customers WHERE phone = ? AND email = ?";
    
    /**
     * Tìm hoặc tạo khách hàng mặc định "Khách vãng lai"
     * Nếu đã có thì trả về ID, nếu chưa có thì tạo mới
     * 
     * @return ID của khách hàng mặc định
     */
    public int findOrCreateDefaultCustomer() {
        try (Connection connection = DBContext.getConnection()) {
            // Tìm khách hàng "Khách vãng lai" đã có
            try (PreparedStatement findStmt = connection.prepareStatement(FIND_CUSTOMER_BY_PHONE_EMAIL)) {
                findStmt.setString(1, "0000000000");
                findStmt.setString(2, "walkin@example.com");
                
                try (ResultSet rs = findStmt.executeQuery()) {
                    if (rs.next()) {
                        int customerId = rs.getInt("offline_customer_id");
                        System.out.println("Đã tìm thấy khách hàng mặc định với ID: " + customerId);
                        return customerId;
                    }
                }
            }
            
            // Nếu chưa có, tạo mới
            OfflineCustomer defaultCustomer = new OfflineCustomer();
            defaultCustomer.setFullName("Khách vãng lai");
            defaultCustomer.setPhone("0000000000");
            defaultCustomer.setEmail("walkin@example.com");
            defaultCustomer.setIdCardNumber(null);
            defaultCustomer.setNationality("Việt Nam");
            defaultCustomer.setGender("other"); // Sửa từ "N/A" thành "other" để phù hợp với CHECK constraint
            defaultCustomer.setDateOfBirth(null);
            defaultCustomer.setAddress("Khách vãng lai");
            
            int customerId = saveOfflineCustomer(defaultCustomer);
            if (customerId > 0) {
                System.out.println("Đã tạo khách hàng mặc định mới với ID: " + customerId);
            }
            return customerId;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding or creating default customer", e);
            System.err.println("Lỗi tìm/tạo khách hàng mặc định: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Lưu thông tin khách hàng offline vào database
     * 
     * @param customer Đối tượng OfflineCustomer cần lưu
     * @return ID của khách hàng vừa được tạo, -1 nếu lỗi
     */
    public int saveOfflineCustomer(OfflineCustomer customer) {
        if (customer == null) {
            LOGGER.warning("Cannot save null customer");
            return -1;
        }
        
        try (Connection connection = DBContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_OFFLINE_CUSTOMER, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set các tham số (chuyển chuỗi rỗng thành NULL để tránh lỗi constraint)
            statement.setString(1, trimToNull(customer.getFullName()));
            
            setNullableString(statement, 2, customer.getPhone());
            setNullableString(statement, 3, customer.getEmail());
            setNullableString(statement, 4, customer.getIdCardNumber());
            setNullableString(statement, 5, customer.getNationality());
            setNullableGender(statement, 6, customer.getGender());
            
            // Xử lý ngày sinh
            if (customer.getDateOfBirth() != null) {
                statement.setDate(7, new java.sql.Date(customer.getDateOfBirth().getTime()));
            } else {
                statement.setNull(7, Types.DATE);
            }
            
            setNullableString(statement, 8, customer.getAddress());
            
            // Thực thi câu lệnh
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // Lấy ID vừa được tạo
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int customerId = generatedKeys.getInt(1);
                        LOGGER.info("Saved offline customer with ID: " + customerId);
                        return customerId;
                    }
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving offline customer", e);
            System.err.println("Lỗi lưu khách hàng offline: " + e.getMessage());
        }
        
        return -1;
    }

    /**
     * Helper: chuyển chuỗi rỗng thành NULL
     */
    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Helper: set NVARCHAR nullable
     */
    private void setNullableString(PreparedStatement statement, int index, String value) throws SQLException {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            statement.setNull(index, Types.NVARCHAR);
        } else {
            statement.setString(index, trimmed);
        }
    }

    /**
     * Helper: set giới tính (chỉ chấp nhận male/female/other hoặc NULL)
     */
    private void setNullableGender(PreparedStatement statement, int index, String gender) throws SQLException {
        String trimmed = trimToNull(gender);
        if (trimmed == null) {
            statement.setNull(index, Types.NVARCHAR);
            return;
        }
        switch (trimmed.toLowerCase()) {
            case "male":
            case "female":
            case "other":
                statement.setString(index, trimmed.toLowerCase());
                break;
            default:
                statement.setNull(index, Types.NVARCHAR);
                break;
        }
    }
}
