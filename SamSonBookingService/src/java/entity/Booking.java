/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;
import java.util.List;

public class Booking {
    private int id;
    private Integer userId;
    private Integer tourId;
    private Integer scheduleId;
    private Integer packageId;
    private Integer guestCount;
    private Integer hotelId;
    private String roomType;
    private int numberOfRooms;
    private Integer transportId;
    private double transportFee;
    private Double subtotal;
    private Double discountTotal;
    private Double taxTotal;
    private double totalPrice;
    private String currency;
    private Date bookingDate;
    private String bookingSource;
    private Integer createdBy;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private Date checkInDate;
    private Date checkOutDate;
    private List<BookingDetail> bookingDetails;
    private List<BookingItem> bookingItems;

    public Booking() {}

    public Booking(int id, Integer userId, Integer hotelId, String roomType, int numberOfRooms, Integer transportId, double transportFee, double totalPrice, Date bookingDate, String bookingSource, Integer createdBy, String status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
        this.transportId = transportId;
        this.transportFee = transportFee;
        this.totalPrice = totalPrice;
        this.bookingDate = bookingDate;
        this.bookingSource = bookingSource;
        this.createdBy = createdBy;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Integer getTransportId() {
        return transportId;
    }

    public void setTransportId(Integer transportId) {
        this.transportId = transportId;
    }

    public double getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(double transportFee) {
        this.transportFee = transportFee;
    }

    public Integer getTourId() { return tourId; }
    public void setTourId(Integer tourId) { this.tourId = tourId; }

    public Integer getScheduleId() { return scheduleId; }
    public void setScheduleId(Integer scheduleId) { this.scheduleId = scheduleId; }

    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }

    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getDiscountTotal() { return discountTotal; }
    public void setDiscountTotal(Double discountTotal) { this.discountTotal = discountTotal; }

    public Double getTaxTotal() { return taxTotal; }
    public void setTaxTotal(Double taxTotal) { this.taxTotal = taxTotal; }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingSource() {
        return bookingSource;
    }

    public void setBookingSource(String bookingSource) {
        this.bookingSource = bookingSource;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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

    public List<BookingDetail> getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(List<BookingDetail> bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

    public List<BookingItem> getBookingItems() { return bookingItems; }
    public void setBookingItems(List<BookingItem> bookingItems) { this.bookingItems = bookingItems; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public Date getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }

    public Date getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }
}
