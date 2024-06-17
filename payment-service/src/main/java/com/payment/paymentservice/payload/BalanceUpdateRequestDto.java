package com.payment.paymentservice.payload;


public record BalanceUpdateRequestDto (int currencyId,
                                       double amount,
                                       int userId){
}
