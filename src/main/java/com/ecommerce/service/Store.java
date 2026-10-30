package com.ecommerce.service;
import com.ecommerce.model.*;import java.util.*;
public class Store {
 public final Map<String,User> users=new LinkedHashMap<>();
 public final Map<String,Product> products=new LinkedHashMap<>();
 public final Map<String,Cart> carts=new LinkedHashMap<>();
 public final Map<String,Order> orders=new LinkedHashMap<>();
 public final Map<String,Payment> payments=new LinkedHashMap<>();
 public final Map<String,Coupon> coupons=new LinkedHashMap<>();
 public final Map<String,Set<String>> couponUses=new HashMap<>();
 public final Map<String,ReturnRequest> returns=new LinkedHashMap<>();
 public final List<Review> reviews=new ArrayList<>();
}
