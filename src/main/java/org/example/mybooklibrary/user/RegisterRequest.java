package org.example.mybooklibrary.user;

import lombok.Data;





@Data
public class RegisterRequest {
        private String email;
        private String password;
        private String confirmPassword;
        private String userName;
private String role;
}

