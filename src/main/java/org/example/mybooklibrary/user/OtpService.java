package org.example.mybooklibrary.user;

import org.example.mybooklibrary.config.EmailService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final UserRepository userRepository;
    private final EmailService emailService;
    public OtpService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public String sendOtp(String email) {
        String otp = generateOtp();
        otpStorage.put(email, otp);

        emailService.sendOtpEmail(email, otp);

        return "OTP sent to your email.";
    }

    public boolean verifyOtp(String email, String otpInput) {
        String storedOtp = otpStorage.get(email);

        if (storedOtp == null) {
            System.out.println(" No OTP found for: " + email);
            return false;
        }

        String trimmedOtp = otpInput.trim();
        System.out.println("Stored OTP: " + storedOtp);
        System.out.println("Received OTP: " + trimmedOtp);

        boolean isValid = trimmedOtp.equals(storedOtp);

        if (isValid) {
            otpStorage.remove(email);
            userRepository.findByEmail(email).ifPresent(user -> {
                user.setVerified(true);
                userRepository.save(user);
                System.out.println("✅ User verified: " + email);
            });
        } else {
            System.out.println(" OTP does not match.");
        }

        return isValid;
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
