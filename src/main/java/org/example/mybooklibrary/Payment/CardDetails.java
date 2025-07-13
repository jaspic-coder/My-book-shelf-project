package org.example.mybooklibrary.Payment;


public record CardDetails(
        String number,
        String expiry,
        String name,
        String cvv,
        String billingAddress
) {}