package com.example.doctorapp.mapper;

import com.example.doctorapp.dto.AppointmentDto;
import com.example.doctorapp.model.Appointment;

public class AppointmentMapper {
    
    public static AppointmentDto toDto(Appointment a) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(a.getId());
        dto.setpatientFirstName(a.getPatient().getFirstName());
        dto.setpatientLastName(a.getPatient().getLastName());
        dto.setdoctorFirstName(a.getDoctor().getFirstName());
        dto.setdoctorLastName(a.getDoctor().getLastName());
        dto.setconsultationFee(a.getDoctor().getConsultationFee());
        dto.setTime(a.getAppointmentTime());
        dto.setDate(a.getAppointmentDate());
        dto.setStatus(a.getStatus());
        
        // Map payment-related fields
        dto.setPaymentAmount(a.getPaymentAmount());
        dto.setPaymentDate(a.getPaymentDate());
        dto.setPaymentMethod(a.getPaymentMethod());
        dto.setPaymentStatus(a.getPaymentStatus());
        
        return dto;
    }
}