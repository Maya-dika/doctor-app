package com.example.doctorapp.controller;

import com.example.doctorapp.model.Patient;
import com.example.doctorapp.model.Billing;
import com.example.doctorapp.service.BillingService;
import com.example.doctorapp.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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

        model.addAttribute("billing", billingService.getBillsByPatient(patient.getId()));
        model.addAttribute("patientName", patient.getFirstName() + " " + patient.getLastName());
        model.addAttribute("totalBalance", appointmentService.getTotalBalance(patient));
        System.out.println(appointmentService.getTotalBalance(patient));


        return "billing";
    }

    @PostMapping("/pay-bill-with-method")
    public String payBillWithMethod(@RequestParam("billId") Long billId,
                                    @RequestParam("paymentMethod") String paymentMethod,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        if (loggedInPatient == null) {
            return "redirect:/login";
        }

        Billing bill = billingService.getBillsByID(billId);
        if (bill != null) {
            bill.setStatus("PAID");
            bill.setPaidDate(LocalDate.now());
            bill.setPatient(loggedInPatient); // ensure bill belongs to patient
            billingService.save(bill);
        }

        redirectAttributes.addFlashAttribute("success", "Bill paid successfully!");
        return "redirect:/billing";
    }

    @PostMapping("/pay-all-with-method")
    public String payAllWithMethod(@RequestParam("paymentMethod") String paymentMethod,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        if (loggedInPatient == null) {
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("success", "All bills paid successfully!");
        return "redirect:/billing";
    }
}
