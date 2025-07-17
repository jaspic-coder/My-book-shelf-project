package org.example.mybooklibrary.user;

import lombok.RequiredArgsConstructor;
import org.example.mybooklibrary.passwordresettoken.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;
    private final UserRepository userRepository;

    @Value("${upload.directory}")
    private String uploadDir;

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
            String token = authService.loginUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerication request) {
        try {
            boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp().trim());
            return isValid
                    ? ResponseEntity.ok("OTP verified successfully.")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OTP verification failed.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody EmailRequest request) {
        try {
            authService.sendPasswordResetEmail(request.getEmail());
            return ResponseEntity.ok(Map.of("message", "Password reset token created and sent."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to create reset token: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,  // <-- token from URL
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        try {
            authService.resetPassword(token, request.getNewPassword(), request.getConfirmPassword());
            return ResponseEntity.ok(Map.of("message", "Password has been reset successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Password reset failed: " + e.getMessage()));
        }
    }


    @Operation(summary = "Upload Profile Picture",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary", description = "Profile picture file"))
            )
    )
    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile file, Principal principal) {
        try {

            String email = principal.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

            if (user.getProfileImagePath() != null) {
                Path oldPath = Paths.get(uploadDir, user.getProfileImagePath());
                Files.deleteIfExists(oldPath);
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfileImagePath(filename);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "Profile picture uploaded successfully."));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "File upload failed."));
        }
    }

    // --- New admin endpoint to create users ---

    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUserByAdmin(@RequestBody CreateUserRequest request) {
        try {
            User user = authService.createUserByAdmin(request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("User creation failed: " + e.getMessage());
        }
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> editProfile(@RequestBody UserProfileRequest request,
                                         @AuthenticationPrincipal UserDetails principal) {
        String currentUserEmail = principal.getUsername();
        AuthService userService = null;
        User updatedUser = userService.updateProfile(currentUserEmail, request);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful");
    }


    public static class CreateUserRequest {
        private String email;
        private String password;
        private String confirmPassword;
        private String userName;
        private Role role;


        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
    }
}