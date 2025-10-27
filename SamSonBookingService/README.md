# 🏨 SamSon Travel Booking Service

Hệ thống quản lý đặt phòng và dịch vụ du lịch Sầm Sơn

---

## 📋 **MÔ TẢ DỰ ÁN**

Dự án **SamSon Travel Booking Service** là hệ thống quản lý booking khách sạn và dịch vụ du lịch, hỗ trợ:
- ✅ Đặt phòng online/offline (tại quầy)
- ✅ Quản lý dịch vụ ăn uống, spa, wellness
- ✅ Quản lý vận chuyển
- ✅ Thanh toán và in hóa đơn
- ✅ Quản lý khách hàng và nhân viên

---

## 🛠️ **CÔNG NGHỆ SỬ DỤNG**

| Công nghệ | Phiên bản | Mục đích |
|-----------|-----------|----------|
| **Java** | JDK 11+ | Backend language |
| **Jakarta EE** | 9.0+ | Servlet, JSP, JSTL |
| **SQL Server** | 2019+ | Database |
| **Apache Tomcat** | 10.0+ | Application server |
| **Apache Ant** | 1.10+ | Build tool |
| **Bootstrap** | 5.3 | CSS framework |
| **Font Awesome** | 6.4 | Icons |

---

## 📂 **CẤU TRÚC PROJECT**

```
SamSonBookingService/
├── src/java/
│   ├── controller/         # Servlets (6 files)
│   │   ├── LoginServlet.java
│   │   ├── RegisterServlet.java
│   │   ├── RoomListServlet.java
│   │   ├── ServiceListServlet.java
│   │   └── ...
│   ├── dao/                # Data Access Objects (16 files)
│   │   ├── RoomDAO.java
│   │   ├── MealServiceDAO.java
│   │   ├── WellnessServiceDAO.java
│   │   └── ...
│   ├── entity/             # Entity classes (16 files)
│   │   ├── Room.java
│   │   ├── Booking.java
│   │   └── ...
│   ├── filter/             # Filters
│   │   └── AuthFilter.java
│   └── util/               # Utilities (3 files)
│       ├── EmailUtil.java
│       ├── PasswordUtil.java
│       └── TokenGenerator.java
├── web/
│   ├── assets/css/         # Stylesheets
│   ├── error/              # Error pages
│   ├── *.jsp               # JSP pages
│   └── WEB-INF/
│       └── web.xml         # Deployment descriptor
├── database/               # SQL scripts
│   └── insert_data_final.sql
├── lib/                    # External libraries
└── build.xml               # Ant build file
```

---

## 🚀 **CÀI ĐẶT & CHẠY PROJECT**

### **1. Yêu cầu hệ thống:**

- ✅ JDK 11 trở lên
- ✅ Apache Tomcat 10.0+
- ✅ SQL Server 2019+
- ✅ NetBeans IDE 12+ (hoặc Eclipse/IntelliJ)

### **2. Clone project:**

```bash
git clone <repository-url>
cd SamSonBookingService
```

### **3. Cấu hình Database:**

#### **3.1. Tạo database:**

```sql
-- Chạy trong SQL Server Management Studio
CREATE DATABASE booking_travel;
```

#### **3.2. Import schema & data:**

```sql
-- File: database/insert_data_final.sql
USE booking_travel;
-- Chạy toàn bộ script
```

#### **3.3. Cập nhật connection string:**

File: `src/java/dao/DBContext.java`

```java
private static final String CONNECTION_URL = 
    "jdbc:sqlserver://localhost:1433;databaseName=booking_travel;trustServerCertificate=true";
private static final String USERNAME = "sa";        // ← Đổi username
private static final String PASSWORD = "your_password"; // ← Đổi password
```

### **4. Build project:**

#### **Option 1: NetBeans**
- Right-click project → **Clean and Build**

#### **Option 2: Command line**
```bash
ant clean
ant build
```

### **5. Deploy & Run:**

#### **Option 1: NetBeans**
- Right-click project → **Run**

#### **Option 2: Tomcat manual**
```bash
# Copy WAR file
cp dist/SamSonBookingService.war $TOMCAT_HOME/webapps/

# Start Tomcat
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
$TOMCAT_HOME/bin/startup.bat # Windows
```

### **6. Truy cập ứng dụng:**

```
http://localhost:8080/SamSonBookingService/
```

---

## 🧪 **TEST FEATURES**

### **Test Room List:**
```
http://localhost:8080/SamSonBookingService/room-list
```

**Kết quả mong đợi:**
- ✅ Hiển thị 6 loại phòng (2 hotels x 3 room types)
- ✅ Date picker hoạt động
- ✅ Shopping cart hoạt động
- ✅ Tính tổng tiền đúng

