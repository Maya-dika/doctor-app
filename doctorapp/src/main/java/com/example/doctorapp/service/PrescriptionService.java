package com.example.doctorapp.service;

import com.example.doctorapp.model.Prescription;
import com.example.doctorapp.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Service

public class PrescriptionService {
    
    private final PrescriptionRepository prescriptionRepository;
    
     public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }
     public Prescription save(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }
    

     // In PrescriptionService
public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
    // This should return prescriptions with doctor information populated
    return prescriptionRepository.findByPatientIdOrderByPrescribedDateDesc(patientId);
}
   
    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        return prescriptionRepository.findByDoctor_Id(doctorId);
    }

   
    public List<Prescription> getPrescriptionsByStatus(String status) {
        return prescriptionRepository.findByStatus(status);
    }

    
    public List<Prescription> getPrescriptionsByPrescribedDate(LocalDate prescribedDate) {
        return prescriptionRepository.findByPrescribedDate(prescribedDate);
    }
    
    
    
    
}