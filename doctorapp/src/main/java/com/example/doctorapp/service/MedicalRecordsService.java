package com.example.doctorapp.service;


import com.example.doctorapp.model.MedicalRecords;
import com.example.doctorapp.repository.MedicalRecordsRepository;
import org.springframework.stereotype.Service;
import com.example.doctorapp.model.Patient;
import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordsService {
    public final MedicalRecordsRepository medicalrecordsRepository;
    
    public MedicalRecordsService(MedicalRecordsRepository medicalrecordsRepository) {
        this.medicalrecordsRepository = medicalrecordsRepository;
    }
    
    public List<MedicalRecords> findByDoctorId(Long doctorId) {
        return medicalrecordsRepository.findByDoctor_Id(doctorId);
    }

    public List<MedicalRecords> findByPatientId(Long patientId) {
        return medicalrecordsRepository.findByPatient_Id(patientId);
    }
    public List<MedicalRecords> findByDiagnosis(String diagnosis) {
        return medicalrecordsRepository.findByDiagnosis(diagnosis);
    }
     public List<MedicalRecords> findByRecordDate(LocalDate recordDate) {
        return medicalrecordsRepository.findByRecordDate(recordDate);
    }

    public MedicalRecords save(MedicalRecords medicalrecords) {
        return medicalrecordsRepository.save(medicalrecords);
    }
    
    
}
