-- ============================================
-- SETUP DATABASE - SamSon Travel Booking
-- ============================================
-- File này insert dữ liệu mẫu cho toàn bộ hệ thống
-- Chạy 1 lần duy nhất sau khi tạo database
-- ============================================

USE booking_travel;
GO

-- ============================================
-- ROLES (5 vai trò)
-- ============================================
IF NOT EXISTS (SELECT 1 FROM Roles WHERE role_name = 'Administrator')
BEGIN
    INSERT INTO Roles (role_name) VALUES 
        ('Administrator'), ('Service Manager'), ('Hotel Manager'), 
        ('Customer'), ('Front Office');
END
GO

-- ============================================
-- SERVICE CATEGORIES (4 loại dịch vụ)
-- ============================================
IF NOT EXISTS (SELECT 1 FROM ServiceCategories WHERE category_code = 'HOTEL')
BEGIN
    INSERT INTO ServiceCategories (category_code, category_name) VALUES 
        ('HOTEL', N'Dịch vụ khách sạn'),
        ('TRANSPORT', N'Dịch vụ vận chuyển'),
        ('MEAL', N'Dịch vụ ăn uống'),
        ('WELLNESS', N'Dịch vụ spa & wellness');
END
GO

-- ============================================
-- HOTELS (2 khách sạn)
-- ============================================
DELETE FROM Hotels WHERE name LIKE N'%Sầm Sơn%' OR name LIKE N'%FLC%';

INSERT INTO Hotels (name, address, description, manager_id) VALUES 
    (N'Khách sạn Sầm Sơn Beach Resort', N'123 Hồ Xuân Hương, Sầm Sơn', N'Resort 5 sao view biển', NULL),
    (N'Khách sạn FLC Luxury Sầm Sơn', N'456 Trần Phú, Sầm Sơn', N'Resort cao cấp đầy đủ tiện nghi', NULL);
GO

-- ============================================
-- ROOMS (6 loại phòng: 2 hotels x 3 types)
-- ============================================
DECLARE @h1 INT = (SELECT TOP 1 id FROM Hotels WHERE name LIKE N'%Beach Resort%' ORDER BY id DESC);
DECLARE @h2 INT = (SELECT TOP 1 id FROM Hotels WHERE name LIKE N'%FLC%' ORDER BY id DESC);

DELETE FROM Rooms WHERE hotel_id IN (@h1, @h2);

INSERT INTO Rooms (hotel_id, room_type, price, total_rooms, available_rooms) VALUES 
    -- Beach Resort
    (@h1, 'single', 500000, 20, 20),
    (@h1, 'double', 800000, 15, 15),
    (@h1, 'dormitory', 200000, 10, 10),
    -- FLC Luxury
    (@h2, 'single', 700000, 15, 15),
    (@h2, 'double', 1200000, 20, 20),
    (@h2, 'dormitory', 300000, 5, 5);
GO

-- ============================================
-- MEAL SERVICES (6 món ăn)
-- ============================================
DECLARE @h1 INT = (SELECT TOP 1 id FROM Hotels WHERE name LIKE N'%Beach Resort%' ORDER BY id DESC);
DECLARE @mc INT = (SELECT category_id FROM ServiceCategories WHERE category_code = 'MEAL');

DELETE FROM Meal_Services WHERE hotel_id = @h1;

INSERT INTO Meal_Services (hotel_id, category_id, meal_type, meal_date, description, price, status) VALUES 
    (@h1, @mc, 'BREAKFAST', '2024-12-01', N'Buffet sáng Á - Âu', 150000, 'ACTIVE'),
    (@h1, @mc, 'BREAKFAST', '2024-12-02', N'Buffet sáng cao cấp', 200000, 'ACTIVE'),
    (@h1, @mc, 'LUNCH', '2024-12-01', N'Set menu hải sản', 350000, 'ACTIVE'),
    (@h1, @mc, 'LUNCH', '2024-12-02', N'Buffet trưa 50+ món', 400000, 'ACTIVE'),
    (@h1, @mc, 'DINNER', '2024-12-01', N'BBQ ven biển', 500000, 'ACTIVE'),
    (@h1, @mc, 'DINNER', '2024-12-02', N'Buffet tối + Live music', 600000, 'ACTIVE');
GO

-- ============================================
-- WELLNESS SERVICES (6 dịch vụ spa)
-- ============================================
DECLARE @h1 INT = (SELECT TOP 1 id FROM Hotels WHERE name LIKE N'%Beach Resort%' ORDER BY id DESC);
DECLARE @wc INT = (SELECT category_id FROM ServiceCategories WHERE category_code = 'WELLNESS');

DELETE FROM Wellness_Services WHERE hotel_id = @h1;

INSERT INTO Wellness_Services (hotel_id, category_id, service_name, description, base_price, duration_minutes, operating_hours, capacity, status) VALUES 
    (@h1, @wc, N'Massage body thư giãn', N'Massage toàn thân tinh dầu', 500000, 60, N'8:00-22:00', 5, 'ACTIVE'),
    (@h1, @wc, N'Massage đá nóng', N'Liệu pháp đá nóng', 700000, 90, N'9:00-21:00', 3, 'ACTIVE'),
    (@h1, @wc, N'Chăm sóc da mặt cao cấp', N'Facial chuyên sâu', 400000, 60, N'9:00-20:00', 4, 'ACTIVE'),
    (@h1, @wc, N'Tắm bùn khoáng Sầm Sơn', N'Bùn khoáng tự nhiên', 300000, 45, N'10:00-19:00', 8, 'ACTIVE'),
    (@h1, @wc, N'Sauna & Steam', N'Xông hơi + hồ massage', 200000, 60, N'6:00-22:00', 10, 'ACTIVE'),
    (@h1, @wc, N'Yoga ven biển', N'Yoga view biển', 150000, 60, N'6:00-8:00', 15, 'ACTIVE');
