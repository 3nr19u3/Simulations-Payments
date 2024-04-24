package com.balance.balanceservice.service;

import com.balance.balanceservice.payload.BalanceDto;
import com.balance.balanceservice.payload.BalanceResponseDto;

public interface BalanceService {
    BalanceResponseDto getBalance() throws Exception;
    BalanceResponseDto createBalance(BalanceDto balanceDto) throws Exception;
    BalanceDto updateBalance(BalanceDto postDto, long id);
    BalanceResponseDto getBalanceById(long id) throws Exception;
}
