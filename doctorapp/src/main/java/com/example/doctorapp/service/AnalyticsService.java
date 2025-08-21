package com.example.doctorapp.service;

import com.example.doctorapp.model.Analytics;
import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.repository.AnalyticsRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AnalyticsService {
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }
    
       public Analytics save(Analytics analytics) {
        return analyticsRepository.save(analytics);
    }

    public List<Analytics> getAnalyticssByDoctorId(Long doctorId) {
        return analyticsRepository.findByDoctor_Id(doctorId);
    }
    
     public List<Analytics> getAnalyticssByTotalRevenue(BigDecimal totalRevenue) {
        return analyticsRepository.findByTotalRevenue(totalRevenue);
    }
    
    
}
