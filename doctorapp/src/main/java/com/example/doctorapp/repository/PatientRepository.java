package com.example.doctorapp.repository;
import com.example.doctorapp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find all patients of a specific gender
    List<Patient> findByGender(String gender);

    // Find patients older than a certain age
    List<Patient> findByAgeGreaterThan(Integer age);

    // Find patients by last name ignoring case
    List<Patient> findByLastNameIgnoreCase(String lastName);
    
    Optional<Patient> findByEmail(String email);
    
    // Find patients by first name or last name containing the search term (case insensitive)
    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    // Find patients by email containing the search term (case insensitive)
    List<Patient> findByEmailContainingIgnoreCase(String email);
    
    // Find patients by phone number containing the search term
    List<Patient> findByPhoneNumberContaining(String phoneNumber);
    
    // Find patients by full name (first name + space + last name) containing the search term (case insensitive)
    @Query("SELECT p FROM Patient p WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<Patient> findByFullNameContainingIgnoreCase(@Param("fullName") String fullName);
}
