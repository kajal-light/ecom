package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductDTO implements Serializable {
    private String productId;
    private Double productPrice;
    private Integer productQuantity;
}
