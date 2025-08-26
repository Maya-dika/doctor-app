package com.example.doctorapp.config;

import com.example.doctorapp.model.Specialty;
import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.repository.SpecialtyRepository;
import com.example.doctorapp.repository.PatientRepository;
import com.example.doctorapp.repository.DoctorRepository;
import com.example.doctorapp.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SpecialtyRepository specialtyRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only load data if the specialties table is empty
        if (specialtyRepository.count() == 0) {
            loadSpecialties();
        }
        
        // Load sample patients if the patients table is empty
        if (patientRepository.count() == 0) {
            loadSamplePatients();
        }
        
        // Load sample doctors if the doctors table is empty
        if (doctorRepository.count() == 0) {
            //loadSampleDoctors();
        }
        
        // Load sample appointments if the appointments table is empty
        if (appointmentRepository.count() == 0) {
            //loadSampleAppointments();
        }
    }

    private void loadSpecialties() {
        // Common medical specialties
        String[] specialties = {
            "General Medicine",
            "Cardiology",
            "Dermatology", 
            "Endocrinology",
            "Gastroenterology",
            "Hematology",
            "Infectious Diseases",
            "Nephrology",
            "Neurology",
            "Oncology",
            "Pulmonology",
            "Rheumatology",
            "General Surgery",
            "Orthopedic Surgery",
            "Neurosurgery",
            "Plastic Surgery",
            "Cardiac Surgery",
            "Pediatrics",
            "Obstetrics and Gynecology",
            "Psychiatry",
            "Radiology",
            "Anesthesiology",
            "Emergency Medicine",
            "Family Medicine",
            "Physical Medicine and Rehabilitation",
            "Pathology",
            "Ophthalmology",
            "Otolaryngology (ENT)",
            "Urology",
            "Allergy and Immunology"
        };

        for (String specialtyName : specialties) {
            Specialty specialty = new Specialty(specialtyName);
            specialtyRepository.save(specialty);
        }

        System.out.println("Loaded " + specialties.length + " specialties into the database.");
    }
    
    private void loadSamplePatients() {
        // Sample patient data
        Patient[] patients = {
            new Patient("John", "password123", "Smith", 35, "Male", "Hypertension, Diabetes", "john.smith@email.com", "123 Main St, City", "555-0101"),
            new Patient("Sarah", "password123", "Johnson", 28, "Female", "Asthma", "sarah.johnson@email.com", "456 Oak Ave, Town", "555-0102"),
            new Patient("Michael", "password123", "Brown", 42, "Male", "None", "michael.brown@email.com", "789 Pine Rd, Village", "555-0103"),
            new Patient("Emily", "password123", "Davis", 31, "Female", "Migraine", "emily.davis@email.com", "321 Elm St, Borough", "555-0104"),
            new Patient("David", "password123", "Wilson", 39, "Male", "Back pain", "david.wilson@email.com", "654 Maple Dr, County", "555-0105"),
            new Patient("Lisa", "password123", "Anderson", 26, "Female", "Anxiety", "lisa.anderson@email.com", "987 Cedar Ln, District", "555-0106"),
            new Patient("Robert", "password123", "Taylor", 45, "Male", "High cholesterol", "robert.taylor@email.com", "147 Birch Way, Parish", "555-0107"),
            new Patient("Jennifer", "password123", "Martinez", 33, "Female", "Insomnia", "jennifer.martinez@email.com", "258 Spruce Ct, Township", "555-0108"),
            new Patient("William", "password123", "Garcia", 37, "Male", "None", "william.garcia@email.com", "369 Willow Pl, Municipality", "555-0109"),
            new Patient("Amanda", "password123", "Rodriguez", 29, "Female", "Seasonal allergies", "amanda.rodriguez@email.com", "741 Aspen Blvd, Territory", "555-0110")
        };

        for (Patient patient : patients) {
            patientRepository.save(patient);
        }

        System.out.println("Loaded " + patients.length + " sample patients into the database.");
    }
    
    private void loadSampleDoctors() {
        // Get some specialties for the doctors
        Specialty generalMedicine = specialtyRepository.findByName("General Medicine").orElse(null);
        Specialty cardiology = specialtyRepository.findByName("Cardiology").orElse(null);
        Specialty dermatology = specialtyRepository.findByName("Dermatology").orElse(null);
        Specialty pediatrics = specialtyRepository.findByName("Pediatrics").orElse(null);
        
        if (generalMedicine == null || cardiology == null || dermatology == null || pediatrics == null) {
            System.out.println("Some specialties not found, skipping doctor creation");
            return;
        }
        
        // Sample doctor data
        Doctor[] doctors = {
            createDoctor("Dr. James", "Wilson", "james.wilson@hospital.com", "password123", 
                      "123 Doctor St", "Medical City", "12345", "Lebanon", "Downtown Medical Center",
                      generalMedicine, 15, "Harvard Medical School", 2008, "MD123456", 
                      java.time.LocalDate.of(2025, 12, 31), "City General Hospital", "Board Certified in Internal Medicine",
                      new java.math.BigDecimal("150.00"), "Monday to Friday", "09:00", "17:00", 
                      "Experienced general practitioner with 15 years of experience"),
            
            createDoctor("Dr. Sarah", "Johnson", "sarah.johnson@hospital.com", "password123", 
                      "456 Heart Ave", "Cardio City", "67890", "Lebanon", "Cardiac Care Center",
                      cardiology, 12, "Johns Hopkins University", 2011, "MD789012", 
                      java.time.LocalDate.of(2025, 10, 15), "Heart Institute", "Board Certified in Cardiology",
                      new java.math.BigDecimal("200.00"), "Monday to Friday", "08:00", "16:00", 
                      "Specialized in interventional cardiology"),
            
            createDoctor("Dr. Michael", "Brown", "michael.brown@hospital.com", "password123", 
                      "789 Skin Rd", "Derma Town", "11111", "Lebanon", "Skin Care Clinic",
                      dermatology, 8, "Stanford Medical School", 2015, "MD345678", 
                      java.time.LocalDate.of(2025, 11, 20), "Dermatology Associates", "Board Certified in Dermatology",
                      new java.math.BigDecimal("180.00"), "Monday to Saturday", "10:00", "18:00", 
                      "Expert in cosmetic and medical dermatology"),
            
            createDoctor("Dr. Emily", "Davis", "emily.davis@hospital.com", "password123", 
                      "321 Child St", "Pediatric City", "22222", "Lebanon", "Children's Medical Center",
                      pediatrics, 10, "Yale Medical School", 2013, "MD901234", 
                      java.time.LocalDate.of(2025, 9, 30), "Children's Hospital", "Board Certified in Pediatrics",
                      new java.math.BigDecimal("120.00"), "Monday to Friday", "09:00", "17:00", 
                      "Dedicated to providing the best care for children")
        };

        for (Doctor doctor : doctors) {
            doctorRepository.save(doctor);
        }

        System.out.println("Loaded " + doctors.length + " sample doctors into the database.");
    }
    
    private Doctor createDoctor(String firstName, String lastName, String email, String password,
                               String streetAddress, String city, String postalCode, String country, String location,
                               Specialty specialty, Integer experience, String medicalSchool, Integer graduationYear,
                               String licenseNumber, java.time.LocalDate licenseExpiryDate, String hospitalAffiliations,
                               String boardCertifications, java.math.BigDecimal consultationFee, String workingDays,
                               String startTime, String endTime, String biography) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setPassword(password);
        doctor.setStreetAddress(streetAddress);
        doctor.setCity(city);
        doctor.setPostalCode(postalCode);
        doctor.setCountry(country);
        doctor.setLocation(location);
        doctor.setSpecialty(specialty);
        doctor.setExperience(experience);
        doctor.setMedicalSchool(medicalSchool);
        doctor.setGraduationYear(graduationYear);
        doctor.setLicenseNumber(licenseNumber);
        doctor.setLicenseExpiryDate(licenseExpiryDate);
        doctor.setHospitalAffiliations(hospitalAffiliations);
        doctor.setBoardCertifications(boardCertifications);
        doctor.setConsultationFee(consultationFee);
        doctor.setWorkingDays(workingDays);
        doctor.setStartTime(java.time.LocalTime.parse(startTime));
        doctor.setEndTime(java.time.LocalTime.parse(endTime));
        doctor.setBiography(biography);
        return doctor;
    }
    
    private void loadSampleAppointments() {
        // Get some patients and doctors for appointments
        java.util.List<Patient> patients = patientRepository.findAll();
        java.util.List<Doctor> doctors = doctorRepository.findAll();
        
        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("No patients or doctors found, skipping appointment creation");
            return;
        }
        
        // Create appointments for the current month and last month
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate startDate = today.minusDays(30); // Last 30 days
        
        java.util.List<Appointment> appointments = new java.util.ArrayList<>();
        
        for (int i = 0; i < 25; i++) {
            // Random date within the last 30 days
            java.time.LocalDate appointmentDate = startDate.plusDays((long) (Math.random() * 30));
            
            // Random patient and doctor
            Patient patient = patients.get((int) (Math.random() * patients.size()));
            Doctor doctor = doctors.get((int) (Math.random() * doctors.size()));
            
            // Random time between 9 AM and 5 PM
            java.time.LocalTime appointmentTime = java.time.LocalTime.of(9 + (int) (Math.random() * 8), 
                                                                        (int) (Math.random() * 4) * 15);
            
            // Random status
            String[] statuses = {"COMPLETED", "PENDING", "CANCELLED"};
            String status = statuses[(int) (Math.random() * statuses.length)];
            
            // Random payment amount
            java.math.BigDecimal paymentAmount = new java.math.BigDecimal(100 + (int) (Math.random() * 200));
            
            // Random payment status and method
            String paymentStatus = status.equals("COMPLETED") ? "PAID" : "PENDING";
            String[] paymentMethods = {"CASH", "CREDIT_CARD", "INSURANCE"};
            String paymentMethod = paymentMethods[(int) (Math.random() * paymentMethods.length)];
            
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setAppointmentDate(appointmentDate);
            appointment.setAppointmentTime(appointmentTime);
            appointment.setStatus(status);
            appointment.setPaymentAmount(paymentAmount);
            appointment.setPaymentStatus(paymentStatus);
            appointment.setPaymentMethod(paymentMethod);
            appointment.setPaymentDate(appointmentDate);
            appointment.setNotes("Sample appointment for reporting purposes");
            
            appointments.add(appointment);
        }
        
        appointmentRepository.saveAll(appointments);
        System.out.println("Loaded " + appointments.size() + " sample appointments into the database.");
    }
}