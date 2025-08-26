package com.example.doctorapp.controller.api;

import com.example.doctorapp.dto.PatientDto;
import com.example.doctorapp.mapper.PatientMapper;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.service.PatientService;
import com.example.doctorapp.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientApiController {
    
    private final PatientService patientService;
    private final ExcelExportService excelExportService;
    
    public PatientApiController(PatientService patientService, ExcelExportService excelExportService) {
        this.patientService = patientService;
        this.excelExportService = excelExportService;
    }
    
    /**
     * Test endpoint to verify controller is working
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Patient API Controller is working!");
    }
    
    /**
     * Get all patients
     */
    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            List<PatientDto> patientDtos = patients.stream()
                .map(PatientMapper::toDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(patientDtos);
        } catch (Exception e) {
            e.printStackTrace(); // Add logging to see what's happening
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get patient by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if (patient != null) {
                return ResponseEntity.ok(PatientMapper.toDto(patient));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Create new patient
     */
    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto patientDto) {
        try {
            Patient patient = PatientMapper.toEntity(patientDto);
            patient.setPassword(patientService.hashPassword("defaultPassword123"));
            Patient savedPatient = patientService.savePatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(PatientMapper.toDto(savedPatient));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Update patient
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, @RequestBody PatientDto patientDto) {
        try {
            Patient existingPatient = patientService.getPatientById(id);
            if (existingPatient == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Update fields
            existingPatient.setFirstName(patientDto.getFirstName());
            existingPatient.setLastName(patientDto.getLastName());
            existingPatient.setEmail(patientDto.getEmail());
            existingPatient.setPhoneNumber(patientDto.getPhoneNumber());
            existingPatient.setAddress(patientDto.getAddress());
            existingPatient.setAge(patientDto.getAge());
            existingPatient.setGender(patientDto.getGender());
            existingPatient.setMedicalHistory(patientDto.getMedicalHistory());
            
            Patient updatedPatient = patientService.savePatient(existingPatient);
            return ResponseEntity.ok(PatientMapper.toDto(updatedPatient));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Delete patient
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Search patients by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<PatientDto>> searchPatients(@RequestParam String name) {
        try {
            List<Patient> patients = patientService.searchPatientsByName(name);
            List<PatientDto> patientDtos = patients.stream()
                .map(PatientMapper::toDto)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(patientDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Export patients to Excel
     */
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportPatientsToExcel() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            List<PatientDto> patientDtos = patients.stream()
                .map(PatientMapper::toDto)
                .collect(Collectors.toList());
            
            byte[] excelData = excelExportService.exportPatientsToExcel(patientDtos);
            
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"patients.xlsx\"")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
