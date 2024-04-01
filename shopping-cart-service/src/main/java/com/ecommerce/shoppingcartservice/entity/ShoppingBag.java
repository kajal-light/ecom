package com.ecommerce.shoppingcartservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class ShoppingBag {
@Id
@GeneratedValue
private Long cartItemId;
@Column
private Double totalPrice;
@Column
private Double productPrice;
@Column
private String productId;
@Column
private LocalDate createdAt;

@Column
private String userId;
@Column
private Integer quantity;

}
