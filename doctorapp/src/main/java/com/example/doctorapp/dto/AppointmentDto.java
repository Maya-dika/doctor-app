package com.example.doctorapp.dto;

import java.time.LocalTime;
import java.time.LocalDate;
import java.math.BigDecimal;

public class AppointmentDto {
    private Long id;
    private String patientFirstName;
    private String patientLastName;
    private LocalTime time; // Changed from Time to time (lowercase)
    private LocalDate date; // Changed from Date to date (lowercase)
    private String status; // e.g., SCHEDULED, CHECKED_IN, COMPLETED
    private String doctorFirstName;
    private String doctorLastName;
    private BigDecimal consultationFee;
    
    // Added payment-related fields from Appointment model
    private BigDecimal paymentAmount;
    private LocalDate paymentDate;
    private String paymentMethod; // CASH, CREDIT_CARD, DEBIT_CARD, CHECK, INSURANCE
    private String paymentStatus; // UNPAID, PAID, PENDING, COMPLETED, FAILED, REFUNDED

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getpatientFirstName() { return patientFirstName; }
    public void setpatientFirstName(String patientFirstName) { this.patientFirstName = patientFirstName; }
    
    public String getpatientLastName() { return patientLastName; }
    public void setpatientLastName(String patientLastName) { this.patientLastName = patientLastName; }
    
    public String getdoctorFirstName() { return doctorFirstName; }
    public void setdoctorFirstName(String doctorFirstName) { this.doctorFirstName = doctorFirstName; }
    
    public String getdoctorLastName() { return doctorLastName; }
    public void setdoctorLastName(String doctorLastName) { this.doctorLastName = doctorLastName; }
    
    public BigDecimal getconsultationFee() { return consultationFee; }
    public void setconsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }
    
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    // Payment-related getters and setters
    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }
    
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}