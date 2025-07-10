package org.example.mybooklibrary.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
    List<Charge> findByBookContainingIgnoreCase(String book);  // Updated method name
}

