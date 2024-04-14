package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.entity.Products;

import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.OutOfStockException;
import com.ecommersce.productservice.dao.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

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
    void createProduct() {
        ProductRequest productRequest = getProductsDtoRequest();
        Products productEntity = getProductEntity();
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        productServiceImpl.createProduct(productRequest);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void createListOfProduct() {
        ProductRequest productRequest = getProductsDtoRequest();
        List<ProductRequest> productRequests = new ArrayList<>();
        productRequests.add(productRequest);
        Products productEntity = getProductEntity();
        List<Products> listOfProductEntity = new ArrayList<>();
        listOfProductEntity.add(productEntity);
        when(productRepository.saveAll(listOfProductEntity)).thenReturn(listOfProductEntity);


        productServiceImpl.createListOfProduct(productRequests);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void updateProduct() {
        ProductRequest productRequest = getProductsDtoRequest();
        Products productEntity = getProductEntity();
        when(productRepository.findByProductId(productRequest.getProductId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);


        productServiceImpl.updateProduct(productRequest.getProductId(), productRequest);
        Assertions.assertNotNull(productRequest);

    }

    @Test
    void deleteProduct() {
        ProductRequest productRequest = getProductsDtoRequest();

        Products productEntity = getProductEntity();
        productRepository.delete(productEntity);
        Optional<Products> deletedProduct = productRepository.findById(productRequest.getProductId());


        productServiceImpl.deleteProduct(productRequest.getProductId());
        assertFalse(deletedProduct.isPresent());

    }


    @Test
    void getProductByProductId() {
        ProductRequest productRequest = getProductsDtoRequest();

        Products productEntity = getProductEntity();

        when(productRepository.findByProductId(productRequest.getProductId())).thenReturn(Optional.of(productEntity));

        ProductResponse productByProductId = productServiceImpl.getProductByProductId(productRequest.getProductId());
        Assertions.assertNotNull(productByProductId);

    }

    @Test
    void testProductNotFoundByProductId() {

        when(productRepository.findByProductId("hdcdh")).thenReturn(null);

        assertThrows(NoProductFoundException.class, () -> {
            productServiceImpl.getProductByProductId("jsdhdbh");
        }, "Expected NoDataFoundException to be thrown");

    }

    @Test
    void getProductByProductName() {
        ProductRequest productRequest = getProductsDtoRequest();

        List<Products> ListOfProductEntity = new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductName(productRequest.getProductName())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByProductName(productRequest.getProductName());
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void testProductNotFoundByProductName() {

        when(productRepository.findByProductName("hdcdh")).thenReturn(null);

        assertThrows(NoProductFoundException.class, () -> {
            productServiceImpl.getProductByProductName("jsdhdbh");
        }, "Expected NoDataFoundException to be thrown");

    }

    @Test
    void getProductByProductCategory() {
        ProductRequest productRequest = getProductsDtoRequest();

        List<Products> ListOfProductEntity = new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductCategory(productRequest.getCategory())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByCategory(productRequest.getCategory());
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void testProductNotFoundByCategory() {

        when(productRepository.findByProductCategory("hdcdh")).thenReturn(null);

        assertThrows(NoProductFoundException.class, () -> {
            productServiceImpl.getProductByCategory("jsdhdbh");
        }, "Expected NoDataFoundException to be thrown");

    }

    @Test
    void getProductByPrice() {
        List<Products> ListOfProductEntity = new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductPriceBetween(23.0, 34.78)).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByPrice(23.0, 34.78);
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void testProductNotFoundByPrice() {

        when(productRepository.findByProductPriceBetween(23.0, 34.78)).thenReturn(new ArrayList<>());

        assertThrows(NoProductFoundException.class, () -> {
            productServiceImpl.getProductByPrice(23.0, 34.78);
        }, "Expected NoDataFoundException to be thrown");

    }

    @Test
    void getProductByRating() {


        List<Products> ListOfProductEntity = new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByRating(2.0)).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByRating(2.0);
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void testProductNotFoundByRating() {

        when(productRepository.findByRating(2.0)).thenReturn(new ArrayList<>());

        assertThrows(NoProductFoundException.class, () -> {
            productServiceImpl.getProductByRating(2.0);
        }, "Expected NoDataFoundException to be thrown");

    }

    @Test
    void getListOfStock() {

        List<Products> ListOfProductEntity = new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        List<String> productsId = new ArrayList<>();
        productsId.add("123drt");

        when(productRepository.findByProductIdIn(productsId)).thenReturn(ListOfProductEntity);

        productServiceImpl.getListOfStock(productsId);
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void testOutOfStock() {

        when(productRepository.findByProductIdIn(new ArrayList<>())).thenReturn(new ArrayList<>());

        assertThrows(OutOfStockException.class, () -> {
            productServiceImpl.getListOfStock(new ArrayList<>());
        }, "Expected NoDataFoundException to be thrown");

    }

    private ProductRequest getProductsDtoRequest() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId("shdddbhd");
        productRequest.setProductName("laptop");
        productRequest.setProductPrice(2300.5);
        productRequest.setStock(1000);
        productRequest.setDate(LocalDate.now());

        return productRequest;
    }

    private Products getProductEntity() {
        Products products = new Products();
        products.setProductId("shdddbhd");
        products.setProductName("laptop");
        products.setProductPrice(2300.5);
        products.setStock(1000);
        products.setDate(LocalDate.now());

        return products;
    }

}
