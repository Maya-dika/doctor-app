package com.example.doctorapp.service;

import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.AppointmentRepository;
import com.example.doctorapp.dto.AppointmentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    
    
    

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment save(Appointment appointment) {
        System.out.println("Saving appointment with status: " + appointment.getStatus());
        Appointment savedAppointment = appointmentRepository.save(appointment);
        System.out.println("Saved appointment ID: " + savedAppointment.getId() + " with status: " + savedAppointment.getStatus());
        return savedAppointment;
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctor_Id(doctorId);
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    public List<Appointment> getAppointmentsByDateAndDoctor(LocalDate date, Long doctorId) {
        return appointmentRepository.findByAppointmentDateAndDoctor_Id(date, doctorId);
    }

    public List<Appointment> getTodaysAppointments() {
        return getAppointmentsByDate(LocalDate.now());
    }

    public List<Appointment> getTodaysAppointmentsForDoctor(Long doctorId) {
        return getAppointmentsByDateAndDoctor(LocalDate.now(), doctorId);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public Double getTotalBalance(Patient patient) {
        Double total = appointmentRepository.getTotalBalance(patient.getId());
        return total != null ? total : 0.0;
    }

  public List<Appointment> getCheckedInAppointments(Long patientId) {
    System.out.println("Getting checked-in unpaid appointments for patient ID: " + patientId);
    List<Appointment> appointments = appointmentRepository.findByPatientIdAndStatusAndPaymentStatusIsNull(patientId, "CHECKED_IN");
    System.out.println("Found " + appointments.size() + " checked-in unpaid appointments");
    return appointments;
}

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        System.out.println("Getting all appointments for patient ID: " + patientId);
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        System.out.println("Found " + appointments.size() + " appointments");
        return appointments;
    }

    public boolean updatePayment(AppointmentDto appointment) {
        try {
            Appointment entity = appointmentRepository.findById(appointment.getId())
                    .orElseThrow();
            entity.setPaymentAmount(appointment.getPaymentAmount());
            entity.setPaymentMethod(appointment.getPaymentMethod());
            entity.setPaymentStatus(appointment.getPaymentStatus());
            entity.setPaymentDate(appointment.getPaymentDate());

            appointmentRepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
      public List<Appointment> findByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctor_Id(doctorId);
    }

    public List<Patient> getPatientsWithCompletedAppointments(Long doctorId) {
    List<Appointment> completedAppointments = appointmentRepository.findByDoctor_IdAndStatus(doctorId, "COMPLETED");
    
    // Get unique patients from completed appointments
    return completedAppointments.stream()
        .map(Appointment::getPatient)
        .distinct()
        .collect(Collectors.toList());
}

public boolean hasCompletedAppointmentWithDoctor(Long patientId, Long doctorId) {
    return appointmentRepository.hasCompletedAppointmentWithDoctor(patientId, doctorId);
}
}
