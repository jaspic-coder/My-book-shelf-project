package org.example.mybooklibrary.Payment;

import lombok.Data;

@Data
public class PaymentDto {
    private String externalTransactionId;
    private Money money;
    private String customerReference;
    private String serviceProviderUserName;

    @Data
    public static class Money {
        private String amount;
        private String currency;
    }
}
