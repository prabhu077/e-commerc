package com.ecommerce.service;
import com.ecommerce.model.*;import com.ecommerce.payment.PaymentMethod;import java.math.BigDecimal;import java.util.*;import java.util.stream.Collectors;
public class OrderService {
 private final Store db;private final CartService carts;private final CatalogService catalog;private final PaymentService payments;private final CouponService coupons;
 public OrderService(Store db,CartService carts,CatalogService catalog,PaymentService payments,CouponService coupons){this.db=db;this.carts=carts;this.catalog=catalog;this.payments=payments;this.coupons=coupons;}
 public Order checkout(String customerId,PaymentMethod method,String couponCode){
  Cart cart=carts.getCart(customerId);if(cart.getItems().isEmpty())throw new IllegalStateException("Cart is empty");
  carts.validateAndRefresh(customerId);
  List<OrderItem> items=new ArrayList<>();BigDecimal subtotal=BigDecimal.ZERO;
  for(CartItem ci:cart.getItems().values()){Product p=catalog.get(ci.getProductId());items.add(new OrderItem(p,ci.getQuantity()));subtotal=subtotal.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));}
  BigDecimal delivery=subtotal.compareTo(new BigDecimal("500"))<0?new BigDecimal("50"):BigDecimal.ZERO;
  BigDecimal discount=coupons.discount(couponCode,customerId,subtotal);
  BigDecimal total=subtotal.add(delivery).subtract(discount);
  String orderId="ORD-"+UUID.randomUUID();
  // Online payment must succeed before stock is decremented or order created.
  Payment payment=payments.process(orderId,total,method);
  if(payment.getStatus()==PaymentStatus.FAILED)throw new IllegalStateException("Payment failed; order not placed and stock unchanged");
  Order order=new Order(orderId,customerId,items,subtotal,delivery,discount,couponCode);
  Map<String,List<String>> sellerProducts=items.stream().collect(Collectors.groupingBy(OrderItem::getSellerId,LinkedHashMap::new,Collectors.mapping(OrderItem::getProductId,Collectors.toList())));
  for(var entry:sellerProducts.entrySet())order.addShipment(new Shipment("SHP-"+UUID.randomUUID(),entry.getKey(),entry.getValue()));
  // Recheck and decrement stock only after successful payment.
  for(OrderItem item:items){Product p=catalog.get(item.getProductId());if(p.getStock()<item.getQuantity()){payment.setStatus(PaymentStatus.REFUNDED);throw new IllegalStateException("Stock changed during checkout; payment marked for refund");}}
  for(OrderItem item:items)catalog.get(item.getProductId()).reduceStock(item.getQuantity());
  db.orders.put(orderId,order);coupons.markUsed(couponCode,customerId);cart.clear();return order;
 }
 public void cancel(String customerId,String orderId){
  Order o=getOwned(customerId,orderId);if(o.getStatus()==OrderStatus.SHIPPED||o.getStatus()==OrderStatus.OUT_FOR_DELIVERY||o.getStatus()==OrderStatus.DELIVERED)throw new IllegalStateException("Cannot cancel after shipment");
  o.cancel();for(OrderItem i:o.getItems())db.products.get(i.getProductId()).restoreStock(i.getQuantity());
  Payment p=db.payments.values().stream().filter(x->x.getOrderId().equals(orderId)).findFirst().orElse(null);
  if(p!=null&&p.getMethod()!=PaymentMethodType.COD)payments.refund(orderId);
 }
 public Order getOwned(String customerId,String orderId){Order o=db.orders.get(orderId);if(o==null||!o.getCustomerId().equals(customerId))throw new IllegalArgumentException("Order not found");return o;}
 public void advanceShipment(String orderId,String shipmentId){
  Order o=db.orders.get(orderId);if(o==null)throw new IllegalArgumentException("Order not found");
  Shipment s=o.getShipments().stream().filter(x->x.getId().equals(shipmentId)).findFirst().orElseThrow();
  s.advance();o.refreshStatus();
 }
}
