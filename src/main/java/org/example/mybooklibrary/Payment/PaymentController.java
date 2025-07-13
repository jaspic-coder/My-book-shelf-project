package org.example.mybooklibrary.Payment;

import org.example.mybooklibrary.Borrow.BorrowRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final Map<String, PaymentService> paymentServices;
    private final BorrowRepository borrowRepository;

    public PaymentController(Map<String, PaymentService> paymentServices,
                             BorrowRepository borrowRepository) {
        this.paymentServices = paymentServices;
        this.borrowRepository = borrowRepository;
    }

    // Initiate a payment (MoMo or Card)
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(
            @RequestBody PaymentRequest request,
            @RequestParam Long borrowRecordId) {

        // Select service based on paymentMethod
        String serviceName = request.paymentMethod().equalsIgnoreCase("CARD")
                ? "payPalPaymentService"
                : "moMoPaymentService";

        PaymentService paymentService = paymentServices.get(serviceName);

        if (paymentService == null) {
            return ResponseEntity.badRequest()
                    .body(new PaymentResponse(false, "Invalid payment method", null, null));
        }

        // Process payment via chosen service
        PaymentResponse paymentResponse = paymentService.processPayment(request);

        if (paymentResponse.success()) {
            // Save payment info and update borrow record status
            borrowRepository.findById(borrowRecordId).ifPresent(record -> {
                record.setPaymentId(paymentResponse.transactionId());
                record.setPaymentStatus("PENDING");
                borrowRepository.save(record);
            });
        }

        return ResponseEntity.ok(paymentResponse);
    }

    // Endpoint to verify and complete payment after callback or polling
    @GetMapping("/complete")
    public ResponseEntity<String> completePayment(
            @RequestParam String paymentId,
            @RequestParam String paymentMethod) {

        String serviceName = paymentMethod.equalsIgnoreCase("CARD")
                ? "payPalPaymentService"
                : "moMoPaymentService";

        PaymentService paymentService = paymentServices.get(serviceName);

        if (paymentService == null) {
            return ResponseEntity.badRequest().body("Invalid payment method");
        }

        boolean verified = paymentService.verifyPayment(paymentId);

        if (verified) {
            borrowRepository.findByPaymentId(paymentId).ifPresent(record -> {
                record.setPaymentStatus("COMPLETED");
                borrowRepository.save(record);
            });
            return ResponseEntity.ok("Payment completed successfully");
        } else {
            return ResponseEntity.badRequest().body("Payment verification failed");
        }
    }

    // Simple pages for success and cancel redirects
    @GetMapping("/success")
    public String paymentSuccess() {
        return "payment-success";  // You can map this to a view if using Thymeleaf or similar
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "payment-cancel";
    }
}
