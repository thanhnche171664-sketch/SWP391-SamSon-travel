-- Script tạo bảng MealService và WellnessService
-- Database: booking_travel

USE booking_travel;
GO

-- ============================================
-- Tạo bảng ServiceCategory (nếu chưa có)
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'ServiceCategory')
BEGIN
    CREATE TABLE ServiceCategory (
        category_id INT PRIMARY KEY IDENTITY(1,1),
        category_code NVARCHAR(50) NOT NULL UNIQUE,
        category_name NVARCHAR(100) NOT NULL,
        description NVARCHAR(500),
        status NVARCHAR(20) DEFAULT 'active',
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE()
    );
    
    PRINT '✅ Đã tạo bảng ServiceCategory';
END
ELSE
BEGIN
    PRINT '⚠️ Bảng ServiceCategory đã tồn tại';
END
GO

-- Insert service categories
IF NOT EXISTS (SELECT * FROM ServiceCategory WHERE category_code = 'MEAL')
BEGIN
    INSERT INTO ServiceCategory (category_code, category_name, description, status)
    VALUES ('MEAL', N'Dịch vụ ăn uống', N'Các dịch vụ ăn uống tại khách sạn', 'active');
    PRINT '✅ Đã thêm category MEAL';
END

IF NOT EXISTS (SELECT * FROM ServiceCategory WHERE category_code = 'WELLNESS')
BEGIN
    INSERT INTO ServiceCategory (category_code, category_name, description, status)
    VALUES ('WELLNESS', N'Spa & Wellness', N'Các dịch vụ chăm sóc sức khỏe và làm đẹp', 'active');
    PRINT '✅ Đã thêm category WELLNESS';
END
GO

-- ============================================
-- Tạo bảng Hotel (nếu chưa có)
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Hotel')
BEGIN
    CREATE TABLE Hotel (
        id INT PRIMARY KEY IDENTITY(1,1),
        name NVARCHAR(200) NOT NULL,
        address NVARCHAR(500),
        description NVARCHAR(1000),
        manager_id INT,
        status NVARCHAR(20) DEFAULT 'active',
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE()
    );
    
    PRINT '✅ Đã tạo bảng Hotel';
END
ELSE
BEGIN
    PRINT '⚠️ Bảng Hotel đã tồn tại';
END
GO

-- Insert default hotel
IF NOT EXISTS (SELECT * FROM Hotel WHERE id = 1)
BEGIN
    SET IDENTITY_INSERT Hotel ON;
    INSERT INTO Hotel (id, name, address, description, status)
    VALUES (1, N'SamSon Beach Resort', N'Bãi biển Sầm Sơn, Thanh Hóa', N'Resort sang trọng bên bờ biển', 'active');
    SET IDENTITY_INSERT Hotel OFF;
    PRINT '✅ Đã thêm hotel mặc định';
END
GO

-- ============================================
-- Tạo bảng MealService
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'MealService')
BEGIN
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
        updated_at DATETIME DEFAULT GETDATE(),
        CONSTRAINT FK_MealService_Hotel FOREIGN KEY (hotel_id) REFERENCES Hotel(id),
        CONSTRAINT FK_MealService_Category FOREIGN KEY (category_id) REFERENCES ServiceCategory(category_id)
    );
    
    PRINT '✅ Đã tạo bảng MealService';
END
ELSE
BEGIN
    PRINT '⚠️ Bảng MealService đã tồn tại';
END
GO

-- ============================================
-- Tạo bảng WellnessService
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'WellnessService')
BEGIN
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
        updated_at DATETIME DEFAULT GETDATE(),
        CONSTRAINT FK_WellnessService_Hotel FOREIGN KEY (hotel_id) REFERENCES Hotel(id),
        CONSTRAINT FK_WellnessService_Category FOREIGN KEY (category_id) REFERENCES ServiceCategory(category_id)
    );
    
    PRINT '✅ Đã tạo bảng WellnessService';
END
ELSE
BEGIN
    PRINT '⚠️ Bảng WellnessService đã tồn tại';
END
GO

-- ============================================
-- Verify tables created
-- ============================================
PRINT '';
PRINT '========================================';
PRINT 'KẾT QUẢ TẠO BẢNG:';
PRINT '========================================';

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'ServiceCategory')
    PRINT '✅ ServiceCategory - OK';
ELSE
    PRINT '❌ ServiceCategory - MISSING';

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'Hotel')
    PRINT '✅ Hotel - OK';
ELSE
    PRINT '❌ Hotel - MISSING';

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'MealService')
    PRINT '✅ MealService - OK';
ELSE
    PRINT '❌ MealService - MISSING';

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'WellnessService')
    PRINT '✅ WellnessService - OK';
ELSE
    PRINT '❌ WellnessService - MISSING';

PRINT '';
PRINT '========================================';
PRINT 'SẴN SÀNG INSERT DỮ LIỆU MẪU!';
PRINT 'Chạy file: insert_sample_services.sql';
PRINT '========================================';
GO



