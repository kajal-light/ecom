package com.ecommerce.orderservice.service;

import com.ecommerce.dto.*;
import com.ecommerce.entity.ProductOrder;
import com.ecommerce.exception.OutOfStockException;
import com.ecommerce.orderservice.constants.OrderServiceConstants;
import com.ecommerce.orderservice.dao.OrderRepository;


import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.dto.ErrorDetails;
import com.ecommerce.shoppingcartservice.constants.ShoppingCartServiceConstants;
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
import java.util.*;

import java.util.stream.Collectors;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${product.service.url}")
    private String productService;
    @Value("${payment.service.url}")
    private String paymentService;


    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();


    @Override
    @CircuitBreaker(name = "processPayment", fallbackMethod = "processPaymentFallback")
    @Transactional
    public ResponseEntity<EcommerceGenericResponse> placeOrder(OrderServiceRequestDTO orderServiceRequestDTO) throws JsonProcessingException {

        try {

            PaymentResponse response = null;
            List<OrderedProductDTO> products = orderServiceRequestDTO.getProducts();
            List<String> productIds = products.stream().map(OrderedProductDTO::getProductId).collect(Collectors.toList());
            boolean productOutOfStock;
            //making call to product service to fetch stocks for their respective products Id
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<String>> requestProductIds = new HttpEntity<>(productIds, headers);
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(productService, HttpMethod.POST, requestProductIds, JsonNode.class);

            String jsonResponse = mapper.writeValueAsString(responseEntity.getBody());

            List<ProductData> stockList = mapper.readValue(jsonResponse, new TypeReference<List<ProductData>>() {});
            List<ProductData> productsInventory = new ArrayList<>(stockList);


            productOutOfStock = isProductOutOfStock(products, productsInventory);


            if (productOutOfStock){

                ProductOrder productOrder = new ProductOrder();

                productOrder.setProductId(productIds);
                productOrder.setTotalAmount(orderServiceRequestDTO.getTotalAmount());
                productOrder.setUserId(orderServiceRequestDTO.getUserId());
                orderRepository.save(productOrder);

                PaymentRequest paymentRequest = new PaymentRequest();
                paymentRequest.setPaymentMethod(PaymentType.CREDIT_CARD);
                paymentRequest.setOrderAmount(BigDecimal.valueOf(orderServiceRequestDTO.getTotalAmount()));
                paymentRequest.setDateOfOrder(LocalDate.now().atStartOfDay());
                paymentRequest.setUserId(orderServiceRequestDTO.getUserId());
                log.info("Making a call to payment service ");
                 callPaymentService(paymentRequest, headers, response);
                 log.info("payment service call completed");
            }else{
                throw new OutOfStockException(new ErrorDetails(HttpStatus.CONFLICT, OrderServiceConstants.PRODUCT_OUT_OF_STOCK_MESSAGE , OrderServiceConstants.PRODUCT_OUT_OF_STOCK_CODE,OrderServiceConstants.SERVICE_NAME,""));
            }
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
            String ed = e.getResponseBodyAsString();

            ErrorDetails errorDetails = mapper.readValue(ed, ErrorDetails.class);
            return new ResponseEntity<>(errorDetails, HttpStatus.PRECONDITION_FAILED);
        }catch (NoProductFoundException e){
            log.error(e.getMessage());
            throw new NoProductFoundException(
                    new ErrorDetails(HttpStatus.BAD_REQUEST, OrderServiceConstants.INVALID_PRODUCT_REQUEST_MESSAGE , OrderServiceConstants.INVALID_PRODUCT_REQUEST_CODE,OrderServiceConstants.SERVICE_NAME,""));

        }catch (OutOfStockException e){
            log.error(e.getMessage());
            throw new OutOfStockException(new ErrorDetails(HttpStatus.BAD_REQUEST, OrderServiceConstants.PRODUCT_OUT_OF_STOCK_MESSAGE , OrderServiceConstants.PRODUCT_OUT_OF_STOCK_CODE,OrderServiceConstants.SERVICE_NAME,""));

        }


    }



    private void callPaymentService(PaymentRequest paymentRequest, HttpHeaders headers, PaymentResponse productResponse) throws JsonProcessingException {
        HttpEntity<PaymentRequest> paymentRequestEntity;
        paymentRequestEntity = new HttpEntity<>(paymentRequest, headers);
        ResponseEntity<JsonNode> paymentJson = restTemplate.exchange(paymentService, HttpMethod.POST, paymentRequestEntity, JsonNode.class);


        mapper.registerModule(new JavaTimeModule());
        String jsonPyamentResponse;
        jsonPyamentResponse = mapper.writeValueAsString(paymentJson.getBody());


         productResponse = mapper.readValue(jsonPyamentResponse, PaymentResponse.class);


    }
    private static boolean isProductOutOfStock(List<OrderedProductDTO> products, List<ProductData> productsInventory) {
        boolean productOutOfStock;
        //
        productOutOfStock = products.stream()
                .noneMatch(productInventory -> {
                    String productId = productInventory.getProductId();
                    int orderQuantity = productInventory.getProductQuantity();
                    int stock = productsInventory.stream()
                            .filter(stockItem -> stockItem.getProductId().equals(productId))
                            .mapToInt(ProductData::getStock)
                            .findFirst()
                            .orElse(0);
                    return stock == 0 || stock - orderQuantity < 0;
                });

        return productOutOfStock;
    }


    public String processPaymentFallback(Exception e) {


        return "SERVICE IS DOWN, PLEASE TRY AFTER SOMETIME !!!";
    }


}
