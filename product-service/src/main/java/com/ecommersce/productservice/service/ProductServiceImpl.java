package com.ecommersce.productservice.service;

import com.ecommersce.productservice.dao.ProductRepository;
import com.ecommerce.entity.Products;
import com.ecommerce.dto.ProductsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;



@Slf4j
@Component
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String createProduct(ProductsDto productsDto) throws RuntimeException {

        try {
           validatedRequestedPayload(productsDto);
           Products productEntity = new Products();
            BeanUtils.copyProperties(productsDto,productEntity);
            productEntity.setProductId(UUID.randomUUID().toString());
           productRepository.save(productEntity);
           return "Product added successfully ,please find the product id "+ productEntity.getProductId() ;
       }catch(Exception e){

           throw new RuntimeException(e.getMessage());

       }
    }



    @Override
    public void createListOfProduct(List<ProductsDto> productsDto) {

      try {
          List<Products> productsEntity = new ArrayList<>();
          for (ProductsDto dto : productsDto) {
              Products productEntity = new Products();
              BeanUtils.copyProperties(dto, productEntity);
              productEntity.setProductId(UUID.randomUUID().toString());
              productsEntity.add(productEntity);
          }

          productRepository.saveAll(productsEntity);
      }catch(Exception e){
          throw new RuntimeException(e.getMessage());


      }
    }

    @Override
    public void updateProduct(String productId, ProductsDto productsDto)  {
       try {
           Optional<Products> product = productRepository.findByProductId(productId);
           if (product.isPresent()) {
               Products productEntity = product.get();
               productEntity.setProductName(productsDto.getProductName());
               productEntity.setProductPrice(productsDto.getProductPrice());
               productEntity.setStock(productsDto.getStock());
               productEntity.setCategory(productsDto.getCategory());
               productRepository.save(productEntity);

           } else {

               throw new NoSuchElementException();

           }
       }catch(Exception e){
           log.error(e.getMessage());

           throw new NoSuchElementException();

       }
    }

    @Override
    public void deleteProduct(String id) {

        Optional<Products> entity = productRepository.findById(id);
        entity.ifPresent(productRepository::delete);

    }

    @Override
    public ProductsDto getProductByProductId(String productId)  {
try {
    Optional<Products> productEntity = productRepository.findByProductId(productId);

    if (productEntity.isPresent()) {
        ProductsDto product = new ProductsDto();
     BeanUtils.copyProperties(productEntity.get(),product);
        return product;
    } else {

        throw new NoSuchElementException();
    }
}catch (Exception e){
    log.error(e.getMessage());
    throw new NoSuchElementException();
}


    }

    @Override
    public List<ProductsDto> getProductByProductName(String name)  {
       try {
           List<Products> listOfProductEntity = productRepository.findByProductName(name);
           if (!listOfProductEntity.isEmpty()) {
               return listOfProductEntity.stream().map(productEntity -> {
                   ProductsDto productsDto = new ProductsDto();
                   BeanUtils.copyProperties(productEntity, productsDto);
                   return productsDto;
               }).collect(Collectors.toList());
           } else {
               throw new NoSuchElementException();
           }
       }catch (Exception e){
           log.error(e.getMessage());
           throw new NoSuchElementException();
       }

    }

    @Override
    public List<ProductsDto> getProductByCategory(String category)  {
      try{  List<Products> listOfProductsEntity = productRepository.findByProductCategory(category);

           if(!listOfProductsEntity.isEmpty()) {
              return listOfProductsEntity.stream().map(ProductsEntity -> {
                     ProductsDto productsDto = new ProductsDto();
                     BeanUtils.copyProperties(ProductsEntity, productsDto);
                     return productsDto;
                    }).collect(Collectors.toList());
           }else {

            throw new NoSuchElementException();
                 }
       }catch (Exception e){
        log.error(e.getMessage());
        throw new NoSuchElementException();
    }


    }

    @Override
    public List<ProductsDto> getProductByPrice(Double minPrice, Double maxPrice)  {

        try {

            List<Products> listOfProductEntity = productRepository.findByProductPriceBetween(minPrice, maxPrice);
            if (!listOfProductEntity.isEmpty()) {

                return listOfProductEntity.stream().map(productEntity -> {
                    ProductsDto productDto = new ProductsDto();
                    BeanUtils.copyProperties(productEntity, productDto);
                    return productDto;
                }).collect(Collectors.toList());
            } else {

                throw new NoSuchElementException();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NoSuchElementException();
        }

    }

    @Override
    public List<ProductsDto> getProductByRating(Double rating)  {
       try{
           List<Products> listOfProductEntity = productRepository.findByRating(rating);


        if (!listOfProductEntity.isEmpty()) {

            return listOfProductEntity.stream().map(productEntity -> {
                ProductsDto productDto = new ProductsDto();
                BeanUtils.copyProperties(productEntity, productDto);
                return productDto;
            }).collect(Collectors.toList());
        } else {

            throw new NoSuchElementException();
        }
    } catch (Exception e) {
        log.error(e.getMessage());
        throw new NoSuchElementException();
    }


    }

    @Override
    public List<ProductsDto> getListOfStock(List<String> productsId)  {

       try {
           List<Products> stocks = productRepository.findByProductIdIn(productsId);
           List<ProductsDto> productsDtoList = new ArrayList<>();
           if (!stocks.isEmpty()) {
               for (Products product : stocks) {
                   ProductsDto productsDto = new ProductsDto();
                   productsDto.setProductId(product.getProductId());
                   productsDto.setStock(product.getStock());
                   productsDtoList.add(productsDto);
               }
               return productsDtoList;
           } else {

               throw new NoSuchElementException();

           }
       }catch (Exception e){

           log.error(e.getMessage());
           throw new NoSuchElementException();
       }

    }

    private void validatedRequestedPayload(ProductsDto productsDto) throws RuntimeException {
        if(productsDto.getProductPrice()<0 && productsDto.getProductName().isBlank()&& productsDto.getStock()<0){

            throw new RuntimeException("Invalid filed value") ;
        }


    }
}
