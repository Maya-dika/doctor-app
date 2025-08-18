package com.example.doctorapp.repository;

import com.example.doctorapp.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatient_Id(Long patientId);
    
    
    List<Appointment> findByDoctor_Id(Long doctorId);

  @Query("SELECT SUM(d.consultationFee) " +
       "FROM Appointment a " +
       "JOIN a.doctor d " +
       "WHERE a.status = 'booked' AND a.patient.id = :patientId")
Double getTotalBalance(@Param("patientId") Long patientId);

}

