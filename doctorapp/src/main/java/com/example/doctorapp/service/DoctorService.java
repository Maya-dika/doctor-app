package com.example.doctorapp.service;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.repository.DoctorRepository;
import com.example.doctorapp.model.Specialty;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class DoctorService {
    
    private final DoctorRepository doctorRepository;
    
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    
    // Save or update doctor
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    
    // Find doctor by ID
    public Optional<Doctor> findDoctorById(Long id) {
        return doctorRepository.findById(id);
    }
    
    // Find all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    
    // Search methods from your original service
  public List<Doctor> getDoctorsBySpecialty(String specialtyName) {
    return doctorRepository.findDoctorsBySpecialtyName(specialtyName);
}
    
    public List<Doctor> getDoctorsBySpecialtyId(Long specialtyId) {
        return doctorRepository.findBySpecialtyId(specialtyId);
    }
    
    public List<Doctor> getDoctorsByLocation(String location) {
        return doctorRepository.findByLocation(location);
    }
    
    // More flexible location search
    public List<Doctor> searchDoctorsByLocation(String location) {
        return doctorRepository.findByLocationContainingIgnoreCase(location);
    }
    
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }
    
    public List<Doctor> getDoctorsByLastName(String lastName) {
        return doctorRepository.findByLastName(lastName);
    }
    
    public List<Doctor> getDoctorsByFirstName(String firstName) {
        return doctorRepository.findByFirstName(firstName);
    }
    
    // Search by name (first or last name containing the search term)
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }
    
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
    
    public List<Doctor> getDoctorsByExperience(Integer experience) {
        return doctorRepository.findByExperience(experience);
    }
    
    // Find doctors with minimum experience
    public List<Doctor> getDoctorsWithMinimumExperience(Integer minExperience) {
        return doctorRepository.findByExperienceGreaterThanEqual(minExperience);
    }
    
    // Validation methods
    public boolean isEmailAvailable(String email) {
        return !doctorRepository.existsByEmail(email);
    }
    
    public boolean isLicenseNumberAvailable(String licenseNumber) {
        return !doctorRepository.existsByLicenseNumber(licenseNumber);
    }
    
    // Delete doctor
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
    
    // Update doctor
    public Doctor updateDoctor(Doctor doctor) {
        if (doctor.getId() != null && doctorRepository.existsById(doctor.getId())) {
            return doctorRepository.save(doctor);
        }
        throw new IllegalArgumentException("Doctor with ID " + doctor.getId() + " does not exist");
    }
    
    // Advanced search method
    public List<Doctor> searchDoctors(String name, String location, Long specialtyId, Integer minExperience) {
        // This would require a custom query or specification
        // For now, returning all doctors - you can implement custom search logic
        return doctorRepository.findAll();
    }
    
    public Doctor authenticateDoctor(String email, String hashedPassword) {
    try {
        // Find doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            
            // Check if passwords match
            if (doctor.getPassword() != null && doctor.getPassword().equals(hashedPassword)) {
                System.out.println("Doctor authentication successful for: " + email);
                return doctor;
            }
        }
        
        System.out.println("Doctor authentication failed for: " + email);
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
