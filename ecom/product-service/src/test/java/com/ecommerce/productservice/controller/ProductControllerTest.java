package com.ecommerce.productservice.controller;

import com.ecommerce.dto.ProductRequest;

import com.ecommerce.productservice.service.ProductService;
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
        ProductRequest productRequest =getProductsDto();
        ResponseEntity<String> response = productController.createProduct(productRequest);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    void createListOfProduct(){
        ProductRequest productRequest =getProductsDto();
        List<ProductRequest> listOfProduct=new ArrayList<>();
        listOfProduct.add(productRequest);
        ResponseEntity<String> response = productController.createListOfProduct(listOfProduct);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
    @Test
    void updateProduct(){
        ProductRequest productRequest =getProductsDto();
        ResponseEntity<String> response = productController.updateProduct("1234dfgh" , productRequest);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
    @Test
    void deleteProduct(){
        ResponseEntity<String> response = productController.deleteProduct("1234dfgh");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    private ProductRequest getProductsDto() {
        ProductRequest productRequest =new ProductRequest();
        productRequest.setProductId("jdgddb");
        productRequest.setProductName("laptop");
        productRequest.setProductPrice(23400.0);
        productRequest.setStock(1000);
        productRequest.setDate(LocalDate.now());

        return productRequest;
    }


}
