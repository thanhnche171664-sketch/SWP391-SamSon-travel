DROP DATABASE IF EXISTS booking_travel;
GO
CREATE DATABASE booking_travel;
GO
USE booking_travel;
GO

-- 1️⃣ BẢNG VAI TRÒ NGƯỜI DÙNG
CREATE TABLE Roles (
    role_id INT IDENTITY(1,1) PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

-- 2️⃣ BẢNG NGƯỜI DÙNG HỆ THỐNG
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(50),
    password VARCHAR(255),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    gender VARCHAR(10) CHECK (gender IN ('male','female','other')),
    address VARCHAR(255),
    role_id INT,
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active','pending','inactive','suspended')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);

-- 2.1️⃣ BẢNG TOKEN XÁC NHẬN/RESET MẬT KHẨU
CREATE TABLE reset_tokens (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    used BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- 3️⃣ BẢNG DANH MỤC DỊCH VỤ
CREATE TABLE ServiceCategories (
    category_id INT IDENTITY(1,1) PRIMARY KEY,
    category_code VARCHAR(50) UNIQUE NOT NULL,   -- VD: HOTEL, TRANSPORT, MEAL, WELLNESS
    category_name NVARCHAR(100) NOT NULL         -- VD: Dịch vụ khách sạn, Dịch vụ vận chuyển...
);

-- 4️⃣ BẢNG KHÁCH SẠN
CREATE TABLE Hotels (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(MAX),
    address NVARCHAR(255),
    description NVARCHAR(MAX),
    manager_id INT,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (manager_id) REFERENCES Users(id)
);

-- 5️⃣ BẢNG PHÒNG
CREATE TABLE Rooms (
    id INT IDENTITY(1,1) PRIMARY KEY,
    hotel_id INT NOT NULL,
    room_type NVARCHAR(MAX) NOT NULL CHECK (room_type IN ('single', 'double', 'dormitory')),
    price DECIMAL(10,2) NOT NULL,
    total_rooms INT NOT NULL,
    available_rooms INT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (hotel_id) REFERENCES Hotels(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 6️⃣ BẢNG DỊCH VỤ VẬN CHUYỂN
CREATE TABLE TransportServices (
    transport_id INT IDENTITY(1,1) PRIMARY KEY,
    category_id INT NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL CHECK (vehicle_type IN ('CAR','MINIVAN','BUS','LIMOUSINE','SELF')),
    vehicle_name NVARCHAR(100),
    description NVARCHAR(MAX),
    pickup_location NVARCHAR(150) NOT NULL,
    departure_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    capacity INT NOT NULL,
    current_passengers INT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES ServiceCategories(category_id)
);

-- 7️⃣ BẢNG DỊCH VỤ ĂN UỐNG
CREATE TABLE Meal_Services (
    meal_id INT IDENTITY(1,1) PRIMARY KEY,
    hotel_id INT NOT NULL,
    category_id INT NOT NULL,
    meal_type NVARCHAR(50) NOT NULL CHECK (meal_type IN ('BREAKFAST','LUNCH','DINNER')),
    meal_date DATE NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(10,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (hotel_id) REFERENCES Hotels(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES ServiceCategories(category_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 8️⃣ BẢNG DỊCH VỤ WELLNESS / SPA
CREATE TABLE Wellness_Services (
    wellness_id INT IDENTITY(1,1) PRIMARY KEY,
    hotel_id INT NOT NULL,
    category_id INT NOT NULL,
    service_name NVARCHAR(150) NOT NULL,
    description NVARCHAR(500),
    base_price DECIMAL(10,2) NOT NULL DEFAULT 0,
    duration_minutes INT NULL,
    operating_hours NVARCHAR(100) NULL,
    capacity INT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (hotel_id) REFERENCES Hotels(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES ServiceCategories(category_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 9️⃣ BẢNG ĐẶT DỊCH VỤ (BOOKINGS)
CREATE TABLE Bookings (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NULL,
    hotel_id INT NULL,
    room_type VARCHAR(20) NULL CHECK (room_type IN ('single', 'double', 'dormitory')),
    number_of_rooms INT,
    transport_id INT NULL,
    transport_fee DECIMAL(10,2) NOT NULL DEFAULT 0,
    total_price DECIMAL(10,2) NOT NULL,
    booking_date DATETIME NOT NULL,
    booking_source VARCHAR(20) DEFAULT 'ONLINE' CHECK (booking_source IN ('ONLINE','OFFLINE')),
    created_by INT NULL,
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending','confirmed','canceled')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (hotel_id) REFERENCES Hotels(id),
    FOREIGN KEY (transport_id) REFERENCES TransportServices(transport_id),
    FOREIGN KEY (created_by) REFERENCES Users(id)
);

-- 🔟 BẢNG CHI TIẾT BOOKING
CREATE TABLE Booking_Details (
    id INT IDENTITY(1,1) PRIMARY KEY,
    booking_id INT NOT NULL,
    category_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES Bookings(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES ServiceCategories(category_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1️⃣1️⃣ BẢNG THANH TOÁN
CREATE TABLE Payments (
    payment_id INT IDENTITY(1,1) PRIMARY KEY,
    booking_id INT NOT NULL,
    transaction_id VARCHAR(100),
    currency VARCHAR(10) DEFAULT 'VND',
    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('CREDIT_CARD','BANK_TRANSFER','CASH')),
    payment_date DATETIME DEFAULT GETDATE(),
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PAID' CHECK (status IN ('PAID','FAILED','REFUNDED')),
    FOREIGN KEY (booking_id) REFERENCES Bookings(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1️⃣2️⃣ BẢNG GIẢM GIÁ
CREATE TABLE Discounts (
    discount_id INT IDENTITY(1,1) PRIMARY KEY,
    category_id INT NOT NULL,
    discount_type VARCHAR(20) NOT NULL CHECK (discount_type IN ('PERCENT','FIXED')),
    value DECIMAL(10,2) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES ServiceCategories(category_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1️⃣3️⃣ BẢNG RESET TOKEN
CREATE TABLE Reset_Tokens (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    used BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1️⃣4️⃣ BẢNG KHÁCH HÀNG ĐẶT OFFLINE
CREATE TABLE Offline_Customers (
    offline_customer_id INT IDENTITY(1,1) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    id_card_number VARCHAR(20),
    nationality NVARCHAR(50),
    gender VARCHAR(10) CHECK (gender IN ('male','female','other')),
    date_of_birth DATE,
    address NVARCHAR(255),
    created_at DATETIME DEFAULT GETDATE()
);

-- 1️⃣5️⃣ BẢNG MEDIA TOUR
CREATE TABLE Tour_Media (
    media_id INT IDENTITY(1,1) PRIMARY KEY,
    section NVARCHAR(100) NULL,
    title NVARCHAR(150) NULL,
    description NVARCHAR(500) NULL,
    media_type VARCHAR(20) NOT NULL CHECK (media_type IN ('IMAGE','VIDEO')),
    file_url NVARCHAR(255) NOT NULL,
    uploaded_by INT NULL,
    uploaded_at DATETIME DEFAULT GETDATE(),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
    FOREIGN KEY (uploaded_by) REFERENCES Users(id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

-- ===========================================
-- INSERT INITIAL DATA
-- ===========================================

-- Insert predefined roles
INSERT INTO Roles (role_name) VALUES 
    ('Administrator'),
    ('Service Manager'),
    ('Hotel Manager'),
    ('Customer'),
    ('Front Office');

-- Insert service categories
INSERT INTO ServiceCategories (category_code, category_name) VALUES 
    ('HOTEL', N'Dịch vụ khách sạn'),
    ('TRANSPORT', N'Dịch vụ vận chuyển'),
    ('MEAL', N'Dịch vụ ăn uống'),
    ('WELLNESS', N'Dịch vụ spa & wellness');

-- Insert sample hotel for testing
INSERT INTO Hotels (name, address, description, manager_id) VALUES 
    (N'Khách sạn Sầm Sơn Resort', N'123 Đường Trần Phú, Sầm Sơn, Thanh Hóa', N'Khách sạn nghỉ dưỡng cao cấp với view biển tuyệt đẹp', NULL);

-- Insert sample rooms
INSERT INTO Rooms (hotel_id, room_type, price, total_rooms, available_rooms) VALUES 
    (1, 'single', 500000.00, 20, 20),
    (1, 'double', 800000.00, 15, 15),
    (1, 'dormitory', 200000.00, 10, 10);

-- Insert sample transport services
INSERT INTO TransportServices (category_id, vehicle_type, vehicle_name, description, pickup_location, departure_time, price, capacity) VALUES 
    (2, 'BUS', N'Xe khách 45 chỗ', N'Xe khách tiện nghi, có điều hòa', N'Hà Nội - Ga Hà Nội', '2024-01-01 08:00:00', 150000.00, 45),
    (2, 'CAR', N'Xe ô tô 4 chỗ', N'Xe ô tô riêng tư, lái xe chuyên nghiệp', N'Hà Nội - Điểm hẹn', '2024-01-01 09:00:00', 800000.00, 4);

-- Insert sample meal services
INSERT INTO Meal_Services (hotel_id, category_id, meal_type, meal_date, description, price) VALUES 
    (1, 3, 'BREAKFAST', '2024-01-01', N'Buffet sáng với các món ăn Việt Nam và quốc tế', 150000.00),
    (1, 3, 'LUNCH', '2024-01-01', N'Set menu trưa với hải sản tươi sống', 300000.00),
    (1, 3, 'DINNER', '2024-01-01', N'Tiệc BBQ ngoài trời', 400000.00);

-- Insert sample wellness services
INSERT INTO Wellness_Services (hotel_id, category_id, service_name, description, base_price, duration_minutes, operating_hours, capacity) VALUES 
    (1, 4, N'Massage thư giãn', N'Massage body toàn thân với tinh dầu thiên nhiên', 500000.00, 60, N'8:00-22:00', 5),
    (1, 4, N'Spa mặt', N'Chăm sóc da mặt chuyên sâu', 300000.00, 45, N'9:00-21:00', 3),
    (1, 4, N'Tắm bùn khoáng', N'Tắm bùn khoáng tự nhiên tốt cho da', 200000.00, 30, N'10:00-20:00', 8);

-- Insert predefined roles for Login & Registration System
INSERT INTO Roles (role_name) VALUES 
    ('Administrator'),
    ('Service Manager'),
    ('Hotel Manager'),
    ('Customer'),
    ('Front Office');

-- Insert sample admin user for testing (password: admin123)
INSERT INTO Users (name, password, email, phone, gender, address, role_id, status) VALUES 
    ('Admin User', 'admin123', 'admin@samsontravel.com', '0123456789', 'MALE', 'Hà Nội', 1, 'ACTIVE');