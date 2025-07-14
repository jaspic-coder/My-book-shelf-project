package org.example.mybooklibrary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender ;

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetUrl = "https://my-book-shelf-frontend.vercel.app/auth/forgotPassword?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetUrl +
                "\n\nIf you did not request a password reset, please ignore this email.");
        javaMailSender.send(message);
    }
    public void sendOtpEmail(String toEmail, String otp) {
        String subject = " Your OTP Code - My Book Shelf";
        String body = "Hello,\n\nYour OTP code is: " + otp +
                "\nIt is valid for 5 minutes." +
                "\n\nIf you didn’t request this, please ignore the email." +
                "\n\n– My Book Shelf Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

}
