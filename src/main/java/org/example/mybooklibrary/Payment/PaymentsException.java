package org.example.mybooklibrary.Payment;



public class PaymentsException extends Exception {
    public PaymentsException(String message) {
        super(message);
    }

    public PaymentsException(String message, Throwable cause) {
        super(message, cause);
    }
}