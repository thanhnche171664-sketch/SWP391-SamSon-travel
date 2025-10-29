/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 * ServiceCategory Entity Class
 * Represents service categories in the SamSon Travel system
 * 
 * @author SamSon Travel Team
 */
public class ServiceCategory {
    private int categoryId;
    private String categoryCode;
    private String categoryName;
    private String iconClass;
    private int displayOrder;
    private String description;

    public ServiceCategory() {}

    public ServiceCategory(int categoryId, String categoryCode, String categoryName, 
                         String iconClass, int displayOrder, String description) {
        this.categoryId = categoryId;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.iconClass = iconClass;
        this.displayOrder = displayOrder;
        this.description = description;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ServiceCategory{" +
                "categoryId=" + categoryId +
                ", categoryCode='" + categoryCode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", iconClass='" + iconClass + '\'' +
                ", displayOrder=" + displayOrder +
                ", description='" + description + '\'' +
                '}';
    }
}