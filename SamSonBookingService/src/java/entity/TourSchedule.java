/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 * TourSchedule Entity Class
 * Represents tour schedules and availability in the SamSon Travel system
 * 
 * @author SamSon Travel Team
 */
public class TourSchedule {
    private int scheduleId;
    private int tourId;
    private Timestamp departureDate;
    private Timestamp returnDate;
    private int availableSlots;
    private int bookedSlots;
    private double priceAdjustment;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public TourSchedule() {}

    public TourSchedule(int scheduleId, int tourId, Timestamp departureDate, Timestamp returnDate, 
                       int availableSlots, int bookedSlots, double priceAdjustment, String status, 
                       Timestamp createdAt, Timestamp updatedAt) {
        this.scheduleId = scheduleId;
        this.tourId = tourId;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.availableSlots = availableSlots;
        this.bookedSlots = bookedSlots;
        this.priceAdjustment = priceAdjustment;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public Timestamp getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Timestamp departureDate) {
        this.departureDate = departureDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public int getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(int bookedSlots) {
        this.bookedSlots = bookedSlots;
    }

    public double getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(double priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
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
    public int getRemainingSlots() {
        return availableSlots - bookedSlots;
    }

    public boolean isAvailable() {
        return "ACTIVE".equals(status) && getRemainingSlots() > 0;
    }

    public double getTotalPrice(double basePrice) {
        return basePrice + priceAdjustment;
    }

    @Override
    public String toString() {
        return "TourSchedule{" +
                "scheduleId=" + scheduleId +
                ", tourId=" + tourId +
                ", departureDate=" + departureDate +
                ", returnDate=" + returnDate +
                ", availableSlots=" + availableSlots +
                ", bookedSlots=" + bookedSlots +
                ", priceAdjustment=" + priceAdjustment +
                ", status='" + status + '\'' +
                '}';
    }
}
