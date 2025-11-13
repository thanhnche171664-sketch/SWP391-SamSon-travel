<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String icon = request.getParameter("icon");
    String category = request.getParameter("category");
    String name = request.getParameter("name");
    String desc = request.getParameter("desc");
    String price = request.getParameter("price");
    String details = request.getParameter("details");
    String color = request.getParameter("color");
    String serviceId = request.getParameter("serviceId");
    String serviceType = request.getParameter("serviceType");
    String[] detailList = details != null ? details.split("\\|") : new String[0];
%>
<div class="service-card" data-category="<%= category %>" data-service-type="<%= serviceType %>" data-service-id="<%= serviceId %>">
    <div class="service-icon-wrapper <%= color %>">
        <i class="<%= icon %> service-icon"></i>
        <span class="service-status-badge">Có sẵn</span>
    </div>
    <div class="service-info">
        <div class="service-header">
            <div class="service-name"><%= name %></div>
            <div class="service-category"><%= category %></div>
        </div>
        <div class="service-description"><%= desc %></div>
        <div class="service-price"><%= price %></div>

        <div class="service-details">
            <% for (String d : detailList) { %>
                <div class="service-detail-item">
                    <i class="fas fa-info-circle"></i>
                    <span><%= d %></span>
                </div>
            <% } %>
        </div>

        <div class="service-actions">
            <button class="btn-action btn-add-to-cart" onclick="addToCart(this)">
                <i class="fas fa-plus"></i> Thêm vào giỏ
            </button>
        </div>
    </div>
</div>
