package com.example.doctorapp.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    // Patient Statistics
    private Long totalPatients;
    private Long newPatientsInPeriod;
    private Long activePatientsInPeriod;
    private Map<String, Long> patientsByGender;
    private Map<String, Long> patientsByAgeGroup;
    
    // Appointment Statistics
    private Long totalAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
    private Long pendingAppointments;
    private Map<String, Long> appointmentsByStatus;
    private Map<String, Long> appointmentsByDoctor;
    private Map<LocalDate, Long> appointmentsByDate;
    
    // Billing Statistics
    private BigDecimal totalRevenue;
    private BigDecimal averageAppointmentCost;
    private BigDecimal totalPaidAmount;
    private BigDecimal totalPendingAmount;
    private Map<String, BigDecimal> revenueByPaymentMethod;
    private Map<LocalDate, BigDecimal> revenueByDate;
    
    // Additional Metrics
    private Double averagePatientsPerDay;
    private Double averageAppointmentsPerDay;
    private Double averageRevenuePerDay;
    private Long totalDoctors;
    private Long totalSpecialties;
    
    // Constructor
    public ReportDto() {}
    
    public ReportDto(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    // Getters and Setters
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public Long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(Long totalPatients) { this.totalPatients = totalPatients; }
    
    public Long getNewPatientsInPeriod() { return newPatientsInPeriod; }
    public void setNewPatientsInPeriod(Long newPatientsInPeriod) { this.newPatientsInPeriod = newPatientsInPeriod; }
    
    public Long getActivePatientsInPeriod() { return activePatientsInPeriod; }
    public void setActivePatientsInPeriod(Long activePatientsInPeriod) { this.activePatientsInPeriod = activePatientsInPeriod; }
    
    public Map<String, Long> getPatientsByGender() { return patientsByGender; }
    public void setPatientsByGender(Map<String, Long> patientsByGender) { this.patientsByGender = patientsByGender; }
    
    public Map<String, Long> getPatientsByAgeGroup() { return patientsByAgeGroup; }
    public void setPatientsByAgeGroup(Map<String, Long> patientsByAgeGroup) { this.patientsByAgeGroup = patientsByAgeGroup; }
    
    public Long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(Long totalAppointments) { this.totalAppointments = totalAppointments; }
    
    public Long getCompletedAppointments() { return completedAppointments; }
    public void setCompletedAppointments(Long completedAppointments) { this.completedAppointments = completedAppointments; }
    
    public Long getCancelledAppointments() { return cancelledAppointments; }
    public void setCancelledAppointments(Long cancelledAppointments) { this.cancelledAppointments = cancelledAppointments; }
    
    public Long getPendingAppointments() { return pendingAppointments; }
    public void setPendingAppointments(Long pendingAppointments) { this.pendingAppointments = pendingAppointments; }
    
    public Map<String, Long> getAppointmentsByStatus() { return appointmentsByStatus; }
    public void setAppointmentsByStatus(Map<String, Long> appointmentsByStatus) { this.appointmentsByStatus = appointmentsByStatus; }
    
    public Map<String, Long> getAppointmentsByDoctor() { return appointmentsByDoctor; }
    public void setAppointmentsByDoctor(Map<String, Long> appointmentsByDoctor) { this.appointmentsByDoctor = appointmentsByDoctor; }
    
    public Map<LocalDate, Long> getAppointmentsByDate() { return appointmentsByDate; }
    public void setAppointmentsByDate(Map<LocalDate, Long> appointmentsByDate) { this.appointmentsByDate = appointmentsByDate; }
    
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public BigDecimal getAverageAppointmentCost() { return averageAppointmentCost; }
    public void setAverageAppointmentCost(BigDecimal averageAppointmentCost) { this.averageAppointmentCost = averageAppointmentCost; }
    
    public BigDecimal getTotalPaidAmount() { return totalPaidAmount; }
    public void setTotalPaidAmount(BigDecimal totalPaidAmount) { this.totalPaidAmount = totalPaidAmount; }
    
    public BigDecimal getTotalPendingAmount() { return totalPendingAmount; }
    public void setTotalPendingAmount(BigDecimal totalPendingAmount) { this.totalPendingAmount = totalPendingAmount; }
    
    public Map<String, BigDecimal> getRevenueByPaymentMethod() { return revenueByPaymentMethod; }
    public void setRevenueByPaymentMethod(Map<String, BigDecimal> revenueByPaymentMethod) { this.revenueByPaymentMethod = revenueByPaymentMethod; }
    
    public Map<LocalDate, BigDecimal> getRevenueByDate() { return revenueByDate; }
    public void setRevenueByDate(Map<LocalDate, BigDecimal> revenueByDate) { this.revenueByDate = revenueByDate; }
    
    public Double getAveragePatientsPerDay() { return averagePatientsPerDay; }
    public void setAveragePatientsPerDay(Double averagePatientsPerDay) { this.averagePatientsPerDay = averagePatientsPerDay; }
    
    public Double getAverageAppointmentsPerDay() { return averageAppointmentsPerDay; }
    public void setAverageAppointmentsPerDay(Double averageAppointmentsPerDay) { this.averageAppointmentsPerDay = averageAppointmentsPerDay; }
    
    public Double getAverageRevenuePerDay() { return averageRevenuePerDay; }
    public void setAverageRevenuePerDay(Double averageRevenuePerDay) { this.averageRevenuePerDay = averageRevenuePerDay; }
    
    public Long getTotalDoctors() { return totalDoctors; }
    public void setTotalDoctors(Long totalDoctors) { this.totalDoctors = totalDoctors; }
    
    public Long getTotalSpecialties() { return totalSpecialties; }
    public void setTotalSpecialties(Long totalSpecialties) { this.totalSpecialties = totalSpecialties; }
}
