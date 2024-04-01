package com.ecommersce.productservice.service;

import com.ecommersce.productservice.dao.ProductRepository;
import com.ecommersce.productservice.entity.Products;
import com.ecommersce.productservice.dto.ProductsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void createProduct(ProductsDto productsDto) {

        try {
           validatedRequestedPayload(productsDto);

           Products productEntity = new Products();
           productEntity.setProductId(UUID.randomUUID().toString());
           productEntity.setProductName(productsDto.getProductName());
           productEntity.setCategory(productsDto.getCategory());
           productEntity.setDate(productsDto.getDate());
           productEntity.setStock(productsDto.getStock());
           productEntity.setRating(productsDto.getRating());
           productEntity.setProductPrice(productsDto.getProductPrice());
           productRepository.save(productEntity);
       }catch(Exception e){

           throw new RuntimeException(e.getMessage());

       }
    }



    @Override
    public void createListOfProduct(List<ProductsDto> productsDto) {

        List<Products> productsEntity = new ArrayList<>();

        for (ProductsDto product : productsDto) {
            Products productEntity = new Products();
            productEntity.setProductPrice(product.getProductPrice());
            productEntity.setRating(product.getRating());
            productEntity.setCategory(product.getCategory());
            productEntity.setProductName(product.getProductName());
            productEntity.setDate(product.getDate());
            productEntity.setStock(product.getStock());
            productsEntity.add(productEntity);
        }


        productRepository.saveAll(productsEntity);

    }

    @Override
    public void updateProduct(String productId, ProductsDto productsDto) {
        Optional<Products> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Products productEntity = product.get();
            productEntity.setProductName(productsDto.getProductName());
            productEntity.setCategory(productsDto.getCategory());
            productEntity.setDate(productsDto.getDate());
            productEntity.setStock(productsDto.getStock());
            productEntity.setRating(productsDto.getRating());
            productEntity.setProductPrice(productsDto.getProductPrice());
            productRepository.save(productEntity);

        } else {

            throw new RuntimeException("no data found for give Product_Id" + productId);

        }

    }

    @Override
    public void DeleteProduct(String id) {

        Optional<Products> entity = productRepository.findById(id);
        entity.ifPresent(product -> productRepository.delete(product));

    }

    @Override
    public ProductsDto getProductByProductId(String productId) {
try {
    Optional<Products> ProductEntity = productRepository.findByProductId(productId);
    ProductsDto product = new ProductsDto();
    if (ProductEntity.isPresent()) {
        product.setProductName(ProductEntity.get().getProductName());
        product.setCategory(ProductEntity.get().getCategory());
        product.setDate(ProductEntity.get().getDate());
        product.setStock(ProductEntity.get().getStock());
        product.setRating(ProductEntity.get().getRating());
        product.setProductPrice(ProductEntity.get().getProductPrice());
        return product;
    } else {

        throw new RuntimeException("No data found for give Product Id" + productId);
    }
}catch (Exception e){

    throw new RuntimeException(e.getMessage());
}


    }

    @Override
    public List<ProductsDto> getProductByProductName(String name) {
        List<Products> ListOfProductEntity = productRepository.findByProductName(name);
        List<ProductsDto> ProductsDtoList = new ArrayList<>();

        for (Products product : ListOfProductEntity) {
            ProductsDto ProductDto = new ProductsDto();
            ProductDto.setProductName(product.getProductName());
            ProductDto.setCategory(product.getCategory());
            ProductDto.setDate(product.getDate());
            ProductDto.setStock(product.getStock());
            ProductDto.setRating(product.getRating());
            ProductDto.setProductPrice(product.getProductPrice());
            ProductsDtoList.add(ProductDto);
        }


        return ProductsDtoList;
    }

    @Override
    public List<ProductsDto> getProductByCategory(String category) {
        List<Products> listOfProductsEntity = productRepository.findByProductCategory(category);
        List<ProductsDto> productsDtoList = new ArrayList<>();

        for (Products product : listOfProductsEntity) {
            ProductsDto productsDto = new ProductsDto();
            productsDto.setProductName(product.getProductName());
            productsDto.setCategory(product.getCategory());
            productsDto.setDate(product.getDate());
            productsDto.setStock(product.getStock());
            productsDto.setRating(product.getRating());
            productsDto.setProductPrice(product.getProductPrice());
            productsDtoList.add(productsDto);
        }


        return productsDtoList;
    }

    @Override
    public List<ProductsDto> getProductByPrice(Double minPrice, Double maxPrice) {
        List<Products> listOfProductEntity = productRepository.findByProductPrice(minPrice, maxPrice);
        List<ProductsDto> productDtoList = new ArrayList<>();

        for (Products productEntity : listOfProductEntity) {
            ProductsDto productsDto = new ProductsDto();
            productsDto.setProductName(productEntity.getProductName());
            productsDto.setCategory(productEntity.getCategory());
            productsDto.setDate(productEntity.getDate());
            productsDto.setStock(productEntity.getStock());
            productsDto.setRating(productEntity.getRating());
            productsDto.setProductPrice(productEntity.getProductPrice());
            productDtoList.add(productsDto);
        }


        return productDtoList;
    }

    @Override
    public List<ProductsDto> getProductByRating(Double rating) {
        List<Products> listOfProductEntity = productRepository.findByRating(rating);
        List<ProductsDto> ProductsDtoList = new ArrayList<>();

        for (Products product : listOfProductEntity) {
            ProductsDto productDto = new ProductsDto();
            productDto.setProductName(product.getProductName());
            productDto.setCategory(product.getCategory());
            productDto.setDate(product.getDate());
            productDto.setStock(product.getStock());
            productDto.setRating(product.getRating());
            productDto.setProductPrice(product.getProductPrice());
            ProductsDtoList.add(productDto);
        }


        return ProductsDtoList;


    }

    @Override
    public List<ProductsDto> getListOfStock(List<String> productsId) {

        List<Products> stocks = productRepository.findByProductIdIn(productsId);
        List<ProductsDto> productsDtoList = new ArrayList<>();
        for (Products product : stocks) {
            ProductsDto productsDto = new ProductsDto();
            productsDto.setProductId(product.getProductId());
            productsDto.setStock(product.getStock());
            productsDtoList.add(productsDto);
        }

        return productsDtoList;
    }

    private void validatedRequestedPayload(ProductsDto productsDto) {
        if(productsDto.getProductPrice()<0 && productsDto.getProductName().isBlank()&& productsDto.getStock()<0){

            throw new RuntimeException("Invalid filed value") ;
        }


    }
}
