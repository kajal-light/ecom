package com.ecommersce.productservice.controller;

import com.ecommersce.productservice.model.ProductData;
import com.ecommersce.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-service")
public class Controller {

    @Autowired
    private ProductService productService;

    @GetMapping("/getProduct/productId/{id}")
    public ResponseEntity<ProductData> getProductById(@PathVariable String id){

        ProductData data= productService.getProductByProductId(id);
        return  ResponseEntity.ok(data);

    }

    @GetMapping("/getProduct/Name/{Name}")
    public ResponseEntity<ProductData> getProductByName(@PathVariable String Name){

        List<ProductData> data= productService.getProductByName(Name);
        return new ResponseEntity(data,HttpStatus.CREATED);

    }
    @GetMapping("/getProduct/Category/{Category}")
    public ResponseEntity<List<ProductData>> getProductByCategory(@PathVariable String Category){

        List<ProductData> data=  productService.getProductByCategory(Category);
        return new ResponseEntity<>(data,HttpStatus.CREATED);

    }
    @GetMapping("/getProduct/minPrice/{minPrice}/maxPrice/{maxPrice}")
    public ResponseEntity<List<ProductData>> getProductByPrice(@PathVariable Double minPrice,@PathVariable Double maxPrice){

        List<ProductData> data=  productService.getProductByPrice(minPrice,maxPrice);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/getProduct/rating/{rating}")
    public ResponseEntity<List<ProductData>> getProductByRating(@PathVariable Double rating){

        List<ProductData> data=  productService.getProductByRating(rating);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
