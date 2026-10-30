package com.ecommerce.model;
import java.math.BigDecimal;
public class Product {
    private final String id, name, sellerId;
    private Category category;
    private BigDecimal price;
    private int stock;
    private boolean active=true;
    public Product(String id,String name,Category category,String sellerId,BigDecimal price,int stock){
        if(name==null||name.isBlank()||category==null||sellerId==null||price==null||price.compareTo(BigDecimal.ZERO)<=0||stock<0) throw new IllegalArgumentException("Invalid product details");
        this.id=id;this.name=name;this.category=category;this.sellerId=sellerId;this.price=price;this.stock=stock;
    }
    public String getId(){return id;} public String getName(){return name;} public Category getCategory(){return category;}
    public String getSellerId(){return sellerId;} public BigDecimal getPrice(){return price;} public int getStock(){return stock;}
    public boolean isActive(){return active;} public boolean isOutOfStock(){return stock==0;}
    public void changePrice(BigDecimal p){if(p==null||p.compareTo(BigDecimal.ZERO)<=0)throw new IllegalArgumentException("Price must be > 0");price=p;}
    public void changeCategory(Category c){category=c;} public void reduceStock(int q){if(q<=0||q>stock)throw new IllegalArgumentException("Insufficient stock");stock-=q;}
    public void restoreStock(int q){if(q<=0)throw new IllegalArgumentException("Invalid quantity");stock+=q;}
    public void setActive(boolean active){this.active=active;}
    public String toString(){return id+" | "+name+" | "+category+" | Rs."+price+" | stock="+stock+(active?"":" | INACTIVE")+(stock==0?" | OUT OF STOCK":"");}
}
