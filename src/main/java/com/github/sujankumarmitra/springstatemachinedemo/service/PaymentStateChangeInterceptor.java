package com.github.sujankumarmitra.springstatemachinedemo.service;

import com.github.sujankumarmitra.springstatemachinedemo.entity.PaymentEntity;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentEvent;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;
import com.github.sujankumarmitra.springstatemachinedemo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentStateChangeInterceptor(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state,
                               Message<PaymentEvent> message,
                               Transition<PaymentState, PaymentEvent> transition,
                               StateMachine<PaymentState, PaymentEvent> stateMachine) {
        String paymentId = message.getHeaders().get("paymentId", String.class);
        Optional
                .ofNullable(paymentId)
                .ifPresent(pId -> {
                    PaymentEntity payment = paymentRepository.getOne(pId);
                    payment.setState(state.getId());
                    paymentRepository.save(payment);
                });
    }
}
