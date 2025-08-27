//package com.example.doctorapp.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDate;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "patientmanagement")
//public class PatientManagement implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String firstName;
//    private String lastName;
//    private LocalDate dateOfBirth;
//    private String gender;
//    private String phoneNumber;
//    private String email;
//    private String address;
//
//    public PatientManagement() {}
//
//    public PatientManagement(Long id, String firstName, String lastName,
//                             LocalDate dateOfBirth, String gender,
//                             String phoneNumber, String email, String address) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.dateOfBirth = dateOfBirth;
//        this.gender = gender;
//        this.phoneNumber = phoneNumber;
//        this.email = email;
//        this.address = address;
//    }
//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public LocalDate getDateOfBirth() {
//        return dateOfBirth;
//    }
//    public void setDateOfBirth(LocalDate dateOfBirth) {
//        this.dateOfBirth = dateOfBirth;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//    public void setAddress(String address) {
//        this.address = address;
//    }
//}
