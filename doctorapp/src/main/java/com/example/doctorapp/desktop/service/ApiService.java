package com.example.doctorapp.desktop.service;

import com.example.doctorapp.dto.AppointmentDto;
import com.example.doctorapp.dto.PatientDto;
import com.example.doctorapp.dto.ReportDto;
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
    
    /**
     * Get all patients
     */
    public List<PatientDto> getAllPatients() throws Exception {
        String endpoint = baseUrl + "/api/patients";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeReference<List<PatientDto>> typeRef = new TypeReference<List<PatientDto>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Failed to fetch patients. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    /**
     * Get patient by ID
     */
    public PatientDto getPatientById(Long patientId) throws Exception {
        String endpoint = baseUrl + "/api/patients/" + patientId;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), PatientDto.class);
        } else {
            throw new RuntimeException("Failed to fetch patient. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    /**
     * Search patients by name, email, or phone
     */
    public List<PatientDto> searchPatients(String searchTerm) throws Exception {
        String endpoint = baseUrl + "/api/patients/search?name=" + java.net.URLEncoder.encode(searchTerm, "UTF-8");
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            TypeReference<List<PatientDto>> typeRef = new TypeReference<List<PatientDto>>() {};
            return objectMapper.readValue(response.body(), typeRef);
        } else {
            throw new RuntimeException("Failed to search patients. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    /**
     * Search appointments by patient name, doctor name, date, or status
     */
    public List<AppointmentDto> searchAppointments(String searchTerm) throws Exception {
        String endpoint = baseUrl + "/api/appointments/search?term=" + java.net.URLEncoder.encode(searchTerm, "UTF-8");
        
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
            throw new RuntimeException("Failed to search appointments. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    /**
     * Create new patient
     */
    public boolean createPatient(PatientDto patient) throws Exception {
        String endpoint = baseUrl + "/api/patients";
        
        String jsonBody = objectMapper.writeValueAsString(patient);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response.statusCode() == 201 || response.statusCode() == 200;
    }
    
    /**
     * Update patient
     */
    public boolean updatePatient(Long patientId, PatientDto patient) throws Exception {
        String endpoint = baseUrl + "/api/patients/" + patientId;
        
        String jsonBody = objectMapper.writeValueAsString(patient);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response.statusCode() == 200;
    }
    
    /**
     * Delete patient
     */
    public boolean deletePatient(Long patientId) throws Exception {
        String endpoint = baseUrl + "/api/patients/" + patientId;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return response.statusCode() == 200 || response.statusCode() == 204;
    }
    
    // Report methods
    public ReportDto getCurrentMonthReport() throws Exception {
        String endpoint = baseUrl + "/api/reports/current-month";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ReportDto.class);
        } else {
            throw new RuntimeException("Failed to fetch current month report. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    public ReportDto getLast30DaysReport() throws Exception {
        String endpoint = baseUrl + "/api/reports/last-30-days";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ReportDto.class);
        } else {
            throw new RuntimeException("Failed to fetch last 30 days report. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    public ReportDto getCurrentYearReport() throws Exception {
        String endpoint = baseUrl + "/api/reports/current-year";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ReportDto.class);
        } else {
            throw new RuntimeException("Failed to fetch current year report. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    public ReportDto generateCustomReport(LocalDate startDate, LocalDate endDate) throws Exception {
        String endpoint = baseUrl + "/api/reports/generate?startDate=" + startDate + "&endDate=" + endDate;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), ReportDto.class);
        } else {
            throw new RuntimeException("Failed to generate custom report. Status: " + response.statusCode() + 
                                     ", Body: " + response.body());
        }
    }
    
    // Excel Export methods
    public byte[] exportPatientsToExcel() throws Exception {
        String endpoint = baseUrl + "/api/patients/export/excel";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Failed to export patients to Excel. Status: " + response.statusCode());
        }
    }
    
    public byte[] exportCurrentMonthReportToExcel() throws Exception {
        String endpoint = baseUrl + "/api/reports/export/current-month/excel";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Failed to export current month report to Excel. Status: " + response.statusCode());
        }
    }
    
    public byte[] exportLast30DaysReportToExcel() throws Exception {
        String endpoint = baseUrl + "/api/reports/export/last-30-days/excel";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Failed to export last 30 days report to Excel. Status: " + response.statusCode());
        }
    }
}