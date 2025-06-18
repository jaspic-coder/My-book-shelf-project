package org.example.mybooklibrary.user;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody Map<String, String> request) {
        User user = authService.registerUser(
                request.get("email"),
                request.get("password"),
                request.get("name")
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String token = authService.loginUser(
                    request.get("email"),
                    request.get("password")
            );
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/otp/send")
    public ResponseEntity<String> sendOTP(@RequestBody Map<String, String> request) {
        String otp = authService.generateOTP(request.get("email"));
        return ResponseEntity.ok("OTP sent: " + otp);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<Boolean> verifyOTP(@RequestBody Map<String, String> request) {
        boolean verified = authService.verifyOTP(
                request.get("email"),
                request.get("otp")
        );
        return ResponseEntity.ok(verified);
    }
}