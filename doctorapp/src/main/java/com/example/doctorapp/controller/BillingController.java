package com.example.doctorapp.controller;

import com.example.doctorapp.model.Patient;
import com.example.doctorapp.service.BillingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/billing")
    public String viewBilling(Model model, HttpSession session) {
        Patient loggedInPatient = (Patient) session.getAttribute("loggedInPatient");
        if (loggedInPatient == null) {
            return "redirect:/login";
        }
        model.addAttribute("billing", billingService.getBillsByPatient(loggedInPatient.getId()));
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


        return "redirect:/billing";
    }

    // Helper method
    private String getPaymentMethodName(String method) {
        switch (method) {
            case "credit-card": return "Credit Card";
            case "bank-transfer": return "Bank Transfer";
            case "paypal": return "PayPal";
            case "digital-wallet": return "Digital Wallet";
            case "insurance": return "Insurance";
            case "cash-check": return "Cash/Check";
            default: return "Selected Payment Method";
        }
    }
}
