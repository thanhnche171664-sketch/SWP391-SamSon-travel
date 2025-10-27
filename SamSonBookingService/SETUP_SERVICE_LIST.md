# Hướng dẫn Setup và Test Service List

## 📋 Tổng quan
Hướng dẫn này giúp bạn setup và hiển thị dữ liệu trên trang **Service-list.jsp** cho dự án SamSon Travel Booking Service.

---

## 🔧 Các thành phần đã tạo

### 1. **DAO Layer** ✅
- `MealServiceDAO.java` - Quản lý dịch vụ ăn uống
- `WellnessServiceDAO.java` - Quản lý dịch vụ Spa & Wellness

**Các method chính:**
- `getAllActiveServices()` - Lấy tất cả dịch vụ đang active
- `getMealServiceById()` / `getWellnessServiceById()` - Lấy theo ID
- `getMealServicesByHotelId()` / `getWellnessServicesByHotelId()` - Lấy theo Hotel
- `createMealService()` / `createWellnessService()` - Tạo mới
- `updateMealService()` / `updateWellnessService()` - Cập nhật
- `deleteMealService()` / `deleteWellnessService()` - Xóa (soft delete)

### 2. **Controller Layer** ✅
- `ServiceListServlet.java` - Servlet xử lý request và forward data đến JSP

**Mapping URL:** `/service-list`

### 3. **View Layer** ✅
- `Service-list.jsp` - Trang hiển thị danh sách dịch vụ
- `assets/css/service-list.css` - Styling cho trang

### 4. **Configuration** ✅
- `web.xml` - Đã thêm servlet mapping và auth filter

---

## 🗄️ Cấu trúc Database

### Bảng MealService
```sql
CREATE TABLE MealService (
    meal_id INT PRIMARY KEY IDENTITY(1,1),
    hotel_id INT NOT NULL,
    category_id INT NOT NULL,
    meal_type NVARCHAR(100) NOT NULL,
    meal_date DATE,
    description NVARCHAR(500),
    price DECIMAL(18,2) NOT NULL,
    status NVARCHAR(20) DEFAULT 'active',
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
```

### Bảng WellnessService
```sql
CREATE TABLE WellnessService (
    wellness_id INT PRIMARY KEY IDENTITY(1,1),
    hotel_id INT NOT NULL,
    category_id INT NOT NULL,
    service_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(500),
    base_price DECIMAL(18,2) NOT NULL,
    duration_minutes INT,
    operating_hours NVARCHAR(50),
    capacity INT,
    status NVARCHAR(20) DEFAULT 'active',
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
```

---

## 🚀 Hướng dẫn Setup

### Bước 1: Tạo bảng trong Database (nếu chưa có)
Chạy các câu lệnh SQL ở trên trong SQL Server Management Studio hoặc tool tương tự.

### Bước 2: Insert dữ liệu mẫu
Chạy file SQL script:
```bash
database/insert_sample_services.sql
```

Hoặc chạy thủ công trong SQL Server:
```sql
-- Trong SSMS, mở file và Execute (F5)
```

**Lưu ý:** Đảm bảo bạn đã có:
- `hotel_id = 1` trong bảng `Hotel`
- `category_id = 1` cho MEAL trong bảng `ServiceCategory`
- `category_id = 2` cho WELLNESS trong bảng `ServiceCategory`

### Bước 3: Build và Deploy Project
```bash
# Trong NetBeans
1. Clean and Build (Shift + F11)
2. Run (F6)
```

Hoặc dùng Ant:
```bash
ant clean
ant compile
ant dist
```

### Bước 4: Truy cập trang Service List

#### Option 1: Qua Dashboard (Recommended)
1. Đăng nhập vào hệ thống: `http://localhost:8080/SamSonBookingService/login`
2. Từ Dashboard, click vào menu "Dịch vụ" hoặc link `/service-list`

#### Option 2: Direct URL
```
http://localhost:8080/SamSonBookingService/service-list
```

**Lưu ý:** Trang này được bảo vệ bởi `AuthFilter`, bạn **phải đăng nhập** trước khi truy cập.

---

## 🧪 Testing

### Test 1: Kiểm tra kết nối Database
```java
// Chạy DBContext.java main method
public static void main(String[] args) {
    try (Connection con = DBContext.getConnection()) {
        if (con != null && !con.isClosed()) {
            System.out.println("✅ KẾT NỐI DATABASE THÀNH CÔNG!");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### Test 2: Kiểm tra dữ liệu trong Database
```sql
-- Kiểm tra số lượng records
SELECT COUNT(*) as MealCount FROM MealService WHERE status = 'active';
SELECT COUNT(*) as WellnessCount FROM WellnessService WHERE status = 'active';

