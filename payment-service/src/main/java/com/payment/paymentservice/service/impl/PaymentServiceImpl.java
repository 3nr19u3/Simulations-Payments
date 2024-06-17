package com.payment.paymentservice.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.paymentservice.entity.Payment;
import com.payment.paymentservice.payload.*;
import com.payment.paymentservice.repository.PaymentRepository;
import com.payment.paymentservice.service.PaymentService;

//import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private ModelMapper modelMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WebClient webClient;
    //usefully library to watch an object as nice log
    private GsonBuilder gsonBuilder = new GsonBuilder();


    //private static final String APPLICATION_NAME="payment-service";

    @Override
    public Payment getPaymentById(String paymentId) {
        return null;
    }

    //@CircuitBreaker(name="${spring.application.name}", fallbackMethod = "fallbackMethod")
    public PaymentResponse createPayment(PaymentDto paymentDto) throws Exception {
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        double paymentAmount = paymentDto.amount();
        String paymentCurrencyName = paymentDto.currencyName();
        LOGGER.info("into .... createPayment Method()");

        //Get the user credentials
        UserDetails userDetails  = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LOGGER.info("userDetails {}",userDetails.toString());
        String email = userDetails.getUsername();

        UserDto userDto = getUserByEmail(email);
        Long userId = userDto.getId();
        LOGGER.info("userId {}",userId);

        //Get the currency from currencyId (in request)
        CurrencyDto currencyDto = getCurrencyByName(paymentCurrencyName);
        String currencyName = currencyDto.name();
        String currencyDtoJson = gson.toJson(currencyDto);
        LOGGER.info(String.format("currencyDto: %s", currencyDtoJson));
        //int currencyId = currencyDto.id();

        //TODO : Handler the correct response from called to another service
        if(currencyDto == null)
            throw new RuntimeException("failed to call another services");

        // (First) : check if the user have an balance account already created and get this
        // call to balance-service(http://localhost:8080/api/balance?userId={userId})

        BalanceDto balanceDto = getBalanceByUserId(userId);
        String balanceDtoJson = gson.toJson(balanceDto);
        LOGGER.info(String.format("balanceDtoJson: %s", balanceDtoJson));

        //TODO : Handler the correct response from called to another service
        if(balanceDto == null)
            throw new RuntimeException("failed to call another services");

        String balanceCurrency = balanceDto.currency();
        //double balanceAmount = balanceDto.amount();

        // (Second) : validate the currenciesIds (exist)
        //verify that the currencyIds fields should be diferents
        if(currencyName.equals(balanceCurrency))
            return makeSimplePayment(balanceDto, currencyDto, paymentAmount);
        else
            return makePaymentWithCurrencyTransform(balanceDto, currencyDto, paymentAmount);

    }

    private PaymentResponse makePaymentWithCurrencyTransform(BalanceDto balanceDto,
                                                             CurrencyDto currencyDto,
                                                             double paymentAmount) {
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        int balanceId = balanceDto.id();
        String balanceCurrency = balanceDto.currency();
        double balanceAmount = balanceDto.amount();
        int userId = balanceDto.userId();

        int currencyId = currencyDto.id();
        String currencyName = currencyDto.name();
        double currencyValue = currencyDto.value();
        Payment payment = new Payment();
        PaymentResponse paymentResponse;

        //payment between diferent currencies types
        double amountToSubtract = 0;
        double amountCurrencyChange = 0;
        //get the object of currencyName from balance
        CurrencyDto currency = getCurrencyByName(balanceCurrency);
        double balanceCurrencyValue = currency.value();
        int balanceCurrencyId = currency.id();
        LOGGER.info(String.format("balanceCurrencyValue: %s",balanceCurrencyValue));

        //double balanceCurrencyValue = getCurrencyByName(balanceCurrency);
        //get the value of currency from payment
        double paymentCurrencyValue = getCurrencyByName(currencyName).value();
        LOGGER.info(String.format("paymentCurrencyValue: %s",paymentCurrencyValue));

        //Getting the amount after process the compute currency exchange
        amountCurrencyChange = getAmountCurrencyExchange(balanceCurrencyValue,paymentCurrencyValue,paymentAmount);

        LOGGER.info(String.format("amountCurrencyChange: %s",amountCurrencyChange));
        LOGGER.info(String.format("balanceAmount: %s",balanceAmount));

        //throw exception because insufficient funds
        if(amountCurrencyChange > balanceAmount)
            throw new RuntimeException("payment failed - insufficient funds");

        //Getting the difference amount to update
        double amountToUpdate = balanceAmount - amountCurrencyChange;
        LOGGER.info(String.format("amountToUpdate: %s",amountToUpdate));

        //BalanceUpdateRequestDto object
        BalanceUpdateRequestDto balanceUpdateRequestDto = new BalanceUpdateRequestDto(balanceCurrencyId, amountToUpdate, userId);
        //update the amount in the balance account - balance service call
        BalanceDto balanceUpdated = updateBalance(balanceId,balanceUpdateRequestDto);
        String balanceUpdatedJson = gson.toJson(balanceUpdated);
        LOGGER.info(String.format("balanceUpdatedJson: %s", balanceUpdatedJson));

        //(mapToEntity)
        payment.setAmount(amountToUpdate);
        payment.setOrigin_currency(currencyId);
        payment.setDestiny_currency(balanceCurrencyId);
        payment.setBalanceId(balanceDto.id());

        //save the payment
        Payment newPayment = paymentRepository.save(payment);
        double newAmount = newPayment.getAmount();

        //response
        return new PaymentResponse(paymentAmount,
                                   newAmount,
                                   currencyName,
                                   balanceCurrency,
                                   currencyValue);
    }

    private PaymentResponse makeSimplePayment(BalanceDto balanceDto,
                                              CurrencyDto currencyDto,
                                              double paymentAmount) throws Exception {
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Payment payment = new Payment();
        PaymentResponse paymentResponse;

        int balanceId = balanceDto.id();
        double balanceAmount = balanceDto.amount();
        LOGGER.info(String.format("balanceAmount: %s",balanceAmount));
        int userId = balanceDto.userId();

        int currencyId = currencyDto.id();
        String currencyName = currencyDto.name();
        double currencyValue = currencyDto.value();

        //insufficient funds
        if(balanceAmount < paymentAmount)
            throw new Exception("payment failed - insufficient funds");

        //Getting the difference amount to update
        double amountToUpdate = balanceAmount - paymentAmount;
        LOGGER.info(String.format("amountToUpdate: %s",amountToUpdate));

        //BalanceUpdateRequestDto object
        BalanceUpdateRequestDto balanceUpdateRequestDto = new BalanceUpdateRequestDto(currencyId, amountToUpdate, userId);
        //update the amount in the balance account - balance service call
        BalanceDto balanceUpdated = updateBalance(balanceId,balanceUpdateRequestDto);
        String balanceUpdatedJson = gson.toJson(balanceUpdated);
        LOGGER.info(String.format("balanceUpdatedJson: %s", balanceUpdatedJson));

        //(mapToEntity)
        payment.setAmount(amountToUpdate);
        payment.setOrigin_currency(currencyId);
        payment.setDestiny_currency(currencyId);
        payment.setBalanceId(balanceId);

        //save the payment
        Payment newPayment = paymentRepository.save(payment);
        double newAmount = newPayment.getAmount();
        //response
        return new PaymentResponse(paymentAmount,
                                   newAmount,
                                   currencyName,
                                   currencyName,
                                   currencyValue);
    }

    private double getAmountCurrencyExchange(double balanceCurrencyValue, double paymentCurrencyValue, double paymenAmount) {

        if(balanceCurrencyValue > paymentCurrencyValue)
            return  Math.round(paymenAmount / paymentCurrencyValue * 100.0) / 100.0;

        if(balanceCurrencyValue < paymentCurrencyValue)
            return Math.round(paymenAmount * paymentCurrencyValue * 100.0) / 100.0;

        return paymenAmount;
    }

    private UserDto getUserByEmail(String email) {
        String url = "http://localhost:8082/api/user?username=";

        UserDto userDto = webClient.get()
                                   .uri(url.concat(email))
                                   .retrieve()
                                   .bodyToMono(UserDto.class)
                                   .block();
        return userDto;
    }

    private BalanceDto getBalanceByUserId(long balanceId) {
        String url = "http://localhost:8080/api/balance?userId=";
        String token = getBearerToken();
        LOGGER.info("token {}",token);

        BalanceDto balanceDto = webClient.get()
                                        .uri(url.concat(String.valueOf(balanceId)))
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                        .retrieve()
                                        .bodyToMono(BalanceDto.class)
                                        .block();
        return balanceDto;
    }

    private CurrencyDto getCurrencyByName(String name) {
        String url = "http://localhost:8080/api/currency?name=";
        String token = getBearerToken();
        LOGGER.info("token {}",token);

        CurrencyDto currencyDto = webClient.get()
                                           .uri(url.concat(name))
                                           .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                           .retrieve()
                                           .bodyToMono(CurrencyDto.class)
                                           .block();

        return currencyDto;
    }

    private BalanceDto updateBalance(int id, BalanceUpdateRequestDto balanceUpdateRequestDto) {
        //String url = "http://localhost:8080/api/balance/";
        String token = getBearerToken();
        LOGGER.info("token {}",token);

        BalanceDto balanceDto1 = webClient.put()
                                          .uri("http://localhost:8080/api/balance/{id}", id)
                                          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                          .body(Mono.just(balanceUpdateRequestDto), BalanceUpdateRequestDto.class)
                                          .retrieve()
                                          .bodyToMono(BalanceDto.class)
                                          .doOnSuccess(response -> {
                                              // Handle successful response
                                              System.out.println("Success: " + response);
                                          })
                                          .doOnError(error -> {
                                              // Handle error response
                                              System.out.println("Error: " + error.getMessage());
                                          })
                                          .block();

        return balanceDto1;
    }

    private String getBearerToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
            Object credentials = authToken.getCredentials();
            if (credentials instanceof String) {
                return (String) credentials;
            }
        }
        return null;
    }

    /*private Payment mapToEntity(PaymentDto paymentDto){
        return new Payment(null,
                            paymentDto.getAmount(),
                            paymentDto.getOriginCurrency(),
                            paymentDto.getDestinyCurrency(),
                            1);
    }*/

}
