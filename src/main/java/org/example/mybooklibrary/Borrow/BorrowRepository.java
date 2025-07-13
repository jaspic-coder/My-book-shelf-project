package org.example.mybooklibrary.Borrow;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrows, Long> {

    Optional<Borrows> findByPaymentId(String paymentId);
}

