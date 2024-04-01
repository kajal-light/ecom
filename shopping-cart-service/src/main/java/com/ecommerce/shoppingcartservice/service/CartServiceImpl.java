package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.shoppingcartservice.dao.ShoppingCartRepository;
import com.ecommerce.shoppingcartservice.entity.ShoppingBag;
import com.ecommerce.shoppingcartservice.dto.OrderServiceRequest;
import com.ecommerce.shoppingcartservice.dto.ProductData;
import com.ecommerce.shoppingcartservice.dto.ProductInformation;
import org.ecommerce.dto.OrderServiceRequestDTO;
import org.ecommerce.dto.ProductDTO;
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
    private ShoppingCartRepository shoppingCartRepository;


    @Override
    public void addCartItem(String userId, String productId, int quantity) {
        if (userId.isBlank()) {

            //throw error
        }
        ShoppingBag item = new ShoppingBag();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/product-service/retrieveProductById/productId/{productId}";

        ProductData product = restTemplate.getForObject(url, ProductData.class, productId);

        double totalPrice = product.getProductPrice() * quantity;

        item.setCreatedAt(LocalDate.now());
        item.setTotalPrice(totalPrice);
        item.setProductPrice(product.getProductPrice());
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setUserId(userId);
        shoppingCartRepository.save(item);

    }

    @Override
    public void deleteCartItem(String userId, Long cartItemId) {
        Optional<ShoppingBag> itemInBag = shoppingCartRepository.findById(cartItemId);

        itemInBag.ifPresent(x -> shoppingCartRepository.delete(x));


    }

    @Override
    public void checkOut(String userId) {
        double totalAmountOfUserOrder = shoppingCartRepository.findAmountByUserId(userId);
        List<ShoppingBag> ListItemsInBag = shoppingCartRepository.findByUserId(userId);
        OrderServiceRequestDTO request = new OrderServiceRequestDTO();
        List<ProductDTO> productDTOs = new ArrayList<>();

        for (ShoppingBag itemInBag : ListItemsInBag) {
            ProductDTO productDetail=new ProductDTO();
            productDetail.setProductPrice(itemInBag.getProductPrice());
            productDetail.setProductId(itemInBag.getProductId());
            productDetail.setProductQuantity(itemInBag.getQuantity());
            productDTOs.add(productDetail);
        }

        request.setProducts(productDTOs);
        request.setTotalAmount(totalAmountOfUserOrder);
        request.setUserId(userId);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9190/order-service/placeOrder";

        restTemplate.postForObject(url, request, String.class);
    }


}
