package com.balance.balanceservice.service.impl;

import com.balance.balanceservice.entity.Currency;
import com.balance.balanceservice.payload.CurrencyDto;
import com.balance.balanceservice.repository.CurrencyRepository;
import com.balance.balanceservice.service.CurrencyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private CurrencyRepository currencyRepository;

    private ModelMapper modelMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               ModelMapper modelMapper) {
        this.currencyRepository = currencyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        //currencyRepository.findByName(currencyDto.getName())
        //        .orElseThrow(()-> new ResourceNotFoundException("Currency", "name", currencyDto.getId()));

        Currency currency = mapToEntity(currencyDto);
        Currency newCurrency = currencyRepository.save(currency);
        return mapToDTO(newCurrency);
    }

    @Override
    public CurrencyDto getCurrency(String name) {
        Currency currency = currencyRepository.findByName(name);
        return mapToDTO(currency);
    }

    private Currency mapToEntity(CurrencyDto currencyDto){
        return modelMapper.map(currencyDto, Currency.class);
    }

    private CurrencyDto mapToDTO(Currency currency){
        return modelMapper.map(currency, CurrencyDto.class);
    }
}
