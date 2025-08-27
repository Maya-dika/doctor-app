package com.example.doctorapp.service;
import com.example.doctorapp.repository.PatientManagementRepository;
import org.springframework.stereotype.Service;
import com.example.doctorapp.model.Patient;
import java.util.List;

@Service 
public class PatientManagementService {
    private final PatientManagementRepository patientmanagementRepository;
    
    public PatientManagementService(PatientManagementRepository patientmanagementRepository) {
        this.patientmanagementRepository = patientmanagementRepository;
        
        
    }
   
      public List<Patient> findPatientsByDoctorId(Long  doctorId) { 
        return patientmanagementRepository.findPatientsByDoctorId(doctorId);
    }
     
     
}
