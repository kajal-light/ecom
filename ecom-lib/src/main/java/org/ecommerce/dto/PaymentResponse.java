package com.ecommerce.paymentservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class PaymentResponse implements Serializable {
    private String userId;
    private PaymentType paymentMethod;
    private String orderAmount;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;
}
