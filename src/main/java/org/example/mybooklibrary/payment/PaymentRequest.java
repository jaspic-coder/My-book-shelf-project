package org.example.mybooklibrary.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull private Long userId;
    @NotNull
    private Long bookId;
    @DecimalMin(value = "0.0", inclusive = false) private BigDecimal amount;
    @NotBlank private String paymentMethod;
    @NotBlank private String paymentStatus;
    @NotBlank
    private String transactionId;
    @NotBlank private String currency;
}


