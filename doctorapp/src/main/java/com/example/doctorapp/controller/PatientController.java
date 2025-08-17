package com.example.doctorapp.controller;

import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.PatientRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.doctorapp.service.PatientService;
import com.example.doctorapp.service.PatientProfileService;
import com.example.doctorapp.model.AdditionalPatientInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class PatientController {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private PatientService patientService;
    
     @Autowired
    private PatientProfileService patientProfileService;
    
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
    
  @GetMapping("/patient-profile")
public String patientProfile(Model model, HttpSession session) {
    Patient patient = (Patient) session.getAttribute("loggedInPatient");
    
    if (patient == null) {
        return "redirect:/login"; // Redirect if no patient in session
    }
    
    System.out.println("Patient ID: " + patient.getId());
    
    // Get existing profile or create new one
    AdditionalPatientInfo patientInfo = patientProfileService.findByPatientId(patient.getId());
    if (patientInfo == null) {
        patientInfo = new AdditionalPatientInfo();
        patientInfo.setPatient(patient);
    }
    
    model.addAttribute("additionalPatientInfo", patientInfo);
    model.addAttribute("patient", patient);
    
    return "patient-profile";
}

@PostMapping("/patient/profile/save")
@Transactional // Add this annotation
public String saveProfile(@ModelAttribute AdditionalPatientInfo additionalPatientInfo,
                         HttpSession session, RedirectAttributes redirectAttributes) {
    try {
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        
        if (patient == null) {
            redirectAttributes.addFlashAttribute("error", "Session expired. Please login again.");
            return "redirect:/login";
        }
        
        // Set the patient relationship
        additionalPatientInfo.setPatient(patient);
        
        // Debug logging
        System.out.println("Saving profile for patient ID: " + patient.getId());
        System.out.println("Profile ID: " + additionalPatientInfo.getId());
        
        AdditionalPatientInfo saved = patientProfileService.save(additionalPatientInfo);
        System.out.println("Saved profile with ID: " + saved.getId());
        
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/patient-dashboard";
        
    } catch (Exception e) {
        System.err.println("Error saving profile: " + e.getMessage());
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        return "redirect:/patient-profile";
    }
}
}