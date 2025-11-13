<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>SamSon Travel - Admin Dashboard</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <style>
            :root {
                --main-text-color: #ffffff;
                --container-bg-color: rgba(0, 0, 0, 0.5); /* Nền container trong suốt hơn để nhìn thấy ảnh */
                --button-color: #007bff; /* Màu xanh dương biển */
                --button-hover-color: #0056b3; /* Màu xanh đậm hơn khi hover */
                --border-radius: 8px;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 0;
                padding: 0;
                color: var(--main-text-color);
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;

                /* Hình nền bãi biển */
                background: url('https://d28jzcg6y4v9j1.cloudfront.net/2025/05/02/hinh_nen_may_tinh_4k_bien_1_1746181249736.jpg') no-repeat center center fixed;
                background-size: cover;
                position: relative; /* Quan trọng cho lớp phủ */
            }

            /* Lớp phủ màu tối lên trên ảnh nền */
            body::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background-color: rgba(0, 0, 0, 0.4); /* Lớp phủ đen trong suốt */
                z-index: -1; /* Đảm bảo nó nằm dưới nội dung */
            }

            .container {
                width: 90%;
                max-width: 450px;
                padding: 30px;
                background-color: var(--container-bg-color); /* Sử dụng màu trong suốt */
                border-radius: 12px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4); /* Tăng độ bóng */
                text-align: center;
                backdrop-filter: blur(5px); /* Hiệu ứng làm mờ nền xuyên qua */
                -webkit-backdrop-filter: blur(5px); /* Hỗ trợ Safari */
                border: 1px solid rgba(255, 255, 255, 0.2); /* Viền mờ */
            }

            h2 {
                margin-bottom: 35px;
                font-weight: 600;
                font-size: 28px; /* Tăng kích thước tiêu đề */
                color: var(--main-text-color);
                border-bottom: 2px solid var(--button-color);
                padding-bottom: 15px; /* Tăng padding dưới */
                text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5); /* Thêm đổ bóng cho chữ */
            }

            .menu-btn {
                display: flex;
                align-items: center;
                justify-content: flex-start;
                gap: 15px;

                background-color: var(--button-color);
                color: white;
                text-decoration: none;
                padding: 18px 25px;
                margin: 18px 0;
                border-radius: var(--border-radius);
                font-size: 17px;
                font-weight: 500;
                transition: background-color 0.3s ease, transform 0.1s;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* Thêm bóng cho nút */
            }

            .menu-btn:hover {
                background-color: var(--button-hover-color);
                transform: translateY(-3px); /* Hiệu ứng nhấc nhẹ hơn */
            }

            /* === CSS TÙY CHỈNH CHO NÚT ĐĂNG XUẤT === */
            .logout-btn {
                background-color: #dc3545; /* Màu đỏ nổi bật */
                margin-top: 30px; /* Tăng khoảng cách để phân biệt */
            }

            .logout-btn:hover {
                background-color: #bd2130; /* Màu đỏ đậm hơn khi hover */
            }
            /* ======================================= */

            .menu-btn i {
                font-size: 20px;
            }

            footer {
                margin-top: 40px;
                padding-top: 15px;
                font-size: 14px;
                opacity: 0.9;
                border-top: 1px solid rgba(255, 255, 255, 0.3); /* Viền footer mờ hơn */
                text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2><i class="fas fa-tachometer-alt"></i> SamSon Travel - Quản Trị</h2>

            <a href="tourMedia_list" class="menu-btn">
                <i class="fas fa-images"></i> Quản lý hình ảnh tour
            </a>
            <a href="users" class="menu-btn">
                <i class="fas fa-users"></i> Quản lý người dùng
            </a>
            <a href="banner_list" class="menu-btn">
                <i class="fas fa-car-side"></i> Quản lý banner
            </a>
            <a href="booking_list" class="menu-btn">
                <i class="fas fa-calendar-check"></i> Quản lý đơn đặt (Booking)
            </a>

            <a href="${pageContext.request.contextPath}/profile" class="menu-btn">
                <i class="fas fa-user-circle"></i> Hồ sơ cá nhân
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="menu-btn logout-btn">
                <i class="fas fa-sign-out-alt"></i> Đăng xuất
            </a>

            <footer>© 2025 SamSon Travel</footer>
        </div>

    </body>
</html>