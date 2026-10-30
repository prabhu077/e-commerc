package com.ecommerce.payment;
import com.ecommerce.model.*;
public interface PaymentMethod {
 PaymentStatus pay(String orderId, java.math.BigDecimal amount);
 PaymentMethodType type();
}
