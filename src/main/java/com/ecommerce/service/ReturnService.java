package com.ecommerce.service;
import com.ecommerce.model.*;import java.math.BigDecimal;import java.time.Duration;import java.util.UUID;
public class ReturnService {
 private final Store db;private final PaymentService payments;public ReturnService(Store db,PaymentService payments){this.db=db;this.payments=payments;}
 public ReturnRequest request(String customerId,String orderId,String productId,int qty){
  Order o=db.orders.get(orderId);if(o==null||!o.getCustomerId().equals(customerId)||o.getStatus()!=OrderStatus.DELIVERED)throw new IllegalStateException("Return requires customer's delivered order");
  if(o.getDeliveredAt()==null||Duration.between(o.getDeliveredAt(),java.time.LocalDateTime.now()).toDays()>7)throw new IllegalStateException("Return window is 7 days");
  OrderItem item=o.getItems().stream().filter(i->i.getProductId().equals(productId)).findFirst().orElseThrow();
  if(item.getCategory()==Category.GROCERY)throw new IllegalArgumentException("Grocery products cannot be returned");
  if(qty<1||qty>item.getQuantity())throw new IllegalArgumentException("Invalid return quantity");
  BigDecimal refund=item.getUnitPrice().multiply(BigDecimal.valueOf(qty));
  ReturnRequest r=new ReturnRequest("RET-"+UUID.randomUUID(),orderId,customerId,productId,qty,refund);db.returns.put(r.getId(),r);return r;
 }
 public void approve(String returnId){
  ReturnRequest r=db.returns.get(returnId);if(r==null)throw new IllegalArgumentException("Return request not found");
  if(r.isApproved())throw new IllegalStateException("Already approved");
  r.approve();db.products.get(r.getProductId()).restoreStock(r.getQuantity());
  // Full item amount refunded; delivery fee is intentionally excluded.
  payments.refund(r.getOrderId());
 }
}
