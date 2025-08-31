
package com.example.doctorapp.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendAppointmentConfirmationToPatient(String patientEmail, String patientName, 
                                                   String doctorName, String appointmentDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(patientEmail);
        message.setSubject("Appointment Confirmation");
        message.setText("Dear " + patientName + ",\n\n"
                + "Your appointment has been successfully booked with Dr. " + doctorName + ".\n"
                + "Date: " + appointmentDate + "\n\n"
                + "Please arrive 15 minutes early.\n\n"
                + "Thank you,\nHealra Team");
        mailSender.send(message);
    }
}

