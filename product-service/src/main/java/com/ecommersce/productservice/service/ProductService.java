package com.ecommersce.productservice.service;

import com.ecommersce.productservice.dto.ProductsDto;

import org.exception.NoDataFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void createProduct(ProductsDto data);

    void createListOfProduct(List<ProductsDto> data);

    void updateProduct(String productId, ProductsDto productsDto) throws NoDataFoundException;

    void DeleteProduct(String productId);

    ProductsDto getProductByProductId(String productId) throws NoDataFoundException;

    List<ProductsDto> getProductByProductName(String name) throws NoDataFoundException;

    List<ProductsDto> getProductByCategory(String category) throws NoDataFoundException;

    List<ProductsDto> getProductByPrice(Double minPrice, Double maxPrice) throws NoDataFoundException;

    List<ProductsDto> getProductByRating(Double rating) throws NoDataFoundException;

    List<ProductsDto> getListOfStock(List<String> productsId) throws NoDataFoundException;
}
