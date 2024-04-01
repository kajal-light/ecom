package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dao.OrderRepository;
import com.ecommerce.orderservice.dto.OrderServiceRequestDTO;
import com.ecommerce.orderservice.dto.OrderServiceResponse;
import com.ecommerce.orderservice.dto.ProductDTO;
import com.ecommerce.orderservice.dto.ProductData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public OrderServiceResponse placeOrder(OrderServiceRequestDTO orderServiceRequestDTO) {
        OrderServiceResponse orderServiceResponse = new OrderServiceResponse();
        if (orderServiceRequestDTO.getUserId().isBlank()) {
// todo: throw exception when userId is blank
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/product-service/fetchStock";
//todo: put all the hardcoded to properties file
        List<ProductDTO> products = orderServiceRequestDTO.getProducts();
        List<String> productIds = products.stream().map(ProductDTO::getProductId).collect(Collectors.toList());

        ObjectMapper mapper=new ObjectMapper();
       // ArrayList<ProductData> productInventoryData = restTemplate.postForObject(url, productIds, ArrayList.class);
        JsonNode stocks=restTemplate.postForObject(url, productIds, JsonNode.class);
        List<ProductData> stockList = mapper.convertValue(stocks, new TypeReference<List<ProductData>>() {});

        List<ProductData> productsInventory = new ArrayList<>(stockList);


        //check availability of each item
        Map<String, List<Integer>> collect = products.stream()
                .collect(Collectors.toMap(
                        ProductDTO::getProductId,
                        productInventory -> {

                            String productId = productInventory.getProductId();
                            int orderQuantity = productInventory.getProductQuantity();
                            int stock = productsInventory.stream()
                                    .filter(stockItem -> stockItem.getProductId().equals(productId))
                                    .map(ProductData::getStock)
                                    .findFirst()
                                    .orElse(0);
                            if (stock == 0 || stock - orderQuantity < 0) {
                                throw new RuntimeException("Item with Product ID: " + productId + " is out of stock");
                            }
                            return Arrays.asList(stock, orderQuantity);
                        }
                ));
        orderServiceResponse.setProductWithStockAndOrderedQuantity(collect);
        return orderServiceResponse;
    }
}
