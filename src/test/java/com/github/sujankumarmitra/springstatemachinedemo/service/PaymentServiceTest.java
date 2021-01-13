package com.github.sujankumarmitra.springstatemachinedemo.service;

import com.github.sujankumarmitra.springstatemachinedemo.builder.PaymentBuilderImpl;
import com.github.sujankumarmitra.springstatemachinedemo.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class PaymentServiceTest {

    protected Payment payment;
    protected PaymentService serviceUnderTest;

    @BeforeEach
    void setUp() {
        payment = PaymentBuilderImpl
                .builder()
                .withId(valueOf(UUID.randomUUID()))
                .withAmount(new BigDecimal("123.45"))
                .build();
    }

    @Test
    void testPreAuth() {
        Payment newPayment = serviceUnderTest.createNewPayment(new BigDecimal("234.56"));

        System.out.println(newPayment);
        String paymentId = newPayment.getId();
        assertDoesNotThrow(() -> serviceUnderTest.preAuth(paymentId));

        Payment payment = assertDoesNotThrow(() -> serviceUnderTest.getPayment(paymentId));

        System.out.println(payment);

        assertNotNull(payment);
    }
}