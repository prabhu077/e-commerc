package com.ecommerce.model;
import java.util.*;
public class Shipment {
 private final String id,sellerId;private final List<String> productIds;private OrderStatus status=OrderStatus.PLACED;
 public Shipment(String id,String sellerId,List<String> productIds){this.id=id;this.sellerId=sellerId;this.productIds=new ArrayList<>(productIds);}
 public String getId(){return id;}public String getSellerId(){return sellerId;}public List<String> getProductIds(){return Collections.unmodifiableList(productIds);}
 public OrderStatus getStatus(){return status;}
 public void advance(){switch(status){case PLACED->status=OrderStatus.PACKED;case PACKED->status=OrderStatus.SHIPPED;case SHIPPED->status=OrderStatus.OUT_FOR_DELIVERY;case OUT_FOR_DELIVERY->status=OrderStatus.DELIVERED;default->throw new IllegalStateException("Shipment cannot advance from "+status);}}
}
