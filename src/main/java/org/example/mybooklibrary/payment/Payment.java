package org.example.mybooklibrary.payment;

import jakarta.persistence.*;
import lombok.Data;
import org.example.mybooklibrary.book.Books;
import org.example.mybooklibrary.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private String currency;

    private LocalDate paymentDate = LocalDate.now();
}
