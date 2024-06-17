package com.balance.balanceservice.controller;

import com.balance.balanceservice.payload.*;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceController.class);

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BalanceDto>> getBalance() throws Exception {
        LOGGER.info("INTO getAllBalance()");
        return ResponseEntity.ok(balanceService.getAllBalance());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BalanceResponseDto> getBalanceById(@PathVariable(value = "id") long id) throws Exception {
        LOGGER.info("INTO getBalanceById()");
        return ResponseEntity.ok(balanceService.getBalanceById(id));
    }

    @GetMapping
    public ResponseEntity<BalanceResponseDto> getBalanceByUserId(@RequestParam(value = "userId") int userId) throws Exception {
        LOGGER.info("INTO getBalanceByUserId()");
        return ResponseEntity.ok(balanceService.getBalanceByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<BalanceResponseDto> createBalance(@RequestBody BalanceRequestDto balanceRequestDto) throws Exception {
        LOGGER.info("INTO createBalance()");
        return new ResponseEntity<>(balanceService.createBalance(balanceRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BalanceDto> updateBalance(@Validated @RequestBody BalanceUpdateRequestDto balanceUpdateRequestDto, @PathVariable(name = "id") long id){
        LOGGER.info("INTO updateBalance()");
        BalanceDto updatedBalance = balanceService.updateBalance(balanceUpdateRequestDto, id);
        return new ResponseEntity<>(updatedBalance, HttpStatus.OK);
    }

}
