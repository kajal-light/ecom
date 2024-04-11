package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.*;
import com.ecommerce.shoppingcartservice.dao.ShoppingCartRepository;

import com.ecommerce.entity.ShoppingBag;

import com.exception.EmptyInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {
    @Value("${order.service.url}")
    private String orderServiceUrl;

    private final ShoppingCartRepository shoppingCartRepository;
    @Autowired
    public CartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }


    @Override
    public String addCartItem(String userId, String productId, int quantity) {

        ShoppingBag item = new ShoppingBag();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/product-service/retrieveProductById/productId/{productId}";

        try {
            ProductDTO product = restTemplate.getForObject(url, ProductDTO.class, productId);
            assert product != null;
            double totalPrice = product.getProductPrice() * quantity;

            item.setCreatedAt(LocalDate.now());
            item.setTotalPrice(totalPrice);
            item.setProductPrice(product.getProductPrice());
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setUserId(userId);
            shoppingCartRepository.save(item);
            return "Product added in a cart successfully";
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public String deleteCartItem(String userId, Long cartItemId) {
        Optional<ShoppingBag> itemInBag = shoppingCartRepository.findById(cartItemId);

        itemInBag.ifPresent(shoppingCartRepository::delete);
           return "item remove from cart";

    }

    @Override
    public PaymentResponse checkOut(String userId) throws Exception {

        try {
            if(userId==null){

                throw new EmptyInputException("SH_03","userId should not be null", errorDetails);
            }
            double totalAmountOfUserOrder = shoppingCartRepository.findAmountByUserId(userId);

            List<ShoppingBag> listItemsInBag = shoppingCartRepository.findByUserId(userId);
            OrderServiceRequestDTO request = getOrderServiceRequestDTO(userId, listItemsInBag, totalAmountOfUserOrder);

            RestTemplate restTemplate = new RestTemplate();

            new PaymentResponse();
            PaymentResponse paymentResponse;
            try {
                paymentResponse = restTemplate.postForObject(orderServiceUrl, request, PaymentResponse.class);
            } catch (Exception e){
                throw new Exception(e.getMessage());
            }

            return paymentResponse;
        }
       catch (Exception e){

            throw new RuntimeException(e.getMessage());
        }
    }

    private static OrderServiceRequestDTO getOrderServiceRequestDTO(String userId, List<ShoppingBag> listItemsInBag, double totalAmountOfUserOrder) {


        OrderServiceRequestDTO request = new OrderServiceRequestDTO();
        List<OrderedProductDTO> orderedProductDTOS = new ArrayList<>();

if(!listItemsInBag.isEmpty()) {
    for (ShoppingBag itemInBag : listItemsInBag) {
        OrderedProductDTO productDetail = new OrderedProductDTO();
        productDetail.setProductPrice(itemInBag.getProductPrice());
        productDetail.setProductId(itemInBag.getProductId());
        productDetail.setProductQuantity(itemInBag.getQuantity());
        orderedProductDTOS.add(productDetail);
    }
}else{

    throw new NoSuchElementException("No data present for given UserId");
}
        request.setProducts(orderedProductDTOS);
        if(totalAmountOfUserOrder!=0) {
            request.setTotalAmount(totalAmountOfUserOrder);
        }
        request.setUserId(userId);
        return request;
    }


}
