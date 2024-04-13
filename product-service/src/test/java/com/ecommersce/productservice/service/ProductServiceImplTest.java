package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductRequest;
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
        ProductRequest productRequest = getProductsDtoRequest();
        Products productEntity = getProductEntity();
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        productServiceImpl.createProduct(productRequest);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void createListOfProduct(){
        ProductRequest productRequest = getProductsDtoRequest();
        List<ProductRequest> productRequests =new ArrayList<>();
        productRequests.add(productRequest);
        Products productEntity = getProductEntity();
        List<Products> listOfProductEntity=new ArrayList<>();
        listOfProductEntity.add(productEntity);
        when(productRepository.saveAll(listOfProductEntity)).thenReturn(listOfProductEntity);


      productServiceImpl.createListOfProduct(productRequests);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void updateProduct(){
        ProductRequest productRequest = getProductsDtoRequest();
        Products productEntity = getProductEntity();
        when(productRepository.findByProductId(productRequest.getProductId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);


          productServiceImpl.updateProduct(productRequest.getProductId(), productRequest);
        Assertions.assertNotNull(productEntity);

    }
    //@Test
    void deleteProduct(){
        ProductRequest productRequest = getProductsDtoRequest();

        Products productEntity = getProductEntity();

        when(productRepository.findById(productRequest.getProductId())).getMock();
       // when(productRepository.delete(productEntity));


        productServiceImpl.deleteProduct(productRequest.getProductId());
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void getProductByProductId(){
        ProductRequest productRequest = getProductsDtoRequest();

        Products productEntity = getProductEntity();

        when(productRepository.findByProductId(productRequest.getProductId())).thenReturn(Optional.of(productEntity));

        ProductResponse productByProductId = productServiceImpl.getProductByProductId(productRequest.getProductId());
        Assertions.assertNotNull(productByProductId);

    }

    @Test
    void getProductByProductName(){
        ProductRequest productRequest = getProductsDtoRequest();
        ;List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductName(productRequest.getProductName())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByProductName(productRequest.getProductName());
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void getProductByProductCategory(){
        ProductRequest productRequest = getProductsDtoRequest();

        List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductCategory(productRequest.getCategory())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByCategory(productRequest.getCategory());
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
    private ProductRequest getProductsDtoRequest() {
        ProductRequest productRequest =new ProductRequest();
        productRequest.setProductId("shdddbhd");
        productRequest.setProductName("laptop");
        productRequest.setProductPrice(2300.5);
        productRequest.setStock(1000);
        productRequest.setDate(LocalDate.now());

        return productRequest;
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
