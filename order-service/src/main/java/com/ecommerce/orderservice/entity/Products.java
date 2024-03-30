package com.ecommerce.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Products {
    @Id
    private String productId;
    @Column
    private String productName;
    @Column
    private String category;
    @Column
    private Double price;
    @Column
    private LocalDate date;
    @Column
    private Double rating;
    @Column
    private Integer stock;

}
