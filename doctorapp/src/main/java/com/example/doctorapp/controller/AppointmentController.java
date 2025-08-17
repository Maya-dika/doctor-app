package com.example.doctorapp.controller;


import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.service.AppointmentService;
import com.example.doctorapp.service.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }
    
    @GetMapping("/appointment")
    public String showDoctorList(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        return "appointment";
    }
//    @GetMapping("/appointment/{doctorId}")
//    public String showBookingForm(@PathVariable Long doctorId, Model model) {
//        Doctor doctor = doctorService.getDoctorById(doctorId);
//        
//        if (doctor == null) {
//            return "redirect:/book-appointment?error=doctorNotFound";
//        }
//        model.addAttribute("doctor", doctor);
//        model.addAttribute("appointment", new Appointment());
//        return "book-appointment-form";
//    }
//    
    @PostMapping("/book-appointment")
    public String saveAppointment(@ModelAttribute Appointment appointment, 
                                  @RequestParam Long doctorId,
                                  HttpSession session,
                                  Model model) {
        
       Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        if (loggedInPatient == null) {
            return "redirect:/login";
        }
        
         Doctor doctor = doctorService.getDoctorById(doctorId);
        if (doctor == null) {
            model.addAttribute("error", "Selected doctor does not exist.");
            return "book-appointment-form";
        }

        if (appointment.getAppointmentDate() == null) {
            model.addAttribute("error", "Please select a valid appointment date.");
            model.addAttribute("doctor", doctor);
            return "book-appointment-form";
        }

        appointment.setPatient(loggedInPatient);
        appointment.setDoctor(doctor);
        appointment.setStatus("BOOKED");
        
        appointmentService.save(appointment);

        return "redirect:/patient-dashboard";
    }


  
   

@GetMapping("/myappointments")
public String viewMyAppointments(Model model, HttpSession session) {
    Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
    if (loggedInPatient == null) {
        return "redirect:/login";
    }
    model.addAttribute("appointments", appointmentService.getAppointmentsByPatient(loggedInPatient.getId()));
    return "myappointments";
}
        
    
   
}
 
