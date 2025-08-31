package com.example.doctorapp.controller;

import com.example.doctorapp.service.DoctorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin(origins = "*")
public class ChatBotController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DoctorInfoService doctorInfoService;

    private static final String OLLAMA_BASE_URL = "http://127.0.0.1:11434";
    private static final String OLLAMA_CHAT_URL = OLLAMA_BASE_URL + "/api/chat";
    private static final String MODEL_NAME = "gemma:2b";

    // This should be managed per-user-session in a real, stateful application.
    // For this stateless example, it's a simple instance variable.
    private final List<Map<String, String>> conversationHistory = new ArrayList<>();

    // Menu options
    private static final String OPTION_1 = "1";
    private static final String OPTION_2 = "2";
    private static final String OPTION_3 = "3";
    private static final String OPTION_4 = "4";

    @PostMapping("/ask")
    public ResponseEntity<Map<String, Object>> askChatbot(@RequestBody Map<String, String> request) {
        String message = request.get("message");

        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Message cannot be empty"));
        }

        String response = handleMessage(message.trim());
        
        return ResponseEntity.ok(Map.of(
            "reply", response,
            "status", "success"
        ));
    }

    private String handleMessage(String message) {
        String lowerMessage = message.toLowerCase();

        // Global command to exit chat and return to the main menu
        if (lowerMessage.equals("menu") || lowerMessage.equals("back") || lowerMessage.equals("main menu")) {
            conversationHistory.clear(); // End the chat session
            return getMainMenu();
        }

        // *** CORRECTED LOGIC START ***

        // If conversation history is not empty, we are in chat mode.
        if (!conversationHistory.isEmpty()) {
            // Check if the user wants to switch to a menu option from within the chat
            switch(message) {
                case OPTION_1:
                    conversationHistory.clear();
                    return handleDoctorsListOption();
                case OPTION_2:
                    conversationHistory.clear();
                    return handleSpecialtiesOption();
                case OPTION_3:
                    conversationHistory.clear();
                    return handleLocationsOption();
                case OPTION_4:
                    return "🤖 You are already in the AI chat. Ask me anything!\n\nType 'menu' to return.";
                default:
                    // If it's not a menu command, treat it as part of the ongoing AI conversation
                    return handleAIResponse(message);
            }
        }

        // If conversation history is empty, handle initial menu selection.
        switch (message) {
            case OPTION_1:
                return handleDoctorsListOption();
            case OPTION_2:
                return handleSpecialtiesOption();
            case OPTION_3:
                return handleLocationsOption();
            case OPTION_4:
                // This is the key change: entering chat mode.
                // We add a system message to the history to signify the chat has started.
                conversationHistory.add(Map.of("role", "system", "content", "The user has entered chat mode."));
                return "🤖 You've entered chat mode! Ask me anything about health, symptoms, or medical questions. Type 'menu' to return to the main menu.\n\n" +
                       "What would you like to know about?";
            default:
                // If it's not a menu option and chat hasn't started, it's an unrecognized command.
                return "I didn't understand that. " + getMainMenu();
        }
        // *** CORRECTED LOGIC END ***
    }

    private String getMainMenu() {
        return "🏥 **Welcome to Healya!** Choose an option:\n\n" +
               "**1** - 👨‍⚕️ View all our doctors\n" +
               "**2** - 🩺 See available specialties\n" +
               "**3** - 📍 Find doctors by location\n" +
               "**4** - 💬 Chat with AI assistant\n\n" +
               "Simply type **1**, **2**, **3**, or **4** to continue.";
    }

    // --- All original helper methods preserved below ---

    private String handleDoctorsListOption() {
        try {
            List<String> allDoctors = doctorInfoService.getAllDoctorsWithSpecialties();
            
            if (allDoctors.isEmpty() || allDoctors.get(0).contains("No doctors available")) {
                return "❌ **No doctors are currently available in our system.**\n\n" +
                       "Please contact our support team for assistance.\n\n" +
                       "Type **menu** to return to main options.";
            }

            StringBuilder response = new StringBuilder();
            response.append("👨‍⚕️ **Our Available Doctors:**\n\n");
            
            for (int i = 0; i < allDoctors.size(); i++) {
                response.append("**").append(i + 1).append(".** ").append(allDoctors.get(i)).append("\n");
            }
            
            response.append("\n📊 **Total Doctors:** ").append(allDoctors.size());
            response.append("\n\nType **menu** to return to main options.");
            
            return response.toString();
            
        } catch (Exception e) {
            return "❌ **Error retrieving doctors list**\n\n" +
                   "Please try again later or contact support.\n\n" +
                   "Type **menu** to return to main options.";
        }
    }

    private String handleSpecialtiesOption() {
        try {
            Map<String, List<String>> doctorsBySpecialty = doctorInfoService.getAllDoctorsGroupedBySpecialty();
            
            if (doctorsBySpecialty.isEmpty()) {
                return "❌ **No specialties are currently available.**\n\n" +
                       "Please contact our support team.\n\n" +
                       "Type **menu** to return to main options.";
            }

            StringBuilder response = new StringBuilder();
            response.append("🩺 **Available Specialties & Doctors:**\n\n");
            
            int specialtyCount = 0;
            for (Map.Entry<String, List<String>> entry : doctorsBySpecialty.entrySet()) {
                specialtyCount++;
                response.append("**").append(specialtyCount).append(". ").append(entry.getKey()).append("**\n");
                
                for (String doctor : entry.getValue()) {
                    response.append("   • ").append(doctor).append("\n");
                }
                response.append("\n");
            }
            
            response.append("📊 **Total Specialties:** ").append(doctorsBySpecialty.size());
            response.append("\n\nType **menu** to return to main options.");
            
            return response.toString();
            
        } catch (Exception e) {
            return "❌ **Error retrieving specialties**\n\n" +
                   "Please try again later or contact support.\n\n" +
                   "Type **menu** to return to main options.";
        }
    }

    private String handleLocationsOption() {
        try {
            List<String> allDoctors = doctorInfoService.getAllDoctorsWithLocations();
            
            if (allDoctors.isEmpty()) {
                return "❌ **No location information available.**\n\n" +
                       "Please contact our support team.\n\n" +
                       "Type **menu** to return to main options.";
            }

            Map<String, List<String>> doctorsByLocation = new HashMap<>();
            
            for (String doctorInfo : allDoctors) {
                String location = extractLocationFromDoctorInfo(doctorInfo);
                doctorsByLocation.computeIfAbsent(location, k -> new ArrayList<>()).add(doctorInfo);
            }

            StringBuilder response = new StringBuilder();
            response.append("📍 **Doctors by Location:**\n\n");
            
            int locationCount = 0;
            for (Map.Entry<String, List<String>> entry : doctorsByLocation.entrySet()) {
                locationCount++;
                response.append("**").append(locationCount).append(". ").append(entry.getKey()).append("**\n");
                
                for (String doctor : entry.getValue()) {
                    response.append("   • ").append(doctor).append("\n");
                }
                response.append("\n");
            }
            
            response.append("📊 **Total Locations:** ").append(doctorsByLocation.size());
            response.append("\n\nType **menu** to return to main options.");
            
            return response.toString();
            
        } catch (Exception e) {
            return "❌ **Error retrieving location information**\n\n" +
                   "Please try again later or contact support.\n\n" +
                   "Type **menu** to return to main options.";
        }
    }

    private String extractLocationFromDoctorInfo(String doctorInfo) {
        if (doctorInfo.contains(" - ")) {
            String[] parts = doctorInfo.split(" - ");
            if (parts.length >= 2) {
                String lastPart = parts[parts.length - 1];
                if (lastPart.contains("(") && lastPart.contains(")")) {
                    return lastPart.substring(lastPart.indexOf("(") + 1, lastPart.indexOf(")"));
                }
            }
        }
        return "Location not specified";
    }

    private String handleAIResponse(String question) {
        try {
            ResponseEntity<Map> healthCheck = restTemplate.getForEntity(OLLAMA_BASE_URL + "/api/tags", Map.class);
            if (!healthCheck.getStatusCode().is2xxSuccessful()) {
                return handleFallbackResponse(question);
            }
        } catch (Exception e) {
            return handleFallbackResponse(question);
        }

        String enhancedContext = buildEnhancedContext(question);

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", MODEL_NAME);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content",
            "You are Healya, a medical assistant. Provide helpful health information but always remind users to consult " +
            "with healthcare professionals for proper diagnosis and treatment. " +
            "Keep responses concise (2-3 sentences) but informative. " +
            "Use a warm, professional tone. Do not mention returning to menu." +
            "\n\nDoctor Information:\n" + enhancedContext));

        List<Map<String, String>> recentHistory = conversationHistory.size() > 6 ? 
            conversationHistory.subList(conversationHistory.size() - 6, conversationHistory.size()) : 
            conversationHistory;
        
        messages.addAll(recentHistory);
        messages.add(Map.of("role", "user", "content", question));

        payload.put("messages", messages);
        payload.put("stream", false);
        payload.put("options", Map.of("temperature", 0.7, "top_p", 0.9, "top_k", 40));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OLLAMA_CHAT_URL, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> message = (Map<String, Object>) responseBody.get("message");
                if (message != null) {
                    String reply = (String) message.get("content");
                    if (reply != null && !reply.trim().isEmpty()) {
                        conversationHistory.add(Map.of("role", "user", "content", question));
                        conversationHistory.add(Map.of("role", "assistant", "content", reply));
                        return reply.trim() + "\n\n💬 *Continue chatting or type 'menu' for main options*";
                    }
                }
            }

            return handleFallbackResponse(question);

        } catch (ResourceAccessException e) {
            return "🔌 **AI service unavailable.** Here's what I can tell you:\n\n" + 
                   handleFallbackResponse(question);
        } catch (Exception e) {
            return "⚠️ **Connection issue.** " + handleFallbackResponse(question);
        }
    }

    private String handleFallbackResponse(String question) {
        return "I understand you're asking about: **" + question + "**\n\n" +
               "For specific medical questions, I recommend consulting with one of our healthcare professionals. " +
               "You can browse our doctors using the menu options.\n\n" +
               "💬 *Continue chatting or type 'menu' for main options*";
    }

    private String buildEnhancedContext(String question) {
        StringBuilder context = new StringBuilder();
        try {
            String lowerQuestion = question.toLowerCase();
            
            if (lowerQuestion.contains("doctor") || lowerQuestion.contains("specialist")) {
                List<String> allDoctors = doctorInfoService.getAllDoctorsWithSpecialties();
                context.append("Available doctors: ");
                context.append(String.join(", ", allDoctors.subList(0, Math.min(5, allDoctors.size()))));
                context.append("\n");
            }
            
            if (lowerQuestion.contains("specialty") || lowerQuestion.contains("specialties")) {
                String specialties = doctorInfoService.getSpecialtiesList();
                context.append(specialties).append("\n");
            }
            
        } catch (Exception e) {
            context.append("Doctor information temporarily unavailable.");
        }
        return context.toString();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        String healthUrl = OLLAMA_BASE_URL + "/api/tags";
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(healthUrl, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(Map.of(
                    "status", "healthy",
                    "ollama", "running",
                    "model", MODEL_NAME,
                    "doctorService", "connected"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(503)
                .body(Map.of(
                    "status", "unhealthy",
                    "error", "Ollama connection failed: " + e.getMessage()
                ));
        }
        return ResponseEntity.status(503)
            .body(Map.of(
                "status", "unhealthy",
                "error", "Unknown failure"
            ));
    }
}