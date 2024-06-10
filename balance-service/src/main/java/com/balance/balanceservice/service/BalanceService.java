package com.balance.balanceservice.service;

import com.balance.balanceservice.payload.BalanceDto;
import com.balance.balanceservice.payload.BalanceRequestDto;
import com.balance.balanceservice.payload.BalanceResponseDto;
import com.balance.balanceservice.payload.BalanceUpdateRequestDto;

import java.util.List;

public interface BalanceService {
    List<BalanceDto> getAllBalance() throws Exception;
    BalanceResponseDto createBalance(BalanceRequestDto balanceRequestDto) throws Exception;
    BalanceDto updateBalance(BalanceUpdateRequestDto balanceUpdateRequestDto, long id);
    BalanceResponseDto getBalanceById(long id) throws Exception;
}
