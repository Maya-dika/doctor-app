package com.example.doctorapp.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "analytics")
public class Analytics implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private int patientsToday;      
    private int patientsThisWeek;     
    private BigDecimal totalRevenue;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public int getPatientsToday() { return patientsToday; }
    public void setPatientsToday(int patientsToday) { this.patientsToday = patientsToday; }

    public int getPatientsThisWeek() { return patientsThisWeek; }
    public void setPatientsThisWeek(int patientsThisWeek) { this.patientsThisWeek = patientsThisWeek; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}

