package com.ecommersce.productservice.controller;

import com.ecommerce.dto.ProductsDto;
import com.ecommersce.productservice.service.ProductService;
import com.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-service")
public class ProductFetcher {

    @Autowired
    private ProductService productService;

    @GetMapping("/retrieveProductById/productId/{productId}")
    public ResponseEntity<ProductsDto> getProductByProductId(@PathVariable String productId) throws NoDataFoundException {

        ProductsDto data= productService.getProductByProductId(productId);
        return  ResponseEntity.ok(data);

    }

    @GetMapping("/getProductByName/productName/{name}")
    public ResponseEntity<List<ProductsDto>> getProductByProductName(@PathVariable String name) throws NoDataFoundException {

        return  ResponseEntity.ok(productService.getProductByProductName(name));

    }
    @GetMapping(value = "/getProductByCategory/category/{category}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ProductsDto>> getProductByCategory(@PathVariable String category) throws NoDataFoundException {

        return ResponseEntity.ok(productService.getProductByCategory(category));

    }
    @GetMapping("/getProductByPriceRange/minPrice/{minPrice}/maxPrice/{maxPrice}")
    public ResponseEntity<List<ProductsDto>> getProductByPrice(@PathVariable Double minPrice, @PathVariable Double maxPrice) throws NoDataFoundException {

        return  ResponseEntity.ok(productService.getProductByPrice(minPrice,maxPrice));

    }

    @GetMapping("/getProductByRating/rating/{rating}")
    public ResponseEntity<List<ProductsDto>> getProductByRating(@PathVariable Double rating) throws NoDataFoundException {

        return  ResponseEntity.ok(productService.getProductByRating(rating));

    }

}
