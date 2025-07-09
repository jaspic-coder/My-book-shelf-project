package org.example.mybooklibrary.payment;

import org.example.mybooklibrary.payment.Payment;
import org.example.mybooklibrary.payment.Payment.BookFormat;
import org.example.mybooklibrary.payment.Payment.PaymentStatus;
import org.example.mybooklibrary.payment.PaymentNotFoundException;
import org.example.mybooklibrary.payment.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Creates a new payment record
     *
     * @param title     The book title
     * @param usageDays Number of days the book was used
     * @param format    Format of the book (HARD_COPY, EBOOK, AUDIOBOOK)
     * @return The created payment
     */
    public Payment createPayment(String title, int usageDays, BookFormat format) {
        Payment payment = new Payment(title, usageDays, format);
        payment.setId(UUID.randomUUID().toString());
        return paymentRepository.save(payment);
    }

    /**
     * Marks a payment as completed
     *
     * @param paymentId        The payment ID to complete
     * @param confirmationCode The confirmation code for the payment
     * @return The updated payment
     * @throws PaymentNotFoundException if payment is not found
     */
    public Payment completePayment(String paymentId, String confirmationCode) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + paymentId + " not found"));

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setConfirmationCode(confirmationCode);
        return paymentRepository.save(payment);
    }

    /**
     * Retrieves all payments
     *
     * @return List of all payments
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Retrieves all pending payments
     *
     * @return List of pending payments
     */
    public List<Payment> getPendingPayments() {
        return paymentRepository.findByStatus(PaymentStatus.PENDING);
    }

    /**
     * Calculates payment summary statistics
     *
     * @return PaymentSummary containing total pending and completed charges
     */
    public PaymentSummary getPaymentSummary() {
        List<Payment> payments = paymentRepository.findAll();

        double totalPending = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                .mapToDouble(Payment::getCharges)
                .sum();

        double totalCompleted = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .mapToDouble(Payment::getCharges)
                .sum();

        return new PaymentSummary(totalPending, totalCompleted);
    }

    /**
     * Record class for payment summary statistics
     */
    public record PaymentSummary(double totalPending, double totalCompleted) {}
}
