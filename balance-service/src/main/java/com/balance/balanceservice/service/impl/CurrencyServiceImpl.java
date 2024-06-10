package com.balance.balanceservice.service.impl;

import com.balance.balanceservice.entity.Currency;
import com.balance.balanceservice.exception.APIException;
import com.balance.balanceservice.payload.CurrencyDto;
import com.balance.balanceservice.repository.CurrencyRepository;
import com.balance.balanceservice.service.CurrencyService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    private CurrencyRepository currencyRepository;

    private ModelMapper modelMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository,
                               ModelMapper modelMapper) {
        this.currencyRepository = currencyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        String currencyName = currencyDto.getName();
        //TODO: validate Admin permission (changes for later)

        //validate that still currency name don't exist
        Optional<Currency> currencyOpt = currencyRepository.findByName(currencyName);
        Currency currency = new Currency();

        currencyOpt.ifPresentOrElse(
                (value) -> {
                    throw new APIException(HttpStatus.BAD_REQUEST, "Currency Name Already Exist!");
                },
                () ->{
                    currency.setName(currencyDto.getName());
                    currency.setValue(currencyDto.getValue());
                    //currency.setLocal(currencyDto.getIsLocal());
                }
        );

        Currency newCurrency = currencyRepository.save(currency);
        return mapToDTO(newCurrency);
    }

    @Override
    public CurrencyDto getCurrencyById(long id) throws Exception {

        Currency currency = currencyRepository.findById(id)
                                              .orElseThrow(()-> new Exception("Currency ID not found!"));
        return mapToDTO(currency);
    }

    @Override
    public CurrencyDto getCurrencyByName(String name) throws Exception {
        Currency currency = currencyRepository.findByName(name)
                                              .orElseThrow(()-> new Exception("Currency Name not found!"));
        return mapToDTO(currency);
    }

    @Override
    public List<CurrencyDto> getAllCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        return currencies.stream().map((element) -> modelMapper.map(element, CurrencyDto.class)).toList();
    }

    private Currency mapToEntity(CurrencyDto currencyDto){
        return modelMapper.map(currencyDto, Currency.class);
    }

    private CurrencyDto mapToDTO(Currency currency){
        return modelMapper.map(currency, CurrencyDto.class);
    }
}
