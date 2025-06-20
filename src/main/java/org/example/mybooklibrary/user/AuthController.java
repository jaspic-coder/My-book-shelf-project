package org.example.mybooklibrary.user;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Register new user")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = authService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getUserName()
        );
        return ResponseEntity.ok(user);
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

    @PostMapping("/otp/send")
    public ResponseEntity<String> sendOTP(@RequestBody SendOtpRequest request) {
        String otp = authService.generateOTP(request.getEmail());
        return ResponseEntity.ok("OTP sent: " + otp);
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<Boolean> verifyOTP(@RequestBody OtpRequest request) {
        boolean verified = authService.verifyOTP(
                request.getEmail(),
                request.getOtp()
        );
        return ResponseEntity.ok(verified);
    }
}
