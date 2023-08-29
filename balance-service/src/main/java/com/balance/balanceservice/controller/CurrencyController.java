package com.balance.balanceservice.controller;

import com.balance.balanceservice.payload.CurrencyDto;
import com.balance.balanceservice.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<CurrencyDto> getCurrencyByName(@PathVariable(name = "name") String name){
        return ResponseEntity.ok(currencyService.getCurrency(name));
    }


    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@RequestBody CurrencyDto currencyDto){
        return new ResponseEntity<>(currencyService.createCurrency(currencyDto), HttpStatus.CREATED);
    }
}
