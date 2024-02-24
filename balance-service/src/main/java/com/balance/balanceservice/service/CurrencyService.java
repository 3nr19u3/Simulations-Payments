package com.balance.balanceservice.service;

import com.balance.balanceservice.payload.CurrencyDto;


public interface CurrencyService {
    CurrencyDto createCurrency(CurrencyDto currencyDto);

    CurrencyDto getCurrency(String name);
}
