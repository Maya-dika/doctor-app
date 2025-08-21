package com.example.doctorapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.io.Serializable;

@Entity
@Table(name = "medicalrecords")
public class MedicalRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    @ManyToOne  
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    private LocalDate recordDate;
    
    private String recordType; 
    
    private String diagnosis;
    
    private String treatment;
    
    public MedicalRecords(){}
    
    public MedicalRecords(Long id ,  Patient patient, Doctor doctor,LocalDate recordDate, String recordType, String diagnosis,String treatment ){
       this.id=id;
       this.patient=patient;
       this.doctor=doctor;
       this.recordDate=recordDate;
       this.recordType=recordType;
       this.diagnosis=diagnosis;
       this.treatment=treatment;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

}
