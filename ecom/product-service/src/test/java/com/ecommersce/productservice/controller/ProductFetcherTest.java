package com.ecommersce.productservice.controller;

import com.ecommerce.dto.ProductResponse;
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
        ProductResponse request=getProductsDto();
        ProductResponse response= getProductsDtoResponse();
        response.setDate(LocalDate.now());
        when( productService.getProductByProductId("shdddbhd")).thenReturn(response);
        ResponseEntity<ProductResponse> responseEntity=productFetcher.getProductByProductId("shdddbhd");
        Assertions.assertEquals(request, responseEntity.getBody());

    }


    @Test
    void getProductByName(){
        ProductResponse request=getProductsDto();
        ProductResponse response= getProductsDtoResponse();
        List<ProductResponse> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByProductName(request.getProductName())).thenReturn(listOfProducts);
        ResponseEntity<List<ProductResponse>> responseEntity=productFetcher.getProductByProductName(request.getProductName());
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }
    @Test
    void getProductByCategory(){
        ProductResponse request=getProductsDto();
        ProductResponse response= getProductsDtoResponse();
        List<ProductResponse> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByCategory(request.getCategory())).thenReturn(listOfProducts);
        ResponseEntity<List<ProductResponse>> responseEntity=productFetcher.getProductByCategory(request.getCategory());
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }
    @Test
    void getProductByPrice(){
        ProductResponse request=getProductsDto();
        ProductResponse response= getProductsDtoResponse();
        List<ProductResponse> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByPrice(23.44,34.0)).thenReturn(listOfProducts);
        ResponseEntity<List<ProductResponse>> responseEntity=productFetcher.getProductByPrice(23.44,34.0);
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }

    @Test
    void getProductByRating(){
        ProductResponse request=getProductsDto();
        ProductResponse response= getProductsDtoResponse();
        List<ProductResponse> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByRating(4.0)).thenReturn(listOfProducts);
        ResponseEntity<List<ProductResponse>> responseEntity=productFetcher.getProductByRating(4.0);
        Assertions.assertEquals(request, responseEntity.getBody().get(0));

    }


    private ProductResponse getProductsDtoResponse() {
        ProductResponse productResponse =new ProductResponse();
        productResponse.setProductId("shdddbhd");
        productResponse.setProductName("laptop");
        productResponse.setProductPrice(2300.5);
        productResponse.setStock(1000);
        productResponse.setDate(LocalDate.now());

        return productResponse;
    }

    private ProductResponse getProductsDto() {
        ProductResponse productResponse =new ProductResponse();
        productResponse.setProductId("shdddbhd");
        productResponse.setProductName("laptop");
        productResponse.setProductPrice(2300.5);
        productResponse.setStock(1000);
        productResponse.setDate(LocalDate.now());

        return productResponse;
    }








}
