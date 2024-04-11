package com.ecommersce.productservice.controller;

import com.ecommerce.dto.ProductDTO;
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
        ProductDTO request=getProductsDto();
        ProductDTO response= getProductsDtoResponse();
        response.setDate(LocalDate.now());
        when( productService.getProductByProductId("shdddbhd")).thenReturn(response);
        ResponseEntity<ProductDTO> responseEntity=productFetcher.getProductByProductId("shdddbhd");
        Assertions.assertEquals(request, responseEntity.getBody());

    }


    @Test
    void getProductByName(){
        ProductDTO request=getProductsDto();
        ProductDTO response= getProductsDtoResponse();
        List<ProductDTO> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByProductName(request.getProductName())).thenReturn(listOfProducts);
        ResponseEntity<List<ProductDTO>> responseEntity=productFetcher.getProductByProductName(request.getProductName());
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }
    @Test
    void getProductByCategory(){
        ProductDTO request=getProductsDto();
        ProductDTO response= getProductsDtoResponse();
        List<ProductDTO> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByCategory(request.getCategory())).thenReturn(listOfProducts);
        ResponseEntity<List<ProductDTO>> responseEntity=productFetcher.getProductByCategory(request.getCategory());
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }
    @Test
    void getProductByPrice(){
        ProductDTO request=getProductsDto();
        ProductDTO response= getProductsDtoResponse();
        List<ProductDTO> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByPrice(23.44,34.0)).thenReturn(listOfProducts);
        ResponseEntity<List<ProductDTO>> responseEntity=productFetcher.getProductByPrice(23.44,34.0);
        Assertions.assertEquals(request, Objects.requireNonNull(responseEntity.getBody()).get(0));

    }

    @Test
    void getProductByRating(){
        ProductDTO request=getProductsDto();
        ProductDTO response= getProductsDtoResponse();
        List<ProductDTO> listOfProducts=new ArrayList<>();
        listOfProducts.add(request);

        response.setDate(LocalDate.now());
        when( productService.getProductByRating(4.0)).thenReturn(listOfProducts);
        ResponseEntity<List<ProductDTO>> responseEntity=productFetcher.getProductByRating(4.0);
        Assertions.assertEquals(request, responseEntity.getBody().get(0));

    }


    private ProductDTO getProductsDtoResponse() {
        ProductDTO productDTO =new ProductDTO();
        productDTO.setProductId("shdddbhd");
        productDTO.setProductName("laptop");
        productDTO.setProductPrice(2300.5);
        productDTO.setStock(1000);
        productDTO.setDate(LocalDate.now());

        return productDTO;
    }

    private ProductDTO getProductsDto() {
        ProductDTO productDTO =new ProductDTO();
        productDTO.setProductId("shdddbhd");
        productDTO.setProductName("laptop");
        productDTO.setProductPrice(2300.5);
        productDTO.setStock(1000);
        productDTO.setDate(LocalDate.now());

        return productDTO;
    }








}
