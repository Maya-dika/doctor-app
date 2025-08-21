package com.example.doctorapp.service;

import com.example.doctorapp.model.PatientManagement;
import com.example.doctorapp.model.PatientManagement;
import com.example.doctorapp.repository.PatientManagementRepository;
import org.springframework.stereotype.Service;
import com.example.doctorapp.model.Patient;
import java.util.List;
import java.util.Optional;

@Service 
public class PatientManagementService {
    private final PatientManagementRepository patientmanagementRepository;
    
    public PatientManagementService(PatientManagementRepository patientmanagementRepository) {
        this.patientmanagementRepository = patientmanagementRepository;
        
        
    }
    public Optional<PatientManagement> findByEmail(String email) {
        return patientmanagementRepository.findByEmail(email);
    }
    
     public Optional<PatientManagement> findByPhonenumber(String  phoneNumber) {
        return patientmanagementRepository.findByPhoneNumber(phoneNumber);
    }
     
      public List<PatientManagement> findByLastName(String  lastName) {
        return patientmanagementRepository.findByLastName(lastName);
    }
      
     public List<PatientManagement> findByGender(String  gender) {
        return patientmanagementRepository.findByGender(gender);
    }
     
      public List<PatientManagement> findByAdress(String  address) {
        return patientmanagementRepository.findByAddress(address);
    }
      
     public PatientManagement save(PatientManagement patientmanagement) {
        return patientmanagementRepository.save(patientmanagement);
    }
}
