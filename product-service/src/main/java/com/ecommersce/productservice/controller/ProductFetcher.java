package com.ecommersce.productservice.controller;

import com.ecommerce.dto.ProductsDto;
import com.ecommersce.productservice.service.ProductService;
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
    public ResponseEntity<ProductsDto> getProductByProductId(@PathVariable String productId)  {

        ProductsDto data= productService.getProductByProductId(productId);
        return  ResponseEntity.ok(data);

    }

    @GetMapping("/getProductByName/productName/{name}")
    public ResponseEntity<List<ProductsDto>> getProductByProductName(@PathVariable String name) {

        return  ResponseEntity.ok(productService.getProductByProductName(name));

    }
    @GetMapping(value = "/getProductByCategory/category/{category}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ProductsDto>> getProductByCategory(@PathVariable String category)  {

        return ResponseEntity.ok(productService.getProductByCategory(category));

    }
    @GetMapping("/getProductByPriceRange/minPrice/{minPrice}/maxPrice/{maxPrice}")
    public ResponseEntity<List<ProductsDto>> getProductByPrice(@PathVariable Double minPrice, @PathVariable Double maxPrice)  {

        return  ResponseEntity.ok(productService.getProductByPrice(minPrice,maxPrice));

    }

    @GetMapping("/getProductByRating/rating/{rating}")
    public ResponseEntity<List<ProductsDto>> getProductByRating(@PathVariable Double rating)  {

        return  ResponseEntity.ok(productService.getProductByRating(rating));

    }

}
