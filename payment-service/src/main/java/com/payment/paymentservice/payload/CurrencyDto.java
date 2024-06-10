package com.payment.paymentservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {
    private int id;
    private String name;
    private double value;
    private boolean isLocal;

}
