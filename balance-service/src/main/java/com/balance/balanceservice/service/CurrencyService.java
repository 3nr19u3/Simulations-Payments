package com.balance.balanceservice.service;

import com.balance.balanceservice.payload.CurrencyDto;

import java.util.List;


public interface CurrencyService {
    CurrencyDto createCurrency(CurrencyDto currencyDto);

    CurrencyDto getCurrencyById(long id) throws Exception;

    List<CurrencyDto> getAllCurrencies();

    CurrencyDto getCurrencyByName(String name) throws Exception;
}
