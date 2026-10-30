package com.ecommerce.service;
import com.ecommerce.model.*;import java.util.*;
public class CartService {
 private final Store db;private final CatalogService catalog;
 public CartService(Store db,CatalogService catalog){this.db=db;this.catalog=catalog;}
 public Cart getCart(String customerId){if(!(db.users.get(customerId) instanceof Customer))throw new IllegalArgumentException("Customer not found");return db.carts.computeIfAbsent(customerId,Cart::new);}
 public void add(String customerId,String productId,int quantity){
  if(quantity<1||quantity>10)throw new IllegalArgumentException("Quantity must be 1 to 10");
  Product p=catalog.get(productId);if(p.getStock()==0)throw new IllegalStateException("Out of Stock");
  Cart c=getCart(customerId);CartItem old=c.getItems().get(productId);int newQty=quantity+(old==null?0:old.getQuantity());
  if(newQty>10)throw new IllegalArgumentException("Maximum 10 units per product");
  if(newQty>p.getStock())throw new IllegalArgumentException("Quantity exceeds available stock");
  if(old==null&&c.getItems().size()>=20)throw new IllegalStateException("Maximum 20 distinct products in cart");
  c.put(productId,newQty);
 }
 public void remove(String customerId,String productId){getCart(customerId).remove(productId);}
 public void validateAndRefresh(String customerId){
  Cart c=getCart(customerId);
  for(CartItem i:c.getItems().values()){Product p=catalog.get(i.getProductId());if(i.getQuantity()>p.getStock())throw new IllegalStateException("Insufficient stock for "+p.getName());}
 }
}
