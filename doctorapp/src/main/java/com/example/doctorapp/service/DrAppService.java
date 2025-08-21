package com.example.doctorapp.service;

import com.example.doctorapp.model.DoctorAppointments;
import com.example.doctorapp.repository.DrAppRepository;
import org.springframework.stereotype.Service;
import com.example.doctorapp.model.Patient;
import java.util.List;

@Service
public class DrAppService {
    
    private final DrAppRepository drAppRepository;

    public DrAppService(DrAppRepository drAppRepository) {
        this.drAppRepository = drAppRepository;
    }
    public List<DoctorAppointments> findByDoctorId(Long doctorId) {
        return drAppRepository.findByDoctor_Id(doctorId);
    }

    public List<DoctorAppointments> findByPatientId(Long patientId) {
        return drAppRepository.findByPatient_Id(patientId);
    }

    public DoctorAppointments save(DoctorAppointments drappointments) {
        return drAppRepository.save(drappointments);
    }

    public void updateStatus(Long id, DoctorAppointments.Status status) {
        DoctorAppointments drappointments = drAppRepository.findById(id).orElseThrow();
        drappointments.setStatus(status); 
        drAppRepository.save(drappointments);
    }
}
