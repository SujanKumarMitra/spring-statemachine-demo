package com.github.sujankumarmitra.springstatemachinedemo.config;

import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentEvent;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;
import com.github.sujankumarmitra.springstatemachinedemo.service.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PaymentStateMachineConfigTest {

    @Autowired
    private StateMachineFactory<PaymentState, PaymentEvent> smFactory;

    @Test
    void testNewStateMachine() {
        StateMachine<PaymentState, PaymentEvent> stateMachine = smFactory.getStateMachine();

        stateMachine.start(); // start the statemachine
        assertEquals(PaymentState.NEW, stateMachine.getState().getId());

        stateMachine.sendEvent(MessageBuilder
                .withPayload(PaymentEvent.PRE_AUTH_APPROVED)
                .setHeader(PaymentServiceImpl.PAYMENT_HEADER_NAME, String.valueOf(UUID.randomUUID()))
                .build());

        assertEquals(PaymentState.PRE_AUTH, stateMachine.getState().getId());
    }
}