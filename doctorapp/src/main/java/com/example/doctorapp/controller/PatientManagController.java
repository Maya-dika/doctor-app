package com.example.doctorapp.controller;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.PatientManagement;
import com.example.doctorapp.service.PatientManagementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patient-management")
public class PatientManagController {

    @Autowired
    private PatientManagementService patientmanagmentService;

  
   @GetMapping
public String viewPatientManagement(Model model, HttpSession session) {
    Doctor loggedInDoctor = (Doctor) session.getAttribute("loggedInDoctor");
    
    PatientManagement loggedInPatient = (PatientManagement) session.getAttribute("loggedInPatient");
    model.addAttribute("patient", loggedInPatient);
     model.addAttribute("doctor", loggedInDoctor);
    return "patient-management"; 
}

}