-- Xem dữ liệu chi tiết
SELECT * FROM MealService WHERE status = 'active';
SELECT * FROM WellnessService WHERE status = 'active';
```

### Test 3: Kiểm tra Servlet logs
Xem console của Tomcat khi truy cập `/service-list`:
```
Found X meal services
Found Y wellness services
```

### Test 4: Kiểm tra hiển thị trên trang
✅ Trang hiển thị đúng các service cards  
✅ Filter button hoạt động (All / Ăn uống / Spa & Wellness)  
✅ Search box tìm kiếm theo tên  
✅ Add to cart button hoạt động  
✅ Mini cart sidebar hiển thị đúng  
✅ Quantity +/- buttons hoạt động  
✅ Cart data được lưu vào sessionStorage  

---

## 🐛 Troubleshooting

### Lỗi 1: Không hiển thị dữ liệu
**Nguyên nhân:** 
- Database không có data hoặc status không phải 'active'
- Kết nối database thất bại

**Giải pháp:**
1. Kiểm tra connection string trong `DBContext.java`
2. Chạy SQL script insert dữ liệu mẫu
3. Check Tomcat console logs để xem error message

### Lỗi 2: 404 Not Found
**Nguyên nhân:**
- Servlet mapping chưa đúng
- Project chưa được deploy

**Giải pháp:**
1. Kiểm tra `web.xml` có mapping `/service-list` chưa
2. Clean and Build lại project
3. Restart Tomcat

### Lỗi 3: 403 Access Denied
**Nguyên nhân:**
- Chưa đăng nhập
- User không có quyền truy cập

**Giải pháp:**
1. Đăng nhập vào hệ thống trước
2. Kiểm tra role của user trong database

### Lỗi 4: CSS không load
**Nguyên nhân:**
- File CSS không tồn tại
- Path không đúng

**Giải pháp:**
1. Kiểm tra file `web/assets/css/service-list.css` có tồn tại không
2. Kiểm tra link trong JSP: `<link rel="stylesheet" href="assets/css/service-list.css">`

---

## 📊 Dữ liệu mẫu đã insert

### MealService (5 records)
1. Bữa sáng Buffet - 250,000₫
2. Bữa trưa Set Menu - 350,000₫
3. Bữa tối Fine Dining - 750,000₫
4. BBQ Hải sản - 650,000₫
5. Lẩu Thái - 450,000₫

### WellnessService (8 records)
1. Massage Body Thư giãn - 500,000₫ (60 phút)
2. Massage Foot & Leg - 300,000₫ (45 phút)
3. Spa Mặt Collagen - 800,000₫ (90 phút)
4. Tắm Bùn Khoáng - 600,000₫ (60 phút)
5. Yoga & Meditation - 200,000₫ (60 phút)
6. Sauna & Jacuzzi - 350,000₫ (45 phút)
7. Thai Massage - 550,000₫ (90 phút)
8. Aromatherapy - 450,000₫ (60 phút)

---

## 🎯 Các tính năng đã implement

### Hiển thị dữ liệu ✅
- [x] Lấy dữ liệu từ database qua DAO
- [x] Hiển thị danh sách Meal Services
- [x] Hiển thị danh sách Wellness Services
- [x] Format giá tiền (VNĐ)
- [x] Format ngày tháng (dd/MM/yyyy)

### Filtering & Search ✅
- [x] Filter theo category (All / Meal / Wellness)
- [x] Search theo tên dịch vụ
- [x] Active state cho filter buttons

### Shopping Cart ✅
- [x] Add to cart functionality
- [x] Update quantity (+/-)
- [x] Remove from cart
- [x] Cart summary với total amount
- [x] Mini cart sidebar
- [x] SessionStorage persistence
- [x] Button state management

### UI/UX ✅
- [x] Modern gradient design
- [x] Responsive layout
- [x] Hover effects và animations
- [x] Icon indicators
- [x] Status badges

---

## 📞 Support
Nếu gặp vấn đề, check:
1. Tomcat logs trong console
2. Browser console (F12) để xem JavaScript errors
3. Network tab để xem HTTP requests

---

## ✨ Next Steps
- [ ] Implement checkout flow
- [ ] Add payment integration
- [ ] Add booking confirmation
- [ ] Email notification cho booking
- [ ] Admin panel để quản lý services



