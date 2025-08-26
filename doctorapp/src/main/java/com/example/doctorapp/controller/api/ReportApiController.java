package com.example.doctorapp.controller.api;

import com.example.doctorapp.dto.ReportDto;
import com.example.doctorapp.service.ReportService;
import com.example.doctorapp.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportApiController {
    
    private final ReportService reportService;
    private final ExcelExportService excelExportService;
    
    public ReportApiController(ReportService reportService, ExcelExportService excelExportService) {
        this.reportService = reportService;
        this.excelExportService = excelExportService;
    }
    
    /**
     * Generate report for a specific date range
     */
    @GetMapping("/generate")
    public ResponseEntity<ReportDto> generateReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            ReportDto report = reportService.generateReport(startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Generate report for the current month
     */
    @GetMapping("/current-month")
    public ResponseEntity<ReportDto> generateCurrentMonthReport() {
        try {
            LocalDate now = LocalDate.now();
            LocalDate startDate = now.withDayOfMonth(1);
            LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
            
            ReportDto report = reportService.generateReport(startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Generate report for the last 30 days
     */
    @GetMapping("/last-30-days")
    public ResponseEntity<ReportDto> generateLast30DaysReport() {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(29);
            
            ReportDto report = reportService.generateReport(startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Generate report for the current year
     */
    @GetMapping("/current-year")
    public ResponseEntity<ReportDto> generateCurrentYearReport() {
        try {
            LocalDate now = LocalDate.now();
            LocalDate startDate = now.withDayOfYear(1);
            LocalDate endDate = now.withDayOfYear(now.lengthOfYear());
            
            ReportDto report = reportService.generateReport(startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Test endpoint to verify controller is working
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Report API Controller is working!");
    }
    
    /**
     * Export current month report to Excel
     */
    @GetMapping("/export/current-month/excel")
    public ResponseEntity<byte[]> exportCurrentMonthReportToExcel() {
        try {
            LocalDate now = LocalDate.now();
            LocalDate startDate = now.withDayOfMonth(1);
            LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
            
            ReportDto report = reportService.generateReport(startDate, endDate);
            byte[] excelData = excelExportService.exportReportToExcel(report);
            
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"current-month-report.xlsx\"")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Export last 30 days report to Excel
     */
    @GetMapping("/export/last-30-days/excel")
    public ResponseEntity<byte[]> exportLast30DaysReportToExcel() {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(29);
            
            ReportDto report = reportService.generateReport(startDate, endDate);
            byte[] excelData = excelExportService.exportReportToExcel(report);
            
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"last-30-days-report.xlsx\"")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
