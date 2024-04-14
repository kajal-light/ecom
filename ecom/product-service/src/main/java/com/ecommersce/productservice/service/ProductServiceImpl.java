package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductData;
import com.ecommerce.dto.ProductRequest;
import com.ecommerce.dto.ProductResponse;
import com.ecommerce.entity.Products;
import com.ecommerce.exception.InvalidProductException;
import com.ecommerce.exception.NoProductFoundException;
import com.ecommerce.exception.OutOfStockException;
import com.ecommerce.exception.dto.ErrorDetails;
import com.ecommersce.productservice.constants.ProductServiceConstants;
import com.ecommersce.productservice.dao.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public String createProduct(ProductRequest productRequest) {

        try {
            validateRequestPayload(productRequest);
            Products productEntity = new Products();
            BeanUtils.copyProperties(productRequest, productEntity);
            productEntity.setProductId(UUID.randomUUID().toString());
            productRepository.save(productEntity);
            return "Product added successfully ,please find the product id " + productEntity.getProductId();
        } catch (InvalidProductException e) {
            log.error(e.getErrorDetails().toString());
            throw e;

        }
    }


    @Override
    public void createListOfProduct(List<ProductRequest> productRequest) {

        try {
            List<Products> productsEntity = new ArrayList<>();
            for (ProductRequest dto : productRequest) {
                validateRequestPayload(dto);
                Products productEntity = new Products();
                BeanUtils.copyProperties(dto, productEntity);
                productEntity.setProductId(UUID.randomUUID().toString());
                productsEntity.add(productEntity);
            }

            productRepository.saveAll(productsEntity);
        } catch (InvalidProductException e) {
            log.error(e.getErrorDetails().toString());
            throw e;


        }
    }

    @Override
    public void updateProduct(String productId, ProductRequest productRequest) {

        Optional<Products> product = productRepository.findByProductId(productId);
        if (product.isPresent()) {
            Products productEntity = product.get();
            productEntity.setProductName(productRequest.getProductName());
            productEntity.setProductPrice(productRequest.getProductPrice());
            productEntity.setStock(productRequest.getStock());
            productEntity.setCategory(productRequest.getCategory());
            productRepository.save(productEntity);

        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_ID_CODE, ProductServiceConstants.INVALID_PRODUCT_ID_MESSAGE, ProductServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));

        }

    }

    @Override
    public void deleteProduct(String id) {

        Optional<Products> entity = productRepository.findById(id);
        entity.ifPresent(productRepository::delete);

    }

    @Override
    public ProductResponse getProductByProductId(String productId) {

        Optional<Products> productEntity = productRepository.findByProductId(productId);

        if (productEntity.isPresent()) {
            ProductResponse product = new ProductResponse();
            BeanUtils.copyProperties(productEntity.get(), product);
            return product;
        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_ID_CODE, ProductServiceConstants.INVALID_PRODUCT_ID_MESSAGE, ProductServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
        }


    }

    @Override
    public List<ProductResponse> getProductByProductName(String name) {

        List<Products> listOfProductEntity = productRepository.findByProductName(name);
        if (!listOfProductEntity.isEmpty()) {
            return listOfProductEntity.stream().map(productEntity -> {
                ProductResponse productResponse = new ProductResponse();
                BeanUtils.copyProperties(productEntity, productResponse);
                return productResponse;
            }).collect(Collectors.toList());
        } else {
            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_NAME_CODE, ProductServiceConstants.INVALID_PRODUCT_NAME_MESSAGE, ProductServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
        }


    }

    @Override
    public List<ProductResponse> getProductByCategory(String category) {

        List<Products> listOfProductsEntity = productRepository.findByProductCategory(category);

        if (!listOfProductsEntity.isEmpty()) {
            return listOfProductsEntity.stream().map(ProductsEntity -> {
                ProductResponse productResponse = new ProductResponse();
                BeanUtils.copyProperties(ProductsEntity, productResponse);
                return productResponse;
            }).collect(Collectors.toList());
        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_CATEGORY_CODE, ProductServiceConstants.INVALID_PRODUCT_CATEGORY_MESSAGE, ProductServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
        }


    }

    @Override
    public List<ProductResponse> getProductByPrice(Double minPrice, Double maxPrice) {


        List<Products> listOfProductEntity = productRepository.findByProductPriceBetween(minPrice, maxPrice);
        if (!listOfProductEntity.isEmpty()) {

            return listOfProductEntity.stream().map(productEntity -> {
                ProductResponse productResponse = new ProductResponse();
                BeanUtils.copyProperties(productEntity, productResponse);
                return productResponse;
            }).collect(Collectors.toList());
        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_PRICE_CODE, ProductServiceConstants.INVALID_PRODUCT_PRICE_MESSAGE, ProductServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
        }


    }

    @Override
    public List<ProductResponse> getProductByRating(Double rating) {

        List<Products> listOfProductEntity = productRepository.findByRating(rating);


        if (!listOfProductEntity.isEmpty()) {

            return listOfProductEntity.stream().map(productEntity -> {
                ProductResponse productResponse = new ProductResponse();
                BeanUtils.copyProperties(productEntity, productResponse);
                return productResponse;
            }).collect(Collectors.toList());
        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_RATING_CODE, ProductServiceConstants.INVALID_PRODUCT_RATING_MESSAGE, ProductServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
        }


    }

    @Override
    public List<ProductData> getListOfStock(List<String> productsId) {


        List<Products> stocks = productRepository.findByProductIdIn(productsId);


        List<ProductData> productResponseList = new ArrayList<>();
        if (!stocks.isEmpty()) {
            for (Products product : stocks) {
                ProductData productResponse = new ProductData();
                productResponse.setProductId(product.getProductId());
                productResponse.setStock(product.getStock());
                productResponseList.add(productResponse);
            }
            return productResponseList;
        } else {

            throw new OutOfStockException(new ErrorDetails(HttpStatus.EXPECTATION_FAILED, ProductServiceConstants.PRODUCT_OUT_OF_STOCK_CODE, ProductServiceConstants.PRODUCT_OUT_OF_STOCK_MESSAGE, ProductServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));

        }


    }

    private void validateRequestPayload(ProductRequest productResponse) throws InvalidProductException {
        if (productResponse.getProductPrice() < 0 && productResponse.getProductName().isBlank() && productResponse.getStock() < 0) {
            throw new InvalidProductException(new ErrorDetails(HttpStatus.PRECONDITION_FAILED, ProductServiceConstants.INVALID_PRODUCT_REQUEST_CODE, ProductServiceConstants.INVALID_PRODUCT_REQUEST_MESSAGE, ProductServiceConstants.SERVICE_NAME, LocalDateTime.now().toString()));
        }
    }
}
