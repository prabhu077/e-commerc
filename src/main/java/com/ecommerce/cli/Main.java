package com.ecommerce.cli;

import com.ecommerce.model.*;
import com.ecommerce.payment.*;
import com.ecommerce.service.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
 public static void main(String[] args) {
  Store db=new Store();
  Address address=new Address("ADDR1","12 Main Road","Chennai","Tamil Nadu","600001");
  Customer customer=new Customer("CUST1","Prabhu","prabhu@example.com","9000000001",address);
  Seller seller1=new Seller("SELLER1","Tech Store","tech@example.com","9000000002");
  Seller seller2=new Seller("SELLER2","Home Store","home@example.com","9000000003");
  Admin admin=new Admin("ADMIN1","Admin","admin@example.com","9000000000");
  db.users.put(customer.getId(),customer);db.users.put(seller1.getId(),seller1);db.users.put(seller2.getId(),seller2);db.users.put(admin.getId(),admin);
  CatalogService catalog=new CatalogService(db);
  catalog.addProduct(admin,"P100","Wireless Mouse",Category.ELECTRONICS,"SELLER1",new BigDecimal("450"),20);
  catalog.addProduct(admin,"P101","Java Programming Book",Category.BOOKS,"SELLER1",new BigDecimal("600"),15);
  catalog.addProduct(admin,"P102","Storage Box",Category.HOME,"SELLER2",new BigDecimal("250"),30);
  catalog.addProduct(admin,"P103","Rice Bag",Category.GROCERY,"SELLER2",new BigDecimal("300"),25);
  CartService cartService=new CartService(db,catalog);
  PaymentService paymentService=new PaymentService(db);
  CouponService couponService=new CouponService(db);
  couponService.add(new Coupon("SAVE10",CouponType.PERCENTAGE,new BigDecimal("10"),new BigDecimal("100"),new BigDecimal("500"),LocalDate.now().plusDays(30)));
  OrderService orderService=new OrderService(db,cartService,catalog,paymentService,couponService);
  ReturnService returnService=new ReturnService(db,paymentService);
  ReviewService reviewService=new ReviewService(db);

  try(Scanner sc=new Scanner(System.in)){
   while(true){
    System.out.println("\n========== E-COMMERCE SHOPPING APP ==========");
    System.out.println("1. List products");
    System.out.println("2. Add product (Admin/Seller demo)");
    System.out.println("3. Add product to cart");
    System.out.println("4. View cart");
    System.out.println("5. Checkout / Place order");
    System.out.println("6. View order");
    System.out.println("7. Advance shipment status");
    System.out.println("8. Cancel order");
    System.out.println("9. Request return");
    System.out.println("10. Approve return (Admin/Seller demo)");
    System.out.println("11. Add product review");
    System.out.println("0. Exit");
    System.out.print("Choose: ");
    String input=sc.nextLine().trim();
    try {
     switch(input){
      case "1" -> catalog.listProducts().forEach(System.out::println);
      case "2" -> {
       System.out.print("Actor ID (ADMIN1/SELLER1/SELLER2): ");User actor=db.users.get(sc.nextLine().trim());
       System.out.print("Product ID: ");String id=sc.nextLine().trim();
       System.out.print("Name: ");String name=sc.nextLine().trim();
       System.out.println("Category: 1 ELECTRONICS, 2 FASHION, 3 HOME, 4 BOOKS, 5 GROCERY");
       Category cat=Category.values()[Integer.parseInt(sc.nextLine().trim())-1];
       System.out.print("Seller ID: ");String sid=sc.nextLine().trim();
       System.out.print("Price: ");BigDecimal price=new BigDecimal(sc.nextLine().trim());
       System.out.print("Stock: ");int stock=Integer.parseInt(sc.nextLine().trim());
       System.out.println("Created: "+catalog.addProduct(actor,id,name,cat,sid,price,stock));
      }
      case "3" -> {
       System.out.print("Customer ID (CUST1): ");String cid=sc.nextLine().trim();
       System.out.print("Product ID: ");String pid=sc.nextLine().trim();
       System.out.print("Quantity: ");int qty=Integer.parseInt(sc.nextLine().trim());
       cartService.add(cid,pid,qty);System.out.println("Added to cart.");
      }
      case "4" -> {
       System.out.print("Customer ID: ");Cart c=cartService.getCart(sc.nextLine().trim());
       c.getItems().values().forEach(i->{Product p=db.products.get(i.getProductId());System.out.println(p.getName()+" x "+i.getQuantity()+" current price Rs."+p.getPrice());});
      }
      case "5" -> {
       System.out.print("Customer ID: ");String cid=sc.nextLine().trim();
       System.out.println("Payment: 1 Card, 2 UPI, 3 Net Banking, 4 COD");
       int m=Integer.parseInt(sc.nextLine().trim());
       PaymentMethod method=switch(m){case 1->new CardPayment();case 2->new UpiPayment();case 3->new NetBankingPayment();case 4->new CodPayment();default->throw new IllegalArgumentException("Invalid method");};
       System.out.print("Coupon code (blank for none): ");String code=sc.nextLine().trim();
       Order o=orderService.checkout(cid,method,code);
       System.out.println("ORDER PLACED: "+o.getId()+" | total Rs."+o.getTotal()+" | status "+o.getStatus());
       o.getShipments().forEach(s->System.out.println("Shipment "+s.getId()+" seller="+s.getSellerId()));
      }
      case "6" -> {
       System.out.print("Order ID: ");String oid=sc.nextLine().trim();Order o=db.orders.get(oid);
       if(o==null)throw new IllegalArgumentException("Order not found");
       System.out.println("Order "+o.getId()+" status="+o.getStatus()+" total=Rs."+o.getTotal());
       o.getItems().forEach(i->System.out.println(i.getProductName()+" x"+i.getQuantity()+" @ Rs."+i.getUnitPrice()));
       o.getShipments().forEach(s->System.out.println(s.getId()+" "+s.getSellerId()+" "+s.getStatus()));
      }
      case "7" -> {
       System.out.print("Order ID: ");String oid=sc.nextLine().trim();Order o=db.orders.get(oid);
       if(o==null)throw new IllegalArgumentException("Order not found");
       for(int i=0;i<o.getShipments().size();i++)System.out.println((i+1)+". "+o.getShipments().get(i).getId()+" "+o.getShipments().get(i).getStatus());
       System.out.print("Shipment number: ");int n=Integer.parseInt(sc.nextLine().trim())-1;
       orderService.advanceShipment(oid,o.getShipments().get(n).getId());System.out.println("Updated order status: "+o.getStatus());
      }
      case "8" -> {System.out.print("Customer ID: ");String cid=sc.nextLine().trim();System.out.print("Order ID: ");String oid=sc.nextLine().trim();orderService.cancel(cid,oid);System.out.println("Order cancelled; stock restored.");}
      case "9" -> {System.out.print("Customer ID: ");String cid=sc.nextLine().trim();System.out.print("Order ID: ");String oid=sc.nextLine().trim();System.out.print("Product ID: ");String pid=sc.nextLine().trim();System.out.print("Quantity: ");int q=Integer.parseInt(sc.nextLine().trim());ReturnRequest r=returnService.request(cid,oid,pid,q);System.out.println("Return requested: "+r.getId()+" refund estimate Rs."+r.getRefundAmount());}
      case "10" -> {System.out.print("Return ID: ");String rid=sc.nextLine().trim();returnService.approve(rid);System.out.println("Return approved; stock restored and payment marked refunded.");}
      case "11" -> {System.out.print("Customer ID: ");String cid=sc.nextLine().trim();System.out.print("Order ID: ");String oid=sc.nextLine().trim();System.out.print("Product ID: ");String pid=sc.nextLine().trim();System.out.print("Rating 1-5: ");int rating=Integer.parseInt(sc.nextLine().trim());System.out.print("Comment: ");String comment=sc.nextLine();Review r=reviewService.add(cid,oid,pid,rating,comment);System.out.println("Review saved: "+r.getId()+"; admin flag="+reviewService.isFlaggedForAdmin(pid));}
      case "0" -> {System.out.println("Goodbye.");return;}
      default -> System.out.println("Invalid option.");
     }
    } catch(Exception e){System.out.println("ERROR: "+e.getMessage());}
   }
  }
 }
}
