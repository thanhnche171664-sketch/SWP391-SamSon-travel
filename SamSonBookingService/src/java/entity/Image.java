package entity;

import java.util.Date;

/**
 * Entity class cho bảng Images
 * Lưu nhiều hình ảnh cho Hotel và Room
 */
public class Image {
    private int id;
    private String entityType;  // 'hotel' hoặc 'room'
    private int entityId;       // ID của hotel hoặc room
    private String imageUrl;
    private boolean isPrimary;   // Ảnh chính
    private int displayOrder;    // Thứ tự hiển thị
    private String altText;      // Mô tả ảnh
    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public Image() {}

    public Image(int id, String entityType, int entityId, String imageUrl, 
                 boolean isPrimary, int displayOrder, String altText, 
                 Date createdAt, Date updatedAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.displayOrder = displayOrder;
        this.altText = altText;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", imageUrl='" + imageUrl + '\'' +
                ", isPrimary=" + isPrimary +
                ", displayOrder=" + displayOrder +
                ", altText='" + altText + '\'' +
                '}';
    }
}
