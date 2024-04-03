package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceRequest implements Serializable {


    private List<ProductInformation> productInformation;
    private Double totalAmount;
    private String userId;


}
