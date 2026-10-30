package com.ecommerce.model;
import java.util.*;
public class Cart {
    private final String customerId; private final Map<String,CartItem> items=new LinkedHashMap<>();
    public Cart(String customerId){this.customerId=customerId;}
    public String getCustomerId(){return customerId;} public Map<String,CartItem> getItems(){return Collections.unmodifiableMap(items);}
    public void put(String productId,int qty){items.put(productId,new CartItem(productId,qty));}
    public void remove(String productId){items.remove(productId);} public void clear(){items.clear();}
}
