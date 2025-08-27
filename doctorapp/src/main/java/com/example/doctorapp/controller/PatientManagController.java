package com.example.doctorapp.controller;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.service.PatientManagementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PatientManagController {

    @Autowired
    private PatientManagementService patientmanagmentService;

    // Show all patients who booked with the logged-in doctor
    
    @GetMapping("/patient-management")
    public String viewPatientManagement(Model model, HttpSession session) {
        // get the logged in doctor from session
        Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");

        if (loggedInDoctor != null) {
            // fetch all patients for this doctor
            List<Patient> patients = patientmanagmentService.findPatientsByDoctorId(loggedInDoctor.getId());
            
             // 🔎 Debugging with System.out.println
            System.out.println("Fetched " + patients.size() + " patients for doctor ID " + loggedInDoctor.getId());
        for (Patient p : patients) {
            System.out.println("Patient -> ID=" + p.getId() + ", Name=" + p.getFirstName() + " " + p.getLastName());
        }
            model.addAttribute("patients", patients);
            model.addAttribute("doctor", loggedInDoctor);
        } else {
            // if no doctor is logged in, redirect to login
            return "redirect:/login";
        }

        return "patient-management"; // your Thymeleaf/HTML page
    }
}



