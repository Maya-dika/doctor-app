package com.example.doctorapp.repository;
import com.example.doctorapp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
