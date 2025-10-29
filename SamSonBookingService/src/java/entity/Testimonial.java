/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 * Testimonial Entity Class
 * Represents customer reviews and testimonials in the SamSon Travel system
 * 
 * @author SamSon Travel Team
 */
public class Testimonial {
    private int testimonialId;
    private String customerName;
    private String customerEmail;
    private String customerAvatar;
    private Integer tourId;
    private int rating;
    private String reviewText;
    private Timestamp reviewDate;
    private String status;

    public Testimonial() {}

    public Testimonial(int testimonialId, String customerName, String customerEmail, 
                     String customerAvatar, Integer tourId, int rating, String reviewText, 
                     Timestamp reviewDate, String status) {
        this.testimonialId = testimonialId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerAvatar = customerAvatar;
        this.tourId = tourId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.status = status;
    }

    // Getters and Setters
    public int getTestimonialId() {
        return testimonialId;
    }

    public void setTestimonialId(int testimonialId) {
        this.testimonialId = testimonialId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAvatar() {
        return customerAvatar;
    }

    public void setCustomerAvatar(String customerAvatar) {
        this.customerAvatar = customerAvatar;
    }

    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Helper methods
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }

    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isRejected() {
        return "REJECTED".equals(status);
    }

    public String getStarRating() {
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }

    public String getShortReviewText(int maxLength) {
        if (reviewText == null) return "";
        if (reviewText.length() <= maxLength) return reviewText;
        return reviewText.substring(0, maxLength) + "...";
    }

    @Override
    public String toString() {
        return "Testimonial{" +
                "testimonialId=" + testimonialId +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", tourId=" + tourId +
                ", rating=" + rating +
                ", reviewText='" + reviewText + '\'' +
                ", reviewDate=" + reviewDate +
                ", status='" + status + '\'' +
                '}';
    }
}
