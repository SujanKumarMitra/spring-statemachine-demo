package com.github.sujankumarmitra.springstatemachinedemo.builder;

import com.github.sujankumarmitra.springstatemachinedemo.model.Payment;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;

import java.math.BigDecimal;

public interface PaymentBuilder {
    PaymentBuilder withId(String id);

    PaymentBuilder withState(PaymentState state);

    PaymentBuilder withAmount(BigDecimal amount);

    Payment build();
}
