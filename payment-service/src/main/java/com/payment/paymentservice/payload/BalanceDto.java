package com.payment.paymentservice.payload;

public record BalanceDto (int id,
                          String currency,
                          double amount,
                          int userId){

}
