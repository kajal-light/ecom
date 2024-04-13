package com.ecommersce.productservice.controller;

import com.ecommerce.dto.ProductResponse;

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
        ProductResponse productResponse =getProductsDto();
        ResponseEntity<String> response = productController.createProduct(productResponse);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    void createListOfProduct(){
        ProductResponse productResponse =getProductsDto();
        List<ProductResponse> listOfProduct=new ArrayList<>();
        listOfProduct.add(productResponse);
        ResponseEntity<String> response = productController.createListOfProduct(listOfProduct);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
    @Test
    void updateProduct(){
        ProductResponse productResponse =getProductsDto();
        ResponseEntity<String> response = productController.updateProduct("1234dfgh" , productResponse);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
    @Test
    void deleteProduct(){
        ResponseEntity<String> response = productController.deleteProduct("1234dfgh");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    private ProductResponse getProductsDto() {
        ProductResponse productResponse =new ProductResponse();
        productResponse.setProductId("jdgddb");
        productResponse.setProductName("laptop");
        productResponse.setProductPrice(23400.0);
        productResponse.setStock(1000);
        productResponse.setDate(LocalDate.now());

        return productResponse;
    }


}
