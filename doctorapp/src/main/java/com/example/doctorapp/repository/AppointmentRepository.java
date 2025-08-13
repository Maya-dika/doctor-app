package com.example.doctorapp.repository;

import com.example.doctorapp.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


import java.util.Optional;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByPatient_Id(Long patientId);
    
    
    List<Appointment> findByDoctor_Id(Long doctorId);
}
