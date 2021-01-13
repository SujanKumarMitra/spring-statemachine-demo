package com.github.sujankumarmitra.springstatemachinedemo.service;

import com.github.sujankumarmitra.springstatemachinedemo.entity.PaymentEntity;
import com.github.sujankumarmitra.springstatemachinedemo.model.Payment;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentEvent;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;
import com.github.sujankumarmitra.springstatemachinedemo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

import static com.github.sujankumarmitra.springstatemachinedemo.builder.PaymentBuilderImpl.builder;

@Service
public class PaymentServiceImpl implements PaymentService {

    public static final String PAYMENT_HEADER_NAME = "paymentId";
    private final PaymentRepository paymentRepository;
    private final StateMachineFactory<PaymentState, PaymentEvent> smFactory;
    private final StateMachineInterceptor<PaymentState, PaymentEvent> smInterceptor;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              StateMachineFactory<PaymentState, PaymentEvent> smFactory,
                              StateMachineInterceptor<PaymentState, PaymentEvent> smInterceptor) {
        this.paymentRepository = paymentRepository;
        this.smFactory = smFactory;
        this.smInterceptor = smInterceptor;
    }

    @Override
    public Payment createNewPayment(BigDecimal amount) {
        Payment payment = builder()
                .withId(String.valueOf(UUID.randomUUID()))
                .withAmount(amount)
                .withState(PaymentState.NEW)
                .build();
        return paymentRepository.save(new PaymentEntity(payment));
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.getOne(paymentId);
    }

    @Override
    public PaymentState preAuth(String paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = buildStateMachine(paymentId);
        sendEvent(paymentId, PaymentEvent.PRE_AUTH_APPROVED, stateMachine);

        return stateMachine.getState().getId();
    }

    @Override
    public PaymentState authorize(String paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = buildStateMachine(paymentId);
        sendEvent(paymentId, PaymentEvent.AUTH_APPROVED, stateMachine);

        return stateMachine.getState().getId();
    }

    @Override
    public PaymentState declineAuth(String paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = buildStateMachine(paymentId);
        sendEvent(paymentId, PaymentEvent.AUTH_DECLINED, stateMachine);

        return stateMachine.getState().getId();
    }

    private void sendEvent(String paymentId, PaymentEvent event, StateMachine<PaymentState, PaymentEvent> stateMachine) {
        Message<PaymentEvent> message = MessageBuilder
                .<PaymentEvent>withPayload(event)
                .setHeader(PAYMENT_HEADER_NAME, paymentId)
                .build();

        stateMachine.sendEvent(message);
    }

    private StateMachine<PaymentState, PaymentEvent> buildStateMachine(String paymentId) {
        Payment payment = paymentRepository.getOne(paymentId);
        StateMachine<PaymentState, PaymentEvent> stateMachine = smFactory.getStateMachine(payment.getId());

        stateMachine.stop();

        stateMachine
                .getStateMachineAccessor()
                .doWithAllRegions(smAccess -> {
                    smAccess.addStateMachineInterceptor(smInterceptor);
                    smAccess.resetStateMachine(
                            new DefaultStateMachineContext<>
                                    (payment.getState(), null, null, null));
                });

        stateMachine.start();
        return stateMachine;
    }
}
