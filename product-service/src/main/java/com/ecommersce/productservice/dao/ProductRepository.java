package com.ecommersce.productservice.dao;

import com.ecommersce.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(value = "select * from products where product_id = :product_id", nativeQuery = true)
    Optional<Product> findByProductId(@Param("product_id") String id);

    @Query(value = "select * from products where product_Name = :product_Name", nativeQuery = true)
    List<Product> findByProductName(@Param("product_Name") String name);

    @Query(value = "select * from products where category = :category", nativeQuery = true)
    List<Product> findByProductCategory(@Param("category") String category);

    @Query(value = "select * from products where price between minPrice and maxPrice", nativeQuery = true)
    List<Product> findByProductPrice(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    List<Product> findByRating(Double rating);

    @Query(value = "select product_Id,stock from products where product_Id in =: list", nativeQuery = true)
    List<Product> getProductsById(@Param("list") List<String> productsId);
}
