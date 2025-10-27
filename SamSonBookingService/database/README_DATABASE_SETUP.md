# 🗄️ Hướng dẫn Setup Database

## ❌ **Lỗi bạn đang gặp:**
```
Invalid object name 'MealService'
```
**Nguyên nhân:** Bảng chưa được tạo trong database!

---

## ✅ **Giải pháp - Chạy theo thứ tự:**

### **Bước 1: Tạo bảng** 
Chạy file này **TRƯỚC TIÊN**:
```sql
database/create_tables.sql
```

**Cách chạy:**
1. Mở **SQL Server Management Studio (SSMS)**
2. Connect đến SQL Server
3. Mở file `create_tables.sql`
4. Chọn database `booking_travel` (hoặc chạy `USE booking_travel;`)
5. Nhấn **F5** hoặc **Execute**

**Kết quả mong đợi:**
```
✅ Đã tạo bảng ServiceCategory
✅ Đã thêm category MEAL
✅ Đã thêm category WELLNESS
✅ Đã tạo bảng Hotel
✅ Đã thêm hotel mặc định
✅ Đã tạo bảng MealService
✅ Đã tạo bảng WellnessService

========================================
KẾT QUẢ TẠO BẢNG:
========================================
✅ ServiceCategory - OK
✅ Hotel - OK
✅ MealService - OK
✅ WellnessService - OK

========================================
SẴN SÀNG INSERT DỮ LIỆU MẪU!
Chạy file: insert_sample_services.sql
========================================
```

---

### **Bước 2: Insert dữ liệu mẫu**
Sau khi bảng đã được tạo, chạy file này:
```sql
database/insert_sample_services.sql
```

**Cách chạy:**
1. Trong **SSMS**, mở file `insert_sample_services.sql`
2. Nhấn **F5** hoặc **Execute**

**Kết quả mong đợi:**
```
========================================
ĐANG INSERT DỮ LIỆU MẪU...
========================================
✅ Category IDs: MEAL=1, WELLNESS=2
🗑️ Đã xóa dữ liệu cũ
✅ Đã insert 5 MealService records
✅ Đã insert 8 WellnessService records

TableName              RecordCount
-------------------------------------
MealService Count      5
WellnessService Count  8
```

---

### **Bước 3: Kiểm tra dữ liệu**
Chạy query để verify:
```sql
-- Kiểm tra số lượng
SELECT COUNT(*) as MealCount FROM MealService WHERE status = 'active';
SELECT COUNT(*) as WellnessCount FROM WellnessService WHERE status = 'active';

-- Xem dữ liệu chi tiết
SELECT * FROM MealService WHERE status = 'active';
SELECT * FROM WellnessService WHERE status = 'active';
```

**Kết quả mong đợi:**
- **5 Meal Services** (Buffet, Set Menu, Fine Dining, BBQ, Lẩu)
- **8 Wellness Services** (Massage, Spa, Yoga, Sauna, v.v.)

---

### **Bước 4: Test lại web**
Refresh trang test:
```
http://localhost:8080/SamSonBookingService/test-db.jsp
```

Bây giờ sẽ thấy:
- ✅ Test 2: Database Statistics → **5 Meal**, **8 Wellness**
- ✅ Test 3: MealService Data → **Bảng 5 records**
- ✅ Test 4: WellnessService Data → **Bảng 8 records**

---

## 📊 **Cấu trúc Database được tạo:**

```
booking_travel
├── ServiceCategory
│   ├── category_id (PK)
│   ├── category_code (MEAL, WELLNESS)
│   └── category_name
│
├── Hotel
│   ├── id (PK)
│   ├── name
│   └── address
│
├── MealService
│   ├── meal_id (PK)
│   ├── hotel_id (FK → Hotel.id)
│   ├── category_id (FK → ServiceCategory.category_id)
│   ├── meal_type
│   ├── description
│   ├── price
│   └── status
│
└── WellnessService
    ├── wellness_id (PK)
    ├── hotel_id (FK → Hotel.id)
    ├── category_id (FK → ServiceCategory.category_id)
    ├── service_name
    ├── description
    ├── base_price
    ├── duration_minutes
    ├── operating_hours
    └── capacity
```

---

## 🔄 **Nếu muốn chạy lại từ đầu:**

### **Reset toàn bộ:**
```sql
-- Xóa dữ liệu (giữ bảng)
DELETE FROM MealService;
DELETE FROM WellnessService;

-- Hoặc xóa cả bảng
DROP TABLE IF EXISTS MealService;
DROP TABLE IF EXISTS WellnessService;
DROP TABLE IF EXISTS ServiceCategory;
DROP TABLE IF EXISTS Hotel;
```

Sau đó chạy lại từ **Bước 1**.

---

## ⚠️ **Lưu ý quan trọng:**

### 1. Thứ tự chạy
```
create_tables.sql → insert_sample_services.sql
     (BẮT BUỘC)        (Sau khi bảng đã có)
```

### 2. Foreign Key Dependencies
Các bảng phải tạo theo thứ tự:
1. ServiceCategory (không phụ thuộc)
2. Hotel (không phụ thuộc)
3. MealService (phụ thuộc vào 1 & 2)
4. WellnessService (phụ thuộc vào 1 & 2)

### 3. Database Name
Đảm bảo database là `booking_travel`:
```sql
USE booking_travel;
GO
```

### 4. Kiểm tra bảng User
Nếu chưa có bảng `[User]`, có thể bỏ qua count trong test:
```sql
-- Bảng User không ảnh hưởng đến MealService/WellnessService
```

---

## 🐛 **Troubleshooting:**

### Lỗi: "Database 'booking_travel' does not exist"
**Giải pháp:** Tạo database trước:
```sql
CREATE DATABASE booking_travel;
GO
USE booking_travel;
GO
```

### Lỗi: "Foreign key constraint..."
**Giải pháp:** Chạy lại từ đầu, đúng thứ tự các bước.

### Lỗi: "Procedure expects parameter '@MEAL_CATEGORY_ID'"
**Giải pháp:** Chưa chạy `create_tables.sql` để tạo categories.

---

## ✅ **Checklist hoàn thành:**

- [ ] Đã chạy `create_tables.sql`
- [ ] Thấy message "✅ Đã tạo bảng..."
- [ ] Đã chạy `insert_sample_services.sql`
- [ ] Thấy message "✅ Đã insert X records"
- [ ] Chạy query verify thành công
- [ ] Refresh `test-db.jsp` → thấy dữ liệu
- [ ] Truy cập `/service-list` → thấy service cards

---

## 🎯 **Kết quả cuối cùng:**

Sau khi hoàn thành, bạn sẽ có:
- ✅ 4 bảng database (ServiceCategory, Hotel, MealService, WellnessService)
- ✅ 2 categories (MEAL, WELLNESS)
- ✅ 1 hotel (SamSon Beach Resort)
- ✅ 5 meal services
- ✅ 8 wellness services
- ✅ Trang `test-db.jsp` hiển thị đầy đủ dữ liệu
- ✅ Trang `service-list` hoạt động bình thường

Good luck! 🚀





