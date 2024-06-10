package com.payment.paymentservice.service.impl;


import com.payment.paymentservice.entity.Payment;
import com.payment.paymentservice.payload.BalanceDto;
import com.payment.paymentservice.payload.CurrencyDto;
import com.payment.paymentservice.payload.PaymentDto;
import com.payment.paymentservice.payload.PaymentResponse;
import com.payment.paymentservice.repository.PaymentRepository;
import com.payment.paymentservice.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;
    //private Currency currencyRepository;
    private ModelMapper modelMapper;
    //@Autowired
    //private RestTemplate restTemplate;
    //@Autowired
    //private APIClient apiClient;
    @Autowired
    private WebClient webClient;

    //private static final String APPLICATION_NAME="payment-service";

    @Override
    public Payment getPaymentById(String paymentId) {
        return null;
    }

    //@CircuitBreaker(name="${spring.application.name}", fallbackMethod = "fallbackMethod")
    public PaymentResponse createPayment(PaymentDto paymentDto) throws Exception {
        Payment payment = new Payment();
        PaymentResponse paymentResponse;
        logger.info("into .... createPayment Method()");
        // (First) : validate the balanceId (exist)
        //call to balance-service(http://localhost:9191/api/balance/{id})
        BalanceDto balanceDto = getBalanceDto(paymentDto.getBalanceId());

        if(balanceDto == null)
            throw new RuntimeException("failed to call another services");

        // (Second) : validate the currenciesIds (exist)
        //verify that the currencyIds fields should be diferents
        if(paymentDto.getOriginCurrency() == paymentDto.getDestinyCurrency())
            throw new RuntimeException("both currencyIds cannot are the same!");

        //Call to currencies endpoint && get the currenciesDto's objects from request
        CurrencyDto currencyDto1 = getCurrency(paymentDto.getOriginCurrency());
        CurrencyDto currencyDto2 = getCurrency(paymentDto.getDestinyCurrency());

        //TODO Three : validate the balance amount be available to pay
        //payment between the same currencies types
        if(paymentDto.getOriginCurrency().equals(balanceDto.getCurrency())){
            //insufficient balance
            if(balanceDto.getAmount() < paymentDto.getAmount())
                throw new Exception("payment failed - insufficient balance");

            //update the amount in the balance account
            //makeThePayment(balanceDto.getAmount(),paymentDto.getAmount());
            //TODO four : Make the update balance service
            double amountToSubtract = balanceDto.getAmount() - paymentDto.getAmount();
            //update the new balance amount

            //(mapToEntity)
            payment.setAmount(paymentDto.getAmount());
            payment.setOrigin_currency(currencyDto1.getId());
            payment.setDestiny_currency(currencyDto2.getId());
            payment.setBalanceId(1);

            //save the payment
            Payment newPayment = paymentRepository.save(payment);

            //response
            paymentResponse = new PaymentResponse(newPayment.getAmount(),
                                                  amountToSubtract,
                                                  currencyDto1.getName(),
                                                  currencyDto2.getName(),
                                                  currencyDto2.getValue());

        }else{
            //payment between diferent currencies types
            double amountToSubtract = 0;
            //get the value of currency from balance
            double balanceCurrencyValue = getCurrency(balanceDto.getCurrency()).getValue();
            //get the value of currency from payment
            double paymentCurrencyValue = getCurrency(paymentDto.getOriginCurrency()).getValue();

            if(balanceCurrencyValue > paymentCurrencyValue)
                amountToSubtract = paymentDto.getAmount() / balanceCurrencyValue;

            if(balanceCurrencyValue < paymentCurrencyValue)
                amountToSubtract = paymentDto.getAmount() * balanceCurrencyValue;

            if(balanceCurrencyValue == paymentCurrencyValue)
                amountToSubtract = paymentDto.getAmount();


            if(amountToSubtract > balanceDto.getAmount())
                throw new RuntimeException("payment failed - insufficient balance");

            //logger.info(String.valueOf(amountToSubtract));

            //update the amount in the balance account
            //makeThePayment(balanceDto.getAmount(),paymentDto.getAmount());
            //TODO four : Make the update balance service
            //double amountToSubtract = balanceDto.getAmount() - paymentDto.getAmount();
            //update the new balance amount

            //(mapToEntity)
            payment.setAmount(paymentDto.getAmount());
            payment.setOrigin_currency(currencyDto1.getId());
            payment.setDestiny_currency(currencyDto2.getId());
            payment.setBalanceId(1);

            //save the payment
            Payment newPayment = paymentRepository.save(payment);

            //response
            paymentResponse = new PaymentResponse(newPayment.getAmount(),
                                                  0.00,
                                                  currencyDto1.getName(),
                                                  currencyDto2.getName(),
                                                  currencyDto2.getValue());

        }

        return paymentResponse;
    }

    private void makeThePayment(double amount, double amount1) {
    //TODO : no yet implemented!!
    }

    private BalanceDto getBalanceDto(int balanceId) {
        BalanceDto balanceDto = webClient.get()
                                        .uri("http://localhost:8080/api/balance/"+balanceId)
                                        .retrieve()
                                        .bodyToMono(BalanceDto.class)
                                        .block();
        return balanceDto;
    }

    private CurrencyDto getCurrency(String name) {
        CurrencyDto currencyDto = webClient.get()
                                         .uri("http://localhost:8080/api/currency?name="+name)
                                         .retrieve()
                                         .bodyToMono(CurrencyDto.class)
                                         .block();

        return currencyDto;
    }

    /*private Payment mapToEntity(PaymentDto paymentDto){
        return new Payment(null,
                            paymentDto.getAmount(),
                            paymentDto.getOriginCurrency(),
                            paymentDto.getDestinyCurrency(),
                            1);
    }*/

}
