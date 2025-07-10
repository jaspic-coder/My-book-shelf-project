package org.example.mybooklibrary.Payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Year;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PendingPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private double amount;
    private Year year;

}
