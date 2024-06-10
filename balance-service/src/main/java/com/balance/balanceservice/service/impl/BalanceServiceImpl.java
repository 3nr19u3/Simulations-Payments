package com.balance.balanceservice.service.impl;

import com.balance.balanceservice.BalanceServiceApplication;
import com.balance.balanceservice.entity.Balance;
import com.balance.balanceservice.entity.Currency;
import com.balance.balanceservice.exception.APIException;
import com.balance.balanceservice.exception.ResourceNotFoundException;
import com.balance.balanceservice.payload.*;
import com.balance.balanceservice.repository.BalanceRepository;
import com.balance.balanceservice.repository.CurrencyRepository;
import com.balance.balanceservice.service.BalanceService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);
    private final BalanceServiceApplication balanceServiceApplication;

    private RestTemplate restTemplate;

    BalanceRepository balanceRepository;

    CurrencyRepository currencyRepository;

    ModelMapper modelMapper;

    public BalanceServiceImpl(BalanceRepository balanceRepository,
                              CurrencyRepository currencyRepository,
                              ModelMapper modelMapper,
                              RestTemplate restTemplate, BalanceServiceApplication balanceServiceApplication) {
        this.balanceRepository = balanceRepository;
        this.currencyRepository = currencyRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.balanceServiceApplication = balanceServiceApplication;
    }

    @Override
    public List<BalanceDto> getAllBalance() throws Exception {
        List<Balance> balance = balanceRepository.findAll();
        return balance.stream().map((element) -> modelMapper.map(element, BalanceDto.class)).toList();
    }

    @Override
    public BalanceResponseDto createBalance(BalanceRequestDto balanceRequestDto) throws Exception {
        //Get the user credentials
        UserDetails userDetails  = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("userDetails {}",userDetails.toString());

        UserDto userDto = getUserDto(userDetails.getUsername());
        Long userId = userDto.getId();
        logger.info("userId {}",userId);
        Balance balance = new Balance();
        //Validate if the user have or not an balance account created
        Optional<Balance> balanceOpt = balanceRepository.findByUserId(userId);

        balanceOpt.ifPresentOrElse(
                    (value)
                        -> {
                        logger.info("value {}",value.toString());
                        //if the user already have create an account
                        throw new APIException(HttpStatus.BAD_REQUEST, "This user already have a balance account created");
                    },
                    ()
                        -> {
                        //otherwise continue the balance creation process
                        balance.setCurrency(balanceRequestDto.getCurrency());
                        balance.setAmount(balanceRequestDto.getAmount());
                        balance.setUserId(userId);
                    }
            );

        logger.info("balance: {}",balance);
        //create the balance linked to userId
        balanceRepository.save(balance);
        //return the ResponseDto Object
        return new BalanceResponseDto(balance.getCurrency(),
                                      balance.getAmount(),
                                      userId);
    }

    @Override
    public BalanceDto updateBalance(BalanceUpdateRequestDto balanceUpdateRequestDto, long id) {
        long currencyId = balanceUpdateRequestDto.getCurrencyId();
        long userId = balanceUpdateRequestDto.getUserId();
        //validate the currencyId and get this value
        Optional<Currency> currencyOpt = currencyRepository.findById(currencyId);
        Currency currency = new Currency();

        currencyOpt.ifPresentOrElse(
                (value) -> {
                    currency.setName(value.getName());
                },
                () ->{
                    throw new APIException(HttpStatus.BAD_REQUEST, "CurrencyId invalid!");
                }
        );
        //validate the userId and get this value
        UserDto userDto = getUserDto(userId);
        //validate the balanceId and get this value
        Balance balance = balanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Balance", "id", id));

        balance.setAmount(balanceUpdateRequestDto.getAmount());
        balance.setCurrency(currency.getName());
        balance.setUserId(userDto.getId());

        Balance updatedBalance = balanceRepository.save(balance);
        return mapToDTO(updatedBalance);
    }

    @Override
    public BalanceResponseDto getBalanceById(long id) throws Exception {
        Balance balance = balanceRepository.findById(id)
                                            .orElseThrow(()-> new Exception("BalanceId not found!"));

        return new BalanceResponseDto(balance.getCurrency(),
                                      balance.getAmount(),
                                      balance.getUserId());
    }

    private UserDto getUserDto(String email) {
        String url = "http://localhost:8082/api/user?username=";
        ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(url.concat(email), UserDto.class);
        return responseEntity.getBody();
    }

    private UserDto getUserDto(long id) {
        String url = "http://localhost:8082/api/user/";
        ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(url.concat(String.valueOf(id)), UserDto.class);
        return responseEntity.getBody();
    }

    private Balance mapToEntity(BalanceDto balanceDto){
        return modelMapper.map(balanceDto, Balance.class);
    }

    private BalanceDto mapToDTO(Balance balance){
        return modelMapper.map(balance, BalanceDto.class);
    }


}