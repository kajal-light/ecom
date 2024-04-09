package com.ecommerce.paymentservice.repository;

import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    BigDecimal findAvailableBalanceByUserId(String userId);

    BigDecimal findAvailableCreditLimitByUserId(String userId);
}
