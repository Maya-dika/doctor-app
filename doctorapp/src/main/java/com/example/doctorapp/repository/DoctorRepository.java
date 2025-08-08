package com.example.doctorapp.repository;
import com.example.doctorapp.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface DoctorRepository extends JpaRepository<Doctor, Long>{

    List<Doctor> findByUserId(Long userId);

    List<Doctor> findBySpecialtyId(Long specialtyId);

    List<Doctor> findByLocation(String location);

    List<Doctor> findByLastName(String lastName);
    
    List<Doctor> findByEmail(String email);
    
    List<Doctor> findByExperience(int experience);
}
