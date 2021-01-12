package com.github.sujankumarmitra.springstatemachinedemo;


import com.github.sujankumarmitra.springstatemachinedemo.model.Payment;
import com.github.sujankumarmitra.springstatemachinedemo.model.PaymentState;
import com.github.sujankumarmitra.springstatemachinedemo.util.PaymentUtils;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class PaymentEntity implements Payment {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private PaymentState state;

    @Column(name = "amount")
    private BigDecimal amount;

    public PaymentEntity() {
    }

    public PaymentEntity(String id, PaymentState state, BigDecimal amount) {
        this.id = id;
        this.state = state;
        this.amount = amount;
    }

    public PaymentEntity(Payment other) {
        this(
                other.getId(),
                other.getState(),
                other.getAmount()
        );
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
        return PaymentUtils.hash(this);
    }

    @Override
    public String toString() {
        return PaymentUtils.toString(this);
    }
}
