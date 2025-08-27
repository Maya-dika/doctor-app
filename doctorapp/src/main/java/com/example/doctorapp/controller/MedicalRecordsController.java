package com.example.doctorapp.controller;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.MedicalRecords;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.service.MedicalRecordsService;
import com.example.doctorapp.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class MedicalRecordsController {
    
    private final MedicalRecordsService medicalRecordsService;
    private final PatientService patientService;
    
    public MedicalRecordsController(MedicalRecordsService medicalRecordsService, 
                                  PatientService patientService) {
        this.medicalRecordsService = medicalRecordsService;
        this.patientService = patientService;
    }
    
    @GetMapping("/medical-records")
    public String viewMedicalRecords(Model model, HttpSession session) {
        Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
        
        List<MedicalRecords> records;
        List<Patient> patients;
        
        if (loggedInDoctor != null) {
            // ✅ Using your service method
            records = medicalRecordsService.findByDoctorId(loggedInDoctor.getId());
      
        } else {
            records = Collections.emptyList();
            patients = Collections.emptyList();
        }
        
        model.addAttribute("doctor", loggedInDoctor);
        model.addAttribute("medicalRecords", records);
        
        return "medical-records";
    }
}