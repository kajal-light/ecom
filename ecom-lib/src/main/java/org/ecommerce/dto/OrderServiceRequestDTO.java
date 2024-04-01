package org.ecommerce.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceRequestDTO implements Serializable {


    private List<ProductDTO> products;
    private Double totalAmount;
    private String userId;


}
