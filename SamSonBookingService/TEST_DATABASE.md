# 🧪 Test Database Connection - Hướng dẫn sử dụng

## 📝 Mô tả
File `test-db.jsp` là công cụ test để kiểm tra kết nối database và xem dữ liệu trong hệ thống SamSon Travel Booking Service.

---

## 🚀 Cách sử dụng

### Bước 1: Đảm bảo database đã chạy
Kiểm tra SQL Server đang chạy và database `booking_travel` đã tồn tại.

### Bước 2: Insert dữ liệu mẫu (nếu chưa có)
Chạy file SQL script:
```sql
database/insert_sample_services.sql
```

### Bước 3: Truy cập trang test
**URL:** 
```
http://localhost:8080/SamSonBookingService/test-db.jsp
```

**Lưu ý:** Trang này **KHÔNG** yêu cầu đăng nhập, có thể test trực tiếp.

---

## ✅ Các test được thực hiện

### Test 1: Database Connection
- ✅ Kiểm tra kết nối đến SQL Server
- ✅ Hiển thị thông tin database (name, version, URL)
- ❌ Hiển thị lỗi nếu không kết nối được

### Test 2: Database Statistics
- Đếm số lượng records trong các bảng:
  - **MealService** (status = 'active')
  - **WellnessService** (status = 'active')
  - **User**
  - **Hotel**

### Test 3: MealService Data
- Lấy tất cả Meal Services qua `MealServiceDAO`
- Hiển thị trong bảng với đầy đủ thông tin:
  - ID, Tên món ăn, Mô tả, Giá, Ngày, Trạng thái

### Test 4: WellnessService Data
- Lấy tất cả Wellness Services qua `WellnessServiceDAO`
- Hiển thị trong bảng với đầy đủ thông tin:
  - ID, Tên dịch vụ, Mô tả, Giá, Thời gian, Giờ hoạt động, Sức chứa, Trạng thái

---

## 🎨 Giao diện

Trang test có giao diện hiện đại với:
- 🎨 Gradient background (purple theme)
- 📊 Info cards hiển thị statistics
- 📋 Data tables với styling đẹp mắt
- ✅ Status indicators (success/error)
- 🔄 Refresh button
- 🔗 Quick navigation buttons

---

## 🐛 Troubleshooting

### Lỗi: "KẾT NỐI THẤT BẠI"
**Nguyên nhân:**
- SQL Server không chạy
- Database không tồn tại
- Username/Password sai
- JDBC Driver chưa được add vào project

**Giải pháp:**
1. Start SQL Server service
2. Kiểm tra database `booking_travel` tồn tại
3. Kiểm tra `src/java/dao/DBContext.java`:
   ```java
   private static final String DATABASE_NAME = "booking_travel";
   private static final String USERNAME = "sa";
   private static final String PASSWORD = "123";
   ```
4. Kiểm tra JDBC Driver trong project libraries

### Lỗi: "Chưa có dữ liệu"
**Nguyên nhân:**
- Chưa insert dữ liệu mẫu vào database

**Giải pháp:**
```sql
-- Chạy file này trong SQL Server:
database/insert_sample_services.sql
```

### Lỗi: "Table does not exist"
**Nguyên nhân:**
- Bảng MealService hoặc WellnessService chưa được tạo

**Giải pháp:**
Tạo bảng trong database:
```sql
-- Xem SETUP_SERVICE_LIST.md phần "Cấu trúc Database"
-- Hoặc check schema trong database design docs
```

---

## 📊 Kết quả mong đợi

### Khi thành công:
```
✅ KẾT NỐI DATABASE THÀNH CÔNG!
Database: Microsoft SQL Server
Version: 15.00.xxxx
URL: jdbc:sqlserver://localhost:1433;databaseName=booking_travel

📊 Statistics:
- 5 Meal Services
- 8 Wellness Services
- X Users
- X Hotels

✅ Tìm thấy 5 Meal Services
[Bảng hiển thị dữ liệu MealService]

✅ Tìm thấy 8 Wellness Services
[Bảng hiển thị dữ liệu WellnessService]
```

### Khi thất bại:
```
❌ LỖI KẾT NỐI DATABASE
Error Message: [Chi tiết lỗi]

Kiểm tra:
☐ SQL Server có đang chạy không?
☐ Database "booking_travel" có tồn tại không?
☐ Username/Password trong DBContext.java có đúng không?
☐ JDBC Driver đã được add vào project chưa?
```

---

## 🔗 Navigation

Từ trang test, bạn có thể:
- 🔄 **Refresh Test** - Chạy lại test
- ➡️ **Go to Service List** - Chuyển đến trang service-list
- 🏠 **Dashboard** - Quay về dashboard

---

## 💡 Tips

### 1. Debug Connection String
Nếu không kết nối được, thử thay đổi connection string:
```java
// Thêm parameters khác nếu cần
jdbc:sqlserver://localhost:1433;
  databaseName=booking_travel;
  encrypt=false;
  trustServerCertificate=true;
```

### 2. Check SQL Server Port
Mặc định là 1433, nếu khác:
```java
private static final String PORT_NUMBER = "1434"; // Thay đổi ở đây
```

### 3. SQL Server Authentication
Đảm bảo SQL Server cho phép SQL Server Authentication (không chỉ Windows Authentication):
- SQL Server Configuration Manager
- SQL Server Properties
- Security → Enable "SQL Server and Windows Authentication mode"

### 4. Firewall
Đảm bảo port 1433 không bị firewall block.

---

## 📝 Notes

- File `test-db.jsp` **không cần đăng nhập**, có thể test ngay lập tức
- Dùng để kiểm tra trước khi chạy các trang chính như `service-list`
- Nếu test này pass thì các trang khác sẽ hoạt động bình thường
- Có thể xóa file này sau khi production (security)

---

## 🔐 Security Warning

⚠️ **QUAN TRỌNG:** File `test-db.jsp` hiển thị thông tin database connection.
- Chỉ dùng trong môi trường **development/testing**
- **XÓA hoặc DISABLE** trước khi deploy lên production
- Hoặc thêm authentication check ở đầu file

Để disable trong production, thêm vào đầu file:
```jsp
<%
    // Chỉ cho phép trong development mode
    if (!"development".equals(System.getProperty("app.mode"))) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
%>
```

---

## ✨ Next Steps

Sau khi test thành công:
1. ✅ Truy cập trang service-list: `http://localhost:8080/SamSonBookingService/service-list`
2. ✅ Đăng nhập với user hợp lệ
3. ✅ Test các chức năng add to cart, filter, search
4. ✅ Kiểm tra mini cart và sessionStorage

Happy Testing! 🚀



