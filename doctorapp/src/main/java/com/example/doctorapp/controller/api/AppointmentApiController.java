package com.example.doctorapp.controller.api;

import com.example.doctorapp.dto.AppointmentDto;
import com.example.doctorapp.mapper.AppointmentMapper;
import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*") // Allow requests from desktop app
public class AppointmentApiController {
    
    private final AppointmentService appointmentService;
    
    public AppointmentApiController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    /**
     * Get today's appointments
     */
    @GetMapping("/today/{date}")
    public ResponseEntity<List<AppointmentDto>> getTodaysAppointments(@PathVariable String date) {
        try {
            LocalDate targetDate = LocalDate.parse(date);
            List<Appointment> appointments = appointmentService.getAppointmentsByDate(targetDate);
            
            List<AppointmentDto> appointmentDtos = appointments.stream()
                    .map(AppointmentMapper::toDto)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(appointmentDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get today's appointments for a specific doctor
     */
    @GetMapping("/today/{date}/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getTodaysAppointmentsForDoctor(
            @PathVariable String date, @PathVariable Long doctorId) {
        try {
            LocalDate targetDate = LocalDate.parse(date);
            List<Appointment> appointments = appointmentService.getAppointmentsByDateAndDoctor(targetDate, doctorId);
            
            List<AppointmentDto> appointmentDtos = appointments.stream()
                    .map(AppointmentMapper::toDto)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(appointmentDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Check in an appointment
     */
    @PostMapping("/checkin/{appointmentId}")
    public ResponseEntity<AppointmentDto> checkInAppointment(@PathVariable Long appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            
            if (appointment == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update status to CHECKED_IN
            appointment.setStatus("CHECKED_IN");
            Appointment updatedAppointment = appointmentService.save(appointment);
            
            AppointmentDto dto = AppointmentMapper.toDto(updatedAppointment);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Search appointments by patient name, doctor name, date, or status
     */
    @GetMapping("/search")
    public ResponseEntity<List<AppointmentDto>> searchAppointments(@RequestParam String term) {
        try {
            List<Appointment> appointments = appointmentService.searchAppointments(term);
            
            List<AppointmentDto> appointmentDtos = appointments.stream()
                    .map(AppointmentMapper::toDto)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(appointmentDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all appointments for a specific date
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDate(@PathVariable String date) {
        try {
            LocalDate targetDate = LocalDate.parse(date);
            List<Appointment> appointments = appointmentService.getAppointmentsByDate(targetDate);
            
            List<AppointmentDto> appointmentDtos = appointments.stream()
                    .map(AppointmentMapper::toDto)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(appointmentDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get a specific appointment by ID
     */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable Long appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            if (appointment != null) {
                AppointmentDto appointmentDto = AppointmentMapper.toDto(appointment);
                return ResponseEntity.ok(appointmentDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
  /**
 * Update billing/payment for an appointment
 */
@PutMapping("/billing/{appointmentId}")
public ResponseEntity<AppointmentDto> updateBilling(
        @PathVariable Long appointmentId,
        @RequestBody Map<String, Object> paymentData) {

    try {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        // Payment Amount
        if (paymentData.containsKey("paymentAmount")) {
            Object amountObj = paymentData.get("paymentAmount");
            BigDecimal amount = new BigDecimal(amountObj.toString());
            appointment.setPaymentAmount(amount);
        }

        // Payment Method
        if (paymentData.containsKey("paymentMethod")) {
            appointment.setPaymentMethod(paymentData.get("paymentMethod").toString());
        }

        // Payment Status
        if (paymentData.containsKey("paymentStatus")) {
            appointment.setPaymentStatus(paymentData.get("paymentStatus").toString());
        }

        // Payment Date
        if (paymentData.containsKey("paymentDate")) {
            String dateStr = paymentData.get("paymentDate").toString();
            LocalDate paymentDate = LocalDate.parse(dateStr); // expects yyyy-MM-dd
            appointment.setPaymentDate(paymentDate);
        }
        
        if (paymentData.containsKey("status")) {
            appointment.setStatus(paymentData.get("status").toString());
        }

        Appointment updatedAppointment = appointmentService.save(appointment);
        AppointmentDto dto = AppointmentMapper.toDto(updatedAppointment);
        return ResponseEntity.ok(dto);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}

}