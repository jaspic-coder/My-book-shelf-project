package org.example.mybooklibrary.user;

import lombok.Data;
@Data
public class RegisterRequest {
        private String UserName;
        private String Email;
        private String Password;
}
