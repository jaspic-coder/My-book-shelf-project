package org.example.mybooklibrary.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService; // ✅ final and injected

    // ✅ Constructor injection
    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.registerUser(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }


    @PostMapping("/otp/send")
    public ResponseEntity<String> sendOTP(@RequestBody EmailRequest request) {
        String otp = otpService.sendOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent: " + otp); // In production, don't return OTP to client
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.loginUser(
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerication request) {
        try {
            boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp().trim());

            if (isValid) {
                return ResponseEntity.ok("✅ OTP verified successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OTP verification failed.");
        }
    }
}

