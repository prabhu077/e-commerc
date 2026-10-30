package com.ecommerce.payment;
import com.ecommerce.model.*;import java.math.BigDecimal;
public class CodPayment implements PaymentMethod {
 public PaymentStatus pay(String orderId,BigDecimal amount){return PaymentStatus.PENDING;}
 public PaymentMethodType type(){return PaymentMethodType.COD;}
}
