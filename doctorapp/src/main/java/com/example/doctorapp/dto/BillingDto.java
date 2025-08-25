package com.example.doctorapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BillingDto {
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal pendingAmount;
    private String status; // PENDING, PARTIALLY_PAID, PAID, OVERDUE
    private LocalDate billingDate;
    private LocalDate dueDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public BillingDto() {}
    
    public BillingDto(Long appointmentId, Long patientId, BigDecimal totalAmount) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.totalAmount = totalAmount;
        this.paidAmount = BigDecimal.ZERO;
        this.pendingAmount = totalAmount;
        this.status = "PENDING";
        this.billingDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(30);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
    public String getPatientFirstName() {
        return patientFirstName;
    }
    
    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }
    
    public String getPatientLastName() {
        return patientLastName;
    }
    
    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
        this.pendingAmount = this.totalAmount.subtract(paidAmount);
        updateStatus();
    }
    
    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }
    
    public void setPendingAmount(BigDecimal pendingAmount) {
        this.pendingAmount = pendingAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDate getBillingDate() {
        return billingDate;
    }
    
    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    private void updateStatus() {
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = "PENDING";
        } else if (paidAmount.compareTo(totalAmount) == 0) {
            this.status = "PAID";
        } else {
            this.status = "PARTIALLY_PAID";
        }
        
        // Check if overdue
        if (LocalDate.now().isAfter(dueDate) && !this.status.equals("PAID")) {
            this.status = "OVERDUE";
        }
    }
    
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate) && !status.equals("PAID");
    }
    
    public String getFormattedTotalAmount() {
        return String.format("$%.2f", totalAmount);
    }
    
    public String getFormattedPaidAmount() {
        return String.format("$%.2f", paidAmount);
    }
    
    public String getFormattedPendingAmount() {
        return String.format("$%.2f", pendingAmount);
    }
}