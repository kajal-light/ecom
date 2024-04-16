package com.ecommerce.shoppingcartservice.dao;

import com.ecommerce.entity.ShoppingBag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingBag, Long> {


    List<ShoppingBag> findByUserId(String userId);

    @Query(value = "select sum(total_price) from shopping_bag where user_id =:user_id", nativeQuery = true)
    double findAmountByUserId(@Param("user_id") String userId);
}
