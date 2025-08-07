package com.example.doctorapp.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "patients")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;       // foreign key to user table
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer age;
    private String gender;
    private String medicalHistory;

    // Constructors
    public Patient() {}

    public Patient(Long userId, String firstName, String lastName, Integer age, String gender, String medicalHistory, String email, String address,String phoneNumber) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
        this.medicalHistory = medicalHistory;
    }

    // Getters and setters for all fields
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    
    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
}
