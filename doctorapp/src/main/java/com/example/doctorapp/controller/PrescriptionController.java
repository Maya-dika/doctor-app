package com.example.doctorapp.controller;
import com.example.doctorapp.model.Prescription;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.repository.PrescriptionRepository;
import com.example.doctorapp.service.PrescriptionService;
import com.example.doctorapp.service.AppointmentService;
import com.example.doctorapp.service.PatientService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller

public class PrescriptionController {
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    
    @Autowired
    private PatientService patientService;

    
   @GetMapping("/prescriptions")
public String showPrescriptions(Model model, HttpSession session) {
    // Get the logged-in patient from session
    Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
    Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
    if (loggedInPatient == null) {
        return "redirect:/login";
    }
    
    // Get all prescriptions for this patient prescribed by doctors
    List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(loggedInPatient.getId());
    
    // Add prescriptions to model
    model.addAttribute("prescriptions", prescriptions);
    
    // Add patient info to model for display purposes
    model.addAttribute("patient", loggedInPatient);
    
    // Calculate some statistics for better UX
    long totalPrescriptions = prescriptions.size();
    long activePrescriptions = prescriptions.stream()
        .filter(p -> "Active".equals(p.getStatus()))
        .count();
    long completedPrescriptions = prescriptions.stream()
        .filter(p -> "Completed".equals(p.getStatus()))
        .count();
    long expiredPrescriptions = prescriptions.stream()
        .filter(p -> "Expired".equals(p.getStatus()))
        .count();
    
    model.addAttribute("totalPrescriptions", totalPrescriptions);
    model.addAttribute("activePrescriptions", activePrescriptions);
    model.addAttribute("completedPrescriptions", completedPrescriptions);
    model.addAttribute("expiredPrescriptions", expiredPrescriptions);
     model.addAttribute("doctor", loggedInDoctor);
    
    return "prescriptions"; // This should be the patient prescriptions view template
}

    
@GetMapping("/doctor-prescriptions")
public String showDoctorPrescriptions(Model model, HttpSession session) {
    Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
    if (loggedInDoctor == null) {
        return "redirect:/doctor-login";
    }
    
    List<Prescription> prescriptions = prescriptionService.getPrescriptionsByDoctorId(loggedInDoctor.getId());
    model.addAttribute("prescriptions", prescriptions);
    model.addAttribute("totalPrescriptions", prescriptions.size());
    
    // Count active prescriptions
    long activePrescriptions = prescriptions.stream()
        .filter(p -> "Active".equals(p.getStatus()))
        .count();
    model.addAttribute("activePrescriptions", activePrescriptions);
    
    return "doctor-prescriptions";
}

@GetMapping("/add-prescription")
public String addPrescriptionForm(@RequestParam(required = false) Long patientId, 
                                 Model model, HttpSession session) {
    Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
    if (loggedInDoctor == null) {
        return "redirect:/doctor-login";
    }
    
    // Get patients with completed appointments for this doctor
    List<Patient> patientsWithCompletedAppointments = appointmentService.getPatientsWithCompletedAppointments(loggedInDoctor.getId());
    
    model.addAttribute("patients", patientsWithCompletedAppointments);
    model.addAttribute("prescription", new Prescription());
    
    if (patientId != null) {
        model.addAttribute("selectedPatientId", patientId);
    }
    
    return "add-prescription";
}

@PostMapping("/save-prescription")
public String savePrescription(@ModelAttribute Prescription prescription,
                              @RequestParam Long patientId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
    Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
    if (loggedInDoctor == null) {
        return "redirect:/doctor-login";
    }
    
    try {
        // Validate that patient has completed appointment with this doctor
        if (!appointmentService.hasCompletedAppointmentWithDoctor(patientId, loggedInDoctor.getId())) {
            redirectAttributes.addFlashAttribute("error", 
                "You can only prescribe medications to patients with completed appointments.");
            return "redirect:/add-prescription";
        }
        
        // Set the doctor and patient
        prescription.setDoctor(loggedInDoctor);
        
        // Find and set the patient
        Patient patient = patientService.findPatientById(patientId).orElse(null);
        if (patient == null) {
            redirectAttributes.addFlashAttribute("error", "Patient not found.");
            return "redirect:/add-prescription";
        }
        prescription.setPatient(patient);
        
        // Set default values
        if (prescription.getPrescribedDate() == null) {
            prescription.setPrescribedDate(LocalDate.now());
        }
        if (prescription.getStatus() == null) {
            prescription.setStatus("Active");
        }
        
        prescriptionService.save(prescription);
        redirectAttributes.addFlashAttribute("success", "Prescription added successfully!");
        
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error adding prescription: " + e.getMessage());
    }
    
    return "redirect:/doctor-prescriptions";
}

@PostMapping("/update-prescription-status/{id}")
public String updatePrescriptionStatus(@PathVariable Long id,
                                     @RequestParam String status,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
    Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
    if (loggedInDoctor == null) {
        return "redirect:/doctor-login";
    }
    
    try {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            
            // Ensure the doctor can only update their own prescriptions
            if (!prescription.getDoctor().getId().equals(loggedInDoctor.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only update your own prescriptions.");
                return "redirect:/doctor-prescriptions";
            }
            
            prescription.setStatus(status);
            prescriptionService.save(prescription);
            redirectAttributes.addFlashAttribute("success", "Prescription status updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Prescription not found.");
        }
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error updating prescription: " + e.getMessage());
    }
    
    return "redirect:/doctor-prescriptions";
}

@PostMapping("/delete-prescription/{id}")
public String deletePrescription(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
    Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
    if (loggedInDoctor == null) {
        return "redirect:/doctor-login";
    }
    
    try {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(id);
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            
            // Ensure the doctor can only delete their own prescriptions
            if (prescription.getDoctor() == null || 
                         !prescription.getDoctor().getId().equals(loggedInDoctor.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only delete your own prescriptions.");
                return "redirect:/doctor-prescriptions";
            }
            
            prescriptionRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Prescription deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Prescription not found.");
        }
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error deleting prescription: " + e.getMessage());
    }
    
    return "redirect:/doctor-prescriptions";
}
}