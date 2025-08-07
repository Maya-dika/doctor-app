package com.example.doctorapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showUserSelectionPage() {
        return "index.html"; // This matches the HTML file name (without `.html`)
    }

    @GetMapping("/registerdoc")
    public String showDoctorRegistrationForm() {
        return "register_doc"; // You will create this HTML page
    }

}
