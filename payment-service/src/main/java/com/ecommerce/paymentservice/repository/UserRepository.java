package com.ecommerce.paymentservice.repository;

import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT availableAmount FROM User WHERE userId =:userId")
    BigDecimal findAvailableBalanceByUserId(@Param("userId") String userId);
@Query("SELECT availableCreditLimit FROM User WHERE userId =:userId")
    BigDecimal findAvailableCreditLimitByUserId(@Param("userId") String userId);
}
