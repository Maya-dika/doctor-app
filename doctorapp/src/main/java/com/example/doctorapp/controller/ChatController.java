package com.example.doctorapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {
    
    @GetMapping("/chat")
    public String chatPage() {
        return "chat"; // returns chat.html template
    }
}