package com.example.doctorapp.repository;

import com.example.doctorapp.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    // Find doctor by email (for login and duplicate checking)
    Optional<Doctor> findByEmail(String email);
    
    // Find doctor by license number (for duplicate checking)
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
    
    @Query("SELECT d FROM Doctor d WHERE d.specialty.name = :name")
    List<Doctor> findDoctorsBySpecialtyName(@Param("name") String name);
    
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
    
    // FIXED: Get all distinct specialty names as strings (not objects)
    @Query("SELECT DISTINCT s.name FROM Doctor d JOIN d.specialty s WHERE s IS NOT NULL")
    List<String> findDistinctSpecialties();
    
    // ========== NEW ENHANCED METHODS ==========
    
    // Find doctors by specialty name (case-insensitive)
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.specialty.name) LIKE LOWER(CONCAT('%', :specialtyName, '%'))")
    List<Doctor> findBySpecialtyNameContainingIgnoreCase(@Param("specialtyName") String specialtyName);
    
    // Find top experienced doctors by specialty
    @Query("SELECT d FROM Doctor d WHERE d.specialty.name = :specialtyName ORDER BY d.experience DESC")
    List<Doctor> findTopExperiencedDoctorsBySpecialty(@Param("specialtyName") String specialtyName);
    
    // Find doctors by experience range
    @Query("SELECT d FROM Doctor d WHERE d.experience BETWEEN :minExp AND :maxExp ORDER BY d.experience DESC")
    List<Doctor> findByExperienceBetween(@Param("minExp") Integer minExperience, @Param("maxExp") Integer maxExperience);
    
    // Find doctors by multiple criteria (enhanced search)
    @Query("SELECT d FROM Doctor d WHERE " +
           "(:specialtyName IS NULL OR LOWER(d.specialty.name) LIKE LOWER(CONCAT('%', :specialtyName, '%'))) AND " +
           "(:location IS NULL OR LOWER(d.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:minExperience IS NULL OR d.experience >= :minExperience)")
    List<Doctor> findDoctorsByCriteria(@Param("specialtyName") String specialtyName, 
                                      @Param("location") String location, 
                                      @Param("minExperience") Integer minExperience);
    
    // Count doctors by specialty
    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.specialty.name = :specialtyName")
    Long countDoctorsBySpecialty(@Param("specialtyName") String specialtyName);
    
    // Get doctor statistics
    @Query("SELECT d.specialty.name, COUNT(d) FROM Doctor d WHERE d.specialty IS NOT NULL GROUP BY d.specialty.name")
    List<Object[]> getDoctorCountBySpecialty();
    
    // Find doctors available at specific locations (if you have location data)
    @Query("SELECT DISTINCT d.location FROM Doctor d WHERE d.location IS NOT NULL AND d.location != ''")
    List<String> findDistinctLocations();
    
    // Advanced search: Find doctors by keyword in name, specialty, or location
    @Query("SELECT d FROM Doctor d WHERE " +
           "LOWER(CONCAT(d.firstName, ' ', d.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.specialty.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchDoctorsByKeyword(@Param("keyword") String keyword);
    
    // Find recent doctors (if you have creation/registration date)
    // @Query("SELECT d FROM Doctor d ORDER BY d.createdDate DESC")
    // List<Doctor> findRecentDoctors(Pageable pageable);
    
    // Find doctors with minimum rating (if you have rating system)
    // List<Doctor> findByRatingGreaterThanEqual(Double minRating);
    
    // Specialty-specific queries for common medical needs
    @Query("SELECT d FROM Doctor d WHERE d.specialty.name IN ('Cardiology', 'Cardiac Surgery')")
    List<Doctor> findHeartSpecialists();
    
    @Query("SELECT d FROM Doctor d WHERE d.specialty.name IN ('Pediatrics', 'Neonatology')")
    List<Doctor> findChildSpecialists();
    
    @Query("SELECT d FROM Doctor d WHERE d.specialty.name IN ('Emergency Medicine', 'Critical Care')")
    List<Doctor> findEmergencySpecialists();
    
    @Query("SELECT d FROM Doctor d WHERE d.specialty.name IN ('Dermatology', 'Plastic Surgery')")
    List<Doctor> findSkinSpecialists();
    
    @Query("SELECT d FROM Doctor d WHERE d.specialty.name IN ('Neurology', 'Neurosurgery')")
    List<Doctor> findBrainSpecialists();
    
    // Enhanced validation methods
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    
    // Find doctors by partial email (for admin searches)
    List<Doctor> findByEmailContainingIgnoreCase(String emailPart);
}