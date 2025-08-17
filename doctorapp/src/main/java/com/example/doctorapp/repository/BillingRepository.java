package com.example.doctorapp.repository;

import com.example.doctorapp.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface BillingRepository extends JpaRepository<Billing, Long> {
    
     List<Billing> findByStatus(String status);

    List<Billing> findByPatient_Id(Long patientId);

    Optional<Billing> findByBillNumber(String billNumber);
}
