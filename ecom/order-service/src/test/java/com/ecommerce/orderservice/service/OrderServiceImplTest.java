package com.ecommerce.orderservice.service;


import com.ecommerce.dto.EcommerceGenericResponse;
import com.ecommerce.dto.OrderServiceRequestDTO;
import com.ecommerce.dto.OrderedProductDTO;
import com.ecommerce.dto.ProductData;
import com.ecommerce.orderservice.dao.OrderRepository;
import com.ecommerce.orderservice.service.OrderServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class OrderServiceImplTest {


    public static final String MOCKED_PRODUCT_SERVICE_URL = "mockedProductServiceUrl";
    /**
     * Method: placeOrder(OrderServiceRequestDTO orderServiceRequestDTO)
     */


    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ObjectMapper mapper;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
/*        restTemplate = Mockito.mock(RestTemplate.class);
        mapper = Mockito.mock(ObjectMapper.class);*/

        // Use reflection to set the objectMapperMock to the orderService's mapper field
        Field mapperField = OrderServiceImpl.class.getDeclaredField("mapper");
        mapperField.setAccessible(true);
        mapperField.set(orderService, mapper);
        ReflectionTestUtils.setField(orderService, "productService", MOCKED_PRODUCT_SERVICE_URL);
        ReflectionTestUtils.setField(orderService, "paymentService", "mockedPaymentServiceUrl");

    }

    @Test
    public void testPlaceOrder() throws Exception {
        OrderServiceRequestDTO orderServiceRequestDTO = Mockito.mock(OrderServiceRequestDTO.class);
        List<OrderedProductDTO> products = List.of(
                new OrderedProductDTO("1", 10d, 100),
                new OrderedProductDTO("2", 12d, 1)
        );
        List<ProductData> productsResponse = List.of(
                new ProductData("1", 1000),
                new ProductData("2", 0)
        );
        String jsonResponse = "[{\"productId\":\"1\",\"stock\":1000},{\"productId\":\"2\",\"stock\":0}]";

        when(orderServiceRequestDTO.getProducts()).thenReturn(products);

        ResponseEntity<JsonNode> productResponse = new ResponseEntity<>(mapper.readTree(jsonResponse), HttpStatus.OK);
        when(restTemplate.exchange(eq(MOCKED_PRODUCT_SERVICE_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(JsonNode.class))).thenReturn(productResponse);

        when(mapper.writeValueAsString(any())).thenReturn(jsonResponse);
        Mockito.when(mapper.readValue(
                anyString(), // JSON string
                ArgumentMatchers.eq(new TypeReference<>() {
                                    } // Type reference
                ))).thenReturn(productsResponse);
        ResponseEntity<EcommerceGenericResponse> actualEcommerceGenericResponseResponseEntity = orderService.placeOrder(orderServiceRequestDTO);

        assertNotNull(actualEcommerceGenericResponseResponseEntity.getBody());
    }


}
