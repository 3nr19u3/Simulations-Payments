package com.balance.balanceservice.service.impl;

import com.balance.balanceservice.controller.BalanceController;
import com.balance.balanceservice.entity.Balance;
import com.balance.balanceservice.entity.Currency;
import com.balance.balanceservice.entity.User;
import com.balance.balanceservice.exception.APIException;
import com.balance.balanceservice.exception.ResourceNotFoundException;
import com.balance.balanceservice.payload.BalanceDto;
import com.balance.balanceservice.payload.BalanceResponseDto;
import com.balance.balanceservice.payload.CurrencyDto;
import com.balance.balanceservice.repository.BalanceRepository;
import com.balance.balanceservice.repository.UserRepository;
import com.balance.balanceservice.service.BalanceService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements BalanceService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);

    UserRepository userRepository;

    BalanceRepository balanceRepository;

    ModelMapper modelMapper;

    public BalanceServiceImpl(UserRepository userRepository,
                              BalanceRepository balanceRepository,
                              ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.balanceRepository = balanceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BalanceResponseDto getBalance() throws Exception {
        //Get the user credentials
        UserDetails userDetails  = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                                  .orElseThrow(()-> new Exception("Username not found!"));

        //User user = userRepository.findById(user.getId()).orElseThrow();
        Balance balance = balanceRepository.getByUserId(user.getId());
        return new BalanceResponseDto(balance.getCurrency(),
                                      balance.getAmount(),
                                      user);
    }

    @Override
    public BalanceResponseDto createBalance(BalanceDto balanceDto) throws Exception {
        //Get the user credentials
        UserDetails userDetails  = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                                  .orElseThrow(()-> new Exception("Username not found!"));

        //validate that the user haven't another balance account
        Balance balance = balanceRepository.getByUserId(user.getId());

        if(balance != null)
            throw new APIException(HttpStatus.BAD_REQUEST, "You already have a balance account created");

        balance = new Balance(null,
                              balanceDto.getCurrency(),
                              balanceDto.getAmount(),
                              user.getId());

        balanceRepository.save(balance);

        return new BalanceResponseDto(balance.getCurrency(),
                                      balance.getAmount(),
                                      user);
    }

    @Override
    public BalanceDto updateBalance(BalanceDto balanceDto, long id) {
        // get post by id from the database
        Balance balance = balanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Balance", "id", id));

        balance.setAmount(balanceDto.getAmount());
        balance.setCurrency(balance.getCurrency());
        balance.setUserId(balance.getUserId());

        Balance updatedBalance = balanceRepository.save(balance);
        return mapToDTO(updatedBalance);
    }

    @Override
    public BalanceResponseDto getBalanceById(long id) throws Exception {
        //Get the user credentials
        //UserDetails userDetails  = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //User user = userRepository.findByEmail(userDetails.getUsername())
        //                          .orElseThrow(()-> new Exception("Username not found!"));

        Balance balance = balanceRepository.findById(id)
                                            .orElseThrow(()-> new Exception("BalanceId not found!"));

        return new BalanceResponseDto(balance.getCurrency(),
                                      balance.getAmount(),
                                      null);
    }

    private Balance mapToEntity(BalanceDto balanceDto){
        return modelMapper.map(balanceDto, Balance.class);
    }

    private BalanceDto mapToDTO(Balance balance){
        return modelMapper.map(balance, BalanceDto.class);
    }
}
