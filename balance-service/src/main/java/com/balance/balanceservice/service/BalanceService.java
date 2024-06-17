package com.balance.balanceservice.service;

import com.balance.balanceservice.payload.*;

import java.util.List;

public interface BalanceService {
    List<BalanceDto> getAllBalance() throws Exception;
    BalanceResponseDto getBalanceById(long id) throws Exception;
    BalanceResponseDto getBalanceByUserId(long userId) throws Exception;
    BalanceResponseDto createBalance(BalanceRequestDto balanceRequestDto) throws Exception;
    BalanceDto updateBalance(BalanceUpdateRequestDto balanceUpdateRequestDto, long id);
}
