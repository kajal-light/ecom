package com.ecommerce.orderservice.service;


import com.ecommerce.dto.*;
import com.ecommerce.entity.ProductOrder;
import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.OutOfStockException;
import com.ecommerce.exception.dto.ErrorDetails;
import com.ecommerce.orderservice.dao.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {


    public static final String MOCKED_PRODUCT_SERVICE_URL = "mockedProductServiceUrl";
    public static final String MOCKED_PAYMENT_SERVICE_URL = "mockedPaymentServiceUrl";


    @Mock
    private CircuitBreakerRegistry circuitBreakerRegistry;
    @InjectMocks
    private OrderServiceImpl orderService = new OrderServiceImpl();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(orderService, "productService", MOCKED_PRODUCT_SERVICE_URL);
        ReflectionTestUtils.setField(orderService, "paymentService", MOCKED_PAYMENT_SERVICE_URL);

    }

    @Test
    public void testPlaceOrderCompleted() throws Exception {
        OrderServiceRequestDTO orderServiceRequestDTO = Mockito.mock(OrderServiceRequestDTO.class);
        List<OrderedProductDTO> products = List.of(
                new OrderedProductDTO("1", 10d, 100),
                new OrderedProductDTO("2", 12d, 1)
        );
        List<ProductData> productsResponse = List.of(
                new ProductData("1", 1000),
                new ProductData("2", 10)
        );
        String jsonResponse = "[{\"productId\":\"1\",\"stock\":1000},{\"productId\":\"2\",\"stock\":0}]";

        when(orderServiceRequestDTO.getProducts()).thenReturn(products);
        when(orderServiceRequestDTO.getTotalAmount()).thenReturn(22d);
        when(orderServiceRequestDTO.getUserId()).thenReturn("testUser");
        //product service call
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode = mapper2.readTree(jsonResponse);
        ResponseEntity<JsonNode> productResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        doReturn(productResponseEntity).when(restTemplate).exchange(eq(MOCKED_PRODUCT_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
        when(mapper.writeValueAsString(eq(productResponseEntity.getBody()))).thenReturn(jsonResponse);
        doReturn(productsResponse).when(mapper).readValue(
                eq(jsonResponse),
                any(TypeReference.class));
        when(orderRepository.save(any())).thenReturn(new ProductOrder());
        //payment service call
        String userId = "123456789";
        PaymentType paymentMethod = PaymentType.CREDIT_CARD;
        String orderAmount = "99.99";
        LocalDateTime orderDate = LocalDateTime.now();
        LocalDateTime paymentDate = LocalDateTime.now().plusHours(1);
        PaymentStatus paymentStatus = PaymentStatus.COMPLETED;
        PaymentResponse paymentResponse = new PaymentResponse(userId, paymentMethod, orderAmount, orderDate, paymentDate, paymentStatus);
        String paymentResponseJson = "{\"userId\":\"123456789\",\"paymentMethod\":\"CREDIT_CARD\",\"orderAmount\":\"99.99\",\"orderDate\":\"2024-04-15T10:30:00\",\"paymentDate\":\"2024-04-15T11:00:00\",\"paymentStatus\":\"COMPLETED\"}\n";
        JsonNode paymentJsonNode = mapper2.readTree(paymentResponseJson);
        ResponseEntity<JsonNode> paymentResponseEntity = new ResponseEntity<>(paymentJsonNode, HttpStatus.OK);
        doReturn(paymentResponseEntity).when(restTemplate).exchange(eq(MOCKED_PAYMENT_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
        when(mapper.writeValueAsString(eq(paymentResponseEntity.getBody()))).thenReturn(paymentResponseJson);
        doReturn(paymentResponse).when(mapper).readValue(
                eq(paymentResponseJson), // JSON string
                eq(PaymentResponse.class));
        when(orderRepository.save(any())).thenReturn(new ProductOrder());

        ResponseEntity<EcommerceGenericResponse> actualEcommerceGenericResponseResponseEntity = orderService.placeOrder(orderServiceRequestDTO);

        assertNotNull(actualEcommerceGenericResponseResponseEntity.getBody());
    }


    @Test
    public void testPlaceOrderProductServiceThrowsJsonProcessingException() throws Exception {
        OrderServiceRequestDTO orderServiceRequestDTO = Mockito.mock(OrderServiceRequestDTO.class);
        List<OrderedProductDTO> products = List.of(
                new OrderedProductDTO("1", 10d, 100),
                new OrderedProductDTO("2", 12d, 1)
        );
        List<ProductData> productsResponse = List.of(
                new ProductData("1", 1000),
                new ProductData("2", 10)
        );
        String jsonResponse = "[{\"productId\":\"1\",\"stock\":1000},{\"productId\":\"2\",\"stock\":0}]";

        when(orderServiceRequestDTO.getProducts()).thenReturn(products);
        //product service call
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode = mapper2.readTree(jsonResponse);
        ResponseEntity<JsonNode> productResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        doReturn(productResponseEntity).when(restTemplate).exchange(eq(MOCKED_PRODUCT_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
        when(mapper.writeValueAsString(eq(productResponseEntity.getBody()))).thenThrow(JsonProcessingException.class);
        assertThrows(NoProductFoundException.class, () -> orderService.placeOrder(orderServiceRequestDTO));
    }

    @Test
    public void testPlaceOrderProductServiceThrowsHttpStatusCodeException() throws Exception {
        OrderServiceRequestDTO orderServiceRequestDTO = Mockito.mock(OrderServiceRequestDTO.class);
        List<OrderedProductDTO> products = List.of(
                new OrderedProductDTO("1", 10d, 100),
                new OrderedProductDTO("2", 12d, 1)
        );
        List<ProductData> productsResponse = List.of(
                new ProductData("1", 1000),
                new ProductData("2", 10)
        );
        String jsonResponse = "{\"status\":404,\"code\":\"NOT_FOUND\",\"message\":\"Resource not found\",\"source\":\"Server\",\"timestamp\":\"2024-04-15T12:00:00\"}\n";
        when(orderServiceRequestDTO.getProducts()).thenReturn(products);
        //product service call
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode = mapper2.readTree(jsonResponse);
        ResponseEntity<JsonNode> productResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(restTemplate.exchange(eq(MOCKED_PRODUCT_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class))).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(500), "Internal Servver Error"));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, "404", "Resource not found", "Server", LocalDateTime.now().toString());
        doReturn(errorDetails).when(mapper).readValue(
                eq(""), // JSON string
                eq(ErrorDetails.class));
        ResponseEntity<EcommerceGenericResponse> actualEcommerceGenericResponseResponseEntity = orderService.placeOrder(orderServiceRequestDTO);
        assertNotNull(actualEcommerceGenericResponseResponseEntity.getBody());
    }


    @Test
    public void testPlaceOrderProductServiceThrowsHttpStatusCodeExceptionCatchJsonProcessingException() throws Exception {
        OrderServiceRequestDTO orderServiceRequestDTO = Mockito.mock(OrderServiceRequestDTO.class);
        List<OrderedProductDTO> products = List.of(
                new OrderedProductDTO("1", 10d, 100),
                new OrderedProductDTO("2", 12d, 1)
        );
        List<ProductData> productsResponse = List.of(
                new ProductData("1", 1000),
                new ProductData("2", 10)
        );
        String jsonResponse = "{\"status\":404,\"code\":\"NOT_FOUND\",\"message\":\"Resource not found\",\"source\":\"Server\",\"timestamp\":\"2024-04-15T12:00:00\"}\n";
        when(orderServiceRequestDTO.getProducts()).thenReturn(products);
        //product service call
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode = mapper2.readTree(jsonResponse);
        ResponseEntity<JsonNode> productResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        when(restTemplate.exchange(eq(MOCKED_PRODUCT_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class))).thenThrow(new HttpClientErrorException(HttpStatusCode.valueOf(500), "Internal Servver Error"));
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, "404", "Resource not found", "Server", LocalDateTime.now().toString());
        when(mapper.readValue(eq(""), eq(ErrorDetails.class))).thenThrow(JsonProcessingException.class);


        assertThrows(NoProductFoundException.class, () -> orderService.placeOrder(orderServiceRequestDTO));
    }

    @Test
    public void testPlaceOrderProductOutOfStock() throws Exception {
        OrderServiceRequestDTO orderServiceRequestDTO = Mockito.mock(OrderServiceRequestDTO.class);
        List<OrderedProductDTO> products = List.of(
                new OrderedProductDTO("1", 10d, 2100),
                new OrderedProductDTO("2", 12d, 21)
        );
        List<ProductData> productsResponse = List.of(
                new ProductData("1", 1000),
                new ProductData("2", 10)
        );
        String jsonResponse = "[{\"productId\":\"1\",\"stock\":1000},{\"productId\":\"2\",\"stock\":0}]";
        when(orderServiceRequestDTO.getProducts()).thenReturn(products);
        //product service call
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode = mapper2.readTree(jsonResponse);
        ResponseEntity<JsonNode> productResponseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
        doReturn(productResponseEntity).when(restTemplate).exchange(eq(MOCKED_PRODUCT_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class));
        when(mapper.writeValueAsString(eq(productResponseEntity.getBody()))).thenReturn(jsonResponse);
        doReturn(productsResponse).when(mapper).readValue(eq(jsonResponse), any(TypeReference.class));

        assertThrows(OutOfStockException.class, () -> orderService.placeOrder(orderServiceRequestDTO));
    }


    @Test
    void testProcessPaymentFallback() {
        OrderServiceRequestDTO orderServiceRequestDTO = Mockito.mock(OrderServiceRequestDTO.class);

        Throwable throwable = new RuntimeException("Fallback error message");

        // Call the fallback method
        ResponseEntity<EcommerceGenericResponse> responseEntity = orderService.placeOrderFallback(orderServiceRequestDTO, throwable);

        // Verify the response
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());

    }
}
