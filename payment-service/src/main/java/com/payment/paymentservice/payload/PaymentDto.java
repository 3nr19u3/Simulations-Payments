package com.payment.paymentservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private double amount;
    private String originCurrency;
    private String destinyCurrency;
    private int balanceId;
}
