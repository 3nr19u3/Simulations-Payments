package com.balance.balanceservice.controller;

import com.balance.balanceservice.payload.CurrencyDto;
import com.balance.balanceservice.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    //TODO : refactor this method
    //@GetMapping
    //public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
    //    return ResponseEntity.ok(currencyService.getAllCurrencies());
    //}

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable(name = "id") long id) throws Exception {
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<CurrencyDto> getCurrencyByName(@RequestParam(value = "name") String name) throws Exception {
        return ResponseEntity.ok(currencyService.getCurrencyByName(name));
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@RequestBody CurrencyDto currencyDto){
        return new ResponseEntity<>(currencyService.createCurrency(currencyDto), HttpStatus.CREATED);
    }
}
