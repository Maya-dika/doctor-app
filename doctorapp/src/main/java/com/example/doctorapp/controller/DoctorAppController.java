package com.example.doctorapp.controller;

import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.service.DrAppService;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class DoctorAppController {
    private final DrAppService drappService;

    public DoctorAppController(DrAppService drappService) {
        this.drappService = drappService;
    }
        @Autowired
    private AppointmentService appointmentService;

   @GetMapping("/doctor-appointments")
public String getDoctorAppointments(Model model, HttpSession session) {
    Doctor doctor = (Doctor) session.getAttribute("loggedInDoctor");
    if (doctor != null) {
        List<Appointment> appointments = appointmentService.findByDoctorId(doctor.getId());
        System.out.println("Doctor ID: " + doctor.getId());
        System.out.println("Appointments found: " + appointments.size());
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctor", doctor);
    } else {
        System.out.println("No doctor in session");
    }
    return "doctor-appointments";
}



      

 
}
