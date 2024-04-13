package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.*;
import com.ecommerce.shoppingcartservice.constants.ShoppingCartServiceConstants;
import com.ecommerce.shoppingcartservice.dao.ShoppingCartRepository;

import com.ecommerce.entity.ShoppingBag;

import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.dto.ErrorDetails;
import com.ecommersce.productservice.constants.ProductServiceConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Value("${order.service.url}")
    private String orderServiceUrl;
    @Value("${product.service.url}")
    private String productServiceUrl;
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }


    @Override
    public ResponseEntity<EcommerceGenericResponse> addCartItem(String userId, String productId, int quantity) throws JsonProcessingException {
        try {
            log.info("Making a call to Product service");
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(productServiceUrl, HttpMethod.GET, null, JsonNode.class, productId);

            objectMapper.registerModule(new JavaTimeModule());
            String jsonResponse = objectMapper.writeValueAsString(responseEntity.getBody());

            ProductResponse productResponse = objectMapper.readValue(jsonResponse, ProductResponse.class);

            return new ResponseEntity<>(productResponse, HttpStatus.CREATED);

        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
            String ed = e.getResponseBodyAsString();

            ErrorDetails errorDetails = objectMapper.readValue(ed, ErrorDetails.class);
            return new ResponseEntity<>(errorDetails, HttpStatus.PRECONDITION_FAILED);
        }


    }


    @Override
    public String deleteCartItem(String userId, Long cartItemId) {
        Optional<ShoppingBag> itemInBag = shoppingCartRepository.findById(cartItemId);

        itemInBag.ifPresent(shoppingCartRepository::delete);
        log.info("Item remove from cart");
        return "ITEM REMOVE FROM CART";


    }

    @Override
    public ResponseEntity<EcommerceGenericResponse> checkOut(String userId) throws Exception {
        try {
            double totalAmountOfUserOrder = shoppingCartRepository.findAmountByUserId(userId);

            List<ShoppingBag> listItemsInBag = shoppingCartRepository.findByUserId(userId);

            if (!listItemsInBag.isEmpty() && totalAmountOfUserOrder > 0) {
                log.info("Making a call to order service");
                return proceedOrder(userId, listItemsInBag, totalAmountOfUserOrder);
            }
            else {
                throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ShoppingCartServiceConstants.INVALID_USER_ID_CODE, ShoppingCartServiceConstants.INVALID_USER_ID_MESSAGE, ShoppingCartServiceConstants.SERVICE_NAME, ""));

            }
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
            objectMapper.registerModule(new JavaTimeModule());
            ErrorDetails errorDetails = objectMapper.readValue(e.getResponseBodyAsString(), ErrorDetails.class);
            return new ResponseEntity<>(errorDetails, HttpStatus.PRECONDITION_FAILED);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ShoppingCartServiceConstants.INVALID_USER_ID_CODE, ShoppingCartServiceConstants.INVALID_USER_ID_MESSAGE, ProductServiceConstants.SERVICE_NAME, ""));

        }


    }

    private ResponseEntity<EcommerceGenericResponse> proceedOrder(String userId, List<ShoppingBag> listItemsInBag, double totalAmountOfUserOrder) throws JsonProcessingException {

        OrderServiceRequestDTO request = getOrderServiceRequestDTO(userId, listItemsInBag, totalAmountOfUserOrder);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderServiceRequestDTO> requestDTOHttpEntity = new HttpEntity<>(request, headers);
       log.info("Making a call to order service");
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(orderServiceUrl, HttpMethod.POST, requestDTOHttpEntity, new ParameterizedTypeReference<JsonNode>() {
        });


        //JsonNode response = responseEntity.getBody();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonResponse = objectMapper.writeValueAsString(responseEntity.getBody());


        PaymentResponse paymentResponse = objectMapper.readValue(jsonResponse, PaymentResponse.class);
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }


    private static OrderServiceRequestDTO getOrderServiceRequestDTO(String userId, List<ShoppingBag> listItemsInBag, double totalAmountOfUserOrder) {


        OrderServiceRequestDTO request = new OrderServiceRequestDTO();
        List<OrderedProductDTO> orderedProductDTOS = new ArrayList<>();


        for (ShoppingBag itemInBag : listItemsInBag) {
            OrderedProductDTO productDetail = new OrderedProductDTO();
            productDetail.setProductPrice(itemInBag.getProductPrice());
            productDetail.setProductId(itemInBag.getProductId());
            productDetail.setProductQuantity(itemInBag.getQuantity());
            orderedProductDTOS.add(productDetail);
        }

        request.setProducts(orderedProductDTOS);
        if (totalAmountOfUserOrder != 0) {
            request.setTotalAmount(totalAmountOfUserOrder);
        }
        request.setUserId(userId);
        return request;
    }


}
