package com.example.doctorapp.repository;

import com.example.doctorapp.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository

public  interface  PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    List<Prescription> findByPatient_Id(Long patientId);

    List<Prescription> findByDoctor_Id(Long doctorId);

    List<Prescription> findByStatus(String status);
    
    List<Prescription> findByPrescribedDate(LocalDate prescribedDate);
    
    List<Prescription> findByPatientIdOrderByPrescribedDateDesc(Long patientId);

    
    
    
    

    
}
