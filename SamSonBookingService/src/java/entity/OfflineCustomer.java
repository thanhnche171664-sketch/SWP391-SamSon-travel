/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;

public class OfflineCustomer {
    private int offlineCustomerId;
    private String fullName;
    private String phone;
    private String email;
    private String idCardNumber;
    private String nationality;
    private String gender;
    private Date dateOfBirth;
    private String address;
    private Date createdAt;

    public OfflineCustomer() {}

    public OfflineCustomer(int offlineCustomerId, String fullName, String phone, String email, String idCardNumber, String nationality, String gender, Date dateOfBirth, String address, Date createdAt) {
        this.offlineCustomerId = offlineCustomerId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.idCardNumber = idCardNumber;
        this.nationality = nationality;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.createdAt = createdAt;
    }

    public int getOfflineCustomerId() {
        return offlineCustomerId;
    }

    public void setOfflineCustomerId(int offlineCustomerId) {
        this.offlineCustomerId = offlineCustomerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    
}
