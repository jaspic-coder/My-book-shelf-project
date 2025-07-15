package org.example.mybooklibrary.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
    private String email; // ← Add this line
}
