package com.ecommerce.service;
import com.ecommerce.model.*;import java.util.*;
public class ReviewService {
 private final Store db;public ReviewService(Store db){this.db=db;}
 public Review add(String customerId,String orderId,String productId,int rating,String comment){
  if(rating<1||rating>5)throw new IllegalArgumentException("Rating must be 1 to 5");
  Order o=db.orders.get(orderId);if(o==null||!o.getCustomerId().equals(customerId)||o.getStatus()!=OrderStatus.DELIVERED)throw new IllegalStateException("Only delivered orders can be reviewed");
  if(o.getItems().stream().noneMatch(i->i.getProductId().equals(productId)))throw new IllegalArgumentException("Product not in order");
  if(db.reviews.stream().anyMatch(r->r.getOrderId().equals(orderId)&&r.getProductId().equals(productId)))throw new IllegalStateException("One review per product per order");
  Review r=new Review("REV-"+UUID.randomUUID(),customerId,orderId,productId,rating,comment);db.reviews.add(r);return r;
 }
 public boolean isFlaggedForAdmin(String productId){
  List<Review> list=db.reviews.stream().filter(r->r.getProductId().equals(productId)).toList();
  return list.size()>=10&&list.stream().mapToInt(Review::getRating).average().orElse(5.0)<2.0;
 }
}
