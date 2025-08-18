package com.example.doctorapp.service;

import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class AppointmentService {
    
   private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    
       public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
       public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatient_Id(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctor_Id(doctorId);
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
    
}
