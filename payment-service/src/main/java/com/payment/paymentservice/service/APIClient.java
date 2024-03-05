package com.payment.paymentservice.service;

import com.payment.paymentservice.payload.CurrencyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url="http://localhost:8080", value = "PAYMENT-SERVICE")
public interface APIClient {
    @GetMapping("/api/currency/{name}")
    CurrencyDto getCurrencyByName(@PathVariable(name = "name") String name);
}
