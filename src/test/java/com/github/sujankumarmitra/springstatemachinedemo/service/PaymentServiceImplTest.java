package com.github.sujankumarmitra.springstatemachinedemo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PaymentServiceImplTest extends PaymentServiceTest {

    @Autowired
    public PaymentServiceImplTest(@Qualifier("paymentServiceImpl") PaymentService paymentService) {
        super.serviceUnderTest = paymentService;
    }

    @Override
    @Test
    @Transactional
    void testPreAuth() {
        super.testPreAuth();
    }
}
