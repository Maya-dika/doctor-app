package com.example.doctorapp.controller;
import com.example.doctorapp.model.Analytics;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.service.AnalyticsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    Doctor doctor = (Doctor) session.getAttribute("loggedInDoctor");

    if (doctor == null) {
        // Optional: redirect to login or handle missing doctor
        return "redirect:/login";
    }

    Long doctorId = doctor.getId();
    List<Analytics> analyticsList = analyticsService.getAnalyticssByDoctorId(doctorId);

    model.addAttribute("doctor", doctor);
    model.addAttribute("analyticsList", analyticsList != null ? analyticsList : new ArrayList<>());
    return "analytics";
}

}