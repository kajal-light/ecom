package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse implements Serializable ,EcommerceGenericResponse{

    private String userId;
    private PaymentType paymentMethod;
    private String orderAmount;
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;
}