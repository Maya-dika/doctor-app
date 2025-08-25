package com.example.doctorapp.repository;

import com.example.doctorapp.model.DoctorAppointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface DrAppRepository extends JpaRepository<DoctorAppointments, Long> {
    
    @Query("SELECT d FROM DoctorAppointments d WHERE d.doctor.id = :doctorId")
    List<DoctorAppointments> findAppointmentsByDoctor(@Param("doctorId") Long doctorId);
    
    List<DoctorAppointments> findByDoctor_Id(Long doctorId);
    
    List<DoctorAppointments> findByPatient_Id(Long patientId);
}

