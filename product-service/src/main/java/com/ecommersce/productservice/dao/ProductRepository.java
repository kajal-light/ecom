package com.ecommersce.productservice.dao;

import com.ecommersce.productservice.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, String> {

    @Query(value = "select * from products where product_id = :product_id", nativeQuery = true)
    Optional<Products> findByProductId(@Param("product_id") String id);

    @Query(value = "select * from products where product_Name = :product_Name", nativeQuery = true)
    List<Products> findByProductName(@Param("product_Name") String name);

    @Query(value = "select * from products where category = :category", nativeQuery = true)
    List<Products> findByProductCategory(@Param("category") String category);

    @Query(value = "select * from products where price between minPrice and maxPrice", nativeQuery = true)
    List<Products> findByProductPrice(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    List<Products> findByRating(Double rating);

   // @Query(value = "SELECT product_id,stock FROM products WHERE product_id IN :list", nativeQuery = true)
    List<Products> findByProductIdIn(List<String> productsId);
}
