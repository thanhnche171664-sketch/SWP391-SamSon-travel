/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 * TourItinerary Entity Class
 * Represents detailed day-by-day tour itineraries in the SamSon Travel system
 * 
 * @author SamSon Travel Team
 */
public class TourItinerary {
    private int itineraryId;
    private int tourId;
    private int dayNumber;
    private String title;
    private String description;
    private String activities;
    private String accommodation;
    private String mealsIncluded;
    private String transportInfo;
    private Timestamp createdAt;

    public TourItinerary() {}

    public TourItinerary(int itineraryId, int tourId, int dayNumber, String title, String description, 
                        String activities, String accommodation, String mealsIncluded, 
                        String transportInfo, Timestamp createdAt) {
        this.itineraryId = itineraryId;
        this.tourId = tourId;
        this.dayNumber = dayNumber;
        this.title = title;
        this.description = description;
        this.activities = activities;
        this.accommodation = accommodation;
        this.mealsIncluded = mealsIncluded;
        this.transportInfo = transportInfo;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getMealsIncluded() {
        return mealsIncluded;
    }

    public void setMealsIncluded(String mealsIncluded) {
        this.mealsIncluded = mealsIncluded;
    }

    public String getTransportInfo() {
        return transportInfo;
    }

    public void setTransportInfo(String transportInfo) {
        this.transportInfo = transportInfo;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean hasAccommodation() {
        return accommodation != null && !accommodation.trim().isEmpty() && !"Không".equals(accommodation);
    }

    public boolean hasMeals() {
        return mealsIncluded != null && !mealsIncluded.trim().isEmpty();
    }

    public boolean hasTransport() {
        return transportInfo != null && !transportInfo.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "TourItinerary{" +
                "itineraryId=" + itineraryId +
                ", tourId=" + tourId +
                ", dayNumber=" + dayNumber +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", accommodation='" + accommodation + '\'' +
                ", mealsIncluded='" + mealsIncluded + '\'' +
                ", transportInfo='" + transportInfo + '\'' +
                '}';
    }
}
