package com.ecommerce.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Products {
 @Id
 private String productId;
  @Column
   private String productName;
  @Column
   private String category;
  @Column
   private Double price;
  @Column
   private LocalDate date;
  @Column
   private Double rating;
  @Column
   private Integer stock;

 public String getProductId() {
  return productId;
 }

 public void setProductId(String productId) {
  this.productId = productId;
 }

 public String getProductName() {
  return productName;
 }

 public void setProductName(String productName) {
  this.productName = productName;
 }

 public String getCategory() {
  return category;
 }

 public void setCategory(String category) {
  this.category = category;
 }

 public Double getPrice() {
  return price;
 }

 public void setPrice(Double price) {
  this.price = price;
 }

 public LocalDate getDate() {
  return date;
 }

 public void setDate(LocalDate date) {
  this.date = date;
 }

 public Double getRating() {
  return rating;
 }

 public void setRating(Double rating) {
  this.rating = rating;
 }

 public Integer getStock() {
  return stock;
 }

 public void setStock(Integer stock) {
  this.stock = stock;
 }

 @Override
 public String toString() {
  return "Products{" +
          "productId=" + productId +
          ", productName='" + productName + '\'' +
          ", category='" + category + '\'' +
          ", price=" + price +
          ", date=" + date +
          ", rating=" + rating +
          ", inventory=" + stock +
          '}';
 }
}
