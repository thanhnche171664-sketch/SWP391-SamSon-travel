DROP DATABASE IF EXISTS booking_travel;
GO
CREATE DATABASE booking_travel;
GO
USE booking_travel;
GO

-- 1️⃣ BẢNG VAI TRÒ NGƯỜI DÙNG
CREATE TABLE Roles (
    role_id INT IDENTITY(1,1) PRIMARY KEY,
    role_name NVARCHAR(50) UNIQUE NOT NULL
);

-- 2️⃣ BẢNG NGƯỜI DÙNG HỆ THỐNG
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50),
    password NVARCHAR(255),
    email NVARCHAR(100) UNIQUE,
    phone NVARCHAR(20),
    gender NVARCHAR(10) CHECK (gender IN ('male','female','other')),
    address NVARCHAR(255),
    avatar_url NVARCHAR(255),
    role_id INT,
    status NVARCHAR(20) DEFAULT 'active' CHECK (status IN ('active','pending','inactive','suspended')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);

-- 2.1️⃣ BẢNG TOKEN XÁC NHẬN/RESET MẬT KHẨU
CREATE TABLE reset_tokens (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    token NVARCHAR(255) NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    used BIT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- 3️⃣ BẢNG DANH MỤC DỊCH VỤ
CREATE TABLE ServiceCategories (
    category_id INT IDENTITY(1,1) PRIMARY KEY,
    category_code NVARCHAR(50) UNIQUE NOT NULL,   -- VD: HOTEL, TRANSPORT, MEAL, WELLNESS
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
    vehicle_type NVARCHAR(20) NOT NULL CHECK (vehicle_type IN ('CAR','MINIVAN','BUS','LIMOUSINE','SELF')),
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
    status NVARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
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
    status NVARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
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
    room_type NVARCHAR(20) NULL CHECK (room_type IN ('single', 'double', 'dormitory')),
    number_of_rooms INT,
    transport_id INT NULL,
    transport_fee DECIMAL(10,2) NOT NULL DEFAULT 0,
    total_price DECIMAL(10,2) NOT NULL,
    booking_date DATETIME NOT NULL,
    booking_source NVARCHAR(20) DEFAULT 'ONLINE' CHECK (booking_source IN ('ONLINE','OFFLINE')),
    created_by INT NULL,
    status NVARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending','confirmed','canceled')),
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
    transaction_id NVARCHAR(100),
    currency NVARCHAR(10) DEFAULT 'VND',
    payment_method NVARCHAR(20) NOT NULL CHECK (payment_method IN ('CREDIT_CARD','BANK_TRANSFER','CASH')),
    payment_date DATETIME DEFAULT GETDATE(),
    amount DECIMAL(10,2) NOT NULL,
    status NVARCHAR(20) DEFAULT 'PAID' CHECK (status IN ('PAID','FAILED','REFUNDED')),
    FOREIGN KEY (booking_id) REFERENCES Bookings(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1️⃣2️⃣ BẢNG GIẢM GIÁ
CREATE TABLE Discounts (
    discount_id INT IDENTITY(1,1) PRIMARY KEY,
    category_id INT NOT NULL,
    discount_type NVARCHAR(20) NOT NULL CHECK (discount_type IN ('PERCENT','FIXED')),
    value DECIMAL(10,2) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    status NVARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES ServiceCategories(category_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1️⃣3️⃣ BẢNG RESET TOKEN (already created above as reset_tokens, skipping duplicate)
-- Note: Reset_Tokens table is already created at line 32 as reset_tokens

-- 1️⃣4️⃣ BẢNG KHÁCH HÀNG ĐẶT OFFLINE
CREATE TABLE Offline_Customers (
    offline_customer_id INT IDENTITY(1,1) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    phone NVARCHAR(20),
    email NVARCHAR(100),
    id_card_number NVARCHAR(20),
    nationality NVARCHAR(50),
    gender NVARCHAR(10) CHECK (gender IN ('male','female','other')),
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
    media_type NVARCHAR(20) NOT NULL CHECK (media_type IN ('IMAGE','VIDEO')),
    file_url NVARCHAR(255) NOT NULL,
    uploaded_by INT NULL,
    uploaded_at DATETIME DEFAULT GETDATE(),
    status NVARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
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

-- Note: Sample hotel and rooms will be inserted in comprehensive data section below

-- Insert sample rooms (moved to comprehensive section after hotels are created)
-- Insert TransportServices
INSERT INTO TransportServices 
(category_id, vehicle_type, vehicle_name, description, pickup_location, departure_time, price, capacity) 
VALUES
(2, 'CAR', N'Xe ô tô 4 chỗ VinFast Lux A2.0', N'Xe riêng tiện nghi, lái xe thân thiện', N'Hà Nội - Sầm Sơn', '2025-01-01 08:00:00', 800000.00, 4),
(2, 'CAR', N'Toyota Vios 4 chỗ', N'Xe phổ thông tiết kiệm nhiên liệu', N'Hà Nội - Sầm Sơn', '2025-01-02 09:00:00', 900000.00, 4),
(2, 'CAR', N'Mazda 3', N'Xe riêng sang trọng, có điều hòa', N'Hà Nội - Sầm Sơn', '2025-01-03 07:30:00', 950000.00, 4),
(2, 'CAR', N'Honda City', N'Lái xe chuyên nghiệp, phục vụ tận nơi', N'Hà Nội - Sầm Sơn', '2025-01-04 08:15:00', 850000.00, 4),
(2, 'MINIVAN', N'Toyota Innova 7 chỗ', N'Phù hợp gia đình hoặc nhóm nhỏ', N'Hà Nội - Sầm Sơn', '2025-01-05 06:00:00', 1800000.00, 7),
(2, 'MINIVAN', N'Mitsubishi Xpander', N'Không gian rộng rãi, tiện nghi', N'Hà Nội - Sầm Sơn', '2025-01-06 09:00:00', 1200000.00, 7),
(2, 'MINIVAN', N'Kia Carnival', N'Xe sang trọng, có TV và wifi', N'Hà Nội - Sầm Sơn', '2025-01-07 08:30:00', 1500000.00, 7),
(2, 'MINIVAN', N'Toyota Fortuner', N'Xe mạnh mẽ, phù hợp địa hình đồi núi', N'Hà Nội - Sầm Sơn', '2025-01-08 05:45:00', 2000000.00, 7),
(2, 'BUS', N'Xe khách 29 chỗ', N'Phục vụ du lịch theo đoàn, có điều hòa', N'Hà Nội - Sầm Sơn', '2025-01-09 07:00:00', 250000.00, 29),
(2, 'BUS', N'Xe giường nằm 45 chỗ', N'Giường nằm cao cấp, chăn gối sạch sẽ', N'Hà Nội - Sầm Sơn', '2025-01-10 18:00:00', 800000.00, 45),
(2, 'BUS', N'Xe khách 35 chỗ', N'Xe mới, có nhà vệ sinh mini', N'Hà Nội - Sầm Sơn', '2025-01-11 19:00:00', 600000.00, 35),
(2, 'BUS', N'Xe giường nằm cao cấp', N'Trang bị TV cá nhân và sạc USB', N'Hà Nội - Sầm Sơn', '2025-01-12 20:00:00', 450000.00, 40),
(2, 'LIMOUSINE', N'Xe Limousine Dcar 9 chỗ', N'Ghế massage, wifi, nước uống miễn phí', N'Hà Nội - Sầm Sơn', '2025-01-13 06:30:00', 900000.00, 9),
(2, 'LIMOUSINE', N'Limousine 16 chỗ VIP', N'Nội thất da cao cấp, màn hình LED', N'Hà Nội - Sầm Sơn', '2025-01-14 08:00:00', 1000000.00, 16),
(2, 'LIMOUSINE', N'Limousine Executive', N'Phục vụ doanh nhân, yên tĩnh và sang trọng', N'Hà Nội - Sầm Sơn', '2025-01-15 07:30:00', 850000.00, 9),
(2, 'LIMOUSINE', N'Limousine 11 chỗ', N'Đưa đón tận nơi, có wifi', N'Hà Nội - Sầm Sơn', '2025-01-16 09:00:00', 950000.00, 11),
(2, 'SELF', N'Toyota Corolla Altis', N'Xe tự lái, tiết kiệm nhiên liệu', N'Hà Nội - Sầm Sơn', '2025-01-17 08:00:00', 700000.00, 4),
(2, 'SELF', N'VinFast VF e34', N'Xe điện tự lái, thân thiện môi trường', N'Hà Nội - Sầm Sơn', '2025-01-18 10:00:00', 900000.00, 4),
(2, 'SELF', N'Kia Morning', N'Xe nhỏ gọn, dễ di chuyển trong thành phố', N'Hà Nội - Sầm Sơn', '2025-01-19 07:45:00', 500000.00, 4),
(2, 'SELF', N'Mazda CX5', N'Xe SUV tự lái cao cấp, phù hợp du lịch', N'Hà Nội - Sầm Sơn', '2025-01-20 08:15:00', 1200000.00, 5);

-- Note: Meal_Services and Wellness_Services will be inserted after Hotels are created in comprehensive section




-- ===========================================
-- ===========================================
-- ENHANCE EXISTING TABLES
-- ===========================================

-- Add icons and display order to ServiceCategories
ALTER TABLE ServiceCategories ADD 
    icon_class NVARCHAR(100),
    display_order INT DEFAULT 0,
    description NVARCHAR(500);
GO

-- Add more fields to Hotels
ALTER TABLE Hotels ADD 
    rating DECIMAL(3,2) DEFAULT 0,
    featured BIT DEFAULT 0,
    amenities NVARCHAR(MAX),
    check_in_time TIME DEFAULT '14:00:00',
    check_out_time TIME DEFAULT '12:00:00';

-- Extend Bookings with date-based fields and booking code
ALTER TABLE Bookings ADD 
    booking_code NVARCHAR(30) NULL,
    check_in_date DATE NULL,
    check_out_date DATE NULL,
    num_adults INT NOT NULL DEFAULT 1,
    num_children INT NOT NULL DEFAULT 0,
    notes NVARCHAR(500) NULL;

-- Set NOT NULL and add uniqueness after backfilling (fresh schema, so direct constraints)
ALTER TABLE Bookings ALTER COLUMN booking_code NVARCHAR(30) NOT NULL;
ALTER TABLE Bookings ADD CONSTRAINT UQ_Bookings_BookingCode UNIQUE (booking_code);
ALTER TABLE Bookings ALTER COLUMN check_in_date DATE NOT NULL;
ALTER TABLE Bookings ALTER COLUMN check_out_date DATE NOT NULL;

-- Helpful index for availability checks
CREATE INDEX IX_Bookings_Availability
ON Bookings (hotel_id, room_type, check_in_date, check_out_date, status);

-- Add description to Payments and extend status values
ALTER TABLE Payments ADD description NVARCHAR(255) NULL;
GO

-- Drop existing status CHECK constraint on Payments (auto-named) and recreate with explicit name
DECLARE @chk NVARCHAR(128);
SELECT @chk = cc.name
FROM sys.check_constraints cc
JOIN sys.columns c ON cc.parent_object_id = c.object_id AND c.column_id = cc.parent_column_id
WHERE cc.parent_object_id = OBJECT_ID('Payments')
  AND c.name = 'status';
IF @chk IS NOT NULL 
BEGIN
    DECLARE @sql NVARCHAR(MAX) = 'ALTER TABLE Payments DROP CONSTRAINT ' + QUOTENAME(@chk);
    EXEC sp_executesql @sql;
END
GO

ALTER TABLE Payments WITH NOCHECK ADD CONSTRAINT CK_Payments_Status
CHECK (status IN ('PENDING','PAID','FAILED','REFUNDED','EXPIRED'));
ALTER TABLE Payments CHECK CONSTRAINT CK_Payments_Status;
GO

-- Create Booking_Addons for itemized meal/wellness selections
IF OBJECT_ID('Booking_Addons','U') IS NULL
BEGIN
    CREATE TABLE Booking_Addons (
        addon_id INT IDENTITY(1,1) PRIMARY KEY,
        booking_id INT NOT NULL,
        addon_type NVARCHAR(20) NOT NULL CHECK (addon_type IN ('MEAL','WELLNESS')),
        reference_id INT NOT NULL,
        name NVARCHAR(150) NOT NULL,
        unit_price DECIMAL(10,2) NOT NULL,
        quantity INT NOT NULL DEFAULT 1,
        total_price AS (quantity * unit_price) PERSISTED,
        created_at DATETIME DEFAULT GETDATE(),
        CONSTRAINT FK_Booking_Addons_Booking FOREIGN KEY (booking_id) REFERENCES Bookings(id)
            ON DELETE CASCADE ON UPDATE CASCADE
    );
    CREATE INDEX IX_Booking_Addons_BookingId ON Booking_Addons(booking_id);
END

-- ===========================================
-- INSERT COMPREHENSIVE SAMPLE DATA
-- ===========================================

-- Note: Roles and ServiceCategories are already inserted above
-- This section only updates/enhances existing data

-- Update ServiceCategories with icons and descriptions
GO
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

-- Insert rooms for hotel_id = 1 (first hotel)
INSERT INTO Rooms (hotel_id, room_type, price, total_rooms, available_rooms) VALUES 
    (1, 'single', 500000.00, 20, 20),
    (1, 'double', 800000.00, 15, 15),
    (1, 'dormitory', 200000.00, 10, 10);

-- Insert meal services for hotel_id = 1
INSERT INTO Meal_Services (hotel_id, category_id, meal_type, meal_date, description, price) VALUES 
    (1, 3, 'BREAKFAST', '2024-01-01', N'Buffet sáng với các món ăn Việt Nam và quốc tế', 150000.00),
    (1, 3, 'LUNCH', '2024-01-01', N'Set menu trưa với hải sản tươi sống', 300000.00),
    (1, 3, 'DINNER', '2024-01-01', N'Tiệc BBQ ngoài trời', 400000.00);

-- Insert wellness services for hotel_id = 1
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

-- Insert sample admin user for testing (password: admin123) - must be before Tour_Media
INSERT INTO Users (name, password, email, phone, gender, address, role_id, status) VALUES 
    ('Admin User', 'admin123', 'admin@samsontravel.com', '0123456789', 'male', N'Hà Nội', 1, 'active');
GO

-- Insert comprehensive tour media (now user_id=1 exists)
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