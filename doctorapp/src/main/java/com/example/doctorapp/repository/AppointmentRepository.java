package com.example.doctorapp.repository;

import com.example.doctorapp.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient_Id(Long patientId);

    List<Appointment> findByDoctor_Id(Long doctorId);

    @Query("SELECT SUM(d.consultationFee) " +
           "FROM Appointment a " +
           "JOIN a.doctor d " +
           "WHERE a.status = 'CHECKED_IN' AND a.patient.id = :patientId")
    Double getTotalBalance(@Param("patientId") Long patientId);

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    List<Appointment> findByAppointmentDateAndDoctor_Id(LocalDate appointmentDate, Long doctorId);

    List<Appointment> findByAppointmentDateOrderByAppointmentTime(LocalDate appointmentDate);


    // ✅ Corrected query
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.paymentStatus = :paymentStatus")
    List<Appointment> findAppointmentsByPatientAndStatus(@Param("patientId") Long patientId,
                                                         @Param("paymentStatus") String paymentStatus);

    
       @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = :status")
    List<Appointment> findByPatientIdAndStatus(@Param("patientId") Long patientId,
                                                         @Param("status") String status);
    
    List<Appointment> findByPatientId(Long patientId);
    
    
    List<Appointment> findByPatientIdAndStatusAndPaymentStatusIsNull(Long patientId, String status);

    
    List<Appointment> findByDoctor_IdAndStatus(Long doctorId, String status);
    
    
       @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM Appointment a " +
           "WHERE a.patient.id = :patientId " +
           "AND a.doctor.id = :doctorId " +
           "AND a.status = 'COMPLETED'")
    boolean hasCompletedAppointmentWithDoctor(@Param("patientId") Long patientId,
                                              @Param("doctorId") Long doctorId);

    /**
     * Search appointments by patient name, doctor name, date, or status
     */
    @Query("SELECT DISTINCT a FROM Appointment a " +
           "LEFT JOIN a.patient p " +
           "LEFT JOIN a.doctor d " +
           "WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(CONCAT(d.firstName, ' ', d.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.status) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR CAST(a.appointmentDate AS string) LIKE CONCAT('%', :searchTerm, '%') " +
           "OR CAST(a.appointmentTime AS string) LIKE CONCAT('%', :searchTerm, '%')")
    List<Appointment> searchAppointments(@Param("searchTerm") String searchTerm);

}
