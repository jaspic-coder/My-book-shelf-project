package org.example.mybooklibrary.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.example.mybooklibrary.config.EmailService;
import org.example.mybooklibrary.exception.ResourceNotFoundException;
import org.example.mybooklibrary.passwordresettoken.PasswordResetToken;
import org.example.mybooklibrary.passwordresettoken.PasswordResetTokenRepository;
import org.example.mybooklibrary.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Map<String, String> otpStore = new HashMap<>();
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

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

    public User registerUser(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
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


    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email is not registered"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        if (!user.isVerified()) {
            throw new IllegalArgumentException("User is not verified. Please verify your account.");
        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());

    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setVerified(true);
            userRepository.save(user);
            otpStore.remove(email);
            return true;
        }
        return false;
    }

    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = passwordResetTokenRepository.findByEmail(email)
                .orElse(new PasswordResetToken());

        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        passwordResetTokenRepository.save(resetToken);
        return token;
    }

    public boolean isResetTokenValid(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid password reset token"));

        return resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public void resetPassword(String token, String newPassword, String confirmPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid password reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    public void sendPasswordResetEmail(String email) {
        String token = createPasswordResetToken(email);
        emailService.sendPasswordResetEmail(email, token);
    }
}
