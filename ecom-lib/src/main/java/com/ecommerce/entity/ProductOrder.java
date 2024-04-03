package com.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Entity
public class ProductOrder {
    @Id
    private String orderId;
    @Column
    private String productId;
    @Column
    private BigDecimal totalAmount;
    @Column
    private String userId;

}
