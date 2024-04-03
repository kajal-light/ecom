package com.ecommerce.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaymentRequest implements Serializable {
    private String userId;
    private PaymentType paymentMethod;
    private BigDecimal orderAmount;
    private LocalDateTime dateOfOrder;
}
