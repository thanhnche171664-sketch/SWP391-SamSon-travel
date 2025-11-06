<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="header" id="header" role="banner">
    <div class="header-container">
        <a href="${pageContext.request.contextPath}/home" class="logo" aria-label="SamSon Travel - Go to homepage">
            <img src="${pageContext.request.contextPath}/assets/images/icons/logo.svg" alt="SamSon Travel Logo" class="logo-icon">
            <span>SamSon Travel</span>
        </a>
        <nav class="nav" id="nav" role="navigation" aria-label="Main navigation">
            <ul class="nav-list" id="navigation">
                <li class="nav-item">
                    <a href="#home" class="nav-link active" aria-current="page">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a href="#hotels" class="nav-link">Khách sạn</a>
                </li>
                <li class="nav-item">
                    <a href="#destinations" class="nav-link">Điểm đến</a>
                </li>
                <li class="nav-item">
                    <a href="#about" class="nav-link">Về chúng tôi</a>
                </li>
                <li class="nav-item">
                    <a href="#contact" class="nav-link">Liên hệ</a>
                </li>
            </ul>
            <div class="user-menu">
                <c:choose>
                    <c:when test="${currentUser != null}">
                        <div class="user-dropdown">
                            <img src="${pageContext.request.contextPath}/uploads/avatars/${currentUser.avatarUrl != null ? currentUser.avatarUrl : 'default-avatar.jpg'}" 
                                 alt="Avatar" class="user-avatar" id="userAvatar">
                            <div class="user-dropdown-menu">
                                <a href="${pageContext.request.contextPath}/profile" class="dropdown-item">
                                    <i class="fas fa-user"></i> Hồ sơ
                                </a>
                                <c:if test="${userRole == 'ADMINISTRATOR' || userRole == 'SERVICE_MANAGER'}">
                                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="dropdown-item">
                                        <i class="fas fa-cog"></i> Quản trị
                                    </a>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/logout" class="dropdown-item">
                                    <i class="fas fa-sign-out-alt"></i> Đăng xuất
                                </a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="auth-buttons">
                            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline btn-sm">Đăng nhập</a>
                            <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary btn-sm">Đăng ký</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </nav>
        <button class="mobile-menu-toggle" id="mobileMenuToggle" aria-label="Toggle mobile menu" aria-expanded="false" aria-controls="nav">
            <span class="sr-only">Menu</span>
            <span></span>
            <span></span>
            <span></span>
        </button>
    </div>
</header>

