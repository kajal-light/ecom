package org.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceResponse  {

    Map<String, List<Integer>>  productWithStockAndOrderedQuantity;


}
