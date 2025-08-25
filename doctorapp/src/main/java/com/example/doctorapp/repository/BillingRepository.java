package com.example.doctorapp.repository;

import com.example.doctorapp.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    
     List<Billing> findByStatus(String status);

    List<Billing> findByPatient_Id(Long patientId);

    Optional<Billing> findByBillNumber(String billNumber);
    
    // Find bills by patient ID
    List<Billing> findByPatientId(Long patientId);
    
    // Check if bill exists for specific appointment
    boolean existsByAppointmentId(Long appointmentId);
    
    // Find bill by appointment ID
    Billing findByAppointmentId(Long appointmentId);
    
    // Find unpaid bills by patient
    @Query("SELECT b FROM Billing b WHERE b.patient.id = :patientId AND b.status != 'PAID'")
    List<Billing> findUnpaidBillsByPatient(@Param("patientId") Long patientId);
    
    
}
