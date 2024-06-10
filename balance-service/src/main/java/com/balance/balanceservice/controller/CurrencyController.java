package com.balance.balanceservice.controller;

import com.balance.balanceservice.payload.CurrencyDto;
import com.balance.balanceservice.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyController.class);

    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        LOGGER.info("INTO getAllCurrencies()");
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@Validated @RequestBody CurrencyDto currencyDto){
        LOGGER.info("INTO createCurrency()");
        return new ResponseEntity<>(currencyService.createCurrency(currencyDto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable(name = "id") long id) throws Exception {
        LOGGER.info("INTO getCurrencyById()");
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    @GetMapping
    public ResponseEntity<CurrencyDto> getCurrencyByName(@RequestParam(value = "name") String name) throws Exception {
        LOGGER.info("INTO getCurrencyByName()");
        return ResponseEntity.ok(currencyService.getCurrencyByName(name));
    }
}
