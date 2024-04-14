package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements EcommerceGenericResponse {
    private String productId;
    private String productName;
    private String category;
    private Double productPrice;
    private LocalDate date;
    private Double rating;
    private Integer stock;

}