GO

-- ============================================
-- TRANSPORT SERVICES (5 xe)
-- ============================================
DECLARE @tc INT = (SELECT category_id FROM ServiceCategories WHERE category_code = 'TRANSPORT');

DELETE FROM TransportServices WHERE category_id = @tc;

INSERT INTO TransportServices (category_id, vehicle_type, vehicle_name, description, pickup_location, departure_time, price, capacity, current_passengers) VALUES 
    (@tc, 'BUS', N'Xe khách 45 chỗ', N'Giường nằm, wifi', N'Bến xe Mỹ Đình, HN', '2024-12-01 07:00', 180000, 45, 0),
    (@tc, 'BUS', N'Xe Limousine 16 chỗ', N'VIP ghế massage', N'Sân bay Nội Bài', '2024-12-01 08:30', 350000, 16, 0),
    (@tc, 'CAR', N'Xe ô tô 4 chỗ', N'Riêng tư, lái xe pro', N'HN (theo yêu cầu)', '2024-12-01 06:00', 1200000, 4, 0),
    (@tc, 'CAR', N'Xe ô tô 7 chỗ', N'Xe gia đình', N'HN (theo yêu cầu)', '2024-12-01 06:00', 1500000, 7, 0),
    (@tc, 'MINIVAN', N'Xe 16 chỗ', N'Phù hợp đoàn nhỏ', N'Điểm hẹn HN', '2024-12-01 07:30', 2000000, 16, 0);
GO

-- ============================================
-- USERS (5 tài khoản test)
-- ============================================
DECLARE @ar INT = (SELECT role_id FROM Roles WHERE role_name = 'Administrator');
DECLARE @fr INT = (SELECT role_id FROM Roles WHERE role_name = 'Front Office');
DECLARE @cr INT = (SELECT role_id FROM Roles WHERE role_name = 'Customer');

DELETE FROM Users WHERE email IN ('khanh@samsontravel.com', 'lan@samsontravel.com', 'customer1@gmail.com', 'customer2@gmail.com');

IF NOT EXISTS (SELECT 1 FROM Users WHERE email = 'admin@samsontravel.com')
    INSERT INTO Users (name, password, email, phone, gender, address, role_id, status) VALUES 
        ('Admin SamSon', 'admin123', 'admin@samsontravel.com', '0901234567', 'male', N'Hà Nội', @ar, 'active');

INSERT INTO Users (name, password, email, phone, gender, address, role_id, status) VALUES 
    (N'Nguyễn Văn Khanh', 'khanh123', 'khanh@samsontravel.com', '0912345678', 'male', N'Thanh Hóa', @fr, 'active'),
    (N'Trần Thị Lan', 'lan123', 'lan@samsontravel.com', '0923456789', 'female', N'Thanh Hóa', @fr, 'active'),
    (N'Nguyễn Văn A', 'customer123', 'customer1@gmail.com', '0987654321', 'male', N'Hà Nội', @cr, 'active'),
    (N'Lê Thị B', 'customer123', 'customer2@gmail.com', '0976543210', 'female', N'TP.HCM', @cr, 'active');
GO

-- ============================================
-- OFFLINE CUSTOMERS (3 khách mẫu)
-- ============================================
DELETE FROM Offline_Customers WHERE phone IN ('0909123456', '0909234567', '0909345678');

INSERT INTO Offline_Customers (full_name, phone, email, id_card_number, nationality, gender, date_of_birth, address) VALUES 
    (N'Nguyễn Văn Tèo', '0909123456', 'teo@gmail.com', '001234567890', N'Việt Nam', 'male', '1990-05-15', N'Hà Nội'),
    (N'Trần Thị Mai', '0909234567', 'mai@gmail.com', '001234567891', N'Việt Nam', 'female', '1985-08-20', N'TP.HCM'),
    (N'John Smith', '0909345678', 'john@gmail.com', 'PASS123456', N'USA', 'male', '1988-03-10', N'New York, USA');
GO

-- ============================================
-- DONE! HIỂN THỊ KẾT QUẢ
-- ============================================
PRINT '✅ Setup hoàn tất!';
PRINT '';
PRINT 'Dữ liệu đã insert:';

SELECT 'Roles' AS [Bảng], COUNT(*) AS [Số dòng] FROM Roles
UNION ALL SELECT 'ServiceCategories', COUNT(*) FROM ServiceCategories
UNION ALL SELECT 'Hotels', COUNT(*) FROM Hotels
UNION ALL SELECT 'Rooms', COUNT(*) FROM Rooms
UNION ALL SELECT 'Meal_Services', COUNT(*) FROM Meal_Services
UNION ALL SELECT 'Wellness_Services', COUNT(*) FROM Wellness_Services
UNION ALL SELECT 'TransportServices', COUNT(*) FROM TransportServices
UNION ALL SELECT 'Users', COUNT(*) FROM Users
UNION ALL SELECT 'Offline_Customers', COUNT(*) FROM Offline_Customers;

PRINT '';
PRINT 'Test tại: http://localhost:8080/SamSonBookingService/room-list';
GO

