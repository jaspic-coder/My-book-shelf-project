package org.example.mybooklibrary.user;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final UserRepository userRepository;

    public OtpService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String sendOtp(String email) {
        String otp = generateOtp();
        otpStorage.put(email, otp);
        System.out.println("OTP sent to " + email + ": " + otp);
        return otp;
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
            otpStorage.remove(email); // Remove OTP after success
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
