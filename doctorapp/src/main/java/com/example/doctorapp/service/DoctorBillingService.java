package com.example.doctorapp.service;

import com.example.doctorapp.model.DoctorBilling;
import com.example.doctorapp.repository.DoctorBillingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorBillingService {

    private final DoctorBillingRepository doctorBillingRepository;

    public DoctorBillingService(DoctorBillingRepository doctorBillingRepository) {
        this.doctorBillingRepository = doctorBillingRepository;
    }

 
    public DoctorBilling save(DoctorBilling doctorBilling) {
        return doctorBillingRepository.save(doctorBilling);
    }
     public List<DoctorBilling> getBillsByPatient(Long patientId) {
    
        return doctorBillingRepository.findByPatient_Id(patientId);
    }

 
    public DoctorBilling getBillsById(Long id) {
        return doctorBillingRepository.findById(id).orElse(null);
    }

    public List<DoctorBilling> getBillsByStatus(String status) {
        return doctorBillingRepository.findByPaymentStatus(status);
    }

  
    public Optional<DoctorBilling> getBillsByTransactionId(String transactionId) {
        return doctorBillingRepository.findByTransactionId(transactionId);
    }
}
