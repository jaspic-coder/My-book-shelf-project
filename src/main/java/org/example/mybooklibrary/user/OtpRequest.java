package org.example.mybooklibrary.user;

import lombok.Data;

@Data
public class OtpRequest {
    private String email;
    private String otp;
}
