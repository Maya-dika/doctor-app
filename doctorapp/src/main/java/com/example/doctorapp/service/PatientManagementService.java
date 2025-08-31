package com.example.doctorapp.service;
import com.example.doctorapp.repository.PatientManagementRepository;
import org.springframework.stereotype.Service;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.AdditionalPatientInfo;
import java.util.List;
import com.example.doctorapp.repository.AdditionalPatientInfoRepository;

@Service 
public class PatientManagementService {
    private final PatientManagementRepository patientmanagementRepository;
    private final AdditionalPatientInfoRepository additionalPatientInfoRepository;
    
    public PatientManagementService(PatientManagementRepository patientmanagementRepository,
                                    AdditionalPatientInfoRepository additionalPatientInfoRepository) {
        this.patientmanagementRepository = patientmanagementRepository;
        this.additionalPatientInfoRepository = additionalPatientInfoRepository;
        
        
    }
   
      public List<Patient> findPatientsByDoctorId(Long  doctorId) { 
        return patientmanagementRepository.findPatientsByDoctorId(doctorId);
    }
      
    public Patient findPatientById(Long patientId) {
        return patientmanagementRepository.findById(patientId).orElse(null);
    }
    public AdditionalPatientInfo findAdditionalInfoByPatientId(Long patientId) {
        return additionalPatientInfoRepository.findByPatientId(patientId);
    }


     
     
}
