package com.example.doctorapp.service;

import com.example.doctorapp.model.Specialty;
import com.example.doctorapp.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    // Get all specialties
    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }

    // Get specialty by ID
    public Optional<Specialty> getSpecialtyById(Long id) {
        return specialtyRepository.findById(id);
    }

    // Get specialty by name
    public Optional<Specialty> getSpecialtyByName(String name) {
        return specialtyRepository.findByNameIgnoreCase(name);
    }

    // Save or update specialty
    public Specialty saveSpecialty(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }
}
