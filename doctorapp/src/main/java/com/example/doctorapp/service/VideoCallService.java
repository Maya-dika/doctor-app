/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.doctorapp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.example.doctorapp.repository.VideoCallRepository;
import com.example.doctorapp.dto.VideoCallRequestDto;
import com.example.doctorapp.dto.VideoCallResponseDto;
import com.example.doctorapp.model.VideoCallRequest;
import com.example.doctorapp.model.enums.CallStatus;

@Service
@Transactional
public class VideoCallService {
    
    @Autowired
    private VideoCallRepository videoCallRepository;
    
    private static final String JITSI_BASE_URL = "https://meet.jit.si/";
    
    /**
     * Patient requests a video call with a doctor
     */
    public VideoCallResponseDto requestVideoCall(Long patientId, VideoCallRequestDto requestDto) {
        VideoCallRequest request = new VideoCallRequest(
            patientId, 
            requestDto.getDoctorId(), 
            requestDto.getReason()
        );
        
        VideoCallRequest saved = videoCallRepository.save(request);
        return new VideoCallResponseDto(saved);
    }
    
    /**
     * Doctor approves a video call request
     */
    public VideoCallResponseDto approveVideoCall(Long callId, Long doctorId) {
        VideoCallRequest request = videoCallRepository.findById(callId)
            .orElseThrow(() -> new RuntimeException("Video call request not found"));
        
        // Verify doctor owns this request
        if (!request.getDoctorId().equals(doctorId)) {
            throw new RuntimeException("Unauthorized: Doctor can only approve their own requests");
        }
        
        if (request.getStatus() != CallStatus.PENDING) {
            throw new RuntimeException("Call request is not in pending status");
        }
        
        // Generate unique room
        String roomName = generateRoomName(request.getDoctorId(), request.getPatientId());
        String roomUrl = JITSI_BASE_URL + roomName;
        
        request.setStatus(CallStatus.APPROVED);
        request.setRoomName(roomName);
        request.setRoomUrl(roomUrl);
        request.setApprovedAt(LocalDateTime.now());
        
        VideoCallRequest saved = videoCallRepository.save(request);
        return new VideoCallResponseDto(saved);
    }
    
    /**
     * Doctor rejects a video call request
     */
    public VideoCallResponseDto rejectVideoCall(Long callId, Long doctorId) {
        VideoCallRequest request = videoCallRepository.findById(callId)
            .orElseThrow(() -> new RuntimeException("Video call request not found"));
        
        if (!request.getDoctorId().equals(doctorId)) {
            throw new RuntimeException("Unauthorized: Doctor can only reject their own requests");
        }
        
        request.setStatus(CallStatus.REJECTED);
        VideoCallRequest saved = videoCallRepository.save(request);
        return new VideoCallResponseDto(saved);
    }
    
    /**
     * Mark call as started
     */
    public VideoCallResponseDto startCall(Long callId) {
        VideoCallRequest request = videoCallRepository.findById(callId)
            .orElseThrow(() -> new RuntimeException("Video call request not found"));
        
        if (request.getStatus() != CallStatus.APPROVED) {
            throw new RuntimeException("Call must be approved before starting");
        }
        
        request.setStatus(CallStatus.IN_PROGRESS);
        request.setCallStartTime(LocalDateTime.now());
        
        VideoCallRequest saved = videoCallRepository.save(request);
        return new VideoCallResponseDto(saved);
    }
    
    /**
     * Mark call as completed
     */
    public VideoCallResponseDto endCall(Long callId) {
        VideoCallRequest request = videoCallRepository.findById(callId)
            .orElseThrow(() -> new RuntimeException("Video call request not found"));
        
        request.setStatus(CallStatus.COMPLETED);
        request.setCallEndTime(LocalDateTime.now());
        
        VideoCallRequest saved = videoCallRepository.save(request);
        return new VideoCallResponseDto(saved);
    }
    
    /**
     * Get pending requests for a doctor
     */
    public List<VideoCallResponseDto> getPendingRequestsForDoctor(Long doctorId) {
        List<VideoCallRequest> requests = videoCallRepository
            .findByDoctorIdAndStatusOrderByRequestedAtDesc(doctorId, CallStatus.PENDING);
        
        return requests.stream()
            .map(VideoCallResponseDto::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all call history for a patient
     */
    public List<VideoCallResponseDto> getCallHistoryForPatient(Long patientId) {
        List<VideoCallRequest> requests = videoCallRepository
            .findByPatientIdOrderByRequestedAtDesc(patientId);
        
        return requests.stream()
            .map(VideoCallResponseDto::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all call history for a doctor
     */
    public List<VideoCallResponseDto> getCallHistoryForDoctor(Long doctorId) {
        List<VideoCallRequest> requests = videoCallRepository
            .findByDoctorIdOrderByRequestedAtDesc(doctorId);
        
        return requests.stream()
            .map(VideoCallResponseDto::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get a specific call by ID (for joining the room)
     */
    public VideoCallResponseDto getCallById(Long callId) {
        VideoCallRequest request = videoCallRepository.findById(callId)
            .orElseThrow(() -> new RuntimeException("Video call request not found"));
        
        return new VideoCallResponseDto(request);
    }
    
    /**
     * Generate unique room name
     */
    private String generateRoomName(Long doctorId, Long patientId) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return String.format("MedCall-D%d-P%d-%s", doctorId, patientId, timestamp);
    }
}