-- Script kiểm tra tên bảng trong database
-- Chạy script này để xem tên bảng chính xác

USE booking_travel;
GO

PRINT '========================================';
PRINT 'KIỂM TRA CÁC BẢNG TRONG DATABASE';
PRINT '========================================';
PRINT '';

-- 1. Liệt kê TẤT CẢ các bảng
PRINT '--- TẤT CẢ CÁC BẢNG ---';
SELECT 
    SCHEMA_NAME(schema_id) AS SchemaName,
    name AS TableName,
    create_date AS CreatedDate
FROM sys.tables
ORDER BY name;
GO

PRINT '';
PRINT '--- TÌM BẢNG CÓ CHỨA "MEAL" ---';
SELECT 
    SCHEMA_NAME(schema_id) AS SchemaName,
    name AS TableName
FROM sys.tables
WHERE name LIKE '%Meal%' OR name LIKE '%meal%'
ORDER BY name;
GO

PRINT '';
PRINT '--- TÌM BẢNG CÓ CHỨA "WELLNESS" ---';
SELECT 
    SCHEMA_NAME(schema_id) AS SchemaName,
    name AS TableName
FROM sys.tables
WHERE name LIKE '%Wellness%' OR name LIKE '%wellness%'
ORDER BY name;
GO

PRINT '';
PRINT '--- TÌM BẢNG CÓ CHỨA "SERVICE" ---';
SELECT 
    SCHEMA_NAME(schema_id) AS SchemaName,
    name AS TableName
FROM sys.tables
WHERE name LIKE '%Service%' OR name LIKE '%service%'
ORDER BY name;
GO

-- 2. Đếm số records trong các bảng (nếu tồn tại)
PRINT '';
PRINT '========================================';
PRINT 'KIỂM TRA DỮ LIỆU';
PRINT '========================================';

-- Kiểm tra MealService
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'MealService')
BEGIN
    DECLARE @MealCount INT;
    SELECT @MealCount = COUNT(*) FROM MealService;
    PRINT '✅ Bảng MealService: ' + CAST(@MealCount AS VARCHAR) + ' records';
    
    -- Hiển thị sample data
    SELECT TOP 3 * FROM MealService;
END
ELSE IF EXISTS (SELECT * FROM sys.tables WHERE name = 'Meal_Service')
BEGIN
    DECLARE @MealCount2 INT;
    SELECT @MealCount2 = COUNT(*) FROM Meal_Service;
    PRINT '✅ Bảng Meal_Service: ' + CAST(@MealCount2 AS VARCHAR) + ' records';
    
    SELECT TOP 3 * FROM Meal_Service;
END
ELSE IF EXISTS (SELECT * FROM sys.tables WHERE name = 'meal_service')
BEGIN
    DECLARE @MealCount3 INT;
    SELECT @MealCount3 = COUNT(*) FROM meal_service;
    PRINT '✅ Bảng meal_service: ' + CAST(@MealCount3 AS VARCHAR) + ' records';
    
    SELECT TOP 3 * FROM meal_service;
END
ELSE
BEGIN
    PRINT '❌ KHÔNG TÌM THẤY bảng MealService (hoặc tương tự)';
END

-- Kiểm tra WellnessService
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'WellnessService')
BEGIN
    DECLARE @WellnessCount INT;
    SELECT @WellnessCount = COUNT(*) FROM WellnessService;
    PRINT '✅ Bảng WellnessService: ' + CAST(@WellnessCount AS VARCHAR) + ' records';
    
    SELECT TOP 3 * FROM WellnessService;
END
ELSE IF EXISTS (SELECT * FROM sys.tables WHERE name = 'Wellness_Service')
BEGIN
    DECLARE @WellnessCount2 INT;
    SELECT @WellnessCount2 = COUNT(*) FROM Wellness_Service;
    PRINT '✅ Bảng Wellness_Service: ' + CAST(@WellnessCount2 AS VARCHAR) + ' records';
    
    SELECT TOP 3 * FROM Wellness_Service;
END
ELSE IF EXISTS (SELECT * FROM sys.tables WHERE name = 'wellness_service')
BEGIN
    DECLARE @WellnessCount3 INT;
    SELECT @WellnessCount3 = COUNT(*) FROM wellness_service;
    PRINT '✅ Bảng wellness_service: ' + CAST(@WellnessCount3 AS VARCHAR) + ' records';
    
    SELECT TOP 3 * FROM wellness_service;
END
ELSE
BEGIN
    PRINT '❌ KHÔNG TÌM THẤY bảng WellnessService (hoặc tương tự)';
END

PRINT '';
PRINT '========================================';
PRINT 'HOÀN THÀNH KIỂM TRA!';
PRINT '========================================';
GO



