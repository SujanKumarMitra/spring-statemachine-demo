package com.github.sujankumarmitra.springstatemachinedemo.model;

import java.math.BigDecimal;

public interface Payment {
    String getId();

    PaymentState getState();

    BigDecimal getAmount();
}
