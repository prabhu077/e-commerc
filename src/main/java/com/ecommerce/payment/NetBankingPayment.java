package com.ecommerce.payment;
import com.ecommerce.model.*;
import java.math.BigDecimal;
public class NetBankingPayment implements PaymentMethod {
 public PaymentStatus pay(String orderId, BigDecimal amount) {
  System.out.println("Simulating NET_BANKING payment for Rs. " + amount);
  return PaymentStatus.PAID;
 }
 public PaymentMethodType type(){return PaymentMethodType.NET_BANKING;}
}
