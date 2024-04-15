package test.com.ecommerce.orderservice.service;


import com.ecommerce.dto.OrderServiceRequestDTO;
import com.ecommerce.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class OrderServiceImplTest {


    /**
     * Method: placeOrder(OrderServiceRequestDTO orderServiceRequestDTO)
     */


    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    public void testPlaceOrder() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: processPaymentFallback()
     */
    @Test
    public void testProcessPaymentFallback() throws Exception {
//TODO: Test goes here... 
    }


    /**
     * Method: isProductOutOfStock(List<OrderedProductDTO> products, List<ProductData> productsInventory)
     */
    @Test
    public void testIsProductOutOfStock() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = OrderServiceImpl.getClass().getMethod("isProductOutOfStock", List<OrderedProductDTO>.class, List<ProductData>.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: saveOrderRecord(OrderServiceRequestDTO orderServiceRequestDTO, List<String> productIds)
     */
    @Test
    public void testSaveOrderRecord() throws Exception {
//TODO: Test goes here... 

try { 
   Method method = OrderServiceImpl.getClass().getMethod("saveOrderRecord", OrderServiceRequestDTO.class, List<String>.class);
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) {
} 

    }

    /**
     * Method: callProductService(List<String> productIds, HttpHeaders headers)
     */
    @Test
    public void testCallProductService() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = OrderServiceImpl.getClass().getMethod("callProductService", List<String>.class, HttpHeaders.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

    /**
     * Method: callPaymentService(OrderServiceRequestDTO orderServiceRequestDTO, HttpHeaders headers)
     */
    @Test
    public void testCallPaymentService() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = OrderServiceImpl.getClass().getMethod("callPaymentService", OrderServiceRequestDTO.class, HttpHeaders.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
