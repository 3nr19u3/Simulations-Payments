package com.payment.paymentservice.payload;

public record PaymentResponse (double amount,
                               double newAmount,
                               String oCurrency,
                               String dCurrency,
                               double currencyValue){

}
