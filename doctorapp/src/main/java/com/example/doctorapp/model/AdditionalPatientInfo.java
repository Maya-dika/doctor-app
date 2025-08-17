package com.example.doctorapp.model;

import java.util.List;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="additional_patient_info")
public class AdditionalPatientInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
     @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    // Health Profile & Baseline Metrics
    private Integer heightFeet;
    private Integer heightInches;
    private Double currentWeight;
    private Double targetWeight;
    private String bloodType;
    private String activityLevel;
    private Integer restingHeartRate;
    private Integer baselineSystolic;
    private Integer baselineDiastolic;
    private Double normalBodyTemp;
    
    // Medical History & Risk Factors
    private String smokingStatus;
    private String alcoholConsumption;
    
    
    @ElementCollection
    @CollectionTable(
        name = "patient_chronic_conditions",
        joinColumns = @JoinColumn(name = "patient_info_id")
    )
    @Column(name = "condition")
    private List<String> chronicConditions;
    
    
    private String otherConditionsDetails;
    private String currentMedications;
    private String allergiesText;
    
    @ElementCollection
    @CollectionTable(
        name = "patient_family_history",
        joinColumns = @JoinColumn(name = "patient_info_id")
    )
    @Column(name = "family_member_condition")
    private List<String> familyHistory;
    
    
    // Emergency Contact
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;
    private String emergencyContactEmail;
    
    // Insurance Information
    private String insuranceProvider;
    private String policyNumber;
    private String groupNumber;
    private String insurancePhone;
    
    // Health Preferences & Monitoring Settings
    @ElementCollection
    @CollectionTable(
        name = "patient_alert_preferences",
        joinColumns = @JoinColumn(name = "patient_info_id")
    )
    @Column(name = "alert")
    private List<String> alertPreferences;
    
    
    private Integer systolicGoal;
    private Integer diastolicGoal;
    private Integer minTargetHR;
    private Integer maxTargetHR;
    private Integer fastingGlucoseTarget;
    private Integer totalCholesterolTarget;
    private String weightMonitoringFreq;
    private String bpMonitoringFreq;
    private String glucoseMonitoringFreq;
    private String reminderPreference;
    private String healthGoalsNotes;
    
    // File uploads (you might want to handle these separately)
    // private MultipartFile profilePhoto;
    // private MultipartFile insuranceCardPhoto;
    
    // Default constructor
    public AdditionalPatientInfo() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    
    
    public Integer getHeightFeet() {
        return heightFeet;
    }
    
    public void setHeightFeet(Integer heightFeet) {
        this.heightFeet = heightFeet;
    }
    
    public Integer getHeightInches() {
        return heightInches;
    }
    
    public void setHeightInches(Integer heightInches) {
        this.heightInches = heightInches;
    }
    
    public Double getCurrentWeight() {
        return currentWeight;
    }
    
    public void setCurrentWeight(Double currentWeight) {
        this.currentWeight = currentWeight;
    }
    
    public Double getTargetWeight() {
        return targetWeight;
    }
    
    public void setTargetWeight(Double targetWeight) {
        this.targetWeight = targetWeight;
    }
    
    public String getBloodType() {
        return bloodType;
    }
    
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    
    public String getActivityLevel() {
        return activityLevel;
    }
    
    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }
    
    public Integer getRestingHeartRate() {
        return restingHeartRate;
    }
    
    public void setRestingHeartRate(Integer restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
    }
    
    public Integer getBaselineSystolic() {
        return baselineSystolic;
    }
    
    public void setBaselineSystolic(Integer baselineSystolic) {
        this.baselineSystolic = baselineSystolic;
    }
    
    public Integer getBaselineDiastolic() {
        return baselineDiastolic;
    }
    
    public void setBaselineDiastolic(Integer baselineDiastolic) {
        this.baselineDiastolic = baselineDiastolic;
    }
    
    public Double getNormalBodyTemp() {
        return normalBodyTemp;
    }
    
    public void setNormalBodyTemp(Double normalBodyTemp) {
        this.normalBodyTemp = normalBodyTemp;
    }
    
    public String getSmokingStatus() {
        return smokingStatus;
    }
    
    public void setSmokingStatus(String smokingStatus) {
        this.smokingStatus = smokingStatus;
    }
    
    public String getAlcoholConsumption() {
        return alcoholConsumption;
    }
    
    public void setAlcoholConsumption(String alcoholConsumption) {
        this.alcoholConsumption = alcoholConsumption;
    }
    
    public List<String> getChronicConditions() {
        return chronicConditions;
    }
    
    public void setChronicConditions(List<String> chronicConditions) {
        this.chronicConditions = chronicConditions;
    }
    
    public String getOtherConditionsDetails() {
        return otherConditionsDetails;
    }
    
    public void setOtherConditionsDetails(String otherConditionsDetails) {
        this.otherConditionsDetails = otherConditionsDetails;
    }
    
    public String getCurrentMedications() {
        return currentMedications;
    }
    
    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications;
    }
    
    public String getAllergiesText() {
        return allergiesText;
    }
    
    public void setAllergiesText(String allergiesText) {
        this.allergiesText = allergiesText;
    }
    
    public List<String> getFamilyHistory() {
        return familyHistory;
    }
    
    public void setFamilyHistory(List<String> familyHistory) {
        this.familyHistory = familyHistory;
    }
    
    public String getEmergencyContactName() {
        return emergencyContactName;
    }
    
    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }
    
    public String getEmergencyContactRelation() {
        return emergencyContactRelation;
    }
    
    public void setEmergencyContactRelation(String emergencyContactRelation) {
        this.emergencyContactRelation = emergencyContactRelation;
    }
    
    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }
    
    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
    
    public String getEmergencyContactEmail() {
        return emergencyContactEmail;
    }
    
    public void setEmergencyContactEmail(String emergencyContactEmail) {
        this.emergencyContactEmail = emergencyContactEmail;
    }
    
    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    
    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }
    
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    
    public String getGroupNumber() {
        return groupNumber;
    }
    
    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }
    
    public String getInsurancePhone() {
        return insurancePhone;
    }
    
    public void setInsurancePhone(String insurancePhone) {
        this.insurancePhone = insurancePhone;
    }
    
    public List<String> getAlertPreferences() {
        return alertPreferences;
    }
    
    public void setAlertPreferences(List<String> alertPreferences) {
        this.alertPreferences = alertPreferences;
    }
    
    public Integer getSystolicGoal() {
        return systolicGoal;
    }
    
    public void setSystolicGoal(Integer systolicGoal) {
        this.systolicGoal = systolicGoal;
    }
    
    public Integer getDiastolicGoal() {
        return diastolicGoal;
    }
    
    public void setDiastolicGoal(Integer diastolicGoal) {
        this.diastolicGoal = diastolicGoal;
    }
    
    public Integer getMinTargetHR() {
        return minTargetHR;
    }
    
    public void setMinTargetHR(Integer minTargetHR) {
        this.minTargetHR = minTargetHR;
    }
    
    public Integer getMaxTargetHR() {
        return maxTargetHR;
    }
    
    public void setMaxTargetHR(Integer maxTargetHR) {
        this.maxTargetHR = maxTargetHR;
    }
    
    public Integer getFastingGlucoseTarget() {
        return fastingGlucoseTarget;
    }
    
    public void setFastingGlucoseTarget(Integer fastingGlucoseTarget) {
        this.fastingGlucoseTarget = fastingGlucoseTarget;
    }
    
    public Integer getTotalCholesterolTarget() {
        return totalCholesterolTarget;
    }
    
    public void setTotalCholesterolTarget(Integer totalCholesterolTarget) {
        this.totalCholesterolTarget = totalCholesterolTarget;
    }
    
    public String getWeightMonitoringFreq() {
        return weightMonitoringFreq;
    }
    
    public void setWeightMonitoringFreq(String weightMonitoringFreq) {
        this.weightMonitoringFreq = weightMonitoringFreq;
    }
    
    public String getBpMonitoringFreq() {
        return bpMonitoringFreq;
    }
    
    public void setBpMonitoringFreq(String bpMonitoringFreq) {
        this.bpMonitoringFreq = bpMonitoringFreq;
    }
    
    public String getGlucoseMonitoringFreq() {
        return glucoseMonitoringFreq;
    }
    
    public void setGlucoseMonitoringFreq(String glucoseMonitoringFreq) {
        this.glucoseMonitoringFreq = glucoseMonitoringFreq;
    }
    
    public String getReminderPreference() {
        return reminderPreference;
    }
    
    public void setReminderPreference(String reminderPreference) {
        this.reminderPreference = reminderPreference;
    }
    
    public String getHealthGoalsNotes() {
        return healthGoalsNotes;
    }
    
    public void setHealthGoalsNotes(String healthGoalsNotes) {
        this.healthGoalsNotes = healthGoalsNotes;
    }
}