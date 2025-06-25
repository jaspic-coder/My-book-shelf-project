package org.example.mybooklibrary.user;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        private final AuthService authService;
        private final OtpService otpService;

        public AuthController(AuthService authService, OtpService otpService) {
            this.authService = authService;
            this.otpService = otpService;
        }

        @PostMapping("/register")
        public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
            authService.registerUser(request.getEmail(), request.getPassword(), request.getUserName());
            String otp = otpService.sendOtp(request.getEmail());
            return ResponseEntity.ok("User registered. OTP sent to email.");
        }

        @PostMapping("/login")
        public ResponseEntity<String> login(@RequestBody LoginRequest request) {
            String token = authService.loginUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(token);
        }

        @PostMapping("/otp/verify")
        public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
            boolean verified = otpService.verifyOtp(request.getEmail(), request.getOtp());
            if (verified) {
                return ResponseEntity.ok("User verified successfully.");
            } else {
                return ResponseEntity.badRequest().body("Invalid OTP.");
            }
        }

        @PostMapping("/otp/send")
        public ResponseEntity<String> sendOtp(@RequestBody SendOtpRequest request) {
            String otp = otpService.sendOtp(request.getEmail());
            return ResponseEntity.ok("OTP sent to email. Your OTP is: " + otp);
        }
    }

