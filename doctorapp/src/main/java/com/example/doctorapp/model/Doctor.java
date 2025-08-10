package com.example.doctorapp.model;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;    

@Entity
@Table(name = "doctors")
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    
    private String phoneNumber;
    private String location;  
    private Integer experience;
    private String streetAddress;
    private String city;
    private String postalCode;
    private String country;
    private String medicalSchool;
    private Integer graduationYear;
    
    @Column(unique = true, nullable = false)
    private String licenseNumber;
    
    private LocalDate licenseExpiryDate;
    private String hospitalAffiliations;
    private String boardCertifications;
    private BigDecimal consultationFee;
    private String workingDays;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Column(length = 2000)
    private String biography;

    // File Upload Paths
    private String profilePhotoPath;
    private String licenseDocumentPath;
    
    // Add this missing field that the controller uses
    private LocalDate registrationDate;

    // Constructors
    public Doctor() {}

    public Doctor(Long id, String firstName, String lastName, String password, String location, Integer experience) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.experience = experience;
        this.password = password;
    }

    // Getters and Setters - Basic Fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Specialty getSpecialty() { return specialty; }
    public void setSpecialty(Specialty specialty) { this.specialty = specialty; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    // Personal Information Fields
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    // Address Information Fields
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    // Professional Information Fields
    public String getMedicalSchool() { return medicalSchool; }
    public void setMedicalSchool(String medicalSchool) { this.medicalSchool = medicalSchool; }

    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) { this.graduationYear = graduationYear; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public LocalDate getLicenseExpiryDate() { return licenseExpiryDate; }
    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) { this.licenseExpiryDate = licenseExpiryDate; }

    public String getHospitalAffiliations() { return hospitalAffiliations; }
    public void setHospitalAffiliations(String hospitalAffiliations) { this.hospitalAffiliations = hospitalAffiliations; }

    public String getBoardCertifications() { return boardCertifications; }
    public void setBoardCertifications(String boardCertifications) { this.boardCertifications = boardCertifications; }

    // Practice Information Fields
    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }

    public String getWorkingDays() { return workingDays; }
    public void setWorkingDays(String workingDays) { this.workingDays = workingDays; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    // File Upload Fields
    public String getProfilePhotoPath() { return profilePhotoPath; }
    public void setProfilePhotoPath(String profilePhotoPath) { this.profilePhotoPath = profilePhotoPath; }

    public String getLicenseDocumentPath() { return licenseDocumentPath; }
    public void setLicenseDocumentPath(String licenseDocumentPath) { this.licenseDocumentPath = licenseDocumentPath; }
    
    // Registration Date - ADD THIS MISSING FIELD
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isLicenseExpired() {
        return licenseExpiryDate != null && licenseExpiryDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", specialty=" + (specialty != null ? specialty.getName() : "null") +
                ", experience=" + experience +
                ", location='" + location + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}