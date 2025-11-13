<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin khách hàng - SamSon Travel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .main-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            margin: 20px auto;
            overflow: hidden;
        }
        
        .header-section {
            background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        .room-summary {
            background: #f8f9fa;
            padding: 20px;
            border-left: 4px solid #4CAF50;
            margin: 20px 0;
        }
        
        .form-section {
            padding: 30px;
        }
        
        .form-label {
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
        }
        
        .form-control:focus {
            border-color: #4CAF50;
            box-shadow: 0 0 0 0.2rem rgba(76, 175, 80, 0.25);
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
            border: none;
            padding: 12px 30px;
            font-weight: 600;
            border-radius: 8px;
            transition: all 0.3s ease;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(76, 175, 80, 0.4);
        }
        
        .btn-secondary {
            background: #6c757d;
            border: none;
            padding: 12px 30px;
            font-weight: 600;
            border-radius: 8px;
        }
        
        .required {
            color: #dc3545;
        }
        
        .room-type-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.85em;
            font-weight: 600;
        }
        
        .room-type-single { background: #e3f2fd; color: #1976d2; }
        .room-type-double { background: #f3e5f5; color: #7b1fa2; }
        .room-type-dormitory { background: #e8f5e8; color: #388e3c; }
        
        .price-highlight {
            font-size: 1.2em;
            font-weight: bold;
            color: #4CAF50;
        }
        
        .alert {
            border-radius: 8px;
            border: none;
        }
        
        .step-indicator {
            display: flex;
            justify-content: center;
            margin-bottom: 30px;
        }
        
        .step {
            display: flex;
            align-items: center;
            margin: 0 10px;
        }
        
        .step-number {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            background: #e9ecef;
            color: #6c757d;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            margin-right: 8px;
        }
        
        .step.active .step-number {
            background: #4CAF50;
            color: white;
        }
        
        .step.completed .step-number {
            background: #28a745;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="main-container">
                    <!-- Header -->
                    <div class="header-section">
                        <h1><i class="fas fa-user-plus me-2"></i>Thông tin khách hàng</h1>
                        <p class="mb-0">Nhập thông tin khách hàng để hoàn tất đặt phòng</p>
                    </div>
                    
                    <!-- Step Indicator -->
                    <div class="step-indicator">
                        <div class="step completed">
                            <div class="step-number">1</div>
                            <span>Chọn phòng</span>
                        </div>
                        <div class="step active">
                            <div class="step-number">2</div>
                            <span>Thông tin khách hàng</span>
                        </div>
                        <div class="step">
                            <div class="step-number">3</div>
                            <span>Xác nhận & thanh toán</span>
                        </div>
                    </div>
                    
                    <!-- Room Summary -->
                    <c:if test="${not empty roomCartItems}">
                        <div class="room-summary">
                            <h5><i class="fas fa-bed me-2"></i>Phòng đã chọn</h5>
                            <c:forEach var="cartItem" items="${roomCartItems}">
                                <c:set var="room" value="${cartItem.room}"/>
                                <div class="room-item mb-3 p-3" style="background: white; border-radius: 8px; border-left: 4px solid #4CAF50;">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <p><strong>Loại phòng:</strong> 
                                                <span class="room-type-badge room-type-${room.roomType}">
                                                    ${room.roomType == 'single' ? 'Phòng đơn' : 
                                                      room.roomType == 'double' ? 'Phòng đôi' : 'Phòng tập thể'}
                                                </span>
                                            </p>
                                            <p><strong>Giá:</strong> <span class="price-highlight">
                                                <fmt:formatNumber value="${cartItem.pricePerNight}" type="currency" currencyCode="VND"/>
                                            </span>/đêm</p>
                                        </div>
                                        <div class="col-md-6">
                                            <p><strong>Số lượng:</strong> <span class="badge bg-primary">${cartItem.quantity} phòng</span></p>
                                            <p><strong>Số đêm:</strong> ${cartItem.nights} đêm</p>
                                            <p><strong>Thành tiền:</strong> <span class="price-highlight">
                                                <fmt:formatNumber value="${cartItem.subtotal}" type="currency" currencyCode="VND"/>
                                            </span></p>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row mt-3 pt-3" style="border-top: 2px solid #4CAF50;">
                                <div class="col-md-6">
                                    <p><strong>Check-in:</strong> ${checkinDate}</p>
                                    <p><strong>Check-out:</strong> ${checkoutDate}</p>
                                    <p><strong>Số đêm:</strong> ${nights} đêm</p>
                                </div>
                                <div class="col-md-6 text-end">
                                    <p><strong>Tổng số phòng:</strong> <span class="badge bg-success">${totalQuantity} phòng</span></p>
                                    <p><strong>Tổng tiền phòng:</strong> <span class="price-highlight fs-5">
                                        <fmt:formatNumber value="${totalRoomPrice}" type="currency" currencyCode="VND"/>
                                    </span></p>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    
                    <!-- Service Summary -->
                    <div class="room-summary mt-4">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5><i class="fas fa-concierge-bell me-2"></i>Dịch vụ đã chọn</h5>
                            <a href="${pageContext.request.contextPath}/service-list?returnUrl=${pageContext.request.contextPath}/customer-info?checkin=${checkinDate}&checkout=${checkoutDate}" 
                               class="btn btn-outline-primary btn-sm">
                                <i class="fas fa-plus-circle me-1"></i> Thêm dịch vụ
                            </a>
                        </div>
                        
                        <c:if test="${not empty serviceCartItems}">
                            <c:forEach var="service" items="${serviceCartItems}">
                                <div class="room-item mb-2 p-3" style="background: #f8f9fa; border-radius: 8px; border-left: 4px solid #667eea;">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <p><strong>${service.name}</strong></p>
                                            <p class="text-muted small">
                                                <c:if test="${service.type == 'meal'}">
                                                    <i class="fas fa-utensils"></i> Ăn uống
                                                </c:if>
                                                <c:if test="${service.type == 'wellness'}">
                                                    <i class="fas fa-spa"></i> Spa & Wellness
                                                </c:if>
                                            </p>
                                        </div>
                                        <div class="col-md-6 text-end">
                                            <p><strong>Số lượng:</strong> <span class="badge bg-info">${service.quantity}</span></p>
                                            <p><strong>Thành tiền:</strong> <span class="price-highlight">
                                                <fmt:formatNumber value="${service.subtotal}" type="currency" currencyCode="VND"/>
                                            </span></p>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="row mt-3 pt-3" style="border-top: 2px solid #667eea;">
                                <div class="col-md-12 text-end">
                                    <p><strong>Tổng tiền dịch vụ:</strong> <span class="price-highlight">
                                        <fmt:formatNumber value="${totalServicePrice}" type="currency" currencyCode="VND"/>
                                    </span></p>
                                </div>
                            </div>
                        </c:if>
                        
                        <c:if test="${empty serviceCartItems}">
                            <div class="alert alert-info mb-0">
                                <i class="fas fa-info-circle me-2"></i>Chưa có dịch vụ nào được chọn. 
                                <a href="${pageContext.request.contextPath}/service-list?returnUrl=${pageContext.request.contextPath}/customer-info?checkin=${checkinDate}&checkout=${checkoutDate}" class="alert-link">
                                    Nhấn vào đây để thêm dịch vụ
                                </a>
                            </div>
                        </c:if>
                    </div>
                    
                    <!-- Grand Total -->
                    <c:if test="${not empty roomCartItems}">
                        <div class="room-summary mt-3" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white;">
                            <div class="row">
                                <div class="col-md-6">
                                    <h5 class="mb-0"><i class="fas fa-calculator me-2"></i>Tổng thanh toán</h5>
                                </div>
                                <div class="col-md-6 text-end">
                                    <h4 class="mb-0">
                                        <fmt:formatNumber value="${grandTotal}" type="currency" currencyCode="VND"/>
                                    </h4>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    
                    <!-- Error Message -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger mx-3" role="alert">
                            <i class="fas fa-exclamation-triangle me-2"></i>${errorMessage}
                        </div>
                    </c:if>
                    
                    <!-- Customer Info Form -->
                    <div class="form-section">
                        <form method="POST" action="customer-info">
                            <!-- Hidden fields -->
                            <input type="hidden" name="checkinDate" value="${checkinDate}">
                            <input type="hidden" name="checkoutDate" value="${checkoutDate}">
                            <!-- Room cart sẽ được lấy từ session trong doPost -->
                            
                            <div class="row">
                                <!-- Thông tin cơ bản -->
                                <div class="col-md-6">
                                    <h6 class="form-label"><i class="fas fa-user me-2"></i>Thông tin cơ bản</h6>
                                    
                                    <div class="mb-3">
                                        <label for="fullName" class="form-label">Họ và tên <span class="required">*</span></label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" 
                                               placeholder="Nhập họ và tên đầy đủ" required>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="phone" class="form-label">Số điện thoại <span class="required">*</span></label>
                                        <input type="tel" class="form-control" id="phone" name="phone" 
                                               placeholder="0123456789" required>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="email" class="form-label">Email</label>
                                        <input type="email" class="form-control" id="email" name="email" 
                                               placeholder="example@email.com">
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="idCardNumber" class="form-label">Số CMND/CCCD</label>
                                        <input type="text" class="form-control" id="idCardNumber" name="idCardNumber" 
                                               placeholder="123456789">
                                    </div>
                                </div>
                                
                                <!-- Thông tin bổ sung -->
                                <div class="col-md-6">
                                    <h6 class="form-label"><i class="fas fa-info-circle me-2"></i>Thông tin bổ sung</h6>
                                    
                                    <div class="mb-3">
                                        <label for="nationality" class="form-label">Quốc tịch</label>
                                        <select class="form-select" id="nationality" name="nationality">
                                            <option value="">Chọn quốc tịch</option>
                                            <option value="Việt Nam">Việt Nam</option>
                                            <option value="Mỹ">Mỹ</option>
                                            <option value="Hàn Quốc">Hàn Quốc</option>
                                            <option value="Nhật Bản">Nhật Bản</option>
                                            <option value="Trung Quốc">Trung Quốc</option>
                                            <option value="Thái Lan">Thái Lan</option>
                                            <option value="Singapore">Singapore</option>
                                            <option value="Khác">Khác</option>
                                        </select>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="gender" class="form-label">Giới tính</label>
                                        <select class="form-select" id="gender" name="gender">
                                            <option value="">Chọn giới tính</option>
                                            <option value="male">Nam</option>
                                            <option value="female">Nữ</option>
                                            <option value="other">Khác</option>
                                        </select>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="dateOfBirth" class="form-label">Ngày sinh</label>
                                        <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth">
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="address" class="form-label">Địa chỉ</label>
                                        <textarea class="form-control" id="address" name="address" rows="3" 
                                                  placeholder="Nhập địa chỉ thường trú"></textarea>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Action Buttons -->
                            <div class="d-flex justify-content-between mt-4">
                                <div>
                                    <a href="${pageContext.request.contextPath}/offline-home" class="btn btn-secondary me-2">
                                        <i class="fas fa-home me-2"></i>Về trang chủ
                                    </a>
                                    <a href="${pageContext.request.contextPath}/room-list" class="btn btn-secondary">
                                        <i class="fas fa-arrow-left me-2"></i>Quay lại
                                    </a>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-arrow-right me-2"></i>Tiếp tục xác nhận
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Form validation
        document.querySelector('form').addEventListener('submit', function(e) {
            const fullName = document.getElementById('fullName').value.trim();
            const phone = document.getElementById('phone').value.trim();
            
            if (!fullName) {
                e.preventDefault();
                alert('Vui lòng nhập họ và tên khách hàng');
                document.getElementById('fullName').focus();
                return;
            }
            
            if (!phone) {
                e.preventDefault();
                alert('Vui lòng nhập số điện thoại');
                document.getElementById('phone').focus();
                return;
            }
            
            // Basic phone validation
            const phoneRegex = /^[0-9]{10,11}$/;
            if (!phoneRegex.test(phone)) {
                e.preventDefault();
                alert('Số điện thoại phải có 10-11 chữ số');
                document.getElementById('phone').focus();
                return;
            }
        });
        
        // Auto-format phone number
        document.getElementById('phone').addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '');
        });
    </script>
</body>
</html>
