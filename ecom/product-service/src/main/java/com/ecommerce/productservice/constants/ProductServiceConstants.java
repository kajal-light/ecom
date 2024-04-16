package com.ecommerce.productservice.constants;

public class ProductServiceConstants {
    private ProductServiceConstants(){}
    public static final String SERVICE_NAME = "PRODUCT_SERVICE";
    public static final String INVALID_PRODUCT_REQUEST_MESSAGE = "PRODUCT WITH INVALID FIELDS";
    public static final String INVALID_PRODUCT_REQUEST_CODE = "PS_001";
    public static final String INVALID_PRODUCT_ID_CODE = "PS_000";
    public static final String INVALID_PRODUCT_ID_MESSAGE = "NO PRODUCT FOUND WITH THE GIVEN ID";
    public static final String INVALID_PRODUCT_NAME_CODE = "PS_002";
    public static final String INVALID_PRODUCT_NAME_MESSAGE = "NO PRODUCT FOUND WITH THE GIVEN NAME";
    public static final String INVALID_PRODUCT_CATEGORY_CODE = "PS_003";
    public static final String INVALID_PRODUCT_CATEGORY_MESSAGE = "NO PRODUCT FOUND IN THE GIVEN CATEGORY";
    public static final String INVALID_PRODUCT_PRICE_CODE = "PS_004";
    public static final String INVALID_PRODUCT_PRICE_MESSAGE = "NO PRODUCT FOUND IN THE GIVEN RANGE";
    public static final String INVALID_PRODUCT_RATING_CODE = "PS_005";
    public static final String INVALID_PRODUCT_RATING_MESSAGE = "NO PRODUCT FOUND WITH THE GIVEN RATING";

    public static final String PRODUCT_OUT_OF_STOCK_CODE = "PS006";
    public static final String PRODUCT_OUT_OF_STOCK_MESSAGE = "PRODUCT IS OUT OF STOCK";
}
