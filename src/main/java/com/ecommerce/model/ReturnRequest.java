package com.ecommerce.model;
import java.math.BigDecimal;import java.time.LocalDateTime;
public class ReturnRequest {
 private final String id,orderId,customerId,productId;private final int quantity;private final BigDecimal refundAmount;private final LocalDateTime requestedAt;
 private boolean approved;
 public ReturnRequest(String id,String orderId,String customerId,String productId,int quantity,BigDecimal refund){this.id=id;this.orderId=orderId;this.customerId=customerId;this.productId=productId;this.quantity=quantity;this.refundAmount=refund;this.requestedAt=LocalDateTime.now();}
 public String getId(){return id;}public String getOrderId(){return orderId;}public String getCustomerId(){return customerId;}public String getProductId(){return productId;}
 public int getQuantity(){return quantity;}public BigDecimal getRefundAmount(){return refundAmount;}public LocalDateTime getRequestedAt(){return requestedAt;}
 public boolean isApproved(){return approved;}public void approve(){approved=true;}
}
