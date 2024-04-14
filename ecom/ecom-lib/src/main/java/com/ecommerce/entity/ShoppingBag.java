package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class ShoppingBag {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
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
