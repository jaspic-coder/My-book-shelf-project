package org.example.mybooklibrary.payment;

import org.springframework.data.jpa.repository.JpaRepository;




import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByStatus(Payment.PaymentStatus status);
}
