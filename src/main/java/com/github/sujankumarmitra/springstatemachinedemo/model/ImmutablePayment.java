package com.github.sujankumarmitra.springstatemachinedemo.model;

import com.github.sujankumarmitra.springstatemachinedemo.util.PaymentUtils;

import java.math.BigDecimal;

public class ImmutablePayment implements Payment {

    private final String id;
    private final PaymentState state;
    private final BigDecimal amount;

    public ImmutablePayment(String id, PaymentState state, BigDecimal amount) {
        this.id = id;
        this.state = state;
        this.amount = amount;
    }

    public ImmutablePayment(Payment other) {
        this(
                other.getId(),
                other.getState(),
                other.getAmount());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public PaymentState getState() {
        return state;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Payment)) return false;

        return PaymentUtils.equals(this, (Payment) o);
    }

    @Override
    public int hashCode() {
        return PaymentUtils.hash(this);
    }

    @Override
    public String toString() {
        return PaymentUtils.toString(this);
    }
}
