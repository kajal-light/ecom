package com.ecommerce.orderservice.service;

import com.ecommerce.dto.OrderServiceRequestDTO;
import com.ecommerce.dto.OrderServiceResponse;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.ProductData;
import com.ecommerce.orderservice.dao.OrderRepository;
import com.exception.OutOfStockException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @CircuitBreaker(name = "processPayment", fallbackMethod = "processPaymentFallback")
    public OrderServiceResponse placeOrder(OrderServiceRequestDTO orderServiceRequestDTO) {
        OrderServiceResponse orderServiceResponse = new OrderServiceResponse();
        if (orderServiceRequestDTO.getUserId().isBlank()) {
            throw new RuntimeException("UserId can not be null");
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/product-service/fetchStock";
//todo: put all the hardcoded to properties file
        List<ProductDTO> products = orderServiceRequestDTO.getProducts();
        List<String> productIds = products.stream().map(ProductDTO::getProductId).collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();

        JsonNode stocks = restTemplate.postForObject(url, productIds, JsonNode.class);
        List<ProductData> stockList = mapper.convertValue(stocks, new TypeReference<List<ProductData>>() {
        });

        List<ProductData> productsInventory = new ArrayList<>(stockList);
        Boolean isProductStockAvailable = isProductStockAvailable(products, productsInventory);
        if (isProductStockAvailable) {
            Map<String, List<Integer>> collect = products.stream()
                    .filter(productInventory -> {
                        String productId = productInventory.getProductId();
                        int orderQuantity = productInventory.getProductQuantity();
                        int stock = productsInventory.stream()
                                .filter(stockItem -> stockItem.getProductId().equals(productId))
                                .mapToInt(ProductData::getStock)
                                .findFirst()
                                .orElse(0);
                        return stock > 0 && stock >= orderQuantity;
                    })
                    .collect(Collectors.toMap(
                            ProductDTO::getProductId,
                            productInventory -> {
                                String productId = productInventory.getProductId();
                                int orderQuantity = productInventory.getProductQuantity();
                                int stock = productsInventory.stream()
                                        .filter(stockItem -> stockItem.getProductId().equals(productId))
                                        .mapToInt(ProductData::getStock)
                                        .findFirst()
                                        .orElse(0);
                                return Arrays.asList(stock, orderQuantity);
                            }
                    ));

            orderServiceResponse.setProductWithStockAndOrderedQuantity(collect);
            return orderServiceResponse;
        } else {
            throw new OutOfStockException("TF_002", "Some products are out of stock");
        }


    }

    public String processPaymentFallback(Exception e) {


        return "PAYMENT SERVICE IS DOWN, PLEASE TRY AFTER SOMETIME !!!";
    }


    private Boolean isProductStockAvailable(List<ProductDTO> products, List<ProductData> productsInventory) {


        return products.stream()
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

    }
}
