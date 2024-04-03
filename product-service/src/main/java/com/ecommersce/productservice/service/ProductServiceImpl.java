package com.ecommersce.productservice.service;

import com.ecommersce.productservice.dao.ProductRepository;
import com.ecommersce.productservice.entity.Products;
import com.ecommersce.productservice.dto.ProductsDto;
import lombok.extern.slf4j.Slf4j;
import org.exception.NoDataFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;



@Slf4j
@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void createProduct(ProductsDto productsDto) {

        try {
           validatedRequestedPayload(productsDto);
           Products productEntity = new Products();
            BeanUtils.copyProperties(productsDto,productEntity);
            productEntity.setProductId(UUID.randomUUID().toString());
           productRepository.save(productEntity);
       }catch(Exception e){

           throw new RuntimeException(e.getMessage());

       }
    }



    @Override
    public void createListOfProduct(List<ProductsDto> productsDto) {

      try {
          List<Products>productsEntity=  productsDto.stream().map(product->{
                  Products productEntity = new Products();
                  BeanUtils.copyProperties(product,productEntity);
              productEntity.setProductId(UUID.randomUUID().toString());
                  return productEntity;
          }).collect(Collectors.toList());

        productRepository.saveAll(productsEntity);
      }catch(Exception e){
          throw new RuntimeException(e.getMessage());


      }
    }

    @Override
    public void updateProduct(String productId, ProductsDto productsDto) throws NoDataFoundException {
       try {
           Optional<Products> product = productRepository.findById(productId);
           if (product.isPresent()) {
               Products productEntity = product.get();
               productEntity.setProductName(productsDto.getProductName());
               productEntity.setProductPrice(productsDto.getProductPrice());
               productEntity.setStock(productsDto.getStock());
               productEntity.setCategory(productsDto.getCategory());
               productRepository.save(productEntity);

           } else {

               throw new NoDataFoundException("NO_DATA_FOUND", "No details found for product with ID: " + productId);

           }
       }catch(Exception e){
           log.error(e.getMessage());

           throw new NoDataFoundException("EXCEPTION_OCCURRED",  e.getMessage());

       }
    }

    @Override
    public void DeleteProduct(String id) {

        Optional<Products> entity = productRepository.findById(id);
        entity.ifPresent(product -> productRepository.delete(product));

    }

    @Override
    public ProductsDto getProductByProductId(String productId) throws NoDataFoundException {
try {
    Optional<Products> productEntity = productRepository.findByProductId(productId);

    if (productEntity.isPresent()) {
        ProductsDto product = new ProductsDto();
     BeanUtils.copyProperties(productEntity.get(),product);
        return product;
    } else {

        throw new NoDataFoundException("NO_DATA_FOUND", "No details found for product with ID: " + productId);
    }
}catch (Exception e){
    log.error(e.getMessage());
    throw new NoDataFoundException("EXCEPTION_OCCURRED", e.getMessage());
}


    }

    @Override
    public List<ProductsDto> getProductByProductName(String name) throws NoDataFoundException {
       try {
           List<Products> ListOfProductEntity = productRepository.findByProductName(name);
           if (!ListOfProductEntity.isEmpty()) {
               return ListOfProductEntity.stream().map(ProductEntity -> {
                   ProductsDto productsDto = new ProductsDto();
                   BeanUtils.copyProperties(ProductEntity, productsDto);
                   return productsDto;
               }).collect(Collectors.toList());
           } else {
               throw new NoDataFoundException("NO_DATA_FOUND", "No details found for product with product name: " + name);
           }
       }catch (Exception e){
           log.error(e.getMessage());
           throw new NoDataFoundException("EXCEPTION_OCCURRED",  e.getMessage());
       }

    }

    @Override
    public List<ProductsDto> getProductByCategory(String category) throws NoDataFoundException {
      try{  List<Products> listOfProductsEntity = productRepository.findByProductCategory(category);

           if(!listOfProductsEntity.isEmpty()) {
              return listOfProductsEntity.stream().map(ProductsEntity -> {
                     ProductsDto productsDto = new ProductsDto();
                     BeanUtils.copyProperties(ProductsEntity, productsDto);
                     return productsDto;
                    }).collect(Collectors.toList());
           }else {

            throw new NoDataFoundException("NO_DATA_FOUND", "No details found for product with category: " + category);
                 }
       }catch (Exception e){
        log.error(e.getMessage());
        throw new NoDataFoundException("EXCEPTION_OCCURRED",  e.getMessage());
    }


    }

    @Override
    public List<ProductsDto> getProductByPrice(Double minPrice, Double maxPrice) throws NoDataFoundException {

        try {

            List<Products> listOfProductEntity = productRepository.findByProductPriceBetween(minPrice, maxPrice);
            if (!listOfProductEntity.isEmpty()) {

                return listOfProductEntity.stream().map(productEntity -> {
                    ProductsDto productDto = new ProductsDto();
                    BeanUtils.copyProperties(productEntity, productDto);
                    return productDto;
                }).collect(Collectors.toList());
            } else {

                throw new NoDataFoundException("NO_DATA_FOUND", "No details found for given range");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new NoDataFoundException("EXCEPTION_OCCURRED", "No details found" + e.getMessage());
        }

    }

    @Override
    public List<ProductsDto> getProductByRating(Double rating) throws NoDataFoundException {
       try{ List<Products> listOfProductEntity = productRepository.findByRating(rating);


        if (!listOfProductEntity.isEmpty()) {

            return listOfProductEntity.stream().map(productEntity -> {
                ProductsDto productDto = new ProductsDto();
                BeanUtils.copyProperties(productEntity, productDto);
                return productDto;
            }).collect(Collectors.toList());
        } else {

            throw new NoDataFoundException("NO_DATA_FOUND", "No details found for given rating");
        }
    } catch (Exception e) {
        log.error(e.getMessage());
        throw new NoDataFoundException("EXCEPTION_OCCURRED", e.getMessage());
    }


    }

    @Override
    public List<ProductsDto> getListOfStock(List<String> productsId) throws NoDataFoundException {

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

               throw new NoDataFoundException("NO_DATA_FOUND", "No details found for given rating");

           }
       }catch (Exception e){

           log.error(e.getMessage());
           throw new NoDataFoundException("EXCEPTION_OCCURRED", e.getMessage());
       }

    }

    private void validatedRequestedPayload(ProductsDto productsDto) {
        if(productsDto.getProductPrice()<0 && productsDto.getProductName().isBlank()&& productsDto.getStock()<0){

            throw new RuntimeException("Invalid filed value") ;
        }


    }
}
