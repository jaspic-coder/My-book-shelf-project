package org.example.mybooklibrary.Payment;




public record PaymentResponse(
        boolean success,
        String message,
        String paymentUrl,
        String transactionId
) {}