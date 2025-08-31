/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.doctorapp.dto;
import java.time.LocalDateTime;
import com.example.doctorapp.model.enums.CallStatus;
import com.example.doctorapp.model.VideoCallRequest;
public class VideoCallResponseDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private CallStatus status;
    private String roomUrl;
    private String roomName;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private String reason;
    
    // Constructor from entity
    public VideoCallResponseDto(VideoCallRequest request) {
        this.id = request.getId();
        this.patientId = request.getPatientId();
        this.doctorId = request.getDoctorId();
        this.status = request.getStatus();
        this.roomUrl = request.getRoomUrl();
        this.roomName = request.getRoomName();
        this.requestedAt = request.getRequestedAt();
        this.approvedAt = request.getApprovedAt();
        this.reason = request.getReason();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    
    public CallStatus getStatus() { return status; }
    public void setStatus(CallStatus status) { this.status = status; }
    
    public String getRoomUrl() { return roomUrl; }
    public void setRoomUrl(String roomUrl) { this.roomUrl = roomUrl; }
    
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
