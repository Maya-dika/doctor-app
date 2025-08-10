package com.example.doctorapp.controller;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.service.DoctorService;
import com.example.doctorapp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private PatientService patientService;
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String processLogin(
            @RequestParam("userType") String userType,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {
        
        try {
            if ("doctor".equals(userType)) {
                String hashedPassword = doctorService.hashPassword(password);
                Doctor doctor = doctorService.authenticateDoctor(email, hashedPassword);
                
                if (doctor != null) {
                    session.setAttribute("loggedInDoctor", doctor);
                    session.setAttribute("userType", "doctor");
                    return "redirect:/doctor-dashboard";
                }
                
            } else if ("patient".equals(userType)) {
                String hashedPassword = patientService.hashPassword(password);
                Patient patient = patientService.authenticatePatient(email, hashedPassword);
                
                if (patient != null) {
                    session.setAttribute("loggedInPatient", patient);
                    session.setAttribute("userType", "patient");
                    return "redirect:/patient-dashboard";
                }
            }
            
            model.addAttribute("error", "Invalid email or password.");
            return "login";
            
        } catch (Exception e) {
            model.addAttribute("error", "Login failed. Please try again.");
            return "login";
        }
    }
    
     
}