package com.example.doctorapp.controller;

import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.PatientRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.doctorapp.service.PatientService;
import com.example.doctorapp.service.AppointmentService;
import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.service.PatientProfileService;
import com.example.doctorapp.model.AdditionalPatientInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PatientController {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private PatientService patientService;
    
     @Autowired
    private PatientProfileService patientProfileService; 
     @Autowired
    private AppointmentService appointmentService; 
    
    
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
    
  // Update your Patient Dashboard Controller method like this:

@GetMapping("/patient-dashboard")
public String patientDashboard(Model model, HttpSession session) {
    Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
    if (loggedInPatient == null) {
        return "redirect:/login";
    }
    
    // Add patient to model
    model.addAttribute("patient", loggedInPatient);
    
    // Get all appointments for this patient
List<Appointment> allAppointments = appointmentService.getAppointmentsByPatient(loggedInPatient.getId());    
    // Calculate upcoming appointments (BOOKED or CONFIRMED, and date is today or future)
    LocalDate today = LocalDate.now();
    long upcomingAppointments = allAppointments.stream()
        .filter(app -> ("BOOKED".equals(app.getPaymentStatus()) || "CONFIRMED".equals(app.getPaymentStatus())) 
                      && (app.getAppointmentDate().isEqual(today) || app.getAppointmentDate().isAfter(today)))
        .count();
    
    // Calculate total visits (COMPLETED appointments)
    long totalVisits = allAppointments.stream()
        .filter(app -> "COMPLETED".equals(app.getPaymentStatus()))
        .count();
    
    // Add stats to model
    model.addAttribute("upcomingAppointments", upcomingAppointments);
    model.addAttribute("totalVisits", totalVisits);
    
    // Keep these as static for now (you mentioned they stay as is)
    model.addAttribute("activePrescriptions", 0);
    model.addAttribute("unreadMessages", 0);
    
    // Optional: Add recent appointments for display
    List<Appointment> recentAppointments = allAppointments.stream()
        .filter(app -> ("BOOKED".equals(app.getPaymentStatus()) || "CONFIRMED".equals(app.getPaymentStatus())) 
                      && (app.getAppointmentDate().isEqual(today) || app.getAppointmentDate().isAfter(today)))
        .sorted((a1, a2) -> a1.getAppointmentDate().compareTo(a2.getAppointmentDate()))
        .limit(3)
        .collect(Collectors.toList());
    
    model.addAttribute("appointments", recentAppointments);
    
    return "patient-dashboard";
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