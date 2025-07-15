package org.example.mybooklibrary.Payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentsController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentsController.class);

    private final FLutterwavePaymentsService service;

    public PaymentsController(FLutterwavePaymentsService service) {
        this.service = service;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentsResponseDto> initiate(@RequestBody PaymentsRequestDto dto) throws PaymentsException {
        String txRef = "TXN_" + UUID.randomUUID().toString().substring(0, 8);
        String link = service.initiatePayment(dto, txRef);

        if (link == null) {
            return ResponseEntity.badRequest()
                    .body(new PaymentsResponseDto("error", null, txRef, "Payment failed"));
        }

        return ResponseEntity.ok(
                new PaymentsResponseDto("success", link, txRef, "Redirect to payment")
        );
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam Map<String, String> params) {
        String txRef = params.get("tx_ref");
        String transactionId = params.get("transaction_id");
        String status = params.get("status");

        if (txRef == null || transactionId == null || status == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "Missing required parameters (tx_ref, transaction_id, status)"
            ));
        }

        if (!"successful".equalsIgnoreCase(status)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "Payment not successful"
            ));
        }

        try {
            service.storeSuccessfulPayment(
                    txRef,
                    transactionId,
                    0,  // In a real setup, you'd query Flutterwave API or use webhook to get amount
                    "RWF",
                    "unknown@example.com",
                    "Unknown",
                    "User",
                    status
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Payment saved",
                    "txRef", txRef,
                    "transactionId", transactionId
            ));
        } catch (Exception e) {
            logger.error("Error processing payment callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error processing payment"
            ));
        }
    }
}
