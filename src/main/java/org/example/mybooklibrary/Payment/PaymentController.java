package org.example.mybooklibrary.Payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Year;
import java.util.List;

    @RestController
    @RequestMapping("/api/payments")
    public class PaymentController {
        @Autowired
        private PaymentService paymentService;

        @GetMapping
        public List<PendingPayment> getAllPayments() {
            return paymentService.getPaymentsBetweenYears(Year.of(2022), Year.of(2027));
        }

        @GetMapping("/total")
        public ResponseEntity<Double> getTotalPendingAmount() {
            return ResponseEntity.ok(paymentService.getTotalPendingAmount());
        }

        @PostMapping
        public PendingPayment createPayment(@RequestBody PendingPayment payment) {
            return paymentService.savePayment(payment);
        }
    }

