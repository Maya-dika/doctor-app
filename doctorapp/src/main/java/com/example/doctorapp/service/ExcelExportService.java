package com.example.doctorapp.service;

import com.example.doctorapp.dto.AppointmentDto;
import com.example.doctorapp.dto.PatientDto;
import com.example.doctorapp.dto.ReportDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public byte[] exportPatientsToExcel(List<PatientDto> patients) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Patients");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "First Name", "Last Name", "Email", "Phone Number", "Address", "Age", "Gender", "Medical History"};
            
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            CellStyle dataStyle = createDataStyle(workbook);
            int rowNum = 1;
            
            for (PatientDto patient : patients) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(patient.getId() != null ? patient.getId() : 0);
                row.createCell(1).setCellValue(patient.getFirstName() != null ? patient.getFirstName() : "");
                row.createCell(2).setCellValue(patient.getLastName() != null ? patient.getLastName() : "");
                row.createCell(3).setCellValue(patient.getEmail() != null ? patient.getEmail() : "");
                row.createCell(4).setCellValue(patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "");
                row.createCell(5).setCellValue(patient.getAddress() != null ? patient.getAddress() : "");
                row.createCell(6).setCellValue(patient.getAge() != null ? patient.getAge() : 0);
                row.createCell(7).setCellValue(patient.getGender() != null ? patient.getGender() : "");
                row.createCell(8).setCellValue(patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "");
                
                // Apply data style to all cells
                for (int i = 0; i < 9; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            return writeWorkbookToBytes(workbook);
        }
    }
    
    public byte[] exportAppointmentsToExcel(List<AppointmentDto> appointments) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Appointments");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Patient Name", "Doctor Name", "Date", "Time", "Status", "Payment Amount", "Payment Status", "Payment Method"};
            
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            CellStyle dataStyle = createDataStyle(workbook);
            int rowNum = 1;
            
            for (AppointmentDto appointment : appointments) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(appointment.getId() != null ? appointment.getId() : 0);
                row.createCell(1).setCellValue(appointment.getpatientFirstName() + " " + appointment.getpatientLastName());
                row.createCell(2).setCellValue(appointment.getdoctorFirstName() + " " + appointment.getdoctorLastName());
                row.createCell(3).setCellValue(appointment.getDate() != null ? appointment.getDate().format(DATE_FORMATTER) : "");
                row.createCell(4).setCellValue(appointment.getTime() != null ? appointment.getTime().toString() : "");
                row.createCell(5).setCellValue(appointment.getStatus() != null ? appointment.getStatus() : "");
                row.createCell(6).setCellValue(appointment.getPaymentAmount() != null ? appointment.getPaymentAmount().doubleValue() : 0.0);
                row.createCell(7).setCellValue(appointment.getPaymentStatus() != null ? appointment.getPaymentStatus() : "");
                row.createCell(8).setCellValue(appointment.getPaymentMethod() != null ? appointment.getPaymentMethod() : "");
                
                // Apply data style to all cells
                for (int i = 0; i < 9; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            return writeWorkbookToBytes(workbook);
        }
    }
    
    public byte[] exportReportToExcel(ReportDto report) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            
            // Summary Sheet
            createSummarySheet(workbook, report);
            
            // Patient Demographics Sheet
            if (report.getPatientsByGender() != null && !report.getPatientsByGender().isEmpty()) {
                createPatientDemographicsSheet(workbook, report);
            }
            
            // Appointment Statistics Sheet
            if (report.getAppointmentsByStatus() != null && !report.getAppointmentsByStatus().isEmpty()) {
                createAppointmentStatisticsSheet(workbook, report);
            }
            
            // Revenue Analysis Sheet
            if (report.getRevenueByPaymentMethod() != null && !report.getRevenueByPaymentMethod().isEmpty()) {
                createRevenueAnalysisSheet(workbook, report);
            }
            
            return writeWorkbookToBytes(workbook);
        }
    }
    
    private void createSummarySheet(Workbook workbook, ReportDto report) {
        Sheet sheet = workbook.createSheet("Summary");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        // Title
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Analytics & Reports Dashboard");
        titleCell.setCellStyle(headerStyle);
        
        // Date Range
        Row dateRow = sheet.createRow(1);
        dateRow.createCell(0).setCellValue("Report Period:");
        dateRow.createCell(1).setCellValue(report.getStartDate() + " to " + report.getEndDate());
        
        // Key Metrics
        int rowNum = 3;
        createMetricRow(sheet, rowNum++, "Total Patients", report.getTotalPatients().toString(), dataStyle);
        createMetricRow(sheet, rowNum++, "Total Appointments", report.getTotalAppointments().toString(), dataStyle);
        createMetricRow(sheet, rowNum++, "Total Revenue", "$" + report.getTotalRevenue().toString(), dataStyle);
        createMetricRow(sheet, rowNum++, "Average Appointment Cost", "$" + report.getAverageAppointmentCost().toString(), dataStyle);
        createMetricRow(sheet, rowNum++, "Completed Appointments", report.getCompletedAppointments().toString(), dataStyle);
        createMetricRow(sheet, rowNum++, "Pending Appointments", report.getPendingAppointments().toString(), dataStyle);
        createMetricRow(sheet, rowNum++, "Cancelled Appointments", report.getCancelledAppointments().toString(), dataStyle);
        createMetricRow(sheet, rowNum++, "Total Doctors", report.getTotalDoctors().toString(), dataStyle);
        createMetricRow(sheet, rowNum++, "Total Specialties", report.getTotalSpecialties().toString(), dataStyle);
        
        // Auto-size columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }
    
    private void createPatientDemographicsSheet(Workbook workbook, ReportDto report) {
        Sheet sheet = workbook.createSheet("Patient Demographics");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Category");
        headerRow.createCell(1).setCellValue("Count");
        headerRow.getCell(0).setCellStyle(headerStyle);
        headerRow.getCell(1).setCellStyle(headerStyle);
        
        // Gender distribution
        int rowNum = 1;
        sheet.createRow(rowNum++).createCell(0).setCellValue("Gender Distribution");
        
        for (Map.Entry<String, Long> entry : report.getPatientsByGender().entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        // Age distribution
        rowNum++;
        sheet.createRow(rowNum++).createCell(0).setCellValue("Age Distribution");
        
        for (Map.Entry<String, Long> entry : report.getPatientsByAgeGroup().entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }
    
    private void createAppointmentStatisticsSheet(Workbook workbook, ReportDto report) {
        Sheet sheet = workbook.createSheet("Appointment Statistics");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Status");
        headerRow.createCell(1).setCellValue("Count");
        headerRow.getCell(0).setCellStyle(headerStyle);
        headerRow.getCell(1).setCellStyle(headerStyle);
        
        // Appointment status distribution
        int rowNum = 1;
        for (Map.Entry<String, Long> entry : report.getAppointmentsByStatus().entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }
    
    private void createRevenueAnalysisSheet(Workbook workbook, ReportDto report) {
        Sheet sheet = workbook.createSheet("Revenue Analysis");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Payment Method");
        headerRow.createCell(1).setCellValue("Revenue");
        headerRow.getCell(0).setCellStyle(headerStyle);
        headerRow.getCell(1).setCellStyle(headerStyle);
        
        // Revenue by payment method
        int rowNum = 1;
        for (Map.Entry<String, BigDecimal> entry : report.getRevenueByPaymentMethod().entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue().doubleValue());
            row.getCell(0).setCellStyle(dataStyle);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        // Auto-size columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }
    
    private void createMetricRow(Sheet sheet, int rowNum, String label, String value, CellStyle style) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
        row.getCell(0).setCellStyle(style);
        row.getCell(1).setCellStyle(style);
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private byte[] writeWorkbookToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
