package com.example.doctorapp.controller;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.DoctorBilling;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.service.DoctorBillingService;
import com.example.doctorapp.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/doctor-billing")
public class DoctorBillingController {
    
    private final DoctorBillingService doctorBillingService;
    private final PatientService patientService;

    public DoctorBillingController(DoctorBillingService doctorBillingService,
                                   PatientService patientService) {
        this.doctorBillingService = doctorBillingService;
        this.patientService = patientService;
    }

    // Default view: all bills for doctor’s patients
    @GetMapping
    public String viewBilling(Model model) {
        List<DoctorBilling> billingRecords = doctorBillingService.getBillsByStatus("ALL"); // or get all
        model.addAttribute("billingRecords", billingRecords);
        return "doctor-billing";
    }

}