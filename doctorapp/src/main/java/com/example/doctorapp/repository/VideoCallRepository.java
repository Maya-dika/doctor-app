/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.doctorapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.doctorapp.model.VideoCallRequest;
import com.example.doctorapp.model.enums.CallStatus;
import java.time.LocalDateTime;
import java.util.List;


public interface VideoCallRepository extends JpaRepository<VideoCallRequest, Long> {
    List<VideoCallRequest> findByPatientIdOrderByRequestedAtDesc(Long patientId);
    List<VideoCallRequest> findByDoctorIdOrderByRequestedAtDesc(Long doctorId);
    List<VideoCallRequest> findByDoctorIdAndStatusOrderByRequestedAtDesc(Long doctorId, CallStatus status);
    List<VideoCallRequest> findByPatientIdAndStatusOrderByRequestedAtDesc(Long patientId, CallStatus status);
    
    @Query("SELECT v FROM VideoCallRequest v WHERE v.status = :status AND v.requestedAt > :since")
    List<VideoCallRequest> findRecentCallsByStatus(@Param("status") CallStatus status, @Param("since") LocalDateTime since);
}
