DROP DATABASE IF EXISTS booking_travel;
GO

-- ===========================================
-- SAMPLE BOOKINGS (for quick end-to-end tests)
-- ===========================================

DECLARE @now DATETIME = GETDATE();

-- Booking 1: Tour 1, Schedule 1, 2 guests, PENDING
INSERT INTO Bookings (
    user_id, tour_id, schedule_id, package_id,
    guest_count, subtotal, discount_total, tax_total, total_price,
    currency, booking_date, booking_source, created_by, status,
    created_at, updated_at,
    contact_name, contact_email, contact_phone
) VALUES (
    4, 1, 1, NULL,
    2, 2500000, 0, 0, 2500000,
    'VND', @now, 'ONLINE', 4, 'pending',
    @now, @now,
    N'Test User 1', 'test1@example.com', '0900000001'
);
DECLARE @bid_sample_1 INT = SCOPE_IDENTITY();

INSERT INTO Booking_Items (
    booking_id, item_type, item_id, title_snapshot, meta_json, start_time, end_time,
    quantity, unit_price, total_price
)
SELECT
    @bid_sample_1, 'TOUR', 1, (SELECT tour_name FROM Tours WHERE tour_id = 1),
    NULL,
    (SELECT departure_date FROM Tour_Schedules WHERE schedule_id = 1),
    (SELECT return_date FROM Tour_Schedules WHERE schedule_id = 1),
    2,
    1250000,
    2500000;

UPDATE Tour_Schedules SET booked_slots = booked_slots + 2 WHERE schedule_id = 1;

-- Booking 2: Tour 2, Schedule 4, Package (id ~ 4), 3 guests, CONFIRMED + PAID
-- Note: package ids start at 1; Tour 1 has 3 packages (1..3), Tour 2 packages (4..6)
INSERT INTO Bookings (
    user_id, tour_id, schedule_id, package_id,
    guest_count, subtotal, discount_total, tax_total, total_price,
    currency, booking_date, booking_source, created_by, status,
    created_at, updated_at,
    contact_name, contact_email, contact_phone
) VALUES (
    4, 2, 4, 4,
    3, 28500000, 0, 0, 28500000,
    'VND', @now, 'ONLINE', 4, 'confirmed',
    @now, @now,
    N'Test User 2', 'test2@example.com', '0900000002'
);
DECLARE @bid_sample_2 INT = SCOPE_IDENTITY();

-- Snapshot TOUR item
INSERT INTO Booking_Items (
    booking_id, item_type, item_id, title_snapshot, meta_json, start_time, end_time,
    quantity, unit_price, total_price
)
SELECT
    @bid_sample_2, 'TOUR', 2, (SELECT tour_name FROM Tours WHERE tour_id = 2),
    NULL,
    (SELECT departure_date FROM Tour_Schedules WHERE schedule_id = 4),
    (SELECT return_date FROM Tour_Schedules WHERE schedule_id = 4),
    3,
    5000000, -- (base 4.5m + adj 0.5m) per guest
    15000000;

-- Snapshot PACKAGE item (package id 4 for Tour 2)
INSERT INTO Booking_Items (
    booking_id, item_type, item_id, title_snapshot, meta_json, start_time, end_time,
    quantity, unit_price, total_price
) VALUES (
    @bid_sample_2, 'PACKAGE', 4, (SELECT package_name FROM Tour_Packages WHERE package_id = 4), NULL, NULL, NULL,
    3, (SELECT price FROM Tour_Packages WHERE package_id = 4), (SELECT price FROM Tour_Packages WHERE package_id = 4) * 3
);

UPDATE Tour_Schedules SET booked_slots = booked_slots + 3 WHERE schedule_id = 4;

