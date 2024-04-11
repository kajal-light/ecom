package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductDTO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    String createProduct(ProductDTO data) throws RuntimeException;

    void createListOfProduct(List<ProductDTO> data);

    void updateProduct(String productId, ProductDTO productDTO) ;

    void deleteProduct(String productId);

    ProductDTO getProductByProductId(String productId) ;

    List<ProductDTO> getProductByProductName(String name) ;

    List<ProductDTO> getProductByCategory(String category) ;

    List<ProductDTO> getProductByPrice(Double minPrice, Double maxPrice) ;
    List<ProductDTO> getProductByRating(Double rating) ;

    List<ProductDTO> getListOfStock(List<String> productsId) ;
}
