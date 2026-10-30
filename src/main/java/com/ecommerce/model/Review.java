package com.ecommerce.model;
import java.time.LocalDateTime;
public class Review {
 private final String id,customerId,orderId,productId,comment;private final int rating;private final LocalDateTime createdAt;
 public Review(String id,String customerId,String orderId,String productId,int rating,String comment){this.id=id;this.customerId=customerId;this.orderId=orderId;this.productId=productId;this.rating=rating;this.comment=comment;this.createdAt=LocalDateTime.now();}
 public String getId(){return id;}public String getCustomerId(){return customerId;}public String getOrderId(){return orderId;}public String getProductId(){return productId;}public int getRating(){return rating;}
}
