package org.example.mybooklibrary.payment;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Getter
@Setter
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Create payment" , security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> createPayment(
            @RequestParam String title,
            @RequestParam int usageDays,
            @RequestParam Payment.BookFormat format) {
        Payment payment = paymentService.createPayment(title, usageDays, format);
        return ResponseEntity.ok(payment);
    }

}