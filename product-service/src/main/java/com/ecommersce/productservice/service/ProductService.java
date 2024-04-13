package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductData;
import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    String createProduct(ProductRequest data) throws RuntimeException;

    void createListOfProduct(List<ProductRequest> data);

    void updateProduct(String productId, ProductRequest productResponse) ;

    void deleteProduct(String productId);

    ProductResponse getProductByProductId(String productId) ;

    List<ProductResponse> getProductByProductName(String name) ;

    List<ProductResponse> getProductByCategory(String category) ;

    List<ProductResponse> getProductByPrice(Double minPrice, Double maxPrice) ;
    List<ProductResponse> getProductByRating(Double rating) ;

    List<ProductData> getListOfStock(List<String> productsId) ;
}
