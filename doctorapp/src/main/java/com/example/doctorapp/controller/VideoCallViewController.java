package com.example.doctorapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.Patient;
import jakarta.servlet.http.HttpSession;

@Controller
public class VideoCallViewController {
    
    @GetMapping("/patient-medical-calls")
    public String patientPortal(Model model, HttpSession session, 
                               @RequestParam(required = false) Long callId, 
                               @RequestParam(required = false) Boolean autoJoin) {
        
        // Get the logged-in patient from session
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        
        if (loggedInPatient == null) {
            return "redirect:/login";
        }
        
        // Add patient information to model
        model.addAttribute("loggedInPatient", loggedInPatient);
        model.addAttribute("patientId", loggedInPatient.getId());
        model.addAttribute("patientName", loggedInPatient.getFirstName());
        model.addAttribute("pageTitle", "Patient Video Calls");
        model.addAttribute("userType", "patient");
        
        if (callId != null) {
            model.addAttribute("callId", callId);
            model.addAttribute("autoJoin", autoJoin != null ? autoJoin : false);
        }
        
        return "patient-medical-calls";
    }
    
    @GetMapping("/doctor-medical-calls")
    public String doctorPortal(Model model, HttpSession session,
                              @RequestParam(required = false) Long callId, 
                              @RequestParam(required = false) Boolean autoJoin) {
        
        // Get the logged-in doctor from session
        Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
        
        if (loggedInDoctor == null) {
            return "redirect:/login";
        }
        
        // Add doctor information to model
        model.addAttribute("loggedInDoctor", loggedInDoctor);
        model.addAttribute("doctorId", loggedInDoctor.getId());
        model.addAttribute("doctorName", loggedInDoctor.getFirstName());
        model.addAttribute("pageTitle", "Doctor Video Calls");
        model.addAttribute("userType", "doctor");
        
        if (callId != null) {
            model.addAttribute("callId", callId);
            model.addAttribute("autoJoin", autoJoin != null ? autoJoin : false);
        }
        
        return "doctor-medical-calls";
    }
    
    @GetMapping("/join")
    public String joinCall(@RequestParam(required = false) Long callId, 
                          @RequestParam(required = false) String userType,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        
        // Check session and determine user type if not provided
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
        
        // If no one is logged in, redirect to login
        if (loggedInPatient == null && loggedInDoctor == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to join a video call.");
            return "redirect:/login";
        }
        
        // Determine user type based on session if not provided
        if (userType == null) {
            if (loggedInPatient != null) {
                userType = "patient";
            } else if (loggedInDoctor != null) {
                userType = "doctor";
            }
        }
        
        // Validate user type matches logged in user
        if (userType.equals("patient") && loggedInPatient == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in as a patient to access the patient portal.");
            return "redirect:/login";
        }
        
        if (userType.equals("doctor") && loggedInDoctor == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in as a doctor to access the doctor portal.");
            return "redirect:/login";
        }
        
        // Validate user type
        if (!userType.equals("patient") && !userType.equals("doctor")) {
            redirectAttributes.addFlashAttribute("error", "Invalid user type. Please specify patient or doctor.");
            return "redirect:/";
        }
        
        if (callId != null) {
            redirectAttributes.addAttribute("callId", callId);
            redirectAttributes.addAttribute("autoJoin", true);
        }
        
        // Route to appropriate portal based on user type
        if (userType.equals("patient")) {
            return "redirect:/patient-medical-calls";
        } else {
            return "redirect:/doctor-medical-calls";
        }
    }
}