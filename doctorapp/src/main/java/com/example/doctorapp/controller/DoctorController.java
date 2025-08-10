package com.example.doctorapp.controller;

import com.example.doctorapp.model.Doctor;
import com.example.doctorapp.model.Specialty;
import com.example.doctorapp.repository.DoctorRepository;
import com.example.doctorapp.repository.SpecialtyRepository;
import com.example.doctorapp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Controller
public class DoctorController {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private SpecialtyRepository specialtyRepository;
    
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/register-doctor")
    public String showRegistrationForm(@RequestParam(required = false) String success, Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("specialties", specialtyRepository.findAll());
        model.addAttribute("success", success != null);
        return "doctorregister";
    }

    @PostMapping("/registerdoc")
    public String registerDoctor(
            @ModelAttribute Doctor doctor,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto,
            @RequestParam(value = "licenseDocument", required = false) MultipartFile licenseDocument,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Debug: Print received data
            System.out.println("=== DEBUG: Doctor Registration Started ===");
            System.out.println("Doctor email: " + doctor.getEmail());
            System.out.println("Doctor firstName: " + doctor.getFirstName());
            System.out.println("Doctor lastName: " + doctor.getLastName());
            System.out.println("Doctor phone: " + doctor.getPhoneNumber());
            System.out.println("Doctor license: " + doctor.getLicenseNumber());
            System.out.println("Password provided: " + (password != null && !password.isEmpty()));
            
            // Validate required fields
            if (doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "Email is required.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }
            
            if (doctor.getFirstName() == null || doctor.getFirstName().trim().isEmpty()) {
                model.addAttribute("error", "First name is required.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }
            
            if (doctor.getLastName() == null || doctor.getLastName().trim().isEmpty()) {
                model.addAttribute("error", "Last name is required.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }
            
            if (doctor.getLicenseNumber() == null || doctor.getLicenseNumber().trim().isEmpty()) {
                model.addAttribute("error", "License number is required.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }
            
            // Validate passwords match
            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Password is required.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }
            
            if (!password.equals(confirmPassword)) {
                System.out.println("ERROR: Passwords do not match");
                model.addAttribute("error", "Passwords do not match.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }

            // Validate password strength
            if (password.length() < 8) {
                System.out.println("ERROR: Password too short");
                model.addAttribute("error", "Password must be at least 8 characters long.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }

            // Check if email already exists
            if (!doctorService.isEmailAvailable(doctor.getEmail())) {
                System.out.println("ERROR: Email already exists");
                model.addAttribute("error", "Email address is already registered.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }

            // Check if license number already exists
            if (!doctorService.isLicenseNumberAvailable(doctor.getLicenseNumber())) {
                System.out.println("ERROR: License number already exists");
                model.addAttribute("error", "Medical license number is already registered.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }

            // Validate license expiry date
            if (doctor.getLicenseExpiryDate() != null && doctor.getLicenseExpiryDate().isBefore(LocalDate.now())) {
                System.out.println("ERROR: License expired");
                model.addAttribute("error", "License expiry date cannot be in the past.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }

            // Check specialty is selected
            if (doctor.getSpecialty() == null || doctor.getSpecialty().getId() == null) {
                model.addAttribute("error", "Specialty is required.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }

            // Get and validate specialty
            Long specialtyId = doctor.getSpecialty().getId();
            Specialty specialty = specialtyRepository.findById(specialtyId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid specialty ID: " + specialtyId));
            
            System.out.println("Specialty found: " + specialty.getName());
            doctor.setSpecialty(specialty);

            // Hash password and set it
            String hashedPassword = doctorService.hashPassword(password);
            doctor.setPassword(hashedPassword);
            System.out.println("Password hashed and set");

            // Set default registration date
            if (doctor.getRegistrationDate() == null) {
                doctor.setRegistrationDate(LocalDate.now());
            }
            
            // Initialize file paths to null (will be set if files are uploaded)
            doctor.setProfilePhotoPath(null);
            doctor.setLicenseDocumentPath(null);

            // Handle profile photo upload
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                System.out.println("Processing profile photo: " + profilePhoto.getOriginalFilename());
                try {
                    String profilePhotoPath = saveFile(profilePhoto, "profile-photos");
                    doctor.setProfilePhotoPath(profilePhotoPath);
                    System.out.println("Profile photo saved to: " + profilePhotoPath);
                } catch (Exception e) {
                    System.out.println("ERROR saving profile photo: " + e.getMessage());
                    e.printStackTrace();
                    // Continue with registration, but inform user about photo upload failure
                    model.addAttribute("warning", "Profile photo could not be uploaded, but registration will continue.");
                }
            }

            // Handle license document upload
            if (licenseDocument != null && !licenseDocument.isEmpty()) {
                System.out.println("Processing license document: " + licenseDocument.getOriginalFilename());
                try {
                    String licenseDocumentPath = saveFile(licenseDocument, "license-documents");
                    doctor.setLicenseDocumentPath(licenseDocumentPath);
                    System.out.println("License document saved to: " + licenseDocumentPath);
                } catch (Exception e) {
                    System.out.println("ERROR saving license document: " + e.getMessage());
                    e.printStackTrace();
                    // Continue with registration, but inform user about document upload failure
                    model.addAttribute("warning", "License document could not be uploaded, but registration will continue.");
                }
            }

            System.out.println("About to save doctor to database...");
            System.out.println("Doctor details before save:");
            System.out.println("- Email: " + doctor.getEmail());
            System.out.println("- Name: " + doctor.getFirstName() + " " + doctor.getLastName());
            System.out.println("- License: " + doctor.getLicenseNumber());
            System.out.println("- Specialty: " + (doctor.getSpecialty() != null ? doctor.getSpecialty().getName() : "null"));
            System.out.println("- Registration Date: " + doctor.getRegistrationDate());
            System.out.println("- Profile Photo Path: " + doctor.getProfilePhotoPath());
            System.out.println("- License Document Path: " + doctor.getLicenseDocumentPath());

            // Save the doctor to database
            Doctor savedDoctor = doctorRepository.save(doctor);
            
            if (savedDoctor != null && savedDoctor.getId() != null) {
                System.out.println("SUCCESS: Doctor saved successfully with ID: " + savedDoctor.getId());
                
                // Verify the save by retrieving from database
                Doctor verifyDoctor = doctorRepository.findById(savedDoctor.getId()).orElse(null);
                if (verifyDoctor != null) {
                    System.out.println("VERIFICATION SUCCESS: Doctor found in database");
                    System.out.println("- Verified Email: " + verifyDoctor.getEmail());
                    System.out.println("- Verified Name: " + verifyDoctor.getFirstName() + " " + verifyDoctor.getLastName());
                } else {
                    System.out.println("VERIFICATION FAILED: Doctor not found after save!");
                }
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Doctor registered successfully! Welcome to our platform, Dr. " + 
                    doctor.getFirstName() + " " + doctor.getLastName() + ".");
                return "redirect:/register-doctor?success=true";
                
            } else {
                System.out.println("ERROR: Save returned null or doctor without ID");
                model.addAttribute("error", "Registration failed. Please try again.");
                model.addAttribute("specialties", specialtyRepository.findAll());
                return "doctorregister";
            }

        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("specialties", specialtyRepository.findAll());
            return "doctorregister";
            
        } catch (Exception e) {
            System.out.println("General Exception during registration: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred during registration. Please try again.");
            model.addAttribute("specialties", specialtyRepository.findAll());
            return "doctorregister";
        }
    }

    
  

    
    private String saveFile(MultipartFile file, String subdirectory) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // Validate file size (5MB max)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("File size exceeds maximum limit of 5MB");
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                throw new RuntimeException("Invalid file type. Only images and PDF files are allowed.");
            }

            // Define base upload directory - make sure this directory exists and is writable
            String baseUploadDir = System.getProperty("user.home") + "/doctor-app-uploads";
            
            // Create uploads directory structure
            String uploadsDir = baseUploadDir + "/" + subdirectory + "/";
            Path uploadPath = Paths.get(uploadsDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created upload directory: " + uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String uniqueFilename = System.currentTimeMillis() + "_" + 
                                  (int)(Math.random() * 10000) + fileExtension;
            String filePath = uploadsDir + uniqueFilename;

            Path destinationPath = Paths.get(filePath);

            // Save file to disk
            Files.copy(file.getInputStream(), destinationPath);
            
            System.out.println("File saved successfully: " + filePath);
            return filePath;

        } catch (IOException e) {
            System.out.println("IOException while saving file: " + e.getMessage());
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        } catch (Exception e) {
            System.out.println("General exception while saving file: " + e.getMessage());
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }

    /**
     * Handle file upload errors gracefully
     */
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(org.springframework.web.multipart.MaxUploadSizeExceededException exc, 
                                       Model model, RedirectAttributes redirectAttributes) {
        System.out.println("File upload size exceeded: " + exc.getMessage());
        redirectAttributes.addFlashAttribute("error", "File size exceeds maximum allowed size (5MB)!");
        return "redirect:/register-doctor";
    }
    
}