package com.balance.balanceservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceUpdateRequestDto {
    @NonNull
    private Long currencyId;
    @NonNull
    private Double amount;
    @NonNull
    private Long userId;
}
