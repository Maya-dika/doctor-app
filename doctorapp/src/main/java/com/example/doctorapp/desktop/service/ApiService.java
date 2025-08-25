package com.example.doctorapp.desktop.service;

import com.example.doctorapp.dto.AppointmentDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiService {
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public ApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.baseUrl = "http://localhost:8080";
    }
        
    public List<AppointmentDto> getTodaysAppointments() throws Exception {
        String todayStr = LocalDate.now().toString();
        String endpoint = baseUrl + "/api/appointments/today/" + todayStr;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeReference<List<AppointmentDto>> typeRef = new TypeReference<List<AppointmentDto>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Failed to fetch appointments. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    public List<AppointmentDto> getTodaysAppointmentsForDoctor(Long doctorId) throws Exception {
        String todayStr = LocalDate.now().toString();
        String endpoint = baseUrl + "/api/appointments/today/" + todayStr + "/doctor/" + doctorId;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeReference<List<AppointmentDto>> typeRef = new TypeReference<List<AppointmentDto>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Failed to fetch doctor appointments. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    public boolean checkInAppointment(Long appointmentId) throws Exception {
        String endpoint = baseUrl + "/api/appointments/checkin/" + appointmentId;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response.statusCode() == 200;
    }
    
    /**
     * Process payment by updating appointment payment fields
     */
    public boolean processPayment(Long appointmentId, double paymentAmount, String paymentMethod, String paymentStatus, String paymentDate) {
        try {
            String url = baseUrl + "/api/appointments/billing/" + appointmentId;
            
            // Create request body with payment data
            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("paymentAmount", paymentAmount);
            paymentData.put("paymentMethod", paymentMethod);
            paymentData.put("paymentDate", paymentDate);
            paymentData.put("paymentStatus", paymentStatus);
            paymentData.put("status", "COMPLETED");
            
            // Convert to JSON
            String jsonBody = objectMapper.writeValueAsString(paymentData);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody)) // Using PUT to update appointment
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            return response.statusCode() == 200;
                
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get appointments by date (for daily billing reports)
     */
    public List<AppointmentDto> getAppointmentsByDate(LocalDate date) throws Exception {
        String endpoint = baseUrl + "/api/appointments/date/" + date.toString();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeReference<List<AppointmentDto>> typeRef = new TypeReference<List<AppointmentDto>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Failed to fetch appointments by date. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
}