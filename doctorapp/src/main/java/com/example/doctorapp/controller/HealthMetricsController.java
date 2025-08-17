package com.example.doctorapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.util.Set;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.AdditionalPatientInfo;
import com.example.doctorapp.service.PatientProfileService;
import org.springframework.stereotype.Controller;

@Controller
public class HealthMetricsController {
     
       @Autowired
    private PatientProfileService patientProfileService;
    
    
   @GetMapping("/health-metrics")
public String showHealthMetrics(Model model, HttpSession session) {
    try {
        // Get the current authenticated patient from session
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        
        if (patient == null) {
            // Redirect to login if no patient in session
            return "redirect:/login";
        }
        
        System.out.println("Loading health metrics for patient: " + patient);
        
        // Get the patient's additional health information
        AdditionalPatientInfo additionalPatientInfo = patientProfileService.findByPatientId(patient.getId());
        
        // Add data to the model
        model.addAttribute("patient", patient);
        model.addAttribute("additionalPatientInfo", additionalPatientInfo);
        
        // Calculate BMI if height and weight are available
        if (additionalPatientInfo != null && 
            additionalPatientInfo.getCurrentWeight() != null && 
            additionalPatientInfo.getHeightFeet() != null && 
            additionalPatientInfo.getHeightInches() != null) {
            
            double totalInches = (additionalPatientInfo.getHeightFeet() * 12) + additionalPatientInfo.getHeightInches();
            double bmi = (additionalPatientInfo.getCurrentWeight() * 703) / (totalInches * totalInches);
            model.addAttribute("calculatedBMI", Math.round(bmi * 10.0) / 10.0); // Round to 1 decimal place
            
            // Determine BMI status
            String bmiStatus;
            if (bmi < 18.5) {
                bmiStatus = "Underweight";
            } else if (bmi < 25) {
                bmiStatus = "Normal";
            } else if (bmi < 30) {
                bmiStatus = "Overweight";
            } else {
                bmiStatus = "Obese";
            }
            model.addAttribute("bmiStatus", bmiStatus);
        }
        
        // Calculate weight progress if current and target weights are available
        if (additionalPatientInfo != null && 
            additionalPatientInfo.getCurrentWeight() != null && 
            additionalPatientInfo.getTargetWeight() != null) {
            
            Double currentWeight = additionalPatientInfo.getCurrentWeight();
            Double targetWeight = additionalPatientInfo.getTargetWeight();
            Double weightDifference = currentWeight - targetWeight;
            
            model.addAttribute("weightDifference", Math.round(weightDifference * 10.0) / 10.0);
            
            // Calculate progress percentage (assuming a starting weight)
            // This could be enhanced by storing an initial weight in the database
            if (Math.abs(weightDifference) > 0) {
                model.addAttribute("weightProgressPercentage", 
                    Math.min(100, Math.abs(weightDifference / currentWeight * 100)));
            }
        }
        
        // Assess blood pressure status if baseline values are available
        if (additionalPatientInfo != null && 
            additionalPatientInfo.getBaselineSystolic() != null && 
            additionalPatientInfo.getBaselineDiastolic() != null) {
            
            Integer systolic = additionalPatientInfo.getBaselineSystolic();
            Integer diastolic = additionalPatientInfo.getBaselineDiastolic();
            
            String bpStatus;
            String bpStatusClass;
            
            if (systolic < 120 && diastolic < 80) {
                bpStatus = "Optimal";
                bpStatusClass = "status-excellent";
            } else if (systolic < 140 && diastolic < 90) {
                bpStatus = "Normal";
                bpStatusClass = "status-good";
            } else if (systolic < 160 && diastolic < 100) {
                bpStatus = "Elevated";
                bpStatusClass = "status-warning";
            } else {
                bpStatus = "High";
                bpStatusClass = "status-danger";
            }
            
            model.addAttribute("bpStatus", bpStatus);
            model.addAttribute("bpStatusClass", bpStatusClass);
        }
        
        // Calculate health risk assessment based on chronic conditions and family history
        if (additionalPatientInfo != null) {
            int riskScore = calculateHealthRiskScore(additionalPatientInfo);
            String riskLevel;
            String riskClass;
            
            if (riskScore <= 2) {
                riskLevel = "Low Risk";
                riskClass = "risk-low";
            } else if (riskScore <= 5) {
                riskLevel = "Moderate Risk";
                riskClass = "risk-moderate";
            } else {
                riskLevel = "High Risk";
                riskClass = "risk-high";
            }
            
            model.addAttribute("healthRiskLevel", riskLevel);
            model.addAttribute("healthRiskClass", riskClass);
        }
        
        return "health-metrics"; // Return the health-metrics.html template
        
    } catch (Exception e) {
        // Log the error
       System.err.println("Error retrieving health metrics for patient: " +
    (session.getAttribute("loggedInPatient") != null ?
        ((Patient) session.getAttribute("loggedInPatient")).getId() : "unknown") +
    " - Exception: " + e.getMessage());
        
        // Add error message to model
        model.addAttribute("errorMessage", "Unable to load health metrics. Please try again.");
        
        // Redirect to dashboard with error
        return "redirect:/patient-dashboard?error=health-metrics-unavailable";
    }
}

/**
 * Calculate a simple health risk score based on chronic conditions and family history
 */
private int calculateHealthRiskScore(AdditionalPatientInfo additionalPatientInfo) {
    int score = 0;
    
    // Add points for chronic conditions
    if (additionalPatientInfo.getChronicConditions() != null) {
        Set<String> highRiskConditions = Set.of("DIABETES", "HEART_DISEASE", "HYPERTENSION");
        for (String condition : additionalPatientInfo.getChronicConditions()) {
            if (highRiskConditions.contains(condition)) {
                score += 2; // High risk conditions get more points
            } else {
                score += 1; // Other conditions get 1 point
            }
        }
    }
    
    // Add points for family history
    if (additionalPatientInfo.getFamilyHistory() != null) {
        Set<String> highRiskFamilyConditions = Set.of("HEART_DISEASE", "DIABETES", "STROKE", "CANCER");
        for (String familyCondition : additionalPatientInfo.getFamilyHistory()) {
            if (highRiskFamilyConditions.contains(familyCondition)) {
                score += 1; // Family history gets 1 point each
            }
        }
    }
    
    // Add points for lifestyle factors
    if ("CURRENT".equals(additionalPatientInfo.getSmokingStatus())) {
        score += 2; // Current smoking is high risk
    }
    
    if ("DAILY".equals(additionalPatientInfo.getAlcoholConsumption()) || 
        "REGULARLY".equals(additionalPatientInfo.getAlcoholConsumption())) {
        score += 1; // Heavy drinking adds risk
    }
    
    if ("SEDENTARY".equals(additionalPatientInfo.getActivityLevel())) {
        score += 1; // Sedentary lifestyle adds risk
    }
    
    return score;
}}