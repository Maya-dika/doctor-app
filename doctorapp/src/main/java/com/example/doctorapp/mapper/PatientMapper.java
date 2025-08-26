package com.example.doctorapp.mapper;

import com.example.doctorapp.dto.PatientDto;
import com.example.doctorapp.model.Patient;

public class PatientMapper {
    
    public static PatientDto toDto(Patient patient) {
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(patient.getEmail());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setAddress(patient.getAddress());
        dto.setAge(patient.getAge());
        dto.setGender(patient.getGender());
        dto.setMedicalHistory(patient.getMedicalHistory());
        
        // Set the full name
        dto.setFullName((patient.getFirstName() != null ? patient.getFirstName() : "") + 
                       " " + (patient.getLastName() != null ? patient.getLastName() : "").trim());
        
        // Set the password (but don't expose it in API responses for security)
        dto.setPassword(patient.getPassword());
        
        return dto;
    }
    
    public static Patient toEntity(PatientDto dto) {
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setAddress(dto.getAddress());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setMedicalHistory(dto.getMedicalHistory());
        
        // Set password - use provided password or default for new patients
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            patient.setPassword(dto.getPassword());
        } else if (dto.getId() == null) {
            // Set a default password for new patients (they can change it later)
            patient.setPassword("defaultPassword123");
        }
        
        return patient;
    }
}
