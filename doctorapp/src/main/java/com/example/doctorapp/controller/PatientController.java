package com.example.doctorapp.controller;

import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.PatientRepository;
import com.example.doctorapp.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.doctorapp.service.PatientService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class PatientController {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private PatientService patientService;
    
    @GetMapping("/register-patient")
    public String showRegistrationForm(@RequestParam(required = false) String success, Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("success", success != null);
        return "register";
    }
    
    @PostMapping("/register-patient")
    public String registerPatient(@ModelAttribute Patient patient, @RequestParam String password) {
        String hashed = patientService.hashPassword(password);
        patient.setPassword(hashed);
        patientRepository.save(patient);
        return "redirect:/register-patient?success";
    }
    
    @GetMapping("/patient-dashboard")
    public String showPatientDashboard(Model model, HttpSession session) {
        // Get the logged-in patient from session
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        
        // Security check - ensure patient is logged in
        if (loggedInPatient == null) {
            // If no patient in session, redirect to login
            return "redirect:/login";
        }
        
        // Add patient data to model for the template
        model.addAttribute("patient", loggedInPatient);
        
           model.addAttribute("upcomingAppointments", 0);
           model.addAttribute("totalVisits", 0);
           model.addAttribute("activePrescriptions", 0);
           model.addAttribute("unreadMessages", 0);
        
        return "patient-dashboard"; // This corresponds to patient-dashboard.html in templates
    }
   
    // Optional: Add logout functionality
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Clear all session data
        session.invalidate();
        return "redirect:/login";
    }
    
    // Optional: Helper method to get logged-in patient
    private Patient getLoggedInPatient(HttpSession session) {
        return (Patient) session.getAttribute("loggedInPatient");
    }   
}