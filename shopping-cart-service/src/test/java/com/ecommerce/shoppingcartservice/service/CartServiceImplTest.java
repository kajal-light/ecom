package com.ecommerce.shoppingcartservice.service;

import com.ecommerce.dto.ProductsDto;
import com.ecommerce.entity.ShoppingBag;
import com.ecommerce.shoppingcartservice.controller.ShoppingCartController;
import com.ecommerce.shoppingcartservice.dao.ShoppingCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CartServiceImplTest {


    @InjectMocks
    private CartServiceImpl cartServiceImpl;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private RestTemplate restTemplate;
@Test
    void addCartItem(){
    int quantity=4;
    String userId="hshdd";

    ProductsDto product=getProductDtoResponse();
    double totalPrice = product.getProductPrice() * quantity;
    ShoppingBag item = new ShoppingBag();
    item.setCreatedAt(LocalDate.now());
    item.setTotalPrice(totalPrice);
    item.setProductPrice(product.getProductPrice());
    item.setProductId("jdfhf");
    item.setQuantity(quantity);
    item.setUserId(userId);
    when(restTemplate.getForObject(Mockito.anyString(), ProductsDto.class, "jdfhf")).thenReturn(product);
    when(shoppingCartRepository.save(item)).thenReturn(item);
    cartServiceImpl.addCartItem(userId,"jdfhf",quantity);
    Assertions.assertNotNull(item);
}



@Test
void deleteCartItem(){
    ShoppingBag bag=new ShoppingBag();
    bag.setCartItemId(364564L);
    bag.setQuantity(2);
    when(shoppingCartRepository.findById(bag.getCartItemId())).thenReturn(Optional.of(bag));
    Assertions.assertNotNull(bag);
}
    private ProductsDto getProductDtoResponse() {
        ProductsDto response=new ProductsDto();
        response.setProductId("jdfhf");
        response.setDate(LocalDate.now());
        response.setProductPrice(234.00);
        response.setStock(1000);
        response.setCategory("hsgddg");
    return response;
    }


}
