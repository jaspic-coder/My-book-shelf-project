package org.example.mybooklibrary.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.registerUser(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.loginUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        }
    }

    @PostMapping("/otp/send")
    public ResponseEntity<String> sendOTP(@RequestBody SendOtpRequest request) {
        String otp = authService.generateOTP(request.getEmail());
        return ResponseEntity.ok("OTP sent: " + otp);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<Boolean> verifyOTP(@RequestBody OtpRequest request) {
        boolean verified = authService.verifyOTP(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(verified);
    }
}
