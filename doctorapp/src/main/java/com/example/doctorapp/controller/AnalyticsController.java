package com.example.doctorapp.controller;
import com.example.doctorapp.model.Analytics;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.service.AnalyticsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.ArrayList;

@Controller
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }
    
    @GetMapping("/analytics")
    public String analytics(HttpSession session, Model model) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");

        // Temporary fix for testing
        if (doctor == null) {
            doctor = new Doctor();
            doctor.setId(1L); // Mock ID
            doctor.setFirstName("Test");
            doctor.setLastName("Doctor");
        }

        Long doctorId = doctor.getId();
        List<Analytics> analyticsList = analyticsService.getAnalyticssByDoctorId(doctorId);

        model.addAttribute("doctor", doctor);
        model.addAttribute("analyticsList", analyticsList != null ? analyticsList : new ArrayList<>());
        return "analytics";
    }
}