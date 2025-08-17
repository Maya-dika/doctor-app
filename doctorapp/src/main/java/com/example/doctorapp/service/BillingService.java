package com.example.doctorapp.service;

import com.example.doctorapp.model.Billing;
import com.example.doctorapp.repository.BillingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class BillingService {
    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }
    
       public Billing save(Billing billing ) {
        return billingRepository.save(billing);
    }
       public List<Billing> getBillsByPatient(Long patientId) {
        return billingRepository.findByPatient_Id(patientId);
    }

    public Optional<Billing> getBillsByBillNumber(String billNumber) {
        return billingRepository.findByBillNumber(billNumber);
    }
    public List<Billing> getBillsByStatus(String status) {
        return billingRepository.findByStatus(status);
    }
}
