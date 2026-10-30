package com.ecommerce.service;
import com.ecommerce.model.*;import java.math.BigDecimal;import java.util.*;
public class CatalogService {
 private final Store db;public CatalogService(Store db){this.db=db;}
 public Product addProduct(User actor,String id,String name,Category category,String sellerId,BigDecimal price,int stock){
  if(!(actor instanceof Admin)&&!(actor instanceof Seller))throw new SecurityException("Only Admin or Seller can add products");
  if(actor instanceof Seller&&!actor.getId().equals(sellerId))throw new SecurityException("Seller can add products only for themselves");
  if(!(db.users.get(sellerId) instanceof Seller))throw new IllegalArgumentException("Seller not found");
  Product p=new Product(id,name,category,sellerId,price,stock);db.products.put(id,p);return p;
 }
 public List<Product> listProducts(){return db.products.values().stream().filter(Product::isActive).toList();}
 public void changePrice(String productId,BigDecimal price){get(productId).changePrice(price);}
 public void changeCategory(String productId,Category category){
  Product p=get(productId);
  boolean ordered=db.orders.values().stream().flatMap(o->o.getItems().stream()).anyMatch(i->i.getProductId().equals(productId));
  if(ordered)throw new IllegalStateException("Category cannot change after an order has been placed");
  p.changeCategory(category);
 }
 public void deactivate(String productId){get(productId).setActive(false);}
 public Product get(String id){Product p=db.products.get(id);if(p==null||!p.isActive())throw new IllegalArgumentException("Product not found/ inactive");return p;}
}
