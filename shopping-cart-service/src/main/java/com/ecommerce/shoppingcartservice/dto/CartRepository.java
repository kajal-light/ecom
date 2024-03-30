package com.ecommerce.shoppingcartservice.dto;

import com.ecommerce.shoppingcartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem,Long> {


    List<CartItem> findByUserId(String userId);
@Query(value = "select sum(totalPrice) from CartItem where user_id =: user_id",nativeQuery = true)
double findAmountByUserId(@Param("user_id") String userId);
}
