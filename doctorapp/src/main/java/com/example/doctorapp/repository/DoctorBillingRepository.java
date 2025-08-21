package com.example.doctorapp.repository;
import com.example.doctorapp.model.DoctorBilling;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface DoctorBillingRepository extends JpaRepository<DoctorBilling, Long> {
    
     List<DoctorBilling> findByPatient_Id(Long patientId) ;
    
     List<DoctorBilling> findByPaymentStatus(String status);

   Optional<DoctorBilling> findByTransactionId(String transactionId);
}
