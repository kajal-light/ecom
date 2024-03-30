package com.ecommersce.productservice.service;

import com.ecommersce.productservice.entity.Product;
import com.ecommersce.productservice.model.ProductData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void createProduct(ProductData data);

    void createListOfProduct(List<ProductData> data);

    void updateProduct(String id,ProductData data);

    void DeleteProduct(String id);

    ProductData getProductByProductId(String id);

    List<ProductData> getProductByName(String name);

    List<ProductData> getProductByCategory(String category);

    List<ProductData> getProductByPrice(Double price,Double maxPrice);

    List<ProductData> getProductByRating(Double rating);

    List<ProductData> getListOfStock(List<String> productsId);
}
