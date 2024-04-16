package com.ecommerce.productservice.controller;

import com.ecommerce.dto.ProductResponse;
import com.ecommerce.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-service")
public class ProductFetcher {


    private final ProductService productService;
    @Autowired
    public ProductFetcher(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/retrieveProductById/productId/{productId}")
    public ResponseEntity<ProductResponse> getProductByProductId(@PathVariable String productId)  {

        ProductResponse data= productService.getProductByProductId(productId);
        return  ResponseEntity.ok(data);

    }

    @GetMapping("/getProductByName/productName/{name}")
    public ResponseEntity<List<ProductResponse>> getProductByProductName(@PathVariable String name) {

        return  ResponseEntity.ok(productService.getProductByProductName(name));

    }
    @GetMapping(value = "/getProductByCategory/category/{category}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ProductResponse>> getProductByCategory(@PathVariable String category)  {

        return ResponseEntity.ok(productService.getProductByCategory(category));

    }
    @GetMapping("/getProductByPriceRange/category/{category}/minPrice/{minPrice}/maxPrice/{maxPrice}")
    public ResponseEntity<List<ProductResponse>> getProductByPrice(@PathVariable String category,@PathVariable Double minPrice, @PathVariable Double maxPrice)  {

        return  ResponseEntity.ok(productService.getProductByCategoryAndPrice(category,minPrice,maxPrice));

    }

    @GetMapping("/getProductByRating/category/{category}/rating/{rating}")
    public ResponseEntity<List<ProductResponse>> getProductByRating(@PathVariable String category, @PathVariable Double rating)  {

        return  ResponseEntity.ok(productService.getProductByRating(category,rating));

    }

}
