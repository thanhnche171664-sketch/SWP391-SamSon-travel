<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân - SamSon Travel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding: 20px;
        }

        .profile-container {
            max-width: 1200px;
            margin: 0 auto;
            animation: fadeIn 0.5s ease-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .profile-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
            border: 1px solid rgba(255, 255, 255, 0.18);
            margin-bottom: 30px;
        }

        .profile-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .avatar-container {
            position: relative;
            display: inline-block;
            margin-bottom: 20px;
        }

        .avatar {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
            border: 5px solid white;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .avatar:hover {
            transform: scale(1.05);
            box-shadow: 0 15px 40px rgba(0,0,0,0.4);
        }

        .avatar-upload-overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0,0,0,0.7);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            opacity: 0;
            transition: opacity 0.3s ease;
            cursor: pointer;
        }

        .avatar-upload-overlay:hover {
            opacity: 1;
        }

        .avatar-upload-overlay i {
            color: white;
            font-size: 2rem;
        }

        .tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 30px;
            border-bottom: 2px solid #e0e0e0;
        }

        .tab {
            padding: 15px 30px;
            cursor: pointer;
            border: none;
            background: transparent;
            color: #666;
            font-weight: 500;
            transition: all 0.3s ease;
            border-bottom: 3px solid transparent;
            position: relative;
        }

        .tab:hover {
            color: #667eea;
        }

        .tab.active {
            color: #667eea;
            border-bottom-color: #667eea;
        }

        .tab-content {
            display: none;
            animation: slideDown 0.3s ease-out;
        }

        .tab-content.active {
            display: block;
        }

        @keyframes slideDown {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-label {
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            display: block;
        }

        .form-control {
            border-radius: 10px;
            border: 2px solid #e0e0e0;
            padding: 12px 15px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }

        .btn {
            padding: 12px 30px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: #e0e0e0;
            color: #333;
        }

        .btn-secondary:hover {
            background: #d0d0d0;
        }

        .loading {
            display: none;
        }

        .loading.active {
            display: inline-block;
        }

        .toast {
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 25px;
            border-radius: 10px;
            color: white;
            font-weight: 600;
            z-index: 1000;
            display: none;
            animation: slideInRight 0.3s ease-out;
        }

        @keyframes slideInRight {
            from { transform: translateX(400px); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }

        .toast.success {
            background: #28a745;
        }

        .toast.error {
            background: #dc3545;
        }

        .booking-card {
            background: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 15px;
            border-left: 4px solid #667eea;
            transition: all 0.3s ease;
        }

        .booking-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }

        .status-badge {
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 600;
        }

        .status-pending {
            background: #ffc107;
            color: #333;
        }

        .status-confirmed {
            background: #28a745;
            color: white;
        }

        .status-canceled {
            background: #dc3545;
            color: white;
        }

        @media (max-width: 768px) {
            .profile-card {
                padding: 20px;
            }

            .tabs {
                flex-direction: column;
            }

            .tab {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="profile-container">
        <div class="profile-card">
            <div class="profile-header">
                <h2 class="mb-4"><i class="fas fa-user-circle"></i> Hồ sơ cá nhân</h2>
                
                <div class="avatar-container">
                    <c:choose>
                        <c:when test="${not empty user.avatarUrl}">
                            <img src="${pageContext.request.contextPath}${user.avatarUrl}" alt="Avatar" class="avatar" id="avatarImg">
                        </c:when>
                        <c:otherwise>
                            <img src="https://via.placeholder.com/150" alt="Avatar" class="avatar" id="avatarImg">
                        </c:otherwise>
                    </c:choose>
                    <div class="avatar-upload-overlay" onclick="document.getElementById('avatarInput').click()">
                        <i class="fas fa-camera"></i>
                    </div>
                    <input type="file" id="avatarInput" accept="image/*" style="display: none;" onchange="uploadAvatar()">
                </div>
            </div>

            <div class="tabs">
                <button class="tab active" onclick="switchTab('personal')">
                    <i class="fas fa-user"></i> Thông tin cá nhân
                </button>
                <button class="tab" onclick="switchTab('password')">
                    <i class="fas fa-key"></i> Đổi mật khẩu
                </button>
                <button class="tab" onclick="switchTab('bookings')">
                    <i class="fas fa-history"></i> Lịch sử đặt chỗ
                </button>
            </div>

            <!-- Tab 1: Personal Information -->
            <div id="tab-personal" class="tab-content active">
                <form id="profileForm" method="POST" action="${pageContext.request.contextPath}/update-profile">
                    <input type="hidden" name="action" value="update">
                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">Họ và tên</label>
                            <input type="text" class="form-control" name="name" id="name" value="${user.name}" required>
                        </div>
                        <div class="col-md-6 form-group">
                            <label class="form-label">Email</label>
                            <input type="email" class="form-control" value="${user.email}" disabled>
                            <small class="text-muted">Để thay đổi email, vui lòng liên hệ quản trị viên</small>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 form-group">
                            <label class="form-label">Số điện thoại</label>
                            <input type="tel" class="form-control" name="phone" id="phone" value="${user.phone}">
                        </div>
                        <div class="col-md-6 form-group">
                            <label class="form-label">Giới tính</label>
                            <select class="form-control" name="gender" id="gender">
                                <option value="">Chọn giới tính</option>
                                <option value="male" ${user.gender == 'male' ? 'selected' : ''}>Nam</option>
                                <option value="female" ${user.gender == 'female' ? 'selected' : ''}>Nữ</option>
                                <option value="other" ${user.gender == 'other' ? 'selected' : ''}>Khác</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Địa chỉ</label>
                        <textarea class="form-control" name="address" id="address" rows="3">${user.address}</textarea>
                    </div>

                    <button type="button" class="btn btn-primary" onclick="updateProfile(event)">
                        <i class="fas fa-save"></i> Lưu thay đổi
                    </button>
                </form>
            </div>

            <!-- Tab 2: Change Password -->
            <div id="tab-password" class="tab-content">
                <form id="passwordForm" onsubmit="changePassword(event)">
                    <div class="form-group">
                        <label class="form-label">Mật khẩu hiện tại</label>
                        <input type="password" class="form-control" id="currentPassword" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Mật khẩu mới</label>
                        <input type="password" class="form-control" id="newPassword" required>
                        <small class="text-muted">Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt</small>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Xác nhận mật khẩu mới</label>
                        <input type="password" class="form-control" id="confirmPassword" required>
                    </div>

                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-key"></i> Đổi mật khẩu
                    </button>
                </form>
            </div>

            <!-- Tab 3: Booking History -->
            <div id="tab-bookings" class="tab-content">
                <c:choose>
                    <c:when test="${not empty bookings and bookings.size() > 0}">
                        <c:forEach var="booking" items="${bookings}">
                            <div class="booking-card">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h5>Mã đặt chỗ: #${booking.id}</h5>
                                        <p class="text-muted mb-0">
                                            <fmt:formatDate value="${booking.bookingDate}" pattern="dd/MM/yyyy HH:mm" />
                                        </p>
                                    </div>
                                    <span class="status-badge status-${booking.status}">
                                        <c:if test="${booking.status == 'pending'}">Đang chờ</c:if>
                                        <c:if test="${booking.status == 'confirmed'}">Đã xác nhận</c:if>
                                        <c:if test="${booking.status == 'canceled'}">Đã hủy</c:if>
                                    </span>
                                </div>
                                <div class="mt-3">
                                    <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${booking.totalPrice}" type="currency" currencyCode="VND" /></p>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="fas fa-history fa-4x text-muted mb-3"></i>
                            <p class="text-muted">Bạn chưa có lịch sử đặt chỗ nào</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <div id="toast"></div>

    <script>
        function switchTab(tabName) {
            // Hide all tabs
            document.querySelectorAll('.tab-content').forEach(tab => {
                tab.classList.remove('active');
            });
            document.querySelectorAll('.tab').forEach(tab => {
                tab.classList.remove('active');
            });

            // Show selected tab
            document.getElementById('tab-' + tabName).classList.add('active');
            event.target.classList.add('active');
        }

        function showToast(message, type) {
            const toast = document.getElementById('toast');
            if (!toast) {
                console.error('Toast element not found!');
                return;
            }
            
            toast.textContent = message;
            toast.className = 'toast ' + type;
            toast.style.display = 'block';
            
            setTimeout(() => {
                toast.style.display = 'none';
                toast.className = 'toast';
            }, 5000);
        }

        function updateProfile(event) {
            if (event) event.preventDefault();
            
            console.log('Update profile called');
            
            try {
                // Get form and manually get all values
                const nameInput = document.getElementById('name');
                const phoneInput = document.getElementById('phone');
                const genderSelect = document.getElementById('gender');
                const addressTextarea = document.getElementById('address');
                
                const name = nameInput ? nameInput.value : '';
                const phone = phoneInput ? phoneInput.value : '';
                const gender = genderSelect ? genderSelect.value : '';
                const address = addressTextarea ? addressTextarea.value : '';
                
                console.log('=== MANUAL FORM DATA ===');
                console.log('name:', name);
                console.log('phone:', phone);
                console.log('gender:', gender);
                console.log('address:', address);
                console.log('=== END MANUAL FORM DATA ===');
                
                // Create FormData manually
                const formData = new FormData();
                formData.append('name', name);
                formData.append('phone', phone);
                formData.append('gender', gender);
                formData.append('address', address);
                
                console.log('=== FORMDATA ENTRIES ===');
                for (let pair of formData.entries()) {
                    console.log(pair[0] + ': [' + pair[1] + ']');
                }
                console.log('=== END FORMDATA ===');
                
                // Use URLSearchParams instead for better compatibility
                const params = new URLSearchParams();
                params.append('name', name);
                params.append('phone', phone);
                params.append('gender', gender);
                params.append('address', address);
                
                console.log('=== URLSearchParams ===');
                console.log(params.toString());
                console.log('=== END URLSearchParams ===');
                
                fetch('${pageContext.request.contextPath}/update-profile', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    body: params
                })
                .then(response => {
                    console.log('Response status:', response.status);
                    if (!response.ok) {
                        throw new Error('HTTP error! status: ' + response.status);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Response data:', data);
                    if (data && data.success) {
                        showToast(data.message || 'Cập nhật thông tin thành công', 'success');
                        setTimeout(() => location.reload(), 1500);
                    } else {
                        showToast(data.message || 'Có lỗi xảy ra', 'error');
                    }
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                    showToast('Có lỗi xảy ra khi cập nhật thông tin. Vui lòng thử lại.', 'error');
                });
            } catch (error) {
                console.error('JavaScript error:', error);
                showToast('Có lỗi xảy ra khi cập nhật thông tin', 'error');
            }
        }

        function changePassword(event) {
            event.preventDefault();
            
            try {
                const currentPassword = document.getElementById('currentPassword').value;
                const newPassword = document.getElementById('newPassword').value;
                const confirmPassword = document.getElementById('confirmPassword').value;

                const formData = new FormData();
                formData.append('currentPassword', currentPassword);
                formData.append('newPassword', newPassword);
                formData.append('confirmPassword', confirmPassword);

                fetch('${pageContext.request.contextPath}/change-password', {
                    method: 'POST',
                    body: formData
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        showToast(data.message, 'success');
                        document.getElementById('passwordForm').reset();
                    } else {
                        showToast(data.message, 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showToast('Có lỗi xảy ra khi đổi mật khẩu', 'error');
                });
            } catch (error) {
                console.error('Error:', error);
                showToast('Có lỗi xảy ra khi đổi mật khẩu', 'error');
            }
        }

        function uploadAvatar() {
            try {
                const fileInput = document.getElementById('avatarInput');
                const file = fileInput.files[0];
                
                if (!file) return;

                const formData = new FormData();
                formData.append('avatar', file);

                fetch('${pageContext.request.contextPath}/upload-avatar', {
                    method: 'POST',
                    body: formData
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        document.getElementById('avatarImg').src = '${pageContext.request.contextPath}' + data.avatarUrl;
                        showToast(data.message, 'success');
                    } else {
                        showToast(data.message, 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showToast('Có lỗi xảy ra khi upload ảnh', 'error');
                });
            } catch (error) {
                console.error('Error:', error);
                showToast('Có lỗi xảy ra khi upload ảnh', 'error');
            }
        }
    </script>
</body>
</html>