-- Payment (PAID) for booking 2
INSERT INTO Payments (booking_id, transaction_id, currency, payment_method, payment_date, amount, status)
VALUES (@bid_sample_2, 'SAMPLE-TX-0001', 'VND', 'BANK_TRANSFER', @now, 28500000, 'PAID');

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
    avatar_url VARCHAR(255),
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
	image_url NVARCHAR(255) NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (hotel_id) REFERENCES Hotels(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES ServiceCategories(category_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 8️⃣ BẢNG HÌNH ẢNH (IMAGES) - Lưu nhiều ảnh cho Hotel và Room
CREATE TABLE Images (
    id INT IDENTITY(1,1) PRIMARY KEY,
    entity_type NVARCHAR(20) NOT NULL ,
    entity_id INT NOT NULL,
    image_url NVARCHAR(255) NOT NULL,
    is_primary BIT DEFAULT 0,
    display_order INT DEFAULT 0,
    alt_text NVARCHAR(255) NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- Index cho tìm kiếm nhanh
CREATE INDEX idx_images_entity ON Images(entity_type, entity_id);
CREATE INDEX idx_images_primary ON Images(entity_type, entity_id, is_primary);

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

-- Insert sample admin user for testing (password: admin123)
-- This must be inserted before Tour_Media because Tour_Media references uploaded_by
SET IDENTITY_INSERT Users ON;
INSERT INTO Users (id, name, password, email, phone, gender, address, role_id, status) VALUES 
    (1, N'Admin User', 'admin123', 'admin@gmail.com', '0123456789', 'male', N'Hà Nội', 1, 'active'),
    (2, N'Hotel Manager', 'hotel123', 'hotelmanager@gmail.com', '0987654321', 'male', N'Thanh Hóa', 3, 'active'),
    (3, N'Service Manager', 'service123', 'servicemanager@gmail.com', '0912345678', 'female', N'Hà Nội', 2, 'active'),
    (4, N'Customer Demo', 'customer123', 'customer@gmail.com', '0923456789', 'male', N'Hồ Chí Minh', 4, 'active');
SET IDENTITY_INSERT Users OFF;

-- Insert service categories
INSERT INTO ServiceCategories (category_code, category_name) VALUES 
    ('HOTEL', N'Dịch vụ khách sạn'),
    ('TRANSPORT', N'Dịch vụ vận chuyển'),
    ('MEAL', N'Dịch vụ ăn uống'),
    ('WELLNESS', N'Dịch vụ spa & wellness');

-- Insert sample hotels (images được lưu trong bảng Images riêng)
INSERT INTO Hotels (name, address, description, manager_id) VALUES 
    (N'Khách sạn Sầm Sơn Resort', 
     N'123 Đường Trần Phú, Sầm Sơn, Thanh Hóa', 
     N'Khách sạn nghỉ dưỡng cao cấp với view biển tuyệt đẹp, đầy đủ tiện nghi hiện đại. Phòng ốc sang trọng, dịch vụ chuyên nghiệp, phù hợp cho kỳ nghỉ gia đình và du lịch công tác.', 
     2),
    (N'Khách sạn Golden Bay Sầm Sơn', 
     N'456 Đường Hồ Xuân Hương, Sầm Sơn, Thanh Hóa', 
     N'Khách sạn 4 sao với hồ bơi ngoài trời và nhà hàng hải sản tươi sống. Vị trí đắc địa ngay cạnh bãi biển, view đẹp, không gian thoáng mát.', 
     2),
    (N'Khách sạn Sunshine Sầm Sơn', 
     N'789 Đường Biển, Sầm Sơn, Thanh Hóa', 
     N'Khách sạn view biển với các phòng nghỉ thoáng mát, phù hợp cho gia đình. Có khu vui chơi trẻ em, bãi đỗ xe miễn phí, wifi tốc độ cao.', 
     2),
    (N'Khách sạn Sea Pearl Resort', 
     N'15 Đường Quang Trung, Sầm Sơn, Thanh Hóa', 
     N'Resort sang trọng bên bờ biển với spa, gym, và các tiện ích giải trí đầy đủ. Phòng ốc thiết kế hiện đại, dịch vụ 5 sao.', 
     2),
    (N'Khách sạn Ocean View', 
     N'234 Đường Lê Lợi, Sầm Sơn, Thanh Hóa', 
     N'Khách sạn boutique với thiết kế độc đáo, tầm nhìn toàn cảnh biển. Thích hợp cho các cặp đôi và honeymoon.', 
     2);

-- Insert sample rooms cho tất cả khách sạn
INSERT INTO Rooms (hotel_id, room_type, price, total_rooms, available_rooms) VALUES 
    -- Khách sạn Sầm Sơn Resort (hotel_id = 1)
    (1, 'single', 500000.00, 20, 20),
    (1, 'double', 800000.00, 15, 15),
    (1, 'dormitory', 200000.00, 10, 10),
    
    -- Khách sạn Golden Bay Sầm Sơn (hotel_id = 2)
    (2, 'single', 600000.00, 15, 15),
    (2, 'double', 900000.00, 20, 20),
    (2, 'dormitory', 250000.00, 8, 8),
    
    -- Khách sạn Sunshine Sầm Sơn (hotel_id = 3)
    (3, 'single', 450000.00, 25, 25),
    (3, 'double', 750000.00, 18, 18),
    (3, 'dormitory', 180000.00, 12, 12),
    
    -- Khách sạn Sea Pearl Resort (hotel_id = 4)
    (4, 'single', 700000.00, 10, 10),
    (4, 'double', 1200000.00, 15, 15),
    (4, 'dormitory', 300000.00, 5, 5),
    
    -- Khách sạn Ocean View (hotel_id = 5)
    (5, 'single', 550000.00, 12, 12),
    (5, 'double', 850000.00, 10, 10),
    (5, 'dormitory', 220000.00, 6, 6);

-- Insert sample images cho Hotels và Rooms
INSERT INTO Images (entity_type, entity_id, image_url, is_primary, display_order, alt_text) VALUES
    -- Images cho Hotel 1 (Sầm Sơn Resort)
    ('hotel', 1, 'uploads/hotels/samson-resort-1.jpg', 1, 1, N'Sầm Sơn Resort - Mặt tiền'),
    ('hotel', 1, 'uploads/hotels/samson-resort-2.jpg', 0, 2, N'Sầm Sơn Resort - Hồ bơi'),
    ('hotel', 1, 'uploads/hotels/samson-resort-3.jpg', 0, 3, N'Sầm Sơn Resort - Nhà hàng'),
    ('hotel', 1, 'uploads/hotels/samson-resort-4.jpg', 0, 4, N'Sầm Sơn Resort - Phòng nghỉ'),
    
    -- Images cho Hotel 2 (Golden Bay)
    ('hotel', 2, 'uploads/hotels/golden-bay-1.jpg', 1, 1, N'Golden Bay - View biển'),
    ('hotel', 2, 'uploads/hotels/golden-bay-2.jpg', 0, 2, N'Golden Bay - Lobby'),
    ('hotel', 2, 'uploads/hotels/golden-bay-3.jpg', 0, 3, N'Golden Bay - Spa'),
    
    -- Images cho Hotel 3 (Sunshine)
    ('hotel', 3, 'uploads/hotels/sunshine-1.jpg', 1, 1, N'Sunshine - Mặt tiền'),
    ('hotel', 3, 'uploads/hotels/sunshine-2.jpg', 0, 2, N'Sunshine - Sảnh chính'),
    ('hotel', 3, 'uploads/hotels/sunshine-3.jpg', 0, 3, N'Sunshine - Phòng family'),
    
    -- Images cho Hotel 4 (Sea Pearl Resort)
    ('hotel', 4, 'uploads/hotels/seapearl-1.jpg', 1, 1, N'Sea Pearl - Resort view'),
    ('hotel', 4, 'uploads/hotels/seapearl-2.jpg', 0, 2, N'Sea Pearl - Beach'),
    ('hotel', 4, 'uploads/hotels/seapearl-3.jpg', 0, 3, N'Sea Pearl - Villa'),
    
    -- Images cho Hotel 5 (Ocean View)
    ('hotel', 5, 'uploads/hotels/oceanview-1.jpg', 1, 1, N'Ocean View - Mặt tiền'),
    ('hotel', 5, 'uploads/hotels/oceanview-2.jpg', 0, 2, N'Ocean View - Rooftop'),
    
    -- Images cho Rooms của Hotel 1
    ('room', 1, 'uploads/rooms/hotel1-single-1.jpg', 1, 1, N'Single Room - Giường đơn'),
    ('room', 1, 'uploads/rooms/hotel1-single-2.jpg', 0, 2, N'Single Room - Phòng tắm'),
    ('room', 2, 'uploads/rooms/hotel1-double-1.jpg', 1, 1, N'Double Room - Giường đôi'),
    ('room', 2, 'uploads/rooms/hotel1-double-2.jpg', 0, 2, N'Double Room - Ban công'),
    ('room', 3, 'uploads/rooms/hotel1-dorm-1.jpg', 1, 1, N'Dormitory - Giường tầng'),
    
    -- Images cho Rooms của Hotel 2
    ('room', 4, 'uploads/rooms/hotel2-single-1.jpg', 1, 1, N'Single Room - View biển'),
    ('room', 5, 'uploads/rooms/hotel2-double-1.jpg', 1, 1, N'Double Room - Deluxe'),
    ('room', 5, 'uploads/rooms/hotel2-double-2.jpg', 0, 2, N'Double Room - Jacuzzi'),
    ('room', 6, 'uploads/rooms/hotel2-dorm-1.jpg', 1, 1, N'Dormitory - Phòng tập thể'),
    
    -- Images cho Rooms của Hotel 3
    ('room', 7, 'uploads/rooms/hotel3-single-1.jpg', 1, 1, N'Single Room - Standard'),
    ('room', 8, 'uploads/rooms/hotel3-double-1.jpg', 1, 1, N'Double Room - Family'),
    ('room', 9, 'uploads/rooms/hotel3-dorm-1.jpg', 1, 1, N'Dormitory - Backpacker');

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
INSERT INTO Wellness_Services (hotel_id, category_id, service_name, description, base_price, duration_minutes, operating_hours, capacity, image_url, status) VALUES
(1, 4, N'Tắm bùn khoáng', N'Tắm bùn khoáng tự nhiên giúp làm mịn da, giảm đau nhức và thư giãn cơ thể.', 350000, 60, N'08:00–20:30', 10, N'Imagewellness/tambunkhoang.jpg', 'ACTIVE'),
(1, 4, N'Massage toàn thân', N'Liệu pháp massage toàn thân chuyên nghiệp giúp giảm căng thẳng và cải thiện lưu thông máu.', 400000, 60, N'09:00–22:00', 6, N'Imagewellness/massagetoanthan.jpg', 'ACTIVE'),
(1, 4, N'Xông hơi thảo dược', N'Xông hơi bằng thảo dược thiên nhiên giúp thải độc, tăng sức đề kháng và giảm stress.', 250000, 40, N'08:00–21:00', 8,N'Imagewellness/xonghoiithaoduoc.jpg', 'ACTIVE'),
(1, 4, N'Gội đầu dưỡng sinh', N'Kết hợp massage đầu, vai, cổ giúp thư giãn và giảm đau mỏi.', 99000, 45, N'09:00–21:30', 5,N'Imagewellness/goidauduongsinh.jpg', 'ACTIVE'),
(1, 4, N'Ngâm chân bằng muối khoáng', N'Ngâm chân với muối khoáng và tinh dầu, giúp giảm nhức mỏi và thư giãn cơ bắp.', 99000, 30, N'09:00–21:00', 6,N'Imagewellness/ngamchanbangmuoikhoang.jpg', 'ACTIVE'),
(1, 4, N'Thuê xe đạp', N'Dịch vụ thuê xe đạp để di chuyển và tham quan quanh khu du lịch.', 99000, 120, N'06:00–18:00', 20, N'Imagewellness/thuexedap.jpg', 'ACTIVE'),
(1, 4, N'Dịch vụ giặt ủi', N'Giặt, sấy và ủi quần áo cho khách lưu trú tại khu nghỉ dưỡng.', 99000, NULL, N'08:00–20:00', 5,N'Imagewellness/dichvugiatui.jpg', 'INACTIVE'),
(1, 4, N'Tắm trắng sữa tươi', N'Tắm dưỡng trắng da bằng sữa tươi nguyên chất và yến mạch, an toàn và tự nhiên.', 300000, 45, N'09:00–20:00', 4,N'Imagewellness/tamtrangsuatuoi.jpg', 'ACTIVE'),
(1, 4, N'Tắm khoáng nóng', N'Ngâm mình trong hồ khoáng nóng thiên nhiên giúp thư giãn, giảm stress và phục hồi năng lượng.', 320000, 50, N'07:00–21:00', 12,N'Imagewellness/tamkhoangnong.jpg', 'ACTIVE'),
(1, 4, N'Tắm sữa ong chúa', N'Tắm dưỡng thể bằng hỗn hợp sữa ong chúa và thảo mộc, giúp da sáng và mềm mịn.', 99000, 60, N'09:00–20:30', 5,N'Imagewellness/tamsuaongchua.jpg', 'ACTIVE'),
(1, 4, N'Chăm sóc da mặt', N'Liệu trình làm sạch, dưỡng ẩm và tái tạo da mặt bằng sản phẩm thiên nhiên.', 99000, 50, N'09:00–21:00', 4,N'Imagewellness/chamsocdamat.jpg', 'ACTIVE'),
(1, 4, N'Tẩy tế bào chết toàn thân', N'Tẩy tế bào chết bằng muối biển và tinh dầu, giúp da sáng mịn và thông thoáng.', 280000, 40, N'09:00–21:00', 5,N'Imagewellness/taytebaochettoanthankhachsan.jpg', 'ACTIVE'),
(1, 4, N'Xông hơi tinh dầu', N'Xông hơi với tinh dầu thiên nhiên giúp giảm stress, thư giãn và chăm sóc da.', 230000, 35, N'08:00–21:00', 6,N'Imagewellness/xonghoitinhdau.jpg', 'ACTIVE'),
(1, 4, N'Tắm khoáng lạnh', N'Tắm khoáng lạnh giúp làm săn chắc da, tăng tuần hoàn máu và giảm mệt mỏi.', 250000, 40, N'08:00–19:30', 8, N'Imagewellness/tamkhoanglanh.jpg', 'ACTIVE'),
(1, 4, N'Hồ bơi nước ấm trong nhà', N'Hồ bơi nước ấm trong nhà phù hợp thư giãn mọi thời tiết.', 150000, 90, N'06:00–21:00', 25,N'Imagewellness/hoboinuocamtrongnha.jpg', 'ACTIVE'),
(1, 4, N'Phòng tập Gym', N'Phòng tập thể hình hiện đại với đầy đủ thiết bị cardio và tạ.', 120000, 60, N'06:00–22:00', 20,N'Imagewellness/phongtapgym.jpg', 'ACTIVE'),
(1, 4, N'Cho thuê phao bơi', N'Dịch vụ cho thuê phao bơi và áo phao an toàn cho trẻ em và người lớn.', 50000, NULL, N'06:00–19:00', 50, N'Imagewellness/chothuephaoboi.jpg', 'INACTIVE'),
(1, 4, N'Thuê xe máy', N'Dịch vụ thuê xe máy cho khách di chuyển tự do trong khu vực.', 200000, 240, N'06:00–22:00', 15,N'Imagewellness/thuexemay.jpg', 'ACTIVE'),
(1, 4, N'Cà phê ngoài trời', N'Không gian cà phê ngoài trời thư giãn với tầm nhìn hướng núi hoặc biển.', 80000, 60, N'07:00–22:00', 40,N'Imagewellness/caphengoaitroi.jpg', 'INACTIVE'),
(1, 4, N'Dịch vụ dọn phòng', N'Dọn dẹp, thay khăn và vệ sinh phòng hàng ngày cho khách lưu trú.', 99000, NULL, N'08:00–18:00', 10,N'Imagewellness/dichvudonphong.jpg', 'INACTIVE'),
(1, 4, N'Cho thuê lều picnic', N'Thuê lều để cắm trại hoặc picnic ngoài trời cùng gia đình.', 99000, NULL, N'06:00–19:00', 10,N'Imagewellness/chothueleupicnic.jpg', 'INACTIVE'),
(1, 4, N'Hồ bơi nước mặn ngoài trời', N'Hồ bơi ngoài trời sử dụng nước mặn nhân tạo, giúp thư giãn và tốt cho da.', 99000, 90, N'06:00–20:00', 40,N'Imagewellness/hoboinuocmanngoaitroi.jpg', 'ACTIVE'),
(1, 4, N'Tắm nước hoa hồng', N'Tắm thư giãn với nước hoa hồng và tinh dầu tự nhiên, giúp da mềm mịn và thơm mát.', 99000, 45, N'09:00–20:00', 5,N'Imagewellness/tamnuochoahong.jpg', 'ACTIVE'),
(1, 4, N'Massage chân bằng đá nóng', N'Massage chân kết hợp đá bóng nóng giúp kích thích huyệt đạo, giảm nhức mỏi.', 99000, 30, N'09:00–22:00', 6,N'Imagewellness/massagechanbangdanong.jpg', 'ACTIVE'),
(1, 4, N'Lớp học Yoga', N'Lớp yoga ngoài trời giúp khởi động ngày mới, tăng cường sức khỏe và tinh thần.', 99000, 60, N'06:00–07:30', 20,N'Imagewellness/lophocyoga.jpg', 'ACTIVE');




-- ===========================================
-- TOUR SYSTEM TABLES
-- ===========================================

-- 1️⃣6️⃣ BẢNG TOURS (Tour Packages)
CREATE TABLE Tours (
    tour_id INT IDENTITY(1,1) PRIMARY KEY,
    tour_name NVARCHAR(200) NOT NULL,
    description NVARCHAR(MAX),
    duration_days INT NOT NULL DEFAULT 1,
    duration_nights INT NOT NULL DEFAULT 0,
    base_price DECIMAL(12,2) NOT NULL,
    featured_image NVARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE','DRAFT')),
    highlights NVARCHAR(MAX), -- JSON string for tour highlights
    inclusions NVARCHAR(MAX), -- What's included
    exclusions NVARCHAR(MAX), -- What's not included
    difficulty_level VARCHAR(20) DEFAULT 'EASY' CHECK (difficulty_level IN ('EASY','MODERATE','HARD','EXPERT')),
    min_age INT DEFAULT 0,
    max_group_size INT DEFAULT 20,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);

-- 1️⃣7️⃣ BẢNG TOUR SCHEDULES (Lịch trình tour)
CREATE TABLE Tour_Schedules (
    schedule_id INT IDENTITY(1,1) PRIMARY KEY,
    tour_id INT NOT NULL,
    departure_date DATETIME NOT NULL,
    return_date DATETIME NOT NULL,
    available_slots INT NOT NULL DEFAULT 20,
    booked_slots INT NOT NULL DEFAULT 0,
    price_adjustment DECIMAL(10,2) DEFAULT 0, -- Price adjustment for this schedule
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','FULL','CANCELLED','COMPLETED')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (tour_id) REFERENCES Tours(tour_id) ON DELETE CASCADE
);

-- 1️⃣8️⃣ BẢNG TOUR ITINERARIES (Chi tiết hành trình)
CREATE TABLE Tour_Itineraries (
    itinerary_id INT IDENTITY(1,1) PRIMARY KEY,
    tour_id INT NOT NULL,
    day_number INT NOT NULL,
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(MAX),
    activities NVARCHAR(MAX), -- Detailed activities for the day
    accommodation NVARCHAR(200), -- Where to stay
    meals_included NVARCHAR(100), -- Breakfast, Lunch, Dinner
    transport_info NVARCHAR(200), -- Transportation details
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (tour_id) REFERENCES Tours(tour_id) ON DELETE CASCADE
);

-- 1️⃣9️⃣ BẢNG TOUR PACKAGES (Gói tour với dịch vụ kèm theo)
CREATE TABLE Tour_Packages (
    package_id INT IDENTITY(1,1) PRIMARY KEY,
    tour_id INT NOT NULL,
    package_name NVARCHAR(200) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(12,2) NOT NULL,
    hotel_id INT NULL,
    transport_id INT NULL,
    includes_meals BIT DEFAULT 0,
    includes_wellness BIT DEFAULT 0,
    package_type VARCHAR(20) DEFAULT 'STANDARD' CHECK (package_type IN ('BASIC','STANDARD','PREMIUM','LUXURY')),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (tour_id) REFERENCES Tours(tour_id) ON DELETE CASCADE,
    FOREIGN KEY (hotel_id) REFERENCES Hotels(id),
    FOREIGN KEY (transport_id) REFERENCES TransportServices(transport_id)
);

-- 2️⃣0️⃣ BẢNG TESTIMONIALS (Đánh giá khách hàng)
CREATE TABLE Testimonials (
    testimonial_id INT IDENTITY(1,1) PRIMARY KEY,
    customer_name NVARCHAR(100) NOT NULL,
    customer_email VARCHAR(100),
    customer_avatar NVARCHAR(255),
    tour_id INT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text NVARCHAR(MAX) NOT NULL,
    review_date DATETIME DEFAULT GETDATE(),
    status VARCHAR(20) DEFAULT 'APPROVED' CHECK (status IN ('PENDING','APPROVED','REJECTED')),
    FOREIGN KEY (tour_id) REFERENCES Tours(tour_id) ON DELETE SET NULL
);

-- ===========================================
-- ENHANCE EXISTING TABLES
-- ===========================================

-- Add icons and display order to ServiceCategories
ALTER TABLE ServiceCategories ADD 
    icon_class VARCHAR(100),
    display_order INT DEFAULT 0,
    description NVARCHAR(500);

-- Add more fields to Hotels
ALTER TABLE Hotels ADD 
    rating DECIMAL(3,2) DEFAULT 0,
    featured BIT DEFAULT 0,
    amenities NVARCHAR(MAX),
    check_in_time TIME DEFAULT '14:00:00',
    check_out_time TIME DEFAULT '12:00:00';

-- ===========================================
-- INSERT COMPREHENSIVE SAMPLE DATA
-- ===========================================



-- Update ServiceCategories with icons and descriptions
UPDATE ServiceCategories SET 
    icon_class = 'fas fa-hotel',
    display_order = 1,
    description = N'Dịch vụ lưu trú và nghỉ dưỡng cao cấp'
WHERE category_code = 'HOTEL';

UPDATE ServiceCategories SET 
    icon_class = 'fas fa-bus',
    display_order = 2,
    description = N'Dịch vụ vận chuyển tiện nghi và an toàn'
WHERE category_code = 'TRANSPORT';

UPDATE ServiceCategories SET 
    icon_class = 'fas fa-utensils',
    display_order = 3,
    description = N'Ẩm thực đa dạng và hấp dẫn'
WHERE category_code = 'MEAL';

UPDATE ServiceCategories SET 
    icon_class = 'fas fa-spa',
    display_order = 4,
    description = N'Dịch vụ spa và chăm sóc sức khỏe'
WHERE category_code = 'WELLNESS';

-- Insert comprehensive hotel data
INSERT INTO Hotels (name, address, description, manager_id, rating, featured, amenities, check_in_time, check_out_time) VALUES 
    (N'Khách sạn Sầm Sơn Resort', N'123 Đường Trần Phú, Sầm Sơn, Thanh Hóa', N'Khách sạn nghỉ dưỡng cao cấp với view biển tuyệt đẹp', NULL, 4.5, 1, N'WiFi, Pool, Spa, Restaurant, Beach Access', '14:00:00', '12:00:00'),
    (N'FLC Sầm Sơn Beach & Golf Resort', N'Đường Trần Phú, Sầm Sơn, Thanh Hóa', N'Resort cao cấp với sân golf và bãi biển riêng', NULL, 4.8, 1, N'Golf Course, Private Beach, Multiple Restaurants, Conference Rooms', '15:00:00', '11:00:00'),
    (N'Khách sạn Sun World Sầm Sơn', N'Khu du lịch Sun World, Sầm Sơn', N'Khách sạn trong khu vui chơi giải trí', NULL, 4.2, 1, N'Theme Park Access, Water Park, Entertainment Shows', '14:00:00', '12:00:00'),
    (N'Khách sạn Hải Đăng', N'456 Đường Lê Lợi, Sầm Sơn', N'Khách sạn view biển với giá cả hợp lý', NULL, 4.0, 0, N'WiFi, Restaurant, Beach View', '14:00:00', '12:00:00'),
    (N'Khách sạn Sao Mai', N'789 Đường Hùng Vương, Sầm Sơn', N'Khách sạn gia đình thân thiện', NULL, 3.8, 0, N'WiFi, Family Rooms, Restaurant', '14:00:00', '12:00:00');

-- Insert comprehensive tour data
INSERT INTO Tours (tour_name, description, duration_days, duration_nights, base_price, featured_image, highlights, inclusions, exclusions, difficulty_level, min_age, max_group_size) VALUES 
    (N'Khám Phá Sầm Sơn 2N1Đ', N'Trải nghiệm trọn vẹn vẻ đẹp của Sầm Sơn với tour 2 ngày 1 đêm', 2, 1, 2500000.00, 'heroSection/bai-bien-sam-son-1-1024x682.webp', 
     N'["Bãi biển Sầm Sơn tuyệt đẹp", "Thưởng thức hải sản tươi ngon", "Tham quan chùa Độc Cước", "Hoàng hôn trên biển"]',
     N'["Vé vào cửa các điểm tham quan", "Hướng dẫn viên chuyên nghiệp", "Bảo hiểm du lịch", "Nước uống trên xe"]',
     N'["Chi phí cá nhân", "Đồ uống có cồn", "Quà lưu niệm", "Chi phí phát sinh"]',
     'EASY', 5, 25),
     
    (N'Sầm Sơn Romance 3N2Đ', N'Tour lãng mạn dành cho các cặp đôi tại Sầm Sơn', 3, 2, 4500000.00, 'heroSection/du-an-flc-sam-son-canh-quan-xanh.jpg',
     N'["Dinner lãng mạn trên biển", "Massage đôi tại spa", "Hoàng hôn tại đảo Hòn Mê", "Chụp ảnh cưới tại bãi biển"]',
     N'["Khách sạn 4 sao", "Ăn sáng buffet", "Vé tham quan", "Hướng dẫn viên riêng"]',
     N'["Chi phí cá nhân", "Đồ uống", "Quà tặng", "Chi phí phát sinh"]',
     'EASY', 18, 15),
     
    (N'Gia Đình Vui Vẻ Sầm Sơn 4N3Đ', N'Tour gia đình với nhiều hoạt động thú vị cho trẻ em', 4, 3, 6000000.00, 'heroSection/f98951892ebd77f285368767785d5740.webp',
     N'["Sun World Theme Park", "Water Park cho trẻ em", "Tham quan làng nghề", "Hoạt động team building"]',
     N'["Khách sạn gia đình", "Ăn sáng", "Vé công viên", "Hoạt động cho trẻ"]',
     N'["Chi phí cá nhân", "Đồ chơi", "Quà lưu niệm", "Chi phí phát sinh"]',
     'EASY', 3, 30),
     
    (N'Wellness Retreat Sầm Sơn 5N4Đ', N'Retreat chăm sóc sức khỏe và thư giãn tại Sầm Sơn', 5, 4, 8000000.00, 'heroSection/sunworld-samson-gioithieu-3.webp',
     N'["Spa treatment hàng ngày", "Yoga trên bãi biển", "Thiền định", "Massage trị liệu", "Detox juice"]',
     N'["Khách sạn spa", "Spa package", "Yoga classes", "Healthy meals", "Personal trainer"]',
     N'["Chi phí cá nhân", "Thuốc bổ sung", "Quà lưu niệm", "Chi phí phát sinh"]',
     'MODERATE', 16, 12),
     
    (N'Adventure Sầm Sơn 3N2Đ', N'Tour khám phá và phiêu lưu tại Sầm Sơn', 3, 2, 3500000.00, 'heroSection/bai-bien-sam-son-1-1024x682.webp',
     N'["Kayaking trên biển", "Trekking núi", "Snorkeling", "Camping trên bãi biển"]',
     N'["Thiết bị thể thao", "Hướng dẫn viên", "Bảo hiểm", "Camping gear"]',
     N'["Chi phí cá nhân", "Đồ uống", "Quà lưu niệm", "Chi phí phát sinh"]',
     'HARD', 12, 20),
     
    (N'Culinary Tour Sầm Sơn 2N1Đ', N'Khám phá ẩm thực đặc sản Sầm Sơn', 2, 1, 2200000.00, 'heroSection/du-an-flc-sam-son-canh-quan-xanh.jpg',
     N'["Thưởng thức hải sản tươi", "Học nấu ăn", "Tham quan chợ cá", "Wine tasting"]',
     N'["Cooking class", "Tất cả bữa ăn", "Hướng dẫn viên", "Recipe book"]',
     N'["Chi phí cá nhân", "Đồ uống", "Quà lưu niệm", "Chi phí phát sinh"]',
     'EASY', 8, 18),
     
    (N'Photography Tour Sầm Sơn 3N2Đ', N'Tour chụp ảnh chuyên nghiệp tại Sầm Sơn', 3, 2, 4000000.00, 'heroSection/f98951892ebd77f285368767785d5740.webp',
     N'["Golden hour photography", "Sunset shots", "Cultural sites", "Portrait sessions"]',
     N'["Photography guide", "Equipment rental", "Photo editing", "Digital gallery"]',
     N'["Chi phí cá nhân", "Camera cá nhân", "Quà lưu niệm", "Chi phí phát sinh"]',
     'MODERATE', 14, 15),
     
    (N'Luxury VIP Sầm Sơn 4N3Đ', N'Trải nghiệm VIP cao cấp tại Sầm Sơn', 4, 3, 12000000.00, 'heroSection/sunworld-samson-gioithieu-3.webp',
     N'["Private beach access", "Butler service", "Helicopter tour", "Fine dining", "Luxury spa"]',
     N'["Villa riêng", "Private chef", "Luxury transport", "Personal concierge", "Premium activities"]',
     N'["Chi phí cá nhân", "Shopping", "Quà lưu niệm", "Chi phí phát sinh"]',
     'EASY', 18, 8);

-- Insert tour schedules
INSERT INTO Tour_Schedules (tour_id, departure_date, return_date, available_slots, booked_slots, price_adjustment, status) VALUES 
    -- Tour 1: Khám Phá Sầm Sơn 2N1Đ
    (1, '2024-02-15 08:00:00', '2024-02-16 18:00:00', 25, 5, 0, 'ACTIVE'),
    (1, '2024-02-22 08:00:00', '2024-02-23 18:00:00', 25, 8, 200000, 'ACTIVE'),
    (1, '2024-03-01 08:00:00', '2024-03-02 18:00:00', 25, 12, 0, 'ACTIVE'),
    
    -- Tour 2: Sầm Sơn Romance 3N2Đ
    (2, '2024-02-14 10:00:00', '2024-02-16 16:00:00', 15, 3, 500000, 'ACTIVE'),
    (2, '2024-02-28 10:00:00', '2024-03-01 16:00:00', 15, 7, 0, 'ACTIVE'),
    
    -- Tour 3: Gia Đình Vui Vẻ Sầm Sơn 4N3Đ
    (3, '2024-02-17 09:00:00', '2024-02-20 17:00:00', 30, 10, 0, 'ACTIVE'),
    (3, '2024-03-08 09:00:00', '2024-03-11 17:00:00', 30, 15, 300000, 'ACTIVE'),
    
    -- Tour 4: Wellness Retreat Sầm Sơn 5N4Đ
    (4, '2024-02-19 08:00:00', '2024-02-23 18:00:00', 12, 2, 0, 'ACTIVE'),
    (4, '2024-03-15 08:00:00', '2024-03-19 18:00:00', 12, 5, 800000, 'ACTIVE'),
    
    -- Tour 5: Adventure Sầm Sơn 3N2Đ
    (5, '2024-02-24 07:00:00', '2024-02-26 19:00:00', 20, 4, 0, 'ACTIVE'),
    (5, '2024-03-10 07:00:00', '2024-03-12 19:00:00', 20, 8, 400000, 'ACTIVE');

-- Insert tour itineraries
INSERT INTO Tour_Itineraries (tour_id, day_number, title, description, activities, accommodation, meals_included, transport_info) VALUES 
    -- Tour 1: Khám Phá Sầm Sơn 2N1Đ
    (1, 1, N'Ngày 1: Khám phá bãi biển Sầm Sơn', N'Bắt đầu hành trình khám phá Sầm Sơn', 
     N'["Check-in khách sạn", "Tham quan bãi biển Sầm Sơn", "Tắm biển và thư giãn", "Thưởng thức hải sản tươi", "Ngắm hoàng hôn trên biển"]',
     N'Khách sạn Sầm Sơn Resort', N'Trưa, Tối', N'Xe ô tô 45 chỗ'),
    (1, 2, N'Ngày 2: Tham quan văn hóa và mua sắm', N'Khám phá văn hóa và mua sắm đặc sản', 
     N'["Tham quan chùa Độc Cước", "Tham quan làng nghề", "Mua sắm đặc sản", "Check-out và về Hà Nội"]',
     N'Không', N'Sáng', N'Xe ô tô 45 chỗ'),
     
    -- Tour 2: Sầm Sơn Romance 3N2Đ
    (2, 1, N'Ngày 1: Chào mừng đến với thiên đường lãng mạn', N'Bắt đầu hành trình lãng mạn', 
     N'["Check-in resort cao cấp", "Massage đôi tại spa", "Dinner lãng mạn trên biển", "Đi dạo trên bãi biển"]',
     N'FLC Sầm Sơn Resort', N'Trưa, Tối', N'Xe limousine'),
    (2, 2, N'Ngày 2: Khám phá đảo Hòn Mê', N'Trải nghiệm lãng mạn tại đảo', 
     N'["Cruise đến đảo Hòn Mê", "Chụp ảnh cưới", "Picnic trên đảo", "Snorkeling", "Hoàng hôn trên đảo"]',
     N'FLC Sầm Sơn Resort', N'Sáng, Trưa, Tối', N'Cruise boat'),
    (2, 3, N'Ngày 3: Thư giãn và chia tay', N'Ngày cuối thư giãn', 
     N'["Yoga trên bãi biển", "Spa treatment", "Mua sắm", "Check-out và về"]',
     N'Không', N'Sáng', N'Xe limousine');

-- Insert tour packages
INSERT INTO Tour_Packages (tour_id, package_name, description, price, hotel_id, transport_id, includes_meals, includes_wellness, package_type) VALUES 
    -- Packages for Tour 1
    (1, N'Gói Cơ Bản', N'Gói tour cơ bản với dịch vụ tiêu chuẩn', 2500000.00, 1, 1, 1, 0, 'BASIC'),
    (1, N'Gói Tiêu Chuẩn', N'Gói tour tiêu chuẩn với dịch vụ tốt hơn', 3200000.00, 2, 2, 1, 1, 'STANDARD'),
    (1, N'Gói Cao Cấp', N'Gói tour cao cấp với dịch vụ premium', 4500000.00, 2, 2, 1, 1, 'PREMIUM'),
    
    -- Packages for Tour 2
    (2, N'Gói Romance Tiêu Chuẩn', N'Gói lãng mạn tiêu chuẩn', 4500000.00, 2, 2, 1, 1, 'STANDARD'),
    (2, N'Gói Romance Cao Cấp', N'Gói lãng mạn cao cấp', 6500000.00, 2, 2, 1, 1, 'PREMIUM'),
    (2, N'Gói Romance Luxury', N'Gói lãng mạn luxury', 8500000.00, 2, 2, 1, 1, 'LUXURY'),
    
    -- Packages for Tour 3
    (3, N'Gói Gia Đình Cơ Bản', N'Gói gia đình cơ bản', 6000000.00, 4, 1, 1, 0, 'BASIC'),
    (3, N'Gói Gia Đình Tiêu Chuẩn', N'Gói gia đình tiêu chuẩn', 7500000.00, 1, 2, 1, 1, 'STANDARD'),
    
    -- Packages for Tour 4
    (4, N'Gói Wellness Tiêu Chuẩn', N'Gói wellness tiêu chuẩn', 8000000.00, 2, 2, 1, 1, 'STANDARD'),
    (4, N'Gói Wellness Premium', N'Gói wellness premium', 12000000.00, 2, 2, 1, 1, 'PREMIUM');

-- Insert testimonials
INSERT INTO Testimonials (customer_name, customer_email, customer_avatar, tour_id, rating, review_text, review_date, status) VALUES 
    (N'Nguyễn Văn An', 'an.nguyen@email.com', 'avatars/customer1.jpg', 1, 5, N'Tour rất tuyệt vời! Hướng dẫn viên nhiệt tình, cảnh đẹp, thức ăn ngon. Sẽ quay lại lần nữa!', '2024-01-15', 'APPROVED'),
    (N'Trần Thị Bình', 'binh.tran@email.com', 'avatars/customer2.jpg', 2, 5, N'Chuyến đi lãng mạn hoàn hảo cho cặp đôi. Resort đẹp, dịch vụ tốt, kỷ niệm đáng nhớ!', '2024-01-20', 'APPROVED'),
    (N'Lê Minh Cường', 'cuong.le@email.com', 'avatars/customer3.jpg', 3, 4, N'Gia đình có khoảng thời gian vui vẻ. Trẻ em rất thích Sun World. Đáng giá!', '2024-01-25', 'APPROVED'),
    (N'Phạm Thị Dung', 'dung.pham@email.com', 'avatars/customer4.jpg', 4, 5, N'Retreat wellness tuyệt vời! Cảm thấy thư giãn và khỏe mạnh hơn sau chuyến đi.', '2024-01-30', 'APPROVED'),
    (N'Hoàng Văn Em', 'em.hoang@email.com', 'avatars/customer5.jpg', 5, 4, N'Tour adventure thú vị! Kayaking và snorkeling rất hay. Phù hợp với người thích phiêu lưu.', '2024-02-05', 'APPROVED'),
    (N'Vũ Thị Phương', 'phuong.vu@email.com', 'avatars/customer6.jpg', 6, 5, N'Culinary tour tuyệt vời! Học được nhiều món ăn ngon và thưởng thức hải sản tươi.', '2024-02-10', 'APPROVED');

-- Insert comprehensive tour media
INSERT INTO Tour_Media (section, title, description, media_type, file_url, uploaded_by, status) VALUES 
    -- Hero Section Images
    ('hero', N'Bãi biển Sầm Sơn tuyệt đẹp', N'Hình ảnh bãi biển Sầm Sơn với nước biển trong xanh', 'IMAGE', 'heroSection/bai-bien-sam-son-1-1024x682.webp', 1, 'ACTIVE'),
    ('hero', N'FLC Sầm Sơn Resort', N'Resort cao cấp với cảnh quan xanh mát', 'IMAGE', 'heroSection/du-an-flc-sam-son-canh-quan-xanh.jpg', 1, 'ACTIVE'),
    ('hero', N'Sun World Sầm Sơn', N'Khu vui chơi giải trí Sun World', 'IMAGE', 'heroSection/sunworld-samson-gioithieu-3.webp', 1, 'ACTIVE'),
    ('hero', N'Cảnh quan thiên nhiên', N'Cảnh quan thiên nhiên tuyệt đẹp của Sầm Sơn', 'IMAGE', 'heroSection/f98951892ebd77f285368767785d5740.webp', 1, 'ACTIVE'),
    
    -- Tour Gallery Images
    ('tours', N'Tour Khám Phá Sầm Sơn', N'Hình ảnh tour khám phá Sầm Sơn', 'IMAGE', 'tours/tour-kham-pha-1.jpg', 1, 'ACTIVE'),
    ('tours', N'Tour Romance', N'Hình ảnh tour lãng mạn', 'IMAGE', 'tours/tour-romance-1.jpg', 1, 'ACTIVE'),
    ('tours', N'Tour Gia Đình', N'Hình ảnh tour gia đình', 'IMAGE', 'tours/tour-gia-dinh-1.jpg', 1, 'ACTIVE'),
    ('tours', N'Tour Wellness', N'Hình ảnh tour wellness', 'IMAGE', 'tours/tour-wellness-1.jpg', 1, 'ACTIVE'),
    
    -- Destination Images
    ('destinations', N'Chùa Độc Cước', N'Chùa Độc Cước - điểm tham quan nổi tiếng', 'IMAGE', 'destinations/chua-doc-cuoc.jpg', 1, 'ACTIVE'),
    ('destinations', N'Đảo Hòn Mê', N'Đảo Hòn Mê với cảnh quan hoang sơ', 'IMAGE', 'destinations/hon-me-island.jpg', 1, 'ACTIVE'),
    ('destinations', N'Làng nghề truyền thống', N'Làng nghề truyền thống tại Sầm Sơn', 'IMAGE', 'destinations/lang-nghe.jpg', 1, 'ACTIVE'),
    ('destinations', N'Chợ cá Sầm Sơn', N'Chợ cá tươi sống tại Sầm Sơn', 'IMAGE', 'destinations/cho-ca.jpg', 1, 'ACTIVE');

-- ===========================================
-- BOOKING EXTENSIONS + BOOKING_ITEMS + INDEXES + STORED PROCEDURE
-- ===========================================

-- Extend Bookings with tour linkage, contact info, and pricing snapshots
ALTER TABLE Bookings ADD 
    tour_id INT NULL,
    schedule_id INT NULL,
    package_id INT NULL,
    guest_count INT NOT NULL CONSTRAINT DF_Bookings_guest_count DEFAULT(1),
    contact_name NVARCHAR(150) NULL,
    contact_email VARCHAR(100) NULL,
    contact_phone VARCHAR(20) NULL,
    check_in_date DATE NULL,
    check_out_date DATE NULL,
    subtotal DECIMAL(12,2) NULL,
    discount_total DECIMAL(12,2) NULL,
    tax_total DECIMAL(12,2) NULL,
    currency VARCHAR(10) NOT NULL CONSTRAINT DF_Bookings_currency DEFAULT('VND');

-- Foreign keys for new linkage
ALTER TABLE Bookings ADD CONSTRAINT FK_Bookings_Tours_tour_id FOREIGN KEY (tour_id) REFERENCES Tours(tour_id);
ALTER TABLE Bookings ADD CONSTRAINT FK_Bookings_Tour_Schedules_schedule_id FOREIGN KEY (schedule_id) REFERENCES Tour_Schedules(schedule_id);
ALTER TABLE Bookings ADD CONSTRAINT FK_Bookings_Tour_Packages_package_id FOREIGN KEY (package_id) REFERENCES Tour_Packages(package_id);

-- Create Booking_Items (polymorphic booking details with snapshots)
CREATE TABLE Booking_Items (
    id INT IDENTITY(1,1) PRIMARY KEY,
    booking_id INT NOT NULL,
    item_type VARCHAR(20) NOT NULL CHECK (item_type IN ('TOUR','PACKAGE','ROOM','TRANSPORT','MEAL','WELLNESS')),
    item_id INT NULL,
    title_snapshot NVARCHAR(255) NULL,
    meta_json NVARCHAR(MAX) NULL,
    start_time DATETIME NULL,
    end_time DATETIME NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(12,2) NOT NULL,
    total_price DECIMAL(12,2) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Booking_Items_Bookings_booking_id FOREIGN KEY (booking_id)
        REFERENCES Bookings(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Helpful indexes
CREATE INDEX IX_Tour_Schedules_tour_date ON Tour_Schedules(tour_id, departure_date);
CREATE INDEX IX_Bookings_user_created ON Bookings(user_id, created_at DESC);
CREATE INDEX IX_Payments_booking ON Payments(booking_id);
CREATE INDEX IX_Booking_Items_booking ON Booking_Items(booking_id);

GO

-- Stored Procedure: sp_CreateTourBooking
IF OBJECT_ID('dbo.sp_CreateTourBooking', 'P') IS NOT NULL
    DROP PROCEDURE dbo.sp_CreateTourBooking;
GO

CREATE PROCEDURE dbo.sp_CreateTourBooking
    @user_id INT,
    @tour_id INT,
    @schedule_id INT,
    @package_id INT = NULL,
    @guest_count INT,
    @contact_name NVARCHAR(150) = NULL,
    @contact_email VARCHAR(100) = NULL,
    @contact_phone VARCHAR(20) = NULL,
    @subtotal DECIMAL(12,2),
    @discount_total DECIMAL(12,2) = 0,
    @tax_total DECIMAL(12,2) = 0,
    @total_price DECIMAL(12,2),
    @currency VARCHAR(10) = 'VND',
    @booking_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @now DATETIME = GETDATE();
    DECLARE @available INT, @booked INT;

    BEGIN TRY
        BEGIN TRAN;

        -- Lock schedule row to ensure slot consistency
        SELECT @available = ts.available_slots,
               @booked = ts.booked_slots
        FROM Tour_Schedules ts WITH (UPDLOCK, ROWLOCK)
        WHERE ts.schedule_id = @schedule_id;

        IF @available IS NULL
        BEGIN
            RAISERROR('Invalid schedule_id', 16, 1);
        END

        IF (@available - @booked) < @guest_count
        BEGIN
            RAISERROR('Not enough available slots', 16, 1);
        END

        -- Insert booking
        INSERT INTO Bookings (
            user_id, tour_id, schedule_id, package_id,
            guest_count, subtotal, discount_total, tax_total, total_price,
            currency, booking_date, booking_source, created_by, status,
            created_at, updated_at,
            contact_name, contact_email, contact_phone
        ) VALUES (
            @user_id, @tour_id, @schedule_id, @package_id,
            @guest_count, @subtotal, @discount_total, @tax_total, @total_price,
            @currency, @now, 'ONLINE', @user_id, 'pending',
            @now, @now,
            @contact_name, @contact_email, @contact_phone
        );

        SET @booking_id = SCOPE_IDENTITY();

        -- Snapshot item: TOUR
        DECLARE @tour_title NVARCHAR(255) = (SELECT TOP 1 tour_name FROM Tours WHERE tour_id = @tour_id);
        INSERT INTO Booking_Items (
            booking_id, item_type, item_id, title_snapshot, meta_json, start_time, end_time,
            quantity, unit_price, total_price
        )
        SELECT
            @booking_id, 'TOUR', @tour_id, @tour_title,
            NULL,
            (SELECT departure_date FROM Tour_Schedules WHERE schedule_id = @schedule_id),
            (SELECT return_date FROM Tour_Schedules WHERE schedule_id = @schedule_id),
            @guest_count,
            CAST(ROUND((@subtotal + 0.0) / NULLIF(@guest_count,0), 0) AS DECIMAL(12,2)),
            @subtotal;

        -- Snapshot item: PACKAGE (optional)
        IF @package_id IS NOT NULL
        BEGIN
            DECLARE @pkg_title NVARCHAR(255) = (SELECT TOP 1 package_name FROM Tour_Packages WHERE package_id = @package_id);
            DECLARE @pkg_price DECIMAL(12,2) = (SELECT TOP 1 price FROM Tour_Packages WHERE package_id = @package_id);

            INSERT INTO Booking_Items (
                booking_id, item_type, item_id, title_snapshot, meta_json, start_time, end_time,
                quantity, unit_price, total_price
            ) VALUES (
                @booking_id, 'PACKAGE', @package_id, @pkg_title, NULL, NULL, NULL,
                @guest_count, @pkg_price, @pkg_price * @guest_count
            );
        END

        -- Update schedule slots
        UPDATE Tour_Schedules
        SET booked_slots = booked_slots + @guest_count,
            updated_at = @now,
            status = CASE WHEN (booked_slots + @guest_count) >= available_slots THEN 'FULL' ELSE status END
        WHERE schedule_id = @schedule_id;

        COMMIT TRAN;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRAN;
        DECLARE @msg NVARCHAR(4000) = ERROR_MESSAGE();
        RAISERROR(@msg, 16, 1);
    END CATCH
END
GO