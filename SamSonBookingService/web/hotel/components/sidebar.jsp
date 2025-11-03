<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Sidebar cho Hotel Manager (role_id = 3) -->
<style>
    .hotel-sidebar {
        width: 250px;
        height: 100vh;
        position: fixed;
        left: 0;
        top: 0;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        box-shadow: 2px 0 10px rgba(0,0,0,0.1);
        z-index: 1000;
    }
    
    .hotel-sidebar .logo {
        padding: 20px;
        text-align: center;
        background: rgba(255,255,255,0.1);
        border-bottom: 1px solid rgba(255,255,255,0.2);
    }
    
    .hotel-sidebar .logo h3 {
        color: white;
        margin: 0;
        font-size: 20px;
        font-weight: 600;
    }
    
    .hotel-sidebar .logo p {
        color: rgba(255,255,255,0.8);
        margin: 5px 0 0 0;
        font-size: 13px;
    }
    
    .hotel-sidebar .nav-menu {
        padding: 20px 0;
    }
    
    .hotel-sidebar .nav-item {
        display: block;
        padding: 12px 25px;
        color: rgba(255,255,255,0.9);
        text-decoration: none;
        transition: all 0.3s ease;
        border-left: 3px solid transparent;
        font-size: 15px;
    }
    
    .hotel-sidebar .nav-item:hover {
        background: rgba(255,255,255,0.1);
        border-left-color: #fff;
        color: white;
        padding-left: 30px;
    }
    
    .hotel-sidebar .nav-item.active {
        background: rgba(255,255,255,0.15);
        border-left-color: #fff;
        color: white;
        font-weight: 600;
    }
    
    .hotel-sidebar .nav-item i {
        margin-right: 10px;
        width: 20px;
        text-align: center;
    }
    
    .hotel-sidebar .user-info {
        position: absolute;
        bottom: 0;
        width: 100%;
        padding: 20px;
        background: rgba(0,0,0,0.2);
        border-top: 1px solid rgba(255,255,255,0.1);
    }
    
    .hotel-sidebar .user-info .user-name {
        color: white;
        font-weight: 600;
        font-size: 14px;
        margin-bottom: 5px;
    }
    
    .hotel-sidebar .user-info .user-role {
        color: rgba(255,255,255,0.7);
        font-size: 12px;
    }
    
    .hotel-sidebar .logout-btn {
        display: block;
        width: 100%;
        padding: 8px 15px;
        margin-top: 10px;
        background: rgba(255,255,255,0.2);
        color: white;
        border: 1px solid rgba(255,255,255,0.3);
        border-radius: 5px;
        text-align: center;
        text-decoration: none;
        transition: all 0.3s ease;
        font-size: 13px;
    }
    
    .hotel-sidebar .logout-btn:hover {
        background: rgba(255,255,255,0.3);
        border-color: rgba(255,255,255,0.5);
    }
</style>

<div class="hotel-sidebar">
    <div class="logo">
        <h3>🏨 Hotel Manager</h3>
        <p>Quản lý Khách sạn</p>
    </div>
    
    <nav class="nav-menu">
        <a href="${pageContext.request.contextPath}/hotel/list" 
           class="nav-item ${pageContext.request.servletPath.contains('/hotel/list') || pageContext.request.servletPath.contains('/hotel_list') ? 'active' : ''}">
            <i class="bi bi-list-ul"></i> Danh sách khách sạn
        </a>
        
        <a href="${pageContext.request.contextPath}/hotel/add" 
           class="nav-item ${pageContext.request.servletPath.contains('/hotel/add') || pageContext.request.servletPath.contains('/hotel_add') ? 'active' : ''}">
            <i class="bi bi-plus-circle"></i> Thêm khách sạn
        </a>
        
        <a href="${pageContext.request.contextPath}/profile" 
           class="nav-item ${pageContext.request.servletPath.contains('/profile') ? 'active' : ''}">
            <i class="bi bi-person-circle"></i> Thông tin cá nhân
        </a>
        
<!--        <a href="${pageContext.request.contextPath}/dashboard" 
           class="nav-item ${pageContext.request.servletPath.contains('/dashboard') ? 'active' : ''}">
            <i class="bi bi-speedometer2"></i> Dashboard
        </a>-->
    </nav>
    
    <div class="user-info">
        <div class="user-name">
            <i class="bi bi-person-badge"></i> ${sessionScope.user.name}
        </div>
        <div class="user-role">Hotel Manager</div>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
            <i class="bi bi-box-arrow-right"></i> Đăng xuất
        </a>
    </div>
</div>

<!-- Bootstrap Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
