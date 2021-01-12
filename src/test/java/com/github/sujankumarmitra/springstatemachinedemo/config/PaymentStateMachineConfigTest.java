package com.github.sujankumarmitra.springstatemachinedemo.config;

import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentEvent;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class
})
class PaymentStateMachineConfigTest {

    @Autowired
    private StateMachineFactory<PaymentState, PaymentEvent> smFactory;

    @Test
    void testNewStateMachine() {
        StateMachine<PaymentState, PaymentEvent> stateMachine = smFactory.getStateMachine();

        stateMachine.start(); // start the statemachine
        assertEquals(PaymentState.NEW, stateMachine.getState().getId());

        stateMachine.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);

        assertEquals(PaymentState.PRE_AUTH, stateMachine.getState().getId());
    }
}