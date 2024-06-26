package com.ecommerce.productservice.service;

import com.ecommerce.dto.ProductData;
import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;

import com.ecommerce.exception.InvalidProductException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    String createProduct(ProductRequest data) throws InvalidProductException;

    void createListOfProduct(List<ProductRequest> data);

    void updateProduct(String productId, ProductRequest productResponse) ;

    void deleteProduct(String productId);

    ProductResponse getProductByProductId(String productId) ;

    List<ProductResponse> getProductByProductName(String name) ;

    List<ProductResponse> getProductByCategory(String category) ;

    List<ProductResponse> getProductByCategoryAndPrice(String category, Double minPrice, Double maxPrice) ;
    List<ProductResponse> getProductByRating(String category,Double rating) ;

    List<ProductData> getListOfStock(List<String> productsId) ;
}
