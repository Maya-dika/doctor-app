package com.example.doctorapp.config;

import com.example.doctorapp.model.Specialty;
import com.example.doctorapp.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only load data if the specialties table is empty
        if (specialtyRepository.count() == 0) {
            loadSpecialties();
        }
    }

    private void loadSpecialties() {
        // Common medical specialties
        String[] specialties = {
            "General Medicine",
            "Cardiology",
            "Dermatology", 
            "Endocrinology",
            "Gastroenterology",
            "Hematology",
            "Infectious Diseases",
            "Nephrology",
            "Neurology",
            "Oncology",
            "Pulmonology",
            "Rheumatology",
            "General Surgery",
            "Orthopedic Surgery",
            "Neurosurgery",
            "Plastic Surgery",
            "Cardiac Surgery",
            "Pediatrics",
            "Obstetrics and Gynecology",
            "Psychiatry",
            "Radiology",
            "Anesthesiology",
            "Emergency Medicine",
            "Family Medicine",
            "Physical Medicine and Rehabilitation",
            "Pathology",
            "Ophthalmology",
            "Otolaryngology (ENT)",
            "Urology",
            "Allergy and Immunology"
        };

        for (String specialtyName : specialties) {
            Specialty specialty = new Specialty(specialtyName);
            specialtyRepository.save(specialty);
        }

        System.out.println("Loaded " + specialties.length + " specialties into the database.");
    }
}