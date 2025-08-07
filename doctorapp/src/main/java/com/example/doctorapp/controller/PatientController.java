package com.example.doctorapp.controller;

import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PatientController {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @GetMapping("/register-patient")
    public String showRegistrationForm(@RequestParam(required = false) String success, Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("success", success != null);
        return "register";
    }
    
    @PostMapping("/register-patient")
    public String registerPatient(@ModelAttribute Patient patient) {
        patientRepository.save(patient);
        return "redirect:/register-patient?success";
    }
}