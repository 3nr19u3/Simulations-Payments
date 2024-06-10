package com.balance.balanceservice.controller;

import com.balance.balanceservice.payload.BalanceDto;
import com.balance.balanceservice.payload.BalanceRequestDto;
import com.balance.balanceservice.payload.BalanceResponseDto;
import com.balance.balanceservice.payload.BalanceUpdateRequestDto;
import com.balance.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping
    public List<BalanceDto> getBalance() throws Exception {
        logger.info("INTO getAllBalance()");
        return balanceService.getAllBalance();
    }


    @GetMapping("/{id}")
    public BalanceResponseDto getBalanceById(@PathVariable(value = "id") long id) throws Exception {
        logger.info("INTO getBalanceById()");
        return balanceService.getBalanceById(id);
    }

    @PostMapping
    public ResponseEntity<BalanceResponseDto> createBalance(@RequestBody BalanceRequestDto balanceRequestDto) throws Exception {
        logger.info("INTO createBalance()");
        return new ResponseEntity<>(balanceService.createBalance(balanceRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BalanceDto> updateBalance(@Validated @RequestBody BalanceUpdateRequestDto balanceUpdateRequestDto, @PathVariable(name = "id") long id){
        logger.info("INTO updateBalance()");
        BalanceDto updatedBalance = balanceService.updateBalance(balanceUpdateRequestDto, id);
        return new ResponseEntity<>(updatedBalance, HttpStatus.OK);
    }

}
