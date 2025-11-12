-- ===========================================
-- SCRIPT HOÀN CHỈNH: SETUP BẢNG IMAGES VÀ DỮ LIỆU
-- Mô tả: File DUY NHẤT chứa TẤT CẢ code cần thiết
-- ===========================================
-- HƯỚNG DẪN:
-- 1. Thay đổi database name tại dòng 25 nếu cần
-- 2. Chạy toàn bộ script này - CHỈ CẦN CHẠY 1 FILE NÀY!
-- 3. Script sẽ tự động:
--    - Tạo bảng Images (nếu chưa có)
--    - Xóa dữ liệu cũ (nếu muốn reset - bỏ comment dòng 75)
--    - Tự động thêm ảnh cho TẤT CẢ hotels (cả đã có và chưa có)
--    - Đảm bảo mỗi hotel chỉ có 1 primary image
--    - Kiểm tra và báo cáo kết quả
-- ===========================================
-- Tác giả: SamSon Travel Team
-- Ngày tạo: 2025-11-06
-- Phiên bản: 2.0 (All-in-One)
-- ===========================================

-- ===========================================
-- BƯỚC 1: CHỌN DATABASE
-- ===========================================
-- ⚠️ THAY ĐỔI DATABASE NAME TẠI ĐÂY NẾU CẦN
-- ===========================================

USE booking_travel_30;
GO

-- ===========================================
-- BƯỚC 2: TẠO BẢNG IMAGES (NẾU CHƯA TỒN TẠI)
-- ===========================================

PRINT '===========================================';
PRINT 'BƯỚC 2: TẠO BẢNG IMAGES';
PRINT '===========================================';

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Images' AND xtype='U')
BEGIN
    CREATE TABLE Images (
        id INT IDENTITY(1,1) PRIMARY KEY,
        entity_type VARCHAR(20) NOT NULL CHECK (entity_type IN ('hotel', 'room')),
        entity_id INT NOT NULL,
        image_url NVARCHAR(500) NOT NULL,
        is_primary BIT DEFAULT 0,
        display_order INT DEFAULT 0,
        alt_text NVARCHAR(255),
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE()
    );
    
    -- Tạo indexes để tối ưu query
    CREATE INDEX idx_images_entity ON Images(entity_type, entity_id);
    CREATE INDEX idx_images_primary ON Images(entity_type, entity_id, is_primary);
    
    PRINT '✓ Bảng Images đã được tạo thành công!';
    PRINT '✓ Đã tạo indexes để tối ưu query.';
END
ELSE
BEGIN
    PRINT '✓ Bảng Images đã tồn tại.';
END

GO

-- ===========================================
-- BƯỚC 3: XÓA DỮ LIỆU CŨ (TÙY CHỌN)
-- ===========================================
-- ⚠️ BỎ COMMENT DÒNG DƯỚI NẾU MUỐN XÓA HẾT DỮ LIỆU CŨ TRƯỚC KHI THÊM LẠI
-- ===========================================

-- DELETE FROM Images WHERE entity_type = 'hotel';
-- PRINT '✓ Đã xóa tất cả images của hotels.';

GO

-- ===========================================
-- BƯỚC 4: TỰ ĐỘNG THÊM ẢNH CHO TẤT CẢ HOTELS
-- ===========================================

PRINT '===========================================';
PRINT 'BƯỚC 4: TỰ ĐỘNG THÊM ẢNH CHO TẤT CẢ HOTELS';
PRINT '===========================================';

-- Bỏ primary của tất cả local images cũ (nếu có)
UPDATE Images 
SET is_primary = 0 
WHERE entity_type = 'hotel' 
  AND (image_url LIKE 'uploads/%' OR image_url LIKE 'assets/%')
  AND is_primary = 1;

PRINT 'Đã bỏ primary của các local images cũ.';

GO

-- Procedure để insert hoặc update ảnh primary cho hotel
CREATE OR ALTER PROCEDURE InsertOrUpdateHotelPrimaryImage
    @hotel_id INT,
    @hotel_name NVARCHAR(255),
    @image_url NVARCHAR(500)
AS
BEGIN
    -- Bỏ primary của tất cả ảnh cũ cho hotel này
    UPDATE Images SET is_primary = 0 WHERE entity_type = 'hotel' AND entity_id = @hotel_id;
    
    -- Insert hoặc cập nhật ảnh primary mới (external URL)
    IF NOT EXISTS (SELECT 1 FROM Images WHERE entity_type = 'hotel' AND entity_id = @hotel_id AND image_url = @image_url)
    BEGIN
        INSERT INTO Images (entity_type, entity_id, image_url, is_primary, display_order, alt_text) 
        VALUES ('hotel', @hotel_id, @image_url, 1, 1, @hotel_name);
        PRINT '✓ Đã insert ảnh primary cho Hotel ID ' + CAST(@hotel_id AS VARCHAR) + ': ' + @hotel_name;
    END
    ELSE
    BEGIN
        UPDATE Images SET is_primary = 1, alt_text = @hotel_name, updated_at = GETDATE() 
        WHERE entity_type = 'hotel' AND entity_id = @hotel_id AND image_url = @image_url;
        PRINT '✓ Đã cập nhật ảnh primary cho Hotel ID ' + CAST(@hotel_id AS VARCHAR) + ': ' + @hotel_name;
    END
