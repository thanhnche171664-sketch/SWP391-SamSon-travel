/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;

public class TransportService {
    private int transportId;
    private int categoryId;
    private String vehicleType;
    private String vehicleName;
    private String description;
    private String pickupLocation;
    private Date departureTime;
    private double price;
    private int capacity;
    private int currentPassengers;
    private Date createdAt;
    private Date updatedAt;

    public TransportService() {}

    public TransportService(int transportId, int categoryId, String vehicleType, String vehicleName, String description, String pickupLocation, Date departureTime, double price, int capacity, int currentPassengers, Date createdAt, Date updatedAt) {
        this.transportId = transportId;
        this.categoryId = categoryId;
        this.vehicleType = vehicleType;
        this.vehicleName = vehicleName;
        this.description = description;
        this.pickupLocation = pickupLocation;
        this.departureTime = departureTime;
        this.price = price;
        this.capacity = capacity;
        this.currentPassengers = currentPassengers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getTransportId() {
        return transportId;
    }

    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentPassengers() {
        return currentPassengers;
    }

    public void setCurrentPassengers(int currentPassengers) {
        this.currentPassengers = currentPassengers;
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