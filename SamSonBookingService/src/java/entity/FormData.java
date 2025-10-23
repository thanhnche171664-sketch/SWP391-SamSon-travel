/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 * Data transfer object for form data
 * Used to hold form input data for redisplay purposes
 * 
 * @author SamSon Travel Team
 */
public class FormData {
    private final String name;
    private final String email;
    private final String phone;
    private final String gender;
    private final String address;
    
    public FormData(String name, String email, String phone, String gender, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
    }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
}
