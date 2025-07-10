package org.example.mybooklibrary.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Year;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<PendingPayment> getPaymentsBetweenYears(Year start, Year end) {
        return paymentRepository.findByYearBetween(start, end);
    }

    public PendingPayment savePayment(PendingPayment payment) {
        return paymentRepository.save(payment);
    }

    public double getTotalPendingAmount() {
        return paymentRepository.findAll().stream()
                .mapToDouble(PendingPayment::getAmount)
                .sum();
    }
}