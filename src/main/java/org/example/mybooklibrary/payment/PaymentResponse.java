package org.example.mybooklibrary.payment;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String id;
    private String title;
    private int usageDays;
    private String format;
    private double penalties;
    private double charges;
    private String status;
    private LocalDate paymentDate;
    private String confirmationCode;

    public static PaymentResponse fromEntity(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getTitle(),
                payment.getUsageDays(),
                payment.getFormat().name(),
                payment.getPenalties(),
                payment.getCharges(),
                payment.getStatus().name(),
                payment.getPaymentDate(),
                payment.getConfirmationCode()
        );
    }
}
