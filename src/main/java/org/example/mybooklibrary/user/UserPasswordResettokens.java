package org.example.mybooklibrary.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

    @Entity
    @Table(name = "user_password_reset_tokens")
    @Getter
    @Setter
    @NoArgsConstructor
    public class  UserPasswordResettokens {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String email;

        @Column(nullable = false, unique = true)
        private String token;

        @Column(name = "expiry_date", nullable = false)
        private LocalDateTime expiryDate;
    }


