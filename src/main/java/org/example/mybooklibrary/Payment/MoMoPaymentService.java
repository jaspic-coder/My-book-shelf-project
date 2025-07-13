package org.example.mybooklibrary.Payment;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MoMoPaymentService implements PaymentService {

    private final RestTemplate restTemplate;
    private final MoMoConfig momoConfig;

    @Autowired
    public MoMoPaymentService(RestTemplate restTemplate, MoMoConfig momoConfig) {
        this.restTemplate = restTemplate;
        this.momoConfig = momoConfig;
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + momoConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = buildMoMoPayload(request);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    momoConfig.getApiUrl() + "/collection/token/",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String paymentUrl = (String) response.getBody().get("payment_url");
                String transactionId = (String) response.getBody().get("transaction_id");

                return new PaymentResponse(
                        true,
                        "MoMo payment initiated",
                        paymentUrl,
                        transactionId
                );
            } else {
                return new PaymentResponse(false, "MoMo payment failed: Invalid response from API", null, null);
            }
        } catch (Exception e) {
            return new PaymentResponse(false, "MoMo payment failed: " + e.getMessage(), null, null);
        }
    }

    private String buildMoMoPayload(PaymentRequest request) {
        return String.format("""
            {
              "amount": "%.2f",
              "currency": "%s",
              "external_id": "BOOK_%d",
              "payer": {
                "party_id": "%s",
                "party_id_type": "MSISDN"
              },
              "payer_message": "Payment for %s",
              "payee_note": "Book borrowing payment"
            }
            """,
                request.amount(),
                request.currency(),
                System.currentTimeMillis(),
                request.userPhone(),
                request.bookTitle()
        );
    }

    @Override
    public boolean verifyPayment(String paymentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + momoConfig.getApiKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    momoConfig.getApiUrl() + "/collection/v1_0/requesttopay/" + paymentId,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            return response.getStatusCode() == HttpStatus.OK &&
                    "SUCCESSFUL".equals(response.getBody().get("status"));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<PendingPayment> getPaymentsBetweenYears(Year start, Year end) {
        // Implementation would depend on your specific requirements
        // Currently returning empty list as placeholder
        return Collections.emptyList();
    }

    @Override
    public PendingPayment savePayment(PendingPayment payment) {
        // Implementation would depend on your persistence layer
        // Currently returning null as placeholder
        return null;
    }

    @Override
    public double getTotalPendingAmount() {
        // Implementation would depend on your specific requirements
        // Currently returning 0 as placeholder
        return 0;
    }
}