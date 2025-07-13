package org.example.mybooklibrary.Payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Year;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class PayPalPaymentService implements PaymentService {
    private final RestTemplate restTemplate;

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.api.base-url}")
    private String baseUrl;

    public PayPalPaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/v1/oauth2/token",
                request,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = buildCardPaymentPayload(request);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    baseUrl + "/v2/checkout/orders",
                    entity,
                    Map.class
            );

            String paymentId = (String) response.getBody().get("id");
            String approvalUrl = extractApprovalUrl(response.getBody());

            return new PaymentResponse(
                    true,
                    "Payment initiated",
                    approvalUrl,
                    paymentId
            );
        } catch (Exception e) {
            return new PaymentResponse(false, "Payment failed: " + e.getMessage(), null, null);
        }
    }

    private String extractApprovalUrl(Map<String, Object> responseBody) {
        return ((List<Map<String, String>>) responseBody.get("links")).stream()
                .filter(link -> "approve".equals(link.get("rel")))
                .findFirst()
                .map(link -> link.get("href"))
                .orElseThrow(() -> new RuntimeException("No approval URL found"));
    }

    private String buildCardPaymentPayload(PaymentRequest request) {
        return String.format("""
            {
              "intent": "CAPTURE",
              "purchase_units": [{
                "amount": {
                  "currency_code": "%s",
                  "value": "%.2f"
                },
                "description": "Book Payment: %s"
              }],
              "payment_source": {
                "card": {
                  "number": "%s",
                  "expiry": "%s",
                  "name": "%s",
                  "security_code": "%s",
                  "billing_address": {
                    "address_line_1": "%s"
                  }
                }
              }
            }
            """,
                request.currency(),
                request.amount(),
                request.bookTitle(),
                request.cardDetails().number(),
                request.cardDetails().expiry(),
                request.cardDetails().name(),
                request.cardDetails().cvv(),
                request.cardDetails().billingAddress()
        );
    }

    @Override
    public boolean verifyPayment(String paymentId) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/v2/checkout/orders/" + paymentId,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return "COMPLETED".equals(response.getBody().get("status"));
    }

    @Override
    public List<PendingPayment> getPaymentsBetweenYears(Year start, Year end) {
        return List.of();
    }

    @Override
    public PendingPayment savePayment(PendingPayment payment) {
        return null;
    }

    @Override
    public double getTotalPendingAmount() {
        return 0;
    }
}