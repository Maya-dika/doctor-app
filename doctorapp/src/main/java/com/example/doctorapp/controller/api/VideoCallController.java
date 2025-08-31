package com.example.doctorapp.controller.api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.doctorapp.service.VideoCallService;
import com.example.doctorapp.dto.VideoCallResponseDto;
import com.example.doctorapp.dto.VideoCallRequestDto;
import java.util.List;
@RestController
@RequestMapping("/api/video-calls")
@CrossOrigin(origins = "*")
public class VideoCallController {

    @Autowired
    private VideoCallService videoCallService;

    // ---------------- PATIENT APIs ----------------
    @PostMapping("/patient-video-calls/request")
    public ResponseEntity<VideoCallResponseDto> requestCall(
            @RequestParam Long patientId,
            @RequestBody VideoCallRequestDto requestDto) {
        return ResponseEntity.ok(videoCallService.requestVideoCall(patientId, requestDto));
    }

    @GetMapping("/patient-video-calls/{patientId}/history")
    public ResponseEntity<List<VideoCallResponseDto>> getPatientHistory(@PathVariable Long patientId) {
        return ResponseEntity.ok(videoCallService.getCallHistoryForPatient(patientId));
    }

    // ---------------- DOCTOR APIs ----------------
    @PostMapping("/doctor-video-calls/{callId}/approve")
    public ResponseEntity<VideoCallResponseDto> approveCall(
            @PathVariable Long callId,
            @RequestParam Long doctorId) {
        return ResponseEntity.ok(videoCallService.approveVideoCall(callId, doctorId));
    }

    @PostMapping("/doctor-video-calls/{callId}/reject")
    public ResponseEntity<VideoCallResponseDto> rejectCall(
            @PathVariable Long callId,
            @RequestParam Long doctorId) {
        return ResponseEntity.ok(videoCallService.rejectVideoCall(callId, doctorId));
    }

    @GetMapping("/doctor-video-calls/{doctorId}/pending")
    public ResponseEntity<List<VideoCallResponseDto>> getPendingRequests(@PathVariable Long doctorId) {
        return ResponseEntity.ok(videoCallService.getPendingRequestsForDoctor(doctorId));
    }

    @GetMapping("/doctor-video-calls/{doctorId}/history")
    public ResponseEntity<List<VideoCallResponseDto>> getDoctorHistory(@PathVariable Long doctorId) {
        return ResponseEntity.ok(videoCallService.getCallHistoryForDoctor(doctorId));
    }

    // ---------------- COMMON ----------------
    @GetMapping("/video-calls/{callId}")
    public ResponseEntity<VideoCallResponseDto> getCall(@PathVariable Long callId) {
        return ResponseEntity.ok(videoCallService.getCallById(callId));
    }

    @PostMapping("/video-calls/{callId}/start")
    public ResponseEntity<VideoCallResponseDto> startCall(@PathVariable Long callId) {
        return ResponseEntity.ok(videoCallService.startCall(callId));
    }

    @PostMapping("/video-calls/{callId}/end")
    public ResponseEntity<VideoCallResponseDto> endCall(@PathVariable Long callId) {
        return ResponseEntity.ok(videoCallService.endCall(callId));
    }
    
    
}
