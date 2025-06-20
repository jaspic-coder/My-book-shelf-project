package org.example.mybooklibrary.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterRequest {
        @Schema(description = "User's name")
        private String userName;

        @Schema(description = "User's email")
        private String email;

        @Schema(description = "User's password")
        private String password;
}
