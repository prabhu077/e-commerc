package com.ecommerce.model;
import java.math.BigDecimal;import java.time.LocalDate;
public class Coupon {
 private final String code;private final CouponType type;private final BigDecimal discount,maxDiscount,minOrder;private final LocalDate expiry;
 public Coupon(String code,CouponType type,BigDecimal discount,BigDecimal maxDiscount,BigDecimal minOrder,LocalDate expiry){
  this.code=code;this.type=type;this.discount=discount;this.maxDiscount=maxDiscount;this.minOrder=minOrder;this.expiry=expiry;
 }
 public String getCode(){return code;} public LocalDate getExpiry(){return expiry;} public BigDecimal getMinOrder(){return minOrder;}
 public BigDecimal calculate(BigDecimal subtotal){
  if(type==CouponType.FLAT)return discount.min(maxDiscount).min(subtotal);
  return subtotal.multiply(discount).divide(new BigDecimal("100"),2,java.math.RoundingMode.HALF_UP).min(maxDiscount).min(subtotal);
 }
}
