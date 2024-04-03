package com.ecommersce.productservice.controller;


import com.ecommersce.productservice.dto.ProductsDto;

import com.ecommersce.productservice.service.ProductService;
import org.exception.NoDataFoundException;
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
public ResponseEntity<String> createProduct(@RequestBody ProductsDto data){

  productService.createProduct(data);
  return new ResponseEntity<>("Product is inserted successfully",HttpStatus.CREATED);

  }
@PostMapping("/createsProducts")
  public ResponseEntity<String> createListOfProduct(@RequestBody List<ProductsDto> data){

    productService.createListOfProduct(data);
    return new ResponseEntity<>("List of Products is inserted successfully",HttpStatus.CREATED);

  }

  @PutMapping("/updateProducts/productId/{productId}")
  public ResponseEntity<String> updateProduct(@PathVariable String productId,@RequestBody ProductsDto productsDto) throws NoDataFoundException {

    productService.updateProduct(productId, productsDto);
    return new ResponseEntity<>("Updated successfully",HttpStatus.CREATED);

  }


  @DeleteMapping("/deleteProducts/productId/{productId}")
  public ResponseEntity<String> deleteProduct(@PathVariable String productId){

    productService.DeleteProduct(productId);
    return new ResponseEntity<>("Deleted successfully",HttpStatus.OK);

  }
  @PostMapping("/fetchStock")
  public ResponseEntity<List<ProductsDto>> getListOfStock(@RequestBody List<String> productsId) throws NoDataFoundException {

  return  ResponseEntity.ok(productService.getListOfStock(productsId));
  }

}
