# 🧪 HƯỚNG DẪN TEST UPLOAD ẢNH

## ✅ Đã Sửa Các File

### 1. **Fix lưu ảnh vào thư mục đúng**
- ✅ `AddHotelServlet.java` 
- ✅ `EditHotelServlet.java` (field: `newImages`)
- ✅ `AddRoomServlet.java`
- ✅ `EditRoomServlet.java` (field: `images`)
- ✅ `AddMealServiceServlet.java`
- ✅ `EditMealServiceServlet.java` (field: `images`)
- ✅ `UploadAvatarServlet.java`

### 2. **Thay đổi chính:**
```java
// Trước (SAI - lưu vào build/):
String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

// Sau (ĐÚNG - lưu vào web/):
String webPath = getServletContext().getRealPath("/");
String uploadPath = webPath + UPLOAD_DIR;
```

## 🔍 CÁCH TEST

### Bước 1: Clean and Build
1. Trong NetBeans, chuột phải vào project
2. Chọn **Clean and Build**
3. Chờ build xong

### Bước 2: Chạy Server
1. Run project (F6)
2. Đăng nhập với tài khoản Hotel Manager (role_id = 3)

### Bước 3: Test Upload Ảnh Khi EDIT

#### Test Edit Hotel:
1. Vào `/hotel/list`
2. Click "Sửa" 1 khách sạn
3. **Cuộn xuống phần "Quản Lý Hình Ảnh"**
4. Tìm form "Thêm hình ảnh mới"
5. Click "Choose Files" và chọn 2-3 ảnh
6. Click nút **"Tải Lên Ảnh"** (CHỨ KHÔNG phải nút "Cập Nhật" ở trên)
7. ✅ **Kiểm tra:** Console có log như sau:
```
=== DEBUG: handleMultipleFileUpload for hotelId=1 ===
Total parts received: 5
Processing part: name=hotelId, size=1
Processing part: name=newImages, size=123456
Saving file: C:\...\web\uploads\hotels\hotel_1_1234567890.jpg
Database insert: true
=== Total images uploaded: 1 ===
```

#### Test Edit Room:
1. Vào hotel detail → Click "Sửa" 1 phòng
2. **Cuộn xuống phần upload ảnh**
3. Chọn ảnh và click "Upload Images"
4. ✅ **Kiểm tra console log**

#### Test Edit Meal Service:
1. Vào hotel detail → Click "Sửa" 1 meal service
2. **Cuộn xuống phần "Quản Lý Hình Ảnh"**
3. Tìm form "Thêm hình ảnh mới"
4. Chọn ảnh (có thể chọn nhiều ảnh cùng lúc)
5. Click nút **"Tải Lên Ảnh"**
6. ✅ **Kiểm tra console log:**
```
=== DEBUG: handleMultipleFileUpload for mealId=1 ===
Total parts received: 4
Processing part: name=mealId, size=1
Processing part: name=hotelId, size=1
Processing part: name=uploadOnly, size=4
Processing part: name=images, size=234567
Saving file: C:\...\web\uploads\meals\meal_1_1234567890.jpg
File saved successfully!
Database insert result: true
=== Total images uploaded: 1 ===
```

### Bước 4: Kiểm Tra File Đã Lưu

#### Kiểm tra trong thư mục source:
```
SamSonBookingService/
└── web/
    └── uploads/
        ├── hotels/
        │   └── hotel_1_1234567890.jpg  ✅ FILE Ở ĐÂY
        ├── rooms/
        │   └── room_1_1234567890.jpg   ✅ FILE Ở ĐÂY
        └── meals/
            └── meal_1_1234567890.jpg   ✅ FILE Ở ĐÂY
```

#### Kiểm tra database:
```sql
SELECT * FROM images 
WHERE entity_type = 'hotel' 
  OR entity_type = 'room' 
  OR entity_type = 'meal'
ORDER BY id DESC
LIMIT 10;
```

### Bước 5: Test Clean and Build Lại
1. **Clean and Build** lại project
2. Chạy lại server
3. ✅ **Ảnh vẫn hiển thị bình thường** (không bị mất)

## 🐛 NẾU VẪN KHÔNG UPLOAD ĐƯỢC

### Debug Checklist:

#### 1. Kiểm tra Console Log
- Có thấy log "=== DEBUG: handleMultipleFileUpload..." không?
- Nếu **KHÔNG** → Form không gọi đến servlet, kiểm tra:
  - Form có `enctype="multipart/form-data"` không?
  - Form action URL có đúng không?
  - Có click đúng nút submit không?

#### 2. Kiểm tra Parts Received
```
Total parts received: 4  ← NẾU = 1 hoặc 2 → Không có file được chọn
```

#### 3. Kiểm tra Processing Part
```
Processing part: name=images, size=0  ← Size = 0 → File rỗng
Processing part: name=images, size=123456  ← OK!
```

#### 4. Kiểm tra Field Name
- **EditHotelServlet**: Phải là `name="newImages"`
- **EditRoomServlet**: Phải là `name="images"`
- **EditMealServiceServlet**: Phải là `name="images"`

#### 5. Kiểm tra Database Insert
```
Database insert result: true  ← OK!
Database insert result: false ← LỖI DATABASE
```
Nếu false → Kiểm tra:
- Bảng `images` có tồn tại không?
- Các field (entity_type, entity_id, image_url...) có đúng không?
- Foreign key constraint có vấn đề không?

## 📋 LƯU Ý QUAN TRỌNG

### 1. **Form riêng biệt để upload ảnh**
Các trang edit có **2 FORM**:
- Form 1: Update thông tin (name, description...) - **KHÔNG có multipart**
- Form 2: Upload ảnh mới - **CÓ multipart**

Phải click đúng nút của Form 2!

### 2. **Field name khác nhau**
- `AddHotelServlet`: `images`
- `EditHotelServlet`: `newImages` ← **Khác!**
- `AddRoomServlet`: `images`
- `EditRoomServlet`: `images`
- `AddMealServiceServlet`: `images`
- `EditMealServiceServlet`: `images`

### 3. **Kiểm tra thư mục**
Sau khi upload, file phải ở:
```
web/uploads/hotels/    ← SOURCE (không bị xóa)
build/web/uploads/hotels/  ← BUILD (bị xóa khi clean)
```

NetBeans sẽ **tự động copy** từ `web/` sang `build/web/` khi build.

## ✅ KẾT LUẬN

Nếu làm đúng các bước trên:
- ✅ Ảnh sẽ được lưu vào `web/uploads/`
- ✅ Database sẽ lưu đường dẫn đúng
- ✅ Ảnh hiển thị bình thường
- ✅ Clean and Build không làm mất ảnh
- ✅ Không còn nhấp nháy!

---

**Ngày tạo:** 2025-11-11
**Người tạo:** GitHub Copilot Assistant
