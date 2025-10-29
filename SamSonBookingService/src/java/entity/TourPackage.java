/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 * TourPackage Entity Class
 * Represents tour packages with different service tiers in the SamSon Travel system
 * 
 * @author SamSon Travel Team
 */
public class TourPackage {
    private int packageId;
    private int tourId;
    private String packageName;
    private String description;
    private double price;
    private Integer hotelId;
    private Integer transportId;
    private boolean includesMeals;
    private boolean includesWellness;
    private String packageType;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public TourPackage() {}

    public TourPackage(int packageId, int tourId, String packageName, String description, 
                      double price, Integer hotelId, Integer transportId, boolean includesMeals, 
                      boolean includesWellness, String packageType, String status, 
                      Timestamp createdAt, Timestamp updatedAt) {
        this.packageId = packageId;
        this.tourId = tourId;
        this.packageName = packageName;
        this.description = description;
        this.price = price;
        this.hotelId = hotelId;
        this.transportId = transportId;
        this.includesMeals = includesMeals;
        this.includesWellness = includesWellness;
        this.packageType = packageType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getTransportId() {
        return transportId;
    }

    public void setTransportId(Integer transportId) {
        this.transportId = transportId;
    }

    public boolean isIncludesMeals() {
        return includesMeals;
    }

    public void setIncludesMeals(boolean includesMeals) {
        this.includesMeals = includesMeals;
    }

    public boolean isIncludesWellness() {
        return includesWellness;
    }

    public void setIncludesWellness(boolean includesWellness) {
        this.includesWellness = includesWellness;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isLuxury() {
        return "LUXURY".equals(packageType);
    }

    public boolean isPremium() {
        return "PREMIUM".equals(packageType);
    }

    public boolean isStandard() {
        return "STANDARD".equals(packageType);
    }

    public boolean isBasic() {
        return "BASIC".equals(packageType);
    }

    public String getFormattedPrice() {
        return String.format("%,.0f VNĐ", price);
    }

    @Override
    public String toString() {
        return "TourPackage{" +
                "packageId=" + packageId +
                ", tourId=" + tourId +
                ", packageName='" + packageName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", hotelId=" + hotelId +
                ", transportId=" + transportId +
                ", includesMeals=" + includesMeals +
                ", includesWellness=" + includesWellness +
                ", packageType='" + packageType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
