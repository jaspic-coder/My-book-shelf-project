package org.example.mybooklibrary.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long userId;
    private Long bookId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private String currency;
}

