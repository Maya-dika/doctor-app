package com.example.doctorapp.service;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.repository.DoctorRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    
    public List<Doctor> getDoctorsByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }
    
    public List<Doctor> getDoctorsBySpecialtyId(Long specialtyId) {
        return doctorRepository.findBySpecialtyId(specialtyId);
    }
    
    
    public List<Doctor>getDoctorsByLocation(String location){
        return doctorRepository.findByLocation(location);
    }
      
      
    public List<Doctor> getDoctorsByLastName(String lastName) {
        return doctorRepository.findByLastName(lastName);
    }

    public List<Doctor> getDoctorByEmail(String email) {
       return doctorRepository.findByEmail(email);
    }
    public List<Doctor> getDoctorsByExperience(int experience) {
       return doctorRepository.findByExperience(experience);
    }
    
}
