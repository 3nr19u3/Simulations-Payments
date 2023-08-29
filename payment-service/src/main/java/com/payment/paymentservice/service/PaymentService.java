package com.payment.paymentservice.service;


import com.payment.paymentservice.payload.PaymentDto;
import com.payment.paymentservice.payload.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentDto paymentDto);
}
