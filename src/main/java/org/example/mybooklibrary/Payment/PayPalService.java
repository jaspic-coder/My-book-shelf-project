package org.example.mybooklibrary.Payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.api.base-url}")
    private String baseUrl;

    private String getAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/v1/oauth2/token", request, Map.class);

        return response.getBody().get("access_token").toString();
    }

    public String createOrder() {
        String accessToken = getAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
        {
          "intent": "CAPTURE",
          "purchase_units": [{
            "amount": {
              "currency_code": "USD",
              "value": "10.00"
            }
          }],
          "application_context": {
            "return_url": "http://localhost:8080/api/payments/success",
            "cancel_url": "http://localhost:8080/api/payments/cancel"
          }
        }
        """;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/v2/checkout/orders", request, Map.class);

        List<Map<String, String>> links = (List<Map<String, String>>) response.getBody().get("links");
        return links.stream()
                .filter(link -> link.get("rel").equals("approve"))
                .findFirst()
                .map(link -> link.get("href"))
                .orElse("No approval URL found");
    }
}
