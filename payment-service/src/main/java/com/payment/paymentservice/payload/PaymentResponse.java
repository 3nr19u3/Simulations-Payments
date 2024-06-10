package com.payment.paymentservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private double amount;//amount of payment
    private double newAmount;//amount of balance after payment
    private String oCurrency;//currency of payment
    private String dCurrency;//currency of balance
    private double currencyValue;//value of currency
}
