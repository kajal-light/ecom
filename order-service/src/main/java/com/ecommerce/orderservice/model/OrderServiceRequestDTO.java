package com.ecommerce.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceRequestDTO {


    private List<ProductDTO> products;
    private Double totalAmount;
    private String userId;


}
