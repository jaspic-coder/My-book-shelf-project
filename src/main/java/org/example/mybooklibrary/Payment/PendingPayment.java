package org.example.mybooklibrary.Payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PendingPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String currency;
    private String description;
    private String paymentMethod; // "CARD" or "MOMO"
    private String transactionId;
    private String status; // "PENDING", "COMPLETED", "FAILED"

    @Column(name = "payment_year")
    private Year year;

    private LocalDateTime createdAt = LocalDateTime.now();
}
