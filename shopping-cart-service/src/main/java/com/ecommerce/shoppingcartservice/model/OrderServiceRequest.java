package com.ecommerce.shoppingcartservice.model;

import java.util.List;

public class OrderServiceRequest {


    private List<String> productId;

    private List<Double> productPrice;

    private Double totalAmount;

    private String userId;
    private List<Integer> quantity;
    public List<String> getProductId() {
        return productId;
    }

    public List<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
    }

    public void setProductId(List<String> productId) {
        this.productId = productId;
    }


    public List<Double> getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(List<Double> productPrice) {
        this.productPrice = productPrice;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
