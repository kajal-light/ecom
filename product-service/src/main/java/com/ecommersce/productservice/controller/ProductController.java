package com.ecommersce.productservice.controller;

import com.ecommersce.productservice.entity.Product;
import com.ecommersce.productservice.model.ProductData;
import com.ecommersce.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-service")
public class ProductController {

  @Autowired
  private ProductService productService;

@PostMapping("/createProduct")
  public ResponseEntity<String> createProduct(@RequestBody ProductData data){

    productService.createProduct(data);
      return new ResponseEntity<>(HttpStatus.CREATED);

  }
@PostMapping("/createsProducts")
  public ResponseEntity<String> createListOfProduct(@RequestBody List<ProductData> data){

    productService.createListOfProduct(data);
    return new ResponseEntity<>(HttpStatus.CREATED);

  }

  @PutMapping("/UpdateProducts/productId/{id}")
  public ResponseEntity<String> updateProduct(@PathVariable String id,@RequestBody ProductData data){

    productService.updateProduct(id,data);
    return new ResponseEntity<>(HttpStatus.CREATED);

  }


  @DeleteMapping("/DeleteProducts/productId/{id}")
  public ResponseEntity<String> deleteProduct(@PathVariable String id){

    productService.DeleteProduct(id);
    return new ResponseEntity<>(HttpStatus.OK);

  }
  @PostMapping("/fetchStock")
  public List<ProductData> getListOfStock(@RequestBody List<String> productsId){

    List<ProductData> productData= productService.getListOfStock(productsId);
    return productData;

  }

}
