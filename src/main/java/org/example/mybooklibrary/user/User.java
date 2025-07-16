package org.example.mybooklibrary.user;

import jakarta.persistence.*;
import lombok.Data;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    // If it's a single value field (not a collection)
    @Column(name = "password_reset_token")
    private String passwordResetToken;

    // OR if it should be a collection (multiple tokens)
    @ElementCollection
    @CollectionTable(name = "user_password_reset_tokens",
            joinColumns = @JoinColumn(name = "user_id"))
    private List<String> passwordResetTokens;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    @Column(name = "profile_image_path")
    private String profileImagePath;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Books > books ;
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setRole(String role) {

    }

    public void setRole(Role role) {
    }
}

