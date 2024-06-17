package com.payment.paymentservice.payload;

public record CurrencyDto (int id,
                           String name,
                           double value,
                           boolean isLocal){

}
