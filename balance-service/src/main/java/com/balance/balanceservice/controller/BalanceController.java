package com.balance.balanceservice.controller;


import com.balance.balanceservice.payload.BalanceDto;
import com.balance.balanceservice.payload.BalanceResponseDto;
import com.balance.balanceservice.service.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {
    private BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{id}")
    public BalanceResponseDto getBalance(@PathVariable(value = "id") long id){
        return balanceService.getBalance(id);
    }

    @PostMapping
    public ResponseEntity<BalanceDto> createBalance(@RequestBody BalanceDto balanceDto){
        return new ResponseEntity<>(balanceService.createBalance(balanceDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BalanceDto> updateBalance(@RequestBody BalanceDto balanceDto, @PathVariable(name = "id") long id){

        BalanceDto updatedBalance = balanceService.updateBalance(balanceDto, id);

        return new ResponseEntity<>(updatedBalance, HttpStatus.OK);
    }
}
