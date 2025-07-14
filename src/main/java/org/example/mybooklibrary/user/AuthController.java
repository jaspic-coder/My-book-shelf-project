package org.example.mybooklibrary.user;

import lombok.RequiredArgsConstructor;
import org.example.mybooklibrary.passwordresettoken.ResetPasswordRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

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
        return ResponseEntity.ok("OTP sent: " + otp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.loginUser(request.getEmail(), request.getPassword()).toString();
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
                return ResponseEntity.ok("OTP verified successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OTP verification failed.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailRequest request) {
        try {
            authService.sendPasswordResetEmail(request.getEmail());
            return ResponseEntity.ok("Password reset token created and sent.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create reset token: " + e.getMessage());
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        try {
            boolean valid = authService.isResetTokenValid(token);
            if (valid) {
                return ResponseEntity.ok("Token is valid. You can reset your password.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid or expired.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error validating token.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword(), request.getConfirmPassword());
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password reset failed: " + e.getMessage());
        }
    }
}
