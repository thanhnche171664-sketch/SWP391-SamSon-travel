# Hướng Dẫn Sử Dụng Giỏ Hàng (Cart) - Cho Sinh Viên

## Tổng Quan
Hệ thống giỏ hàng cho phép khách hàng:
1. Chọn phòng từ `Room-list.jsp`
2. Chọn dịch vụ từ `Service-list.jsp`  
3. Xem tổng hợp và điền thông tin ở `Customer-info.jsp`

## Cách Hoạt Động

### Bước 1: Chọn Phòng (Room-list.jsp)
- Khách hàng chọn phòng và nhấn "Đặt phòng"
- JavaScript gửi dữ liệu đến `/api/cart/save`
- `CartSaveServlet` lưu vào session
- Chuyển đến trang `Customer-info.jsp`

### Bước 2: Chọn Dịch Vụ (Service-list.jsp) - Tùy chọn
- Từ `Customer-info.jsp`, nhấn "Thêm dịch vụ"
- Chọn dịch vụ và nhấn "Quay lại"
- Dịch vụ được lưu vào session
- Quay lại `Customer-info.jsp` với dịch vụ đã chọn

### Bước 3: Điền Thông Tin (Customer-info.jsp)
- `CustomerInfoServlet` lấy dữ liệu từ session
- Hiển thị phòng và dịch vụ đã chọn
- Khách hàng điền thông tin và submit
- Dữ liệu được lưu và chuyển sang bước tiếp theo

## Các File Quan Trọng

### 1. CartSaveServlet.java
**Chức năng:** Lưu giỏ hàng vào session

**Cách hoạt động:**
```
1. Nhận JSON từ JavaScript
2. Parse JSON để lấy roomCart và serviceCart
3. Lưu vào session
4. Trả về kết quả thành công/thất bại
```

**URL:** `/api/cart/save`

### 2. CustomerInfoServlet.java
**Chức năng:** Hiển thị form và xử lý thông tin khách hàng

**Cách hoạt động:**
```
GET:
1. Lấy giỏ hàng từ session
2. Lấy thông tin phòng/dịch vụ từ database
3. Tính tổng tiền
4. Hiển thị trong Customer-info.jsp

POST:
1. Lấy thông tin từ form
2. Kiểm tra dữ liệu
3. Lưu vào session
4. Chuyển sang bước tiếp theo
```

**URL:** `/customer-info`

### 3. Customer-info.jsp
**Chức năng:** Hiển thị form điền thông tin

**Hiển thị:**
- Danh sách phòng đã chọn
- Danh sách dịch vụ đã chọn (nếu có)
- Form điền thông tin khách hàng
- Tổng thanh toán

## Dữ Liệu Trong Session

### roomCart
Danh sách phòng đã chọn:
```java
List<Map<String, Object>> roomCart
// Mỗi item có: id, quantity, nights, pricePerNight, ...
```

### serviceCart
Danh sách dịch vụ đã chọn:
```java
List<Map<String, Object>> serviceCart
// Mỗi item có: id, type (meal/wellness), quantity, price, ...
```

### checkinDate, checkoutDate
Ngày check-in và check-out

## Lưu Ý Cho Sinh Viên

1. **Session:** Dữ liệu được lưu trong session, chỉ tồn tại khi người dùng còn đăng nhập/sử dụng

2. **JSON Parse:** Code parse JSON đơn giản, không dùng thư viện phức tạp

3. **Database:** Khi hiển thị, code lấy thông tin đầy đủ từ database (giá, tên, ...)

4. **Error Handling:** Code có xử lý lỗi cơ bản, nếu có lỗi sẽ hiển thị thông báo

5. **Comment:** Code có comment tiếng Việt để dễ hiểu

## Cách Test

1. Vào trang `Room-list.jsp`
2. Chọn phòng và nhấn "Đặt phòng"
3. Kiểm tra xem có chuyển đến `Customer-info.jsp` không
4. Kiểm tra xem phòng đã chọn có hiển thị không
5. Thử thêm dịch vụ
6. Điền form và submit

## Các Bước Tiếp Theo

1. Tạo servlet để lưu booking vào database
2. Tạo trang hiển thị hóa đơn (Invoice)
3. Xử lý thanh toán

## Câu Hỏi Thường Gặp

**Q: Tại sao không dùng database để lưu giỏ hàng?**
A: Giỏ hàng chỉ là tạm thời, khi người dùng đặt phòng mới lưu vào database.

**Q: Làm sao để thêm/xóa item khỏi giỏ hàng?**
A: Cần tạo thêm API để cập nhật giỏ hàng trong session.

**Q: Giỏ hàng có bị mất không?**
A: Có, nếu session hết hạn hoặc đóng trình duyệt.





