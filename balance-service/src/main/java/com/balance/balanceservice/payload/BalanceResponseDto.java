package com.balance.balanceservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponseDto {
    private String currency;
    private double amount;
    private long userId;
}
