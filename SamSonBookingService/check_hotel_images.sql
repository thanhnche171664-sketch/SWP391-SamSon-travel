-- Script để kiểm tra ảnh của hotels trong database
-- Chạy script này để xem có ảnh nào trong bảng Images không

USE [YourDatabaseName];  -- Thay YourDatabaseName bằng tên database của bạn
GO

-- 1. Kiểm tra tất cả hotels
SELECT 
    h.id AS hotel_id,
    h.name AS hotel_name,
    h.featured,
    h.rating
FROM Hotels h
ORDER BY h.id;
GO

-- 2. Kiểm tra tất cả ảnh của hotels
SELECT 
    i.id AS image_id,
    i.entity_type,
    i.entity_id AS hotel_id,
    h.name AS hotel_name,
    i.image_url,
    i.is_primary,
    i.display_order,
    i.created_at
FROM Images i
LEFT JOIN Hotels h ON i.entity_id = h.id AND i.entity_type = 'hotel'
WHERE i.entity_type = 'hotel'
ORDER BY i.entity_id, i.is_primary DESC, i.display_order, i.id;
GO

-- 3. Kiểm tra hotels có ảnh hay không
SELECT 
    h.id AS hotel_id,
    h.name AS hotel_name,
    COUNT(i.id) AS image_count,
    MAX(CASE WHEN i.is_primary = 1 THEN i.image_url END) AS primary_image_url,
    MAX(CASE WHEN i.is_primary = 0 THEN i.image_url END) AS first_image_url
FROM Hotels h
LEFT JOIN Images i ON i.entity_type = 'hotel' AND i.entity_id = h.id
GROUP BY h.id, h.name
ORDER BY h.id;
GO

-- 4. Kiểm tra featured hotels và ảnh của chúng
SELECT 
    h.id AS hotel_id,
    h.name AS hotel_name,
    h.featured,
    h.rating,
    COUNT(i.id) AS image_count,
    MAX(CASE WHEN i.is_primary = 1 THEN i.image_url END) AS primary_image_url
FROM Hotels h
LEFT JOIN Images i ON i.entity_type = 'hotel' AND i.entity_id = h.id
WHERE h.featured = 1
GROUP BY h.id, h.name, h.featured, h.rating
ORDER BY h.rating DESC;
GO

-- 5. Xem chi tiết ảnh của từng featured hotel
SELECT 
    h.id AS hotel_id,
    h.name AS hotel_name,
    i.id AS image_id,
    i.image_url,
    i.is_primary,
    i.display_order
FROM Hotels h
LEFT JOIN Images i ON i.entity_type = 'hotel' AND i.entity_id = h.id
WHERE h.featured = 1
ORDER BY h.id, i.is_primary DESC, i.display_order, i.id;
GO

