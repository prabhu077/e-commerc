package com.ecommerce.model;
import java.math.BigDecimal;import java.time.LocalDateTime;import java.util.*;
public class Order {
 private final String id,customerId;private final List<OrderItem> items;private final List<Shipment> shipments=new ArrayList<>();
 private final BigDecimal itemTotal,deliveryCharge,couponDiscount,total;private final String couponCode;private OrderStatus status=OrderStatus.PLACED;
 private final LocalDateTime createdAt;private LocalDateTime deliveredAt;
 public Order(String id,String customerId,List<OrderItem> items,BigDecimal itemTotal,BigDecimal delivery,BigDecimal discount,String coupon){
  this.id=id;this.customerId=customerId;this.items=new ArrayList<>(items);this.itemTotal=itemTotal;this.deliveryCharge=delivery;this.couponDiscount=discount;this.total=itemTotal.add(delivery).subtract(discount);this.couponCode=coupon;this.createdAt=LocalDateTime.now();
 }
 public void addShipment(Shipment s){shipments.add(s);}public String getId(){return id;}public String getCustomerId(){return customerId;}
 public List<OrderItem> getItems(){return Collections.unmodifiableList(items);}public List<Shipment> getShipments(){return Collections.unmodifiableList(shipments);}
 public BigDecimal getItemTotal(){return itemTotal;}public BigDecimal getDeliveryCharge(){return deliveryCharge;}public BigDecimal getCouponDiscount(){return couponDiscount;}
 public BigDecimal getTotal(){return total;}public String getCouponCode(){return couponCode;}public OrderStatus getStatus(){return status;}
 public LocalDateTime getCreatedAt(){return createdAt;}public LocalDateTime getDeliveredAt(){return deliveredAt;}
 public void refreshStatus(){
  if(shipments.stream().allMatch(s->s.getStatus()==OrderStatus.DELIVERED)){status=OrderStatus.DELIVERED;deliveredAt=LocalDateTime.now();}
  else if(shipments.stream().anyMatch(s->s.getStatus()==OrderStatus.OUT_FOR_DELIVERY))status=OrderStatus.OUT_FOR_DELIVERY;
  else if(shipments.stream().anyMatch(s->s.getStatus()==OrderStatus.SHIPPED))status=OrderStatus.SHIPPED;
  else if(shipments.stream().anyMatch(s->s.getStatus()==OrderStatus.PACKED))status=OrderStatus.PACKED;
 }
 public void cancel(){if(status==OrderStatus.SHIPPED||status==OrderStatus.OUT_FOR_DELIVERY||status==OrderStatus.DELIVERED)throw new IllegalStateException("Cannot cancel after shipping");status=OrderStatus.CANCELLED;}
}
