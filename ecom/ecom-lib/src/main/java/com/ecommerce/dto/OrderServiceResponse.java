package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceResponse implements EcommerceGenericResponse {

    Map<String, List<Integer>>  productWithStockAndOrderedQuantity;


}
