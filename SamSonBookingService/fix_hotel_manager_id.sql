-- Script để XÓA cột manager_id khỏi bảng Hotels
-- Chạy script này trong SQL Server Management Studio hoặc database tool
-- LƯU Ý: Backup database trước khi chạy script này!

USE [YourDatabaseName];  -- Thay YourDatabaseName bằng tên database của bạn
GO

-- Bước 1: Kiểm tra cấu trúc bảng Hotels hiện tại
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'Hotels'
ORDER BY ORDINAL_POSITION;
GO

-- Bước 2: Kiểm tra xem có Foreign Key constraint nào liên quan đến manager_id không
SELECT 
    fk.name AS ForeignKeyName,
    OBJECT_NAME(fk.parent_object_id) AS TableName,
    COL_NAME(fc.parent_object_id, fc.parent_column_id) AS ColumnName,
    OBJECT_NAME(fk.referenced_object_id) AS ReferencedTableName,
    COL_NAME(fc.referenced_object_id, fc.referenced_column_id) AS ReferencedColumnName
FROM sys.foreign_keys AS fk
INNER JOIN sys.foreign_key_columns AS fc ON fk.object_id = fc.constraint_object_id
WHERE OBJECT_NAME(fk.parent_object_id) = 'Hotels'
  AND COL_NAME(fc.parent_object_id, fc.parent_column_id) = 'manager_id';
GO

-- Bước 3: Kiểm tra xem có Index nào trên cột manager_id không
SELECT 
    i.name AS IndexName,
    i.type_desc AS IndexType,
    COL_NAME(ic.object_id, ic.column_id) AS ColumnName
FROM sys.indexes AS i
INNER JOIN sys.index_columns AS ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id
WHERE OBJECT_NAME(i.object_id) = 'Hotels'
  AND COL_NAME(ic.object_id, ic.column_id) = 'manager_id';
GO

-- Bước 4: XÓA Foreign Key constraint nếu có (thay ForeignKeyName bằng tên thực tế từ Bước 2)
-- Nếu có Foreign Key, uncomment và chạy dòng dưới (thay FK_Hotels_ManagerId bằng tên thực tế)
-- ALTER TABLE Hotels DROP CONSTRAINT FK_Hotels_ManagerId;  -- Thay bằng tên Foreign Key thực tế
-- GO

-- Bước 5: XÓA Index nếu có (thay IndexName bằng tên thực tế từ Bước 3)
-- Nếu có Index, uncomment và chạy dòng dưới (thay IX_Hotels_ManagerId bằng tên thực tế)
-- DROP INDEX IX_Hotels_ManagerId ON Hotels;  -- Thay bằng tên Index thực tế
-- GO

-- Bước 6: XÓA cột manager_id khỏi bảng Hotels
ALTER TABLE Hotels
DROP COLUMN manager_id;
GO

-- Bước 7: Kiểm tra lại cấu trúc bảng sau khi xóa
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'Hotels'
ORDER BY ORDINAL_POSITION;
GO

-- Bước 8: Xác nhận cột manager_id đã bị xóa
IF EXISTS (
    SELECT 1 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_NAME = 'Hotels' 
    AND COLUMN_NAME = 'manager_id'
)
    PRINT 'LỖI: Cột manager_id vẫn còn tồn tại!'
ELSE
    PRINT 'THÀNH CÔNG: Cột manager_id đã được xóa khỏi bảng Hotels!'
GO

