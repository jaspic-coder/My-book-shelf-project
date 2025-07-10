package org.example.mybooklibrary.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChargeService {
    private final ChargeRepository chargeRepository;

    @Autowired
    public ChargeService(ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
    }

    public List<Charge> getAllCharges() {
        return chargeRepository.findAll();
    }

    public List<Charge> getChargesByBook(String book) {  // Updated method name and parameter
        return chargeRepository.findByBookContainingIgnoreCase(book);
    }

    public Charge saveCharge(Charge charge) {
        return chargeRepository.save(charge);
    }
}