package com.github.sujankumarmitra.springstatemachinedemo.builder;

import com.github.sujankumarmitra.springstatemachinedemo.model.Payment;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;
import com.github.sujankumarmitra.springstatemachinedemo.util.PaymentUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class PaymentBuilderImpl implements PaymentBuilder {

    private MutablePayment payment;

    private PaymentBuilderImpl() {
        payment = new MutablePayment();
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilderImpl();
    }

    @Override
    public PaymentBuilder withId(String id) {
        payment.setId(id);
        return this;
    }

    @Override
    public PaymentBuilder withState(PaymentState state) {
        payment.setState(state);
        return this;
    }

    @Override
    public PaymentBuilder withAmount(BigDecimal amount) {
        payment.setAmount(amount);
        return this;
    }

    @Override
    public Payment build() {
        return payment;
    }

    private class MutablePayment implements Payment {
        private String id;
        private PaymentState state;
        private BigDecimal amount;

        public MutablePayment() {
        }

        @Override
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public PaymentState getState() {
            return state;
        }

        public void setState(PaymentState state) {
            this.state = state;
        }

        @Override
        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || !(o instanceof Payment)) return false;

            return PaymentUtils.equals(this, (Payment) o);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, state, amount);
        }

        @Override
        public String toString() {
            return PaymentUtils.toString(this);
        }
    }

}