### **Test Service List:**
```
http://localhost:8080/SamSonBookingService/service-list
```

**Kết quả mong đợi:**
- ✅ Hiển thị 12 dịch vụ (6 meal + 6 wellness)
- ✅ Filter hoạt động
- ✅ Add to cart hoạt động

---

## 👥 **TÀI KHOẢN TEST**

### **Admin:**
```
Email: admin@samsontravel.com
Password: admin123
```

### **Front Office Staff:**
```
Email: khanh@samsontravel.com
Password: khanh123

Email: lan@samsontravel.com
Password: lan123
```

### **Customer:**
```
Email: customer1@gmail.com
Password: customer123
```

---

## 📊 **DATABASE SCHEMA**

### **Các bảng chính:**

| Bảng | Mô tả | Số dòng mẫu |
|------|-------|-------------|
| `Roles` | Vai trò người dùng | 5 |
| `Users` | Người dùng hệ thống | 5 |
| `Hotels` | Khách sạn | 2 |
| `Rooms` | Phòng | 6 |
| `ServiceCategories` | Danh mục dịch vụ | 4 |
| `Meal_Services` | Dịch vụ ăn uống | 6 |
| `Wellness_Services` | Dịch vụ spa/wellness | 6 |
| `TransportServices` | Dịch vụ vận chuyển | 5 |
| `Bookings` | Đơn đặt phòng | - |
| `Booking_Details` | Chi tiết booking | - |
| `Payments` | Thanh toán | - |
| `Offline_Customers` | Khách offline | 3 |

---

## 🎯 **TÍNH NĂNG CHÍNH**

### **✅ Đã hoàn thành:**
- [x] Login/Register/Email verification
- [x] Role-based authentication (Admin, Front Office, Customer)
- [x] Room listing với filter & search
- [x] Service listing (Meal & Wellness)
- [x] Shopping cart (sessionStorage)
- [x] Room DAO với full CRUD
- [x] Service DAOs (Meal & Wellness)

### **🚧 Đang phát triển:**
- [ ] Customer info form
- [ ] Booking summary & payment
- [ ] Print receipt
- [ ] Walk-in service (quick sale)
- [ ] Booking management
- [ ] Dashboard & reports

---

## 📖 **API ENDPOINTS**

### **Authentication:**
```
POST /login              → Login
POST /register           → Register
GET  /verify-email       → Verify email
POST /logout             → Logout
```

### **Rooms:**
```
GET  /room-list          → List all rooms
GET  /room-list?filter=single → Filter by room type
GET  /room-list?hotelId=1    → Filter by hotel
```

### **Services:**
```
GET  /service-list       → List all services
GET  /service-list?filter=MEAL     → Filter meals
GET  /service-list?filter=WELLNESS → Filter wellness
```

---

## 🐛 **TROUBLESHOOTING**

### **Lỗi 404 - Page Not Found:**
- ✅ Kiểm tra URL mapping trong servlet
- ✅ Clean & rebuild project
- ✅ Restart Tomcat

### **Lỗi 500 - Internal Server Error:**
- ✅ Kiểm tra database connection
- ✅ Xem log trong NetBeans Output
- ✅ Kiểm tra SQL Server đang chạy

### **CSS không load:**
- ✅ Check đường dẫn: `${pageContext.request.contextPath}/assets/css/...`
- ✅ Hard refresh: Ctrl+F5
- ✅ Clear browser cache

### **Database connection failed:**
- ✅ Kiểm tra SQL Server đang chạy
- ✅ Kiểm tra username/password trong `DBContext.java`
- ✅ Kiểm tra port 1433
- ✅ Enable TCP/IP trong SQL Server Configuration Manager

---

## 📝 **GIT WORKFLOW**

### **Branch strategy:**
```
main          → Production code
develop       → Development code
feature/*     → New features
bugfix/*      → Bug fixes
```

### **Commit message format:**
```
feat: Add room booking feature
fix: Fix shopping cart calculation
docs: Update README
style: Format code
refactor: Refactor RoomDAO
test: Add unit tests
```

---

## 👨‍💻 **TEAM**

- **Project Lead:** [Tên]
- **Backend Developer:** [Tên]
- **Frontend Developer:** [Tên]
- **Database Designer:** [Tên]

---

## 📄 **LICENSE**

This project is licensed for educational purposes only.

---

## 📞 **CONTACT**

- **Email:** contact@samsontravel.com
- **Phone:** 0237.123.4567
- **Address:** Sầm Sơn, Thanh Hóa

---

**Last Updated:** 27/11/2024

