package org.example.mybooklibrary.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String message;
    private User user;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
        this.message = "Login successful";
    }
}