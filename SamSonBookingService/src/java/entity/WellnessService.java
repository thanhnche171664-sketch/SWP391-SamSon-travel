/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;

public class WellnessService {
    private int wellnessId;
    private int hotelId;
    private int categoryId;
    private String serviceName;
    private String description;
    private double basePrice;
    private Integer durationMinutes;
    private String operatingHours;
    private Integer capacity;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public WellnessService() {}

    public WellnessService(int wellnessId, int hotelId, int categoryId, String serviceName, String description, double basePrice, Integer durationMinutes, String operatingHours, Integer capacity, String status, Date createdAt, Date updatedAt) {
        this.wellnessId = wellnessId;
        this.hotelId = hotelId;
        this.categoryId = categoryId;
        this.serviceName = serviceName;
        this.description = description;
        this.basePrice = basePrice;
        this.durationMinutes = durationMinutes;
        this.operatingHours = operatingHours;
        this.capacity = capacity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getWellnessId() {
        return wellnessId;
    }

    public void setWellnessId(int wellnessId) {
        this.wellnessId = wellnessId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    
}