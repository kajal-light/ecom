package com.ecommerce.entity;

import com.ecommerce.dto.PaymentStatus;
import com.ecommerce.dto.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Payment {
    @Id
    private String paymentId;
    @Column
    private String userId;
    @Column
    private PaymentType paymentMethod;
    @Column(precision = 8, scale = 2)
    private BigDecimal orderAmount;
    @Column
    private LocalDateTime orderDate;
    @Column
    private LocalDateTime paymentDate;
    @Column
    private String paymentStatus;
}
