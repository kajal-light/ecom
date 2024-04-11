package com.ecommersce.productservice.controller;

import com.ecommerce.dto.ProductDTO;

import com.ecommersce.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@SpringBootTest
class ProductControllerTest {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productService;



    @Test
    void createProduct(){
        ProductDTO productDTO =getProductsDto();
        ResponseEntity<String> response = productController.createProduct(productDTO);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    void createListOfProduct(){
        ProductDTO productDTO =getProductsDto();
        List<ProductDTO> listOfProduct=new ArrayList<>();
        listOfProduct.add(productDTO);
        ResponseEntity<String> response = productController.createListOfProduct(listOfProduct);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
    @Test
    void updateProduct(){
        ProductDTO productDTO =getProductsDto();
        ResponseEntity<String> response = productController.updateProduct("1234dfgh" , productDTO);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
    @Test
    void deleteProduct(){
        ResponseEntity<String> response = productController.deleteProduct("1234dfgh");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    private ProductDTO getProductsDto() {
        ProductDTO productDTO =new ProductDTO();
        productDTO.setProductId("jdgddb");
        productDTO.setProductName("laptop");
        productDTO.setProductPrice(23400.0);
        productDTO.setStock(1000);
        productDTO.setDate(LocalDate.now());

        return productDTO;
    }


}
