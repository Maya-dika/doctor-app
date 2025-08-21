package com.example.doctorapp.controller;

import com.example.doctorapp.model.DoctorAppointments;
import com.example.doctorapp.service.DrAppService;
import com.example.doctorapp.repository.DrAppRepository;
import com.example.doctorapp.model.Doctor;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DoctorAppController {
    private final DrAppService drappService;

    public DoctorAppController(DrAppService drappService) {
        this.drappService = drappService;
    }

    @GetMapping("/doctor-appointments")
    public String getDoctorAppointments(Model model, HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("loggedInDoctor");
        if (doctor != null) {
            List<DoctorAppointments> appointments = drappService.findByDoctorId(doctor.getId());
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
