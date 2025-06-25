package org.example.mybooklibrary.payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private String userName;
    private String bookTitle;
    private BigDecimal amount;
    private String method;
    private String status;
    private String transactionId;
    private String currency;
    private LocalDate paymentDate;
}

