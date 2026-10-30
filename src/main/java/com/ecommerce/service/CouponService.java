package com.ecommerce.service;
import com.ecommerce.model.*;import java.math.BigDecimal;import java.time.LocalDate;import java.util.Set;
public class CouponService {
 private final Store db;public CouponService(Store db){this.db=db;}
 public void add(Coupon c){db.coupons.put(c.getCode().toUpperCase(),c);}
 public BigDecimal discount(String code,String customerId,BigDecimal subtotal){
  if(code==null||code.isBlank())return BigDecimal.ZERO;
  Coupon c=db.coupons.get(code.toUpperCase());if(c==null)throw new IllegalArgumentException("Coupon not found");
  if(c.getExpiry().isBefore(LocalDate.now()))throw new IllegalArgumentException("Coupon expired");
  if(subtotal.compareTo(c.getMinOrder())<0)throw new IllegalArgumentException("Minimum order value not met");
  Set<String> used=db.couponUses.getOrDefault(c.getCode().toUpperCase(),java.util.Set.of());
  if(used.contains(customerId))throw new IllegalArgumentException("Customer already used this coupon");
  return c.calculate(subtotal);
 }
 public void markUsed(String code,String customerId){if(code!=null&&!code.isBlank())db.couponUses.computeIfAbsent(code.toUpperCase(),k->new java.util.HashSet<>()).add(customerId);}
}
