package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductDTO;
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
        ProductDTO productDTO =getProductsDtoResponse();
        Products productEntity = getProductEntity();
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        productServiceImpl.createProduct(productDTO);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void createListOfProduct(){
        ProductDTO productDTO =getProductsDtoResponse();
        List<ProductDTO> listOfProductDto=new ArrayList<>();
        listOfProductDto.add(productDTO);
        Products productEntity = getProductEntity();
        List<Products> listOfProductEntity=new ArrayList<>();
        listOfProductEntity.add(productEntity);
        when(productRepository.saveAll(listOfProductEntity)).thenReturn(listOfProductEntity);


      productServiceImpl.createListOfProduct(listOfProductDto);
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void updateProduct(){
        ProductDTO productDTO =getProductsDtoResponse();
        Products productEntity = getProductEntity();
        when(productRepository.findByProductId(productDTO.getProductId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);


          productServiceImpl.updateProduct(productDTO.getProductId(), productDTO);
        Assertions.assertNotNull(productEntity);

    }
    //@Test
    void deleteProduct(){
        ProductDTO productDTO =getProductsDtoResponse();

        Products productEntity = getProductEntity();

        when(productRepository.findById(productDTO.getProductId())).getMock();
       // when(productRepository.delete(productEntity));


        productServiceImpl.deleteProduct(productDTO.getProductId());
        Assertions.assertNotNull(productEntity);

    }


    @Test
    void getProductByProductId(){
        ProductDTO productDTO =getProductsDtoResponse();

        Products productEntity = getProductEntity();

        when(productRepository.findByProductId(productDTO.getProductId())).thenReturn(Optional.of(productEntity));

        productDTO =productServiceImpl.getProductByProductId(productDTO.getProductId());
        Assertions.assertNotNull(productEntity);

    }

    @Test
    void getProductByProductName(){
        ProductDTO productDTO =getProductsDtoResponse();
        ;List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductName(productDTO.getProductName())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByProductName(productDTO.getProductName());
        Assertions.assertNotNull(ListOfProductEntity);

    }

    @Test
    void getProductByProductCategory(){
        ProductDTO productDTO =getProductsDtoResponse();

        List<Products> ListOfProductEntity=new ArrayList<>();
        ListOfProductEntity.add(getProductEntity());
        when(productRepository.findByProductCategory(productDTO.getCategory())).thenReturn(ListOfProductEntity);

        productServiceImpl.getProductByCategory(productDTO.getCategory());
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
    private ProductDTO getProductsDtoResponse() {
        ProductDTO productDTO =new ProductDTO();
        productDTO.setProductId("shdddbhd");
        productDTO.setProductName("laptop");
        productDTO.setProductPrice(2300.5);
        productDTO.setStock(1000);
        productDTO.setDate(LocalDate.now());

        return productDTO;
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
