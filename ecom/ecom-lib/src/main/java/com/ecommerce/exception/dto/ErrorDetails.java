package com.ecommerce.exception.dto;


import com.ecommerce.dto.EcommerceGenericResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails implements EcommerceGenericResponse {
    private HttpStatus status;
    private String code;
    private String message;
    private String source;
    private String timestamp;
}
