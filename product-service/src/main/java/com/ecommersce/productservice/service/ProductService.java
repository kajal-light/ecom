package com.ecommersce.productservice.service;

import com.ecommersce.productservice.dto.ProductsDto;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void createProduct(ProductsDto data);

    void createListOfProduct(List<ProductsDto> data);

    void updateProduct(String productId, ProductsDto productsDto);

    void DeleteProduct(String productId);

    ProductsDto getProductByProductId(String productId);

    List<ProductsDto> getProductByProductName(String name);

    List<ProductsDto> getProductByCategory(String category);

    List<ProductsDto> getProductByPrice(Double minPrice, Double maxPrice);

    List<ProductsDto> getProductByRating(Double rating);

    List<ProductsDto> getListOfStock(List<String> productsId);
}
