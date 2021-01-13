package com.github.sujankumarmitra.springstatemachinedemo.service;

import com.github.sujankumarmitra.springstatemachinedemo.model.Payment;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;

import java.math.BigDecimal;

public interface PaymentService {

    Payment createNewPayment(BigDecimal amount);

    Payment getPayment(String paymentId);

    PaymentState preAuth(String paymentId);

    PaymentState authorize(String paymentId);

    PaymentState declineAuth(String paymentId);
}
