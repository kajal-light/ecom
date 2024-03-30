package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderRepository;
import com.ecommerce.orderservice.model.OrderServiceRequestDTO;
import com.ecommerce.orderservice.model.OrderServiceResponse;
import com.ecommerce.orderservice.model.ProductDTO;
import com.ecommerce.orderservice.model.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        List<String> productIds = products.stream().map(product -> product.getProductId()).collect(Collectors.toList());
        restTemplate.put(url, products);

        List<ProductData> productInventoryData = restTemplate.postForObject(url, productIds, List.class);
//check availability of each item
        Map<String, List<Integer>> collect = products.stream()
                .collect(Collectors.toMap(
                        ProductDTO::getProductId,
                        productInventory -> {

                            String productId = productInventory.getProductId();
                            int orderQuantity = productInventory.getProductQuantity();
                            int stock = productInventoryData.stream()
                                    .filter(stockItem -> stockItem.getProductId().equals(productId))
                                    .map(product -> product.getStock())
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
