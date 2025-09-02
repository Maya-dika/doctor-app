package com.example.doctorapp.service;

import com.example.doctorapp.model.AdditionalPatientInfo;
import com.example.doctorapp.repository.PatientProfileRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientProfileService {
    
    private final PatientProfileRepository repository;
    
    public PatientProfileService(PatientProfileRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public AdditionalPatientInfo save(AdditionalPatientInfo info) {
        try {
            // If updating existing record, fetch it first
            if (info.getPatient() != null && info.getPatient().getId() != null) {
                AdditionalPatientInfo existing = findByPatientId(info.getPatient().getId());
                if (existing != null) {
                    info.setId(existing.getId()); // Set the ID for update
                }
            }
            
            AdditionalPatientInfo saved = repository.save(info);
            return saved;
        } catch (Exception e) {
            System.err.println("Error in service save method: " + e.getMessage());
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public AdditionalPatientInfo findByPatientId(Long patientId) {
        return repository.findByPatientId(patientId).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public List<AdditionalPatientInfo> findAll() {
        return repository.findAll();
    }
    
    @Transactional(readOnly = true)
    public AdditionalPatientInfo findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}   