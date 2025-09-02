/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.doctorapp.dto;

public class VideoCallRequestDto {
    private Long doctorId;
    private String reason;
    

    public VideoCallRequestDto() {}
    
    public VideoCallRequestDto(Long doctorId, String reason) {
        this.doctorId = doctorId;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

