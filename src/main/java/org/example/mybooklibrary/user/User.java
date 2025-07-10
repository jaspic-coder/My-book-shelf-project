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
//
//    @Column(name = "college_reg_no", unique = true, nullable = false)
//    private String collegeRegNo;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER ;

    @Column(name = "is_verified")
    private boolean verified;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    // New fields for password reset
    @Column(name = "password_reset_token", unique = true)
    private String passwordResetToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;
}
