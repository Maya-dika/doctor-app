package com.example.doctorapp.repository;

import com.example.doctorapp.model.MedicalRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public  interface MedicalRecordsRepository extends JpaRepository<MedicalRecords, Long>{
    
    List<MedicalRecords> findByDoctor_Id(Long doctorId);
    
    List<MedicalRecords> findByPatient_Id(Long patientId);
    
    List<MedicalRecords>findByDiagnosis(String diagnosis);
    
    List<MedicalRecords> findByRecordDate(LocalDate recordDate);
    
}
