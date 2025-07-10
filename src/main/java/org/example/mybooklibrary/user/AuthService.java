package org.example.mybooklibrary.user;

import org.example.mybooklibrary.config.EmailService;
import org.example.mybooklibrary.exception.InvalidPasswordException;
import org.example.mybooklibrary.exception.ResourceNotFoundException;
import org.example.mybooklibrary.exception.UserNotFoundException;
import org.example.mybooklibrary.passwordresettoken.PasswordResetToken;
import org.example.mybooklibrary.passwordresettoken.PasswordResetTokenRepository;
import org.example.mybooklibrary.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final Map<String, String> otpStore = new HashMap<>();

    @Value("${upload.directory}")
    private String uploadPath;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    //  Register new user
    public User registerUser(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceNotFoundException("Email already exists");
        }

        User user = new User();
        user.setRegNo(request.getRegNo());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getUserName());
        user.setRole(request.getRole());
        user.setVerified(false);
        return userRepository.save(user);
    }

    //  Login user
    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email is not registered"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Incorrect password");
        }

        if (!user.isVerified()) {
            throw new InvalidPasswordException("User is not verified. Please verify your account.");
        }

        return jwtUtil.generateToken(email);
    }

    //  Generate OTP for email
    public String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        return otp;
    }

    //  Verify OTP and activate user
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            user.setVerified(true);
            userRepository.save(user);
            otpStore.remove(email);
            return true;
        }
        return false;
    }

    //  Create token for password reset
    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = passwordResetTokenRepository.findByEmail(email)
                .orElse(new PasswordResetToken());

        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        passwordResetTokenRepository.save(resetToken);
        return token;
    }

    //  Validate reset token
    public boolean isResetTokenValid(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid password reset token"));

        return resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    //  Reset password
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid password reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidPasswordException("Token expired");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    //  Send password reset email
    public void sendPasswordResetEmail(String email) {
        String token = createPasswordResetToken(email);
        emailService.sendPasswordResetEmail(email, token);
    }

    //  Upload or replace profile picture
    public String updateProfilePicture(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            // Delete old picture if it exists
            String existingFilename = user.getProfileImagePath();
            if (existingFilename != null && !existingFilename.isEmpty()) {
                Path existingFilePath = Paths.get(uploadPath).resolve(existingFilename);
                Files.deleteIfExists(existingFilePath);
            }

            // Save new picture
            String newFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath);
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path newFilePath = uploadDir.resolve(newFilename);
            Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

            // Update user record
            user.setProfileImagePath(newFilename);
            userRepository.save(user);

            // Return access URL path
            return "/api/auth/" + userId + "/profile-picture";

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture: " + e.getMessage());
        }
    }
}
