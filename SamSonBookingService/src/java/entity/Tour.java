/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 * Tour Entity Class
 * Represents tour packages in the SamSon Travel system
 * 
 * @author SamSon Travel Team
 */
public class Tour {
    private int tourId;
    private String tourName;
    private String description;
    private int durationDays;
    private int durationNights;
    private double basePrice;
    private String featuredImage;
    private String status;
    private String highlights;
    private String inclusions;
    private String exclusions;
    private String difficultyLevel;
    private int minAge;
    private int maxGroupSize;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Tour() {}

    public Tour(int tourId, String tourName, String description, int durationDays, int durationNights, 
                double basePrice, String featuredImage, String status, String highlights, String inclusions, 
                String exclusions, String difficultyLevel, int minAge, int maxGroupSize, 
                Timestamp createdAt, Timestamp updatedAt) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.description = description;
        this.durationDays = durationDays;
        this.durationNights = durationNights;
        this.basePrice = basePrice;
        this.featuredImage = featuredImage;
        this.status = status;
        this.highlights = highlights;
        this.inclusions = inclusions;
        this.exclusions = exclusions;
        this.difficultyLevel = difficultyLevel;
        this.minAge = minAge;
        this.maxGroupSize = maxGroupSize;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getDurationNights() {
        return durationNights;
    }

    public void setDurationNights(int durationNights) {
        this.durationNights = durationNights;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public String getExclusions() {
        return exclusions;
    }

    public void setExclusions(String exclusions) {
        this.exclusions = exclusions;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
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

    @Override
    public String toString() {
        return "Tour{" +
                "tourId=" + tourId +
                ", tourName='" + tourName + '\'' +
                ", description='" + description + '\'' +
                ", durationDays=" + durationDays +
                ", durationNights=" + durationNights +
                ", basePrice=" + basePrice +
                ", featuredImage='" + featuredImage + '\'' +
                ", status='" + status + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", minAge=" + minAge +
                ", maxGroupSize=" + maxGroupSize +
                '}';
    }
}
