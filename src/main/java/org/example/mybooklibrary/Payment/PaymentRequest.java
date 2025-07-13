package org.example.mybooklibrary.Payment;

import org.example.mybooklibrary.Payment.CardDetails;

public record PaymentRequest(
        String paymentMethod, // "CARD" or "MOMO"
        double amount,
        String currency,
        String bookTitle,
        String userPhone, // For MoMo
        CardDetails cardDetails // For card payments
) {}