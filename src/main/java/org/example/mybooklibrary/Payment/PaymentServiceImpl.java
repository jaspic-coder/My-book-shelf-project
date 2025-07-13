package org.example.mybooklibrary.Payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Year;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PayPalPaymentService payPalPaymentService;
    private final MoMoPaymentService moMoPaymentService;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PayPalPaymentService PayPalPaymentService,
                              MoMoPaymentService moMoPaymentService) {
        this.paymentRepository = paymentRepository;
        this.payPalPaymentService = PayPalPaymentService;
        this.moMoPaymentService = moMoPaymentService;
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Process payment based on method
        PaymentResponse response = switch (request.paymentMethod().toUpperCase()) {
            case "CARD" -> payPalPaymentService.processPayment(request);
            case "MOMO" -> moMoPaymentService.processPayment(request);
            default -> throw new IllegalArgumentException("Unsupported payment method");
        };

        // Save payment record
        if (response.success()) {
            PendingPayment payment = new PendingPayment();
            payment.setAmount(request.amount());
            payment.setCurrency(request.currency());
            payment.setDescription("Payment for: " + request.bookTitle());
            payment.setPaymentMethod(request.paymentMethod());
            payment.setTransactionId(response.transactionId());
            payment.setStatus("PENDING");

            paymentRepository.save(payment);
        }

        return response;
    }

    @Override
    public boolean verifyPayment(String paymentId) {
        // First check if we have a record
        PendingPayment payment = paymentRepository.findByTransactionId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Verify with appropriate provider
        boolean verified = switch (payment.getPaymentMethod().toUpperCase()) {
            case "CARD" -> payPalPaymentService.verifyPayment(paymentId);
            case "MOMO" -> moMoPaymentService.verifyPayment(paymentId);
            default -> false;
        };

        if (verified) {
            payment.setStatus("COMPLETED");
            paymentRepository.save(payment);
        }

        return verified;
    }

    // Additional methods from your original implementation
    public List<PendingPayment> getPaymentsBetweenYears(Year start, Year end) {
        return paymentRepository.findByYearBetween(start, end);
    }

    public PendingPayment savePayment(PendingPayment payment) {
        return paymentRepository.save(payment);
    }

    public double getTotalPendingAmount() {
        return paymentRepository.findAll().stream()
                .filter(p -> "PENDING".equals(p.getStatus()))
                .mapToDouble(PendingPayment::getAmount)
                .sum();
    }
}
