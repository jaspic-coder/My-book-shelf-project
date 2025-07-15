package org.example.mybooklibrary.Payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
class FLutterwavePaymentsService {
    private static final Logger logger = LoggerFactory.getLogger(FLutterwavePaymentsService.class);

    @Value("${FLUTTERWAVE_SECRET_KEY}")
    private String secretKey;

    @Value("${FLUTTERWAVE_BASE_URL}")
    private String baseUrl;

    private final PaymentsRepository paymentRepo;
    private final RestTemplate restTemplate;

    public FLutterwavePaymentsService(PaymentsRepository paymentRepo, RestTemplate restTemplate) {
        this.paymentRepo = paymentRepo;
        this.restTemplate = restTemplate;
    }

    public String initiatePayment(PaymentsRequestDto dto, String txRef) throws PaymentsException {
        try {
            // Validate input
            if (dto == null || dto.getAmount() == null || dto.getCurrency() == null ||
                    dto.getEmail() == null || txRef == null) {
                throw new PaymentsException("Invalid payment request data");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(secretKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("tx_ref", txRef);
            requestBody.put("amount", dto.getAmount());
            requestBody.put("currency", dto.getCurrency());
            requestBody.put("redirect_url", "http://localhost:8080/api/payment/callback");
            requestBody.put("payment_options", "card,banktransfer,ussd");

            Map<String, String> customer = new HashMap<>();
            customer.put("email", dto.getEmail());
            customer.put("first_name", dto.getFirstName());
            customer.put("last_name", dto.getLastName());
            requestBody.put("customer", customer);

            logger.info("Initiating payment with Flutterwave for txRef: {}", txRef);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String status = (String) responseBody.get("status");

                if ("success".equalsIgnoreCase(status)) {
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                    String paymentLink = (String) data.get("link");
                    logger.info("Payment initiated successfully. Payment link: {" +
                            "}", paymentLink);
                    return paymentLink;
                } else {
                    String message = (String) responseBody.get("message");
                    logger.error("Flutterwave API returned error: {}", message);
                    throw new PaymentsException("Payment initiation failed: " + message);
                }
            } else {
                logger.error("Unexpected response from Flutterwave: {}", response);
                throw new PaymentsException("Unexpected response from payment gateway");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error while calling Flutterwave API: {}", e.getResponseBodyAsString());
            throw new PaymentsException("Payment gateway error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while initiating payment", e);
            throw new PaymentsException("Unexpected error while initiating payment", e);
        }
    }

    public void storeSuccessfulPayment(String txRef,
                                       String transactionId,
                                       int amount,
                                       String currency,
                                       String email,
                                       String firstName,
                                       String lastName,
                                       String status) throws PaymentsException {
        try {
            if (paymentRepo.existsByTxRef(txRef)) {
                logger.warn("Payment with txRef {} already exists", txRef);
                return;
            }

            Payments payment = Payments.builder()
                    .txRef(txRef)
                    .transactionId(transactionId)
                    .status(status)
                    .amount(amount)
                    .currency(currency)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .paymentTime(LocalDateTime.now())
                    .build();

            paymentRepo.save(payment);
            logger.info("Successfully stored payment with txRef: {" +
                    "}", txRef);
        } catch (Exception e) {
            logger.error("Error storing payment with txRef: {" +
                    "}", txRef, e);
            throw new PaymentsException("Error storing payment", e);
        }
    }
}