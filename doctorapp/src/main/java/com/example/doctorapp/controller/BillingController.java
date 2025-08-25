package com.example.doctorapp.controller;

import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.Billing;
import com.example.doctorapp.model.Appointment;
import com.example.doctorapp.service.BillingService;
import com.example.doctorapp.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BillingController {
    
    @Autowired
    private BillingService billingService;
    
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/billing")
    public String viewBilling(Model model, HttpSession session) {
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        if (patient == null) {
            return "redirect:/login";
        }
        
        // Get existing bills (if any)
        List<Billing> bills = billingService.getBillsByPatient(patient.getId());
        
        // Get checked-in appointments that need payment
        List<Appointment> checkedInAppointments = appointmentService.getCheckedInAppointments(patient.getId());
        
        model.addAttribute("billing", bills);
        model.addAttribute("checkedInAppointments", checkedInAppointments);
        model.addAttribute("patientName", patient.getFirstName() + " " + patient.getLastName());
        model.addAttribute("totalBalance", appointmentService.getTotalBalance(patient));
        
        System.out.println("Patient ID: " + patient.getId());
        System.out.println("Number of bills: " + (bills != null ? bills.size() : 0));
        System.out.println("Number of checked-in appointments: " + (checkedInAppointments != null ? checkedInAppointments.size() : 0));
        System.out.println("Total balance: " + appointmentService.getTotalBalance(patient));
        
        return "billing";
    }

    // MODIFIED METHOD: Direct payment for checked-in appointments
    @PostMapping("/proceed-payment")
    public String proceedPayment(@RequestParam("appointmentId") Long appointmentId,
                                @RequestParam("paymentMethod") String paymentMethod,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        
        System.out.println("=== PROCEED PAYMENT DEBUG ===");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Payment Method: " + paymentMethod);
        
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        if (loggedInPatient == null) {
            System.out.println("No logged in patient found!");
            return "redirect:/login";
        }
        
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            System.out.println("Retrieved appointment: " + (appointment != null ? appointment.getId() : "null"));
            
            if (appointment != null) {
                System.out.println("Appointment status: " + appointment.getStatus());
                System.out.println("Patient ID from appointment: " + appointment.getPatient().getId());
                System.out.println("Logged in patient ID: " + loggedInPatient.getId());
                
                // Check if appointment belongs to the logged-in patient
                if (!appointment.getPatient().getId().equals(loggedInPatient.getId())) {
                    System.out.println("Appointment does not belong to logged-in patient!");
                    redirectAttributes.addFlashAttribute("error", 
                        "Appointment not found or not eligible for payment!");
                    return "redirect:/billing";
                }
                
                // *** THE FIX IS HERE: Changed from "CHECKEDIN" to "CHECKED_IN" ***
                if ("CHECKED_IN".equals(appointment.getStatus())) {
                    System.out.println("Processing payment for appointment: " + appointment.getId());
                    
                    // Update appointment with payment information
                    appointment.setPaymentAmount(appointment.getDoctor().getConsultationFee());
                    appointment.setPaymentDate(LocalDate.now());
                    appointment.setPaymentMethod(paymentMethod);
                    appointment.setPaymentStatus("PAID");
                    
                    // Update appointment status to COMPLETED
                    appointment.setStatus("COMPLETED");
                    
                    appointmentService.save(appointment);
                                      
                    redirectAttributes.addFlashAttribute("success", 
                        "Payment of $" + appointment.getDoctor().getConsultationFee() + 
                        " processed successfully using " + paymentMethod + "!");
                        
                    System.out.println("Payment processed successfully for appointment: " + appointmentId);
                    
                } else {
                    System.out.println("Appointment status is not CHECKED_IN. Current status: " + appointment.getStatus());
                    redirectAttributes.addFlashAttribute("error", 
                        "Appointment is not eligible for payment. Current status: " + appointment.getStatus());
                }
                
            } else {
                System.out.println("Appointment not found with ID: " + appointmentId);
                redirectAttributes.addFlashAttribute("error", 
                    "Appointment not found or not eligible for payment!");
            }
            
        } catch (Exception e) {
            System.out.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", 
                "Payment processing failed: " + e.getMessage());
        }
        
        System.out.println("=== END PROCEED PAYMENT DEBUG ===");
        return "redirect:/billing";
    }

