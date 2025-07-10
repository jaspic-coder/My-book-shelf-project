package org.example.mybooklibrary.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/charges")
public class ChargeController {
    @Autowired
    private ChargeService chargeService;

    @GetMapping
    public List<Charge> getAllCharges() {
        return chargeService.getAllCharges();
    }

    @GetMapping("/book/{book}")  // Updated path variable
    public List<Charge> getChargesByBook(@PathVariable String Book) {  // Updated method name and parameter
        return chargeService.getChargesByBook(Book);
    }

    @PostMapping
    public Charge createCharge(@RequestBody Charge charge) {
        return chargeService.saveCharge(charge);
    }
}