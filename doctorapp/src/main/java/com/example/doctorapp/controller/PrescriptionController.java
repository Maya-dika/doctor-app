package com.example.doctorapp.controller;
import com.example.doctorapp.model.Prescription;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.repository.PrescriptionRepository;
import com.example.doctorapp.service.PrescriptionService;
import com.example.doctorapp.service.DoctorService;
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

    
    @GetMapping("/prescriptions")
    public String showPrescriptions(Model model, HttpSession session) {
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        if (loggedInPatient == null) {
            return "redirect:/login";
        }
        
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(loggedInPatient.getId());
        model.addAttribute("prescriptions", prescriptions);


        
        return "prescriptions"; 
    }
    
//    // POST mapping would be used for actions like creating/updating prescriptions
//    // (This would typically be used by doctors, not patients)
//    @PostMapping("/prescriptions/create")
//    public String createPrescription(@ModelAttribute Prescription prescription, 
//                                   HttpSession session, 
//                                   Model model) {
//        // This would be for doctors to create prescriptions
//        // Not typically used in patient views
//        return "redirect:/prescriptions";
//    }
}
    
