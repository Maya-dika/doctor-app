package com.example.doctorapp.repository;

import com.example.doctorapp.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    
    // Find specialty by name (case insensitive)
    Optional<Specialty> findByNameIgnoreCase(String name);
    
    // Check if specialty exists by name
    boolean existsByNameIgnoreCase(String name);
    
    // Find all specialties ordered by name
    @Query("SELECT s FROM Specialty s ORDER BY s.name ASC")
    List<Specialty> findAllOrderByName();
    
    // Find specialties containing a keyword
    List<Specialty> findByNameContainingIgnoreCase(String keyword);
    
    // Get distinct specialty names
    @Query("SELECT DISTINCT s.name FROM Specialty s ORDER BY s.name")
    List<String> findDistinctSpecialtyNames();
}