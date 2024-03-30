package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.shoppingcartservice.dto.CartRepository;
import com.ecommerce.shoppingcartservice.entity.CartItem;
import com.ecommerce.shoppingcartservice.model.OrderServiceRequest;
import com.ecommerce.shoppingcartservice.model.ProductData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;


    @Override
    public void addCartItem(String userId, String productId, int quantity) {
        if (userId.isBlank()) {

            //throw error
        }
        CartItem item = new CartItem();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/product-service/getProduct/productId/{productId}";

        ProductData product = restTemplate.getForObject(url, ProductData.class, productId);

        double totalPrice = product.getPrice() * quantity;

        item.setCreatedAt(LocalDate.now());
        item.setTotalPrice(totalPrice);
        item.setProductPrice(product.getPrice());
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setUserId(userId);
        cartRepository.save(item);

    }

    @Override
    public void deleteCartItem(String userId, Long id) {
        Optional<CartItem> byId = cartRepository.findById(id);

        byId.ifPresent(x -> cartRepository.delete(x));


    }

    @Override
    public void checkOut(String userId) {
        double totalAmount = cartRepository.findAmountByUserId(userId);
        List<CartItem> itemsInBag = cartRepository.findByUserId(userId);


        OrderServiceRequest request = new OrderServiceRequest();
        request.setTotalAmount(totalAmount);
        List<Double> amountOfEachProduct = new ArrayList<>();
        List<String> productId = new ArrayList<>();
        List<Integer> productsQuantity = new ArrayList<>();

        for (CartItem s : itemsInBag) {

            amountOfEachProduct.add(s.getProductPrice());
            productId.add(s.getProductId());
            productsQuantity.add(s.getQuantity());
        }
        request.setProductPrice(amountOfEachProduct);
        request.setProductId(productId);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/order-service/placeOrder";

        restTemplate.postForObject(url, request, String.class);
    }


}
