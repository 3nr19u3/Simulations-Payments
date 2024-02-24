package com.payment.paymentservice.service.impl;


import com.payment.paymentservice.entity.Payment;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
@AllArgsConstructor
@NoArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private PaymentRepository paymentRepository;
    //private Currency currencyRepository;
    private ModelMapper modelMapper;

    private RestTemplate restTemplate;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              //CurrencyRepository currencyRepository,
                              ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        //this.currencyRepository = currencyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaymentResponse createPayment(PaymentDto paymentDto) {

        ResponseEntity<CurrencyDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/currency/"+paymentDto.getCurrency1(), CurrencyDto.class);
        ResponseEntity<CurrencyDto> responseEntity2 = restTemplate.getForEntity("http://localhost:8080/api/currency/"+paymentDto.getCurrency2(), CurrencyDto.class);
        CurrencyDto currency = responseEntity.getBody();
        CurrencyDto currency2 = responseEntity2.getBody();
        //Currency currencyI = currencyRepository.findByName(paymentDto.getCurrency1());
        //Currency currencyF =currencyRepository.findByName(paymentDto.getCurrency2());
        //Recovery data form MS-1
        Payment payment = mapToEntity(paymentDto);
        Payment newPayment = paymentRepository.save(payment);

        //return new PaymentResponse();
        assert currency != null;
        assert currency2 != null;
        return new PaymentResponse(newPayment.getAmount(),
                                    newPayment.getAmount()*currency.getValue(),
                                    newPayment.getOrigin_currency(),
                                    newPayment.getDestiny_currency(),
                                    currency2.getValue());
    }

    private Payment mapToEntity(PaymentDto paymentDto){
        return new Payment(null,
                            paymentDto.getAmount(),
                            paymentDto.getCurrency1(),
                            paymentDto.getCurrency2(),
                            1L);
    }

}
