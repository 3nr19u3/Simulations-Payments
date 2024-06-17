package com.payment.paymentservice.controller;

import com.google.gson.*;
import com.payment.paymentservice.payload.PaymentDto;
import com.payment.paymentservice.payload.PaymentResponse;
import com.payment.paymentservice.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.payment.paymentservice.entity.Payment;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    private PaymentService paymentService;

    private GsonBuilder gsonBuilder = new GsonBuilder();

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentDto paymentDto) throws Exception {
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String paymentDtoJson = gson.toJson(paymentDto);
        LOGGER.info("INTO createPayment {}",paymentDtoJson);
        return new ResponseEntity<>(paymentService.createPayment(paymentDto), HttpStatus.CREATED);
    }

    //TODO: implements function to retrieve currency origin and currency destiny by payment ID
    //@GetMapping
    //public Payment getPaymentById(@RequestParam("payment") String paymentId){
    //    return paymentService.getPaymentById(paymentId);
    //}

}
