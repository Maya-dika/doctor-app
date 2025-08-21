package com.example.doctorapp.repository;

import com.example.doctorapp.model.Analytics;
import com.example.doctorapp.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.math.BigDecimal;


public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    
    List<Analytics> findByDoctor_Id(Long doctorId);
    
    List<Analytics>findByTotalRevenue(BigDecimal totalRevenue);
    
    List<Analytics> findByTotalRevenueGreaterThan(BigDecimal minRevenue);
    
}
