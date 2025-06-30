package org.example.mybooklibrary.passwordresettoken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
        private String email;
        private String token;
        private String newPassword;
        private String confirmPassword;
}
