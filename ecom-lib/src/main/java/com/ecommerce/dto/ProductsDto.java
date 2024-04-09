package com.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.web.JsonPath;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDto implements Serializable {
    private String productId;
  @JsonIgnore
    private String productName;
    @JsonIgnore
    private String category;
    @JsonIgnore
    private Double productPrice;
    @JsonIgnore
    private LocalDate date;
    @JsonIgnore
    private Double rating;

    private Integer stock;

}
