package com.ecommersce.productservice.controller;

import com.ecommerce.dto.ProductsDto;

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
        ProductsDto productsDto=getProductsDto();
        ResponseEntity<String> response = productController.createProduct(productsDto);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    void createListOfProduct(){
        ProductsDto productsDto=getProductsDto();
        List<ProductsDto> listOfProduct=new ArrayList<>();
        listOfProduct.add(productsDto);
        ResponseEntity<String> response = productController.createListOfProduct(listOfProduct);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
    @Test
    void updateProduct(){
        ProductsDto productsDto=getProductsDto();
        ResponseEntity<String> response = productController.updateProduct("1234dfgh" ,productsDto);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }
    @Test
    void deleteProduct(){
        ResponseEntity<String> response = productController.deleteProduct("1234dfgh");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    private ProductsDto getProductsDto() {
        ProductsDto productsDto=new ProductsDto();
        productsDto.setProductId("jdgddb");
        productsDto.setProductName("laptop");
        productsDto.setProductPrice(23400.0);
        productsDto.setStock(1000);
        productsDto.setDate(LocalDate.now());

        return productsDto;
    }


}
