package com.ecommerce.service;
import com.ecommerce.model.*;import com.ecommerce.payment.PaymentMethod;import java.math.BigDecimal;import java.util.UUID;
public class PaymentService {
 private final Store db;public PaymentService(Store db){this.db=db;}
 public Payment process(String orderId,BigDecimal amount,PaymentMethod method){
  if(method.type()==PaymentMethodType.COD&&amount.compareTo(new BigDecimal("5000"))>0)throw new IllegalArgumentException("COD allowed only up to Rs.5000");
  PaymentStatus status=method.pay(orderId,amount);
  Payment p=new Payment(UUID.randomUUID().toString(),orderId,amount,method.type(),status);db.payments.put(p.getId(),p);
  return p;
 }
 public void refund(String orderId){db.payments.values().stream().filter(p->p.getOrderId().equals(orderId)).findFirst().ifPresent(p->p.setStatus(PaymentStatus.REFUNDED));}
}
