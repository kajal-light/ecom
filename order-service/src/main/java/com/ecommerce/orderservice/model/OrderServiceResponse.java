package com.ecommerce.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceResponse  {

    Map<String, List<Integer>>  productWithStockAndOrderedQuantity;


}
