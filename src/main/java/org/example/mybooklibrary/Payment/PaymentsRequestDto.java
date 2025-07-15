package org.example.mybooklibrary.Payment;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private Integer  amount;
    private String currency;
}
