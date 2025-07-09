package org.example.mybooklibrary.payment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mybooklibrary.user.User;

import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Title is required")
    private String title;
    private int usageDays;
    @Enumerated(EnumType.STRING)
    private BookFormat format;

    @PositiveOrZero
    private double penalties;

    @PositiveOrZero
    private double charges;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @PastOrPresent
    private LocalDate paymentDate;

    private String location;

    private String confirmationCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Payment(String title, int usageDays, BookFormat format) {
        this.title = title;
        this.usageDays = usageDays;
        this.format = format;
        this.status = PaymentStatus.PENDING;
        this.paymentDate = LocalDate.now();
        this.penalties = calculatePenalties();
        this.charges = calculateCharges();
    }

    private double calculatePenalties() {
        return usageDays > 7 ? (usageDays - 7) * 5 : 0;
    }

    private double calculateCharges() {
        double baseRate = (format == BookFormat.HARD_COPY) ? 100 : 80;
        return baseRate + (usageDays * 10);
    }

    public enum BookFormat {
        HARD_COPY, EBOOK, AUDIOBOOK
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED
    }
}
