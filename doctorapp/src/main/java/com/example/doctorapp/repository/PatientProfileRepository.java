package com.example.doctorapp.repository;

import com.example.doctorapp.model.AdditionalPatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientProfileRepository extends JpaRepository<AdditionalPatientInfo, Long> {
    
    /**
     * Find patient profile by patient ID
     */
    Optional<AdditionalPatientInfo> findByPatientId(Long patientId);
    
    /**
     * Check if profile exists for patient ID
     */
    boolean existsByPatientId(Long patientId);
    
    /**
     * Delete profile by patient ID
     */
    void deleteByPatientId(Long patientId);
    
    /**
     * Find all patients with a specific chronic condition
     */
    @Query("SELECT p FROM AdditionalPatientInfo p WHERE :condition MEMBER OF p.chronicConditions")
    List<AdditionalPatientInfo> findByChronicCondition(@Param("condition") String condition);
    
    /**
     * Find all patients with a specific family history
     */
    @Query("SELECT p FROM AdditionalPatientInfo p WHERE :condition MEMBER OF p.familyHistory")
    List<AdditionalPatientInfo> findByFamilyHistory(@Param("condition") String condition);
    
    /**
     * Find patients by blood type
     */
    List<AdditionalPatientInfo> findByBloodType(String bloodType);
    
    /**
     * Find patients by smoking status
     */
    List<AdditionalPatientInfo> findBySmokingStatus(String smokingStatus);
    
    /**
     * Find patients by activity level
     */
    List<AdditionalPatientInfo> findByActivityLevel(String activityLevel);
    
    /**
     * Find patients with specific insurance provider
     */
    List<AdditionalPatientInfo> findByInsuranceProviderContainingIgnoreCase(String insuranceProvider);
    
    /**
     * Find patients within BMI range (custom query)
     */
    @Query("SELECT p FROM AdditionalPatientInfo p WHERE " +
           "((p.currentWeight * 703) / POWER((p.heightFeet * 12 + p.heightInches), 2)) " +
           "BETWEEN :minBmi AND :maxBmi")
    List<AdditionalPatientInfo> findByBMIRange(@Param("minBmi") double minBmi, @Param("maxBmi") double maxBmi);
    
    /**
     * Find patients with high blood pressure (based on baseline)
     */
    @Query("SELECT p FROM AdditionalPatientInfo p WHERE " +
           "p.baselineSystolic >= :systolicThreshold OR p.baselineDiastolic >= :diastolicThreshold")
    List<AdditionalPatientInfo> findWithHighBloodPressure(@Param("systolicThreshold") int systolicThreshold,
                                                              @Param("diastolicThreshold") int diastolicThreshold);
    
    /**
     * Find patients due for monitoring based on frequency
     */
    @Query("SELECT p FROM AdditionalPatientInfo p WHERE " +
           "p.weightMonitoringFreq = :frequency OR p.bpMonitoringFreq = :frequency OR p.glucoseMonitoringFreq = :frequency")
    List<AdditionalPatientInfo> findByMonitoringFrequency(@Param("frequency") String frequency);
    
    /**
     * Find patients with incomplete profiles (missing required fields)
     */
    @Query("SELECT p FROM AdditionalPatientInfo p WHERE " +
           "p.emergencyContactName IS NULL OR p.emergencyContactPhone IS NULL")
    List<AdditionalPatientInfo> findIncompleteProfiles();
    
    /**
     * Find patients with specific alert preferences
     */
    @Query("SELECT p FROM AdditionalPatientInfo p WHERE :alertType MEMBER OF p.alertPreferences")
    List<AdditionalPatientInfo> findByAlertPreference(@Param("alertType") String alertType);
    
    /**
     * Count patients by chronic condition
     */
    @Query("SELECT COUNT(p) FROM AdditionalPatientInfo p WHERE :condition MEMBER OF p.chronicConditions")
    long countByChronicCondition(@Param("condition") String condition);
    
    /**
     * Find patients with multiple chronic conditions (having more than specified count)
     */
    @Query("SELECT p FROM AdditionalPatientInfo p WHERE SIZE(p.chronicConditions) > :count")
    List<AdditionalPatientInfo> findWithMultipleChronicConditions(@Param("count") int count);
}