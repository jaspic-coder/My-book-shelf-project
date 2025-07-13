package org.example.mybooklibrary.Payment;


import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PendingPayment, Long> {
    List<PendingPayment> findByYearBetween(Year start, Year end);
    Optional<PendingPayment> findByTransactionId(String transactionId);
}
