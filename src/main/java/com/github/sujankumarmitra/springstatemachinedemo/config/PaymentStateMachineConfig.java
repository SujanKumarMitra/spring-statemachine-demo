package com.github.sujankumarmitra.springstatemachinedemo.config;

import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentEvent;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Random;

import static com.github.sujankumarmitra.springstatemachinedemo.service.PaymentServiceImpl.PAYMENT_HEADER_NAME;

@Configuration
@EnableStateMachineFactory
public class PaymentStateMachineConfig extends EnumStateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates()
                .initial(PaymentState.NEW)
                .states(EnumSet.allOf(PaymentState.class))
                .end(PaymentState.AUTH)
                .end(PaymentState.PRE_AUTH_ERROR)
                .end(PaymentState.AUTH_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(PaymentState.NEW)
                    .event(PaymentEvent.PRE_AUTHORIZE)
                    .target(PaymentState.NEW)
                .and().withExternal()
                    .source(PaymentState.NEW)
                    .event(PaymentEvent.PRE_AUTH_APPROVED)
                    .action(preAuthAction())
                    .guard(paymentIdGuard())
                    .target(PaymentState.PRE_AUTH)
                .and().withExternal()
                    .source(PaymentState.NEW)
                    .event(PaymentEvent.PRE_AUTH_DECLINED)
                    .target(PaymentState.PRE_AUTH_ERROR);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
        config.withConfiguration()
                .listener(getListener());

    }

    @Bean
    public Action<PaymentState, PaymentEvent> preAuthAction() {
        return new Action<PaymentState, PaymentEvent>() {
            private Random random = new Random();

            @Override
            public void execute(StateContext<PaymentState, PaymentEvent> context) {
                PaymentEvent event;
                if (random.nextBoolean()) {
                    event = PaymentEvent.PRE_AUTH_APPROVED;
                } else {
                    event = PaymentEvent.PRE_AUTH_DECLINED;
                }
                System.out.println(event);

                String paymentId = context.getMessage().getHeaders().get(PAYMENT_HEADER_NAME, String.class);
                Message<PaymentEvent> message = MessageBuilder
                        .withPayload(event)
                        .setHeader(PAYMENT_HEADER_NAME, paymentId)
                        .build();

                context.getStateMachine().sendEvent(message);
            }
        };
    }

    @Bean
    public Guard<PaymentState, PaymentEvent> paymentIdGuard() {
        return new Guard<PaymentState, PaymentEvent>() {
            @Override
            public boolean evaluate(StateContext<PaymentState, PaymentEvent> context) {
                return context.getMessage().getHeaders().containsKey(PAYMENT_HEADER_NAME);
            }
        };
    }

    private StateMachineListenerAdapter<PaymentState, PaymentEvent> getListener() {

        return new StateMachineListenerAdapter<PaymentState, PaymentEvent>() {
            private final Logger logger = LoggerFactory.getLogger(StateMachineListenerAdapter.class);

            @Override
            public void stateMachineStarted(StateMachine<PaymentState, PaymentEvent> stateMachine) {
                logger.info("Statemachine {} has started", stateMachine);
            }

            @Override
            public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
                logger.info("State Changed from {} to {}", from, to);
            }

        };
    }
}
