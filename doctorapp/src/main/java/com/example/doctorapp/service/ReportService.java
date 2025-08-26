package com.example.doctorapp.service;

import com.example.doctorapp.dto.ReportDto;
import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.Specialty;
import com.example.doctorapp.repository.AppointmentRepository;
import com.example.doctorapp.repository.DoctorRepository;
import com.example.doctorapp.repository.PatientRepository;
import com.example.doctorapp.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private SpecialtyRepository specialtyRepository;
    
    public ReportDto generateReport(LocalDate startDate, LocalDate endDate) {
        ReportDto report = new ReportDto(startDate, endDate);
        
        // Calculate patient statistics
        calculatePatientStatistics(report, startDate, endDate);
        
        // Calculate appointment statistics
        calculateAppointmentStatistics(report, startDate, endDate);
        
        // Calculate billing statistics
        calculateBillingStatistics(report, startDate, endDate);
        
        // Calculate additional metrics
        calculateAdditionalMetrics(report, startDate, endDate);
        
        return report;
    }
    
    private void calculatePatientStatistics(ReportDto report, LocalDate startDate, LocalDate endDate) {
        List<Patient> allPatients = patientRepository.findAll();
        List<Patient> patientsInPeriod = patientRepository.findAll().stream()
            .filter(patient -> {
                // For demo purposes, consider patients active if they have appointments in the period
                // In a real system, you might have a registration date field
                return true; // All patients are considered for now
            })
            .collect(Collectors.toList());
        
        report.setTotalPatients((long) allPatients.size());
        report.setNewPatientsInPeriod((long) patientsInPeriod.size());
        report.setActivePatientsInPeriod((long) patientsInPeriod.size());
        
        // Patients by gender
        Map<String, Long> patientsByGender = allPatients.stream()
            .collect(Collectors.groupingBy(
                patient -> patient.getGender() != null ? patient.getGender() : "Unknown",
                Collectors.counting()
            ));
        report.setPatientsByGender(patientsByGender);
        
        // Patients by age group
        Map<String, Long> patientsByAgeGroup = allPatients.stream()
            .collect(Collectors.groupingBy(
                patient -> getAgeGroup(patient.getAge()),
                Collectors.counting()
            ));
        report.setPatientsByAgeGroup(patientsByAgeGroup);
    }
    
    private void calculateAppointmentStatistics(ReportDto report, LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointmentsInPeriod = appointmentRepository.findAll().stream()
            .filter(appointment -> {
                LocalDate appointmentDate = appointment.getAppointmentDate();
                return appointmentDate != null && 
                       !appointmentDate.isBefore(startDate) && 
                       !appointmentDate.isAfter(endDate);
            })
            .collect(Collectors.toList());
        
        report.setTotalAppointments((long) appointmentsInPeriod.size());
        
        // Appointments by status
        Map<String, Long> appointmentsByStatus = appointmentsInPeriod.stream()
            .collect(Collectors.groupingBy(
                appointment -> appointment.getStatus() != null ? appointment.getStatus() : "Unknown",
                Collectors.counting()
            ));
        report.setAppointmentsByStatus(appointmentsByStatus);
        
        // Count specific statuses
        report.setCompletedAppointments(appointmentsByStatus.getOrDefault("COMPLETED", 0L));
        report.setCancelledAppointments(appointmentsByStatus.getOrDefault("CANCELLED", 0L));
        report.setPendingAppointments(appointmentsByStatus.getOrDefault("PENDING", 0L));
        
        // Appointments by doctor
        Map<String, Long> appointmentsByDoctor = appointmentsInPeriod.stream()
            .filter(appointment -> appointment.getDoctor() != null)
            .collect(Collectors.groupingBy(
                appointment -> appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName(),
                Collectors.counting()
            ));
        report.setAppointmentsByDoctor(appointmentsByDoctor);
        
        // Appointments by date
        Map<LocalDate, Long> appointmentsByDate = appointmentsInPeriod.stream()
            .collect(Collectors.groupingBy(
                Appointment::getAppointmentDate,
                Collectors.counting()
            ));
        report.setAppointmentsByDate(appointmentsByDate);
    }
    
    private void calculateBillingStatistics(ReportDto report, LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointmentsInPeriod = appointmentRepository.findAll().stream()
            .filter(appointment -> {
                LocalDate appointmentDate = appointment.getAppointmentDate();
                return appointmentDate != null && 
                       !appointmentDate.isBefore(startDate) && 
                       !appointmentDate.isAfter(endDate);
            })
            .collect(Collectors.toList());
        
        // Calculate total revenue
        BigDecimal totalRevenue = appointmentsInPeriod.stream()
            .map(appointment -> appointment.getPaymentAmount() != null ? appointment.getPaymentAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setTotalRevenue(totalRevenue);
        
        // Calculate average appointment cost
        if (!appointmentsInPeriod.isEmpty()) {
            BigDecimal averageCost = totalRevenue.divide(BigDecimal.valueOf(appointmentsInPeriod.size()), 2, RoundingMode.HALF_UP);
            report.setAverageAppointmentCost(averageCost);
        } else {
            report.setAverageAppointmentCost(BigDecimal.ZERO);
        }
        
        // Calculate paid vs pending amounts
        BigDecimal totalPaid = appointmentsInPeriod.stream()
            .filter(appointment -> "PAID".equals(appointment.getPaymentStatus()))
            .map(appointment -> appointment.getPaymentAmount() != null ? appointment.getPaymentAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setTotalPaidAmount(totalPaid);
        
        BigDecimal totalPending = appointmentsInPeriod.stream()
            .filter(appointment -> !"PAID".equals(appointment.getPaymentStatus()))
            .map(appointment -> appointment.getPaymentAmount() != null ? appointment.getPaymentAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setTotalPendingAmount(totalPending);
        
        // Revenue by payment method
        Map<String, BigDecimal> revenueByPaymentMethod = appointmentsInPeriod.stream()
            .filter(appointment -> appointment.getPaymentMethod() != null && appointment.getPaymentAmount() != null)
            .collect(Collectors.groupingBy(
                Appointment::getPaymentMethod,
                Collectors.reducing(BigDecimal.ZERO, Appointment::getPaymentAmount, BigDecimal::add)
            ));
        report.setRevenueByPaymentMethod(revenueByPaymentMethod);
        
        // Revenue by date
        Map<LocalDate, BigDecimal> revenueByDate = appointmentsInPeriod.stream()
            .filter(appointment -> appointment.getPaymentAmount() != null)
            .collect(Collectors.groupingBy(
                Appointment::getAppointmentDate,
                Collectors.reducing(BigDecimal.ZERO, Appointment::getPaymentAmount, BigDecimal::add)
            ));
        report.setRevenueByDate(revenueByDate);
    }
    
    private void calculateAdditionalMetrics(ReportDto report, LocalDate startDate, LocalDate endDate) {
        long daysInPeriod = Period.between(startDate, endDate).getDays() + 1;
        
        // Average patients per day
        report.setAveragePatientsPerDay((double) report.getTotalPatients() / daysInPeriod);
        
        // Average appointments per day
        report.setAverageAppointmentsPerDay((double) report.getTotalAppointments() / daysInPeriod);
        
        // Average revenue per day
        if (daysInPeriod > 0) {
            double avgRevenue = report.getTotalRevenue().doubleValue() / daysInPeriod;
            report.setAverageRevenuePerDay(avgRevenue);
        } else {
            report.setAverageRevenuePerDay(0.0);
        }
        
        // Total doctors and specialties
        report.setTotalDoctors(doctorRepository.count());
        report.setTotalSpecialties(specialtyRepository.count());
    }
    
    private String getAgeGroup(Integer age) {
        if (age == null) return "Unknown";
        if (age < 18) return "Under 18";
        if (age < 30) return "18-29";
        if (age < 50) return "30-49";
        if (age < 65) return "50-64";
        return "65+";
    }
}
