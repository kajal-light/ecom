package com.ecommerce.orderservice.service;

import com.ecommerce.dto.*;
import com.ecommerce.entity.ProductOrder;
import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.OutOfStockException;
import com.ecommerce.exception.dto.ErrorDetails;
import com.ecommerce.orderservice.constants.OrderServiceConstants;
import com.ecommerce.orderservice.dao.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private static final String MAKING_A_CALL_TO_PAYMENT_SERVICE = "Making a call to order service";
    private static final String MAKING_A_CALL_TO_PRODUCT_SERVICE = "Making a call to Product service";
    private static final String PAYMENT_SERVICE_RESPONSE = "payment service call completed";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper mapper ;

    @Autowired
    private OrderRepository orderRepository;

    @Value("${product.service.url}")
    private String productService;

    @Value("${payment.service.url}")
    private String paymentService;


    @Override
    @CircuitBreaker(name = "processPayment", fallbackMethod = "processPaymentFallback")
    @Transactional
    public ResponseEntity<EcommerceGenericResponse> placeOrder(OrderServiceRequestDTO orderServiceRequestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        PaymentResponse response = new PaymentResponse();
        List<String> outOfStockProducts = null;
        try {
            List<OrderedProductDTO> products = orderServiceRequestDTO.getProducts();
            List<String> productIds = products.stream().map(OrderedProductDTO::getProductId).toList();
            log.info(MAKING_A_CALL_TO_PRODUCT_SERVICE);
            //making call to product service to fetch stocks for their respective products Id
            List<ProductData> stockList = callProductService(productIds, headers);
            outOfStockProducts = checkProductStockAvailabilityStatus(products, stockList);

            if (outOfStockProducts.isEmpty()) {
                saveOrderRecord(orderServiceRequestDTO, productIds);
                response = callPaymentService(orderServiceRequestDTO, headers);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                throw new OutOfStockException(new ErrorDetails(HttpStatus.CONFLICT, OrderServiceConstants.PRODUCT_OUT_OF_STOCK_MESSAGE, OrderServiceConstants.PRODUCT_OUT_OF_STOCK_CODE, OrderServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
            }

        } catch (JsonProcessingException | NoProductFoundException e) {
            log.error(e.getMessage());
            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, OrderServiceConstants.INVALID_PRODUCT_REQUEST_MESSAGE, OrderServiceConstants.INVALID_PRODUCT_REQUEST_CODE, OrderServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
            String ed = e.getResponseBodyAsString();
            ErrorDetails errorDetails = null;
            try {
                errorDetails = mapper.readValue(ed, ErrorDetails.class);
            } catch (JsonProcessingException jsonProcessingException) {
                log.error(jsonProcessingException.getMessage());
                throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, OrderServiceConstants.INVALID_PRODUCT_REQUEST_MESSAGE, OrderServiceConstants.INVALID_PRODUCT_REQUEST_CODE, OrderServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));

            }
            return new ResponseEntity<>(errorDetails, HttpStatus.PRECONDITION_FAILED);
        } catch (OutOfStockException e) {
            log.error(e.getMessage());
            throw new OutOfStockException(new ErrorDetails(HttpStatus.BAD_REQUEST, OrderServiceConstants.PRODUCT_OUT_OF_STOCK_MESSAGE, OrderServiceConstants.PRODUCT_OUT_OF_STOCK_CODE, OrderServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));

        }


    }

    private List<String> checkProductStockAvailabilityStatus(List<OrderedProductDTO> products, List<ProductData> productsInventory) {
        return products.stream()
                .filter(productInventory -> {
                    String productId = productInventory.getProductId();
                    int orderQuantity = productInventory.getProductQuantity();
                    int stock = productsInventory.stream()
                            .filter(stockItem -> stockItem.getProductId().equals(productId))
                            .mapToInt(ProductData::getStock)
                            .findFirst()
                            .orElse(0);
                    return stock == 0 || stock - orderQuantity < 0;
                }).map(OrderedProductDTO::getProductId)
                .collect(Collectors.toList());

    }

    private void saveOrderRecord(OrderServiceRequestDTO orderServiceRequestDTO, List<String> productIds) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(productIds);
        productOrder.setTotalAmount(orderServiceRequestDTO.getTotalAmount());
        productOrder.setUserId(orderServiceRequestDTO.getUserId());
        orderRepository.save(productOrder);
    }

    private List<ProductData> callProductService(List<String> productIds, HttpHeaders headers) {
        try {
            HttpEntity<List<String>> requestProductIds = new HttpEntity<>(productIds, headers);
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(productService, HttpMethod.POST, requestProductIds, JsonNode.class);
            String jsonResponse = mapper.writeValueAsString(responseEntity.getBody());
           //todo: below sentence returning null
            return mapper.readValue(jsonResponse, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new NoProductFoundException(new ErrorDetails(HttpStatus.NO_CONTENT, OrderServiceConstants.INVALID_PRODUCT_REQUEST_CODE, OrderServiceConstants.PRODUCT_SERVICE_INVALID_RESPONSE_FOUND_MESSAGE, OrderServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
        }

    }

    private PaymentResponse callPaymentService(OrderServiceRequestDTO orderServiceRequestDTO, HttpHeaders headers) throws JsonProcessingException {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentMethod(PaymentType.CREDIT_CARD);
        paymentRequest.setOrderAmount(BigDecimal.valueOf(orderServiceRequestDTO.getTotalAmount()));
        paymentRequest.setDateOfOrder(LocalDate.now().atStartOfDay());
        paymentRequest.setUserId(orderServiceRequestDTO.getUserId());
        HttpEntity<PaymentRequest> paymentRequestEntity= new HttpEntity<>(paymentRequest, headers);
        log.info(MAKING_A_CALL_TO_PAYMENT_SERVICE);
        ResponseEntity<JsonNode> paymentJson = restTemplate.exchange(paymentService, HttpMethod.POST, paymentRequestEntity, JsonNode.class);
        log.info(PAYMENT_SERVICE_RESPONSE);
        mapper.registerModule(new JavaTimeModule());
        String jsonPaymentResponse = mapper.writeValueAsString(paymentJson.getBody());
        return mapper.readValue(jsonPaymentResponse, PaymentResponse.class);

    }

    //todo: implement fallback method
    public String processPaymentFallback() {


        return "SERVICE IS DOWN, PLEASE TRY AFTER SOMETIME !!!";
    }


}
