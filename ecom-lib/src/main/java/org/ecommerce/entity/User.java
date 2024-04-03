package com.ecommerce.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.math.BigDecimal;


public class User {

    @Id
    private String userId;
    @Column
    private BigDecimal availableAmount;
    @Column
    private String address;
    @Column
    private String availableCreditLimit;

}
