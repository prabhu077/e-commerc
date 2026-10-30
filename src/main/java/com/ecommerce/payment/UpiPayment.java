package com.ecommerce.payment;
import com.ecommerce.model.*;
import java.math.BigDecimal;
public class UpiPayment implements PaymentMethod {
 public PaymentStatus pay(String orderId, BigDecimal amount) {
  System.out.println("Simulating UPI payment for Rs. " + amount);
  return PaymentStatus.PAID;
 }
 public PaymentMethodType type(){return PaymentMethodType.UPI;}
}
