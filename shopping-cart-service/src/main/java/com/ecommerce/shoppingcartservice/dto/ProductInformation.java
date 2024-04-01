package com.ecommerce.shoppingcartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInformation implements Serializable {

    private String productId;
    private Double productPrice;
    private Integer productQuantity;
}
