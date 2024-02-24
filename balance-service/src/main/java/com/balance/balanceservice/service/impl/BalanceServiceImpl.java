package com.balance.balanceservice.service.impl;

import com.balance.balanceservice.entity.Balance;
import com.balance.balanceservice.entity.Currency;
import com.balance.balanceservice.entity.User;
import com.balance.balanceservice.exception.ResourceNotFoundException;
import com.balance.balanceservice.payload.BalanceDto;
import com.balance.balanceservice.payload.BalanceResponseDto;
import com.balance.balanceservice.payload.CurrencyDto;
import com.balance.balanceservice.repository.BalanceRepository;
import com.balance.balanceservice.repository.UserRepository;
import com.balance.balanceservice.service.BalanceService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements BalanceService {

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
    public BalanceResponseDto getBalance(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Balance balance = balanceRepository.getByUserId(user.getId());
        return new BalanceResponseDto(balance.getCurrency(),
                                      balance.getAmount(),
                                      user);
    }

    @Override
    public BalanceDto createBalance(BalanceDto balanceDto) {
        Balance balance = new Balance(null,
                                       balanceDto.getCurrency(),
                                       balanceDto.getAmount(),
                                       1L);//TODO get the userId
        Balance balance1 = balanceRepository.save(balance);
        return mapToDTO(balance1);
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




    private Balance mapToEntity(BalanceDto balanceDto){
        return modelMapper.map(balanceDto, Balance.class);
    }

    private BalanceDto mapToDTO(Balance balance){
        return modelMapper.map(balance, BalanceDto.class);
    }
}
