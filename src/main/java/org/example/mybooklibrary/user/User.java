package org.example.mybooklibrary.user;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

