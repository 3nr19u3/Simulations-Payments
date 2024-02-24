package com.payment.paymentservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private double amount;
    private String currency1;
    private String currency2;
    private int balanceId;

}