//    // EXISTING METHODS (keep these for backward compatibility)
//    @PostMapping("/pay-bill-with-method")
//    public String payBillWithMethod(@RequestParam("billId") Long billId,
//                                    @RequestParam("paymentMethod") String paymentMethod,
//                                    RedirectAttributes redirectAttributes,
//                                    HttpSession session) {
//        
//        System.out.println("=== PAY BILL DEBUG ===");
//        System.out.println("Bill ID: " + billId);
//        System.out.println("Payment Method: " + paymentMethod);
//        
//        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
//        if (loggedInPatient == null) {
//            System.out.println("No logged in patient found!");
//            return "redirect:/login";
//        }
//        
//        System.out.println("Patient ID: " + loggedInPatient.getId());
//
//        try {
//            Billing bill = billingService.getBillsByID(billId);
//            System.out.println("Retrieved bill: " + (bill != null ? bill.getId() : "null"));
//            
//            if (bill != null) {
//                System.out.println("Before update - Status: " + bill.getStatus());
//                System.out.println("Before update - Paid Date: " + bill.getPaidDate());
//                
//                bill.setStatus("PAID");
//                bill.setPaidDate(LocalDate.now());
//                
//                Billing savedBill = billingService.save(bill);
//                System.out.println("After save - Status: " + savedBill.getStatus());
//                System.out.println("After save - Paid Date: " + savedBill.getPaidDate());
//                
//                redirectAttributes.addFlashAttribute("success", 
//                    "Bill #" + billId + " paid successfully using " + paymentMethod + "!");
//            } else {
//                System.out.println("Bill not found with ID: " + billId);
//                redirectAttributes.addFlashAttribute("error", "Bill not found!");
//            }
//        } catch (Exception e) {
//            System.out.println("Error processing payment: " + e.getMessage());
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("error", "Payment processing failed: " + e.getMessage());
//        }
//        
//        System.out.println("=== END PAY BILL DEBUG ===");
//        return "redirect:/billing";
//    }
//
//    @PostMapping("/pay-all-with-method")
//    public String payAllWithMethod(@RequestParam("paymentMethod") String paymentMethod,
//                                   RedirectAttributes redirectAttributes,
//                                   HttpSession session) {
//        
//        System.out.println("=== PAY ALL BILLS DEBUG ===");
//        System.out.println("Payment Method: " + paymentMethod);
//        
//        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
//        if (loggedInPatient == null) {
//            System.out.println("No logged in patient found!");
//            return "redirect:/login";
//        }
//        
//        System.out.println("Patient ID: " + loggedInPatient.getId());
//
//        try {
//            List<Billing> unpaidBills = billingService.getUnpaidBillsByPatient(loggedInPatient.getId());
//            System.out.println("Found " + unpaidBills.size() + " unpaid bills");
//            
//            int paidCount = 0;
//            for (Billing bill : unpaidBills) {
//                System.out.println("Paying bill ID: " + bill.getId());
//                bill.setStatus("PAID");
//                bill.setPaidDate(LocalDate.now());
//                billingService.save(bill);
//                paidCount++;
//            }
//            
//            System.out.println("Successfully paid " + paidCount + " bills");
//            redirectAttributes.addFlashAttribute("success", 
//                "All " + paidCount + " bills paid successfully using " + paymentMethod + "!");
//                
//        } catch (Exception e) {
//            System.out.println("Error processing payment: " + e.getMessage());
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("error", "Payment processing failed: " + e.getMessage());
//        }
//        
//        System.out.println("=== END PAY ALL BILLS DEBUG ===");
//        return "redirect:/billing";
//    }
//
}
