package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest implements Serializable {
    private String userId;
    private PaymentType paymentMethod;
    private BigDecimal orderAmount;
    private LocalDateTime dateOfOrder;
}
