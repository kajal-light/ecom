package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.EcommerceGenericResponse;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.entity.Payment;
import com.ecommerce.entity.ShoppingBag;
import com.ecommerce.shoppingcartservice.dao.ShoppingCartRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

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


@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    public static final String MOCKED_PRODUCT_SERVICE_URL = "mockedProductServiceUrl";
    @InjectMocks
    private ShoppingCartServiceImpl cartServiceImpl;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(cartServiceImpl, "productServiceUrl", MOCKED_PRODUCT_SERVICE_URL);


    }


    @Test
    public void testAddToShoppingBag_Success() throws Exception {
        // Arrange
        String productId = "123";
        int quantity = 2;
        String userId = "user123";


        String jsonResponse = "{\"productId\":\"1\",\"productName\":\"1000\",\"category\":\"HeadPhone\",\"productPrice\":\"2300.00\",\"date\":\"2024-03-30\",\"rating\":\"4.0\",\"stock\":1000}";
        ProductResponse productResponse = getProductResponse();
        // Arrange
        productResponse.setProductId(productId);

        ShoppingBag shoppingBag = new ShoppingBag();
        shoppingBag.setUserId(userId);
        shoppingBag.setTotalPrice(quantity * productResponse.getProductPrice());
        shoppingBag.setCreatedAt(LocalDate.now());
        shoppingBag.setProductId(productId);
        //making call to product service
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode = mapper2.readTree(jsonResponse);
        ResponseEntity<JsonNode> productResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        doReturn(productResponseEntity).when(restTemplate).exchange(eq(MOCKED_PRODUCT_SERVICE_URL), eq(HttpMethod.GET), any(), eq(JsonNode.class), eq(productId));
        when(objectMapper.writeValueAsString(eq(productResponseEntity.getBody()))).thenReturn(jsonResponse);

        when(objectMapper.readValue(any(String.class), any(Class.class)))
                .thenReturn(productResponse);


        // Act
        ResponseEntity<EcommerceGenericResponse> responseEntity = cartServiceImpl.addCartItem(userId, productId, quantity);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    private ProductResponse getProductResponse() {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setCategory("Head Phone");
        productResponse.setProductName("jshdhdh");
        productResponse.setProductPrice(2300.00);
        productResponse.setDate(LocalDate.now());
        productResponse.setStock(1000);
        productResponse.setRating(4.0);
        return productResponse;
    }


    private ProductResponse getProductDtoResponse() {
        ProductResponse response = new ProductResponse();
        response.setProductId("1");
        response.setDate(LocalDate.ofEpochDay(2024 - 03 - 30));
        response.setProductPrice(234.00);
        response.setStock(1000);
        response.setCategory("HeadPhone");
        response.setProductName("1000");
        return response;
    }


}
