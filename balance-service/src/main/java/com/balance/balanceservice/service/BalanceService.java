package com.balance.balanceservice.service;

import com.balance.balanceservice.payload.BalanceDto;
import com.balance.balanceservice.payload.BalanceResponseDto;

public interface BalanceService {
    BalanceResponseDto getBalance(long userId);
    BalanceDto createBalance(BalanceDto balanceDto);
    BalanceDto updateBalance(BalanceDto postDto, long id);
}
