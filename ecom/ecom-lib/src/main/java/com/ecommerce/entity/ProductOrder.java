package com.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;


@Data
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue
    private Integer orderId;
    @Column
    private List<String> productId;
    @Column
    private Double totalAmount;
    @Column
    private String userId;

}
