package com.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class User {

    @Id
    private String userId;
    @Column
    private BigDecimal availableAmount;
    @Column
    private String address;
    @Column
    private BigDecimal availableCreditLimit;

}
