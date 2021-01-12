package com.github.sujankumarmitra.springstatemachinedemo.util;

import com.github.sujankumarmitra.springstatemachinedemo.model.Payment;

import java.util.Objects;

public abstract class PaymentUtils {

    public static boolean equals(Payment p1, Payment p2) {
        return Objects.equals(p1.getId(), p2.getId()) &&
                p1.getState() == p2.getState() &&
                Objects.equals(p1.getAmount(), p2.getAmount());
    }

    public static int hash(Payment p) {
        return Objects.hash(p.getId(), p.getState(), p.getAmount());
    }

    public static String toString(Payment p) {
        return "Payment{" +
                "id='" + p.getId() + '\'' +
                ", state=" + p.getState() +
                ", amount=" + p.getAmount() +
                '}';
    }
}
