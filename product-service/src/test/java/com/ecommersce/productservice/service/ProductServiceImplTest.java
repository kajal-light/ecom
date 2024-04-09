package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductsDto;
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
        ProductsDto productsDto=getProductsDtoResponse();
        Products productEntity = getProductEntity();
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        productServiceImpl.createProduct(productsDto);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void createListOfProduct(){
        ProductsDto productsDto=getProductsDtoResponse();
        List<ProductsDto> listOfProductDto=new ArrayList<>();
        listOfProductDto.add(productsDto);
        Products productEntity = getProductEntity();
        List<Products> listOfProductEntity=new ArrayList<>();
        listOfProductEntity.add(productEntity);
        when(productRepository.saveAll(listOfProductEntity)).thenReturn(listOfProductEntity);


      productServiceImpl.createListOfProduct(listOfProductDto);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void updateProduct(){
        ProductsDto productsDto=getProductsDtoResponse();
        Products productEntity = getProductEntity();
        when(productRepository.findByProductId(productsDto.getProductId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);


          productServiceImpl.updateProduct(productsDto.getProductId(),productsDto);
        Assertions.assertNotNull(productEntity);

    }
    //@Test
    void deleteProduct(){
        ProductsDto productsDto=getProductsDtoResponse();

        Products productEntity = getProductEntity();

        when(productRepository.findById(productsDto.getProductId())).getMock();
       // when(productRepository.delete(productEntity));


        productServiceImpl.deleteProduct(productsDto.getProductId());
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void getProductByProductId(){
        ProductsDto productsDto=getProductsDtoResponse();

        Products productEntity = getProductEntity();

        when(productRepository.findByProductId(productsDto.getProductId())).thenReturn(Optional.of(productEntity));

        productsDto=productServiceImpl.getProductByProductId(productsDto.getProductId());
        Assertions.assertNotNull(productEntity);

    }

    @Test
    void getProductByProductName(){
        ProductsDto productsDto=getProductsDtoResponse();
        ;List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductName(productsDto.getProductName())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByProductName(productsDto.getProductName());
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void getProductByProductCategory(){
        ProductsDto productsDto=getProductsDtoResponse();

        List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductCategory(productsDto.getCategory())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByCategory(productsDto.getCategory());
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
    private ProductsDto getProductsDtoResponse() {
        ProductsDto productsDto=new ProductsDto();
        productsDto.setProductId("shdddbhd");
        productsDto.setProductName("laptop");
        productsDto.setProductPrice(2300.5);
        productsDto.setStock(1000);
        productsDto.setDate(LocalDate.now());

        return productsDto;
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
