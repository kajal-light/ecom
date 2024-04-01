package com.ecommersce.productservice.controller;

import com.ecommersce.productservice.dto.ProductsDto;
import com.ecommersce.productservice.service.ProductService;
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
    public ResponseEntity<ProductsDto> getProductByProductId(@PathVariable String productId){

        ProductsDto data= productService.getProductByProductId(productId);
        return  ResponseEntity.ok(data);

    }

    @GetMapping("/getProductByName/ProductName/{Name}")
    public ResponseEntity<List<ProductsDto>> getProductByProductName(@PathVariable String Name){

        return  ResponseEntity.ok(productService.getProductByProductName(Name));

    }
    @GetMapping(value = "/getProductByCategory/category/{category}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ProductsDto>> getProductByCategory(@PathVariable String Category){

        return ResponseEntity.ok(productService.getProductByCategory(Category));

    }
    @GetMapping("/getProductByPriceRange/minPrice/{minPrice}/maxPrice/{maxPrice}")
    public ResponseEntity<List<ProductsDto>> getProductByPrice(@PathVariable Double minPrice, @PathVariable Double maxPrice){

        return  ResponseEntity.ok(productService.getProductByPrice(minPrice,maxPrice));

    }

    @GetMapping("/getProductByRating/rating/{rating}")
    public ResponseEntity<List<ProductsDto>> getProductByRating(@PathVariable Double rating){

        return  ResponseEntity.ok(productService.getProductByRating(rating));

    }

}
