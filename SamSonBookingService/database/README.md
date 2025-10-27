# 🗄️ Database Setup

## 📋 File chính

**`setup_database.sql`** - File duy nhất cần chạy!

---

## 🚀 Cách sử dụng

### Bước 1: Tạo database
```sql
CREATE DATABASE booking_travel;
```

### Bước 2: Chạy script
Mở file `setup_database.sql` trong **SQL Server Management Studio** và chạy (F5)

### Bước 3: Xem kết quả
```sql
USE booking_travel;

-- Xem hotels
SELECT * FROM Hotels;

-- Xem phòng
SELECT * FROM Rooms;

-- Xem dịch vụ
SELECT * FROM Meal_Services;
SELECT * FROM Wellness_Services;
```

---

## 📊 Dữ liệu mẫu

| Bảng | Số dòng | Mô tả |
|------|---------|-------|
| Roles | 5 | Administrator, Manager, Customer, Front Office |
| ServiceCategories | 4 | Hotel, Transport, Meal, Wellness |
| Hotels | 2 | Beach Resort, FLC Luxury |
| Rooms | 6 | 3 loại phòng x 2 hotels |
| Meal_Services | 6 | Breakfast, Lunch, Dinner |
| Wellness_Services | 6 | Massage, Spa, Sauna, Yoga |
| TransportServices | 5 | Bus, Car, Minivan |
| Users | 5 | Admin + 2 Staff + 2 Customers |
| Offline_Customers | 3 | Khách mẫu |

---

## 🔐 Tài khoản test

### Admin
```
Email: admin@samsontravel.com
Password: admin123
```

### Lễ tân
```
Email: khanh@samsontravel.com
Password: khanh123
```

### Khách hàng
```
Email: customer1@gmail.com
Password: customer123
```

---

## ⚠️ Lưu ý

- File này **XÓA** dữ liệu cũ trước khi insert mới
- Chỉ chạy **1 lần** khi setup
- Nếu chạy lại → Dữ liệu sẽ bị reset

