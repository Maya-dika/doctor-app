package com.example.doctorapp.service;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.ArrayList;

@Service
public class DoctorInfoService {
    
    private static final Logger logger = LoggerFactory.getLogger(DoctorInfoService.class);
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    // Enhanced method that returns List<String> for better controller compatibility
    public List<String> getDoctorsBySpecialty(String specialty) {
        try {
            List<Doctor> doctors = doctorRepository.findDoctorsBySpecialtyName(specialty);
            
            if (doctors.isEmpty()) {
                logger.info("No doctors found for specialty: {}", specialty);
                return Collections.singletonList("No doctors found for specialty: " + specialty);
            }
            
            return doctors.stream()
                    .map(doctor -> String.format("Dr. %s %s - %s (%d years experience)", 
                        doctor.getFirstName(), 
                        doctor.getLastName(), 
                        getSpecialtyName(doctor),
                        doctor.getExperience()))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching doctors by specialty: {}", specialty, e);
            return Collections.singletonList("Unable to retrieve doctors for " + specialty);
        }
    }
    
    // Keep original method for backward compatibility
    public String getDoctorsBySpecialtyString(String specialty) {
        List<String> doctors = getDoctorsBySpecialty(specialty);
        return String.join(", ", doctors);
    }
    
    public String getDoctorInfo(String doctorName) {
        try {
            List<Doctor> doctors = doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                    doctorName, doctorName);
                    
            if (doctors.isEmpty()) {
                return "No doctor found with name: " + doctorName;
            }
            
            return doctors.stream()
                    .map(doctor -> String.format("Dr. %s %s - %s, Experience: %d years, Location: %s", 
                        doctor.getFirstName(), 
                        doctor.getLastName(), 
                        getSpecialtyName(doctor),
                        doctor.getExperience(),
                        doctor.getLocation() != null ? doctor.getLocation() : "Not specified"))
                    .collect(Collectors.joining("; "));
                    
        } catch (Exception e) {
            logger.error("Error fetching doctor info for: {}", doctorName, e);
            return "Unable to retrieve information for doctor: " + doctorName;
        }
    }
    
    @Cacheable("specialties")
    public String getSpecialtiesList() {
        try {
            List<String> specialties = doctorRepository.findDistinctSpecialties();
            if (specialties.isEmpty()) {
                return "No specialties available at this time";
            }
            return "Available specialties: " + String.join(", ", specialties);
        } catch (Exception e) {
            logger.error("Error fetching specialties list", e);
            return "Unable to retrieve specialties list";
        }
    }
    
    @Cacheable("allDoctors")
    public List<String> getAllDoctorsWithSpecialties() {
        try {
            List<Doctor> doctors = doctorRepository.findAll();
            if (doctors.isEmpty()) {
                return Collections.singletonList("No doctors available at this time");
            }
            
            return doctors.stream()
                    .map(doctor -> String.format("Dr. %s %s - %s (%d years experience)", 
                            doctor.getFirstName(),
                            doctor.getLastName(),
                            getSpecialtyName(doctor),
                            doctor.getExperience()))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching all doctors", e);
            return Collections.singletonList("Unable to retrieve doctors list");
        }
    }
    
    // NEW METHOD: Get all doctors with their locations
    @Cacheable("allDoctorsWithLocations")
    public List<String> getAllDoctorsWithLocations() {
        try {
            List<Doctor> doctors = doctorRepository.findAll();
            if (doctors.isEmpty()) {
                return Collections.singletonList("No doctors available at this time");
            }
            
            return doctors.stream()
                    .map(doctor -> String.format("Dr. %s %s - %s (%s)", 
                            doctor.getFirstName(),
                            doctor.getLastName(),
                            getSpecialtyName(doctor),
                            doctor.getLocation() != null ? doctor.getLocation() : "Location not specified"))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching all doctors with locations", e);
            return Collections.singletonList("Unable to retrieve doctors with location information");
        }
    }
    
    // Enhanced method: Get doctors grouped by specialty for better organization
    @Cacheable("doctorsBySpecialty")
    public Map<String, List<String>> getAllDoctorsGroupedBySpecialty() {
        try {
            List<Doctor> doctors = doctorRepository.findAll();
            
            if (doctors.isEmpty()) {
                return Collections.emptyMap();
            }
            
            return doctors.stream()
                    .filter(doctor -> doctor.getSpecialty() != null)
                    .collect(Collectors.groupingBy(
                            doctor -> getSpecialtyName(doctor),
                            Collectors.mapping(
                                    doctor -> String.format("Dr. %s %s (%d years experience)", 
                                            doctor.getFirstName(),
                                            doctor.getLastName(),
                                            doctor.getExperience()),
                                    Collectors.toList()
                            )
                    ));
                    
        } catch (Exception e) {
            logger.error("Error grouping doctors by specialty", e);
            return Collections.emptyMap();
        }
    }
    
    // NEW METHOD: Get doctors grouped by location
    @Cacheable("doctorsByLocation")
    public Map<String, List<String>> getAllDoctorsGroupedByLocation() {
        try {
            List<Doctor> doctors = doctorRepository.findAll();
            
            if (doctors.isEmpty()) {
                return Collections.emptyMap();
            }
            
            return doctors.stream()
                    .collect(Collectors.groupingBy(
                            doctor -> doctor.getLocation() != null ? doctor.getLocation() : "Location not specified",
                            Collectors.mapping(
                                    doctor -> String.format("Dr. %s %s - %s (%d years)", 
                                            doctor.getFirstName(),
                                            doctor.getLastName(),
                                            getSpecialtyName(doctor),
                                            doctor.getExperience()),
                                    Collectors.toList()
                            )
                    ));
                    
        } catch (Exception e) {
            logger.error("Error grouping doctors by location", e);
            return Collections.emptyMap();
        }
    }
    
    // Enhanced method: Search doctors by multiple keywords
    public List<String> searchDoctorsByKeywords(List<String> keywords) {
        try {
            List<Doctor> allDoctors = doctorRepository.findAll();
            List<String> results = new ArrayList<>();
            
            for (Doctor doctor : allDoctors) {
                String doctorInfo = (doctor.getFirstName() + " " + doctor.getLastName() + " " + 
                                   getSpecialtyName(doctor) + " " + 
                                   (doctor.getLocation() != null ? doctor.getLocation() : "")).toLowerCase();
                
                boolean matches = keywords.stream()
                        .anyMatch(keyword -> doctorInfo.contains(keyword.toLowerCase()));
                
                if (matches) {
                    results.add(String.format("Dr. %s %s - %s (%d years experience)", 
                            doctor.getFirstName(),
                            doctor.getLastName(),
                            getSpecialtyName(doctor),
                            doctor.getExperience()));
                }
            }
            
            return results.isEmpty() ? 
                    Collections.singletonList("No doctors found matching your criteria") : 
                    results;
                    
        } catch (Exception e) {
            logger.error("Error searching doctors by keywords: {}", keywords, e);
            return Collections.singletonList("Unable to search doctors");
        }
    }
    
    // Enhanced method: Get doctors by location
    public List<String> getDoctorsByLocation(String location) {
        try {
            List<Doctor> doctors = doctorRepository.findByLocationContainingIgnoreCase(location);
            
            if (doctors.isEmpty()) {
                return Collections.singletonList("No doctors found in location: " + location);
            }
            
            return doctors.stream()
                    .map(doctor -> String.format("Dr. %s %s - %s (%s)", 
                            doctor.getFirstName(),
                            doctor.getLastName(),
                            getSpecialtyName(doctor),
                            doctor.getLocation()))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching doctors by location: {}", location, e);
            return Collections.singletonList("Unable to retrieve doctors for location: " + location);
        }
    }
    
    // Enhanced method: Get doctors with minimum experience
    public List<String> getDoctorsByMinExperience(Integer minExperience) {
        try {
            List<Doctor> doctors = doctorRepository.findByExperienceGreaterThanEqual(minExperience);
            
            if (doctors.isEmpty()) {
                return Collections.singletonList("No doctors found with " + minExperience + "+ years experience");
            }
            
            return doctors.stream()
                    .sorted((d1, d2) -> Integer.compare(d2.getExperience(), d1.getExperience())) // Sort by experience desc
                    .map(doctor -> String.format("Dr. %s %s - %s (%d years experience)", 
                            doctor.getFirstName(),
                            doctor.getLastName(),
                            getSpecialtyName(doctor),
                            doctor.getExperience()))
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching doctors by min experience: {}", minExperience, e);
            return Collections.singletonList("Unable to retrieve experienced doctors");
        }
    }
    
    // Enhanced method: Get quick doctor stats for summary
    public String getDoctorStats() {
        try {
            List<Doctor> allDoctors = doctorRepository.findAll();
            List<String> specialties = doctorRepository.findDistinctSpecialties();
            
            Map<String, Long> doctorCountBySpecialty = allDoctors.stream()
                    .filter(doctor -> doctor.getSpecialty() != null)
                    .collect(Collectors.groupingBy(
                            doctor -> getSpecialtyName(doctor),
                            Collectors.counting()
                    ));
            
            Map<String, Long> doctorCountByLocation = allDoctors.stream()
                    .collect(Collectors.groupingBy(
                            doctor -> doctor.getLocation() != null ? doctor.getLocation() : "Location not specified",
                            Collectors.counting()
                    ));
            
            StringBuilder stats = new StringBuilder();
            stats.append(String.format("We have %d doctors across %d specialties and %d locations:\n", 
                    allDoctors.size(), specialties.size(), doctorCountByLocation.size()));
            
            stats.append("\nBy Specialty:\n");
            doctorCountBySpecialty.forEach((specialty, count) -> 
                    stats.append(String.format("- %s: %d doctors\n", specialty, count)));
            
            stats.append("\nBy Location:\n");
            doctorCountByLocation.forEach((location, count) -> 
                    stats.append(String.format("- %s: %d doctors\n", location, count)));
            
            return stats.toString();
            
        } catch (Exception e) {
            logger.error("Error generating doctor stats", e);
            return "Unable to generate doctor statistics";
        }
    }
    
    // Helper method to safely get specialty name
    private String getSpecialtyName(Doctor doctor) {
        return doctor.getSpecialty() != null ? doctor.getSpecialty().getName() : "General Practice";
    }
    
    // Enhanced method: Check if doctor exists by name
    public boolean doctorExists(String doctorName) {
        try {
            List<Doctor> doctors = doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                    doctorName, doctorName);
            return !doctors.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking if doctor exists: {}", doctorName, e);
            return false;
        }
    }
    
    // Enhanced method: Get emergency contact info
    public List<String> getEmergencyContacts() {
        return List.of(
            "🚨 For medical emergencies, call 911 immediately",
            "🏥 Emergency Department: Available 24/7",
            "📞 Nurse Hotline: Contact our platform for non-emergency medical questions"
        );
    }
}