END
GO

-- Tự động thêm ảnh cho tất cả hotels
DECLARE @hotel_id INT;
DECLARE @hotel_name NVARCHAR(255);
DECLARE @image_url NVARCHAR(500);
DECLARE @processed_count INT = 0;

-- Tìm tất cả hotels
DECLARE hotel_cursor CURSOR FOR
SELECT id, name
FROM Hotels
ORDER BY id;

OPEN hotel_cursor;
FETCH NEXT FROM hotel_cursor INTO @hotel_id, @hotel_name;

WHILE @@FETCH_STATUS = 0
BEGIN
    -- Chọn external URL dựa trên tên hotel hoặc ID
    SET @image_url = NULL;
    
    -- Kiểm tra tên hotel để chọn URL phù hợp
    IF @hotel_name LIKE '%Sầm Sơn Resort%' OR @hotel_name LIKE '%Sam Son Resort%' OR @hotel_id = 1
    BEGIN
        SET @image_url = 'https://mia.vn/media/uploads/blog-du-lich/top-10-khach-san-sam-son-cao-cap-view-bien-01-1681097848.jpeg';
    END
    ELSE IF @hotel_name LIKE '%FLC%' OR @hotel_name LIKE '%Beach & Golf%' OR @hotel_id = 2
    BEGIN
        SET @image_url = 'https://bizweb.dktcdn.net/100/081/807/articles/480817872-964984232399392-8248075897494586713-n.jpg?v=1742984793930';
    END
    ELSE IF @hotel_name LIKE '%Sun World%' OR @hotel_name LIKE '%SunWorld%' OR @hotel_id = 3
    BEGIN
        SET @image_url = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyO_G7xL8Y-xXEqFfTnTHZRVaHtjKOnQdjiA&s';
    END
    ELSE IF @hotel_name LIKE '%Hải Đăng%' OR @hotel_name LIKE '%Hai Dang%' OR @hotel_id = 4
    BEGIN
        SET @image_url = 'https://mia.vn/media/uploads/blog-du-lich/top-10-khach-san-sam-son-cao-cap-view-bien-03-1681097848.jpeg';
    END
    ELSE IF @hotel_name LIKE '%Sao Mai%' OR @hotel_name LIKE '%SaoMai%' OR @hotel_id = 5
    BEGIN
        SET @image_url = 'https://media-cdn.tripadvisor.com/media/photo-s/08/cf/fd/6d/a-la-carte-sam-son-facade.jpg';
    END
    ELSE IF @hotel_name LIKE '%Golden Bay%' OR @hotel_name LIKE '%GoldenBay%'
    BEGIN
        SET @image_url = 'https://mia.vn/media/uploads/blog-du-lich/top-10-khach-san-sam-son-cao-cap-view-bien-02-1681097848.jpeg';
    END
    ELSE IF @hotel_name LIKE '%Sunshine%'
    BEGIN
        SET @image_url = 'https://mia.vn/media/uploads/blog-du-lich/top-10-khach-san-sam-son-cao-cap-view-bien-04-1681097848.jpeg';
    END
    ELSE IF @hotel_name LIKE '%Sea Pearl%' OR @hotel_name LIKE '%Seapearl%'
    BEGIN
        SET @image_url = 'https://mia.vn/media/uploads/blog-du-lich/top-10-khach-san-sam-son-cao-cap-view-bien-05-1681097848.jpeg';
    END
    ELSE IF @hotel_name LIKE '%Ocean View%' OR @hotel_name LIKE '%OceanView%'
    BEGIN
        SET @image_url = 'https://mia.vn/media/uploads/blog-du-lich/top-10-khach-san-sam-son-cao-cap-view-bien-06-1681097848.jpeg';
    END
    ELSE
    BEGIN
        -- URL mặc định cho các hotels khác
        SET @image_url = 'https://mia.vn/media/uploads/blog-du-lich/top-10-khach-san-sam-son-cao-cap-view-bien-01-1681097848.jpeg';
    END
    
    -- Insert hoặc update ảnh cho hotel
    IF @image_url IS NOT NULL
    BEGIN
        EXEC InsertOrUpdateHotelPrimaryImage @hotel_id, @hotel_name, @image_url;
        SET @processed_count = @processed_count + 1;
    END
    ELSE
    BEGIN
        PRINT '⚠ Không thể xác định URL ảnh cho Hotel ID ' + CAST(@hotel_id AS VARCHAR) + ': ' + @hotel_name;
    END
    
    FETCH NEXT FROM hotel_cursor INTO @hotel_id, @hotel_name;
END

CLOSE hotel_cursor;
DEALLOCATE hotel_cursor;

PRINT 'Đã xử lý ' + CAST(@processed_count AS VARCHAR) + ' hotels.';

GO

