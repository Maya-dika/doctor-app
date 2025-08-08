package com.example.doctorapp.controller;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository ;
    @GetMapping("/register-doctor")
     public String showRegistrationForm(@RequestParam(required = false) String success, Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("success", success != null);
        return "register";
     }
     

@PostMapping("/register-patient")
    public String registerPatient(@ModelAttribute Doctor doctor) {
        doctorRepository.save(doctor);
        return "redirect:/register-doctor?success";
    }
}
