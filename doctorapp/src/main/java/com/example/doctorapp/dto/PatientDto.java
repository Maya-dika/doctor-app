package com.example.doctorapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

public class PatientDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer age;
    private String gender;
    private String medicalHistory;
    private String fullName;
    private String password;
    
    // Constructors
    public PatientDto() {}
    
    public PatientDto(Long id, String firstName, String lastName, String email, String phoneNumber, 
                     String address, Integer age, String gender, String medicalHistory) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.medicalHistory = medicalHistory;
        this.fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "").trim();
        this.password = "defaultPassword123"; // Default password for new patients
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    @JsonIgnore
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // Helper method to get full name (fallback if fullName is not set)
    @JsonIgnore
    public String getFullNameComputed() {
        if (fullName != null && !fullName.trim().isEmpty()) {
            return fullName;
        }
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "").trim();
    }
}