-- ===========================================
-- BƯỚC 5: ĐẢM BẢO MỖI HOTEL CHỈ CÓ 1 PRIMARY IMAGE
-- ===========================================

PRINT '===========================================';
PRINT 'BƯỚC 5: ĐẢM BẢO MỖI HOTEL CHỈ CÓ 1 PRIMARY IMAGE';
PRINT '===========================================';

-- Bỏ primary của các images không phải external URL (nếu có nhiều primary)
UPDATE Images 
SET is_primary = 0 
WHERE entity_type = 'hotel' 
  AND is_primary = 1
  AND id NOT IN (
      SELECT TOP 1 id 
      FROM Images i2 
      WHERE i2.entity_type = 'hotel' 
        AND i2.entity_id = Images.entity_id 
        AND i2.is_primary = 1
        AND (i2.image_url LIKE 'http://%' OR i2.image_url LIKE 'https://%')
      ORDER BY i2.id DESC
  );

PRINT 'Đã đảm bảo mỗi hotel chỉ có 1 primary image (ưu tiên external URL).';

GO

-- ===========================================
-- BƯỚC 6: KIỂM TRA KẾT QUẢ
-- ===========================================

PRINT '===========================================';
PRINT 'BƯỚC 6: KIỂM TRA KẾT QUẢ';
PRINT '===========================================';

-- Hiển thị danh sách hotels và images
SELECT 
    h.id AS hotel_id,
    h.name AS hotel_name,
    i.id AS image_id,
    i.image_url,
    i.is_primary,
    CASE 
        WHEN i.image_url LIKE 'http://%' OR i.image_url LIKE 'https://%' THEN 'External URL'
        WHEN i.image_url IS NULL THEN 'NO IMAGE'
        ELSE 'Local Path'
    END AS url_type,
    i.alt_text
FROM Hotels h
LEFT JOIN Images i ON i.entity_type = 'hotel' AND i.entity_id = h.id AND i.is_primary = 1
ORDER BY h.id;

GO

-- Thống kê
DECLARE @total_hotels INT;
DECLARE @hotels_with_images INT;
DECLARE @hotels_without_images INT;
DECLARE @total_images INT;

SELECT @total_hotels = COUNT(*) FROM Hotels;
SELECT @hotels_with_images = COUNT(DISTINCT i.entity_id) FROM Images i WHERE i.entity_type = 'hotel' AND i.is_primary = 1;
SELECT @hotels_without_images = @total_hotels - @hotels_with_images;
SELECT @total_images = COUNT(*) FROM Images WHERE entity_type = 'hotel';

PRINT '===========================================';
PRINT 'THỐNG KÊ:';
PRINT '  - Tổng số hotels: ' + CAST(@total_hotels AS VARCHAR);
PRINT '  - Hotels có ảnh: ' + CAST(@hotels_with_images AS VARCHAR);
PRINT '  - Hotels chưa có ảnh: ' + CAST(@hotels_without_images AS VARCHAR);
PRINT '  - Tổng số images: ' + CAST(@total_images AS VARCHAR);
PRINT '===========================================';

GO

-- Kiểm tra hotels không có ảnh
IF EXISTS (SELECT 1 FROM Hotels h WHERE NOT EXISTS (SELECT 1 FROM Images i WHERE i.entity_type = 'hotel' AND i.entity_id = h.id AND i.is_primary = 1))
BEGIN
    PRINT '⚠ CẢNH BÁO: Có hotels không có ảnh:';
    SELECT h.id, h.name
    FROM Hotels h
    WHERE NOT EXISTS (
        SELECT 1 
        FROM Images i 
        WHERE i.entity_type = 'hotel' 
          AND i.entity_id = h.id 
          AND i.is_primary = 1
    )
    ORDER BY h.id;
END
ELSE
BEGIN
    PRINT '✓ TẤT CẢ HOTELS ĐỀU CÓ ẢNH!';
END

GO

-- Kiểm tra duplicate primary images
IF EXISTS (
    SELECT entity_id, COUNT(*) 
    FROM Images 
    WHERE entity_type = 'hotel' AND is_primary = 1
    GROUP BY entity_id
    HAVING COUNT(*) > 1
)
BEGIN
    PRINT '⚠ CẢNH BÁO: Có hotels có nhiều hơn 1 primary image:';
    SELECT entity_id AS hotel_id, COUNT(*) AS primary_count
    FROM Images
    WHERE entity_type = 'hotel' AND is_primary = 1
    GROUP BY entity_id
    HAVING COUNT(*) > 1;
END
ELSE
BEGIN
    PRINT '✓ TẤT CẢ HOTELS ĐỀU CHỈ CÓ 1 PRIMARY IMAGE!';
END

GO

-- Xóa procedure tạm
DROP PROCEDURE IF EXISTS InsertOrUpdateHotelPrimaryImage;
GO

PRINT '===========================================';
PRINT 'HOÀN THÀNH!';
PRINT '===========================================';
PRINT 'Bảng Images đã được tạo và dữ liệu đã được insert.';
PRINT 'Bạn có thể build lại project và test.';
PRINT '===========================================';

