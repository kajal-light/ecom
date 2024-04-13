package com.ecommersce.productservice.controller;


import com.ecommerce.dto.ProductData;
import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommersce.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-service")
public class ProductController {


    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/createProduct")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {

        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);

    }

    @PostMapping("/createsProducts")
    public ResponseEntity<String> createListOfProduct(@RequestBody List<ProductRequest> productRequest) {

        productService.createListOfProduct(productRequest);
        return new ResponseEntity<>("List of Products is inserted successfully", HttpStatus.CREATED);

    }

    @PutMapping("/updateProducts/productId/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest) {

        productService.updateProduct(productId, productRequest);
        return new ResponseEntity<>("Updated successfully", HttpStatus.CREATED);

    }


    @DeleteMapping("/deleteProducts/productId/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {

        productService.deleteProduct(productId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);

    }

    @PostMapping("/fetchStock")
    public ResponseEntity<List<ProductData>> getListOfStock(@RequestBody List<String> productsId) {

        return ResponseEntity.ok(productService.getListOfStock(productsId));
    }

}
