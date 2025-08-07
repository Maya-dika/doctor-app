package com.example.doctorapp.service;

import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getPatientsByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }

    public List<Patient> getFemalePatients() {
        return patientRepository.findByGender("female");
    }

    public List<Patient> getPatientsOlderThan(int age) {
        return patientRepository.findByAgeGreaterThan(age);
    }

    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameIgnoreCase(lastName);
    }

    // Add more business logic methods as needed
}
