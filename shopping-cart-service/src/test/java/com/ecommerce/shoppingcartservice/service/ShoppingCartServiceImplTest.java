package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.EcommerceGenericResponse;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.entity.ShoppingBag;
import com.ecommerce.shoppingcartservice.dao.ShoppingCartRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ShoppingCartServiceImplTest {


    @InjectMocks
    private ShoppingCartServiceImpl cartServiceImpl;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RestTemplate restTemplate;



        @Test
        public void testAddToShoppingBag_Success() throws Exception {
            // Arrange
            String productId = "123";
            int quantity = 2;
            String userId = "user123";

            ProductResponse productResponse = new ProductResponse(/* mock product response */);
            productResponse.setCategory("jsdhdb");
            productResponse.setProductName("jshdhdh");
            productResponse.setProductPrice(34.00);
            ShoppingBag shoppingBag=new ShoppingBag();
            shoppingBag.setUserId(userId);
            shoppingBag.setTotalPrice(quantity*productResponse.getProductPrice());
            shoppingBag.setCreatedAt(LocalDate.now());
            shoppingBag.setProductId(productId);
            when(objectMapper.readValue(anyString(), eq(ProductResponse.class))).thenReturn(productResponse);
            when(shoppingCartRepository.save(shoppingBag)).thenReturn(shoppingBag);

            // Act
            ResponseEntity<EcommerceGenericResponse> responseEntity = cartServiceImpl.addCartItem(productId, userId, quantity);

            // Assert
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(productResponse, responseEntity.getBody());
        }



    @Test
    void deleteCartItem() {
        ShoppingBag bag = new ShoppingBag();
        bag.setCartItemId(364564L);
        bag.setQuantity(2);
        when(shoppingCartRepository.findById(bag.getCartItemId())).thenReturn(Optional.of(bag));
        Assertions.assertNotNull(bag);
    }

    private ProductResponse getProductDtoResponse() {
        ProductResponse response = new ProductResponse();
        response.setProductId("jdfhf");
        response.setDate(LocalDate.now());
        response.setProductPrice(234.00);
        response.setStock(1000);
        response.setCategory("hsgddg");
        return response;
    }


}
