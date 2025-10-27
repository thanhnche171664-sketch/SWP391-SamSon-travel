-- Script để insert dữ liệu mẫu cho MealService và WellnessService
-- Chạy script này để test hiển thị dữ liệu trên Service-list.jsp
-- LƯU Ý: Phải chạy file create_tables.sql TRƯỚC để tạo bảng!

USE booking_travel;
GO

PRINT '========================================';
PRINT 'ĐANG INSERT DỮ LIỆU MẪU...';
PRINT '========================================';

-- Lấy category_id từ bảng ServiceCategory
DECLARE @MEAL_CATEGORY_ID INT;
DECLARE @WELLNESS_CATEGORY_ID INT;

SELECT @MEAL_CATEGORY_ID = category_id FROM ServiceCategory WHERE category_code = 'MEAL';
SELECT @WELLNESS_CATEGORY_ID = category_id FROM ServiceCategory WHERE category_code = 'WELLNESS';

IF @MEAL_CATEGORY_ID IS NULL
BEGIN
    PRINT '❌ ERROR: Không tìm thấy MEAL category. Vui lòng chạy create_tables.sql trước!';
    RETURN;
END

IF @WELLNESS_CATEGORY_ID IS NULL
BEGIN
    PRINT '❌ ERROR: Không tìm thấy WELLNESS category. Vui lòng chạy create_tables.sql trước!';
    RETURN;
END

PRINT '✅ Category IDs: MEAL=' + CAST(@MEAL_CATEGORY_ID AS VARCHAR) + ', WELLNESS=' + CAST(@WELLNESS_CATEGORY_ID AS VARCHAR);

-- Xóa dữ liệu cũ (nếu có)
DELETE FROM MealService WHERE hotel_id = 1;
DELETE FROM WellnessService WHERE hotel_id = 1;
PRINT '🗑️ Đã xóa dữ liệu cũ';

-- Insert sample MealService data
INSERT INTO MealService (hotel_id, category_id, meal_type, meal_date, description, price, status)
VALUES 
(1, @MEAL_CATEGORY_ID, N'Bữa sáng Buffet', '2025-01-01', N'Buffet sáng đa dạng với món Á - Âu, hải sản tươi sống', 250000, 'active'),
(1, @MEAL_CATEGORY_ID, N'Bữa trưa Set Menu', '2025-01-01', N'Set menu 3 món: Khai vị, món chính, tráng miệng', 350000, 'active'),
(1, @MEAL_CATEGORY_ID, N'Bữa tối Fine Dining', '2025-01-01', N'Bữa tối sang trọng với steak cao cấp và rượu vang', 750000, 'active'),
(1, @MEAL_CATEGORY_ID, N'BBQ Hải sản', '2025-01-01', N'Tiệc BBQ hải sản tươi sống nướng tại chỗ', 650000, 'active'),
(1, @MEAL_CATEGORY_ID, N'Lẩu Thái', '2025-01-01', N'Lẩu Thái Tom Yum chua cay đặc trưng', 450000, 'active');

PRINT '✅ Đã insert ' + CAST(@@ROWCOUNT AS VARCHAR) + ' MealService records';
GO

-- Insert sample WellnessService data  
DECLARE @WELLNESS_CATEGORY_ID INT;
SELECT @WELLNESS_CATEGORY_ID = category_id FROM ServiceCategory WHERE category_code = 'WELLNESS';

INSERT INTO WellnessService (hotel_id, category_id, service_name, description, base_price, duration_minutes, operating_hours, capacity, status)
VALUES 
(1, @WELLNESS_CATEGORY_ID, N'Massage Body Thư giãn', N'Massage toàn thân với tinh dầu thảo mộc thiên nhiên', 500000, 60, '08:00 - 22:00', 10, 'active'),
(1, @WELLNESS_CATEGORY_ID, N'Massage Foot & Leg', N'Massage chân với kỹ thuật bấm huyệt', 300000, 45, '08:00 - 22:00', 15, 'active'),
(1, @WELLNESS_CATEGORY_ID, N'Spa Mặt Collagen', N'Chăm sóc da mặt với mặt nạ collagen cao cấp', 800000, 90, '09:00 - 20:00', 5, 'active'),
(1, @WELLNESS_CATEGORY_ID, N'Tắm Bùn Khoáng', N'Tắm bùn khoáng với đá muối Himalaya', 600000, 60, '08:00 - 18:00', 8, 'active'),
(1, @WELLNESS_CATEGORY_ID, N'Yoga & Meditation', N'Lớp yoga và thiền định với huấn luyện viên chuyên nghiệp', 200000, 60, '06:00 - 08:00, 17:00 - 19:00', 20, 'active'),
(1, @WELLNESS_CATEGORY_ID, N'Sauna & Jacuzzi', N'Phòng xông hơi và bồn sục massage nước nóng', 350000, 45, '08:00 - 22:00', 12, 'active'),
(1, @WELLNESS_CATEGORY_ID, N'Thai Massage', N'Massage Thái cổ truyền với kỹ thuật kéo giãn cơ', 550000, 90, '08:00 - 22:00', 8, 'active'),
(1, @WELLNESS_CATEGORY_ID, N'Aromatherapy', N'Liệu pháp hương thơm với tinh dầu thiên nhiên', 450000, 60, '09:00 - 21:00', 6, 'active');

PRINT '✅ Đã insert ' + CAST(@@ROWCOUNT AS VARCHAR) + ' WellnessService records';
GO

-- Verify data
SELECT 'MealService Count' as TableName, COUNT(*) as RecordCount FROM MealService WHERE status = 'active'
UNION ALL
SELECT 'WellnessService Count', COUNT(*) FROM WellnessService WHERE status = 'active';
GO

-- Display inserted data
SELECT * FROM MealService WHERE status = 'active';
SELECT * FROM WellnessService WHERE status = 'active';
GO

