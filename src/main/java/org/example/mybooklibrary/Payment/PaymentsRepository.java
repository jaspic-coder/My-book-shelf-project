package org.example.mybooklibrary.Payment;




import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
    boolean existsByTxRef(String txRef);
}

