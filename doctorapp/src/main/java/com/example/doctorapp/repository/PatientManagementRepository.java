package com.example.doctorapp.repository;

import com.example.doctorapp.model.PatientManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PatientManagementRepository extends JpaRepository<PatientManagement, Long> {
    
    Optional<PatientManagement> findByEmail(String email);
    boolean existsByEmail(String email);

    
    Optional<PatientManagement> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);


    List<PatientManagement> findByLastName(String lastName);
    
    List<PatientManagement> findByGender(String gender);

   
    List<PatientManagement> findByAddress(String address);

    
}
