package com.example.doctorapp.repository;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    // Find doctor by email (for login and duplicate checking)
    Optional<Doctor> findByEmail(String email);
    
    // Find doctor by license number (for duplicate checking)
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    
    // Find doctors by specialty (matching your service)
    List<Doctor> findBySpecialty(Specialty specialty);
    
    // Find doctors by specialty ID
    List<Doctor> findBySpecialtyId(Long specialtyId);
    
    // Find doctors by location (matching your service)
    List<Doctor> findByLocation(String location);
    
    // Find doctors by location containing text (more flexible search)
    List<Doctor> findByLocationContainingIgnoreCase(String location);
    
    // Find doctors by last name (matching your service)
    List<Doctor> findByLastName(String lastName);
    
    // Find doctors by exact experience (matching your service)
    List<Doctor> findByExperience(Integer experience);
    
    // Find doctors by experience range (more useful)
    List<Doctor> findByExperienceGreaterThanEqual(Integer experience);
    
    // Find doctors by first name
    List<Doctor> findByFirstName(String firstName);
    
    // Find doctors by full name search
    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    // Check if email exists (for validation)
    boolean existsByEmail(String email);
    
    // Check if license number exists (for validation)
    boolean existsByLicenseNumber(String licenseNumber);
    
    
}