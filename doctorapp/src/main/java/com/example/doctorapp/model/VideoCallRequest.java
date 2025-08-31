    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package com.example.doctorapp.model;
    import jakarta.persistence.*;
    import java.time.LocalDateTime;
    import com.example.doctorapp.model.enums.CallStatus;
    @Entity
    @Table(name = "video_call_requests")
    public class VideoCallRequest {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "patient_id", nullable = false)
        private Long patientId;

        @Column(name = "doctor_id", nullable = false)
        private Long doctorId;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private CallStatus status = CallStatus.PENDING;

        @Column(name = "room_name")
        private String roomName;

        @Column(name = "room_url")
        private String roomUrl;

        @Column(name = "requested_at")
        private LocalDateTime requestedAt;

        @Column(name = "approved_at")
        private LocalDateTime approvedAt;

        @Column(name = "call_start_time")
        private LocalDateTime callStartTime;

        @Column(name = "call_end_time")
        private LocalDateTime callEndTime;

        @Column(name = "reason")
        private String reason; // Why patient wants the call

        // Constructors
        public VideoCallRequest() {
            this.requestedAt = LocalDateTime.now();
        }

        public VideoCallRequest(Long patientId, Long doctorId, String reason) {
            this();
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.reason = reason;
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

        public String getRoomName() { return roomName; }
        public void setRoomName(String roomName) { this.roomName = roomName; }

        public String getRoomUrl() { return roomUrl; }
        public void setRoomUrl(String roomUrl) { this.roomUrl = roomUrl; }

        public LocalDateTime getRequestedAt() { return requestedAt; }
        public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

        public LocalDateTime getApprovedAt() { return approvedAt; }
        public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

        public LocalDateTime getCallStartTime() { return callStartTime; }
        public void setCallStartTime(LocalDateTime callStartTime) { this.callStartTime = callStartTime; }

        public LocalDateTime getCallEndTime() { return callEndTime; }
        public void setCallEndTime(LocalDateTime callEndTime) { this.callEndTime = callEndTime; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
