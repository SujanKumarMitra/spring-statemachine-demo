package com.github.sujankumarmitra.springstatemachinedemo.model;

public enum PaymentState {
    NEW,
    PRE_AUTH,
    PRE_AUTH_ERROR,
    AUTH,
    AUTH_ERROR,
}
