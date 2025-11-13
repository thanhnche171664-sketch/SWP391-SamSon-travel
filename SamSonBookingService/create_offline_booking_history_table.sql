-- ===========================================
-- BẢNG LỊCH SỬ ĐẶT PHÒNG OFFLINE
-- Lưu thông tin khách hàng và lịch sử đặt phòng offline cho mỗi khách sạn
-- ===========================================

USE booking_travel;
GO

-- Xóa VIEW và bảng cũ nếu đã tồn tại (để chạy lại được)
IF EXISTS (SELECT * FROM sys.views WHERE name = 'vw_Offline_Hotel_Booking_History')
    DROP VIEW vw_Offline_Hotel_Booking_History;
GO

IF EXISTS (SELECT * FROM sys.views WHERE name = 'vw_Offline_Customer_Booking_History')
    DROP VIEW vw_Offline_Customer_Booking_History;
GO

IF EXISTS (SELECT * FROM sys.tables WHERE name = 'Offline_Booking_Customers')
    DROP TABLE Offline_Booking_Customers;
GO

-- Bảng lưu lịch sử đặt phòng offline
-- Mỗi dòng = 1 booking của 1 khách hàng tại 1 khách sạn
CREATE TABLE Offline_Booking_Customers (
    id INT IDENTITY(1,1) PRIMARY KEY,
    booking_id INT NOT NULL,                    -- ID của booking
    offline_customer_id INT NOT NULL,           -- ID của khách hàng
    hotel_id INT NOT NULL,                      -- ID của khách sạn
    
    -- Thông tin đặt phòng
    check_in_date DATE NOT NULL,                -- Ngày check-in
    check_out_date DATE NOT NULL,               -- Ngày check-out
    num_adults INT NOT NULL DEFAULT 1,          -- Số người lớn
    num_children INT NOT NULL DEFAULT 0,         -- Số trẻ em
    total_amount DECIMAL(10,2) NOT NULL,       -- Tổng tiền
    payment_status NVARCHAR(20) DEFAULT 'PAID',  -- Trạng thái thanh toán
    notes NVARCHAR(500) NULL,                   -- Ghi chú
    
    created_at DATETIME DEFAULT GETDATE(),      -- Ngày tạo
    updated_at DATETIME DEFAULT GETDATE(),      -- Ngày cập nhật
    
    -- Liên kết với các bảng khác
    FOREIGN KEY (booking_id) REFERENCES Bookings(id),
    FOREIGN KEY (offline_customer_id) REFERENCES Offline_Customers(offline_customer_id),
    FOREIGN KEY (hotel_id) REFERENCES Hotels(id),
    
    -- Mỗi booking chỉ có 1 record
    UNIQUE (booking_id)
);
GO

-- Tạo VIEW để xem lịch sử theo khách hàng
-- Lưu ý: Không dùng ORDER BY trong VIEW, sẽ ORDER BY khi query
CREATE VIEW vw_Offline_Customer_Booking_History AS
SELECT 
    obc.id,
    obc.booking_id,
    b.booking_code,
    b.room_type,
    b.number_of_rooms,
    b.booking_date,
    b.status as booking_status,
    oc.offline_customer_id,
    oc.full_name as customer_name,
    oc.phone as customer_phone,
    oc.email as customer_email,
    oc.id_card_number as customer_id_card,
    oc.nationality as customer_nationality,
    oc.gender as customer_gender,
    oc.date_of_birth as customer_date_of_birth,
    oc.address as customer_address,
    oc.created_at as customer_created_at,
    h.id as hotel_id,
    h.name as hotel_name,
    obc.check_in_date,
    obc.check_out_date,
    obc.num_adults,
    obc.num_children,
    obc.total_amount,
    obc.payment_status,
    obc.notes,
    obc.created_at,
    (
        SELECT STRING_AGG(
            COALESCE(sc.category_name, N'Dịch vụ') + N' (x' + CAST(bd.quantity AS NVARCHAR(10)) + N')',
            N', '
        )
        FROM Booking_Details bd
        LEFT JOIN ServiceCategories sc ON bd.category_id = sc.category_id
        WHERE bd.booking_id = b.id
    ) AS service_items
FROM Offline_Booking_Customers obc
INNER JOIN Bookings b ON obc.booking_id = b.id
INNER JOIN Offline_Customers oc ON obc.offline_customer_id = oc.offline_customer_id
INNER JOIN Hotels h ON obc.hotel_id = h.id
WHERE b.booking_source = 'OFFLINE';
GO

-- Tạo VIEW để xem lịch sử theo khách sạn
-- Lưu ý: Không dùng ORDER BY trong VIEW, sẽ ORDER BY khi query
CREATE VIEW vw_Offline_Hotel_Booking_History AS
SELECT 
    obc.id,
    obc.booking_id,
    b.booking_code,
    b.room_type,
    b.number_of_rooms,
    b.booking_date,
    h.id as hotel_id,
    h.name as hotel_name,
    oc.offline_customer_id,
    oc.full_name as customer_name,
    oc.phone as customer_phone,
    oc.email as customer_email,
    obc.check_in_date,
    obc.check_out_date,
    obc.num_adults,
    obc.num_children,
    obc.total_amount,
    obc.payment_status,
    obc.created_at,
    (
        SELECT STRING_AGG(
            COALESCE(sc.category_name, N'Dịch vụ') + N' (x' + CAST(bd.quantity AS NVARCHAR(10)) + N')',
            N', '
        )
        FROM Booking_Details bd
        LEFT JOIN ServiceCategories sc ON bd.category_id = sc.category_id
        WHERE bd.booking_id = b.id
    ) AS service_items
FROM Offline_Booking_Customers obc
INNER JOIN Bookings b ON obc.booking_id = b.id
INNER JOIN Offline_Customers oc ON obc.offline_customer_id = oc.offline_customer_id
INNER JOIN Hotels h ON obc.hotel_id = h.id
WHERE b.booking_source = 'OFFLINE';
GO

PRINT 'Đã tạo bảng Offline_Booking_Customers thành công!';

