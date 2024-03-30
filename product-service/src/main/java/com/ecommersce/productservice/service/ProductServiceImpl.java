package com.ecommersce.productservice.service;

import com.ecommersce.productservice.dao.ProductRepository;
import com.ecommersce.productservice.entity.Product;
import com.ecommersce.productservice.model.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void createProduct(ProductData data) {
        Product productEntity = new Product();
        productEntity.setProductId(UUID.randomUUID().toString());
        productEntity.setProductName(data.getProductName());
        productEntity.setCategory(data.getCategory());
        productEntity.setDate(data.getDate());
        productEntity.setStock(data.getStock());
        productEntity.setRating(data.getRating());
        productEntity.setPrice(data.getPrice());
        productRepository.save(productEntity);

    }

    @Override
    public void createListOfProduct(List<ProductData> data) {
        List<Product> productEntity = new ArrayList<>();

        for (ProductData s : data) {
            Product entity = new Product();
            entity.setPrice(s.getPrice());
            entity.setRating(s.getPrice());
            entity.setCategory(s.getCategory());
            entity.setProductName(s.getProductName());
            entity.setDate(s.getDate());
            entity.setStock(s.getStock());
            productEntity.add(entity);
        }


        productRepository.saveAll(productEntity);

    }

    @Override
    public void updateProduct(String id, ProductData data) {
        Optional<Product> entity = productRepository.findById(id);
        if (entity.isPresent()) {
            Product productEntity = entity.get();
            productEntity.setProductName(data.getProductName());
            productEntity.setCategory(data.getCategory());
            productEntity.setDate(data.getDate());
            productEntity.setStock(data.getStock());
            productEntity.setRating(data.getRating());
            productEntity.setPrice(data.getPrice());
            productRepository.save(productEntity);

        } else {

            //throw exception

        }

    }

    @Override
    public void DeleteProduct(String id) {

        Optional<Product> entity = productRepository.findById(id);
        entity.ifPresent(product -> productRepository.delete(product));
    }

    @Override
    public ProductData getProductByProductId(String id) {

        Optional<Product> entity = productRepository.findByProductId(id);
        ProductData data = new ProductData();
        if (entity.isPresent()) {
            data.setProductName(entity.get().getProductName());
            data.setCategory(entity.get().getCategory());
            data.setDate(entity.get().getDate());
            data.setStock(entity.get().getStock());
            data.setRating(entity.get().getRating());
            data.setPrice(entity.get().getPrice());
        }


        return data;
    }

    @Override
    public List<ProductData> getProductByName(String name) {
        List<Product> entity = productRepository.findByProductName(name);
        List<ProductData> data = new ArrayList<>();

        for (Product s : entity) {
            ProductData a = new ProductData();
            a.setProductName(s.getProductName());
            a.setCategory(s.getCategory());
            a.setDate(s.getDate());
            a.setStock(s.getStock());
            a.setRating(s.getRating());
            a.setPrice(s.getPrice());
            data.add(a);
        }


        return data;
    }

    @Override
    public List<ProductData> getProductByCategory(String category) {
        List<Product> products = productRepository.findByProductCategory(category);
        List<ProductData> productDataList = new ArrayList<>();

        for (Product s : products) {
            ProductData productData = new ProductData();
            productData.setProductName(s.getProductName());
            productData.setCategory(s.getCategory());
            productData.setDate(s.getDate());
            productData.setStock(s.getStock());
            productData.setRating(s.getRating());
            productData.setPrice(s.getPrice());
            productDataList.add(productData);
        }


        return productDataList;
    }

    @Override
    public List<ProductData> getProductByPrice(Double minPrice, Double maxPrice) {
        List<Product> productEntity = productRepository.findByProductPrice(minPrice, maxPrice);
        List<ProductData> data = new ArrayList<>();
        ProductData a = new ProductData();
        for (Product s : productEntity) {
            a.setProductName(s.getProductName());
            a.setCategory(s.getCategory());
            a.setDate(s.getDate());
            a.setStock(s.getStock());
            a.setRating(s.getRating());
            a.setPrice(s.getPrice());

        }
        data.add(a);

        return data;
    }

    @Override
    public List<ProductData> getProductByRating(Double rating) {
        List<Product> entity = productRepository.findByRating(rating);
        List<ProductData> data = new ArrayList<>();
        ProductData a = new ProductData();
        for (Product s : entity) {
            a.setProductName(s.getProductName());
            a.setCategory(s.getCategory());
            a.setDate(s.getDate());
            a.setStock(s.getStock());
            a.setRating(s.getRating());
            a.setPrice(s.getPrice());

        }
        data.add(a);

        return data;


    }

    @Override
    public List<ProductData> getListOfStock(List<String> productsId) {

        List<Product> stocks = productRepository.getProductsById(productsId);
        List<ProductData> productDataList = new ArrayList<>();
        for (Product product : stocks) {
            ProductData productData = new ProductData();
            productData.setProductId(product.getProductId());
            productData.setStock(product.getStock());
            productDataList.add(productData);
        }

        return productDataList;
    }
}
