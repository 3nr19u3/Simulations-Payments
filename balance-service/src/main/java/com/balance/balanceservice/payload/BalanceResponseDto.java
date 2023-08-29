package com.balance.balanceservice.payload;

import com.balance.balanceservice.entity.User;
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
    private User user;
}
