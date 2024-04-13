package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductResponse;
import com.ecommerce.entity.Products;

import com.ecommersce.productservice.dao.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
 class ProductServiceImplTest {


    @InjectMocks
    private ProductServiceImpl productServiceImpl;
    @Mock
    private ProductRepository productRepository;


    @Test
    void createProduct(){
        ProductResponse productResponse =getProductsDtoResponse();
        Products productEntity = getProductEntity();
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        productServiceImpl.createProduct(productResponse);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void createListOfProduct(){
        ProductResponse productResponse =getProductsDtoResponse();
        List<ProductResponse> listOfProductResponse =new ArrayList<>();
        listOfProductResponse.add(productResponse);
        Products productEntity = getProductEntity();
        List<Products> listOfProductEntity=new ArrayList<>();
        listOfProductEntity.add(productEntity);
        when(productRepository.saveAll(listOfProductEntity)).thenReturn(listOfProductEntity);


      productServiceImpl.createListOfProduct(listOfProductResponse);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void updateProduct(){
        ProductResponse productResponse =getProductsDtoResponse();
        Products productEntity = getProductEntity();
        when(productRepository.findByProductId(productResponse.getProductId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);


          productServiceImpl.updateProduct(productResponse.getProductId(), productResponse);
        Assertions.assertNotNull(productEntity);

    }
    //@Test
    void deleteProduct(){
        ProductResponse productResponse =getProductsDtoResponse();

        Products productEntity = getProductEntity();

        when(productRepository.findById(productResponse.getProductId())).getMock();
       // when(productRepository.delete(productEntity));


        productServiceImpl.deleteProduct(productResponse.getProductId());
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void getProductByProductId(){
        ProductResponse productResponse =getProductsDtoResponse();

        Products productEntity = getProductEntity();

        when(productRepository.findByProductId(productResponse.getProductId())).thenReturn(Optional.of(productEntity));

        productResponse =productServiceImpl.getProductByProductId(productResponse.getProductId());
        Assertions.assertNotNull(productEntity);

    }

    @Test
    void getProductByProductName(){
        ProductResponse productResponse =getProductsDtoResponse();
        ;List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductName(productResponse.getProductName())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByProductName(productResponse.getProductName());
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void getProductByProductCategory(){
        ProductResponse productResponse =getProductsDtoResponse();

        List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductCategory(productResponse.getCategory())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByCategory(productResponse.getCategory());
        Assertions.assertNotNull(ListOfProductEntity);

    }


    @Test
    void getProductByPrice(){
        List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductPriceBetween(23.0,34.78)).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByPrice(23.0, 34.78);
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void getProductByRating(){


        List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByRating(2.0)).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByRating(2.0);
        Assertions.assertNotNull(ListOfProductEntity);

    }
    @Test
    void getListOfStock(){

        List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        List<String> productsId=new ArrayList<>();
        productsId.add("123drt");

        when(productRepository.findByProductIdIn(productsId)).thenReturn(ListOfProductEntity);

        productServiceImpl.getListOfStock(productsId);
        Assertions.assertNotNull(ListOfProductEntity);

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

    private Products getProductEntity() {
        Products products=new Products();
        products.setProductId("shdddbhd");
        products.setProductName("laptop");
        products.setProductPrice(2300.5);
        products.setStock(1000);
        products.setDate(LocalDate.now());

        return products;
    }

}
