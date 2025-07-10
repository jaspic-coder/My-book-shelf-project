package org.example.mybooklibrary.Payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PayPalController {

    private final PayPalService payPalService;
    // Uncomment when you implement MoMo
    // private final MoMoPaymentService momoPaymentService;

    // Update constructor when adding MoMo
    public PayPalController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    /*
     * PayPal Endpoints
     */
    @GetMapping("/paypal/create")
    public ResponseEntity<String> createPaypalOrder() {
        String approvalUrl = payPalService.createOrder();
        return ResponseEntity.ok(approvalUrl);
    }

    @GetMapping("/paypal/success")
    public ResponseEntity<String> paymentSuccess() {
        return ResponseEntity.ok("PayPal Payment successful!");
    }

    @GetMapping("/paypal/cancel")
    public ResponseEntity<String> paymentCancelled() {
        return ResponseEntity.ok("PayPal Payment cancelled.");
    }

    /*
     * MoMo Endpoints (placeholder)
     */
    @PostMapping("/momo/pay")
    public ResponseEntity<String> payWithMoMo(
            @RequestParam String phoneNumber,
            @RequestParam String amount,
            @RequestParam(defaultValue = "EUR") String currency) {
        return ResponseEntity.ok("MoMo payment endpoint - not yet implemented");

        // Future implementation:
        // String refId = momoPaymentService.requestToPay(phoneNumber, amount, currency);
        // return ResponseEntity.ok("MoMo Payment requested. Reference ID: " + refId);
    }
}