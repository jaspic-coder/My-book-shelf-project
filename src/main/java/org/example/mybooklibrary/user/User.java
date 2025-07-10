package org.example.mybooklibrary.user;

import jakarta.persistence.*;
import lombok.Data;
import org.example.mybooklibrary.payment.Payment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reg_no", unique = true, nullable = false)
    private String regNo;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "is_verified")
    private boolean verified;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @Column(name = "password_reset_token", unique = true)
    private String passwordResetToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    // Profile image path (saved file name)
    @Column(name = "profile_image_path")
    private String profileImagePath;

    // Returns the URL path to access profile picture via controller endpoint
    public String getProfilePictureUrl() {
        if (profileImagePath == null || profileImagePath.isBlank()) {
            return null;
        }
        return "/api/auth/" + this.id + "/profile-picture";
    }
}

