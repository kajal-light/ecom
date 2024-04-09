package com.ecommersce.productservice.controller;

import com.ecommerce.dto.ProductsDto;
import com.ecommersce.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static org.mockito.Mockito.when;

@SpringBootTest
 class ProductFetcherTest {

    @InjectMocks
    private ProductFetcher productFetcher;

    @Mock
    private ProductService productService;

    @Test
    void getProduct(){
        ProductsDto request=getProductsDto();
        ProductsDto response= getProductsDtoResponse();
        response.setDate(LocalDate.now());
        when( productService.getProductByProductId("shdddbhd")).thenReturn(response);
        ResponseEntity<ProductsDto> responseEntity=productFetcher.getProductByProductId("shdddbhd");
        Assertions.assertEquals(request, responseEntity.getBody());

    }


    @Test
    void getProductByName(){
        ProductsDto request=getProductsDto();
        ProductsDto response= getProductsDtoResponse();
        List<ProductsDto> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByProductName(request.getProductName())).thenReturn(listOfProducts);
        ResponseEntity<List<ProductsDto>> responseEntity=productFetcher.getProductByProductName(request.getProductName());
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }
    @Test
    void getProductByCategory(){
        ProductsDto request=getProductsDto();
        ProductsDto response= getProductsDtoResponse();
        List<ProductsDto> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByCategory(request.getCategory())).thenReturn(listOfProducts);
        ResponseEntity<List<ProductsDto>> responseEntity=productFetcher.getProductByCategory(request.getCategory());
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }
    @Test
    void getProductByPrice(){
        ProductsDto request=getProductsDto();
        ProductsDto response= getProductsDtoResponse();
        List<ProductsDto> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByPrice(23.44,34.0)).thenReturn(listOfProducts);
        ResponseEntity<List<ProductsDto>> responseEntity=productFetcher.getProductByPrice(23.44,34.0);
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }

    @Test
    void getProductByRating(){
        ProductsDto request=getProductsDto();
        ProductsDto response= getProductsDtoResponse();
        List<ProductsDto> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByRating(4.0)).thenReturn(listOfProducts);
        ResponseEntity<List<ProductsDto>> responseEntity=productFetcher.getProductByRating(4.0);
        Assertions.assertEquals(request, responseEntity.getBody().get(0));

    }


    private ProductsDto getProductsDtoResponse() {
        ProductsDto productsDto=new ProductsDto();
        productsDto.setProductId("shdddbhd");
        productsDto.setProductName("laptop");
        productsDto.setProductPrice(2300.5);
        productsDto.setStock(1000);
        productsDto.setDate(LocalDate.now());

        return productsDto;
    }

    private ProductsDto getProductsDto() {
        ProductsDto productsDto=new ProductsDto();
        productsDto.setProductId("shdddbhd");
        productsDto.setProductName("laptop");
        productsDto.setProductPrice(2300.5);
        productsDto.setStock(1000);
        productsDto.setDate(LocalDate.now());

        return productsDto;
    }








}
