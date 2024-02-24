package com.payment.paymentservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private double amount;
    private double newAmount;
    private String oCurrency;
    private String dCurrency;
    private double currencyValue;
}
