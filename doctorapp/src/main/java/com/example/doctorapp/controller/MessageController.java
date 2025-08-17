package com.example.doctorapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MessageController {
     @GetMapping("/messaging")  
    public String messages() {

        return "messaging";
    }
}
