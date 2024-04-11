package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.*;
import com.ecommerce.shoppingcartservice.constants.ShoppingCartServiceConstants;
import com.ecommerce.shoppingcartservice.dao.ShoppingCartRepository;

import com.ecommerce.entity.ShoppingBag;

import com.ecommersce.productservice.constants.ProductServiceConstants;
import com.exception.EmptyInputException;
import com.exception.NoProductFoundException;
import com.exception.model.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    @Value("${product.service.url}")
    private String productServiceUrl;
    RestTemplate restTemplate = new RestTemplate();
    private final ShoppingCartRepository shoppingCartRepository;
    @Autowired
    public CartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }


    @Override
    public String addCartItem(String userId, String productId, int quantity) {

        ShoppingBag item = new ShoppingBag();

            ProductDTO product = restTemplate.getForObject(productServiceUrl, ProductDTO.class, productId);
            if(product != null) {
                double totalPrice = product.getProductPrice() * quantity;

                item.setCreatedAt(LocalDate.now());
                item.setTotalPrice(totalPrice);
                item.setProductPrice(product.getProductPrice());
                item.setProductId(productId);
                item.setQuantity(quantity);
                item.setUserId(userId);
                shoppingCartRepository.save(item);
                return "Product added in a cart successfully";
            }else{

                throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_ID_CODE, ProductServiceConstants.INVALID_PRODUCT_ID_MESSAGE,ProductServiceConstants.SERVICE_NAME));
            }

        }





    @Override
    public String deleteCartItem(String userId, Long cartItemId) {
        Optional<ShoppingBag> itemInBag = shoppingCartRepository.findById(cartItemId);

        itemInBag.ifPresent(shoppingCartRepository::delete);
           return "ITEM REMOVE FROM CART";

    }

    @Override
    public PaymentResponse checkOut(String userId) throws Exception {

        if(userId==null){

               //
            }
            double totalAmountOfUserOrder = shoppingCartRepository.findAmountByUserId(userId);

            List<ShoppingBag> listItemsInBag = shoppingCartRepository.findByUserId(userId);
            if(!listItemsInBag.isEmpty() && totalAmountOfUserOrder>0) {
                OrderServiceRequestDTO request = getOrderServiceRequestDTO(userId, listItemsInBag, totalAmountOfUserOrder);

                PaymentResponse paymentResponse;

                paymentResponse = restTemplate.postForObject(orderServiceUrl, request, PaymentResponse.class);

                return paymentResponse;
        }else{

                throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ShoppingCartServiceConstants.INVALID_PRODUCT_NAME_CODE, ShoppingCartServiceConstants.INVALID_PRODUCT_NAME_MESSAGE,"Shopping-Cart Service"));
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
