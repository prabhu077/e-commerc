package com.ecommerce.model;
import java.math.BigDecimal;
public class OrderItem {
 private final String productId,productName,sellerId;private final Category category;private final int quantity;private final BigDecimal unitPrice;
 public OrderItem(Product p,int q){productId=p.getId();productName=p.getName();sellerId=p.getSellerId();category=p.getCategory();quantity=q;unitPrice=p.getPrice();}
 public String getProductId(){return productId;}public String getProductName(){return productName;}public String getSellerId(){return sellerId;}
 public Category getCategory(){return category;}public int getQuantity(){return quantity;}public BigDecimal getUnitPrice(){return unitPrice;}
 public BigDecimal lineTotal(){return unitPrice.multiply(BigDecimal.valueOf(quantity));}
}
