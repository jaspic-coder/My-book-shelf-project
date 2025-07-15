package org.example.mybooklibrary.Payment;




import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mybooklibrary.book.Books;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payments" )
@Entity
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String txRef;
    private String status;
    private Integer amount;
    private String currency;
    private String email;
    private String firstName;
    private String lastName;

    private LocalDateTime paymentTime;
    @ManyToOne
private Books books;
}

