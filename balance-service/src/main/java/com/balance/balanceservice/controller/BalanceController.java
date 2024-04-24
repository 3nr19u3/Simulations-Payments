package com.balance.balanceservice.controller;

import com.balance.balanceservice.payload.BalanceDto;
import com.balance.balanceservice.payload.BalanceResponseDto;
import com.balance.balanceservice.repository.BalanceRepository;
import com.balance.balanceservice.repository.UserRepository;
import com.balance.balanceservice.service.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    private BalanceService balanceService;
    private UserRepository userRepository;
    private final BalanceRepository balanceRepository;

    public BalanceController(BalanceService balanceService,
                             UserRepository userRepository,
                             BalanceRepository balanceRepository) {
                    this.balanceService = balanceService;
                    this.userRepository = userRepository;
                    this.balanceRepository = balanceRepository;
    }

    @GetMapping
    public BalanceResponseDto getBalance() throws Exception {
        return balanceService.getBalance();
    }


    @GetMapping("/{id}")
    public BalanceResponseDto getBalanceById(@PathVariable(value = "id") long id) throws Exception {
        return balanceService.getBalanceById(id);
    }

    @PostMapping
    public ResponseEntity<BalanceResponseDto> createBalance(@RequestBody BalanceDto balanceDto) throws Exception {
        logger.info("INTO createBalance()");
        return new ResponseEntity<>(balanceService.createBalance(balanceDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BalanceDto> updateBalance(@RequestBody BalanceDto balanceDto, @PathVariable(name = "id") long id){
        BalanceDto updatedBalance = balanceService.updateBalance(balanceDto, id);
        return new ResponseEntity<>(updatedBalance, HttpStatus.OK);
    }

}
