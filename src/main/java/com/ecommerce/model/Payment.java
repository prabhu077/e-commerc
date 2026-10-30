package com.ecommerce.model;
import java.math.BigDecimal;import java.time.LocalDateTime;
public class Payment {
 private final String id,orderId;private final BigDecimal amount;private final LocalDateTime date;private final PaymentMethodType method;
 private PaymentStatus status;
 public Payment(String id,String orderId,BigDecimal amount,PaymentMethodType method,PaymentStatus status){this.id=id;this.orderId=orderId;this.amount=amount;this.method=method;this.status=status;this.date=LocalDateTime.now();}
 public String getId(){return id;}public String getOrderId(){return orderId;}public BigDecimal getAmount(){return amount;}public LocalDateTime getDate(){return date;}
 public PaymentMethodType getMethod(){return method;}public PaymentStatus getStatus(){return status;}public void setStatus(PaymentStatus s){status=s;}
}
