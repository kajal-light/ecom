package com.ecommerce.paymentservice.entity;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public class Payment {

    @Column(name = "ORDER_AMOUNT", precision = 8, scale = 2)
    private BigDecimal orderAmount;
}
