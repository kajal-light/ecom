package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.*;

import com.ecommerce.entity.ShoppingBag;
import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.dto.ErrorDetails;
import com.ecommerce.shoppingcartservice.dao.ShoppingCartRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;


import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


import com.fasterxml.jackson.core.JsonProcessingException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    public static final String MOCKED_PRODUCT_SERVICE_URL = "mockedProductServiceUrl";
    public static final String MOCKED_ORDER_SERVICE_URL = "mockedPaymentServiceUrl";
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
        ReflectionTestUtils.setField(cartServiceImpl, "orderServiceUrl", MOCKED_ORDER_SERVICE_URL);

    }

    @Test
     void testAddToShoppingBag_Success() throws Exception {

        String productId = "123";
        var quantity = 2;
        String userId = "user123";

        String jsonResponse = "{\"productId\":\"1\",\"productName\":\"1000\",\"category\":\"HeadPhone\",\"productPrice\":\"2300.00\",\"date\":\"2024-03-30\",\"rating\":\"4.0\",\"stock\":1000}";
        ProductResponse productResponse = getProductResponse();
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


        ResponseEntity<EcommerceGenericResponse> responseEntity = cartServiceImpl.addCartItem(userId, productId, quantity);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
     void testAddToShoppingBagThrowsHttpStatusCodeException() throws Exception {

        String productId = "123";
        int quantity = 2;
        String userId = "user123";

        ProductResponse productResponse = getProductResponse();

        productResponse.setProductId(productId);

        ShoppingBag shoppingBag = new ShoppingBag();
        shoppingBag.setUserId(userId);
        shoppingBag.setTotalPrice(quantity * productResponse.getProductPrice());
        shoppingBag.setCreatedAt(LocalDate.now());
        shoppingBag.setProductId(productId);
        //making call to product service
        ObjectMapper mapper2 = new ObjectMapper();

        when(restTemplate.exchange(eq(MOCKED_PRODUCT_SERVICE_URL), eq(HttpMethod.GET), any(), eq(JsonNode.class), eq(productId))).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(500), "Internal Servver Error"));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, "404", "Resource not found", "Server", LocalDateTime.now().toString());
        doReturn(errorDetails).when(objectMapper).readValue(
                eq(""), // JSON string
                eq(ErrorDetails.class));

        ResponseEntity<EcommerceGenericResponse> responseEntity = cartServiceImpl.addCartItem(userId, productId, quantity);
        assertNotNull(responseEntity.getBody());
    }

    @Test
     void testAddToShoppingBag_ThrowsJsonProcessingException() throws Exception {
        // Arrange
        String productId = "123";
        int quantity = 2;
        String userId = "user123";


        String jsonResponse = "{\"productId\":\"1\",\"productName\":\"1000\",\"category\":\"HeadPhone\",\"productPrice\":\"2300.00\",\"date\":\"2024-03-30\",\"rating\":\"4.0\",\"stock\":1000}";
        ProductResponse productResponse = getProductResponse();
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
        when(objectMapper.writeValueAsString(eq(productResponseEntity.getBody()))).thenThrow(JsonProcessingException.class);


        assertThrows(NoProductFoundException.class, () -> cartServiceImpl.addCartItem(userId, productId, quantity));

    }

    @Test
    void testPlaceOrderSuccess() throws Exception {
        String userId = "1234";
        double totalAmountOfUserOrder = 2300.00;
        List<ShoppingBag> listOfBag = getShoppingBags(userId);

        getOrderServiceRequest(listOfBag, totalAmountOfUserOrder, userId);


        String jsonResponse = "{\"userId\":\"1234\",\"paymentMethod\":\"COD\",\"orderAmount\":\"2300.00\",\"orderDate\":\"2024-03-30\",\"paymentDate\":\"2024-03-30\",\"paymentStatus\":\"COMPLETED\"}";
        PaymentResponse mockPaymentResponse = getPaymentResponse();
        mockPaymentResponse.setUserId(userId);
        when(shoppingCartRepository.findAmountByUserId(anyString())).thenReturn(2300.00);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(listOfBag);
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode = mapper2.readTree(jsonResponse);
        ResponseEntity<JsonNode> productResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        doReturn(productResponseEntity).when(restTemplate).exchange(eq(MOCKED_ORDER_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(new ParameterizedTypeReference<JsonNode>() {
        }));
        when(objectMapper.writeValueAsString(eq(productResponseEntity.getBody()))).thenReturn(jsonResponse);

        when(objectMapper.readValue(any(String.class), any(Class.class)))
                .thenReturn(mockPaymentResponse);


        ResponseEntity<EcommerceGenericResponse> ecommerceGenericResponse = cartServiceImpl.checkOut(userId);
        assertNotNull(ecommerceGenericResponse.getBody());
    }

    @Test
    void testPlaceOrderNoProductFoundException() throws Exception {
        String userId = "1234";
        double totalAmountOfUserOrder = 2300.00;
        List<ShoppingBag> listOfBag = getShoppingBags(userId);

        getOrderServiceRequest(listOfBag, totalAmountOfUserOrder, userId);


        String jsonResponse = "{\"userId\":\"1234\",\"paymentMethod\":\"COD\",\"orderAmount\":\"2300.00\",\"orderDate\":\"2024-03-30\",\"paymentDate\":\"2024-03-30\",\"paymentStatus\":\"COMPLETED\"}";
        PaymentResponse mockPaymentResponse = getPaymentResponse();
        mockPaymentResponse.setUserId(userId);
        when(shoppingCartRepository.findAmountByUserId(anyString())).thenReturn(2300.00);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(listOfBag);
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode = mapper2.readTree(jsonResponse);
        ResponseEntity<JsonNode> productResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        doReturn(productResponseEntity).when(restTemplate).exchange(eq(MOCKED_ORDER_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(new ParameterizedTypeReference<JsonNode>() {
        }));


        when(objectMapper.writeValueAsString(eq(productResponseEntity.getBody()))).thenThrow(JsonProcessingException.class);


        assertThrows(NoProductFoundException.class, () -> cartServiceImpl.checkOut(userId));

    }

    @Test
    void testPlaceOrderHttpStatusCodeException() throws Exception {
        String userId = "1234";
        double totalAmountOfUserOrder = 2300.00;
        List<ShoppingBag> listOfBag = getShoppingBags(userId);

        getOrderServiceRequest(listOfBag, totalAmountOfUserOrder, userId);

        String jsonResponse = "{\"status\":404,\"code\":\"NOT_FOUND\",\"message\":\"Resource not found\",\"source\":\"Server\",\"timestamp\":\"2024-04-15T12:00:00\"}\n";

        PaymentResponse mockPaymentResponse = getPaymentResponse();
        mockPaymentResponse.setUserId(userId);
        when(shoppingCartRepository.findAmountByUserId(anyString())).thenReturn(2300.00);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(listOfBag);
        when(restTemplate.exchange(eq(MOCKED_ORDER_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(new ParameterizedTypeReference<JsonNode>() {
        }))).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(500), "Internal Servver Error"));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, "404", "Resource not found", "Server", LocalDateTime.now().toString());
        doReturn(errorDetails).when(objectMapper).readValue(
                eq(""), // JSON string
                eq(ErrorDetails.class));

        ResponseEntity<EcommerceGenericResponse> ecommerceGenericResponse = cartServiceImpl.checkOut(userId);
        assertNotNull(ecommerceGenericResponse.getBody());
    }

    private static void getOrderServiceRequest(List<ShoppingBag> listOfBag, double totalAmountOfUserOrder, String userId) {
        OrderServiceRequestDTO request = new OrderServiceRequestDTO();
        List<OrderedProductDTO> orderedProductDTOS = new ArrayList<>();
        for (ShoppingBag itemInBag : listOfBag) {
            OrderedProductDTO productDetail = new OrderedProductDTO();
            productDetail.setProductPrice(itemInBag.getProductPrice());
            productDetail.setProductId(itemInBag.getProductId());
            productDetail.setProductQuantity(itemInBag.getQuantity());
            orderedProductDTOS.add(productDetail);
        }
        request.setProducts(orderedProductDTOS);

        request.setTotalAmount(totalAmountOfUserOrder);
        request.setUserId(userId);
    }

    private static List<ShoppingBag> getShoppingBags(String userId) {
        List<ShoppingBag> listOfBag = new ArrayList<>();
        ShoppingBag bagItem = new ShoppingBag();
        bagItem.setCartItemId(1L);
        bagItem.setQuantity(4);
        bagItem.setProductId("1");
        bagItem.setUserId(userId);
        bagItem.setTotalPrice(2300.00);
        bagItem.setProductPrice(2300.0);
        bagItem.setCreatedAt(LocalDate.now());
        listOfBag.add(bagItem);
        return listOfBag;
    }

    private PaymentResponse getPaymentResponse() {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentStatus(PaymentStatus.COMPLETED);
        response.setPaymentMethod(PaymentType.COD);

        response.setOrderAmount(String.valueOf(2300.00));
        response.setOrderDate(LocalDateTime.now());
        response.setPaymentDate(LocalDateTime.now());

        return response;
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


}
