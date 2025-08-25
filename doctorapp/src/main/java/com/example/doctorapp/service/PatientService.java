package com.example.doctorapp.service;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.PatientRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getFemalePatients() {
        return patientRepository.findByGender("female");
    }
    public Optional<Patient> findPatientById(Long id) {
        return patientRepository.findById(id);
    }
    public Optional<Patient> getDoctorByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public List<Patient> getPatientsOlderThan(int age) {
        return patientRepository.findByAgeGreaterThan(age);
    }

    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameIgnoreCase(lastName);
    }

    public Patient authenticatePatient(String email, String hashedPassword) {
    try {
        // Find doctor by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            
            // Check if passwords match
            if (patient.getPassword() != null && patient.getPassword().equals(hashedPassword)) {
                System.out.println("Patient authentication successful for: " + email);
                return patient;
            }
        }
        
        System.out.println("Patient authentication failed for: " + email);
        return null;
        
    } catch (Exception e) {
        System.out.println("Error during doctor authentication: " + e.getMessage());
        return null;
    }
}
    
     public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
