package org.example.mybooklibrary.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsResponseDto {
    private String status;
    private String paymentLink;
    private String txRef;
    private String message;
}

