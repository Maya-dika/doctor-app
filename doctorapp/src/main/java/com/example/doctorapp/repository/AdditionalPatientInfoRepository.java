package com.example.doctorapp.repository;

import com.example.doctorapp.model.AdditionalPatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalPatientInfoRepository extends JpaRepository<AdditionalPatientInfo, Long> {
    

    AdditionalPatientInfo findByPatientId(Long patientId);
}
