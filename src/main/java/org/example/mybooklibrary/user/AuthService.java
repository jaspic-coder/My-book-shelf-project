package org.example.mybooklibrary.user;

import org.example.mybooklibrary.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Map<String, String> otpStore = new HashMap<>();

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setRegNo(request.getRegNo());
        user.setCollegeRegNo(request.getCollegeRegNo());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getUserName());
        user.setRole(request.getRole());
        user.setVerified(false);
        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isVerified()) {
            throw new RuntimeException("User is not verified");
        }


        return jwtUtil.generateToken(email);
    }

    public String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        return otp;
    }

    public boolean verifyOTP(String email, String otp) {
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setVerified(true);
            userRepository.save(user);
            otpStore.remove(email);
            return true;
        }
        return false;
    }
}
