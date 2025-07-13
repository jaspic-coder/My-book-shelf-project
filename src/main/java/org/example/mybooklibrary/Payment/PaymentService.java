package org.example.mybooklibrary.Payment;

import java.time.Year;
import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    boolean verifyPayment(String paymentId);
    List<PendingPayment> getPaymentsBetweenYears(Year start, Year end);
    PendingPayment savePayment(PendingPayment payment);
    double getTotalPendingAmount();
}



