package com.ecommersce.productservice.service;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.entity.Products;
import com.ecommersce.productservice.constants.ProductServiceConstants;
import com.ecommersce.productservice.dao.ProductRepository;
import com.exception.InvalidProductException;
import com.exception.NoProductFoundException;
import com.exception.OutOfStockException;
import com.exception.model.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public String createProduct(ProductDTO productDTO) {

        try {
            validateRequestPayload(productDTO);
            Products productEntity = new Products();
            BeanUtils.copyProperties(productDTO, productEntity);
            productEntity.setProductId(UUID.randomUUID().toString());
            productRepository.save(productEntity);
            return "Product added successfully ,please find the product id " + productEntity.getProductId();
        } catch (InvalidProductException e) {
            log.error(e.getErrorDetails().toString());
            throw e;

        }
    }


    @Override
    public void createListOfProduct(List<ProductDTO> productDTO) {

        try {
            List<Products> productsEntity = new ArrayList<>();
            for (ProductDTO dto : productDTO) {
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
    public void updateProduct(String productId, ProductDTO productDTO) {

        Optional<Products> product = productRepository.findByProductId(productId);
        if (product.isPresent()) {
            Products productEntity = product.get();
            productEntity.setProductName(productDTO.getProductName());
            productEntity.setProductPrice(productDTO.getProductPrice());
            productEntity.setStock(productDTO.getStock());
            productEntity.setCategory(productDTO.getCategory());
            productRepository.save(productEntity);

        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_ID_CODE, ProductServiceConstants.INVALID_PRODUCT_ID_MESSAGE,ProductServiceConstants.SERVICE_NAME));

        }

    }

    @Override
    public void deleteProduct(String id) {

        Optional<Products> entity = productRepository.findById(id);
        entity.ifPresent(productRepository::delete);

    }

    @Override
    public ProductDTO getProductByProductId(String productId) {

            Optional<Products> productEntity = productRepository.findByProductId(productId);

            if (productEntity.isPresent()) {
                ProductDTO product = new ProductDTO();
                BeanUtils.copyProperties(productEntity.get(), product);
                return product;
            } else {

                throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_ID_CODE, ProductServiceConstants.INVALID_PRODUCT_ID_MESSAGE,ProductServiceConstants.SERVICE_NAME));
            }



    }

    @Override
    public List<ProductDTO> getProductByProductName(String name) {

        List<Products> listOfProductEntity = productRepository.findByProductName(name);
        if (!listOfProductEntity.isEmpty()) {
            return listOfProductEntity.stream().map(productEntity -> {
                ProductDTO productDTO = new ProductDTO();
                BeanUtils.copyProperties(productEntity, productDTO);
                return productDTO;
            }).collect(Collectors.toList());
        } else {
            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_NAME_CODE, ProductServiceConstants.INVALID_PRODUCT_NAME_MESSAGE,ProductServiceConstants.SERVICE_NAME));
        }


    }

    @Override
    public List<ProductDTO> getProductByCategory(String category) {

        List<Products> listOfProductsEntity = productRepository.findByProductCategory(category);

        if (!listOfProductsEntity.isEmpty()) {
            return listOfProductsEntity.stream().map(ProductsEntity -> {
                ProductDTO productDTO = new ProductDTO();
                BeanUtils.copyProperties(ProductsEntity, productDTO);
                return productDTO;
            }).collect(Collectors.toList());
        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_CATEGORY_CODE, ProductServiceConstants.INVALID_PRODUCT_CATEGORY_MESSAGE,ProductServiceConstants.SERVICE_NAME));
        }


    }

    @Override
    public List<ProductDTO> getProductByPrice(Double minPrice, Double maxPrice) {


        List<Products> listOfProductEntity = productRepository.findByProductPriceBetween(minPrice, maxPrice);
        if (!listOfProductEntity.isEmpty()) {

            return listOfProductEntity.stream().map(productEntity -> {
                ProductDTO productDto = new ProductDTO();
                BeanUtils.copyProperties(productEntity, productDto);
                return productDto;
            }).collect(Collectors.toList());
        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_PRICE_CODE, ProductServiceConstants.INVALID_PRODUCT_PRICE_MESSAGE,ProductServiceConstants.SERVICE_NAME));
        }


    }

    @Override
    public List<ProductDTO> getProductByRating(Double rating) {

        List<Products> listOfProductEntity = productRepository.findByRating(rating);


        if (!listOfProductEntity.isEmpty()) {

            return listOfProductEntity.stream().map(productEntity -> {
                ProductDTO productDto = new ProductDTO();
                BeanUtils.copyProperties(productEntity, productDto);
                return productDto;
            }).collect(Collectors.toList());
        } else {

            throw new NoProductFoundException(new ErrorDetails(HttpStatus.BAD_REQUEST, ProductServiceConstants.INVALID_PRODUCT_RATING_CODE, ProductServiceConstants.INVALID_PRODUCT_RATING_MESSAGE,ProductServiceConstants.SERVICE_NAME));
        }


    }

    @Override
    public List<ProductDTO> getListOfStock(List<String> productsId) {


        List<Products> stocks = productRepository.findByProductIdIn(productsId);
        List<ProductDTO> productDTOList = new ArrayList<>();
        if (!stocks.isEmpty()) {
            for (Products product : stocks) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setProductId(product.getProductId());
                productDTO.setStock(product.getStock());
                productDTOList.add(productDTO);
            }
            return productDTOList;
        } else {

          throw new OutOfStockException(new ErrorDetails(HttpStatus.EXPECTATION_FAILED, ProductServiceConstants.PRODUCT_OUT_OF_STOCK_CODE,ProductServiceConstants.PRODUCT_OUT_OF_STOCK_MESSAGE, ProductServiceConstants.SERVICE_NAME));

        }


    }

    private void validateRequestPayload(ProductDTO productDTO) throws InvalidProductException {
        if (productDTO.getProductPrice() < 0 && productDTO.getProductName().isBlank() && productDTO.getStock() < 0) {
            throw new InvalidProductException(new ErrorDetails(HttpStatus.PRECONDITION_FAILED, ProductServiceConstants.INVALID_PRODUCT_REQUEST_CODE, ProductServiceConstants.INVALID_PRODUCT_REQUEST_MESSAGE, ProductServiceConstants.SERVICE_NAME));
        }
    }
}
